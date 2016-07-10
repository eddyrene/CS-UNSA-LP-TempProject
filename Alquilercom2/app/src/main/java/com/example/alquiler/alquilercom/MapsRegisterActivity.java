package com.example.alquiler.alquilercom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

/**
 * Created by Alexandra_H on 09/07/2016.
 */
public class MapsRegisterActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private Button ok;
    private LatLng posAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_register);
        posAct=new LatLng(getIntent().getExtras().getDouble("lat"),getIntent().getExtras().getDouble("lon"));
        ok=(Button)findViewById(R.id.buttonregister2);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent();
                a.putExtra("lat",posAct.latitude);
                a.putExtra("lon",posAct.longitude);
                MapsRegisterActivity.this.setResult(RESULT_OK,a);
                finish();
            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(posAct));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point));
                posAct=point;
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posAct,14));
    }

}
