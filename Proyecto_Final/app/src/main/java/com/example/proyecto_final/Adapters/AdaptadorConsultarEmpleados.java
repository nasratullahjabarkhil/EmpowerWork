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

import com.example.finalproyect.Activities.ActivityModificarElemininarEmpleado;
import com.example.finalproyect.Models.Empleados;
import com.example.finalproyect.R;

import java.util.ArrayList;

public class AdaptadorConsultarEmpleados extends RecyclerView.Adapter<AdaptadorConsultarEmpleados.EmpleadosViewHolder> {

    ArrayList<Empleados> listaEmpleados;
    Activity activity;
    Context context;

    public AdaptadorConsultarEmpleados(Activity activity, Context context, ArrayList<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public EmpleadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datos_consultar_empleados, parent, false);
        return new EmpleadosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadosViewHolder holder, int position) {
        Empleados empleado = listaEmpleados.get(position);
        holder.nombre.setText(empleado.getNombre().toUpperCase());
        holder.apellido.setText(empleado.getApellido().toUpperCase());
        holder.codigo.setText(empleado.getCodigo());
        holder.puesto.setText(empleado.getPuesto());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityModificarElemininarEmpleado.class);
                intent.putExtra("codigo", empleado.getCodigo());
                intent.putExtra("nombre", empleado.getNombre());
                intent.putExtra("apellido", empleado.getApellido());
                intent.putExtra("correo", empleado.getEmail());
                intent.putExtra("puesto", empleado.getPuesto());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEmpleados.size();
    }

    public class EmpleadosViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, apellido, codigo, puesto;
        LinearLayout linearLayout;

        public EmpleadosViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreEmpleado);
            apellido = itemView.findViewById(R.id.apellidoEmpleado);
            codigo = itemView.findViewById(R.id.codigoEmpleado);
            puesto = itemView.findViewById(R.id.puestoEmpleado);
            linearLayout = itemView.findViewById(R.id.EliminarActualizarEmpleado);
        }
    }

    public void actualizarListaEmpleados(ArrayList<Empleados> nuevaListaEmpleados) {
        this.listaEmpleados.clear();
        this.listaEmpleados.addAll(nuevaListaEmpleados);
        notifyDataSetChanged();
    }



}
