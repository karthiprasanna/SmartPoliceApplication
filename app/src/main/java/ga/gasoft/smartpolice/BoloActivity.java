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
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.adapters.CustomArrayAdapter;
import ga.gasoft.smartpolice.model.Bolo;
import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.Utils;


public class BoloActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener  {

    private TextView hometitle;
    private SwitchCompat checkedSwitch;
    private static final int INTENT_REQUEST_GET_IMAGES = 20;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private Bitmap bitmap;
    private Button upload, getImagesButton;
    private LinearLayout lnrImagesLayout;
    private ImageView back, imageView;
    private Bolo bolo;
    private int SELECT_FILE =101;
    private int REQUEST_CAMERA = 100;
    private ArrayList<String> imagesPathList;
    private ProgressDialog pDialog;
    private List<String> boloSpinnerList;
    private Spinner mySpinner;
    private List<Bolo> boloList;
    private ProgressDialog loading;
    private JSONObject jsonObject;
    private GPSTracker gpsTracker;
    private CustomArrayAdapter adapter;
    private ImageView imageView1, imageView2, imageView3, imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolo);
        hometitle=(TextView)findViewById(R.id.kkk);
        hometitle.setText(R.string.bolo);
        lnrImagesLayout = (LinearLayout) findViewById(R.id.lnrImages);
        imageView1 = (ImageView)findViewById(R.id.img1);
        imageView2 = (ImageView)findViewById(R.id.img2);
        imageView3 = (ImageView)findViewById(R.id.img3);
        imageView4 = (ImageView)findViewById(R.id.img4);

        upload = (Button)findViewById(R.id.submitinfo);
        getImagesButton = (Button)findViewById(R.id.takephoto);
        back =(ImageView) findViewById(R.id.back);
        checkedSwitch = (SwitchCompat)findViewById(R.id.checkedSwitch);
        mySpinner = (Spinner) findViewById(R.id.my_spinner);


        bolo = new Bolo();
        boloList = new ArrayList<>();
        boloSpinnerList = new ArrayList<>();

        checkedSwitch.setOnCheckedChangeListener(this);

        gpsTracker = new GPSTracker(BoloActivity.this);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        boloSpinnerList.add(getResources().getString(R.string.selectName));

        getBoloDetails();



        adapter = new CustomArrayAdapter(BoloActivity.this, R.layout.spinner_drop_down, boloSpinnerList);

        mySpinner.setAdapter(adapter);
        

        getImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                com.gun0912.tedpicker.Config config = new com.gun0912.tedpicker.Config();
                config.setCameraHeight(R.dimen.app_camera_height);
                config.setToolbarTitleRes(R.string.custom_title);
                config.setSelectionMin(1);
                config.setSelectionLimit(4);
                config.setSelectedBottomHeight(R.dimen.bottom_height);
                config.setFlashOn(true);
                getImages(config);
            }
        });
