package ga.gasoft.smartpolice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.utils.PreferenceHelper;


public class SplashScreenActivity extends Activity{
	Boolean login;
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 5000;

	private static String email;
	private static String password, suname, spassword;
	private static final String TAG_SESSION = "sessionid";
	private static final String TAG_SUCCESS = "code";
	private static final String TAG_ID = "userid";
	private static final String TAG_LANG = "lang";
	private static final String TAG_ROLE = "roleid";
	private static final String tag = "splash Activity";private static final String TAG_NAME = "username";

	public static String username;
	public static String ipaddress;
	public static String FinalData;
	public static String regId;
	String lang;
	public static String userid;
	private TextView msg;
	AsyncTask<Void, Void, Void> mRegisterTask;
	private String TAG = " PushActivity";
	private Typeface myTypeface;
	private TextView remember, user, toolbartitle;
	private String unique_id;
	private LocationManager locationManager;
	private double lattitude, longitude;
	private PreferenceHelper splashScreenHelper;
	private int status;
	private ProgressDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark
			));
		}

		View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		decorView.setSystemUiVisibility(uiOptions);

		splashScreenHelper = new PreferenceHelper(SplashScreenActivity.this);

//		SharedPreferences settings = getSharedPreferences("Qib", 0);
		login = splashScreenHelper.getBoolFromSharedPrefs("IsLoggedIn");
		Log.d(tag, "login " + login);

//		SharedPreferences settings2 = getSharedPreferences("qibhr1", 0);
		lang = splashScreenHelper.getValueFromSharedPrefs("lang");

		SharedPreferences settings = getSharedPreferences("Qib", 0);
		login = settings.getBoolean("IsLoggedIn", false);
		Log.d(tag, "login " + login);
		SharedPreferences settings2 = getSharedPreferences("qibhr1", 0);
		lang = settings2.getString("lang", null);

		//userid = settings.getString("userid", null);
		Log.d(tag, "Lang " + lang);

//		splashScreenHelper.saveValueToSharedPrefs("DeviceId", Android_device);
		new Handler().postDelayed(                                                                                                                                                                                                                                                                                                                                                                                                                           new Runnable() {
        	@Override
			public void run() {
				// This method will be executed once the timer is over
                if(App.getUserId() != 0){
					userPasswordCheck();
					Log.d(tag, "user verification ");
				}else{
					App.clear();
					Log.e("inside","user not  present");
					Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
					startActivity(i);
				}


			}


		}, SPLASH_TIME_OUT);

	}

  public void userPasswordCheck(){


	  if(NetUtils.isOnline(SplashScreenActivity.this)){
	  JSONObject jsonData = new JSONObject();
		  
		 progressDialog =  new ProgressDialog(SplashScreenActivity.this);
		  progressDialog.setMessage(getResources().getString(R.string.Verification));
		  progressDialog.setCancelable(false);
		  progressDialog.show();

	  try {
		  jsonData.put("userid", App.getUserId());
		  jsonData.put("uname", App.getUserName());
		  jsonData.put("password", App.getPassword());

		  UserNetworkManager.userPasswordCheck(new CallBacks.ObjectCallBackListener() {
			  @Override
			  public void onSuccess(Object responseObject) {
                 if(responseObject != null) {
					 progressDialog.dismiss();
					 JSONObject object = (JSONObject) responseObject;
					 try {
						 status = Integer.parseInt(object.getString("status"));
						 Log.e("password check ", " "+status);
					 } catch (JSONException e) {
						 e.printStackTrace();
					 }

					 if(status == 0 ){
						 Log.e("inside","user present");
						 Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
						 startActivity(i);
					 }else if(status == 1){
						 App.clear();
						 Log.e("inside","user not  present");
						 Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
						 startActivity(i);
					 }

					 finish();
				 }


			  }

			  @Override
			  public void onError(String errorMessage) {
                progressDialog.dismiss();
			  }

			  @Override
			  public void onErrorWithData(JSONObject errorjson) {

			  }

			  @Override
			  public void onStart() {

			  }
		  }, jsonData);


//					 close this activity_instruction


	  } catch (JSONException e) {
		  e.printStackTrace();
		  progressDialog.dismiss();
	  }
	  }else{
		  Toast.makeText(SplashScreenActivity.this,getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
	  }

  }


}

