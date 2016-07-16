package com.example.alquiler.alquilercom;

import com.example.alquiler.alquilercom.data.JsonHttpHandler;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;

import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Filtros extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SeekBar seekBarprecio, seekBardistancia;
    private TextView textViewSeekBar_precio, textViewSeekBar_distancia;
    ToggleButton btn_agua, btn_animales, btn_men, btn_toilet, btn_tv, btn_wifi, btn_woman;
    private CheckBox filtros;
    Location pos=null;
    LocationManager mlocManager=null;
    Double lon, lat;
    int radio_;
    private  View scroll;
    private View spinner;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        scroll=findViewById(R.id.scrollView);
        spinner=findViewById(R.id.search_progress);

        filtros=(CheckBox) findViewById(R.id.checkBox);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        }
        else{
            Toast.makeText(Filtros.this, "No permission granted", Toast.LENGTH_SHORT).show();
        }

        mlocManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new  Localizacion();
        Local.setMainActivity(this);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,Local);



        //Parte del scroll!!!!*****************************************************

        seekBarprecio = (SeekBar) findViewById(R.id.seekBar_precio);
        seekBardistancia = (SeekBar) findViewById(R.id.seekBar_distancia);
        textViewSeekBar_precio= (TextView) findViewById(R.id.textView_precio);
        textViewSeekBar_distancia= (TextView) findViewById(R.id.textView_distancia);
        textViewSeekBar_precio.setText("80");
        textViewSeekBar_distancia.setText("1");
        seekBarprecio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //la Seekbar siempre empieza en cero, si queremos que el valor mÃ­nimo sea otro podemos modificarlo
                textViewSeekBar_precio.setText(progress + 80 + "");
            }

            /**
             * El usuario inicia la interacciÃ³n con la Seekbar.
             */
            @Override
            public void onStartTrackingTouch(SeekBar arg0)
            {
            }
            /**
             * El usuario finaliza la interacciÃ³n con la Seekbar.
             */
            @Override
            public void onStopTrackingTouch(SeekBar arg0)
            {
            }
        });


        seekBardistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSeekBar_distancia.setText(progress+1+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //*******************************************FIN *****************************************
        //********************Propiedad Seleccionados de Botones-Servicio*************************

        filtros.setOnCheckedChangeListener(this);

        btn_agua= (ToggleButton) findViewById(R.id.imageButton_agua);
        btn_animales= (ToggleButton) findViewById(R.id.imageButton_animales);
        btn_men= (ToggleButton) findViewById(R.id.imageButton_men);
        btn_toilet= (ToggleButton) findViewById(R.id.imageButton_toilet);
        btn_tv= (ToggleButton) findViewById(R.id.imageButton_tv);
        btn_wifi= (ToggleButton) findViewById(R.id.imageButton_wifi);
        btn_woman= (ToggleButton) findViewById(R.id.imageButton_woman);

        btn_agua.setOnCheckedChangeListener(this);
        btn_animales.setOnCheckedChangeListener(this);
        btn_men.setOnCheckedChangeListener(this);
        btn_toilet.setOnCheckedChangeListener(this);
        btn_tv.setOnCheckedChangeListener(this);
        btn_wifi.setOnCheckedChangeListener(this);
        btn_woman.setOnCheckedChangeListener(this);

        btn_woman.setChecked(true);
        btn_men.setChecked(true);

        //******************************************FIN*******************************************


        //Parte del boton aceptar!!!!!
        Button mAplicar = (Button) findViewById(R.id.button);
        mAplicar.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                AplicarCambios();
            }
        });

        lon=mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
        lat=mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
        final String email=getIntent().getExtras().getString("email");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Filtros.this, RegisterActivity.class);
                i.putExtra("email",email);
                i.putExtra("lon",lon);
                i.putExtra("lat",lat);
                startActivity(i);
            }
        });
    }

    private void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            this.lat=loc.getLatitude();
            this.lon=loc.getLongitude();
        }
        /*    try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);

                    //Toast.makeText(FiltrosActivity.this, "Mi direccion es: \n"+ DirCalle.getAddressLine(0), Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void AplicarCambios() {

        String radio1 = textViewSeekBar_distancia.getText().toString();
        this.radio_=Integer.parseInt(radio1);
        float r = Float.parseFloat(radio1) * (0.009f);
        String radio = String.valueOf(r);

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lon=mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
            lat=mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
        }
        else{
            lat=-16.406437;
            lon=-71.5245201;
        }*/


        if (filtros.isChecked()) {
            showProgress(true);
            new BuscarTask().execute("sinfiltros",String.valueOf(lat), String.valueOf(lon), radio);
        } else {
            String gen = "";
            String p_min = "0";
            String p_max = textViewSeekBar_precio.getText().toString();
            String[] servicios = {"0", "0", "0", "0", "0"};
            boolean men = btn_men.isChecked();
            boolean women = btn_woman.isChecked();
            if (men && women) {
                gen = "3";
            } else if (men && !women) {
                gen = "1";
            } else {
                gen = "2";
            }
            if (btn_wifi.isChecked())
                servicios[0] = "1";
            if (btn_animales.isChecked())
                servicios[1] = "1";
            if (btn_tv.isChecked())
                servicios[2] = "1";
            if (btn_agua.isChecked())
                servicios[3] = "1";
            if (btn_toilet.isChecked())
                servicios[4] = "1";

            showProgress(true);

            new BuscarTask().execute(String.valueOf(lat), String.valueOf(lon), radio, gen, servicios[4], servicios[2], servicios[3], servicios[0], servicios[1], p_min, p_max);
        }
    }

    //////////////////////////////////////actividad de botones///////////////////////////////
    // *********aqui hacer sus funciones!!!!!!
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!buttonView.isChecked() && (buttonView.getId()==btn_men.getId() || buttonView.getId()==btn_woman.getId() || (buttonView.getId()==filtros.getId())) ) {
            //Toast.makeText(FiltrosActivity.this,"Inactivo", Toast.LENGTH_SHORT).show();
            if (buttonView.getId() == btn_men.getId() && !filtros.isChecked()) {
                btn_woman.setChecked(true);
            } else if (buttonView.getId() == btn_woman.getId() && !filtros.isChecked()) {
                btn_men.setChecked(true);
            } else if (buttonView.getId() == filtros.getId()) {
                btn_men.setEnabled(true);
                btn_men.setChecked(true);
                btn_men.setAlpha(1f);
                btn_woman.setEnabled(true);
                btn_woman.setAlpha(1f);
                btn_animales.setEnabled(true);
                btn_animales.setAlpha(1f);
                btn_agua.setEnabled(true);
                btn_agua.setAlpha(1f);
                btn_toilet.setEnabled(true);
                btn_toilet.setAlpha(1f);
                btn_tv.setEnabled(true);
                btn_tv.setAlpha(1f);
                btn_wifi.setEnabled(true);
                btn_wifi.setAlpha(1f);
                seekBarprecio.setEnabled(true);
                textViewSeekBar_precio.setEnabled(true);
            }
        }
        else if (buttonView.isChecked() && buttonView.getId()==filtros.getId()){
                btn_men.setEnabled(false);
                btn_men.setChecked(false);
                btn_men.setAlpha(.5f);
                btn_woman.setEnabled(false);
                btn_woman.setChecked(false);
                btn_woman.setAlpha(.5f);
                btn_animales.setEnabled(false);
                btn_animales.setChecked(false);
                btn_animales.setAlpha(.5f);
                btn_agua.setEnabled(false);
                btn_agua.setChecked(false);
                btn_agua.setAlpha(.5f);
                btn_toilet.setEnabled(false);
                btn_toilet.setChecked(false);
                btn_toilet.setAlpha(.5f);
                btn_tv.setEnabled(false);
                btn_tv.setChecked(false);
                btn_tv.setAlpha(.5f);
                btn_wifi.setEnabled(false);
                btn_wifi.setChecked(false);
                btn_wifi.setAlpha(.5f);
                seekBarprecio.setEnabled(false);
                textViewSeekBar_precio.setEnabled(false);
            }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            scroll.setVisibility(show ? View.GONE : View.VISIBLE);
            fab.setVisibility(show ? View.GONE : View.VISIBLE);
            fab.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scroll.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            scroll.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scroll.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            spinner.setVisibility(show ? View.VISIBLE : View.GONE);
            spinner.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    spinner.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            spinner.setVisibility(show ? View.VISIBLE : View.GONE);
            scroll.setVisibility(show ? View.GONE : View.VISIBLE);
            fab.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    /*
    Clase para buscar un cuarto
    */
    private class BuscarTask extends AsyncTask<String, Void, String[]> {

        public BuscarTask() {

        }
        //String im;
        @Override
        protected String[] doInBackground(String... params) {

            JSONObject jsonr = null;
            try {

                if(params[0].equals("sinfiltros")){
                    jsonr = new JsonHttpHandler().getJSONfromUrl("http://10.0.2.2:8100/buscar/" + params[1] + "/" + params[2] + "/" + params[3]);
                }
                else {
                    //lon,lat,rad,genero,precio_min,precio_max,servicios

                    jsonr = new JsonHttpHandler().getJSONfromUrl("http://10.0.2.2:8100/buscar/" + params[0] + "/" + params[1] + "/" + params[2] + "/" + params[3] + "/" + params[4] + "/" + params[5] + "/" + params[6] + "/" + params[7] + "/" + params[8] + "/" + params[9] + "/" + params[10]);
                    Log.d("WARNING", "http://myflaskapp2-alquiler.rhcloud.com/buscar/" + params[0] + "/" + params[1] + "/" + params[2] + "/" + params[3] + "/" + params[4] + "/" + params[5] + "/" + params[6] + "/" + params[7] + "/" + params[8] + "/" + params[9] + "/" + params[10]);
                }
                if (jsonr == null) {
                    return null;
                }

                JSONArray rooms = jsonr.getJSONArray("rooms");
                String[] aux={"","","n"};
                if (rooms==null){
                    aux=null;
                }
                else if (rooms.length()==0) {
                    aux[0]= "0";
                }
                else{
                    aux[0]=String.valueOf(rooms.length());
                    for(int m=0;m<rooms.length();++m){
                        JSONObject n=(JSONObject)rooms.get(m);
                        /*if(m==14)
                            im=n.getString("Img");*/
                        String co=n.getJSONObject("Coord").getString("coordinates");
                        String replace = co.replace("[","").replace("]","");
                        if (m==0)
                            aux[1]+=replace;
                        else
                            aux[1]+=","+replace;
                    }
                    if(!params[0].equals("sinfiltros"))
                        aux[2]="/"+params[3] + "/" + params[4] + "/" + params[5] + "/" + params[6] + "/" + params[7] + "/" + params[8] + "/" + params[9] + "/" + params[10];

                }
                return aux;

            }catch (JSONException e) {
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
        protected void onPostExecute(String[] aux) {
            //super.onPostExecute(result);
            //set a null el objeto de esta clase
            //crear=null;
            showProgress(false);
            if (aux==null){
                Toast.makeText(Filtros.this, "Se ha producido un error. Vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            } else if (aux[0].equals("0")) {
                Toast.makeText(Filtros.this, "No se han encontrado resultados", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(Filtros.this, aux[0] + " resultado(s)", Toast.LENGTH_SHORT).show();

                Toast.makeText(Filtros.this, lat + " " + lon, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Filtros.this, MapsActivity.class);
                i.putExtra("param",aux[2]);
                i.putExtra("pos", aux[1]);
                i.putExtra("lon", lon);
                i.putExtra("lat", lat);
                i.putExtra("radio", Filtros.this.radio_);

                //i.putExtra("i",im);

                startActivity(i);
                //finish();

            }
        }
        @Override
        protected void onCancelled() {

            showProgress(false);
        }

    }
    /*
    Fin de la clase para buscar un cuarto
     */

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        Filtros mainActivity;
        public Filtros getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(Filtros mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            //loc.getLatitude();
            //loc.getLongitude();
            //String Text = "Mi ubicacion actual es: " + "\n Lat = "+ loc.getLatitude() + "\n Long = " + loc.getLongitude();
            //Toast.makeText(FiltrosActivity.this, Text, Toast.LENGTH_SHORT).show();
            this.mainActivity.setLocation(loc);

        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //mensaje1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //mensaje1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Este metodo se ejecuta cada vez que se detecta un cambio en el
            // status del proveedor de localizacion (GPS)
            // Los diferentes Status son:
            // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
            // TEMPORARILY_UNAVAILABLE -> Temporalmente no disponible pero se
            // espera que este disponible en breve
            // AVAILABLE -> Disponible
        }

    }/* Fin de la clase localizacion */

}


