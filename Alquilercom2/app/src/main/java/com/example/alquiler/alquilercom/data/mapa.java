package com.example.alquiler.alquilercom.data;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Alexandra_H on 22/06/2016.
 */
public class mapa extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public LatLng pos;

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    public void setPosicion(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        }
        else{
            Toast.makeText(mapa.this, "No permission granted", Toast.LENGTH_SHORT).show();
        }
        GoogleApiClient mGoogleApiClient;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();

        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        pos = new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude());
    };


    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnected(Bundle bundle) {

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
