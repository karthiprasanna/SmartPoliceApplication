package ga.gasoft.smartpolice;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Locale;

import ga.gasoft.smartpolice.utils.PreferenceHelper;

@SuppressLint("NewApi")
public class LanguageActivity extends AppCompatActivity {

	Button btn;
	Locale myLocale;
	public static String lang;
	String langu;
	private static final String tag = "SignUp Activity";
	SharedPreferences settings;
	private Activity mContext;
	//private static final int TIME_DELAY = 2000;
	private static long back_pressed;
	private PreferenceHelper languageActivityHelper;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_launguage);
		languageActivityHelper = new PreferenceHelper(LanguageActivity.this);
		//	setupWindowAnimations();
	}




	public void english(View view) {
		lang = "EN";
		languageActivityHelper.saveValueToSharedPrefs("lang",lang);
		/*SharedPreferences settings = getSharedPreferences("qibhr2", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("lang", lang);
		//editor.putString("lang1",lang);
		editor.commit();*/
/*
		Intent intent = new Intent(this, LoginActivity.class);
		//overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
		startActivity(intent);
*/

		Log.d(tag, "lang " + lang);
		/*SharedPreferences.Editor editor = settings.edit();
		editor.putString(lang, lang);*/

		//Toast.makeText(getApplicationContext(), R.string.succesfullyen, Toast.LENGTH_LONG).show();
		setLocale("en");
		loginValidation();
	}



	public void kannada(View view) {
		lang = "KAN";
		languageActivityHelper.saveValueToSharedPrefs("lang",lang);

		/*SharedPreferences settings = getSharedPreferences("qibhr2", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("lang", lang);
		//editor.putString("lang1",lang);
		editor.commit();*/
/*
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);*/
		//Toast.makeText(getApplicationContext(),R.string.succesfullyar, Toast.LENGTH_LONG).show();

		Log.d(tag, "lang " + lang);

		setLocale("kan");
		loginValidation();
	}



	public void setLocale(String lang) {

		myLocale = new Locale(lang);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		loginValidation();
	/*	Intent refresh = new Intent(this, LoginActivity.class);
		startActivity(refresh)*/;
	}
	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		// set dialog message
		alertDialogBuilder
				.setMessage(this.getString(R.string.areyou))
				.setCancelable(false)
				.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();
						System.exit(0);
						dialog.cancel();
					}
				})
				.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						/*Intent intent = new Intent(AndroidLocalize.this, AndroidLocalize.class);
						startActivity(intent);*/
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}
	public void loginValidation(){
		if(languageActivityHelper.getIntFromSharedPrefs("userId") == 0 || languageActivityHelper.getIntFromSharedPrefs("userId") == -1 ||languageActivityHelper.getIntFromSharedPrefs("userId") == -2) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

}