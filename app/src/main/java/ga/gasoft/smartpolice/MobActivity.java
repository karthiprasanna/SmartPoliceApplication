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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.model.MobType;
import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.RequestHandler;
import ga.gasoft.smartpolice.utils.Utils;


public class MobActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

	private TextView hometitle;
	private ImageView home,back;
	private ImageView imageView;
	private Button upload;
	private static final int INTENT_REQUEST_GET_IMAGES = 20;
	public static final String UPLOAD_KEY = "image";
	private LinearLayout lnrImages;
	ArrayList<Uri> image_uris = new ArrayList<Uri>();
	private Bitmap bitmap;
	private ArrayList<String> mobSpinnerList;
	private ArrayAdapter<String> mobTypeArrayAdapter;
	private ProgressDialog pDialog;
	private EditText nameEditText, addressEditText, phoneEditText, familyNameEditText, neighbourNameEditText, neighbourNumberEditText,
	 employerNameEditText, employerAddressEditText, advocateNameEditText, advocateNumberEditText, numberCaseEditText, gangNameEditText,
	alibiNameEditText, economicalConditionEditText, miscInfoEditText, previousIncidentEditText;
	private SwitchCompat punishedSwitch, checkedSwitch;
	private MobType mobType;
	private GPSTracker gpsTracker;
	private double latitude, longitude;
	private ArrayList<MobType> mobTypeList;
	private ProgressDialog loading;
	private JSONObject jsonObject;
	private AutoCompleteTextView mobTypeTextView;
	private ImageView imageView1, imageView2, imageView3, imageView4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mob);

		mobSpinnerList = new ArrayList<>();
		mobTypeList = new ArrayList<MobType>();

        mobType = new MobType();
		mobTypeTextView = (AutoCompleteTextView) findViewById(R.id.mobType);
		nameEditText = (EditText)findViewById(R.id.name);
		nameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		addressEditText = (EditText)findViewById(R.id.address);
		phoneEditText = (EditText)findViewById(R.id.phoneNo);
		familyNameEditText = (EditText)findViewById(R.id.familyName);
		familyNameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		neighbourNameEditText = (EditText)findViewById(R.id.neighbourName);
		neighbourNameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		neighbourNumberEditText = (EditText)findViewById(R.id.neighbourNumber);
		employerNameEditText = (EditText)findViewById(R.id.employerName);
		employerNameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		employerAddressEditText = (EditText)findViewById(R.id.employerAddress);
		advocateNameEditText = (EditText)findViewById(R.id.advocateName);
		advocateNameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		advocateNumberEditText = (EditText)findViewById(R.id.advocateNumber);
		numberCaseEditText = (EditText)findViewById(R.id.numOfCase);
		gangNameEditText = (EditText)findViewById(R.id.gangName);
		gangNameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		alibiNameEditText = (EditText)findViewById(R.id.alibiName);
		alibiNameEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		economicalConditionEditText = (EditText)findViewById(R.id.economicCondition);
		economicalConditionEditText.setFilters(new InputFilter[]{Utils.filtertxt});

		previousIncidentEditText = (EditText)findViewById(R.id.previousIncident);

		miscInfoEditText = (EditText)findViewById(R.id.miscInfo);
		punishedSwitch = (SwitchCompat)findViewById(R.id.punishedSwitch);
		checkedSwitch = (SwitchCompat)findViewById(R.id.checkedSwitch);
		hometitle=(TextView)findViewById(R.id.kkk);
		hometitle.setText(R.string.mob);
		lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
		imageView1 = (ImageView)findViewById(R.id.img1);
		imageView2 = (ImageView)findViewById(R.id.img2);
		imageView3 = (ImageView)findViewById(R.id.img3);
		imageView4 = (ImageView)findViewById(R.id.img4);

		View getImages = findViewById(R.id.takephoto);

		punishedSwitch.setOnCheckedChangeListener(this);
		checkedSwitch.setOnCheckedChangeListener(this);


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
		upload = (Button)findViewById(R.id.submitinfo);

		gpsTracker = new GPSTracker(MobActivity.this);
		latitude = gpsTracker.getLatitude();
		longitude = gpsTracker.getLongitude();

		Log.e("latitude.."+latitude,"longitude.."+longitude);


		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gpsTracker.getLocation();
				mobType.latitude = gpsTracker.getLatitude();
				mobType.longitude = gpsTracker.getLongitude();

				if (mobTypeTextView != null && !mobTypeTextView.getText().toString().trim().isEmpty()) {
					if(phoneEditText != null && (phoneEditText.getText().toString().trim().length() == 0 || (phoneEditText.getText().toString().trim().length()>=10))) {
						if (neighbourNumberEditText != null && (neighbourNumberEditText.getText().toString().trim().length() == 0 || (neighbourNumberEditText.getText().toString().trim().length() >= 10))) {
							if (advocateNumberEditText != null && (advocateNumberEditText.getText().toString().trim().length() == 0 || (advocateNumberEditText.getText().toString().trim().length() >= 10))) {
							if (mobType.checked == 1) {
								if (image_uris != null && image_uris.size() > 0) {
									if (mobType.latitude == 0 && mobType.longitude == 0) {
										Toast.makeText(MobActivity.this, getResources().getString(R.string.LocationNotEnabled), Toast.LENGTH_SHORT).show();
									} else {
										uploadFile(image_uris);
									}
								} else {
									Toast.makeText(MobActivity.this, getResources().getString(R.string.ImagesNotSelected), Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(MobActivity.this, getResources().getString(R.string.CheckNotSelected), Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(MobActivity.this, getResources().getString(R.string.InvalidPhoneNo), Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(MobActivity.this, getResources().getString(R.string.InvalidPhoneNo), Toast.LENGTH_SHORT).show();
					}
					}else{
						Toast.makeText(MobActivity.this, getResources().getString(R.string.InvalidPhoneNo), Toast.LENGTH_SHORT).show();
					}

				}else{
					Toast.makeText(MobActivity.this, getResources().getString(R.string.NoMobSelected), Toast.LENGTH_SHORT).show();
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

		getMobType();

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


	// Method for fetching Mob type details
	private void getMobType() {
		if (NetUtils.isOnline(MobActivity.this)) {
			pDialog = new ProgressDialog(MobActivity.this);
			pDialog.setMessage(getResources().getString(R.string.GetDetails));
			pDialog.setCancelable(false);
			pDialog.show();

			UserNetworkManager.getMobTypeDetails(new CallBacks.ListCallBackListener<MobType>() {

				@Override
				public void onSuccess(List<MobType> responseList) {

					pDialog.dismiss();
					if (responseList != null && responseList.size() > 0) {

						for (int i = 0; i < responseList.size(); i++) {
							mobSpinnerList.add((responseList.get(i)).MobName);
							Log.e("Mob details", responseList.get(i).toString());
							// Populate spinner with country names
							mobTypeList.add(responseList.get(i));
						}
						mobTypeArrayAdapter = new ArrayAdapter<String>(MobActivity.this,
								android.R.layout.simple_list_item_1, mobSpinnerList);

						mobTypeTextView.setAdapter(mobTypeArrayAdapter);

					}

				}

				@Override
				public void onError(String errorMessage) {
					pDialog.dismiss();
					Toast.makeText(MobActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onStart() {

				}
			}, MobType.class);
		} else {
			Toast.makeText(MobActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
		}

	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()){
			case R.id.punishedSwitch:
				if(!isChecked)
					mobType.punished = 0;
					else
				    mobType.punished = 1;
				break;
			case R.id.checkedSwitch:
				if(!isChecked)
					mobType.checked = 0;
					else
				    mobType.checked = 1;
				break;

		}
	}

	//Uploading images to server
	public void uploadFile(List<Uri> sourceFileUri) {
		List<File> fileList = new ArrayList<>();
		for (int i = 0; i < sourceFileUri.size(); i++) {
			fileList.add(new File(String.valueOf(sourceFileUri.get(i))));
		}
		if (NetUtils.isOnline(MobActivity.this)) {
			loading = new ProgressDialog(MobActivity.this);
			loading.setMessage(getResources().getString(R.string.uploadImage));
			loading.setCancelable(false);
			loading.show();
			UserNetworkManager.uploadImages(new CallBacks.StringCallBackListener() {

				@Override
				public void onSuccess(String successMessage) {
						if (successMessage.contains("200")) {
							postMobDetails(mobType);
						} else {
							loading.dismiss();
							Toast.makeText(MobActivity.this, getResources().getString(R.string.imageUploadFail), Toast.LENGTH_SHORT).show();
						}

				}

				@Override
				public void onError(String errorMessage) {
					loading.dismiss();
					Toast.makeText(MobActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onStart() {

				}
			}, fileList, "Mob");
		} else {
			Toast.makeText(MobActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
		}


	}




	//After success of images posting mob  details to server
	private void postMobDetails(MobType mobType) {

		loading.setMessage(getResources().getString(R.string.uploadingDetails));
		loading.setCancelable(false);
		Log.e("post method", "called"+mobType.toString());

		try {


			jsonObject = new JSONObject();
			jsonObject.put("MobType", mobTypeTextView.getText().toString().trim());
			jsonObject.put("MobName", nameEditText.getText().toString().trim());
			jsonObject.put("mobaddress", addressEditText.getText().toString().trim());
			jsonObject.put("Phone", phoneEditText.getText().toString().trim());
			jsonObject.put("FamilyName", familyNameEditText.getText().toString().trim());
			jsonObject.put("NeighbourName", neighbourNameEditText.getText().toString().trim());
			jsonObject.put("NeighbourNumber", neighbourNumberEditText.getText().toString().trim());
			jsonObject.put("EmployerName", employerNameEditText.getText().toString().trim());
			jsonObject.put("EmployerAdd", employerAddressEditText.getText().toString().trim());
			jsonObject.put("AdvocateName", advocateNameEditText.getText().toString().trim());
			jsonObject.put("AdvocateNumber", advocateNumberEditText.getText().toString().trim());
			jsonObject.put("NoOfCase", numberCaseEditText.getText().toString().trim());
			jsonObject.put("GangeName", gangNameEditText.getText().toString().trim());
			jsonObject.put("PreviousInc", previousIncidentEditText.getText().toString().trim());
			jsonObject.put("AlibiName", alibiNameEditText.getText().toString().trim());
			jsonObject.put("EconomicalBack", economicalConditionEditText.getText().toString().trim());
			jsonObject.put("MiscInfo", miscInfoEditText.getText().toString().trim());
			jsonObject.put("userid", App.getUserId());
			jsonObject.put("punished", mobType.punished);
			jsonObject.put("check", mobType.checked);
			jsonObject.put("Latitude", mobType.latitude);
			jsonObject.put("Longitude", mobType.longitude);

			UserNetworkManager.postMobDetails(new CallBacks.ObjectCallBackListener() {
				@Override
				public void onSuccess(Object responseObject) {
					loading.dismiss();

					if(responseObject != null && (responseObject.toString().trim().contains("Success")|| responseObject.toString().trim().contains("Sucess"))) {
						Utils.showSuccessDialog(MobActivity.this);
					}else {
						Toast.makeText(MobActivity.this, getResources().getString(R.string.errorMessage), Toast.LENGTH_SHORT).show();
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
			},MobActivity.this,jsonObject);


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
