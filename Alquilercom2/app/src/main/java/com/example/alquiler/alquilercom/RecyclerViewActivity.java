package com.example.alquiler.alquilercom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private List<Habitacion> habs;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recyclerview);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        habs = new ArrayList<>();
        habs.add(new Habitacion("S./ 245", "La Colina A-4 ", R.drawable.habitacion1));
        habs.add(new Habitacion("S./ 200", "Cerro Salaverry A-4 ", R.drawable.habitacion2));
        habs.add(new Habitacion("S./ 233", "Villa el Golf A-4 ", R.drawable.habitacion3));
        habs.add(new Habitacion("S./ 233", "Urb las Orquideas A-4 ", R.drawable.habitacion4));
        habs.add(new Habitacion("S./ 233", "Centenario A-4 ", R.drawable.habitacion5));
        habs.add(new Habitacion("S./ 233", "Parque Las Condes 41 ", R.drawable.habitacion6));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(habs);
        rv.setAdapter(adapter);
    }
}
