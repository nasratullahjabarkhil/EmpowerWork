package com.example.proyecto_final.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyect.Models.Solicitud;
import com.example.finalproyect.R;

import java.util.List;

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudViewHolder> {

    private List<Solicitud> listaSolicitudes;
    Context context;


    public SolicitudesAdapter(Context context, List<Solicitud> solicitudes) {
        this.listaSolicitudes = solicitudes;
        this.context = context;

    }

    @NonNull
    @Override
    public SolicitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datos_mis_solicitudes, parent, false);
        return new SolicitudViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudViewHolder holder, int position) {
        Solicitud solicitud = listaSolicitudes.get(position);
        holder.fechaInicioTextView.setText(solicitud.getFechaInicio());
        holder.fechaFinTextView.setText(solicitud.getFechaFin());
        holder.tipoTextView.setText(solicitud.getTipoSolicitud());
        String estado = solicitud.getEstadoSolicitud();
        if (estado.equalsIgnoreCase("Pendiente")) {
            holder.estadoTextView.setTextColor(context.getResources().getColor(R.color.pending)); // Color blanco
        } else if (estado.equalsIgnoreCase("Confirmada")) {
            holder.estadoTextView.setTextColor(context.getResources().getColor(R.color.confirmed)); // Color verde
        } else if (estado.equalsIgnoreCase("Rechazada")) {
            holder.estadoTextView.setTextColor(context.getResources().getColor(R.color.rejected)); // Color rojo
        }
        holder.estadoTextView.setText(estado);
        holder.diasTextView.setText(solicitud.getDias());


    }

    @Override
    public int getItemCount() {
        return listaSolicitudes.size();
    }

    static class SolicitudViewHolder extends RecyclerView.ViewHolder {
        TextView fechaInicioTextView, fechaFinTextView, tipoTextView, diasTextView, estadoTextView;
        LinearLayout linearLayout;

        SolicitudViewHolder(View itemView) {
            super(itemView);
            fechaInicioTextView = itemView.findViewById(R.id.fechaInicio);
            fechaFinTextView = itemView.findViewById(R.id.fechaFin);
            tipoTextView = itemView.findViewById(R.id.tipo);
            estadoTextView = itemView.findViewById(R.id.estado);
            diasTextView = itemView.findViewById(R.id.dias);
            linearLayout = itemView.findViewById(R.id.LinearLayoutEliminarActualizarSolicitud);
        }
    }
}
