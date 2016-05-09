package com.example.alexandra_h.prueba2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class actPrueba2 extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private CameraUpdate mCamera;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Button askpermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_prueba2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        askpermission=(Button)findViewById(R.id.askpermission);
        askpermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission();
            }
        });
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(actPrueba2.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void askForPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showMessageOKCancel("You need to allow access to your location",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[] {Manifest.permission.CAMERA},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(actPrueba2.this, " Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                    // Permission Granted put your code here
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(actPrueba2.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
        Marker source,destination;

        /*
        * Posición actual
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        else{
            Toast.makeText(actPrueba2.this, "No permission granted", Toast.LENGTH_SHORT).show();
        }


        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //Ubicacion unsa
        source=mMap.addMarker(new MarkerOptions().position(new LatLng(-16.4136551, -71.5341894)).title("UNSA"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-16.4136551, -71.5341894),15));

        destination= mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-16.4176551, -71.5341894))
                .title("MapleBear Head Office")
                .snippet("Jayanager"));
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.powered_by_google_light)));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                if(arg0.getTitle().equals("UNSA")) // if marker source is clicked
                    Toast.makeText(actPrueba2.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                return true;
            }

        });

    }
}
