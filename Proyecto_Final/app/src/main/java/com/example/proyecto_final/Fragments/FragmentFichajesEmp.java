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

import com.example.finalproyect.Adapters.FichajesAdapter;
import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.MainPages.MainPageEmpleado;
import com.example.finalproyect.Models.Fichaje;
import com.example.finalproyect.R;

import java.util.ArrayList;

public class FragmentFichajesEmp extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Fichaje> fichajes;
    String codigoEmpleado;
    ImageView img_no_dat;

    public FragmentFichajesEmp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fichajes_emp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        MainPageEmpleado mainPageEmpleado = (MainPageEmpleado) getActivity();

        codigoEmpleado = mainPageEmpleado.getCodigoEmpleado();
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recylerViewFichajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        img_no_dat = view.findViewById(R.id.imageview_nmo_data);


        // Aquí debes obtener la lista de fichajes desde tu base de datos o desde donde los tengas
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());

        // Crea un adaptador y configúralo con la lista de fichajes
        fichajes = dbHelper.obtenerFichajesPorCodigoEmpleado(codigoEmpleado);



        // Verifica si la lista de empleados está vacía
        if (fichajes.isEmpty()) {
            // Si está vacía, muestra la imagen y oculta el RecyclerView
            img_no_dat.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No hay fichajes disponibles", Toast.LENGTH_SHORT).show();
        } else {
            // Si no está vacía, muestra el RecyclerView y oculta la imagen
            img_no_dat.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Configura el RecyclerView con los datos
            FichajesAdapter adapter = new FichajesAdapter(dbHelper.obtenerFichajesPorCodigoEmpleado(codigoEmpleado));
            recyclerView.setAdapter(adapter);
        }
    }


}
