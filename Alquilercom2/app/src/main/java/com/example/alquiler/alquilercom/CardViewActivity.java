package com.example.alquiler.alquilercom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CardViewActivity extends AppCompatActivity {


    TextView hab_precio;
    TextView hab_direccion;
    ImageView hab_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_view);
        hab_precio = (TextView)findViewById(R.id.hab_precio);
        hab_direccion = (TextView)findViewById(R.id.hab_direccion);
        hab_imagen = (ImageView)findViewById(R.id.hab_imagen);

        hab_precio.setText("S./ 178");
        hab_direccion.setText("Bellavista 45");
        hab_imagen.setImageResource(R.drawable.habitacion1);
    }
}
