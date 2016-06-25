package com.example.alquiler.alquilercom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.renderscript.Double2;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    String lat,lon;
    Double mylat,mylon;
    List<LatLng> resultados;
    int radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String pos=getIntent().getExtras().getString("pos");
        //String pos="-16.4636455,-71.501366";
        List<String> myList = new ArrayList<String>(Arrays.asList(pos.split(",")));
        //Toast.makeText(this, myList.toString()+"\n"+myList.size(), Toast.LENGTH_SHORT).show();
        resultados=new ArrayList<LatLng>(myList.size()/2);
        for (int i=0;i<myList.size();i=i+2){
            resultados.add(i/2,new LatLng(Double.parseDouble(myList.get(i)),Double.parseDouble(myList.get(i+1))));
        }
        mylat=getIntent().getExtras().getDouble("lat");
        mylon=getIntent().getExtras().getDouble("lon");
        radio=getIntent().getExtras().getInt("radio");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*
        * Posición actual
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        else{
            Toast.makeText(MapsActivity.this, "No permission granted", Toast.LENGTH_SHORT).show();
        }

        /*GoogleApiClient mGoogleApiClient;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (mGoogleApiClient != null)
            if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()){
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
            } else if (!mGoogleApiClient.isConnected()){
                mGoogleApiClient.connect();
            }
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng pos = new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude());*/
        LatLng r=new LatLng(mylat,mylon);

        //mMap.addMarker(new MarkerOptions().position(pos));//.title("Unsa"));

        for(int i=0;i<resultados.size();i++){
            mMap.addMarker(new MarkerOptions().position(resultados.get(i)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(r,radio));


    }
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
