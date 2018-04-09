package ga.gasoft.smartpolice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;
import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.utils.FcmServerRegistration;
import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.Utils;



public class LoginActivity extends AppCompatActivity {

    public static EditText username, pass;
    public static Button submit;
    public static String usrStr, passStr;
    public static String text = "";
    private int userId;
    private ProgressDialog pDialog;
    private double lattitude, longitude;
    private boolean onSubmitClicked = false;
    private String Device_Id;
    private GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Log.e("oncreate"+passStr,"called"+usrStr);

        usrStr = null;
        passStr = null;

        username = (EditText) findViewById(R.id.email);
        username.setFilters(new InputFilter[]{Utils.filtertxt});

        pass = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.btnLogin1);

        gpsTracker = new GPSTracker(LoginActivity.this);
        lattitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        Log.e("lattitude, longitude", lattitude+"......"+longitude);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker.getLocation();
                lattitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                usrStr = username.getText().toString();
                Log.e("lattitude, longitude", lattitude+"......"+longitude);
                passStr = pass.getText().toString();
                if (NetUtils.isOnline(LoginActivity.this)){
                    onSubmitClicked = true;
                    uploadDetails(onSubmitClicked);
                  }else{
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
                }


            }
        });


        Device_Id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("Android", "Android Device ID : " + Device_Id);
        App.setDeviceId(Device_Id);


    }




    public void uploadDetails(boolean onSubmitClick) {
        Log.e("update ", "called");
        if (onSubmitClick) {
            if (usrStr != null && !usrStr.trim().isEmpty() && passStr != null && !passStr.trim().isEmpty()) {
                Log.e("in side update " + usrStr, "called.." + passStr);
                if (lattitude != 0 && longitude != 0) {
                    App.setUserName(usrStr.trim());
                    App.setPassword(passStr.trim());

                    Log.e("update ", "called");
                    onSubmitClicked = false;

                        userLogin();

                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.userNamePasswordRequires), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void userLogin(){
        if(NetUtils.isOnline(LoginActivity.this)) {
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pleasewait));
            pDialog.setCancelable(false);
            pDialog.show();
            String deviceId = App.getDeviceId();
            JSONObject json = new JSONObject();
            try {
                json.put("uname", usrStr);
                json.put("password", passStr);
                json.put("latitude", "" + lattitude);
                json.put("lagnitude", "" + longitude);
                json.put("deviceid", deviceId);
                json.put("fcmregistrationid", App.getFcmregistrationid());
                Log.e("jsondata", json.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            UserNetworkManager.userLogin(new CallBacks.ObjectCallBackListener() {
                @Override
                public void onSuccess(Object responseObject) {
                    Log.e("login response", responseObject+"");
                    pDialog.dismiss();
                    if(responseObject != null) {
                        JSONObject object = (JSONObject) responseObject;
                        try {
                            userId = Integer.parseInt(object.getString("output"));
                            Log.e("login userId", userId+"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        App.setUserId(userId);
                        userIdValidation(userId);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e("login error response", errorMessage+"");
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

                @Override
                public void onErrorWithData(JSONObject errorjson) {
                    pDialog.dismiss();
                }

                @Override
                public void onStart() {

                }
            }, getApplicationContext(),json);



        }else{
            Toast.makeText(LoginActivity.this,getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }
    }

    private void userIdValidation(int uid) {
        int userId = uid;
        System.out.println("output--post------------->" + userId);

        if (userId == -1) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.Inactive), Toast.LENGTH_SHORT).show();
        } else if (userId == -2 || userId == 0) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.Invalid), Toast.LENGTH_SHORT).show();
        } else if (userId == -3) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.AlreadyAssigned), Toast.LENGTH_SHORT).show();
        } else if (userId == -4) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.NoPermission), Toast.LENGTH_SHORT).show();
        } else {
            App.setUserId(userId);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            try {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                Log.e("Firbase id login", "Refreshed token: " + refreshedToken);
                App.setFcmregistrationid(refreshedToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new FCMRegistrationAsyncTask().execute();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.LoginSuccess), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void finish() {
        gpsTracker.stopUsingGPS();
        super.finish();
    }

    public class FCMRegistrationAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            new FcmServerRegistration().sendRegisterFcmId(LoginActivity.this);
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}




