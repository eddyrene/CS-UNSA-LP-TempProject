package com.example.alquiler.alquilercom;

import android.Manifest;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.renderscript.Double2;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import java.util.ArrayList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.splittransformation.SquareViewPagerIndicator;
import com.cleveroad.splittransformation.TransformationAdapterWrapper;
import com.example.alquiler.alquilercom.data.JsonHttpHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;
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

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,

        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng r;

    private String pos;
    private int radio;
    private Circle circle;
    private SlidingUpPanelLayout slidingLayout;
    private String param;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        slidingLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        slidingLayout.setAnchorPoint(0.25f);

        pos = getIntent().getExtras().getString("pos");

        r = new LatLng(getIntent().getExtras().getDouble("lat"), getIntent().getExtras().getDouble("lon"));
        radio = getIntent().getExtras().getInt("radio");
        param = getIntent().getExtras().getString("param");

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
        circle = mMap.addCircle(new CircleOptions().center(r).radius(radio * 990).strokeColor(Color.BLUE));
        circle.setVisible(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(r, zoomLevel(circle)));
        VisibleRegion vr = mMap.getProjection().getVisibleRegion();
        new QueryWindowTask(param, radio * 1000, circle.getCenter(), pos).execute(vr.latLngBounds.southwest.latitude, vr.latLngBounds.southwest.longitude, vr.latLngBounds.northeast.latitude, vr.latLngBounds.northeast.longitude);

        googleMap.setOnMarkerClickListener(this);

        /*
        * Posición actual
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(MapsActivity.this, "No permission granted", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //Log.v("TITLEEEEEEEEEE", marker.getTitle());
        //Intent i=new Intent(MapsActivity.this, MainActivityt.class);
        //startActivity(i);
        progress = ProgressDialog.show(this, "",
                "Espere un momento...", true);
        new InfoTask().execute(marker.getTitle());
        return true;
    }

    public float zoomLevel(Circle circle) {
        float zoomLevel = 15;
        if (circle != null) {
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel = (float) (16 - Math.log(scale) / Math.log(2));
            //Log.i(TAG, "Zoom level = " + zoomLevel );
        }
        return zoomLevel - 0.5f;
    }

    /*
    Clase para mostrar informacion por cada marcador
     */

    private class InfoTask extends AsyncTask<String, Void, JSONArray> {
        public InfoTask() {
        }


        @Override
        protected JSONArray doInBackground(String... params) {
            JSONObject json;
            try {
                json = new JsonHttpHandler().getJSONfromUrl("http://myflaskapp2-alquiler.rhcloud.com/reg/" + params[0]);
                if (json == null) {
                    return null;
                }
                JSONArray rooms = json.getJSONArray("rooms");
                if (rooms == null)
                    return null;
                if (rooms.length() == 0)
                    return null;
                return rooms;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray result) {

            if (result == null)
                Toast.makeText(MapsActivity.this, "No se puede mostrar la información.", Toast.LENGTH_SHORT).show();


            else {

                //Toast.makeText(MapsActivity.this, "ALL right", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject n = (JSONObject) result.get(0);
                    JSONArray imgs = n.getJSONArray("Img");
                    ArrayList<String> aux;
                    List<String> imagenes = new ArrayList<String>();
                    //=new ArrayList<String>(Arrays.asList(imgs.toString().replace("[","").replace("]","").split(",")));
                    for (int i = 0; i < imgs.length(); ++i) {
                        imagenes.add(imgs.get(i).toString());
                    }
                    JSONObject s = (JSONObject) n.get("Servicios");
                    String[] ser = {n.getString("Genero"), s.getString("tv"), s.getString("wifi"), s.getString("ducha"), s.getString("mascota"), s.getString("baño")};
                    List<String> servicios = Arrays.asList(ser);

                    String[] co1 = n.getString("Coord").replace("[","").replace("]","").split(",");
                    Location locationA = new Location("a");

                    locationA.setLatitude(r.latitude);
                    locationA.setLongitude(r.longitude);

                    Location locationB = new Location("b");

                    locationB.setLatitude(Double.parseDouble(co1[0]));
                    locationB.setLongitude(Double.parseDouble(co1[1]));

                    String distance = String.valueOf((Math.round((locationA.distanceTo(locationB))*100)/100)/1000f)+" km";

                    MyDialogFragment dialog = new MyDialogFragment();
                    dialog.setArgs(imagenes, n.get("Direccion").toString(), n.get("Nombre").toString(),
                            n.get("Telefono").toString(), n.get("Precio").toString(), servicios,distance);
                    progress.dismiss();
                    dialog.show(getSupportFragmentManager(), "asdf");

                    //startActivity(i);
                    //finish();
                    //this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "No se puede mostrar la información.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    /*
    Clase para procesar los resultados obtenidos con un radio y hacer una consulta rectangular
     */

    private class QueryWindowTask extends AsyncTask<Double, Void, List<MarkerOptions>> {
        String paramQ;
        LatLng centro;
        double radio_;
        String points;

        public QueryWindowTask(String param, double rad, LatLng cen, String puntos) {
            paramQ = param;
            centro = cen;
            radio_ = rad;
            points = puntos;
        }

        @Override
        protected List<MarkerOptions> doInBackground(Double... params) {
            JSONObject jsonr;

            try {
                List<LatLng> resultados;
                List<String> myList = new ArrayList<String>(Arrays.asList(points.split(",")));
                resultados = new ArrayList<LatLng>(myList.size() / 2);
                for (int i = 0; i < myList.size(); i = i + 2) {
                    resultados.add(i / 2, new LatLng(Double.parseDouble(myList.get(i)), Double.parseDouble(myList.get(i + 1))));
                }
                //- inf izq -- sup der- lat long
                if (paramQ.equals("n")) {
                    jsonr = new JsonHttpHandler().getJSONfromUrl("http://myflaskapp2-alquiler.rhcloud.com//buscar2/" + String.valueOf(params[0]) + "/" + String.valueOf(params[1]) + "/" + String.valueOf(params[2]) + "/" + String.valueOf(params[3]));
                    Log.d("URLLLLLL", "http://myflaskapp2-alquiler.rhcloud.com//buscar2/" + String.valueOf(params[0]) + "/" + String.valueOf(params[1]) + "/" + String.valueOf(params[2]) + "/" + String.valueOf(params[3]));
                } else {
                    jsonr = new JsonHttpHandler().getJSONfromUrl("http://myflaskapp2-alquiler.rhcloud.com//buscar2/" + String.valueOf(params[0]) + "/" + String.valueOf(params[1]) + "/" + String.valueOf(params[2]) + "/" + String.valueOf(params[3]) + paramQ);
                }
                if (jsonr == null) {
                    return null;
                }
                JSONArray rooms = jsonr.getJSONArray("rooms");
                if (rooms == null) {
                    return null;
                }
                if (rooms.length() == 0) {
                    return null;
                } else {

                    List<MarkerOptions> result = new ArrayList<MarkerOptions>();

                    for (int m = 0; m < rooms.length(); ++m) {
                        JSONObject n = (JSONObject) rooms.get(m);
                        String[] co1 = n.getString("Coord").replace("[","").replace("]","").split(",");

                        LatLng aux = new LatLng(Double.parseDouble(co1[0]), Double.parseDouble(co1[1]));
                        if (resultados.contains(aux)) {
                            result.add(new MarkerOptions()
                                    .position(aux)
                                    .title(n.getJSONObject("_id").get("$oid").toString()));
                        } else {
                            result.add(new MarkerOptions()
                                    .position(aux)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.no))
                                    .title(n.getJSONObject("_id").get("$oid").toString()));
                        }
                    /*
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
                    //Log.v("RADIO",String.valueOf(results[0]));*/
                    }
                    Log.v("SALIENDOOOOOOOO", "xxxxxx");
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return null;
            } catch (NullPointerException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MarkerOptions> markers) {
            if (markers != null) {
                for (int i = 0; i < markers.size(); ++i) {
                    mMap.addMarker(markers.get(i));
                }
            }
        }
    }

    public static class MyDialogFragment extends DialogFragment {

   List<String> im,serv;
        String lo;
        String no,tele,prec,dist;

        public void setArgs(List<String> i, String l,String n,String tel_,String pre_,List<String> serv_,String dist_){
            im=new ArrayList<String>(i);
            lo=l;
            no=n;
            tele=tel_;
            prec=pre_;
            serv=new ArrayList<>(serv_);
            dist=dist_;
        }
        @Override
        public void onSaveInstanceState(Bundle b){
            b.putStringArrayList("img",new ArrayList<String>(im));
            b.putString("log",lo);
            b.putString("nog",no);
            b.putString("teleg",tele);
            b.putString("precg",prec);
            b.putString("distg",dist);
            b.putStringArrayList("servg",new ArrayList<String>(serv));
            super.onSaveInstanceState(b);
        }
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            super.onCreateView(inflater,container,savedInstanceState);
            if( savedInstanceState != null ) {
                im=savedInstanceState.getStringArrayList("img");
                no=savedInstanceState.getString("nog");
                lo=savedInstanceState.getString("log");
                tele=savedInstanceState.getString("teleg");
                prec=savedInstanceState.getString("precg");
                serv=savedInstanceState.getStringArrayList("servg");
                dist=savedInstanceState.getString("distg");
            }
            View v = inflater.inflate(R.layout.activity_main_slider, container, false);
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            getChildFragmentManager().beginTransaction()
                    .replace(R.id.container, SimpleViewsFragment.instance(im,no,lo,tele,prec,serv,dist))
                    .addToBackStack(null)
                    .commit();
            return v;
        }
        @Override
        public void onDestroyView() {
            if (getDialog() != null && getRetainInstance())
                getDialog().setOnDismissListener(null);
            super.onDestroyView();
        }


        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }



}



