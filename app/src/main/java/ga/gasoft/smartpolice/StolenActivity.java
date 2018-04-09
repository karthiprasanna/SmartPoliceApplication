package ga.gasoft.smartpolice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import ga.gasoft.smartpolice.model.StolenProperty;
import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.RequestHandler;
import ga.gasoft.smartpolice.utils.Utils;


public class StolenActivity extends AppCompatActivity  implements CompoundButton.OnCheckedChangeListener{
	private TextView hometitle;
	private ImageView home,back;
	private ImageView imageView;
	private static final int INTENT_REQUEST_GET_IMAGES = 20;
	public static final String UPLOAD_KEY = "image";
	private LinearLayout lnrImages;
	ArrayList<Uri> image_uris = new ArrayList<Uri>();
	private GPSTracker gpsTracker;
	private double latitude, longitude;
	private Bitmap bitmap;
	private ProgressDialog loading;
	private StolenProperty stolenProperty;
	private EditText nameEditText, addressEditText, phoneNoEditText;
	private SwitchCompat checkedButton;
	private JSONObject jsonObject;
	private ImageView imageView1, imageView2, imageView3, imageView4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stolenproperty);


		hometitle=(TextView)findViewById(R.id.kkk);
		hometitle.setText(R.string.stolenproperty);
		lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
		imageView1 = (ImageView)findViewById(R.id.img1);
		imageView2 = (ImageView)findViewById(R.id.img2);
		imageView3 = (ImageView)findViewById(R.id.img3);
		imageView4 = (ImageView)findViewById(R.id.img4);

		nameEditText = (EditText) findViewById(R.id.name);
		nameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		addressEditText = (EditText) findViewById(R.id.address);
		phoneNoEditText = (EditText) findViewById(R.id.phone);
		checkedButton = (SwitchCompat) findViewById(R.id.checkedSwitch);
		back=(ImageView) findViewById(R.id.back);

		View getImages = findViewById(R.id.takephoto);
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
		View upload = findViewById(R.id.submitinfo);

		gpsTracker = new GPSTracker(StolenActivity.this);
		latitude = gpsTracker.getLatitude();
		longitude = gpsTracker.getLongitude();

		Log.e("latitude.."+latitude,"longitude.."+longitude);
		stolenProperty = new StolenProperty();

		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsTracker.getLocation();
				stolenProperty.latitude = gpsTracker.getLatitude();
				stolenProperty.longitude = gpsTracker.getLongitude();

				if(nameEditText != null && !nameEditText.getText().toString().trim().isEmpty()) {
					if(phoneNoEditText != null &&  (phoneNoEditText.getText().toString().trim().length()>=10)) {
					if (stolenProperty.checked == 1) {
						if (image_uris != null && image_uris.size() > 0) {
							if (stolenProperty.latitude == 0 && stolenProperty.longitude == 0) {
								Toast.makeText(StolenActivity.this, getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
							} else {
								uploadFile(image_uris);
							}
						} else {
							Toast.makeText(StolenActivity.this, getResources().getString(R.string.ImagesNotSelected), Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(StolenActivity.this, getResources().getString(R.string.CheckNotSelected), Toast.LENGTH_SHORT).show();
					}
				}else {
						Toast.makeText(StolenActivity.this, getResources().getString(R.string.InvalidPhoneNo), Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(StolenActivity.this, getResources().getString(R.string.NameNotSelected), Toast.LENGTH_SHORT).show();
				}

			}
		});


		checkedButton.setOnCheckedChangeListener(this);


		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
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


	//Uploading images to server
	public void uploadFile(List<Uri> sourceFileUri) {
		List<File> fileList = new ArrayList<>();
		for (int i = 0; i < sourceFileUri.size(); i++) {
			fileList.add(new File(String.valueOf(sourceFileUri.get(i))));
		}
		if (NetUtils.isOnline(StolenActivity.this)) {
			loading = new ProgressDialog(StolenActivity.this);
			loading.setMessage(getResources().getString(R.string.uploadImage));
			loading.setCancelable(false);
			loading.show();
			UserNetworkManager.uploadImages(new CallBacks.StringCallBackListener() {

				@Override
				public void onSuccess(String successMessage) {
						if (successMessage.contains("200")) {
							postStolenPropertyDetails(stolenProperty);
						} else {
							loading.dismiss();
							Toast.makeText(StolenActivity.this, getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
						}

				}

				@Override
				public void onError(String errorMessage) {
					loading.dismiss();
					Toast.makeText(StolenActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onStart() {

				}
			}, fileList, "StolenProperty");
		} else {
			Toast.makeText(StolenActivity.this,getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
		}


	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		switch (buttonView.getId()) {

			case R.id.checkedSwitch:

				if (!isChecked) {
					stolenProperty.checked = 0;
				} else {
					stolenProperty.checked = 1;

				}
				break;

			default:
				break;
		}
	}


		//After success of images posting stolen property  details to server
	private void postStolenPropertyDetails(StolenProperty stolenProperty) {

		loading.setMessage(getResources().getString(R.string.uploadingDetails));
		loading.setCancelable(false);
		Log.e("post method", "called"+stolenProperty.toString());

		try {
			jsonObject = new JSONObject();
			jsonObject.put("Name", nameEditText.getText().toString().trim());
			jsonObject.put("Address", addressEditText.getText().toString().trim());
			jsonObject.put("Phone", phoneNoEditText.getText().toString().trim());
			jsonObject.put("userid", App.getUserId());
			jsonObject.put("check", stolenProperty.checked);
			jsonObject.put("Latitude", stolenProperty.latitude);
			jsonObject.put("Longitude", stolenProperty.longitude);

			UserNetworkManager.postStolenPropertyDetails(new CallBacks.ObjectCallBackListener() {
				@Override
				public void onSuccess(Object responseObject) {
					loading.dismiss();

					if(responseObject != null && (responseObject.toString().trim().contains("Success")|| responseObject.toString().trim().contains("Sucess"))) {
						Utils.showSuccessDialog(StolenActivity.this);
					}else {
						Toast.makeText(StolenActivity.this, getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
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
			},StolenActivity.this,jsonObject);


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
