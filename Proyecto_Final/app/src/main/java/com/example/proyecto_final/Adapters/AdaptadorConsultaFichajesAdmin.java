package com.example.proyecto_final.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyect.Activities.ActivityactualizarEliminarFichajes;
import com.example.finalproyect.Models.Fichaje;
import com.example.finalproyect.R;

import java.util.ArrayList;

public class AdaptadorConsultaFichajesAdmin extends RecyclerView.Adapter<AdaptadorConsultaFichajesAdmin.ViewHolder> {

    private ArrayList<Fichaje> fichajesList;
    Activity activity;
    Context context;

    public AdaptadorConsultaFichajesAdmin(Activity activity, Context context, ArrayList<Fichaje> fichajesList) {
        this.activity = activity;
        this.context = context;
        this.fichajesList = fichajesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datos_consultar_fichajes_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Fichaje fichaje = fichajesList.get(position);

            // Setear los datos del fichaje en los TextViews
            holder.textViewCodigo.setText(fichaje.getEmpleadoCodigo());
            holder.textViewFecha.setText(fichaje.getFecha());
            holder.textViewHoraEntrada.setText(fichaje.getHoraEntrada());
            String horaSalida = fichaje.getHoraSalida();

            if(horaSalida == null){
                holder.textViewHoraSalida.setText("No registro");
            } else{
                holder.textViewHoraSalida.setText(fichaje.getHoraSalida());
            }
            String horas = fichaje.getHoras();

            if(horas.equals("0")){
                holder.textViewHoras.setText("00:00");
            } else {
                holder.textViewHoras.setText(fichaje.getHoras());
            }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityactualizarEliminarFichajes.class);
                intent.putExtra("idFichaje", fichaje.getId());
                intent.putExtra("codigoEmpleado", fichaje.getEmpleadoCodigo());
                intent.putExtra("fecha", fichaje.getFecha());
                intent.putExtra("horaEntrada", fichaje.getHoraEntrada());
                intent.putExtra("horaSalida", fichaje.getHoraSalida());
                intent.putExtra("horas", fichaje.getHoras());
                activity.startActivityForResult(intent, 1);
            }
        });

    }


    @Override
    public int getItemCount() {
        return fichajesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCodigo;
        TextView textViewFecha;
        TextView textViewHoraEntrada;
        TextView textViewHoraSalida;
        TextView textViewHoras;

        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCodigo = itemView.findViewById(R.id.textViewCodigo);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
            textViewHoraEntrada = itemView.findViewById(R.id.textViewHoraEntrada);
            textViewHoraSalida = itemView.findViewById(R.id.textViewHoraSalida);
            textViewHoras = itemView.findViewById(R.id.textViewHoras);
            linearLayout = itemView.findViewById(R.id.LinearLayoutFichajes);
        }
    }
}
