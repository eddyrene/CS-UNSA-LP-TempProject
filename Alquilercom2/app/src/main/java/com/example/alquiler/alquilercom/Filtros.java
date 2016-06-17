package com.example.alquiler.alquilercom;

import com.example.alquiler.alquilercom.data.JsonHttpHandler;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Filtros extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SeekBar seekBar;
    private TextView textViewSeekBar;
    ToggleButton btn_agua, btn_animales, btn_men, btn_toilet, btn_tv, btn_wifi, btn_woman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

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
        return;
    }


    //////////////////////////////////////actividad de botones///////////////////////////////
    // *********aqui hacer sus funciones!!!!!!
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(btn_agua.isChecked())
            Toast.makeText(Filtros.this,
                    "Activo-aguacaliente", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(Filtros.this,
                    "Inactivo-aguacaliente", Toast.LENGTH_SHORT).show();

    }
}
