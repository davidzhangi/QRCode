package com.david.qrcode.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * preference 相关操作
 *
 * @author yuyang at 2014.11.05
 */
public class PrefUtil {
  private static SharedPreferences pref;
  private static final String APPLICATION_PREFERENCES = "app_prefs";
  /**
   * 记录引导页
   */
  public static final String GUIDE_SHOWED_CODE = "guide_showed_code";

  private static SharedPreferences.Editor edit(Context context) {
    return getPref(context)
        .edit();
  }

  private static SharedPreferences getPref(Context context) {
    if (pref == null) {
      pref = context.getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
    }

    return pref;
  }

  /**
   * 保存字符串Pref
   *
   * @param context
   * @param key
   * @param value
   */
  public static void savePref(Context context, String key, String value) {
    edit(context)
        .putString(key, value)
        .apply();
  }

  /**
   * 保存布尔值
   *
   * @param context
   * @param key
   * @param value
   */
  public static void savePref(Context context, String key, boolean value) {
    edit(context)
        .putBoolean(key, value)
        .apply();
  }

  /**
   * 保存int
   *
   * @param context
   * @param key
   * @param value
   */
  public static void savePref(Context context, String key, int value) {
    edit(context)
        .putInt(key, value)
        .apply();
  }

  /**
   * 保存long
   *
   * @param context
   * @param key
   * @param value
   */
  public static void savePref(Context context, String key, Long value) {
    edit(context)
        .putLong(key, value)
        .apply();
  }

  /**
   * 获取字符串值
   *
   * @param context
   * @param key
   * @return
   */
  public static String getStringPref(Context context, String key) {
    return getStringPref(context, key, "");
  }

  public static String getStringPref(Context context, String key, String defaultValue) {
    return getPref(context)
        .getString(key, defaultValue);
  }

  /**
   * 获取布尔值
   *
   * @param context
   * @param key
   * @param defaultValue
   * @return
   */
  public static boolean getBooleanPref(Context context, String key, boolean defaultValue) {
    return getPref(context)
        .getBoolean(key, defaultValue);
  }

  /**
   * 获取int
   *
   * @param context
   * @param key
   * @param defaultValue
   * @return
   */
  public static int getIntPref(Context context, String key, int defaultValue) {
    return getPref(context)
        .getInt(key, defaultValue);
  }

  /***
   * 获取long
   *
   * @param context
   * @param key
   * @param defaultValue
   * @return
   */
  public static Long getLongPref(Context context, String key, Long defaultValue) {
    return getPref(context)
        .getLong(key, defaultValue);
  }

  /**
   * 保存集合
   *
   * @param context
   * @param key
   * @param value
   */
  public static void saveSet(Context context, String key, Set<String> value) {
    edit(context)
        .putStringSet(key, value)
        .apply();
  }

  /**
   * 获取集合
   *
   * @param context
   * @param key
   * @return
   */
  public static Set<String> getSet(Context context, String key) {
    return getPref(context)
        .getStringSet(key, null);
  }



  public static void putString(Context context, String key, String value) {
    savePref(context,key,value);
  }

  public static String getString(Context context, String key) {
    return getString(context, key, "");
  }

  public static String getString(Context context, String key, String defaultValue) {
    return getStringPref(context,key,defaultValue);
  }


  public static void putInt(Context context, String key, int value) {
    savePref(context,key,value);
  }


  public static int getInt(Context context, String key) {
    return getInt(context, key, -1);
  }


  public static int getInt(Context context, String key, int defaultValue) {
    return getIntPref(context,key,defaultValue);
  }

  public static void putLong(Context context, String key, long value) {
    savePref(context,key,value);
  }


  public static long getLong(Context context, String key) {
    return getLong(context, key, -1);
  }


  public static long getLong(Context context, String key, long defaultValue) {
    return getLongPref(context,key,defaultValue);
  }

  public static void putFloat(Context context, String key, float value) {
    edit(context)
        .putFloat(key,value)
        .apply();
  }


  public static float getFloat(Context context, String key) {
    return getFloat(context, key, -1);
  }


  public static float getFloat(Context context, String key, float defaultValue) {
    return getPref(context)
        .getFloat(key,defaultValue);
  }

  public static void putBoolean(Context context, String key, boolean value) {
    savePref(context,key,value);
  }


  public static boolean getBoolean(Context context, String key) {
    return getBoolean(context, key, false);
  }


  public static boolean getBoolean(Context context, String key, boolean defaultValue) {
    return getBooleanPref(context,key,defaultValue);
  }
}