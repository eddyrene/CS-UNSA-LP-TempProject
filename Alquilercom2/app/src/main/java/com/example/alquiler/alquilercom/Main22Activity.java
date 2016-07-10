package com.example.alquiler.alquilercom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

public class Main22Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);
        ImageView hab_imagen=(ImageView)findViewById(R.id.hab_imagen2);
        String im=getIntent().getExtras().get("i").toString();
        byte[] decodedBytes = Base64.decode(im,Base64.DEFAULT);
        Bitmap b= BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        hab_imagen.setImageBitmap(b);
    }

}
