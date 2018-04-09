package ga.gasoft.smartpolice.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Himank on 17-08-2016.
 */
public class PreferenceHelper {

	Context context;
	SharedPreferences sharedPreferences;

	public PreferenceHelper(Context context) {
		this.context = context;
		sharedPreferences = context.getSharedPreferences("SMARTPOLICE",Context.MODE_PRIVATE);
	}

	public SharedPreferences getGcmPreferences() {

		return sharedPreferences;
	}

	public String getValueFromSharedPrefs(String KeyName) {
		return sharedPreferences.getString(KeyName, "");
	}

	

	public boolean saveValueToSharedPrefs(String KeyName, String value) {
		Editor editor = sharedPreferences.edit();
		editor.putString(KeyName, value);
		editor.commit();
		return true;
	}
	public void saveIntToSharedPrefs(String keyName, int index) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(keyName, index);
		editor.commit();
	}
	public int getIntFromSharedPrefs(String KeyName) {
		return sharedPreferences.getInt(KeyName, 1);
	}

	public boolean getBoolFromSharedPrefs(String KeyName) {
		return sharedPreferences.getBoolean(KeyName, false);
	}

	public void savePreferenceIndex(String keyName, int index) {
		Editor editor = sharedPreferences.edit();
		editor.putInt(keyName, index);
		editor.commit();
	}

	public int getPreferenceIndex(String keyName) {
		return sharedPreferences.getInt(keyName, 0);
	}
	
	public void saveBooleanValueToSharedPrefs(String KeyName,boolean value){
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KeyName, value);
		editor.commit();
	}

	public void removeValue(String keyName){
		Editor editor = sharedPreferences.edit();
		editor.remove(keyName);
		editor.apply();
	}

	public void clear()
	{
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
}
