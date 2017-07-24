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
  // @SuppressWarnings("ResourceType")
  // public static void getStats(Context context){
  //   UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
  //   int interval = UsageStatsManager.INTERVAL_YEARLY;
  //   Calendar calendar = Calendar.getInstance();
  //   long endTime = calendar.getTimeInMillis();
  //   calendar.add(Calendar.YEAR, -1);
  //   long startTime = calendar.getTimeInMillis();
  //
  //   Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
  //   Log.d(TAG, "Range end:" + dateFormat.format(endTime));
  //
  //   UsageEvents uEvents = usm.queryEvents(startTime,endTime);
  //   while (uEvents.hasNextEvent()){
  //     UsageEvents.Event e = new UsageEvents.Event();
  //     uEvents.getNextEvent(e);
  //
  //     if (e != null){
  //       Log.d(TAG, "Event: " + e.getPackageName() + "\t" +  e.getTimeStamp());
  //     }
  //   }
  // }

  public static List getDates(int durationInDays){
    List dates = getDateRangeFromNow(Calendar.DATE, -(durationInDays));

    return dates;
  }

  public static List getDateRangeFromNow(int field, int amount){
  // public static List getDateRangeFromNow(int field, int amount){
    List dates = new ArrayList();
    Calendar calendar = Calendar.getInstance();
    long endTime = calendar.getTimeInMillis();
    calendar.add(field, amount);
    long startTime = calendar.getTimeInMillis();


    // TESTING 1 2 3...
    // SimpleDateFormat formatOne = new SimpleDateFormat("yyyy-MM-dd");
    // String dateOne = formatOne.format(startTime);
    // String dateTwo = formatOne.format(endTime);
    // Toast.makeText(getReactApplicationContext(), dateOne, Toast.LENGTH_SHORT).show();
    // Toast.makeText(getReactApplicationContext(), dateTwo, Toast.LENGTH_SHORT).show();

    dates.add(startTime);
    dates.add(endTime);

    return dates;
  }

  // public static List<UsageStats> getUsageStatsList(Context context){
  //   UsageStatsManager usm = getUsageStatsManager(context);
  //   Calendar calendar = Calendar.getInstance();
  //   long endTime = calendar.getTimeInMillis();
  //   calendar.add(Calendar.YEAR, -1);
  //   long startTime = calendar.getTimeInMillis();
  //
  //   Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
  //   Log.d(TAG, "Range end:" + dateFormat.format(endTime));
  //
  //   List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
  //   return usageStatsList;
  // }

  public static Map<String, UsageStats> getAggregateStatsMap(Context context, int durationInDays){
    UsageStatsManager usm = getUsageStatsManager(context);
    // Calendar calendar = Calendar.getInstance();
    // long endTime = calendar.getTimeInMillis();
    // calendar.add(Calendar.YEAR, -1);
    // long startTime = calendar.getTimeInMillis();

    List dates = getDates(durationInDays);
    long startTime = (long)dates.get(0);
    long endTime = (long)dates.get(1);

    Map<String, UsageStats> aggregateStatsMap = usm.queryAndAggregateUsageStats(startTime, endTime);
    return aggregateStatsMap;
  }

  // See here for more help:
  // https://github.com/ColeMurray/UsageStatsSample/blob/master/app/src/main/java/com/murraycole/appusagesample/UStats.java
  // public static String printUsageStats(List<UsageStats> usageStatsList){
  //   String statsString = new String();
  //   statsString = statsString + "hello";
  //   for (UsageStats u : usageStatsList){
  //     // statsString = statsString + "Pkg: " + u.getPackageName() +  "\t" + "ForegroundTime: "
  //     //   + u.getTotalTimeInForeground() + "\n";
  //     statsString = statsString + "!";
  //   }
  //   return statsString;
  // }

  // public static void printCurrentUsageStatus(Context context){
  //   printUsageStats(getUsageStatsList(context));
  // }
  // @SuppressWarnings("ResourceType")
  // private static UsageStatsManager getUsageStatsManager(Context context){
  //   UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
  //   return usm;
  // }

  public static String getStatsString(Map<String, UsageStats> aggregateStats){

    List statsCollection = new ArrayList();
    List appsCollection = new ArrayList();

    for(Map.Entry<String, UsageStats> entry: aggregateStats.entrySet()) {
        appsCollection.add(entry.getValue().getPackageName());
        statsCollection.add(entry.getValue().getTotalTimeInForeground());
    }

    String stats = joinStringList(",", statsCollection);;
    String apps = joinStringList(",", appsCollection);;

    String res = apps + ";" + stats;

    return res;
  }

  public static String joinStringList(String joiner, List items){
    String joined = new String();

    for(int i = 0; i < items.size(); i++) {
      joined += items.get(i);
      if(i < items.size() - 1){
        joined += joiner;
      }
    }

    return joined;
  }

  @ReactMethod
  public void getStats(
    int durationInDays,
    Callback successCallback) {
      if (durationInDays > 0) {
        try {
          String stats = getStatsString(getAggregateStatsMap(getReactApplicationContext(), durationInDays));

          // List dates = getDates(durationInDays);

          successCallback.invoke(stats);
        } catch (Exception e) {
          String errorMessage = e.getMessage();
          Toast.makeText(getReactApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
      } else {
        String noticeMessage = "Enter an integer greater than 0!";
        Toast.makeText(getReactApplicationContext(), noticeMessage, Toast.LENGTH_SHORT).show();
      }
    }

  @ReactMethod
  public void testToast(
    int duration) {
      String test = "It works!";
      Toast.makeText(getReactApplicationContext(), test, duration).show();
    }
}
