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

import com.example.finalproyect.BBDD.EmpleadoDataSource;
import com.example.finalproyect.Models.Empleados;
import com.example.finalproyect.R;

public class ActivityModificarElemininarEmpleado extends AppCompatActivity {

    TextView codigo;
    EditText nombre, apellido, correo, puesto;

    Button btnGuardar, btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_elemininar_empleado);

        // Referencias a los elementos de la interfaz de usuario
        codigo = findViewById(R.id.codigo);
        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        correo = findViewById(R.id.correo);
        puesto = findViewById(R.id.puesto);

        btnGuardar = findViewById(R.id.buttonGuardar);
        btnEliminar = findViewById(R.id.buttonEliminar);

        // Obtener los datos pasados desde el adaptador
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("codigo") && intent.hasExtra("nombre") &&
                intent.hasExtra("apellido") && intent.hasExtra("correo") && intent.hasExtra("puesto")) {
            // Obtener los datos del intent
            String codigoEmpleado = intent.getStringExtra("codigo");
            String nombreEmpleado = intent.getStringExtra("nombre");
            String apellidoEmpleado = intent.getStringExtra("apellido");
            String correoEmpleado = intent.getStringExtra("correo");
            String puestoEmpleado = intent.getStringExtra("puesto");

            // Mostrar los datos en los EditText y TextView correspondientes
            codigo.setText(codigoEmpleado);
            nombre.setText(nombreEmpleado);
            apellido.setText(apellidoEmpleado);
            correo.setText(correoEmpleado);
            puesto.setText(puestoEmpleado);
        }


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Obtener el código del empleado
                String codigoEmpleado = codigo.getText().toString();

                // Obtener los nuevos datos del empleado desde los EditText
                String nuevoNombre = nombre.getText().toString();
                String nuevoApellido = apellido.getText().toString();
                String nuevoCorreo = correo.getText().toString();
                String nuevoPuesto = puesto.getText().toString();

                // Crear un objeto Empleados con los nuevos datos
                Empleados empleadoActualizado = new Empleados();
                empleadoActualizado.setNombre(nuevoNombre);
                empleadoActualizado.setApellido(nuevoApellido);
                empleadoActualizado.setEmail(nuevoCorreo);
                empleadoActualizado.setPuesto(nuevoPuesto);


                // Obtener una instancia de EmpleadoDataSource
                EmpleadoDataSource empleadoDataSource = new EmpleadoDataSource(ActivityModificarElemininarEmpleado.this);

                // Abrir la conexión a la base de datos
                empleadoDataSource.open();

                // Llamar al método para guardar cambios pasando el objeto Empleados y el código del empleado
                boolean cambiosGuardados = empleadoDataSource.guardarCambiosEmpleado(empleadoActualizado, codigoEmpleado);

                // Cerrar la conexión a la base de datos
                empleadoDataSource.close();

                // Verificar si los cambios se guardaron correctamente
                if (cambiosGuardados) {
                    // Mostrar mensaje de éxito
                    Toast.makeText(ActivityModificarElemininarEmpleado.this, "Los cambios se guardaron correctamente", Toast.LENGTH_SHORT).show();

                    finish();

                } else {
                    // Mostrar mensaje de error
                    Toast.makeText(ActivityModificarElemininarEmpleado.this, "No se pudieron guardar los cambios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarConfirmacionEliminacion();
            }
        });


    }


    private void mostrarConfirmacionEliminacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Eliminación");
        builder.setMessage("¿Está seguro de que desea eliminar este empleado?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Eliminar el empleado
                eliminarEmpleado();
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void eliminarEmpleado() {
        String codigoEmpleado = codigo.getText().toString();
        EmpleadoDataSource empleadoDataSource = new EmpleadoDataSource(ActivityModificarElemininarEmpleado.this);
        empleadoDataSource.open();
        boolean eliminado = empleadoDataSource.eliminarEmpleadoPorCodigo(codigoEmpleado);
        empleadoDataSource.close();
        if (eliminado) {
            // Mostrar mensaje de éxito
            Toast.makeText(ActivityModificarElemininarEmpleado.this, "El empleado fue eliminado correctamente", Toast.LENGTH_SHORT).show();
            // Redirigir a la actividad principal o realizar cualquier otra acción necesaria
        } else {
            // Mostrar mensaje de error
            Toast.makeText(ActivityModificarElemininarEmpleado.this, "No se pudo eliminar el empleado", Toast.LENGTH_SHORT).show();
        }
    }
}
