package ga.gasoft.smartpolice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ga.gasoft.smartpolice.utils.GPSTracker;
import ga.gasoft.smartpolice.utils.Utils;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;


public class AndroidGPSTrackingActivity extends AppCompatActivity
		implements
		OnMyLocationButtonClickListener,
		OnMapReadyCallback,
		ActivityCompat.OnRequestPermissionsResultCallback,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationListener{


	public double latti,longi;


	/**
	 * Request code for location permission request.
	 *
	 * @see #onRequestPermissionsResult(int, String[], int[])
	 */

	/**
	 * Flag indicating whether a requested permission has been denied after returning in
	 * {@link #onRequestPermissionsResult(int, String[], int[])}.
	 */

	// Refresh menu item
	private MenuItem refreshMenuItem;

	private boolean mPermissionDenied = false;

	private GoogleMap mMap;

	static final int REQUEST_LOCATION = 1;
	LocationManager locationManager;

	public  String text;
	protected GoogleApiClient mGoogleApiClient;
	protected LocationRequest locationRequest;
	int REQUEST_CHECK_SETTINGS = 100;
	private ImageView back;

	/**
	 * Request code for location permission request.
	 *
	 * @see #onRequestPermissionsResult(int, String[], int[])
	 */
	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
	private Location location1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_tracker);

		SupportMapFragment mapFragment =
				(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeButtonEnabled(true);
		back=(ImageView) findViewById(R.id.back);

		getLocation();

	}


	@Override
	public void onMapReady(GoogleMap map) {
		mMap = map;

		mMap.setOnMyLocationButtonClickListener(this);
		enableMyLocation();
	}

	/**
	 * Enables the My Location layer if the fine location permission has been granted.
	 */
	private void enableMyLocation() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			// Permission to access the location is missing.
			Utils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
					Manifest.permission.ACCESS_FINE_LOCATION, true);
		} else if (mMap != null) {
			// Access to the location has been granted to the app.
			mMap.setMyLocationEnabled(true);

//			setUpMap();

//			mMap.addCircle(new CircleOptions()
//					.center(new LatLng(latti, longi))
//					.radius(1500)
//					.strokeColor(Color.BLUE));
			createMarker();
			back.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
	}


	@Override
	public boolean onMyLocationButtonClick() {
		Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
		// Return false so that we don't consume the event and the default behavior still occurs
		// (the camera animates to the user's current position).
		return false;
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {

		switch (requestCode) {
			case REQUEST_LOCATION:
				getLocation();
				break;
		}

		if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
			return;
		}

		if (Utils.isPermissionGranted(permissions, grantResults,
				Manifest.permission.ACCESS_FINE_LOCATION)) {
			// Enable the my location layer if the permission has been granted.
			enableMyLocation();
		} else {
			// Display the missing permission error dialog when the fragments resume.
			mPermissionDenied = true;
		}
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		if (mPermissionDenied) {
			// Permission was not granted, display error dialog.
			showMissingPermissionError();
			mPermissionDenied = false;
		}
	}

	/**
	 * Displays a dialog with error message explaining that the location permission is missing.
	 */
	private void showMissingPermissionError() {
		Utils.PermissionDeniedDialog
				.newInstance(true).show(getSupportFragmentManager(), "dialog");
	}


	private void setUpMap() {
		// Retrieve the city data from the web service
		// In a worker thread since it's a network operation.
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					retrieveAndAddCities();
//				} catch (IOException e) {
//					Log.e(LOG_TAG, "Cannot retrive cities", e);
//					return;
//				}
//			}
//		}).start();
	}

	/*protected void retrieveAndAddCities() throws IOException {
		HttpURLConnection conn = null;
		final StringBuilder json = new StringBuilder();
		try {
			// Connect to the web service
			URL url = new URL(SERVICE_URL);
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Read the JSON data into the StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				json.append(buff, 0, read);
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to service", e);
			throw new IOException("Error connecting to service", e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		// Create markers for the city data.
		// Must run this on the UI thread since it's a UI operation.
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					createMarkersFromJson(json.toString());
				} catch (JSONException e) {
					Log.e(LOG_TAG, "Error processing JSON", e);
				}
			}
		});
	}*/

	void createMarker() {

		int height = 100;
		int width = 100;
		BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.locator);
		Bitmap b=bitmapdraw.getBitmap();
		Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
		// De-serialize the JSON string into an array of city objects
			// Create a marker for each city in the JSON data.
			mMap.addMarker(new MarkerOptions()
					.title("Name")
					.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
					.position(new LatLng(latti, longi
					))
			);
	}


	void getLocation() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
				(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

		} else {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).build();
			mGoogleApiClient.connect();

			locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationRequest.setInterval(30 * 1000);
			locationRequest.setFastestInterval(5 * 1000);

			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);




			if (location1 != null) {
				latti = location1.getLatitude();
				longi = location1.getLongitude();

				System.out.println("latti"+latti);
				System.out.println("longi"+longi);

				if(mMap != null)
               createMarker();

			} else {



			}
		}
	}



