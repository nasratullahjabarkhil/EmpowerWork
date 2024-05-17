package com.example.proyecto_final.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyect.Adapters.AdaptadorConsultaFichajesAdmin;
import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.Models.Fichaje;
import com.example.finalproyect.R;

import java.util.ArrayList;

public class FragmentoConsultarFichajesAdmin extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Fichaje> fichajes;
    ImageView imageViewNoData;
    DatabaseHelper dbHelper;
    public FragmentoConsultarFichajesAdmin() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmento_consultar_fichajes_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recylerViewConsultarFichajes);
        imageViewNoData = view.findViewById(R.id.imageview_nmo_data);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());

        fichajes =  dbHelper.consultarTodosLosFichajes();

        // Verifica si la lista de empleados está vacía
        if (fichajes.isEmpty()) {
            // Si está vacía, muestra la imagen y oculta el RecyclerView
            imageViewNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No hay fichajes disponibles", Toast.LENGTH_SHORT).show();
        } else {
            // Si no está vacía, muestra el RecyclerView y oculta la imagen
            imageViewNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Configura el RecyclerView con los datos
            AdaptadorConsultaFichajesAdmin adaptador = new AdaptadorConsultaFichajesAdmin(getActivity(), getContext(), fichajes);
            recyclerView.setAdapter(adaptador);
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        // Fetch updated fichaje data from database
        fichajes = dbHelper.consultarTodosLosFichajes();

        // Update the adapter with new data
        if (fichajes.isEmpty()) {
            imageViewNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No hay fichajes disponibles", Toast.LENGTH_SHORT).show();
        } else {
            imageViewNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            AdaptadorConsultaFichajesAdmin adaptador = new AdaptadorConsultaFichajesAdmin(getActivity(), getContext(), fichajes);
            recyclerView.setAdapter(adaptador);
        }
    }
}