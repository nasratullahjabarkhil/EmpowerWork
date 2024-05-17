package com.example.proyecto_final.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.MainPages.MainPageEmpleado;
import com.example.finalproyect.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FragmentoSolicitudes extends Fragment {

    private Calendar calendarInicio, calendarFin;
    private SimpleDateFormat dateFormat;
    private EditText editTextFechaInicio, editTextFechaFin, editTextObservaciones;
    private Button buttonEnviar;
    private String codigoEmpleado;
    private Spinner spinnerTipoSolicitud;


    TextView solicitudesPendiente;


    public FragmentoSolicitudes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmento_solicitudes, container, false);

        // Inicializar calendarios y formato de fecha
        calendarInicio = Calendar.getInstance();
        calendarFin = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Obtener referencias a los EditText, Spinner y el botón
        editTextFechaInicio = view.findViewById(R.id.editTextFechaInicio);
        editTextFechaFin = view.findViewById(R.id.editTextFechaFin);
        editTextObservaciones = view.findViewById(R.id.editTextObservaciones);
        buttonEnviar = view.findViewById(R.id.buttonEnviar);
        spinnerTipoSolicitud = view.findViewById(R.id.spinnerTipoSolicitud);




        // Configurar adapter para el spinner de tipo de solicitud
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.tipos_solicitud, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoSolicitud.setAdapter(adapter);

        // Configurar listener para el spinner de tipo de solicitud
        spinnerTipoSolicitud.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle selection of item in spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });

        final TextView textViewCharCount = view.findViewById(R.id.textViewCharCount);

        editTextObservaciones.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int charCount = 200 - s.length(); // Máximo de 200 caracteres
                textViewCharCount.setText(String.valueOf(charCount));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Configurar listeners para los EditText de fecha
        editTextFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker(editTextFechaInicio, calendarInicio);
            }
        });

        editTextFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker(editTextFechaFin, calendarFin);
            }
        });

        // Configurar listener para el botón de enviar
        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener valores y enviar solicitud
                String fechaInicio = editTextFechaInicio.getText().toString();
                String fechaFin = editTextFechaFin.getText().toString();
                String observaciones = editTextObservaciones.getText().toString();
                String tipoSolicitud = spinnerTipoSolicitud.getSelectedItem().toString();

                MainPageEmpleado mainPageEmpleado = (MainPageEmpleado) getActivity();


                codigoEmpleado = mainPageEmpleado.getCodigoEmpleado();
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());



                // Verificar si se ha seleccionado un tipo de solicitud
                if (tipoSolicitud.equals(getString(R.string.seleccionar_tipo_solicitud))) {
                    Toast.makeText(getContext(), "Por favor seleccione un tipo de solicitud", Toast.LENGTH_SHORT).show();
                    return; // Salir del método si no se ha seleccionado un tipo de solicitud
                }

                if (!fechaInicio.isEmpty() && !fechaFin.isEmpty() && !observaciones.isEmpty()) {
                    databaseHelper.guardarSolicitud(codigoEmpleado, fechaInicio, fechaFin, observaciones, tipoSolicitud);
                    int solicitudesPendientes = databaseHelper.obtenerSolicitudesPendientes(codigoEmpleado);

                    if (mainPageEmpleado != null) {
                        solicitudesPendiente = mainPageEmpleado.findViewById(R.id.solicitudesPendientes);
                        if (solicitudesPendiente != null) {
                            // Accede al TextView y realiza las operaciones necesarias
                            solicitudesPendiente.setText("Solicitudes Pendientes: " + solicitudesPendientes);                } else {
                        }
                    }
                    Toast.makeText(getContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button buttonMisSolicitudes = view.findViewById(R.id.buttonMisSolicitudes);

        // Configurar OnClickListener para el botón "mis solicitud"
        buttonMisSolicitudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del FragmentMisSolicitudes
                FragmentMisSolicitudes fragmentMisSolicitudes = new FragmentMisSolicitudes();

                // Obtener el FragmentManager del Activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Iniciar una transacción de fragmentos
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Reemplazar el contenido del contenedor con el fragmento FragmentMisSolicitudes
                transaction.replace(R.id.frameLayout, fragmentMisSolicitudes);

                // Agregar la transacción a la pila de retroceso
                transaction.addToBackStack(null);

                // Commit la transacción
                transaction.commit();
            }
        });

        return view;
    }

    // Método para mostrar el selector de fecha
    private void mostrarDatePicker(final EditText editTextFecha, final Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Actualizar texto del EditText con la fecha seleccionada
                        editTextFecha.setText(dateFormat.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }


}
