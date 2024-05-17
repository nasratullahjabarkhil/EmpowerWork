package com.example.proyecto_final.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyect.Adapters.AdaptadorTodasSolicitudes;
import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.Models.Solicitud;
import com.example.finalproyect.R;

import java.util.ArrayList;


public class FragmentoSolicitudesAdmin extends Fragment {

    RecyclerView recyclerView;
    ImageView img_no_dat;
    Spinner filterSpinner; // Agregamos el Spinner
    ArrayList<Solicitud> solicitudes;
    AdaptadorTodasSolicitudes adaptador;
    Spinner spinnerFilter;

    DatabaseHelper dbHelper;

    public FragmentoSolicitudesAdmin() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        return inflater.inflate(R.layout.fragment_fragmento_solicitudes_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewConsultarSolicitudes);
        filterSpinner = view.findViewById(R.id.filter_spinner); // Inicializamos el Spinner
        img_no_dat = view.findViewById(R.id.imageview_nmo_data);
        spinnerFilter = view.findViewById(R.id.spinnerTipoSolicitud);
        // Configurar el adaptador del Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.estado_solicitud, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Lógica de filtrado aquí
                String selectedState = parentView.getItemAtPosition(position).toString();
                // Llamar al método de filtrado del RecyclerView Adapter
                if (adaptador != null) {
                    adaptador.filterByState(selectedState);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona nada
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());

        solicitudes = dbHelper.todasLasSolicitudes();


        // Verifica si la lista de solicitudes está vacía
        if (solicitudes.isEmpty()) {
            // Si está vacía, muestra la imagen y oculta el RecyclerView
            img_no_dat.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // Si no está vacía, muestra el RecyclerView y oculta la imagen
            img_no_dat.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Configura el RecyclerView con los datos
            adaptador = new AdaptadorTodasSolicitudes(getActivity(), getContext(), solicitudes);
            recyclerView.setAdapter(adaptador);

        }

    }
    @Override
    public void onResume() {
        super.onResume();

        // Obtener datos actualizados de la base de datos
        solicitudes = dbHelper.todasLasSolicitudes();

        // Actualizar datos del adaptador
        if (adaptador != null) {
            // Actualizar datos del adaptador
            adaptador.updateSolicitudes(solicitudes);
            adaptador.notifyDataSetChanged();
        }


        // Comprobar si la lista está vacía y actualizar la visibilidad
    }



}