/*

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.submenu3a:
				//   Toast.makeText(MyLocation.this, "Normal", Toast.LENGTH_SHORT).show();
				mMap.setMapType(MAP_TYPE_NORMAL);
				return true;
			case R.id.submenu3b:
				mMap.setMapType(MAP_TYPE_HYBRID);
				return true;
			case R.id.submenu3c:
				mMap.setMapType(MAP_TYPE_SATELLITE);
				return true;
			case R.id.submenu3d:
				mMap.setMapType(MAP_TYPE_TERRAIN);
				return true;
			case R.id.submenu3e:
				mMap.setMapType(MAP_TYPE_NONE);
				return true;


			case R.id.add_tabs:




				// refresh
				refreshMenuItem = item;
				// load the data from server
				new SyncData().execute();

				return true;

			case android.R.id.home:
				Intent intent = new Intent(this, MainActivity.class);
				this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
*/

	/**
	 * Async task to load the data from server
	 * **/
	/*private class SyncData extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			// set the progress bar view
			refreshMenuItem.setActionView(R.layout.action_progressbar);

			refreshMenuItem.expandActionView();
		}

		@Override
		protected String doInBackground(String... params) {
			// not making real request in this demo
			// for now we use a timer to wait for sometime
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			refreshMenuItem.collapseActionView();
			// remove the progress bar view
			refreshMenuItem.setActionView(null);

			Intent intent = new Intent(AndroidGPSTrackingActivity.this, AndroidGPSTrackingActivity.class);
			startActivity(intent);
		}
	};
*/

	@Override
	public void onConnected(@Nullable Bundle bundle) {

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(locationRequest);
		builder.setAlwaysShow(true);
		PendingResult<LocationSettingsResult> result =
				LocationServices.SettingsApi.checkLocationSettings(
						mGoogleApiClient,
						builder.build()
				);

		result.setResultCallback(this);

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
		final Status status = locationSettingsResult.getStatus();
		switch (status.getStatusCode()) {
			case LocationSettingsStatusCodes.SUCCESS:
				// NO need to show the dialog;
               getLocation();
				break;

			case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
				//  Location settings are not satisfied. Show the user a dialog

				try {
					// Show the dialog by calling startResolutionForResult(), and check the result
					// in onActivityResult().

					status.startResolutionForResult(AndroidGPSTrackingActivity.this, REQUEST_CHECK_SETTINGS);

				} catch (IntentSender.SendIntentException e) {

					//failed to show
				}
				break;

			case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
				// Location settings are unavailable so not possible to show any dialog now
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CHECK_SETTINGS) {

			if (resultCode == RESULT_OK) {

				Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
                getLocation();

			} else {

				Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
			}

		}
	}

	@Override
	public void onBackPressed(){
		finish();
	}


	@Override
	public void onLocationChanged(Location location) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}


