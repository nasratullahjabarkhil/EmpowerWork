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

import com.example.finalproyect.Activities.ActivityEliminarActualizarSolicitudes;
import com.example.finalproyect.Models.Solicitud;
import com.example.finalproyect.R;

import java.util.ArrayList;

public class AdaptadorTodasSolicitudes extends RecyclerView.Adapter<AdaptadorTodasSolicitudes.MyViewHolder> {

    private ArrayList<Solicitud> listaSolicitudes;
    private ArrayList<Solicitud> listaOriginalSolicitudes; // Lista original sin filtrar
    private Activity activity;
    private Context context;

    public AdaptadorTodasSolicitudes(Activity activity, Context context, ArrayList<Solicitud> solicitudes) {
        this.activity = activity;
        this.context = context;
        this.listaSolicitudes = solicitudes;
        this.listaOriginalSolicitudes = new ArrayList<>(solicitudes); // Inicializar lista original
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datos_todas_solicitudes, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Solicitud solicitud = listaSolicitudes.get(position);
        holder.codig.setText(solicitud.getCodigoEmpleado());
        holder.fechaInicioTextView.setText(solicitud.getFechaInicio());
        holder.fechaFinTextView.setText(solicitud.getFechaFin());
        holder.tipoTextView.setText(solicitud.getTipoSolicitud());
        //holder.observaciones.setText(solicitud.getObservaciones());
        holder.diasTextView.setText(String.valueOf(solicitud.getDias()));

        // Establecer el texto del estado
        String estado = solicitud.getEstadoSolicitud();
        holder.estadoTextView.setText(estado);

        // Cambiar el color del texto según el estado de la solicitud
        if (estado.equals("Pendiente")) {
            holder.estadoTextView.setTextColor(context.getResources().getColor(R.color.pending)); // Color blanco
        } else if (estado.equals("Confirmada")) {
            holder.estadoTextView.setTextColor(context.getResources().getColor(R.color.confirmed)); // Color verde
        } else if (estado.equals("Rechazada")) {
            holder.estadoTextView.setTextColor(context.getResources().getColor(R.color.rejected)); // Color rojo
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityEliminarActualizarSolicitudes.class);
                intent.putExtra("id", solicitud.getId());
                intent.putExtra("codigoEmpleado", solicitud.getCodigoEmpleado());
                intent.putExtra("f.inicio", solicitud.getFechaInicio());
                intent.putExtra("f.fin", solicitud.getFechaFin());
                intent.putExtra("tipo", solicitud.getTipoSolicitud());
                intent.putExtra("observacion", solicitud.getObservaciones());
                intent.putExtra("estado", solicitud.getEstadoSolicitud());
                intent.putExtra("dias", solicitud.getDias());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaSolicitudes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView codig, fechaInicioTextView, fechaFinTextView, tipoTextView, observaciones, diasTextView, estadoTextView;
        LinearLayout linearLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            codig = itemView.findViewById(R.id.TextCodigo);
            fechaInicioTextView = itemView.findViewById(R.id.fechaInicio1);
            fechaFinTextView = itemView.findViewById(R.id.fechaFin1);
            tipoTextView = itemView.findViewById(R.id.tipo1);
            estadoTextView = itemView.findViewById(R.id.estado1);
            diasTextView = itemView.findViewById(R.id.dias1);
            //observaciones = itemView.findViewById(R.id.obs1);
            linearLayout = itemView.findViewById(R.id.LinearLayoutEliminarActualizarSolicitud);
        }
    }


    // Método para filtrar las solicitudes por estado
    public void filterByState(String state) {
        ArrayList<Solicitud> listaFiltrada = new ArrayList<>();
        if (state.equals("Filtrar por estado")) {
            // Si el estado seleccionado es "Filtrar por estado", mostramos todas las solicitudes sin filtrar
            listaFiltrada.addAll(listaOriginalSolicitudes);
        } else {
            // Filtramos las solicitudes según el estado seleccionado
            for (Solicitud solicitud : listaOriginalSolicitudes) {
                if (solicitud.getEstadoSolicitud().equals(state)) {
                    listaFiltrada.add(solicitud);
                }
            }
        }
        // Actualizamos la lista de solicitudes con la lista filtrada
        listaSolicitudes = listaFiltrada;
        notifyDataSetChanged(); // Notificar al RecyclerView que los datos han cambiado
    }

    public void updateSolicitudes(ArrayList<Solicitud> nuevasSolicitudes) {
        // Limpiar la lista actual
        listaSolicitudes.clear();

        // Añadir las nuevas solicitudes
        listaSolicitudes.addAll(nuevasSolicitudes);

        // Notificar al adaptador que los datos han cambiado
        notifyDataSetChanged();

    }



}
