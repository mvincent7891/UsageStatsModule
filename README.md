# UsageStatsModule
A sample React Native application demonstrating the use of [Modules](https://facebook.github.io/react-native/docs/native-modules-android.html) to make the Android platform's [UsageStats](https://developer.android.com/reference/android/app/usage/UsageStats.html) class available from within your JavaScript code. Quickly obtain statistics on app usage over the past day, week or month.

# Setup [WIP]
Environment and sample application were setup using the React Native tutorial [here](https://facebook.github.io/react-native/docs/getting-started.html).

## Add Module
We'll start by creating the module:
```
// android/app/src/main/java/com/sampleapp/packages/UsageStatsModule.java

package com.sampleapp.packages;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class UsageStatsModule extends ReactContextBaseJavaModule {

  public UsageStatsModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "UsageStats";
  }

  ...

  @ReactMethod
  public void getSomeStats() {
      ...
    }
}

```

## Create Package
+ Create modules package
+ Add module to package

## MainApplication
+ Add modules package

## MainActivity
+ Check for permissions

## AndroidManifest.xml
+ Add permissions
```xml
<!---android/app/src/main/AndroidManifest.xml--->

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    ...
    />
    
...

<uses-permission
    android:name="android.permission.PACKAGE_USAGE_STATS"
    tools:ignore="ProtectedPermissions" />

...

```

# Todo
- [ ] Add module
- [ ] Add basic module setup instructions and examples
- [ ] Add sample React Native application using module
