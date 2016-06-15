package com.example.alquiler.alquilercom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Filtros extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textViewSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        //Parte del scroll!!!!
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
}
