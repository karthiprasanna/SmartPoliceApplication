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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.model.CheckPoint;
import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.RequestHandler;
import ga.gasoft.smartpolice.utils.Utils;

public class CheckPointActivity extends AppCompatActivity implements  CompoundButton.OnCheckedChangeListener{

	private TextView hometitle;
	private ImageView home,back;
	private ImageView imageView;
	private Button upload;
	private static final int INTENT_REQUEST_GET_IMAGES = 20;
	public static final String UPLOAD_KEY = "image";
	private LinearLayout lnrImages;
	ArrayList<Uri> image_uris = new ArrayList<Uri>();
	private Bitmap bitmap;
	private Button getImages;
	private ProgressDialog pDialog;
	private List<String> checkPointSpinnerList = new ArrayList<String>();
	private List<CheckPoint> checkPointlist;
	private Spinner mySpinner;
	private SwitchCompat checkedSwitch;
	private AutoCompleteTextView myAutoCompleteTextView;
	private EditText vehicleType, driverName, phoneNo,journeyFrom, journeyTo, noPassengers;
	private LocationManager locationManager;
	private ProgressDialog loading;
	private CheckPoint checkPoint;
	private JSONObject jsonObject;
	private GPSTracker gpsTracker;
	private ImageView imageView1, imageView2, imageView3, imageView4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkpoint);

		hometitle=(TextView)findViewById(R.id.kkk);
		hometitle.setText(R.string.checkpoint);
		lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
		imageView1 = (ImageView)findViewById(R.id.img1);
		imageView2 = (ImageView)findViewById(R.id.img2);
		imageView3 = (ImageView)findViewById(R.id.img3);
		imageView4 = (ImageView)findViewById(R.id.img4);

		upload = (Button)findViewById(R.id.submitinfo);
        myAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.regNo);
		vehicleType = (EditText)findViewById(R.id.vehicleType);
		driverName = (EditText)findViewById(R.id.driver);
		driverName.setFilters(new InputFilter[]{Utils.filtertxt});
		phoneNo = (EditText)findViewById(R.id.phone);
		journeyFrom = (EditText)findViewById(R.id.jFrom);
		journeyFrom.setFilters(new InputFilter[]{Utils.filtertxt});

		journeyTo = (EditText)findViewById(R.id.jto);
		journeyTo.setFilters(new InputFilter[]{Utils.filtertxt});

		noPassengers = (EditText)findViewById(R.id.pass);
		checkedSwitch = (SwitchCompat)findViewById(R.id.checkedSwitch);
		getImages = (Button)findViewById(R.id.takephoto);

		checkPointlist = new ArrayList<CheckPoint>();
		checkPoint = new CheckPoint();

		checkedSwitch.setOnCheckedChangeListener(this);

	   gpsTracker = new GPSTracker(CheckPointActivity.this);

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
//				getImages(new Config());
			}
		});
		
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsTracker.getLocation();
				checkPoint.latitude = gpsTracker.getLatitude();
				checkPoint.longitude = gpsTracker.getLongitude();
				if(!myAutoCompleteTextView.getText().toString().isEmpty()) {
					if(phoneNo != null && (phoneNo.getText().toString().trim().length() == 0 || (phoneNo.getText().toString().trim().length()>=10))) {
						if (checkPoint.checked == 1) {
							if (image_uris != null && image_uris.size() > 0) {

								if (checkPoint.latitude == 0 && checkPoint.longitude == 0) {
									Toast.makeText(CheckPointActivity.this, getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
								} else {
									uploadFile(image_uris);
								}
							} else {
								Toast.makeText(CheckPointActivity.this, getResources().getString(R.string.ImagesNotSelected), Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(CheckPointActivity.this, getResources().getString(R.string.CheckNotSelected), Toast.LENGTH_SHORT).show();
						}
					}
					else{
						Toast.makeText(CheckPointActivity.this, getResources().getString(R.string.InvalidPhoneNo), Toast.LENGTH_SHORT).show();
					}

				}else{
					Toast.makeText(CheckPointActivity.this, getResources().getString(R.string.RegNoNotSelected), Toast.LENGTH_SHORT).show();
				}
			}
		});



		back=(ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});


		getCheckPointDetails();

		myAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				String selected = (String) arg0.getAdapter().getItem(arg2);
				checkPoint = checkPointlist.get(arg2);
				Log.e("selected", selected+"......."+checkPoint);
				vehicleType.setText(checkPoint.VehicalType);
				driverName.setText(checkPoint.DriverName);
				phoneNo.setText(checkPoint.Phone);
				journeyFrom.setText(checkPoint.JourneyFrom);
				journeyTo.setText(checkPoint.JourneyTo);
				noPassengers.setText(String.valueOf(checkPoint.NoOfPass));

			}
		});

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

	// Method for fetching checkPoint details
	private void getCheckPointDetails() {
		if (NetUtils.isOnline(CheckPointActivity.this)) {
			pDialog = new ProgressDialog(CheckPointActivity.this);
			pDialog.setMessage(getResources().getString(R.string.GetDetails));
			pDialog.setCancelable(false);
			pDialog.show();

			UserNetworkManager.getCheckPointDetails(new CallBacks.ListCallBackListener<CheckPoint>() {

				@Override
				public void onSuccess(List<CheckPoint> responseList) {

					pDialog.dismiss();
					if (responseList != null && responseList.size() > 0) {

						for (int i = 0; i < responseList.size(); i++) {

							checkPointSpinnerList.add((responseList.get(i)).RegNo);
							Log.e("check point details", responseList.get(i).toString());
							// Populate spinner with country names
							checkPointlist.add(responseList.get(i));

						}
						myAutoCompleteTextView.setAdapter(new ArrayAdapter<String>(CheckPointActivity.this,
								android.R.layout.simple_spinner_dropdown_item,
								checkPointSpinnerList));
					}

				}

				@Override
				public void onError(String errorMessage) {
					pDialog.dismiss();
					Toast.makeText(CheckPointActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onStart() {

				}
			}, CheckPoint.class);
		} else {
			Toast.makeText(CheckPointActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if(!isChecked){
            checkPoint.checked = 0;
		}else{
			checkPoint.checked = 1;
		}
	}


	//Uploading images to server
	public void uploadFile(List<Uri> sourceFileUri) {
		List<File> fileList = new ArrayList<>();
		for (int i = 0; i < sourceFileUri.size(); i++) {
			fileList.add(new File(String.valueOf(sourceFileUri.get(i))));
		}
		if (NetUtils.isOnline(CheckPointActivity.this)) {
			loading = new ProgressDialog(CheckPointActivity.this);
			loading.setMessage(getResources().getString(R.string.uploadImage));
			loading.setCancelable(false);
			loading.show();
			UserNetworkManager.uploadImages(new CallBacks.StringCallBackListener() {

				@Override
				public void onSuccess(String successMessage) {

						if (successMessage.contains("200")) {
							postCheckPointDetails(checkPoint);
						} else {
							loading.dismiss();
							Toast.makeText(CheckPointActivity.this, getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
						}

				}

				@Override
				public void onError(String errorMessage) {
					loading.dismiss();
					Toast.makeText(CheckPointActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onStart() {

				}
			}, fileList, "CheckPoint");
		} else {
			Toast.makeText(CheckPointActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
		}


	}


	//After success of images posting check point  details to server
	private void postCheckPointDetails(CheckPoint checkPoint) {

		    loading.setMessage(getResources().getString(R.string.uploadingDetails));
			loading.setCancelable(false);
			Log.e("post method", "called"+checkPoint.toString());

			try {
				jsonObject = new JSONObject();
				if(checkPoint.CHK_ID != 0){
					jsonObject.put("checkid", checkPoint.CHK_ID);
				}

				if(checkPoint.CheckType != null){
					jsonObject.put("checktype", (checkPoint.CheckType));
				}

				jsonObject.put("userid", App.getUserId());
				jsonObject.put("Vehicaltype", vehicleType.getText().toString().trim());
				jsonObject.put("RegNo", myAutoCompleteTextView.getText().toString().trim());
				jsonObject.put("DriverName", driverName.getText().toString().trim());
				jsonObject.put("Phone", phoneNo.getText().toString().trim());
				jsonObject.put("Journeyfrom", journeyFrom.getText().toString().trim());
				jsonObject.put("JourneyTo", journeyTo.getText().toString().trim());
				jsonObject.put("NoOfPassangers", Integer.parseInt(noPassengers.getText().toString().trim()));
				jsonObject.put("check", checkPoint.checked);
				jsonObject.put("Latitude", checkPoint.latitude);
				jsonObject.put("Longitude", checkPoint.longitude);

				Log.e("jsonObject", "" + jsonObject);


				UserNetworkManager.postCheckPointDetails(new CallBacks.ObjectCallBackListener() {
					@Override
					public void onSuccess(Object responseObject) {
						loading.dismiss();

						if(responseObject != null && (responseObject.toString().trim().contains("Success")|| responseObject.toString().trim().contains("Sucess"))) {
							Utils.showSuccessDialog(CheckPointActivity.this);
						}else {
							Toast.makeText(CheckPointActivity.this, getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onError(String errorMessage) {
						loading.dismiss();
						Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onErrorWithData(JSONObject errorjson) {
						loading.dismiss();
						Log.e("error",""+errorjson.toString());
						Toast.makeText(getApplication(), getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onStart() {

					}
				},CheckPointActivity.this,jsonObject);


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
