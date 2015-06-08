package helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class that can be extended to make available simple preference
 * setter/getters.
 * 
 * Should be extended to provide Facades.
 * 
 */
public class PreferenceHelper {
	
	
	protected void putStringPreference( Context context, String prefsName,	String key, String value ) {
		
		SharedPreferences preferences = context.getSharedPreferences( prefsName, Activity.MODE_PRIVATE );
		android.content.SharedPreferences.Editor editor = preferences.edit();
		
		editor.putString( key, value );
		editor.commit();
	}
	
	protected String getStringPreference( Context context, String prefsName,
			String key ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		String value = preferences.getString( key, "" );
		return value;
	}
	
	protected void putBooleanPreference( Context context, String prefsName,
			String key, boolean value ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		android.content.SharedPreferences.Editor editor = preferences.edit();
		
		editor.putBoolean( key, value );
		editor.commit();
	}
	
	protected boolean getBooleanPreference( Context context, String prefsName,
			String key ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		boolean value = preferences.getBoolean( key, false );
		return value;
	}
	
	protected void putIntegerPreference( Context context, String prefsName,
			String key, int value ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		android.content.SharedPreferences.Editor editor = preferences.edit();
		
		editor.putInt( key, value );
		editor.commit();
	}
	
	/**
	 * Get a integer under a particular key and filename
	 * 
	 * @param context
	 * @param the
	 *            filename of preferences
	 * @param key
	 *            name of preference
	 * @return -1 if key is not found
	 */
	protected int getIntegerPreference( Context context, String prefsName,
			String key ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		int value = preferences.getInt( key, -1 );
		return value;
	}
	
	protected void putLongPreference( Context context, String prefsName,
			String key, long value ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		android.content.SharedPreferences.Editor editor = preferences.edit();
		
		editor.putLong( key, value );
		editor.commit();
	}
	
	/**
	 * Get a integer under a particular key and filename
	 * 
	 * @param context
	 * @param the
	 *            filename of preferences
	 * @param key
	 *            name of preference
	 * @return Integer.MIN if key is not found
	 */
	protected long getLongPreference( Context context, String prefsName,
			String key ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		long value = preferences.getLong( key, Integer.MIN_VALUE );
		return value;
	}
	
	protected void putFloatPreference( Context context, String prefsName,
			String key, float value ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		android.content.SharedPreferences.Editor editor = preferences.edit();
		editor.putFloat( key, value );
		editor.commit();
	}
	
	protected float getFloatPreference( Context context, String prefsName,
			String key ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		float value = preferences.getFloat( key, Float.MIN_VALUE );
		return value;
	}
	
	protected void removePreference( Context context, String prefsName,
			String key ) {
		
		SharedPreferences preferences = context.getSharedPreferences(
				prefsName, Activity.MODE_PRIVATE );
		android.content.SharedPreferences.Editor editor = preferences.edit();
		
		editor.remove( key );
		editor.commit();
	}
}
