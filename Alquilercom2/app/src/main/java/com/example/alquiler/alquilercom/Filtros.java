package com.example.alquiler.alquilercom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Filtros extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
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
