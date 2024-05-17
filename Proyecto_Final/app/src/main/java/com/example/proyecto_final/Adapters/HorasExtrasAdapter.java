package com.example.proyecto_final.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyect.Models.Fichaje;
import com.example.finalproyect.R;

import java.util.List;

public class HorasExtrasAdapter extends RecyclerView.Adapter<HorasExtrasAdapter.HorasExtrasViewHolder> {

    private List<Fichaje> fichajes;

    public HorasExtrasAdapter(List<Fichaje> fichajes) {
        this.fichajes = fichajes;
    }

    @NonNull
    @Override
    public HorasExtrasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datos_horas_extras, parent, false);
        return new HorasExtrasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorasExtrasViewHolder holder, int position) {
        holder.textViewFecha.setText(fichajes.get(position).getFecha());
        holder.textViewHoras.setText(fichajes.get(position).getHoras());
    }

    @Override
    public int getItemCount() {
        return fichajes.size();
    }

    static class HorasExtrasViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFecha, textViewHoras;

        HorasExtrasViewHolder(View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.fechaHora);
            textViewHoras = itemView.findViewById(R.id.horaExtra);
        }
    }
}