//       getImages(new Config());
//                selectImage();


        // Spinner on item click listener
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int position, long arg3) {
                // TODO Auto-generated method stub

                if(position != 0 ) {
                    bolo.id = boloList.get(position-1).id;
                    bolo.boloname = boloList.get(position-1).boloname;
                }else{
                    bolo.id = 0;
                    bolo.boloname = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker.getLocation();
                bolo.latitude = gpsTracker.getLatitude();
                bolo.longitude = gpsTracker.getLongitude();
                if (bolo.boloname != null && !bolo.boloname.isEmpty()) {
                    if (bolo.checked == 1) {
                        if (image_uris != null && image_uris.size() > 0) {

                            if (bolo.latitude == 0 && bolo.longitude == 0) {
                                Toast.makeText(BoloActivity.this, getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
                            } else {
                                uploadFile(image_uris);
                            }
                        } else {
                            Toast.makeText(BoloActivity.this, getResources().getString(R.string.ImagesNotSelected), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BoloActivity.this, getResources().getString(R.string.CheckNotSelected), Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(BoloActivity.this, getResources().getString(R.string.NameNotSelected), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.checkedSwitch:

                if(!isChecked){
                    bolo.checked = 0;
                }else{
                    bolo.checked = 1;
                }
                break;
        }
    }

    private void getImages(Config config) {


        com.gun0912.tedpicker.ImagePickerActivity.setConfig(config);

        Intent intent = new Intent(this, com.gun0912.tedpicker.ImagePickerActivity.class);

        if (image_uris != null) {
            intent.putParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
        }


        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

    }



   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                   *//* imagesPathList = new ArrayList<String>();
                    String[] imagesPath = intent.getStringExtra("data").split("\\|");
                    try{
                        lnrImagesLayout.removeAllViews();
                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                    for (int i=0;i<imagesPath.length;i++){
                        imagesPathList.add(imagesPath[i]);
                        bitmap = BitmapFactory.decodeFile(imagesPath[i]);
                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(bitmap);
                        imageView.setAdjustViewBounds(true);
                        lnrImagesLayout.addView(imageView);
                    }
                Log.e("image_path list", imagesPathList.toString());*//*

                image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                lnrImagesLayout.removeAllViews();
                for (int i=0;i<image_uris.size();i++){

                    bitmap = BitmapFactory.decodeFile(String.valueOf(image_uris.get(i)));
                    imageView = new ImageView(this);
                    bitmap = getResizedBitmap(bitmap, 256);
                    imageView.setImageBitmap(bitmap);

                    imageView.setAdjustViewBounds(true);

                    lnrImagesLayout.addView(imageView);
                }

            }
        *//*else if (requestCode == REQUEST_CAMERA) {
              Bitmap thumbnail = (Bitmap) intent.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                imageView.setLayoutParams(layoutParams);
                imageView.setImageBitmap(thumbnail);
                imageView.setAdjustViewBounds(true);
                lnrImagesLayout.addView(imageView);*//*
//        }
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                imageView1.setVisibility(View.GONE);
                imageView2.setVisibility(View.GONE);
                imageView3.setVisibility(View.GONE);
                imageView4.setVisibility(View.GONE);
                image_uris = intent.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);

                if(image_uris != null && image_uris.size()>0)
                    lnrImagesLayout.setVisibility(View.VISIBLE);
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



  /*  private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(BoloActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(BoloActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }*/

   /* private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent(BoloActivity.this, CustomPhotoGalleryActivity.class);
        intent.setType("image*//*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }*/

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
//                onSelectFromGalleryResult(data);

            } else if (requestCode == REQUEST_CAMERA) {
//                onCaptureImageResult(data);
            }
        }
    }*/

    //Uploading images to server
    public void uploadFile(List<Uri> sourceFileUri) {
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < sourceFileUri.size(); i++) {
            fileList.add(new File(String.valueOf(sourceFileUri.get(i))));
        }
        if (NetUtils.isOnline(BoloActivity.this)) {
            loading = new ProgressDialog(BoloActivity.this);
            loading.setMessage(getResources().getString(R.string.uploadImage));
            loading.setCancelable(false);
            loading.show();
            UserNetworkManager.uploadImages(new CallBacks.StringCallBackListener() {

                @Override
                public void onSuccess(String successMessage) {

                        if (successMessage.contains("200")) {
                            postBoloDetails(bolo);
                        } else {
                            loading.dismiss();
                            Toast.makeText(BoloActivity.this, getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
                        }

                }

                @Override
                public void onError(String errorMessage) {
                    loading.dismiss();
                    Toast.makeText(BoloActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, fileList, "Bolo");
        } else {
            Toast.makeText(BoloActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void finish() {
        gpsTracker.stopUsingGPS();
        super.finish();
    }

    private void selectImages() {

        Intent intent = new Intent(BoloActivity.this, ImagePickerActivity.class);
        startActivityForResult(intent, 200);
    }

   /* private void launchImagePickerActivity() {
        Intent intent = new Intent(BoloActivity.this, ImagePickerActivity.class);

        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                .setTabSelectionIndicatorColor(R.color.black)
                .setCameraButtonColor(R.color.blue)
                .setSelectionLimit(5)    // set photo selection limit. Default unlimited selection.
                .build();
//        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, 200);
    }*/


    // Method for fetching Bolo details
    private void getBoloDetails() {
        if (NetUtils.isOnline(BoloActivity.this)) {
            pDialog = new ProgressDialog(BoloActivity.this);
            pDialog.setMessage(getResources().getString(R.string.GetDetails));
            pDialog.setCancelable(false);
            pDialog.show();

            UserNetworkManager.getBoloDetails(new CallBacks.ListCallBackListener<Bolo>() {

                @Override
                public void onSuccess(List<Bolo> responseList) {

                    pDialog.dismiss();
                    if (responseList != null && responseList.size() > 0) {

                        for (int i = 0; i < responseList.size(); i++) {
                            boloSpinnerList.add((responseList.get(i)).boloname);
                            Log.e("bolo details", responseList.get(i).toString());
                            // Populate spinner with country names
                            boloList.add(responseList.get(i));

                        }

                    }else{
                        Toast.makeText(BoloActivity.this, getResources().getString(R.string.noDetailsAvailable), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onError(String errorMessage) {
                    pDialog.dismiss();
                    Toast.makeText(BoloActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, Bolo.class);
        } else {
            Toast.makeText(BoloActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }

    }

    //After success of images posting bolo details to server
    public void postBoloDetails(Bolo bolo) {

        loading.setMessage(getResources().getString(R.string.uploadingDetails));
        loading.setCancelable(false);
        Log.e("post method", "called");

        try {
            jsonObject = new JSONObject();
            jsonObject.put("boloName", bolo.boloname);
            jsonObject.put("boloid", bolo.id);
            jsonObject.put("Userid", App.getUserId());
            jsonObject.put("check", bolo.checked);
            jsonObject.put("Latitude",bolo.latitude);
            jsonObject.put("Longitude", bolo.longitude);


            Log.e("jsonObject", "" + jsonObject);


            UserNetworkManager.postBoloDetails(new CallBacks.ObjectCallBackListener() {
                @Override
                public void onSuccess(Object responseObject) {
                    loading.dismiss();

                    if(responseObject != null && (responseObject.toString().trim().contains("Success") || responseObject.toString().trim().contains("Sucess"))) {
                        Utils.showSuccessDialog(BoloActivity.this);
                    }else {
                        Toast.makeText(BoloActivity.this, getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String errorMessage) {

                    loading.dismiss();
                    Toast.makeText(BoloActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onErrorWithData(JSONObject errorjson) {
                    loading.dismiss();
                    Toast.makeText(BoloActivity.this, getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onStart() {

                }
            }, BoloActivity.this,jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
