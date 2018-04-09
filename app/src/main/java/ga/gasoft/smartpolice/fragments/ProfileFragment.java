package ga.gasoft.smartpolice.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.MobActivity;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.utils.HttpHandler;
import ga.gasoft.smartpolice.MainActivity;
import ga.gasoft.smartpolice.R;
import ga.gasoft.smartpolice.model.Profile;
import ga.gasoft.smartpolice.utils.Utility;

/**
 * Created by niccapdevila on 3/26/16.
 */
public class ProfileFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    private TextView fname, lname,uname,gender,email,address,mobile,policeStationName,subname,diname,disname;
    private Profile profile;
    private List<Profile> profileDetailsList;
    private ImageView profileImage;
    private String userChoosenTask;
    public static final int SELECT_FILE = 101;
    public static final int REQUEST_CAMERA = 100;
    public Bitmap image;
    private ProgressDialog loading;

    public ProfileFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.profileusr, container, false);
        
        profileImage = (ImageView) view.findViewById(R.id.profile_image);
        
        fname=(TextView) view.findViewById(R.id.fname);

        lname=(TextView) view.findViewById(R.id.lname);

        uname=(TextView) view.findViewById(R.id.uname);

        gender=(TextView) view.findViewById(R.id.gender);

        email=(TextView) view.findViewById(R.id.email);

        address=(TextView) view.findViewById(R.id.address);

        mobile=(TextView) view.findViewById(R.id.mobile);

        policeStationName=(TextView) view.findViewById(R.id.policeStation);

        subname=(TextView) view.findViewById(R.id.subname);

        diname=(TextView) view.findViewById(R.id.diname);

        disname=(TextView) view.findViewById(R.id.disname);

        profileDetailsList = new ArrayList<Profile>();

        getProfileDetails();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        return view;
    }


//Fetching profile details from server based on user Id
    private void getProfileDetails() {

        pDialog = new ProgressDialog(getActivity());
        if(NetUtils.isOnline(getActivity())) {

            pDialog.setMessage(getResources().getString(R.string.pleasewait));
            pDialog.setCancelable(false);
            pDialog.show();
            JSONObject data = new JSONObject();


                UserNetworkManager.getUserDetails(new CallBacks.ListCallBackListener<Profile>() {


                    @Override
                    public void onSuccess(List<Profile> responseList) {
                        Log.d("responseList ", responseList.toString());

                        if(responseList != null && responseList.size() > 0 ) {

                            profileDetailsList = responseList;
                            Log.e("responselist", profileDetailsList.toString());
                            for(int i = 0 ; i < profileDetailsList.size();i++){
                                profile = profileDetailsList.get(i);
                                fname.setText(profile.firstname);
                                lname.setText(profile.lastname);
                                uname.setText(profile.username);
                                gender.setText(profile.gender);
                                email.setText(profile.email);
                                address.setText(profile.address);
                                mobile.setText(profile.mobile);
                                policeStationName.setText(profile.PS_NAME);
                                subname.setText(profile.SUB_NAME);
                                diname.setText(profile.DI_Name);
                                disname.setText(profile.DIS_NAME);
                                new DownloadImageAsyncTask().execute(profile.ImagesPath);


                            }

                        }

                    }

                    @Override
                    public void onError(String errorMessage) {


                        Log.d("On onError", "stop dialog here");

                        Log.d("errorMessage", "" + errorMessage);

                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                      pDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        Log.d("On Start", "show dialog here");

                    }


                }, Profile.class, data);





        }else {
            Toast.makeText(getActivity(),getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_LONG).show();
        }
    }


    private void selectImage() {
        final CharSequence[] items = { getResources().getString(R.string.takePhoto), getResources().getString(R.string.chooseFromGallery),
                getResources().getString(R.string.close) };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals(getResources().getString(R.string.takePhoto))) {
                    userChoosenTask=getResources().getString(R.string.takePhoto);
                    if(result)
                        cameraIntent();
                } else if (items[item].equals(getResources().getString(R.string.chooseFromGallery))) {
                    userChoosenTask = getResources().getString(R.string.chooseFromGallery);
                    if(result)
                        galleryIntent();
                } else if (items[item].equals(getResources().getString(R.string.close))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals(getResources().getString(R.string.takePhoto)))
                        cameraIntent();
                    else if(userChoosenTask.equals(getResources().getString(R.string.chooseFromGallery)))
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {

            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
           File imageFile = new File(Environment.getExternalStorageDirectory(), App.getUserId() + ".jpg");

            Log.e("URL", imageFile.toString());
            FileOutputStream fo;
            try {
                imageFile.createNewFile();
                fo = new FileOutputStream(imageFile);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        uploadFile(imageFile);
        profileImage.setImageBitmap(bm);
        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                App.getUserId() + ".jpg");

        Log.e("URL", destination.toString());
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

        uploadFile(destination);
        profileImage.setImageBitmap(thumbnail);
    }

    public class DownloadImageAsyncTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            String src = params[0];
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                 image = BitmapFactory.decodeStream(input);

//                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(image != null)
            profileImage.setImageBitmap(image);
            pDialog.dismiss();
        }
    }
//    public Bitmap getBitmapFromURL(String src) {
//
//    }

    //Uploading images to server
    public void uploadFile(File imgSrc) {


        if (NetUtils.isOnline(getActivity())) {
            loading = new ProgressDialog(getActivity());
            loading.setMessage(getResources().getString(R.string.uploadImage));
            loading.setCancelable(false);
            loading.show();
            UserNetworkManager.uploadProfileImages(new CallBacks.StringCallBackListener() {

                @Override
                public void onSuccess(String successMessage) {
                    if (successMessage.contains("200")) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.imageUploadSuccess), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    } else {
                        loading.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
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
            }, imgSrc);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }


    }


}
