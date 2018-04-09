package ga.gasoft.smartpolice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.adapters.CustomArrayAdapter;
import ga.gasoft.smartpolice.model.Atm;
import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.JSONfunctions;
import ga.gasoft.smartpolice.utils.PreferenceHelper;
import ga.gasoft.smartpolice.utils.RequestHandler;
import ga.gasoft.smartpolice.utils.Utils;


public class AtmActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SwitchCompat switch1, switch2, switch3, switch4, switch5;
    private String checkvalue;

    ArrayList<Atm> atmlist;
    private TextView hometitle;
    private ImageView home, back;
    private ImageView imageView;

    private static final int INTENT_REQUEST_GET_IMAGES = 20;
    public static final String UPLOAD_KEY = "image";
    private LinearLayout lnrImages;
    private ArrayList<String> atmSpinnerList = new ArrayList<>();
    ArrayList<Uri> image_uris = new ArrayList<Uri>();


    private Bitmap bitmap;
    private Atm atm;
    private LocationManager locationManager;
    private boolean onSubmitClicked = false;
    private ProgressDialog pDialog;
    private Button imageButton;
    private Button uploadButton;
    private Spinner mySpinner;
    private TextView atmAddress;
    private ProgressDialog loading;
    private JSONObject jsonObject;
    private GPSTracker gpsTracker;
    private double latitude, longitude;
    private ArrayAdapter<String> adapter;
    private ImageView imageView1, imageView2, imageView3, imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        hometitle = (TextView) findViewById(R.id.kkk);
        hometitle.setText(R.string.atm);
        atm = new Atm();

        atmAddress = (TextView) findViewById(R.id.address);
        uploadButton = (Button) findViewById(R.id.submitinfo);
        back = (ImageView) findViewById(R.id.back);

        atmAddress.setSelected(true);

        switch1 = (SwitchCompat) findViewById(R.id.switch1);
        switch2 = (SwitchCompat) findViewById(R.id.switch2);
        switch3 = (SwitchCompat) findViewById(R.id.switch3);
        switch4 = (SwitchCompat) findViewById(R.id.switch4);
        switch5 = (SwitchCompat) findViewById(R.id.switch5);

        switch1.setOnCheckedChangeListener(this);
        switch2.setOnCheckedChangeListener(this);
        switch3.setOnCheckedChangeListener(this);
        switch4.setOnCheckedChangeListener(this);
        switch5.setOnCheckedChangeListener(this);

        lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
        imageView1 = (ImageView)findViewById(R.id.img1);
        imageView2 = (ImageView)findViewById(R.id.img2);
        imageView3 = (ImageView)findViewById(R.id.img3);
        imageView4 = (ImageView)findViewById(R.id.img4);



        imageButton = (Button) findViewById(R.id.takephoto);
        atmlist = new ArrayList<Atm>();

        gpsTracker = new GPSTracker(AtmActivity.this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        mySpinner = (Spinner) findViewById(R.id.my_spinner);
        atmSpinnerList.add(getResources().getString(R.string.selectName));

        getAtmDetails();

//        adapter = new ArrayAdapter<String>(AtmActivity.this,
//                android.R.layout.simple_spinner_dropdown_item,
//                atmSpinnerList);

        adapter = new CustomArrayAdapter(AtmActivity.this, R.layout.spinner_drop_down, atmSpinnerList);

        mySpinner.setAdapter(adapter);



        // Spinner on item click listener
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int position, long arg3) {
                // TODO Auto-generated method stub


                if(position != 0) {
                    atmAddress.setText(atmlist.get(position-1).ATM_Address);
                    atm.ATM_ID = atmlist.get(position-1).ATM_ID;
                    atm.A_ID = atmlist.get(position-1).A_ID;
                }else{
                    atmAddress.setText("");
                    atmAddress.setHint("Address");
                    atm.ATM_ID = "";
                    atm.A_ID = 0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gpsTracker.getLocation();
                atm.lattitude = gpsTracker.getLatitude();
                atm.longitude = gpsTracker.getLongitude();
                if(atm.A_ID != 0) {
                    if (atm.checked == 1) {
                        if (image_uris != null && image_uris.size() > 0) {
                            onSubmitClicked = true;

                            if (atm.lattitude == 0 && atm.longitude == 0) {
                                Toast.makeText(AtmActivity.this, getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
                            } else {
                                uploadFile(image_uris);
                            }
                        } else {
                            Toast.makeText(AtmActivity.this, getResources().getString(R.string.ImagesNotSelected), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AtmActivity.this, getResources().getString(R.string.CheckNotSelected), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AtmActivity.this, getResources().getString(R.string.NoAtmSelected), Toast.LENGTH_SHORT).show();
                }

            }

        });


        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Config config = new Config();
                config.setCameraHeight(R.dimen.app_camera_height);
                config.setToolbarTitleRes(R.string.custom_title);
                config.setSelectionMin(1);
                config.setSelectionLimit(4);
                config.setSelectedBottomHeight(R.dimen.bottom_height);
                config.setFlashOn(true);
                getImages(config);
            }
        });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.switch1:

                if (!isChecked) {
                    atm.cctv_present = 0;
                    System.out.println("first" + checkvalue);
                } else {
                    atm.cctv_present = 1;
                    System.out.println("first--------------->>>>>" + checkvalue);

                }
                break;
            case R.id.switch2:

                if (!isChecked) {
                    atm.cctv_working = 0;
                } else {
                    atm.cctv_working = 1;

                }
                break;
            case R.id.switch3:

                if (!isChecked) {
                    atm.secPerson = 0;
                } else {
                    atm.secPerson = 1;

                }
                break;
            case R.id.switch4:

                if (!isChecked) {
                    atm.atm_working = 0;
                } else {
                    atm.atm_working = 1;

                }
                break;
            case R.id.switch5:

                if (!isChecked) {
                    atm.checked = 0;
                } else {
                    atm.checked = 1;

                }
                break;
            default:
                break;
        }

    }

