package com.andlausia.timerlist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class SecondActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    Button btnCall, btnMessage;
    GoogleApiClient googleApiClient;

    Location location;
    boolean btnMessagePressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btnCall = (Button) findViewById(R.id.btn_call);
        btnMessage = (Button) findViewById(R.id.btn_message);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:01114498330"));

                startActivity(intent);
            }
        });


        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMessagePressed = true;
                LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(isPermissionsGranted(SecondActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
                    location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(location != null) {
                        sendSMS("01114498330", location.getLatitude() + "," + location.getLongitude());
                        btnMessagePressed = false;
                    }else{
                        if(googleApiClient == null) {
                            googleApiClient = new GoogleApiClient.Builder(SecondActivity.this)
                                    .addConnectionCallbacks(SecondActivity.this)
                                    .addOnConnectionFailedListener(SecondActivity.this)
                                    .addApi(LocationServices.API)
                                    .enableAutoManage(SecondActivity.this, 0, SecondActivity.this)
                                    .build();
                        }
                        if(!googleApiClient.isConnected())
                            googleApiClient.connect();
                        else {
                            googleApiClient.disconnect();
                            googleApiClient.connect();
                        }
                    }

                }else {
                    requestGrantedPermissions(SecondActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == RESULT_OK){
                LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location != null) {
                    sendSMS("01114498330", location.getLatitude() + "," + location.getLongitude());
                    btnMessagePressed = false;
                }else {
                    if(googleApiClient == null) {
                        googleApiClient = new GoogleApiClient.Builder(this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .enableAutoManage(this, 0, this)
                                .build();
                    }
                    if(!googleApiClient.isConnected())
                        googleApiClient.connect();
                    else{
                        googleApiClient.disconnect();
                        googleApiClient.connect();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 101:{
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        sendSMS("01114498330", location.getLatitude() + "," + location.getLongitude());
                        btnMessagePressed = false;
                    }else {
                        if(googleApiClient == null) {
                            googleApiClient = new GoogleApiClient.Builder(this)
                                    .addConnectionCallbacks(this)
                                    .addOnConnectionFailedListener(this)
                                    .addApi(LocationServices.API)
                                    .enableAutoManage(this, 0, this)
                                    .build();
                        }
                        if(!googleApiClient.isConnected())
                            googleApiClient.connect();
                        else{
                            googleApiClient.disconnect();
                            googleApiClient.connect();
                        }
                    }
                }else {
                    permissionsDenied();
                }
                break;
            }
        }
    }

    private void permissionsDenied() {
        Log.w("TEST", "permissionsDenied()");
    }

    public void sendSMS(String phoneNo, String msg) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("address", phoneNo);
        sendIntent.putExtra("sms_body", "http://maps.google.com/?q=" + msg);
        startActivity(sendIntent);
    }


    public  boolean isPermissionsGranted(Context context, String[] grantPermissions) {
        /* this method is called to check if user grant specific permissions for android 6.0 and heigher  */
        boolean accessGranted = true;
        if (grantPermissions == null || grantPermissions.length == 0) {
            accessGranted = false;
        } else {
            for (String permission : grantPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    accessGranted = false;
                    break;
                }
            }
        }
        return accessGranted;
    }

    public  boolean requestGrantedPermissions(Context context, String[] permissions, int requestCode) {
        /* the method request permission on android 6.0 and heigher */
        boolean requestPermission = true;
        if (!isPermissionsGranted(context, permissions)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(permissions, requestCode);
            } else {
                requestPermission = false;
            }
        } else {
            requestPermission = false;
        }
        return requestPermission;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(isPermissionsGranted(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
            checkIfLocationEnabled();
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(location != null && btnMessagePressed){
                sendSMS("01114498330", location.getLatitude() + "," + location.getLongitude());
                btnMessagePressed = false;
            }else {
                LocationRequest locationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                        .setFastestInterval(3000)
                        .setInterval(3000);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null && btnMessagePressed) {
            sendSMS("01114498330", location.getLatitude() + "," + location.getLongitude());
            btnMessagePressed = false;
        }
    }

    private void checkIfLocationEnabled(){
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setFastestInterval(3000)
                .setInterval(3000);
        LocationSettingsRequest.Builder mBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        mBuilder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, mBuilder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        if (location != null && btnMessagePressed) {
                            sendSMS("01114498330", location.getLatitude() + "," + location.getLongitude());
                            btnMessagePressed = false;
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    SecondActivity.this, 1000);
                            if (location != null) {
                                sendSMS("01114498330", location.getLatitude() + "," + location.getLongitude());
                                btnMessagePressed = false;
                            }
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
}
