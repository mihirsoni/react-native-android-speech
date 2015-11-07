React Native Android Text Top Speech

A react native android wrapper for Text To speech

## Purpose 

Currently , React Native doesn't have inbuilt support for TextToSpeech library for Android.

But with help of exposing module in react-native , this wrapper will solve the problem for TTS.

## Setup

* Install Module

```bash
npm install --save-dev react-native-android-speech
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

* register module (in MainActivity.java)

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

Currently there following functionality available.

-getLocales
- speak(args)
- isSpeaking
- shutDown
- stop


### Importing module


```js
var tts = require('react-native-android-speech')

```

### getLocales()
Returns all avialbale langauges in android TTS. Currently it returns Langugage Name , you might need to find code from https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes


## Example 

```js
tts.getLocales(locales=>{
    console.log(locales)
})
```
## Example 
### speak()

## Example

```js
tts.speak({
    text:'Please provide some text to speak.',
    pitch:1.5, //Optional Parameter to set the speed of Speech,
    forceStop : false , //  Optional Parameter if true , it will stop TTS if it is already in process
    language : 'en' // Default is EN you can provide any supported lang by TTS

}).then(isSpeaking=>{
    //Success Callback
    console.log(isSpeaking);
});

```

### isSpeaking()

This method will help to figure out wheter TTS engine is currently speaking or not.

## Example

```js
tts.isSpeakig().then(isSpeaking=>{
    //Callback
    console.log(isSpeaking);
});

```

### Stop()

Stop currently processing speech. Return True/False

## Example

```js
tts.shutDown().then(stop=>{
    //Callback 
    console.log(stop);
});

```

### shutDown()

Shutdown TTS Engine

## Example

```js
tts.shutDown().then(shutDown=>{
    //Callback 
    console.log(shutDown);
});

```

## Changes
PR's are welcome

## Credits
* [React Native](https://facebook.github.io/react-native/) - Awesome software.
* [React Native Android Badge](https://github.com/jhen0409/react-native-android-badge) - For showing me the light with regards to the gradle build system, as applied to react native

## LICENSE

MIT
