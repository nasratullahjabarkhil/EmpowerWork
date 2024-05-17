package com.example.proyecto_final.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.R;

import java.util.Arrays;


public class ActivityEliminarActualizarSolicitudes extends AppCompatActivity {
    TextView idEmp, txtCodigo, nombre;
    EditText fechaIn, fechaFin, tipo, obser, dias, estado;
    Button btnGuardar, btnEliminar;


    Spinner spinnerEstado;
    String[] estadosSpinner = {"Pendiente", "Confirmada", "Rechazada"}; // Definir los estados

    String id, codigoEmp, fechaInicio, fechaFinn, tipos,observaciones, dia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_actualizar_solicitudes);
        idEmp = findViewById(R.id.idSolicitudEmp);
        txtCodigo = findViewById(R.id.codigoEmp);

        nombre = findViewById(R.id.nombreEmpleado);
        fechaIn = findViewById(R.id.fInicio);
        fechaFin = findViewById(R.id.fFin);
        tipo = findViewById(R.id.tipoSolicitud);
        obser = findViewById(R.id.obsSolicitud);
        dias = findViewById(R.id.diasSolicitud);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.BtnEliminar);
        spinnerEstado = findViewById(R.id.spinnerEstado);


        // Configurar adaptador para el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, estadosSpinner);
        spinnerEstado.setAdapter(adapter);


        // Cargar los datos de la solicitud
        getIntentData();

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarDialogoConfirmacionEliminarSolicitud(id);
            }
        });
        getIntentData();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoid = idEmp.getText().toString();
                String nuevoCodigo = txtCodigo.getText().toString();
                String nuevaFechaInicio = fechaIn.getText().toString();
                String nuevaFechaFin = fechaFin.getText().toString();
                String nuevaObservacion = obser.getText().toString();
                String nuevoTipoSolicitud = tipo.getText().toString();
                String nuevosDias = dias.getText().toString();
                String nuevoEstado = spinnerEstado.getSelectedItem().toString();

                DatabaseHelper databaseHelper = new DatabaseHelper(ActivityEliminarActualizarSolicitudes.this);
                databaseHelper.actualizarSolicitud(nuevoid, nuevoCodigo, nuevaFechaInicio, nuevaFechaFin, nuevaObservacion,
                        nuevoTipoSolicitud, nuevosDias, nuevoEstado );


                finish();

                //adaptadorTodasSolicitudes.actualizarSolicitudes(solicitudesActualizadas);



                // Aquí notificamos al adaptador después de actualizar los datos
                //adaptadorTodasSolicitudes.filterByState(nuevoEstado);

            }



        });


    }
    void getIntentData(){
        // Resto del código para obtener otros datos del Intent
        if (getIntent().hasExtra("id") && getIntent().hasExtra("codigoEmpleado") && getIntent().hasExtra("f.inicio") &&
                getIntent().hasExtra("f.fin") && getIntent().hasExtra("tipo") && getIntent().hasExtra("observacion") &&
                getIntent().hasExtra("dias")) {
            id = getIntent().getStringExtra("id");
            codigoEmp = getIntent().getStringExtra("codigoEmpleado");
            fechaInicio = getIntent().getStringExtra("f.inicio");
            fechaFinn = getIntent().getStringExtra("f.fin");
            tipos = getIntent().getStringExtra("tipo");
            observaciones = getIntent().getStringExtra("observacion");
            dia = getIntent().getStringExtra("dias");

            // Crear instancia de DatabaseHelper
            DatabaseHelper db = new DatabaseHelper(ActivityEliminarActualizarSolicitudes.this);

            // Obtener nombre y apellido del empleado usando el código
            String[] nombreYApellido = db.consultarNombreYApellidoPorCodigo(codigoEmp);
            String nombreEmpleado = nombreYApellido[0]; // Nombre del empleado
            String apellidoEmpleado = nombreYApellido[1]; // Apellido del empleado


            // Obtener el estado inicial del Intent
            String estadoInicial = getIntent().getStringExtra("estado");
            // Buscar el índice del estado inicial en el array de estados
            int index = Arrays.asList(estadosSpinner).indexOf(estadoInicial);
            // Establecer el estado inicial seleccionado en el spinner
            spinnerEstado.setSelection(index);

            // Luego puedes usar estos datos como desees
            nombre.setText(nombreEmpleado + " " + apellidoEmpleado);
            idEmp.setText(id);
            txtCodigo.setText(codigoEmp);
            fechaIn.setText(fechaInicio);
            fechaFin.setText(fechaFinn);
            tipo.setText(tipos);
            obser.setText(observaciones);
            dias.setText(dia);
        } else {
            Toast.makeText(this, "No hay datos", Toast.LENGTH_SHORT).show();
        }
    }



    public void mostrarDialogoConfirmacionEliminarSolicitud(final String idSolicitud) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que quieres eliminar esta solicitud?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseHelper databaseHelper = new DatabaseHelper(ActivityEliminarActualizarSolicitudes.this);
                databaseHelper.eliminarSolicitudPorId(idSolicitud);
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}