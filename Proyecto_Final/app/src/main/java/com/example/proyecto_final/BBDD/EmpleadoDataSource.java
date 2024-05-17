package com.example.proyecto_final.BBDD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.finalproyect.Models.Empleados;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoDataSource {

    private SQLiteDatabase database;
    private final DatabaseHelper dbHelper;

    public EmpleadoDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Empleados login(String identifier, String contrasena) {
        // Verifica si el identificador es un correo electrónico
        boolean isUsingEmail = identifier.contains("@");

        // Establece la columna para la verificación
        String columnToCheck;
        if (isUsingEmail) {
            columnToCheck = DatabaseHelper.COLUMN_EMAIL;
        } else {
            columnToCheck = DatabaseHelper.COLUMN_CODIGO;
        }

        // Realiza la consulta en la base de datos
        Cursor cursor = database.query(DatabaseHelper.TABLE_EMPLEADOS, null,
                columnToCheck + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?",
                new String[]{identifier, contrasena}, null, null, null);

        // Verifica si el empleado existe y tiene la contraseña correcta
        if (cursor != null && cursor.moveToFirst()) {
            // Obtiene los atributos del empleado

            int columnIndexNombre = cursor.getColumnIndex(DatabaseHelper.COLUMN_NOMBRE);
            int columnIndexApellido = cursor.getColumnIndex(DatabaseHelper.COLUMN_APELLIDO);
            int columnIndexCodigo = cursor.getColumnIndex(DatabaseHelper.COLUMN_CODIGO);


            String nombre = cursor.getString(columnIndexNombre);
            String apellido = cursor.getString(columnIndexApellido);
            String codigo = cursor.getString(columnIndexCodigo);



            // Cierra el cursor
            cursor.close();

            // Crea y devuelve un objeto Empleados con la información obtenida
            Empleados empleado = new Empleados();
            empleado.setNombre(nombre);
            empleado.setApellido(apellido);
            empleado.setCodigo(codigo);

            return empleado;
        }

        // Cierra el cursor
        if (cursor != null) {
            cursor.close();
        }

        // Devuelve null si no se encuentra el empleado
        return null;
    }


    public boolean eliminarEmpleado(String identifier, boolean isUsingEmail) {
        // Establece la columna para la eliminación
        String columnToDelete;
        if (isUsingEmail) {
            columnToDelete = DatabaseHelper.COLUMN_EMAIL;
        } else {
            columnToDelete = DatabaseHelper.COLUMN_CODIGO;
        }

        // Realiza la eliminación en la base de datos
        String selection = columnToDelete + " = ?";
        String[] selectionArgs = {identifier};

        int deletedRows = database.delete(DatabaseHelper.TABLE_EMPLEADOS, selection, selectionArgs);

        // Devuelve true si se eliminó al menos una fila, indicando que el empleado fue eliminado con éxito
        return deletedRows > 0;
    }




    public boolean hasFichadoEntradaHoy(String codigoEmpleado) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String fechaActual = obtenerFechaActual();

        Cursor cursor = db.query(DatabaseHelper.TABLE_FICHAJES, null,
                DatabaseHelper.COLUMN_EMPLEADO_CODIGO + " = ? AND " + DatabaseHelper.COLUMN_FECHA + " = ? AND " +
                        DatabaseHelper.COLUMN_HORA_ENTRADA + " IS NOT NULL",
                new String[]{codigoEmpleado, fechaActual}, null, null, null);

        boolean haFichadoEntrada = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return haFichadoEntrada;
    }

    public boolean hasFichadoSalidaHoy(String codigoEmpleado) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String fechaActual = obtenerFechaActual();

        Cursor cursor = db.query(DatabaseHelper.TABLE_FICHAJES, null,
                DatabaseHelper.COLUMN_EMPLEADO_CODIGO + " = ? AND " + DatabaseHelper.COLUMN_FECHA + " = ? AND " +
                        DatabaseHelper.COLUMN_HORA_SALIDA + " IS NOT NULL",
                new String[]{codigoEmpleado, fechaActual}, null, null, null);

        boolean haFichadoSalida = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return haFichadoSalida;
    }

    public void guardarEntrada(String codigoEmpleado, String fecha, String horaEntrada) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_EMPLEADO_CODIGO, codigoEmpleado);
            values.put(DatabaseHelper.COLUMN_FECHA, fecha);
            values.put(DatabaseHelper.COLUMN_HORA_ENTRADA, horaEntrada);
            values.put(DatabaseHelper.COLUMN_HORAS, "00:00"); // Inicialmente establecido en 0

            long result = db.insert(DatabaseHelper.TABLE_FICHAJES, null, values);
            db.close();

            if (result == -1) {
                // Error al insertar
                Log.e("EmpleadoDataSource", "Error al guardar la entrada");
            } else {
                // Éxito al insertar
                Log.d("EmpleadoDataSource", "Entrada guardada correctamente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("EmpleadoDataSource", "SQLException al guardar la entrada: " + e.getMessage());
        }
    }


    public void guardarSalida(String codigoEmpleado, String horaSalida) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String fechaActual = obtenerFechaActual();

        // Obtener la hora de entrada correspondiente
        String horaEntrada = obtenerHoraEntrada(codigoEmpleado, fechaActual);

        // Calcular la diferencia de horas
        String diferenciaHoras = calcularDiferenciaHoras(horaEntrada, horaSalida);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_HORA_SALIDA, horaSalida);
        values.put(DatabaseHelper.COLUMN_HORAS, diferenciaHoras); // Actualizar el campo horas con la diferencia de horas

        db.update(DatabaseHelper.TABLE_FICHAJES, values,
                DatabaseHelper.COLUMN_EMPLEADO_CODIGO + " = ? AND " +
                        DatabaseHelper.COLUMN_FECHA + " = ?",
                new String[]{codigoEmpleado, fechaActual});
        db.close();
    }


    private String obtenerHoraEntrada(String codigoEmpleado, String fecha) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_FICHAJES, new String[]{DatabaseHelper.COLUMN_HORA_ENTRADA},
                DatabaseHelper.COLUMN_EMPLEADO_CODIGO + " = ? AND " +
                        DatabaseHelper.COLUMN_FECHA + " = ?",
                new String[]{codigoEmpleado, fecha}, null, null, null);

        String horaEntrada = "";
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexHoraEntrada = cursor.getColumnIndex(DatabaseHelper.COLUMN_HORA_ENTRADA);
            horaEntrada = cursor.getString(columnIndexHoraEntrada);
        }
        return horaEntrada;
    }

    private String calcularDiferenciaHoras(String horaEntrada, String horaSalida) {


        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        try {
            Date dateEntrada = dateFormat.parse(horaEntrada);
            Date dateSalida = dateFormat.parse(horaSalida);

            long diferenciaMillis = dateSalida.getTime() - dateEntrada.getTime();
            long horas = diferenciaMillis / (60 * 60 * 1000);
            long minutos = (diferenciaMillis % (60 * 60 * 1000)) / (60 * 1000);

            // Formatea la diferencia en formato HH:mm
            return String.format("%02d:%02d", horas, minutos);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "00:00"; // Si hay algún error, devuelve un valor predeterminado
    }


    public String obtenerFechaActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = Calendar.getInstance().getTime();
        return dateFormat.format(date);
    }



    public boolean guardarCambiosEmpleado(Empleados empleado, String codigo) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOMBRE, empleado.getNombre());
        values.put(DatabaseHelper.COLUMN_APELLIDO, empleado.getApellido());
        values.put(DatabaseHelper.COLUMN_PUESTO, empleado.getPuesto());
        values.put(DatabaseHelper.COLUMN_EMAIL, empleado.getEmail());


        String whereClause = DatabaseHelper.COLUMN_CODIGO + " = ?";
        String[] whereArgs = {codigo};

        int rowsUpdated = database.update(DatabaseHelper.TABLE_EMPLEADOS, values, whereClause, whereArgs);

        return rowsUpdated > 0;
    }


    public Empleados buscarEmpleadoPorCodigoYCorreo(String codigoEmpleado, String correoEmpleado) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_EMPLEADOS, null,
                DatabaseHelper.COLUMN_CODIGO + " = ? AND " + DatabaseHelper.COLUMN_EMAIL + " = ?",
                new String[]{codigoEmpleado, correoEmpleado}, null, null, null);

        Empleados empleado = null;

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndexNombre = cursor.getColumnIndex(DatabaseHelper.COLUMN_NOMBRE);
            String nombre = cursor.getString(columnIndexNombre);

            int columnIndexApellido = cursor.getColumnIndex(DatabaseHelper.COLUMN_APELLIDO);
            String apellido = cursor.getString(columnIndexApellido);

            int columnIndexPuesto = cursor.getColumnIndex(DatabaseHelper.COLUMN_PUESTO);
            String puesto = cursor.getString(columnIndexPuesto);

            int columnIndexEmail = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);
            String email = cursor.getString(columnIndexEmail);

            int columnIndexPassword = cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD);
            String password = cursor.getString(columnIndexPassword);

            empleado = new Empleados();
            empleado.setCodigo(codigoEmpleado);
            empleado.setNombre(nombre);
            empleado.setApellido(apellido);
            empleado.setPuesto(puesto);
            empleado.setEmail(email);
            empleado.setPassword(password);

            cursor.close();
        }

        return empleado;
    }


    public boolean eliminarEmpleadoPorCodigo(String codigoEmpleado) {
        // Realiza la eliminación en la base de datos
        String selection = DatabaseHelper.COLUMN_CODIGO + " = ?";
        String[] selectionArgs = {codigoEmpleado};

        int deletedRows = database.delete(DatabaseHelper.TABLE_EMPLEADOS, selection, selectionArgs);

        // Devuelve true si se eliminó al menos una fila, indicando que el empleado fue eliminado con éxito
        return deletedRows > 0;
    }



}
