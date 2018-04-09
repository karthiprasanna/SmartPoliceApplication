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
import android.util.Base64;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.adapters.CustomArrayAdapter;
import ga.gasoft.smartpolice.model.Reglious;
import ga.gasoft.smartpolice.model.SlumArea;
import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.JSONfunctions;
import ga.gasoft.smartpolice.utils.RequestHandler;
import ga.gasoft.smartpolice.utils.Utils;


public class SlumAreaVisitActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
	private TextView hometitle;
	private ImageView home,back;
	private ImageView imageView;
	private static final int INTENT_REQUEST_GET_IMAGES = 20;
	private static final String UPLOAD_SLUM_AREA_DETAILS_URL ="http://10.20.30.126:3000/api/DetailsAPI/SaveSlumAreaDetails?source=";
	public static final String UPLOAD_KEY = "image";
	private LinearLayout lnrImages;
	private ArrayList<String> mStrings;
	ArrayList<Uri> image_uris = new ArrayList<Uri>();
	private ArrayList<String> slumAreaSpinnerList = new ArrayList<>();
	private Bitmap bitmap;
	private ProgressDialog pDialog;
	private List<SlumArea> slumAreaList;
	private Spinner mySpinner;
	private SlumArea slumArea;
	private Button getImages, upload;
	private SwitchCompat mobPresentSwitch, badCharacterSwitch, rowdyPresentSwitch, checkedSwitch;
	private ProgressDialog loading;
	private boolean onSubmitClicked;
	private GPSTracker gpsTracker;
	private TextView txtcountry;
	private CustomArrayAdapter adapter;
	private ImageView imageView1, imageView2, imageView3, imageView4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slumareavisit);

        mySpinner = (Spinner)findViewById(R.id.slumAreaSpinner);
		hometitle=(TextView)findViewById(R.id.kkk);
		hometitle.setText(R.string.slumareavisit);
		lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
		imageView1 = (ImageView)findViewById(R.id.img1);
		imageView2 = (ImageView)findViewById(R.id.img2);
		imageView3 = (ImageView)findViewById(R.id.img3);
		imageView4 = (ImageView)findViewById(R.id.img4);

		slumArea = new SlumArea();
		upload = (Button)findViewById(R.id.submitinfo);
		getImages = (Button)findViewById(R.id.takephoto);
		back=(ImageView) findViewById(R.id.back);
		txtcountry = (TextView) findViewById(R.id.address);
		txtcountry.setSelected(true);

		mobPresentSwitch = (SwitchCompat) findViewById(R.id.mobPresentSwitch);
		badCharacterSwitch = (SwitchCompat) findViewById(R.id.badCharacterSwitch);
		rowdyPresentSwitch = (SwitchCompat) findViewById(R.id.rowdyPresentSwitch);
		checkedSwitch = (SwitchCompat) findViewById(R.id.checkedSwitch);

		mobPresentSwitch.setOnCheckedChangeListener(this);
		badCharacterSwitch.setOnCheckedChangeListener(this);
		rowdyPresentSwitch.setOnCheckedChangeListener(this);
		checkedSwitch.setOnCheckedChangeListener(this);


		slumAreaSpinnerList.add(getResources().getString(R.string.selectName));

		getSlumAreaDetails();

		adapter = new CustomArrayAdapter(SlumAreaVisitActivity.this, R.layout.spinner_drop_down, slumAreaSpinnerList);

		mySpinner.setAdapter(adapter);

		gpsTracker = new GPSTracker(SlumAreaVisitActivity.this);

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

		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsTracker.getLocation();
				slumArea.latitude = gpsTracker.getLatitude();
				slumArea.longitude = gpsTracker.getLongitude();

				if(slumArea.SLM_ID != 0) {
					if (slumArea.checked == 1) {
						if (image_uris != null && image_uris.size() > 0) {
							onSubmitClicked = true;
							if (slumArea.latitude == 0 && slumArea.longitude == 0) {
								Toast.makeText(SlumAreaVisitActivity.this, getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
							} else {
								uploadFile(image_uris);
							}
						} else {
							Toast.makeText(SlumAreaVisitActivity.this, getResources().getString(R.string.ImagesNotSelected), Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(SlumAreaVisitActivity.this,getResources().getString(R.string.CheckNotSelected), Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(SlumAreaVisitActivity.this,getResources().getString(R.string.NoSlumSelected), Toast.LENGTH_SHORT).show();
				}

			}
		});


		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		slumAreaList = new ArrayList<>();


		mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0,
									   View arg1, int position, long arg3) {
				// TODO Auto-generated method stub


				if(position != 0) {
					txtcountry.setText(slumAreaList.get(position-1).Slumheadname);
					slumArea.SLM_ID = (slumAreaList.get(position-1)).SLM_ID;
					slumArea.Slumheadname = (slumAreaList.get(position-1)).Slumheadname;
					slumArea.Details = (slumAreaList.get(position-1)).Details;
				}else{
					txtcountry.setText("");
					txtcountry.setHint("Address");
					slumArea.SLM_ID = 0;
					slumArea.Slumheadname = "";
					slumArea.Details = "";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});


	}


	private void getSlumAreaDetails() {
		if (NetUtils.isOnline(SlumAreaVisitActivity.this)) {
			pDialog = new ProgressDialog(SlumAreaVisitActivity.this);
			pDialog.setMessage(getResources().getString(R.string.GetDetails));
			pDialog.setCancelable(false);
			pDialog.show();

			UserNetworkManager.getSlumAreaDetails(new CallBacks.ListCallBackListener<SlumArea>() {

				@Override
				public void onSuccess(List<SlumArea> responseList) {

					pDialog.dismiss();
					if (responseList != null && responseList.size() > 0) {

						for (int i = 0; i < responseList.size(); i++) {
							slumAreaSpinnerList.add("" + (responseList.get(i)).Slumheadname);
							Log.e("atm details", responseList.get(i).toString());
							// Populate spinner with country names
							slumAreaList.add(responseList.get(i));

						}

					}else{
						Toast.makeText(SlumAreaVisitActivity.this, getResources().getString(R.string.noDetailsAvailable), Toast.LENGTH_SHORT).show();
					}

				}

				@Override
				public void onError(String errorMessage) {
					pDialog.dismiss();
					Toast.makeText(SlumAreaVisitActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onStart() {

				}
			}, SlumArea.class);
		} else {
			Toast.makeText(SlumAreaVisitActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
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

	//Uploading images to server
	public void uploadFile(List<Uri> sourceFileUri) {
		List<File> fileList = new ArrayList<>();
		for (int i = 0; i < sourceFileUri.size(); i++) {
			fileList.add(new File(String.valueOf(sourceFileUri.get(i))));
		}
		if (NetUtils.isOnline(SlumAreaVisitActivity.this)) {
			loading = new ProgressDialog(SlumAreaVisitActivity.this);
			loading.setMessage(getResources().getString(R.string.uploadImage));
			loading.setCancelable(false);
			loading.show();
			UserNetworkManager.uploadImages(new CallBacks.StringCallBackListener() {

				@Override
				public void onSuccess(String successMessage) {
						if (successMessage.contains("200")) {
							postSlumAreaDetails(slumArea);
						} else {
							loading.dismiss();
							Toast.makeText(SlumAreaVisitActivity.this, getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
						}

				}

				@Override
				public void onError(String errorMessage) {
					loading.dismiss();
					Toast.makeText(SlumAreaVisitActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onStart() {

				}
			}, fileList, "SlumArea");
		} else {
			Toast.makeText(SlumAreaVisitActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
		}


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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		switch (buttonView.getId()) {

			case R.id.mobPresentSwitch:

				if (!isChecked) {
					slumArea.mobPresent = 0;
				} else {
					slumArea.mobPresent = 1;

				}
				break;
			case R.id.badCharacterSwitch:

				if(!isChecked) {
					slumArea.badCharacterPresent = 0;
				}else {
					slumArea.badCharacterPresent = 1;
				}
			    break;
			case R.id.rowdyPresentSwitch:

				if(!isChecked) {
					slumArea.rowdyPresent = 0;
				}else {
					slumArea.rowdyPresent = 1;
				}
				break;
			case R.id.checkedSwitch:

				if(!isChecked) {
					slumArea.checked = 0;
				}else {
					slumArea.checked = 1;
				}
				break;



			default:
				break;
		}


	}



	// posting slum area checked details to server.
	private void postSlumAreaDetails(SlumArea slumArea) {

		loading.setMessage(getResources().getString(R.string.uploadingDetails));
		loading.setCancelable(false);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("slumid", slumArea.SLM_ID);
			jsonObject.put("badcharacterpresent", slumArea.badCharacterPresent);
			jsonObject.put("mobpresent", slumArea.mobPresent);
			jsonObject.put("rowdypresent",slumArea.rowdyPresent);
			jsonObject.put("check",slumArea.checked);
			jsonObject.put("Userid",App.getUserId());
			jsonObject.put("Latitude",slumArea.latitude);
			jsonObject.put("Longitude",slumArea.longitude);


			UserNetworkManager.postSlumAreaDetails(new CallBacks.StringCallBackListener() {
				@Override
				public void onSuccess(String successMessage) {
					loading.dismiss();

					if(successMessage != null && (successMessage.toString().trim().contains("Success")|| successMessage.toString().trim().contains("Sucess"))) {
						Utils.showSuccessDialog(SlumAreaVisitActivity.this);
					}else {
						Toast.makeText(SlumAreaVisitActivity.this, getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onError(String errorMessage) {
					loading.dismiss();
					Toast.makeText(SlumAreaVisitActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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
