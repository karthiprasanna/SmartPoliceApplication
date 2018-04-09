package ga.gasoft.smartpolice.Application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.support.multidex.MultiDexApplication;
import android.util.Log;



public class App extends MultiDexApplication
{
    /**
     * Keeps a reference of the application context
     */
    private static App app;
    public static AssetManager assetManager = null;
    public static Properties properties = null;


    private static int userId;
    private static String userName;
    private static String token;
    private static String profileName;
    private static String email;
    private static String disciplineCode;
    private static String lattitude;
    private static String longitude;
    private static String deviceId;
    private static String fcmregistrationid;
    private static String password;
    private static int messageCount;
    private static String profileImageUrl;



    private static int notificationCount;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Production
//		Crittercism.initialize(getApplicationContext(), "54bca36651de5e9f042ecdc3");
        assetManager = getResources().getAssets();
        // Development
        // Crittercism.initialize(getApplicationContext(), "54fe8c92b59ef2d535335c48");
        preferences = getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        app = this;
    }

    public static int getUserId() {
        return preferences.getInt("userId", 00);
    }

    public static void setUserId(int userId) {
        editor = preferences.edit();
        editor.putInt("userId", userId);
        Log.e("userId",""+userId);
        editor.commit();

    }

    public static String getDeviceId() {
        return preferences.getString("deviceId", null);
    }

    public static void setDeviceId(String deviceId) {
        editor = preferences.edit();
        editor.putString("deviceId", deviceId);
        Log.e("deviceId",""+deviceId);
        editor.commit();
    }

    public static String getFcmregistrationid() {
        return preferences.getString("fcmregistrationid", null);
    }

    public static void setFcmregistrationid(String fcmregistrationid) {
        editor = preferences.edit();
        editor.putString("fcmregistrationid", fcmregistrationid);
        Log.e("fcmregistrationid",""+fcmregistrationid);
        editor.commit();
    }



    public static String getUserName() {

        return preferences.getString("userName", null);
    }

    public static void setUserName(String userName) {
        editor = preferences.edit();
        editor.putString("userName", userName);
        Log.e("userName",""+userName);
        editor.commit();
    }

    public static String getToken() {

        return preferences.getString("token", null);
    }

    public static void setToken(String token) {

//		APP.token = token;
        editor = preferences.edit();
        editor.putString("token", token);
        editor.commit();

    }

    public static String getPassword() {
        return preferences.getString("password", null);
    }

    public static void setPassword(String password) {
        editor = preferences.edit();
        editor.putString("password", password);
        editor.commit();
    }

    public static String getEmail() {
        return preferences.getString("email", null);
    }

    public static void setEmail(String email) {
        editor = preferences.edit();
        editor.putString("email",email);
        editor.commit();
    }

    public static int getMessageCount() {
        return  preferences.getInt("msgCount", 1);
    }

    public static void setMessageCount(int messageCount) {
        editor = preferences.edit();
        editor.putInt("msgCount",messageCount);
        editor.commit();
    }

    public static String getProfileName() {
        return preferences.getString("profileName", null);
    }

    public static void setProfileName(String profileName) {
        editor = preferences.edit();
        editor.putString("profileName",profileName);
        editor.commit();
    }

    public String getLattitude() {
        return preferences.getString("lattitude", null);
    }

    public void setLattitude(String lattitude) {
        editor = preferences.edit();
        editor.putString("lattitude", lattitude);
        Log.e("lattitude",""+lattitude);
        editor.commit();
    }

    public String getLongitude() {
        return preferences.getString("longitude", null);
    }

    public void setLongitude(String longitude) {
        editor = preferences.edit();
        editor.putString("longitude", longitude);
        Log.e("longitude",""+longitude);
        editor.commit();
    }

    public static int getNotificationCount() {
        return preferences.getInt("notificationCount", 0);
    }

    public static void setNotificationCount(int notificationCount) {
        editor = preferences.edit();
        editor.putInt("notificationCount", notificationCount);
        Log.e("notificationCount",""+notificationCount);
        editor.commit();
    }

//	public static DtoFactory getDtoFactory(Context context)
//	{
//		if(dtoFactory == null)
//		{
//			dtoFactory = new DtoFactory(context.getApplicationContext());
//		}
//		return dtoFactory;
//	}


    public static App getApplicationInstance()
    {
        return app;
    }

    @Override
    public void onTrimMemory(int level)
    {
        // TODO Auto-generated method stub
        super.onTrimMemory(level);
        Log.e("onTrimMemory", "level " + level);
        if(level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL || level == ComponentCallbacks2.TRIM_MEMORY_BACKGROUND || level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE
                || level == ComponentCallbacks2.TRIM_MEMORY_MODERATE)
        {
            Log.e("onTrimMemory", "level " + level);
        }
    }


    // Read from the /assets directory
    /*public static Properties getToastProperties()
    {
        try
        {
            if(properties == null)
            {
                // Toast.makeText(getApplicationInstance(),
                // "Property Object Created Once", Toast.LENGTH_LONG).show();
                InputStream inputStream = assetManager.open("messages.properties");
                properties = new Properties();
                properties.load(inputStream);
            }
            return properties;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return new Properties();
        }
    }
*/

    public String getProfileImageUrl() {
        return  preferences.getString("imageUrl", null);
    }

    public void setProfileImageUrl(String profileImageUrl) {
        editor = preferences.edit();
        editor.putString("imageUrl",profileImageUrl);
        editor.commit();
    }

    public static void clear()
    {
        userId = 0;
        lattitude = null;
        longitude = null;
        userName = null;
        token = null;
        deviceId = null;

        editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}