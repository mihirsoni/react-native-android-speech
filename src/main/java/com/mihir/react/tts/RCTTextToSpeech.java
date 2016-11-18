package com.mihir.react.tts;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;


import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;


import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


/**
 * Created by mihir on 11/4/15.
 */
public class RCTTextToSpeech extends ReactContextBaseJavaModule{

    private static TextToSpeech tts;

    public RCTTextToSpeech(ReactApplicationContext reactContext) {
        super(reactContext);
        init();
    }
    /***
     * This method will expose all the available languages in TTS engine
     * @param callback
     */
    @ReactMethod
    public void getLocale(final Callback callback) {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                try{
                    if(tts == null){
                        init();
                    }
                    Locale[] locales = Locale.getAvailableLocales();
                    WritableArray data = Arguments.createArray();
                    for (Locale locale : locales) {
                        int res = tts.isLanguageAvailable(locale);
                        if(res == TextToSpeech.LANG_COUNTRY_AVAILABLE){
                            data.pushString(locale.getLanguage());
                        }
                    }
                    callback.invoke(null,data);
                } catch (Exception e) {
                    callback.invoke(ErrorUtils.getError(null,e.getMessage()),null);
                }
            }
        }.execute();
    }

    @ReactMethod
    public void isSpeaking(final Callback callback){
        new GuardedAsyncTask<Void,Void>(getReactApplicationContext()){
            @Override
            protected  void doInBackgroundGuarded(Void... params){
                try {
                    if (tts.isSpeaking()) {
                        callback.invoke(null,true);
                    } else {
                        callback.invoke(null,false);
                    }
                } catch (Exception e){
                    callback.invoke(ErrorUtils.getError(null,e.getMessage()),null);
                }
            }
        }.execute();
    }
    public void init(){
        tts = new TextToSpeech(getReactApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.ERROR){
                    FLog.e(ReactConstants.TAG,"Not able to initialized the TTS object");
                }
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onDone(String utteranceId) {
                WritableMap map = Arguments.createMap();
                map.putString("utteranceId", utteranceId);
                getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)
                    .emit("FinishSpeechUtterance", map);
            }

            @Override
            public void onError(String utteranceId) {
                WritableMap map = Arguments.createMap();
                map.putString("utteranceId", utteranceId);
                getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)
                    .emit("ErrorSpeechUtterance", map);
            }

            @Override
            public void onStart(String utteranceId) {
                WritableMap map = Arguments.createMap();
                map.putString("utteranceId", utteranceId);
                getReactApplicationContext().getJSModule(RCTDeviceEventEmitter.class)
                    .emit("StartSpeechUtterance", map);
            }
        });
    }

    @ReactMethod
    public void stop(final Callback callback){
        new GuardedAsyncTask<Void,Void>(getReactApplicationContext()){
            @Override
            protected  void doInBackgroundGuarded(Void... params){
                try{
                    tts.stop();
                    callback.invoke(null,true);

                } catch (Exception e){
                    callback.invoke(ErrorUtils.getError(null,e.getMessage()),null);
                }
            }
        }.execute();
    }

    @ReactMethod
    public void shutDown(final Callback callBack){
        new GuardedAsyncTask<Void,Void>(getReactApplicationContext()){
            @Override
            protected  void doInBackgroundGuarded(Void... params){
                if(tts == null) {
                    callBack.invoke(true);
                }
                try{
                    tts.shutdown();
                    callBack.invoke(null,true);
                } catch (Exception e){
                    callBack.invoke(ErrorUtils.getError(null,e.getMessage()),null);
                }
            }
        }.execute();
    }

    @Override
    public String getName() {
        return "AndroidTTS";
    }

    @ReactMethod
    public void speak(final ReadableMap args,final  Callback callback) {
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                if(tts == null){
                    init();
                }
                String text = args.hasKey("text") ? args.getString("text") : null;
                String language = args.hasKey("language") ? args.getString("language") : null;
                Boolean forceStop = args.hasKey("forceStop") ?  args.getBoolean("forceStop") : null;
                String country = args.hasKey("country") ? args.getString("country") : null;
                Float pitch = args.hasKey("pitch") ? (float)  args.getDouble("pitch") : null;
                if(tts.isSpeaking()){
                    //Force to stop and start new speech
                    if(forceStop != null && forceStop){
                        tts.stop();
                    } else {
                        callback.invoke(ErrorUtils.getError(null,"TTS is already speaking something , Please shutdown or stop  TTS and try again"),null);
                        return;
                    }
                }
                if(args.getString("text") == null || text == ""){
                    callback.invoke(ErrorUtils.getError(null,"t can not be blank"),null);
                    return;
                }
                try {
                    if (language != null && language != "") {
                        if (country != null && country != "") {
                            tts.setLanguage(new Locale(language,country));
                        }else {
                            tts.setLanguage(new Locale(language));
                        }
                    } else {
                        //Setting up default language
                        tts.setLanguage(new Locale("en"));
                    }
                    //Set the pitch if provided by the user
                    if(pitch != null){
                        tts.setPitch(pitch);
                    }
                    //TODO:: Need to give the callback
                    int speakResult = 0;
                    if(Build.VERSION.SDK_INT >= 21) {
                        Bundle bundle = new Bundle();
                        bundle.putCharSequence(Engine.KEY_PARAM_UTTERANCE_ID, "");
                        speakResult = tts.speak(text, TextToSpeech.QUEUE_FLUSH, bundle, UUID.randomUUID().toString());
                    } else {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(Engine.KEY_PARAM_UTTERANCE_ID, UUID.randomUUID().toString());
                        speakResult = tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
                    }

                    if(speakResult < 0)
                        throw new Exception("Speak failed, make sure that TTS service is installed on you device");

                    callback.invoke(null,true);
                } catch (Exception e) {
                    callback.invoke(ErrorUtils.getError(null,e.getMessage()),null);
                }
            }
        }.execute();
    }
}
