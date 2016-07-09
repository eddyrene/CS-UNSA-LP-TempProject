package com.example.alquiler.alquilercom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class CardViewActivity extends AppCompatActivity {


    TextView hab_precio;
    TextView hab_direccion;
    ImageView hab_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("INICIO","CARd view");
        setContentView(R.layout.activity_card_view);
        setContentView(R.layout.info);
        hab_precio = (TextView)findViewById(R.id.hab_precio);
        hab_direccion = (TextView)findViewById(R.id.hab_direccion);
        hab_imagen = (ImageView)findViewById(R.id.hab_imagen);

        hab_precio.setText("S./ 178");
        hab_direccion.setText("Bellavista 45");



    }
}
