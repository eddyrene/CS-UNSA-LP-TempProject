package com.example.alquiler.alquilercom;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.HabitacionViewHolder> {

    public static class HabitacionViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView precioHabitacion;
        TextView direccionHabitacion;
        ImageView imagenHabitacion;

        HabitacionViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            precioHabitacion = (TextView)itemView.findViewById(R.id.hab_precio);
            direccionHabitacion = (TextView)itemView.findViewById(R.id.hab_direccion);
            imagenHabitacion = (ImageView)itemView.findViewById(R.id.hab_imagen);
        }
    }

    List<Habitacion> habitaciones;

    RVAdapter(List<Habitacion> habitaciones){
        this.habitaciones = habitaciones;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public HabitacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        HabitacionViewHolder pvh = new HabitacionViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(HabitacionViewHolder habitacionViewHolder, int i) {
        habitacionViewHolder.precioHabitacion.setText(habitaciones.get(i).precio);
        habitacionViewHolder.direccionHabitacion.setText(habitaciones.get(i).direccion);
        habitacionViewHolder.imagenHabitacion.setImageResource(habitaciones.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return habitaciones.size();
    }
}
