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
import ga.gasoft.smartpolice.model.WineShop;
import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.JSONfunctions;
import ga.gasoft.smartpolice.utils.RequestHandler;
import ga.gasoft.smartpolice.utils.Utils;


public class WineShopBarActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    JSONObject jsonobject;

    private SwitchCompat switch1,switch2,switch3,switch4;
    private String checkvalue;

    ArrayList<WineShop> winelist;

    private JSONArray jsonArray1;
    private TextView hometitle;
    private ImageView home,back;
    private ImageView imageView;

    private static final int INTENT_REQUEST_GET_IMAGES = 20;
    public static final String UPLOAD_KEY ="image";
    private LinearLayout lnrImages;
    private ArrayList<String> wineSpinnerList = new ArrayList<>();
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private EditText address;
    private Bitmap bitmap;
    private WineShop wineShop;
    private List serverResponseCodeList;
    private boolean onSubmitClicked = false;
    private LocationManager locationManager;
    private ProgressDialog pDialog;
    private Spinner mySpinner;
    private Button getImages2, upload;
    private ProgressDialog loading;
    private GPSTracker gpsTracker;
    private TextView txtpopulation;
    private CustomArrayAdapter adapter;
    private ImageView imageView1, imageView2, imageView3, imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wineshopbar);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        hometitle=(TextView)findViewById(R.id.kkk);
        hometitle.setText(R.string.wineshopandbar);
        mySpinner = (Spinner) findViewById(R.id.wineshopspinner);
        back=(ImageView) findViewById(R.id.back);
        wineShop = new WineShop();
        winelist = new ArrayList<WineShop>();
        txtpopulation = (TextView) findViewById(R.id.wineshopaddress);
        txtpopulation.setSelected(true);

        switch1 = (SwitchCompat)findViewById(R.id.switch1);
        switch2 = (SwitchCompat)findViewById(R.id.switch2);
        switch3 = (SwitchCompat)findViewById(R.id.switch3);
        switch4 = (SwitchCompat)findViewById(R.id.switch4);

        switch1.setOnCheckedChangeListener(this);
        switch2.setOnCheckedChangeListener(this);
        switch3.setOnCheckedChangeListener(this);
        switch4.setOnCheckedChangeListener(this);

        lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
        imageView1 = (ImageView)findViewById(R.id.img1);
        imageView2 = (ImageView)findViewById(R.id.img2);
        imageView3 = (ImageView)findViewById(R.id.img3);
        imageView4 = (ImageView)findViewById(R.id.img4);


        getImages2 = (Button)findViewById(R.id.takephoto);

        gpsTracker = new GPSTracker(WineShopBarActivity.this);

        wineSpinnerList.add(getResources().getString(R.string.selectName));
        getWineShopDetails();

        adapter = new CustomArrayAdapter(WineShopBarActivity.this, R.layout.spinner_drop_down, wineSpinnerList);

        mySpinner.setAdapter(adapter);



        getImages2.setOnClickListener(new View.OnClickListener() {
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


         upload = (Button)findViewById(R.id.submitinfo);
        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gpsTracker.getLocation();
                wineShop.lattitude = gpsTracker.getLatitude();
                wineShop.longitude = gpsTracker.getLongitude();
                if(wineShop.WB_ID != 0) {
                    if (wineShop.checked == 1) {
                        if (image_uris != null && image_uris.size() > 0) {
                            onSubmitClicked = true;
                            if (wineShop.lattitude == 0 && wineShop.longitude == 0) {
                                Toast.makeText(WineShopBarActivity.this, getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
                            } else {
//                            uploadDetails(onSubmitClicked);

                                uploadFile(image_uris);
                            }
                        } else {
                            Toast.makeText(WineShopBarActivity.this, getResources().getString(R.string.ImagesNotSelected), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WineShopBarActivity.this, getResources().getString(R.string.CheckNotSelected), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(WineShopBarActivity.this, getResources().getString(R.string.WineShopNotSelected), Toast.LENGTH_SHORT).show();
                }


//                uploadFile(image_uris);
            }
        });

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int position, long arg3) {
                // TODO Auto-generated method stub


             if(position !=0) {
                 txtpopulation.setText(winelist.get(position-1).WB_Place);
                 wineShop.WB_Name = winelist.get(position-1).WB_Name;
                 wineShop.WB_ID = winelist.get(position-1).WB_ID;
             }else{
                 txtpopulation.setText("");
                 txtpopulation.setHint("Address");
                 wineShop.WB_Name = "";
                 wineShop.WB_ID = 0;
             }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.switch1:

                if (!isChecked) {
                    wineShop.cctv_present = 0;
                    System.out.println("first"+checkvalue);
                } else {
                    wineShop.cctv_present = 1;
                    System.out.println("first--------------->>>>>"+checkvalue);

                }
                break;
            case R.id.switch2:

                if (!isChecked) {
                    wineShop.cctv_working = 0;
                } else {
                    wineShop.cctv_working = 1;

                }
                break;
            case R.id.switch3:

                if (!isChecked) {
                    wineShop.secPerson = 0;
                } else {
                    wineShop.secPerson = 1;

                }
                break;
            case R.id.switch4:

                if (!isChecked) {
                    wineShop.checked = 0;
                } else {
                    wineShop.checked = 1;

                }
                break;

            default:
                break;
        }

    }



    private void getWineShopDetails() {
        if (NetUtils.isOnline(WineShopBarActivity.this)) {
            pDialog = new ProgressDialog(WineShopBarActivity.this);
            pDialog.setMessage(getResources().getString(R.string.GetDetails));
            pDialog.setCancelable(false);
            pDialog.show();

            UserNetworkManager.getWineShopDetails(new CallBacks.ListCallBackListener<WineShop>() {

                @Override
                public void onSuccess(List<WineShop> responseList) {

                    pDialog.dismiss();
                    if (responseList != null && responseList.size() > 0) {

                        for (int i = 0; i < responseList.size(); i++) {
                            wineSpinnerList.add("" + (responseList.get(i)).WB_Name);
                            Log.e("atm details", responseList.get(i).toString());
                            // Populate spinner with country names
                            winelist.add(responseList.get(i));

                        }

                    }else{
                        Toast.makeText(WineShopBarActivity.this, getResources().getString(R.string.noDetailsAvailable), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError(String errorMessage) {
                    pDialog.dismiss();
                    Toast.makeText(WineShopBarActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, WineShop.class);
        } else {
            Toast.makeText(WineShopBarActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }

    }


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

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getStringImage(Bitmap bmp){
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
        if (NetUtils.isOnline(WineShopBarActivity.this)) {
            loading = new ProgressDialog(WineShopBarActivity.this);
            loading.setMessage(getResources().getString(R.string.uploadImage));
            loading.setCancelable(false);
            loading.show();
            UserNetworkManager.uploadImages(new CallBacks.StringCallBackListener() {

                @Override
                public void onSuccess(String successMessage) {
                    loading.dismiss();
                        if (successMessage.contains("200")) {
                            postWineShopDetails(wineShop);
                        } else {
                            Toast.makeText(WineShopBarActivity.this, getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
                        }

                }

                @Override
                public void onError(String errorMessage) {
                    loading.dismiss();
                    Toast.makeText(WineShopBarActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, fileList, "WineShop");
        } else {
            Toast.makeText(WineShopBarActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }


    }


    // posting wine shop details to server
    private void postWineShopDetails(WineShop wineShop) {

        loading.setMessage(getResources().getString(R.string.uploadingDetails));
        loading.setCancelable(false);
        loading.show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Wineid", wineShop.WB_ID);
            jsonObject.put("CCTVPresent", wineShop.cctv_present);
            jsonObject.put("CCTVWorking", wineShop.cctv_working);
            jsonObject.put("SecPresent", wineShop.secPerson);
            jsonObject.put("Checked", wineShop.checked);
            jsonObject.put("Timestampdata", 0);
            jsonObject.put("Userid", App.getUserId());
            jsonObject.put("latitude",wineShop.lattitude);
            jsonObject.put("longitude",wineShop.longitude);

            UserNetworkManager.postWineShopDetails(new CallBacks.StringCallBackListener() {
                @Override
                public void onSuccess(String successMessage) {
                    loading.dismiss();

                    if(successMessage != null && (successMessage.toString().trim().contains("Success")|| successMessage.toString().trim().contains("Sucess"))) {
                        Utils.showSuccessDialog(WineShopBarActivity.this);
                    }else {
                        Toast.makeText(WineShopBarActivity.this, getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    loading.dismiss();
                    Toast.makeText(WineShopBarActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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




