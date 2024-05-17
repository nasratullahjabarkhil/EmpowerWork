package com.example.proyecto_final.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.BBDD.EmpleadoDataSource;
import com.example.finalproyect.MainPages.MainPageEmpleado;
import com.example.finalproyect.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentoFichar extends Fragment {
    LinearLayout entrar, salir;
    EmpleadoDataSource empleadoDataSource;
    DatabaseHelper databaseHelper;
    String codigoEmpleado;

    public FragmentoFichar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmento_fichar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        empleadoDataSource = new EmpleadoDataSource(getActivity().getApplicationContext());

        MainPageEmpleado mainPageEmpleado = (MainPageEmpleado) getActivity();

        codigoEmpleado = mainPageEmpleado.getCodigoEmpleado();

        entrar = view.findViewById(R.id.linearLayoutEntrar);
        salir = view.findViewById(R.id.linearLayoutSalir);

        // Para el botón de fichar entrada
        entrar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Aquí se ejecuta el código cuando se realiza un long clic en el botón de entrar
                ficharEntrada();
                return true; // Devuelve true para indicar que el long clic ha sido manejado
            }
        });
        // Para el botón de fichar salida
        salir.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Aquí se ejecuta el código cuando se realiza un long clic en el botón de salir
                ficharSalida();
                TextView horasTotales =mainPageEmpleado.findViewById(R.id.horas);
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                // Calcular el total de horas extras
                String horasActuales = databaseHelper.calcularHorasTotalesTrabajadas(codigoEmpleado);

                horasTotales.setText("Horas totales: " +horasActuales);
                return true; // Devuelve true para indicar que el long clic ha sido manejado
            }
        });
    }

    private void ficharEntrada() {
        try {
            if (!empleadoDataSource.hasFichadoEntradaHoy(codigoEmpleado)) {
                String fechaActual = empleadoDataSource.obtenerFechaActual();
                String horaEntrada = obtenerHoraActual();
                empleadoDataSource.guardarEntrada(codigoEmpleado, fechaActual, horaEntrada);
                Toast.makeText(getActivity().getApplicationContext(), "Entrada registrada correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Ya has fichado la entrada hoy", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Error al fichar la entrada", Toast.LENGTH_SHORT).show();
        }
    }

    private void ficharSalida() {
        try {
            if (empleadoDataSource.hasFichadoEntradaHoy(codigoEmpleado)) {
                if (!empleadoDataSource.hasFichadoSalidaHoy(codigoEmpleado)) {
                    String horaSalida = obtenerHoraActual();
                    empleadoDataSource.guardarSalida(codigoEmpleado, horaSalida);
                    Toast.makeText(getActivity().getApplicationContext(), "Salida registrada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Ya has fichado la salida hoy", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No has realizado la entrada", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Error al fichar la salida", Toast.LENGTH_SHORT).show();
        }
    }

    private String obtenerHoraActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
