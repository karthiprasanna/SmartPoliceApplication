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
import ga.gasoft.smartpolice.model.WarrantAndNotice;
import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.RequestHandler;
import ga.gasoft.smartpolice.utils.Utils;


public class WarrantNoticeActivity extends AppCompatActivity  implements  CompoundButton.OnCheckedChangeListener{

	private TextView hometitle;
	private ImageView home,back;
	private ImageView imageView;
	private Button upload;
	private static final int INTENT_REQUEST_GET_IMAGES = 20;
	public static final String UPLOAD_URL = "http://10.20.30.112:8080/imageupload/uploadimage.php";
	public static final String UPLOAD_KEY = "image";
	private LinearLayout lnrImages;
	private ArrayList<String> mStrings;
	private static final String TAG = "TedPicker";
	ArrayList<Uri> image_uris = new ArrayList<Uri>();
	private ProgressDialog loading;
	private WarrantAndNotice warrantAndNotice;
	private EditText nameEditText, detailsEditText;
	private SwitchCompat checkedButton;
	private JSONObject jsonObject;

	private Bitmap bitmap;
	private GPSTracker gpsTracker;
	private double latitude, longitude;
	private ImageView imageView1, imageView2, imageView3, imageView4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warrantnotice);

		hometitle=(TextView)findViewById(R.id.kkk);
		hometitle.setText(R.string.warrantandnotice);
		lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
		imageView1 = (ImageView)findViewById(R.id.img1);
		imageView2 = (ImageView)findViewById(R.id.img2);
		imageView3 = (ImageView)findViewById(R.id.img3);
		imageView4 = (ImageView)findViewById(R.id.img4);

		nameEditText = (EditText) findViewById(R.id.name);
		nameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		detailsEditText = (EditText) findViewById(R.id.detailsEditText);
		checkedButton = (SwitchCompat) findViewById(R.id.checkedSwitch);
		View upload = findViewById(R.id.submitinfo);
		View getImages = findViewById(R.id.takephoto);
		gpsTracker = new GPSTracker(WarrantNoticeActivity.this);
		latitude = gpsTracker.getLatitude();
		longitude = gpsTracker.getLongitude();
		warrantAndNotice = new WarrantAndNotice();

		Log.e("latitude.."+latitude,"longitude.."+longitude);
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

		checkedButton.setOnCheckedChangeListener(this);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsTracker.getLocation();
				warrantAndNotice.latitude = gpsTracker.getLatitude();
				warrantAndNotice.longitude = gpsTracker.getLongitude();
				if(nameEditText != null && !nameEditText.getText().toString().trim().isEmpty()) {
					if (warrantAndNotice.checked == 1) {
						if (image_uris != null && image_uris.size() > 0) {
							if (warrantAndNotice.latitude == 0 && warrantAndNotice.longitude == 0) {
								Toast.makeText(WarrantNoticeActivity.this, getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
							} else {
								uploadFile(image_uris);
							}
						} else {
							Toast.makeText(WarrantNoticeActivity.this, getResources().getString(R.string.ImagesNotSelected), Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(WarrantNoticeActivity.this, getResources().getString(R.string.CheckNotSelected), Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(WarrantNoticeActivity.this, getResources().getString(R.string.NameNotSelected), Toast.LENGTH_SHORT).show();
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
		if (NetUtils.isOnline(WarrantNoticeActivity.this)) {
			loading = new ProgressDialog(WarrantNoticeActivity.this);
			loading.setMessage(getResources().getString(R.string.uploadImage));
			loading.setCancelable(false);
			loading.show();
			UserNetworkManager.uploadImages(new CallBacks.StringCallBackListener() {

				@Override
				public void onSuccess(String successMessage) {
					loading.dismiss();
						if (successMessage.contains("200")) {
							postWarrantAndNoticeDetails(warrantAndNotice);
						} else {
							Toast.makeText(WarrantNoticeActivity.this, getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
						}

				}

				@Override
				public void onError(String errorMessage) {
					loading.dismiss();
					Toast.makeText(WarrantNoticeActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onStart() {

				}
			}, fileList, "WarrantAndNotice");
		} else {
			Toast.makeText(WarrantNoticeActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
		}


	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		switch (buttonView.getId()) {

			case R.id.checkedSwitch:

				if (!isChecked) {
					warrantAndNotice.checked = 0;
				} else {
					warrantAndNotice.checked = 1;

				}
				break;

			default:
				break;
		}
	}


	//After success of images posting warrant  details to server
	private void postWarrantAndNoticeDetails(WarrantAndNotice warrantAndNotice) {

		loading.setMessage(getResources().getString(R.string.uploadingDetails));
		loading.setCancelable(false);
		loading.show();
		Log.e("post method", "called"+warrantAndNotice.toString());


		try {
			jsonObject = new JSONObject();
			jsonObject.put("name", nameEditText.getText().toString().trim());
			jsonObject.put("WDetails", detailsEditText.getText().toString().trim());
			jsonObject.put("userid", App.getUserId());
			jsonObject.put("check", warrantAndNotice.checked);
			jsonObject.put("Latitude", warrantAndNotice.latitude);
			jsonObject.put("Longitude", warrantAndNotice.longitude);

			UserNetworkManager.postWarrantAndNoticeDetails(new CallBacks.ObjectCallBackListener() {
				@Override
				public void onSuccess(Object responseObject) {
					loading.dismiss();

					if(responseObject != null && (responseObject.toString().trim().contains("Success")|| responseObject.toString().trim().contains("Sucess"))) {
						Utils.showSuccessDialog(WarrantNoticeActivity.this);
					}else {
						Toast.makeText(WarrantNoticeActivity.this, getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
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
			},WarrantNoticeActivity.this,jsonObject);


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
