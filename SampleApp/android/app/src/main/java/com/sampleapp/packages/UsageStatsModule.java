package com.sampleapp.packages;

import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import java.text.SimpleDateFormat;

import android.util.Log;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

public class UsageStatsModule extends ReactContextBaseJavaModule {

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  public UsageStatsModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "UsageStats";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    // TODO: Add any necessary constants to the module here
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");
  public static final String TAG = UsageStatsModule.class.getSimpleName();
  @SuppressWarnings("ResourceType")
  public static void getStats(Context context){
    UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
    int interval = UsageStatsManager.INTERVAL_YEARLY;
    Calendar calendar = Calendar.getInstance();
    long endTime = calendar.getTimeInMillis();
    calendar.add(Calendar.YEAR, -1);
    long startTime = calendar.getTimeInMillis();

    Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
    Log.d(TAG, "Range end:" + dateFormat.format(endTime));

    UsageEvents uEvents = usm.queryEvents(startTime,endTime);
    while (uEvents.hasNextEvent()){
      UsageEvents.Event e = new UsageEvents.Event();
      uEvents.getNextEvent(e);

      if (e != null){
        Log.d(TAG, "Event: " + e.getPackageName() + "\t" +  e.getTimeStamp());
      }
    }
  }

  public static List<long> getPastWeekDates(){
    List<long> dates = getDateRangeFromNow(Calendar.DATE, -7);

    return dates;
  }

  public static List<long> getPastMonthDates(){
    List<long> dates = getDateRangeFromNow(Calendar.MONTH, -1);

    return dates;
  }

  public static List<long> getPastYearDates(){
    List<long> dates = getDateRangeFromNow(Calendar.YEAR, -1);

    return dates;
  }

  public static List<long> getDateRangeFromNow(int field, int amount){
    List<long> dates = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    long endTime = calendar.getTimeInMillis();
    calendar.add(field, amount);
    long startTime = calendar.getTimeInMillis();

    dates.add(startTime);
    dates.add(endTime);

    return dates;
  }

  public static List<UsageStats> getUsageStatsList(Context context){
    UsageStatsManager usm = getUsageStatsManager(context);
    Calendar calendar = Calendar.getInstance();
    long endTime = calendar.getTimeInMillis();
    calendar.add(Calendar.YEAR, -1);
    long startTime = calendar.getTimeInMillis();

    Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
    Log.d(TAG, "Range end:" + dateFormat.format(endTime));

    List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
    return usageStatsList;
  }

  public static Map<String, UsageStats> getAggregateStatsMap(Context context){
    UsageStatsManager usm = getUsageStatsManager(context);
    Calendar calendar = Calendar.getInstance();
    long endTime = calendar.getTimeInMillis();
    calendar.add(Calendar.YEAR, -1);
    long startTime = calendar.getTimeInMillis();

    Map<String, UsageStats> aggregateStatsMap = usm.queryAndAggregateUsageStats(startTime,endTime);
    return aggregateStatsMap;
  }

  // See here for more help:
  // https://github.com/ColeMurray/UsageStatsSample/blob/master/app/src/main/java/com/murraycole/appusagesample/UStats.java
  public static String printUsageStats(List<UsageStats> usageStatsList){
    String statsString = new String();
    statsString = statsString + "hello";
    for (UsageStats u : usageStatsList){
      // statsString = statsString + "Pkg: " + u.getPackageName() +  "\t" + "ForegroundTime: "
      //   + u.getTotalTimeInForeground() + "\n";
      statsString = statsString + "!";
    }
    return statsString;
  }

  public static void printCurrentUsageStatus(Context context){
    printUsageStats(getUsageStatsList(context));
  }
  @SuppressWarnings("ResourceType")
  private static UsageStatsManager getUsageStatsManager(Context context){
    UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
    return usm;
  }

  public static String myAppStats(Map<String, UsageStats> aggregateStats){
    String statsString = new String();
    for(Map.Entry<String, UsageStats> entry: aggregateStats.entrySet()) {
        statsString = statsString + entry.getKey() + ":" + entry.getValue().getTotalTimeInForeground() + ";";
    }
    return statsString;
  }

  @ReactMethod
  public void showStats(
    int duration,
    Callback successCallback) {
      try {
        String message = myAppStats(getAggregateStatsMap(getReactApplicationContext()));
        successCallback.invoke(message);
      } catch (Exception e) {
        String error = e.getMessage();
        Toast.makeText(getReactApplicationContext(), error, duration).show();
      }
    }
}
