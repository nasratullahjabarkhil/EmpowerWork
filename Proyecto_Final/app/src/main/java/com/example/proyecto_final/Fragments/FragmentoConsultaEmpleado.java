package com.example.proyecto_final.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyect.Adapters.AdaptadorConsultarEmpleados;
import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.Models.Empleados;
import com.example.finalproyect.R;

import java.util.ArrayList;

public class FragmentoConsultaEmpleado extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Empleados> empleados;
    ImageView imageViewNoData;

    DatabaseHelper dbHelper;

    public FragmentoConsultaEmpleado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmento_consulta_empleado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recylerViewEmpleados);
        imageViewNoData = view.findViewById(R.id.imageview_nmo_data);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());

        empleados = dbHelper.todosEmpleados(); // Obtén los empleados de la base de datos

        // Verifica si la lista de empleados está vacía
        if (empleados.isEmpty()) {
            // Si está vacía, muestra la imagen y oculta el RecyclerView
            imageViewNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // Si no está vacía, muestra el RecyclerView y oculta la imagen
            imageViewNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Configura el RecyclerView con los datos
            AdaptadorConsultarEmpleados adaptador = new AdaptadorConsultarEmpleados(getActivity(), getContext(), empleados);
            recyclerView.setAdapter(adaptador);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Fetch updated employee data from database
        empleados = dbHelper.todosEmpleados();

        // Update the adapter with new data
        if (empleados.isEmpty()) {
            imageViewNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            imageViewNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            AdaptadorConsultarEmpleados adaptador = new AdaptadorConsultarEmpleados(getActivity(), getContext(), empleados);
            recyclerView.setAdapter(adaptador);
        }
    }


}
