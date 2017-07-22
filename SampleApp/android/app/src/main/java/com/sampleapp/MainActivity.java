package com.sampleapp;

import com.sampleapp.packages.*;

import com.facebook.react.ReactActivity;
import java.util.Arrays;
import java.util.List;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends ReactActivity {

      /**
       * Returns the name of the main component registered from JavaScript.
       * This is used to schedule rendering of the component.
       */
      @Override
      protected String getMainComponentName() {
          return "SampleApp";
      }

      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          //Check if permission enabled
          if (UsageStatsModule.getUsageStatsList(this).isEmpty()){
              Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
              startActivity(intent);
          }
      }
}
