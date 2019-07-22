# Keyguard for React Native (Android only)

A react native module to control the Keyguard.

## Setup

```
npm install react-native-keyguard --save
```

* Link the native library to your project

```
react-native link react-native-interactable
```

* Rebuild app from your project root directory

```
  react-native run-android
```

## Usage

```js
import Keyguard from 'react-native-keyguard'

/* 
Keyguard returns a promise when the lockscreen is opened.

Android does not provide Keyguard control before 21. 
The promise will be resolved by default in such cases */

Keyguard
  .unlock(title, desctiption)
  .then(() => console.log("Success"))
  .catch(error => console.log("Error",error))

```

## Manual Installation


1. update `android/settings.gradle`
2. update `android/app/build.gradle`
3. Register module in MainActivity.java or MainApplication.java (depending on your RN version)
4. Rebuild and restart package manager


* `android/settings.gradle`

```gradle
...
include ':react-native-keyguard'
project(':react-native-keyguard').projectDir = new File(settingsDir, '../node_modules/react-native-keyguard/android')
```

* `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':react-native-keyguard')
}
```  

* register module on **React Native >= 0.30** (in MainApplication.java)

```java
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.Arrays;
import java.util.List;

import me.neo.keyguard.KeyguardPackage; // <--- import

public class MainApplication extends Application implements ReactApplication {

    [...]

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    
     @Override
     protected boolean getUseDeveloperSupport() {
         return BuildConfig.DEBUG;
     }

     @Override
     protected List<ReactPackage> getPackages() {
       return Arrays.<ReactPackage>asList(
         new MainReactPackage(),
         new KeyguardPackage() // <------- add package
       );
     }
    }
}
```  

* register module on **React Native >= 0.19 and RN < 0.30** (in MainActivity.java)

```java
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;

import java.util.Arrays;
import java.util.List;

import me.neo.keyguard.KeyguardPackage; // <--- import

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "SampleAppReact";
    }

    /**
     * Returns whether dev mode should be enabled.
     * This enables e.g. the dev menu.
     */
    @Override
    protected boolean getUseDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

   /**
   * A list of packages used by the app. If the app uses additional views
   * or modules besides the default ones, add more packages here.
   */
    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new KeyguardPackage() // <------- add package
      );
    }
}
```

* register module on **React Native < 0.19** (in MainActivity.java)

```java
import me.neo.keyguard.KeyguardPackage;  // <--- import

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {

  ......
  private static Activity mActivity = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mReactRootView = new ReactRootView(this);

    mActivity = this;
    mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(getApplication())
      .setBundleAssetName("index.android.bundle")
      .setJSMainModuleName("index.android")
      .addPackage(new MainReactPackage())
      .addPackage(new KeyguardPackage())      // <------- add package
      .setUseDeveloperSupport(BuildConfig.DEBUG)
      .setInitialLifecycleState(LifecycleState.RESUMED)
      .build();

    mReactRootView.startReactApplication(mReactInstanceManager, "ExampleRN", null);

    setContentView(mReactRootView);
  }

  ......

}
```
* Run `react-native run-android` from your project root directory

