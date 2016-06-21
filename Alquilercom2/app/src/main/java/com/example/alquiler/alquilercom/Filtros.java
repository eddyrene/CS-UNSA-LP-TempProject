package com.example.alquiler.alquilercom;

import com.example.alquiler.alquilercom.data.JsonHttpHandler;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Filtros extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SeekBar seekBar;
    private TextView textViewSeekBar;
    ToggleButton btn_agua, btn_animales, btn_men, btn_toilet, btn_tv, btn_wifi, btn_woman;
    private Spinner distrito;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        distrito=(Spinner) findViewById(R.id.spinner);
        //Parte del scroll!!!!*****************************************************
        seekBar = (SeekBar) findViewById(R.id.seekBar_precio);
        textViewSeekBar= (TextView) findViewById(R.id.textView_precio);
        textViewSeekBar.setText("80");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //la Seekbar siempre empieza en cero, si queremos que el valor mínimo sea otro podemos modificarlo
                textViewSeekBar.setText(progress + 80 + "");
            }

            /**
             * El usuario inicia la interacción con la Seekbar.
             */
            @Override
            public void onStartTrackingTouch(SeekBar arg0)
            {
            }
            /**
             * El usuario finaliza la interacción con la Seekbar.
             */
            @Override
            public void onStopTrackingTouch(SeekBar arg0)
            {
            }
        });

        //*******************************************FIN *****************************************
        //********************Propiedad Seleccionados de Botones-Servicio*************************



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
    }

    private void AplicarCambios(){
        String dist=distrito.getSelectedItem().toString();
        String gen="";
        String p_min="0";
        String p_max=textViewSeekBar.getText().toString();
        String[] servicios={"0","0","0","0","0"};
        boolean men=btn_men.isChecked();
        boolean women=btn_woman.isChecked();
        if (men && women){
            gen="3";
        } else if (men && !women){
            gen="1";
        } else {
            gen="2";
        }
        if (btn_wifi.isChecked())
            servicios[0]="1";
        if (btn_animales.isChecked())
            servicios[1]="1";
        if (btn_tv.isChecked())
            servicios[2]="1";
        if (btn_agua.isChecked())
            servicios[3]="1";
        if (btn_toilet.isChecked())
            servicios[4]="1";
        //Toast.makeText(Filtros.this,dist+","+gen+","+p_min+","+p_max+","+servicios[0]+servicios[1]+servicios[2]+servicios[3]+servicios[4], Toast.LENGTH_SHORT).show();
        new BuscarTask().execute(dist,gen,p_min,p_max,servicios[0],servicios[1],servicios[2],servicios[3],servicios[4]);
    }


    //////////////////////////////////////actividad de botones///////////////////////////////
    // *********aqui hacer sus funciones!!!!!!
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!buttonView.isChecked() && (buttonView.getId()==btn_men.getId() || buttonView.getId()==btn_woman.getId()) )
            //Toast.makeText(Filtros.this,"Inactivo", Toast.LENGTH_SHORT).show();
            if (buttonView.getId()==btn_men.getId()){
                btn_woman.setChecked(true);
            }
            else if (buttonView.getId()==btn_woman.getId()){
                btn_men.setChecked(true);
            }

    }

    /*
    Clase para buscar un cuarto
    */
    private class BuscarTask extends AsyncTask<String, Void, JSONArray> {


        public BuscarTask() {

        }
        @Override
        protected JSONArray doInBackground(String... params) {

            JSONObject jsonr = null;
            try {

                //distrito,genero,precio_min,precio_max,servicios

                jsonr=new JsonHttpHandler().getJSONfromUrl("http://myflaskapp-alquiler.rhcloud.com/buscar/"+params[0]+"/"+params[1]+"/"+params[2]+"/"+params[3]+"/"+params[4]+"/"+params[5]+"/"+params[6]+"/"+params[7]+"/"+params[8]);

                if (jsonr == null){return null;}

                JSONArray rooms = jsonr.getJSONArray("rooms");

                return rooms;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray rooms) {
            //super.onPostExecute(result);
            //set a null el objeto de esta clase
            //crear=null;
            if (rooms==null){
                Toast.makeText(Filtros.this, "Se ha producido un error. Vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            } else if (rooms.length()==0) {
                Toast.makeText(Filtros.this, "No se han encontrado resultados", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(Filtros.this, "exito", Toast.LENGTH_SHORT).show();
                finish();
                //iniciar activity de resultados y pasar los resultados
            }
        }

    }
    /*
    Fin de la clase para buscar un cuarto
     */

    //************************/ Funcion de radio buton**************************************


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    //rules
                    break;
            case R.id.radio_no:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

}
