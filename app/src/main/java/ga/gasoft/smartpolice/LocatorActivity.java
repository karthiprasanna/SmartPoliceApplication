package ga.gasoft.smartpolice;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.utils.Utils;

public class LocatorActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener{


    private Location location;
    private double longitude, latitude;
    private LatLng currentLoc;
    private Geocoder myGeocoder;
    private List<Address> mAddress;
    String myCityName, mySubLocality;
    private LocationManager lm;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private String address;
    private GoogleMap mMap;
    private ProgressDialog progressDialog;
    private TextView hometitle;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_tracker);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hometitle = (TextView) findViewById(R.id.kkk);
        hometitle.setText(R.string.locator);
        back = (ImageView) findViewById(R.id.back);

        displayLocationSettingsRequest(LocatorActivity.this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayLocationSettingsRequest(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(LocatorActivity.this, 100);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    private void startLocationUpdates() {
        progressDialog =  new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.fetchingLocation));
        progressDialog.show();
         lm = (LocationManager) getSystemService(Context
                .LOCATION_SERVICE);

//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, SplashScreen.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling

//            Toast.makeText(getApplicationContext(), "if not location", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100);
            }
            return;
        }else{
//            Toast.makeText(getApplicationContext(), "if yes location", Toast.LENGTH_SHORT).show();

            if (googleApiClient != null) {
                googleApiClient.connect();
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);
        }


    }


    @Override
    public void onLocationChanged(Location location) {
//        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        longitude = location.getLongitude();
        latitude = location.getLatitude();
//        Toast.makeText(getApplicationContext(), "on location change "+latitude+"..."+longitude, Toast.LENGTH_SHORT).show();

        currentLoc = new LatLng(latitude, longitude);


        myGeocoder = new Geocoder(LocatorActivity.this, Locale.getDefault());
        try {
            mAddress = myGeocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mAddress != null ) {
            String street = mAddress.get(0).getAddressLine(0).toString();
            String street1 = mAddress.get(0).getAddressLine(1).toString();
            String city = mAddress.get(0).getLocality();
            String state = mAddress.get(0).getAdminArea();
            String zip = mAddress.get(0).getPostalCode();
            String country = mAddress.get(0).getCountryName();

            address = street + ", " + street1 + ", " + city + ", " + state + ", " + zip + ", " + country;
        }

        if(mMap != null)
        createMarker(latitude, longitude, address);

        stopLocationUpdates();
       /* final Intent locationIntent = new Intent(SplashScreen.this, LocationActivity.class);
        locationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        locationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        new AlertDialog.Builder(SplashScreen.this)
                .setTitle("Current delivery location")
                .setMessage(address)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        startActivity(locationIntent);
                    }
                })
                .setNegativeButton("CHANGE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        startActivity(locationIntent);

                    }
                })

                .show();
*/

//        myCityName = mAddress.get(0).getLocality();
//        mySubLocality = mAddress.get(0).getSubLocality();

//        Toast.makeText(getApplicationContext(), "location "+myCityName+"..."+mySubLocality, Toast.LENGTH_SHORT).show();

    }

    protected void stopLocationUpdates() {
        Log.e("onstop", "location update");
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }



    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case 100:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        displayLocationSettingsRequest(LocatorActivity.this);//keep asking if imp or do whatever
                        break;
                }
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient != null) {
                            googleApiClient.connect();
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                googleApiClient, locationRequest, this);

                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(LocatorActivity.this);
    }

    void createMarker(double latti, double longi, String address) {

        int height = 70;
        int width = 70;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.locator);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        // De-serialize the JSON string into an array of city objects
        // Create a marker for each city in the JSON data.
        mMap.addMarker(new MarkerOptions()
                .title(address)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .position(new LatLng(latti, longi
                ))
        );
        progressDialog.dismiss();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        postLatLongDetails(latitude, longitude);
        return false;
    }

    private void postLatLongDetails(double latitude, double longitude){

        progressDialog.setMessage(getResources().getString(R.string.uploadingDetails));
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(NetUtils.isOnline(LocatorActivity.this)){

            UserNetworkManager.postCurrentLatLong(new CallBacks.StringCallBackListener() {
                @Override
                public void onSuccess(String successMessage) {
                    progressDialog.dismiss();

                    if(successMessage != null && (successMessage.toString().trim().contains("Success")|| successMessage.toString().trim().contains("Sucess"))) {
                        Utils.showSuccessDialog(LocatorActivity.this);
                    }else {
                        Toast.makeText(LocatorActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    progressDialog.dismiss();
                    Toast.makeText(LocatorActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, latitude, longitude);


        }else{
            Toast.makeText(LocatorActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }

    }
}
