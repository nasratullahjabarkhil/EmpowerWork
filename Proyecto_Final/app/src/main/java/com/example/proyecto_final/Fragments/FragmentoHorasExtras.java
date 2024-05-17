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

import com.example.finalproyect.Adapters.HorasExtrasAdapter;
import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.MainPages.MainPageEmpleado;
import com.example.finalproyect.Models.Fichaje;
import com.example.finalproyect.R;

import java.util.ArrayList;

public class FragmentoHorasExtras extends Fragment {

    String codigoEmpleado;
    RecyclerView recyclerView;
    HorasExtrasAdapter adapter;
    ArrayList<Fichaje> fichajes;
    ImageView img_no_dat;

    public FragmentoHorasExtras() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmento_horas_extras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainPageEmpleado mainPageEmpleado = (MainPageEmpleado) getActivity();
        codigoEmpleado = mainPageEmpleado.getCodigoEmpleado();

        // Obtener la lista de fichajes desde tu base de datos o desde donde la tengas
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());

        // Obtener la lista de fichajes actualizada
        fichajes = dbHelper.obtenerFichajesPorCodigoEmpleado(codigoEmpleado);

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recylerViewHoras);
        img_no_dat = view.findViewById(R.id.imageview_nmo_data);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Crear el adaptador y configurarlo con la lista de fichajes
        adapter = new HorasExtrasAdapter(fichajes);
        recyclerView.setAdapter(adapter);
        // Verificar si la lista de fichajes está vacía
        if (fichajes.isEmpty()) {
            // Si está vacía, muestra la imagen y oculta el RecyclerView
            img_no_dat.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No hay horas disponibles", Toast.LENGTH_SHORT).show();
        } else {
            // Si no está vacía, muestra el RecyclerView y oculta la imagen
            img_no_dat.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
