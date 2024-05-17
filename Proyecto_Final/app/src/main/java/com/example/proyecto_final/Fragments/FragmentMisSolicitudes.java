package com.example.proyecto_final.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyect.Adapters.SolicitudesAdapter;
import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.MainPages.MainPageEmpleado;
import com.example.finalproyect.Models.Solicitud;
import com.example.finalproyect.R;

import java.util.List;

public class FragmentMisSolicitudes extends Fragment {

    RecyclerView recyclerView;
    List<Solicitud> solicitudes;
    String codigoEmpleado;

    Button btnSolicitar;
    ImageView img_no_dat;

    public FragmentMisSolicitudes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mis_solicitudes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obteniendo el código de empleado desde el fragmento principal
        MainPageEmpleado mainPageEmpleado = (MainPageEmpleado) getActivity();
        codigoEmpleado = mainPageEmpleado.getCodigoEmpleado();

        recyclerView = view.findViewById(R.id.recylerViewSolicitudes);
        img_no_dat = view.findViewById(R.id.imageview_nmo_data);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Aquí debes obtener la lista de fichajes desde tu base de datos o desde donde los tengas
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity().getApplicationContext());

        // Crea un adaptador y configúralo con la lista de fichajes
        solicitudes = dbHelper.obtenerSolicitudesPorCodigoEmpleado(codigoEmpleado);

        // Verificar si la lista de solicitudes está vacía
        if (solicitudes.isEmpty()) {
            // Si está vacía, muestra la imagen y oculta el RecyclerView
            img_no_dat.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(mainPageEmpleado, "No hay solicitudes realizadas", Toast.LENGTH_SHORT).show();
        } else {
            // Si no está vacía, muestra el RecyclerView y oculta la imagen
            img_no_dat.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }



        // Crear un adaptador y configurarlo con la lista de solicitudes
        SolicitudesAdapter adapter = new SolicitudesAdapter(getContext(), dbHelper.obtenerSolicitudesPorCodigoEmpleado(codigoEmpleado));
        recyclerView.setAdapter(adapter);

        btnSolicitar = view.findViewById(R.id.buttonSolicitar);

        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del FragmentMisSolicitudes
                FragmentoSolicitudes fragmentSolicitudes = new FragmentoSolicitudes();

                // Obtener el FragmentManager del Activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Iniciar una transacción de fragmentos
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Reemplazar el contenido del contenedor con el fragmento FragmentMisSolicitudes
                transaction.replace(R.id.frameLayout, fragmentSolicitudes);

                // Agregar la transacción a la pila de retroceso
                transaction.addToBackStack(null);

                // Commit la transacción
                transaction.commit();
            }
        });
    }
}
