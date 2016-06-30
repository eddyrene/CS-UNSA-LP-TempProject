package com.example.alquiler.alquilercom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.renderscript.Double2;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    LatLng r;

    List<LatLng> resultados;
    int radio;
    Circle circle;
    private SlidingUpPanelLayout slidingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        slidingLayout=(SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        slidingLayout.setAnchorPoint(0.25f);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String pos=getIntent().getExtras().getString("pos");

        List<String> myList = new ArrayList<String>(Arrays.asList(pos.split(",")));
        resultados=new ArrayList<LatLng>(myList.size()/2);
        for (int i=0;i<myList.size();i=i+2){
            resultados.add(i/2,new LatLng(Double.parseDouble(myList.get(i)),Double.parseDouble(myList.get(i+1))));
        }
        r=new LatLng(getIntent().getExtras().getDouble("lat"),getIntent().getExtras().getDouble("lon"));
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

        circle = mMap.addCircle(new CircleOptions().center(r).radius(radio*100).strokeColor(Color.RED));
        circle.setVisible(true);
        googleMap.setOnMarkerClickListener(this);

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


        //mMap.addMarker(new MarkerOptions().position(pos));//.title("Unsa"));

        for(int i=0;i<resultados.size();i++){
            mMap.addMarker(new MarkerOptions().position(resultados.get(i)));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(r,zoomLevel(circle)));


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
    @Override
    public boolean onMarkerClick(Marker marker) {
        this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        return  true;
    }
    public float zoomLevel(Circle circle){
        float zoomLevel = 15;
        if (circle != null){
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel =(float) (16 - Math.log(scale) / Math.log(2));
            //Log.i(TAG, "Zoom level = " + zoomLevel );
        }
        return zoomLevel - 0.5f ; }
}
