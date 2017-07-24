React Native Android Text To Speech

A react native android wrapper for Text To speech

## Purpose 

Currently , React Native doesn't have inbuilt support for TextToSpeech library for Android.

But with help of exposing module in react-native , this wrapper will solve the problem for TTS.

## Setup

* Install Module

```bash
npm install --save react-native-android-speech
```

* `android/settings.gradle`

```gradle
...
include ':react-native-android-speech'
project(':react-native-android-speech').projectDir = new File(settingsDir, '../node_modules/react-native-android-speech')
```

* `android/app/build.gradle`

```gradle
dependencies {
	...
	compile project(':react-native-android-speech')
}
```
* for React Native Version >= 0.29, register module (in android/app/src/main/java/com/[app name]/MainApplication.java)
```java
...
import com.mihir.react.tts.*; // Import package

public class MainApplication extends Application implements ReactApplication {
	...
	
    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          ...,
          new RCTTextToSpeechModule()
      );
    }
}
```

* for React Native Version < 0.29, register module (in android/app/src/main/java/com/[app name]/MainActivity.java)

```java
...
import com.mihir.react.tts.*; // Import package

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
	...
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactRootView = new ReactRootView(this);

        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .addPackage(new MainReactPackage())
                .addPackage(new RCTTextToSpeechModule())           // Add any extra modules here
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();

        mReactRootView.startReactApplication(mReactInstanceManager, "YourProject", null);

        setContentView(mReactRootView);
    }
}
```

## Usage

Currently there following functionality available. All below functions return Promise , with proper error codes.

- getLocales
- checkLanguageAvailability(language)
- downloadTTSVoice()
- speak(args)
- isSpeaking
- shutDown
- stop

### Importing module


```js
var tts = require('react-native-android-speech')

```

### getLocales()
Returns all available langauges from TTS make sure that exists in device also.

## Example 

```js
tts.getLocales().then(locales=>{
    console.log(locales)
});

```

### checkLanguageAvailability(language)
returns whether a language is available on the device TTS engine

## Example 

```js
tts.checkLanguageAvailability('ko').then(result => {
    console.log('TTS voice availability for Korean is '+result);
});

```

### downloadTTSVoice()
opens the TTS download page on the device

## Example 

```js
tts.checkLanguageAvailability('ko').then(result => {
    if (!result) {
        downloadTTSVoice();
    }
});

```

### speak()

## Example

```js
tts.speak({
    text:'Please provide some text to speak.', // Mandatory
    pitch:1.5, // Optional Parameter to set the pitch of Speech,
    forceStop : false , //  Optional Parameter if true , it will stop TTS if it is already in process
    language : 'en', // Optional Paramenter Default is en you can provide any supported lang by TTS
    country : 'US' // Optional Paramenter Default is null, it provoques that system selects its default
}).then(isSpeaking=>{
    //Success Callback
    console.log(isSpeaking);
}).catch(error=>{
    //Errror Callback
    console.log(error)
});

```

### isSpeaking()

This method will help to figure out whether TTS engine is currently speaking or not.

## Example

```js
tts.isSpeaking()
.then(isSpeaking=>{
    //Callback
    console.log(isSpeaking);
})
.catch(error=>{
    //if it fails 
    console.log(error)
});

```

### Stop()

Stop currently processing speech. Return True/False

## Example

```js
tts.shutDown()
.then(isStopped=>{
    console.log(isStopped);
})
.catch(error=>{
    console.log(error);
});

```

### shutDown()

Shutdown TTS Engine

## Example

```js
tts.shutDown()
.then(shutDown=>{
    //Callback 
    console.log(shutDown);
})
.catch(error=>{
    console.log(error)
});

```

## Changes
PR's are welcome

## Todo 

* [ ]  providing UternceId and callback
* [ ]  Differnt Voices
* [ ]  Custome Engines 
* [ ]  Support Speech To Text


## LICENSE

MIT
