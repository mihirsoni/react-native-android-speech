package com.mihir.react.tts;


import android.speech.tts.TextToSpeech;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.common.ReactConstants;

import java.util.Locale;
import java.util.Set;


/**
 * Created by mihir on 11/4/15.
 */
public class RCTTextToSpeech extends  ReactContextBaseJavaModule{

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
        FLog.i(ReactConstants.TAG, "Got the locales now trying to return ");
        new GuardedAsyncTask<Void, Void>(getReactApplicationContext()) {
            @Override
            protected void doInBackgroundGuarded(Void... params) {
                try{
                    if(tts == null){
                        init();
                    }
                    Set<Locale> availableLanguages = tts.getAvailableLanguages();
                    WritableArray data = Arguments.createArray();
                    for (Locale locale : availableLanguages) {
                        data.pushString(locale.getDisplayLanguage());
                    }
                    callback.invoke(null,data);
                } catch (Exception e) {
                    callback.invoke(e,null);
                }
            }
        }.execute();
    }

    @ReactMethod
    public void isSpeaking(final Callback successCallback){
        new GuardedAsyncTask<Void,Void>(getReactApplicationContext()){
            @Override
            protected  void doInBackgroundGuarded(Void... params){
                if(tts.isSpeaking()){
                    successCallback.invoke(true);
                } else {
                    successCallback.invoke(false);
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
    }

    @ReactMethod
    public void stop(final Callback successCallback){
        new GuardedAsyncTask<Void,Void>(getReactApplicationContext()){
            @Override
            protected  void doInBackgroundGuarded(Void... params){
                try{
                    tts.stop();
                    successCallback.invoke(true);

                } catch (Exception e){
                    tts.stop();
                    successCallback.invoke(e);
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
                    callBack.invoke(true);
                } catch (Exception e){
                    callBack.invoke(e);
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
                Float pitch = args.hasKey("pitch") ? (float)  args.getDouble("pitch") : null;
                if(tts.isSpeaking()){
                    //Force to stop and start new speech
                    if(forceStop != null && forceStop){
                        tts.stop();
                    } else {
                        callback.invoke("TTS is already speaking something , Please shutdown or stop  TTS and try again");
                        return;
                    }
                }
                if(args.getString("text") == null || text == ""){
                    callback.invoke("Text can not be blank");
                }
                try {
                    if (language != null && language != "") {
                        tts.setLanguage(new Locale(language));
                    } else {
                        //Setting up default language
                        tts.setLanguage(new Locale("en"));
                    }
                    //Set the pitch if provided by the user
                    if(pitch != null){
                        tts.setPitch(pitch);
                    }
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
                    callback.invoke(true);
                } catch (Exception e) {
                    callback.invoke(false);
                }
            }
        }.execute();
    }
}