package com.example.alquiler.alquilercom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.renderscript.Double2;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.alquiler.alquilercom.data.JsonHttpHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.android.gms.maps.model.VisibleRegion;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,

        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng r;

    private List<LatLng> resultados;
    private int radio;
    private Circle circle;
    private SlidingUpPanelLayout slidingLayout;
    private String param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        slidingLayout=(SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        slidingLayout.setAnchorPoint(0.25f);

        String pos=getIntent().getExtras().getString("pos");

        r=new LatLng(getIntent().getExtras().getDouble("lat"),getIntent().getExtras().getDouble("lon"));
        radio=getIntent().getExtras().getInt("radio");
        param=getIntent().getExtras().getString("param");

        //new ProcessTask().execute(pos);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        circle = mMap.addCircle(new CircleOptions().center(r).radius(radio*1000).strokeColor(Color.BLUE));
        circle.setVisible(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(r,zoomLevel(circle)));
        VisibleRegion vr = mMap.getProjection().getVisibleRegion();
        new QueryWindowTask(param,radio*1000,circle.getCenter()).execute(vr.latLngBounds.southwest.latitude,vr.latLngBounds.southwest.longitude,vr.latLngBounds.northeast.latitude,vr.latLngBounds.northeast.longitude);

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

    /*
    Clase para procesar los resultados obtenidos con un radio
     */

    private class ProcessTask extends AsyncTask<String, Void, Void> {

        public ProcessTask() {}
        @Override
        protected Void doInBackground(String... params) {
            List<String> myList = new ArrayList<String>(Arrays.asList(params[0].split(",")));
            resultados=new ArrayList<LatLng>(myList.size()/2);
            for (int i=0;i<myList.size();i=i+2){
                resultados.add(i/2,new LatLng(Double.parseDouble(myList.get(i)),Double.parseDouble(myList.get(i+1))));
            }
            return null;
        }

    }
    /*
    Clase para procesar los resultados obtenidos con un radio
     */

    private class QueryWindowTask extends AsyncTask<Double, Void,List<MarkerOptions> > {
        String paramQ;
        LatLng centro;
        double radio_;
        public QueryWindowTask(String param, double rad,LatLng cen) {
            paramQ=param;
            centro=cen;
            radio_=rad;
        }

        @Override
        protected List<MarkerOptions> doInBackground(Double... params) {
            JSONObject jsonr;

            try {
                //- inf izq -- sup der- lat long
                if (paramQ.equals("n")) {
                    jsonr = new JsonHttpHandler().getJSONfromUrl("http://myflaskapp2-alquiler.rhcloud.com/buscar2/" + String.valueOf(params[0]) + "/" + String.valueOf(params[1]) + "/" + String.valueOf(params[2]) + "/" + String.valueOf(params[3]));
                    Log.d("URLLLLLL","http://myflaskapp2-alquiler.rhcloud.com/buscar2/" + String.valueOf(params[0]) + "/" + String.valueOf(params[1]) + "/" + String.valueOf(params[2]) + "/" + String.valueOf(params[3]));
                } else {
                    jsonr = new JsonHttpHandler().getJSONfromUrl("http://myflaskapp2-alquiler.rhcloud.com/buscar2/" + String.valueOf(params[0]) + "/" + String.valueOf(params[1]) + "/" + String.valueOf(params[2]) + "/" + String.valueOf(params[3]) + paramQ);
                }
                if (jsonr == null) {
                    return null;
                }
                JSONArray rooms = jsonr.getJSONArray("rooms");
                if (rooms==null){
                    return null;
                }
                else if (rooms.length()==0) {
                    return null;
                }
                else {
                    float[] results={0};
                    List<MarkerOptions> result=new ArrayList<MarkerOptions>();
                    Log.v("ENTRANDO","xxxxxx");
                    for (int m = 0; m < rooms.length(); ++m) {
                        JSONObject n = (JSONObject) rooms.get(m);
                        String co1 = n.getJSONObject("Coord").getJSONArray("coordinates").get(0).toString();
                        String co2 = n.getJSONObject("Coord").getJSONArray("coordinates").get(1).toString();

                        //google.maps.geometry.spherical.computeDistanceBetween(centro, latLng) <= radio;
                        Location.distanceBetween(
                                centro.latitude,
                                centro.longitude,
                                Double.parseDouble(co1),
                                Double.parseDouble(co2),
                                results);
                        if(results[0]>radio_){

                            result.add(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(co1),Double.parseDouble(co2)))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.no)));
                        }
                        else{
                            result.add(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(co1),Double.parseDouble(co2))));
                        }
                        //Log.v("RADIO",String.valueOf(results[0]));
                    }
                    Log.v("SALIENDOOOOOOOO","xxxxxx");
                    return result;
                }
            }
             catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return null;
            } catch (NullPointerException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e){
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(List<MarkerOptions> markers) {
            if (markers!=null){
                for (int i=0;i<markers.size();++i){
                    mMap.addMarker(markers.get(i));
                }
            }
        }
    }
}
