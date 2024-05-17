package com.example.proyecto_final.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.R;

public class ActivityactualizarEliminarFichajes extends AppCompatActivity {

    TextView codigoEmpleado, nombre;
    EditText fecha, horaEntrada, horaSalida, horas;
    Button btnGuardar, btnEliminar;
    int idFichaje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityactualizar_eliminar_fichajes);

        nombre = findViewById(R.id.nombreEmpleado);
        codigoEmpleado = findViewById(R.id.codigoEmpleado);
        fecha = findViewById(R.id.fecha);
        horaEntrada = findViewById(R.id.horaEntrada);
        horaSalida = findViewById(R.id.horaSalida);
        horas = findViewById(R.id.horas);
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnEliminar = findViewById(R.id.buttonEliminar);

        // Obtener los datos pasados desde el adaptador
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("idFichaje") && intent.hasExtra("codigoEmpleado") && intent.hasExtra("fecha") &&
                intent.hasExtra("horaEntrada") && intent.hasExtra("horaSalida") && intent.hasExtra("horas")) {
            // Obtener los datos del intent
             idFichaje = intent.getIntExtra("idFichaje", 0);

            String codigoEmp = intent.getStringExtra("codigoEmpleado");
            String fecha1 = intent.getStringExtra("fecha");
            String entrada = intent.getStringExtra("horaEntrada");
            String salida = intent.getStringExtra("horaSalida");
            String horas1 = intent.getStringExtra("horas");

            // Crear instancia de DatabaseHelper
            DatabaseHelper db = new DatabaseHelper(ActivityactualizarEliminarFichajes.this);

            // Obtener nombre y apellido del empleado usando el código
            String[] nombreYApellido = db.consultarNombreYApellidoPorCodigo(codigoEmp);
            String nombreEmpleado = nombreYApellido[0]; // Nombre del empleado
            String apellidoEmpleado = nombreYApellido[1]; // Apellido del empleado


            // Mostrar los datos en los EditText y TextView correspondientes
            nombre.setText(nombreEmpleado + " " + apellidoEmpleado);
            codigoEmpleado.setText(codigoEmp);
            fecha.setText(fecha1);
            horaEntrada.setText(entrada);
            if(salida == null){
                horaSalida.setText("No registro");
            } else{
                horaSalida.setText(entrada);
            }
            if(horas1.equals("0")){
                horas.setText("00:00");
            } else {
                horas.setText(horas1);
            }
        }



        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén los datos actualizados del fichaje
                String nuevaHoraEntrada = horaEntrada.getText().toString();
                String nuevaHoraSalida = horaSalida.getText().toString();
                String nuevasHoras = horas.getText().toString();

                // Dentro del listener de btnActualizar
                DatabaseHelper databaseHelper = new DatabaseHelper(ActivityactualizarEliminarFichajes.this);
                databaseHelper.actualizarFichaje(idFichaje, nuevaHoraEntrada, nuevaHoraSalida, nuevasHoras);

                // Mostrar un mensaje de confirmación
                Toast.makeText(ActivityactualizarEliminarFichajes.this, "Fichaje actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Listener para el botón de eliminar
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un diálogo de confirmación
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityactualizarEliminarFichajes.this);
                builder.setMessage("¿Estás seguro de que deseas eliminar este fichaje?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Eliminar el fichaje de la base de datos
                        DatabaseHelper databaseHelper = new DatabaseHelper(ActivityactualizarEliminarFichajes.this);
                        databaseHelper.eliminarFichajePorId(idFichaje);

                        // Mostrar un mensaje de confirmación
                        Toast.makeText(ActivityactualizarEliminarFichajes.this, "Fichaje eliminado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada si el usuario cancela la eliminación
                    }
                });
                // Mostrar el diálogo
                builder.show();
            }
        });

    }
}
