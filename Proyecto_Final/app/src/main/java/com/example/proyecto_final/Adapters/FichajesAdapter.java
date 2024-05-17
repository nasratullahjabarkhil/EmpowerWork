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

public class FichajesAdapter extends RecyclerView.Adapter<FichajesAdapter.FichajeViewHolder> {

    private List<Fichaje> fichajes;

    public FichajesAdapter(List<Fichaje> fichajes) {
        this.fichajes = fichajes;
    }

    @NonNull
    @Override
    public FichajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datos_consulatr_fichajes, parent, false);
        return new FichajeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FichajeViewHolder holder, int position) {

        holder.textViewFecha.setText(fichajes.get(position).getFecha());
        holder.textViewHoraEntrada.setText(fichajes.get(position).getHoraEntrada());
        holder.textViewHoraSalida.setText(fichajes.get(position).getHoraSalida());

    }

    @Override
    public int getItemCount() {
        return fichajes.size();
    }

    static class FichajeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFecha, textViewHoraEntrada, textViewHoraSalida;

        FichajeViewHolder(View itemView) {
            super(itemView);
            textViewFecha = itemView.findViewById(R.id.fecha);
            textViewHoraEntrada = itemView.findViewById(R.id.entrada);
            textViewHoraSalida = itemView.findViewById(R.id.salida);

        }
    }
}
