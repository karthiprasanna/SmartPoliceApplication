package ga.gasoft.smartpolice.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.PayingGuestActivity;
import ga.gasoft.smartpolice.R;
import ga.gasoft.smartpolice.model.ComplaintStatus;
import ga.gasoft.smartpolice.model.ComplaintType;
import ga.gasoft.smartpolice.utils.GPSTracker;

import ga.gasoft.smartpolice.utils.Utils;

/**
 * Created by niccapdevila on 3/26/16.
 */
public class ComplaintFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {


    public static final String ARG_PAGE = "ARG_PAGE";
    private ImageView imageView;
    private static final int INTENT_REQUEST_GET_IMAGES = 20;
    public static final String UPLOAD_URL = "http://10.20.30.112:8080/imageupload/uploadimage.php";
    public static final String UPLOAD_KEY = "image";
    private LinearLayout lnrImages;
    private ArrayList<String> mStrings;
    ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private Bitmap bitmap;
    private EditText nameEditText, addressEditText;
    private SwitchCompat checkedSwitch;
    private AutoCompleteTextView complaintTypeTextView, complaintStatusTextView;
    private GPSTracker gpsTracker;
    private double latitude, longitude;
    private int checked;
    private ProgressDialog loading, pDialog1, pDialog;
    List<String> complaintTypeList = new ArrayList<String>();
    List<String> complaintStatusList = new ArrayList<String>();
    private JSONObject jsonObject;
    private ImageView imageView1, imageView2, imageView3, imageView4;

    public ComplaintFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        ComplaintFragment fragment = new ComplaintFragment();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.complaint, container, false);
        nameEditText = (EditText) view.findViewById(R.id.name);
        nameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

        addressEditText = (EditText) view.findViewById(R.id.address);

        checkedSwitch = (SwitchCompat) view.findViewById(R.id.checkedSwitch);

        complaintTypeTextView = (AutoCompleteTextView) view.findViewById(R.id.complaintType);

        complaintStatusTextView = (AutoCompleteTextView) view.findViewById(R.id.complaintStatus);

        lnrImages = (LinearLayout) view.findViewById(R.id.lnrImages);
        imageView1 = (ImageView)view.findViewById(R.id.img1);
        imageView2 = (ImageView)view.findViewById(R.id.img2);
        imageView3 = (ImageView)view.findViewById(R.id.img3);
        imageView4 = (ImageView)view.findViewById(R.id.img4);



        gpsTracker = new GPSTracker(getActivity());

        checkedSwitch.setOnCheckedChangeListener(this);


        View getImages = view.findViewById(R.id.takephoto);
        getImages.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

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
        View upload = view.findViewById(R.id.submitinfo);

        upload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gpsTracker.getLocation();
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();
                if (complaintTypeTextView != null && !complaintTypeTextView.getText().toString().trim().isEmpty() && complaintStatusTextView != null && !complaintStatusTextView.getText().toString().trim().isEmpty()) {
                    if (NetUtils.isOnline(getActivity())) {
                        if (checked == 1) {
                            if (image_uris != null && image_uris.size() > 0) {
                                if (latitude == 0 && longitude == 0) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
                                } else {
                                    uploadFile(image_uris);
                                }
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.ImagesNotSelected), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.CheckNotSelected), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getActivity(), getResources().getString(R.string.MandatoryNotSelected), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            getComplaintType();
        }else{
            // fragment is no longer visible
        }
    }



    private void getImages(Config config) {


        ImagePickerActivity.setConfig(config);

        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);

        if (image_uris != null) {
            intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
        }


        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
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

    // fetching complaint type details
    private void getComplaintType(){
        if(NetUtils.isOnline(getActivity())){

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getResources().getString(R.string.GetDetails));
            pDialog.setCancelable(false);
            pDialog.show();

            UserNetworkManager.getComplaintType(new CallBacks.ListCallBackListener<ComplaintType>() {
                @Override
                public void onSuccess(List responseList) {
                    if(responseList != null && responseList.size()>0) {
                        for (int i = 0; i < responseList.size();i++) {
                            complaintTypeList.add(((ComplaintType)responseList.get(i)).C_Name);
                        }
                        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_dropdown_item_1line, complaintTypeList);
                        complaintTypeTextView.setAdapter(arrayAdapter1);
                        getComplaintStatus();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    pDialog.dismiss();
                }

                @Override
                public void onStart() {

                }
            },ComplaintType.class);

        }else{
            if(getActivity() != null)
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }

    }

    //fetching complaint status list from server
    private void getComplaintStatus(){
        if(NetUtils.isOnline(getActivity())){
            pDialog.setMessage(getResources().getString(R.string.GetDetails));
            pDialog.setCancelable(false);
            UserNetworkManager.getComplaintStatus(new CallBacks.ListCallBackListener<ComplaintStatus>() {
                @Override
                public void onSuccess(List responseList) {
                    if(responseList != null && responseList.size()>0) {
                        for (int i = 0; i < responseList.size();i++) {
                            complaintStatusList.add(((ComplaintStatus)responseList.get(i)).CS_Name);
                            ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_dropdown_item_1line, complaintStatusList);
                            complaintStatusTextView.setAdapter(arrayAdapter2);
                            pDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    pDialog.dismiss();
                }

                @Override
                public void onStart() {

                }
            },ComplaintStatus.class);

        }else{
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }

    }

    //Uploading images to server
    public void uploadFile(List<Uri> sourceFileUri) {
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < sourceFileUri.size(); i++) {
            fileList.add(new File(String.valueOf(sourceFileUri.get(i))));
        }
        if (NetUtils.isOnline(getActivity())) {
            loading = new ProgressDialog(getActivity());
            loading.setMessage(getResources().getString(R.string.uploadImage));
            loading.setCancelable(false);
            loading.show();
            UserNetworkManager.uploadImages(new CallBacks.StringCallBackListener() {

                @Override
                public void onSuccess(String successMessage) {

                    if (NetUtils.isOnline(getActivity())) {
                        if (successMessage.contains("200")) {
                            postComplaintDetails();
                        } else {
                            loading.dismiss();
                            Toast.makeText(getActivity(), getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    loading.dismiss();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, fileList, "Complaint");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }


    }




    //After success of images posting complaint details to server
    private void postComplaintDetails() {

        loading.setMessage(getResources().getString(R.string.uploadingDetails));
        loading.setCancelable(false);
        loading.show();


        try {
            jsonObject = new JSONObject();
            jsonObject.put("name", nameEditText.getText().toString().trim());
            jsonObject.put("address", addressEditText.getText().toString().trim());
            jsonObject.put("complainttype", complaintTypeTextView.getText().toString().trim());
            jsonObject.put("complaintstatus", complaintStatusTextView.getText().toString().trim());
            jsonObject.put("userid", App.getUserId());
            jsonObject.put("check", checked);
            jsonObject.put("latitude", latitude);
            jsonObject.put("longnitude", longitude);

            UserNetworkManager.postComplaintDetails(new CallBacks.ObjectCallBackListener() {
                @Override
                public void onSuccess(Object responseObject) {
                    loading.dismiss();

                    if(responseObject != null && (responseObject.toString().trim().contains("Success")|| responseObject.toString().trim().contains("Sucess"))) {
                        Utils.showSuccessDialog(getActivity());
                    }else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    loading.dismiss();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onErrorWithData(JSONObject errorjson) {
                    loading.dismiss();
                    Log.e("error",""+errorjson.toString());
                    Toast.makeText(getActivity(), getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            },getActivity(),jsonObject);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked)
            checked = 1;
        else
            checked = 0;
    }
}