// Method for fetching Atm details
    private void getAtmDetails() {
        if (NetUtils.isOnline(AtmActivity.this)) {
            pDialog = new ProgressDialog(AtmActivity.this);
            pDialog.setMessage(getResources().getString(R.string.GetDetails));
            pDialog.setCancelable(false);
            pDialog.show();

            UserNetworkManager.getAtmDetails(new CallBacks.ListCallBackListener<Atm>() {

                @Override
                public void onSuccess(List<Atm> responseList) {

                    pDialog.dismiss();
                    if (responseList != null && responseList.size() > 0) {

                        for (int i = 0; i < responseList.size(); i++) {
                            atmSpinnerList.add((responseList.get(i)).ATM_ID);
                            Log.e("atm details", responseList.get(i).toString());
                            // Populate spinner with country names
                            atmlist.add(responseList.get(i));

                        }

                    }else{
                        Toast.makeText(AtmActivity.this, getResources().getString(R.string.noDetailsAvailable), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError(String errorMessage) {
                    pDialog.dismiss();
                    Toast.makeText(AtmActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, Atm.class);
        } else {
            Toast.makeText(AtmActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }

    }

    // Opening new activity to pick images
    private void getImages(Config config) {

        ImagePickerActivity.setConfig(config);

        Intent intent = new Intent(this, ImagePickerActivity.class);

        if (image_uris != null) {
            intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
        }


        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
                imageView3.setVisibility(View.GONE);
                imageView4.setVisibility(View.GONE);
                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if(image_uris != null && image_uris.size()>0)
                lnrImages.setVisibility(View.VISIBLE);
                for (int i = 0; i < image_uris.size(); i++) {

                    bitmap = BitmapFactory.decodeFile(String.valueOf(image_uris.get(i)));
                    if(i==0) {
                        imageView1.setImageBitmap(bitmap);
                        imageView1.setVisibility(View.VISIBLE);
                    }

                    if(i==1) {
                        imageView2.setImageBitmap(bitmap);
                        imageView2.setVisibility(View.VISIBLE);
                    }

                    if(i==2) {
                        imageView3.setImageBitmap(bitmap);
                        imageView3.setVisibility(View.VISIBLE);
                    }
                    if(i==3) {
                        imageView4.setImageBitmap(bitmap);
                        imageView4.setVisibility(View.VISIBLE);
                    }

                }


            }
        }
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    //Uploading images to server
    public void uploadFile(List<Uri> sourceFileUri) {
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < sourceFileUri.size(); i++) {
            fileList.add(new File(String.valueOf(sourceFileUri.get(i))));
        }
        if (NetUtils.isOnline(AtmActivity.this)) {
            loading = new ProgressDialog(AtmActivity.this);
            loading.setMessage(getResources().getString(R.string.uploadImage));
            loading.setCancelable(false);
            loading.show();
            UserNetworkManager.uploadImages(new CallBacks.StringCallBackListener() {

                @Override
                public void onSuccess(String successMessage) {

                        if (successMessage.contains("200")) {
                            postAtmDetails(atm);
                        } else {
                            loading.dismiss();
                            Toast.makeText(AtmActivity.this, getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
                        }

                }

                @Override
                public void onError(String errorMessage) {
                    loading.dismiss();
                    Toast.makeText(AtmActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, fileList, "Atm");
        } else {
            Toast.makeText(AtmActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }


    }



    //After success of images posting atm details to server
    public void postAtmDetails(Atm atm) {

        loading.setMessage(getResources().getString(R.string.uploadingDetails));
        loading.setCancelable(false);
        Log.e("post method", "called");

        try {
            jsonObject = new JSONObject();
            jsonObject.put("ATMId", atm.A_ID);
            jsonObject.put("CCTVPresent", atm.cctv_present);
            jsonObject.put("CCTVWorking", atm.cctv_working);
            jsonObject.put("ATMworking", atm.atm_working);
            jsonObject.put("SecPresent", atm.secPerson);
            jsonObject.put("Checked", atm.checked);
            jsonObject.put("Timestampdata", 0);
            jsonObject.put("Userid", App.getUserId());
            jsonObject.put("latitude", atm.lattitude);
            jsonObject.put("longitude", atm.longitude);
            Log.e("jsonObject", "" + jsonObject);

        UserNetworkManager.postAtmDetails(new CallBacks.StringCallBackListener() {
            @Override
            public void onSuccess(String successMessage) {
                loading.dismiss();

                if(successMessage != null && (successMessage.toString().trim().contains("Success")|| successMessage.toString().trim().contains("Sucess"))) {
                    Utils.showSuccessDialog(AtmActivity.this);
                }else {
                    Toast.makeText(AtmActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                loading.dismiss();
                Toast.makeText(AtmActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {

            }
        }, jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void finish() {
        gpsTracker.stopUsingGPS();
        super.finish();
    }

}



