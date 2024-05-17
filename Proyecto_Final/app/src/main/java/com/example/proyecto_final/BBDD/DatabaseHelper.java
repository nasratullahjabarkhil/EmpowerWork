package com.example.proyecto_final.BBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.finalproyect.Models.Empleados;
import com.example.finalproyect.Models.Fichaje;
import com.example.finalproyect.Models.Solicitud;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Constantes para el nombre de la base de datos y su versión
    private static final String DATABASE_NAME = "RegistroEmpleados.db";
    private static final int DATABASE_VERSION = 21;



    /// Constantes para la tabla de empleados y sus columnas
    public static final String TABLE_EMPLEADOS = "empleados";
    public static final String COLUMN_CODIGO = "codigo";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_APELLIDO = "apellido";
    public static final String COLUMN_PUESTO = "puesto";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IMAGEN = "imagen"; // Nuevo campo para la imagen
    public static final String COLUMN_IS_PRIMERA_VEZ = "isPrimeraVez"; // Nuevo campo para la contraseña aleatoria

    // Constantes para valores booleanos


    // Consulta SQL para crear la tabla de empleados
    private static final String SQL_CREATE_EMPLEADOS =
            "CREATE TABLE " + TABLE_EMPLEADOS + " (" +
                    COLUMN_CODIGO + " TEXT PRIMARY KEY NOT NULL," +
                    COLUMN_NOMBRE + " TEXT NOT NULL," +
                    COLUMN_APELLIDO + " TEXT NOT NULL," +
                    COLUMN_PUESTO + " TEXT NOT NULL," +
                    COLUMN_EMAIL + " TEXT UNIQUE NOT NULL," +
                    COLUMN_PASSWORD + " TEXT NOT NULL," +
                    COLUMN_IS_PRIMERA_VEZ + " INTEGER NOT NULL DEFAULT 1," + // La columna por defecto es FALSE (0)
                    COLUMN_IMAGEN + " BLOB NOT NULL)";

    // Constantes para la tabla de solicitudes y sus columnas
    public static final String TABLE_SOLICITUDES = "Solicitudes";
    public static final String COLUMN_SOLICITUD_ID = "id";
    public static final String COLUMN_CODIGO_EMPLEADO = "codigo_empleado";
    public static final String COLUMN_FECHA_INICIO = "fecha_inicio";
    public static final String COLUMN_FECHA_FIN = "fecha_fin";
    public static final String COLUMN_OBSERVACIONES = "observaciones";
    public static final String COLUMN_TIPO_SOLICITUD = "tipo_solicitud";
    public static final String COLUMN_DIAS = "dias"; // Nuevo campo para los días
    public static final String COLUMN_ESTADO_SOLICITUD = "estado"; // Columna para el estado

    // Consulta SQL para crear la tabla de solicitudes
    private static final String SQL_CREATE_SOLICITUDES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_SOLICITUDES + " (" +
                    COLUMN_SOLICITUD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_CODIGO_EMPLEADO + " TEXT NOT NULL," +
                    COLUMN_FECHA_INICIO + " TEXT NOT NULL," +
                    COLUMN_FECHA_FIN + " TEXT NOT NULL," +
                    COLUMN_OBSERVACIONES + " TEXT NOT NULL," +
                    COLUMN_TIPO_SOLICITUD + " TEXT NOT NULL," +
                    COLUMN_DIAS + " INTEGER NOT NULL," + // El nuevo campo es de tipo INTEGER
                    COLUMN_ESTADO_SOLICITUD + " TEXT NOT NULL)";


    // Constantes para la tabla de fichajes y sus columnas
    public static final String TABLE_FICHAJES = "fichajes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMPLEADO_CODIGO = "empleado_codigo";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_HORA_ENTRADA = "hora_entrada";
    public static final String COLUMN_HORA_SALIDA = "hora_salida";
    public static final String COLUMN_HORAS = "horas";

    // Consulta SQL para crear la tabla de fichajes
    private static final String SQL_CREATE_FICHAJES =
            "CREATE TABLE " + TABLE_FICHAJES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_EMPLEADO_CODIGO + " TEXT NOT NULL," +
                    COLUMN_FECHA + " TEXT NOT NULL," +
                    COLUMN_HORA_ENTRADA + " TEXT NOT NULL," +
                    COLUMN_HORA_SALIDA + " TEXT," +
                    COLUMN_HORAS + " TEXT NOT NULL)";

    // Consulta SQL para eliminar la tabla de empleados
    private static final String SQL_DELETE_EMPLEADOS =
            "DROP TABLE IF EXISTS " + TABLE_EMPLEADOS;

    // Consulta SQL para eliminar la tabla de fichajes
    private static final String SQL_DELETE_FICHAJES =
            "DROP TABLE IF EXISTS " + TABLE_FICHAJES;

    private static final String SQL_DELETE_SOLICITUDES =
            "DROP TABLE IF EXISTS " + TABLE_SOLICITUDES;

    Context context;
    // Constructor
    public DatabaseHelper(@Nullable  Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Método llamado cuando se crea la base de datos por primera vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EMPLEADOS);
        db.execSQL(SQL_CREATE_FICHAJES);
        db.execSQL(SQL_CREATE_SOLICITUDES);
    }

    // Método llamado cuando la base de datos necesita ser actualizada
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_EMPLEADOS);
        db.execSQL(SQL_DELETE_FICHAJES);
        db.execSQL(SQL_DELETE_SOLICITUDES);
        onCreate(db);
    }

    // Métodos para operaciones con empleados
    public ArrayList<Empleados> todosEmpleados() {
        ArrayList<Empleados> listaEmpleados = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorEmpleados = db.rawQuery("SELECT " + COLUMN_CODIGO + ", " + COLUMN_NOMBRE + ", " + COLUMN_APELLIDO + ", " + COLUMN_PUESTO + ", " + COLUMN_EMAIL + ", " + COLUMN_IS_PRIMERA_VEZ + " FROM " + TABLE_EMPLEADOS, null);

        if (cursorEmpleados.moveToFirst()) {
            do {
                Empleados empleados = new Empleados();
                empleados.setCodigo(cursorEmpleados.getString(0));
                empleados.setNombre(cursorEmpleados.getString(1));
                empleados.setApellido(cursorEmpleados.getString(2));
                empleados.setPuesto(cursorEmpleados.getString(3));
                empleados.setEmail(cursorEmpleados.getString(4));
                empleados.setContrasenaAleatoria(cursorEmpleados.getString(5)); // Establecer la contraseña aleatoria
                listaEmpleados.add(empleados);
            } while (cursorEmpleados.moveToNext());
            cursorEmpleados.close();
        }
        db.close();
        return listaEmpleados;
    }


    public ArrayList<Fichaje> obtenerFichajesPorCodigoEmpleado(String codigoEmpleado) {
        ArrayList<Fichaje> listaFichajes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_EMPLEADO_CODIGO, COLUMN_FECHA, COLUMN_HORA_ENTRADA, COLUMN_HORA_SALIDA};
        String selection = COLUMN_EMPLEADO_CODIGO + " = ?";
        String[] selectionArgs = {codigoEmpleado};

        String orderBy = COLUMN_FECHA + " DESC"; // Ordenar por fecha descendente

        Cursor cursorFichajes = db.query(TABLE_FICHAJES, projection, selection, selectionArgs, null, null, orderBy);

        if (cursorFichajes.moveToFirst()) {
            do {
                Fichaje fichaje = new Fichaje();
                fichaje.setEmpleadoCodigo(codigoEmpleado);
                fichaje.setFecha(cursorFichajes.getString(cursorFichajes.getColumnIndexOrThrow(COLUMN_FECHA)));

                // Validar y establecer hora de entrada
                String horaEntrada = cursorFichajes.getString(cursorFichajes.getColumnIndexOrThrow(COLUMN_HORA_ENTRADA));
                if (horaEntrada == null) {
                    horaEntrada = "No registro";
                }
                fichaje.setHoraEntrada(horaEntrada);

                // Validar y establecer hora de salida
                String horaSalida = cursorFichajes.getString(cursorFichajes.getColumnIndexOrThrow(COLUMN_HORA_SALIDA));
                if (horaSalida == null) {
                    horaSalida = "No registro";
                }
                fichaje.setHoraSalida(horaSalida);

                String horasExtras = calcularHorasExtras(fichaje.getHoraEntrada(), fichaje.getHoraSalida());
                if(horasExtras == null){
                    fichaje.setHoras("00:00");
                }
                fichaje.setHoras(horasExtras);

                listaFichajes.add(fichaje);
            } while (cursorFichajes.moveToNext());
        }
        cursorFichajes.close();
        db.close();
        return listaFichajes;
    }




    // Método para calcular las horas extras
    private String calcularHorasExtras(String horaEntrada, String horaSalida) {
        // Verificar si la hora de entrada o salida es "No registro"
        if ("No registro".equals(horaEntrada) || "No registro".equals(horaSalida)) {
            return "00:00";
        }

        // Convertir las horas de entrada y salida a minutos
        String[] entradaParts = horaEntrada.split(":");
        String[] salidaParts = horaSalida.split(":");

        int entradaHoras = Integer.parseInt(entradaParts[0]);
        int entradaMinutos = Integer.parseInt(entradaParts[1]);

        int salidaHoras = Integer.parseInt(salidaParts[0]);
        int salidaMinutos = Integer.parseInt(salidaParts[1]);

        // Calcular el total de minutos trabajados
        int totalMinutosEntrada = entradaHoras * 60 + entradaMinutos;
        int totalMinutosSalida = salidaHoras * 60 + salidaMinutos;

        int diferenciaMinutos = totalMinutosSalida - totalMinutosEntrada;

        // Verificar si la diferencia es mayor que 8 horas
        if (diferenciaMinutos > 8 * 60) { // Convertir 8 horas a minutos
            // Calcular las horas extras
            int horasExtras = (diferenciaMinutos - 8 * 60) / 60;
            int minutosExtras = (diferenciaMinutos - 8 * 60) % 60;

            // Formatear las horas extras
            return String.format("%02d:%02d", horasExtras, minutosExtras);
        } else {
            return "00:00"; // No hay horas extras
        }
    }



    // Método para recuperar datos del empleado por código
    public Empleados obtenerEmpleadoPorCodigo(String codigoEmpleado) {
        SQLiteDatabase db = this.getReadableDatabase();
        Empleados empleado = null;

        String[] projection = {
                COLUMN_CODIGO,
                COLUMN_NOMBRE,
                COLUMN_APELLIDO,
                COLUMN_PUESTO,
                COLUMN_EMAIL,
                COLUMN_PASSWORD
        };

        String selection = COLUMN_CODIGO + " = ?";
        String[] selectionArgs = {codigoEmpleado};

        Cursor cursor = db.query(
                TABLE_EMPLEADOS,   // Tabla a consultar
                projection,        // Columnas a incluir en el resultado
                selection,         // Cláusula WHERE (selección)
                selectionArgs,     // Valores para la cláusula WHERE
                null,              // Agrupar las filas
                null,              // Filtrar por grupos de filas
                null               // Orden de las filas
        );

        if (cursor != null && cursor.moveToFirst()) {
            empleado = new Empleados();
            empleado.setCodigo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODIGO)));
            empleado.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));
            empleado.setApellido(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO)));
            empleado.setPuesto(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUESTO)));
            empleado.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
            empleado.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)));
            cursor.close();
        }
        db.close();
        return empleado;
    }

    // Método para actualizar datos del empleado por código
    public void actualizarEmpleadoPorCodigo(String codigoEmpleado, String nombre, String apellido, String puesto, String email, String password, byte[] imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_APELLIDO, apellido);
        values.put(COLUMN_PUESTO, puesto);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_IMAGEN, imagen); // Agregar la imagen a los valores

        String selection = COLUMN_CODIGO + " = ?";
        String[] selectionArgs = {codigoEmpleado};

        db.update(TABLE_EMPLEADOS, values, selection, selectionArgs);
        db.close();
    }



    // Método para guardar la solicitud en la base de datos
    public void guardarSolicitud(String codigoEmpleado, String fechaInicio, String fechaFin, String observaciones, String tipoSolicitud) {
        // Verificar si la fecha de fin es anterior a la fecha de inicio
        if (fechaFinAnteriorFechaInicio(fechaInicio, fechaFin)) {
            // Mostrar un mensaje de error
            Toast.makeText(context, "La fecha de fin no puede ser anterior a la fecha de inicio", Toast.LENGTH_SHORT).show();
            return; // Salir del método sin realizar la inserción
        }

        // Calcular el número de días entre la fecha de inicio y la fecha de fin
        int dias = calcularDiasEntreFechas(fechaInicio, fechaFin);


        SQLiteDatabase db = this.getWritableDatabase();

        // Crear un objeto ContentValues para guardar los datos
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CODIGO_EMPLEADO, codigoEmpleado);
        values.put(DatabaseHelper.COLUMN_FECHA_INICIO, fechaInicio);
        values.put(DatabaseHelper.COLUMN_FECHA_FIN, fechaFin);
        values.put(DatabaseHelper.COLUMN_OBSERVACIONES, observaciones);
        values.put(DatabaseHelper.COLUMN_ESTADO_SOLICITUD, "Pendiente"); // Estado por defecto
        values.put(DatabaseHelper.COLUMN_TIPO_SOLICITUD, tipoSolicitud); // Tipo de solicitud
        values.put(DatabaseHelper.COLUMN_DIAS, dias); // Número de días entre las fechas

        // Insertar la nueva fila en la tabla de solicitudes
        long newRowId = db.insert(DatabaseHelper.TABLE_SOLICITUDES, null, values);

        // Verificar si la inserción fue exitosa
        if (newRowId != -1) {
            // Mostrar un mensaje de éxito
            Toast.makeText(context, "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show();
        } else {
            // Mostrar un mensaje de error si la inserción falló
            Toast.makeText(context, "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
        }

        // Cerrar la conexión con la base de datos
        db.close();
    }

    // Método para calcular el número de días entre dos fechas
    private int calcularDiasEntreFechas(String fechaInicio, String fechaFin) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fechaInicioDate = sdf.parse(fechaInicio);
            Date fechaFinDate = sdf.parse(fechaFin);
            // Calcular la diferencia en milisegundos entre las dos fechas
            long diferenciaMillis = fechaFinDate.getTime() - fechaInicioDate.getTime();
            // Convertir la diferencia de milisegundos a días
            return (int) (diferenciaMillis / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // En caso de error, retornamos 0 días
        }
    }


    // Método para verificar si la fecha de fin es anterior a la fecha de inicio
    private boolean fechaFinAnteriorFechaInicio(String fechaInicio, String fechaFin) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date fechaInicioDate = sdf.parse(fechaInicio);
            Date fechaFinDate = sdf.parse(fechaFin);
            // Comparamos las fechas
            return fechaFinDate.before(fechaInicioDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return true; // En caso de error, consideramos que la fecha de fin es anterior a la fecha de inicio
        }
    }



    public List<Solicitud> obtenerSolicitudesPorCodigoEmpleado(String codigoEmpleado) {
        List<Solicitud> listaSolicitudes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_FECHA_INICIO,
                COLUMN_FECHA_FIN,
                COLUMN_TIPO_SOLICITUD,
                COLUMN_OBSERVACIONES,
                COLUMN_DIAS,
                COLUMN_ESTADO_SOLICITUD
        };

        String selection = COLUMN_CODIGO_EMPLEADO + " = ?";
        String[] selectionArgs = {codigoEmpleado};

        // **Modificación:** Ordenar por fecha de inicio descendente
        String sortOrder = COLUMN_FECHA_INICIO + " DESC";

        Cursor cursor = db.query(
                TABLE_SOLICITUDES,   // Tabla a consultar
                projection,          // Columnas a incluir en el resultado
                selection,           // Cláusula WHERE (selección)
                selectionArgs,       // Valores para la cláusula WHERE
                null,                // Agrupar las filas
                null,                // Filtrar por grupos de filas
                sortOrder            // Orden de las filas
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_INICIO));
                String fechaFin = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_FIN));
                String tipoSolicitud = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_SOLICITUD));
                String observaciones = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBSERVACIONES));
                String dias = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIAS)));
                String estadoSolicitud = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO_SOLICITUD));

                // Crear objeto Solicitud y añadirlo a la lista
                Solicitud solicitud = new Solicitud(fechaInicio, fechaFin, tipoSolicitud, observaciones, dias, estadoSolicitud);
                listaSolicitudes.add(solicitud);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return listaSolicitudes;
    }


    public ArrayList<Solicitud> todasLasSolicitudes() {
        ArrayList<Solicitud> listaSolicitudes = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SOLICITUDES + " ORDER BY " + COLUMN_FECHA_INICIO + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    Solicitud solicitudes = new Solicitud();
                    solicitudes.setId(cursor.getString(0));
                    solicitudes.setCodigoEmpleado(cursor.getString(1));
                    solicitudes.setFechaInicio(cursor.getString(2));
                    solicitudes.setFechaFin(cursor.getString(3));
                    solicitudes.setObservaciones(cursor.getString(4));
                    solicitudes.setTipoSolicitud(cursor.getString(5));
                    solicitudes.setDias(cursor.getString(6));
                    solicitudes.setEstadoSolicitud(cursor.getString(7));
                    listaSolicitudes.add(solicitudes);


                } while (cursor.moveToNext());
            }
            // Cerrar el cursor después de usarlo
            cursor.close();
        }

        return listaSolicitudes;
    }


    public void actualizarSolicitud(String id, String codigoEmpleado, String nuevaFechaInicio, String nuevaFechaFin, String nuevaObservacion, String nuevoTipoSolicitud, String nuevosDias, String nuevoEstado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CODIGO_EMPLEADO, codigoEmpleado);
        values.put(COLUMN_FECHA_INICIO, nuevaFechaInicio);
        values.put(COLUMN_FECHA_FIN, nuevaFechaFin);
        values.put(COLUMN_OBSERVACIONES, nuevaObservacion);
        values.put(COLUMN_TIPO_SOLICITUD, nuevoTipoSolicitud);
        values.put(COLUMN_DIAS, nuevosDias);
        values.put(COLUMN_ESTADO_SOLICITUD, nuevoEstado);

        String whereClause = COLUMN_SOLICITUD_ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};

        int result = db.update(TABLE_SOLICITUDES, values, whereClause, whereArgs);

        if (result == -1) {
            Toast.makeText(context, "No se actualizó la solicitud", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Solicitud actualizada correctamente", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }


    public void eliminarSolicitudPorId(String idSolicitud) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = COLUMN_SOLICITUD_ID + " = ?";
        String[] whereArgs = {idSolicitud};

        int result = db.delete(TABLE_SOLICITUDES, whereClause, whereArgs);

        if (result == 0) {
            Toast.makeText(context, "No se encontró ninguna solicitud con el ID proporcionado", Toast.LENGTH_SHORT).show();
        } else if (result == 1) {
            Toast.makeText(context, "Solicitud eliminada correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Se eliminaron múltiples solicitudes", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public List<Solicitud> obtenerSolicitudesPorEstado(String estadoSolicitud) {
        List<Solicitud> listaSolicitudes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_SOLICITUD_ID,
                COLUMN_CODIGO_EMPLEADO,
                COLUMN_FECHA_INICIO,
                COLUMN_FECHA_FIN,
                COLUMN_OBSERVACIONES,
                COLUMN_TIPO_SOLICITUD,
                COLUMN_DIAS,
                COLUMN_ESTADO_SOLICITUD
        };

        String selection = COLUMN_ESTADO_SOLICITUD + " = ?";
        String[] selectionArgs = {estadoSolicitud};

        // Ordenar por fecha de inicio descendente
        String sortOrder = COLUMN_FECHA_INICIO + " DESC";

        Cursor cursor = db.query(
                TABLE_SOLICITUDES,   // Tabla a consultar
                projection,          // Columnas a incluir en el resultado
                selection,           // Cláusula WHERE (selección)
                selectionArgs,       // Valores para la cláusula WHERE
                null,                // Agrupar las filas
                null,                // Filtrar por grupos de filas
                sortOrder            // Orden de las filas
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String solicitudId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOLICITUD_ID));
                String codigoEmpleado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODIGO_EMPLEADO));
                String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_INICIO));
                String fechaFin = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_FIN));
                String observaciones = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBSERVACIONES));
                String tipoSolicitud = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_SOLICITUD));
                String dias = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIAS)));
                String estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO_SOLICITUD));

                // Crear objeto Solicitud y añadirlo a la lista si coincide con el estado buscado
                if (estado.equals(estadoSolicitud)) {
                    Solicitud solicitud = new Solicitud(solicitudId, codigoEmpleado, fechaInicio, fechaFin, observaciones, tipoSolicitud, dias, estado);
                    listaSolicitudes.add(solicitud);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return listaSolicitudes;
    }



    // Método para consultar todos los fichajes
    public ArrayList<Fichaje> consultarTodosLosFichajes() {
        ArrayList<Fichaje> listaFichajes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para obtener todos los fichajes
        String query = "SELECT * FROM " + TABLE_FICHAJES;

        Cursor cursor = db.rawQuery(query, null);

        // Verificar si el cursor contiene datos
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Obtener los datos de cada fila y crear un objeto Fichaje
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String empleadoCodigo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMPLEADO_CODIGO));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA));
                String horaEntrada = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA_ENTRADA));
                String horaSalida = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA_SALIDA));
                String horas = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORAS));

                Fichaje fichaje = new Fichaje(id, empleadoCodigo, fecha, horaEntrada, horaSalida, horas);

                // Agregar el objeto Fichaje a la lista
                listaFichajes.add(fichaje);
            } while (cursor.moveToNext());

            // Cerrar el cursor
            cursor.close();
        }

        // Cerrar la conexión con la base de datos
        db.close();

        // Retornar la lista de fichajes
        return listaFichajes;
    }



    public void actualizarFichaje(int idFichaje, String horaEntrada, String horaSalida, String horasTrabajadas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HORA_ENTRADA, horaEntrada);
        values.put(COLUMN_HORA_SALIDA, horaSalida);
        values.put(COLUMN_HORAS, horasTrabajadas);

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(idFichaje)};

        db.update(TABLE_FICHAJES, values, selection, selectionArgs);
        db.close();
    }



    public void eliminarFichajePorId(int idFichaje) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(idFichaje)};

        int result = db.delete(TABLE_FICHAJES, whereClause, whereArgs);

        if (result == 0) {
            Toast.makeText(context, "No se encontró ningún fichaje con el ID proporcionado", Toast.LENGTH_SHORT).show();
        } else if (result == 1) {
            Toast.makeText(context, "Fichaje eliminado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Se eliminaron múltiples fichajes", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public void guardarImagenEmpleado(String codigoEmpleado, byte[] imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGEN, imagen);

        // Definir la cláusula WHERE para actualizar la fila correspondiente al código de empleado
        String whereClause = COLUMN_CODIGO + " = ?";
        String[] whereArgs = {codigoEmpleado};

        // Actualizar la fila en la tabla de empleados con la nueva imagen
        db.update(TABLE_EMPLEADOS, values, whereClause, whereArgs);
        db.close();
    }

    public byte[] cargarImagenEmpleado(String codigoEmpleado) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] imagen = null;

        // Definir las columnas que se deben consultar
        String[] projection = {COLUMN_IMAGEN};

        // Definir la cláusula WHERE para obtener la fila correspondiente al código de empleado
        String selection = COLUMN_CODIGO + " = ?";
        String[] selectionArgs = {codigoEmpleado};

        // Realizar la consulta en la tabla de empleados
        Cursor cursor = db.query(TABLE_EMPLEADOS, projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Obtener la imagen en formato byte[]
            imagen = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN));
            cursor.close();
        }
        db.close();

        return imagen;
    }

    // Método para consultar el nombre y el apellido del empleado por código
    public String[] consultarNombreYApellidoPorCodigo(String codigo) {
        String[] nombreYApellido = new String[2]; // Array para almacenar nombre y apellido
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL
        String query = "SELECT " + COLUMN_NOMBRE + ", " + COLUMN_APELLIDO +
                " FROM " + TABLE_EMPLEADOS +
                " WHERE " + COLUMN_CODIGO + " = ?";

        // Ejecutar la consulta
        Cursor cursor = db.rawQuery(query, new String[]{codigo});

        // Verificar si se encontró un resultado
        if (cursor.moveToFirst()) {
            // Obtener el nombre y apellido del cursor
            nombreYApellido[0] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE));
            nombreYApellido[1] = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDO));
        }

        // Cerrar cursor y base de datos
        cursor.close();
        db.close();

        // Devolver el nombre y apellido
        return nombreYApellido;
    }



    public boolean isPrimeraVez(String correoEmpleado) {
        boolean isFirstTime = false;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_IS_PRIMERA_VEZ};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {correoEmpleado};

        Cursor cursor = db.query(TABLE_EMPLEADOS, projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int isFirstTimeInt = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_PRIMERA_VEZ));
            isFirstTime = (isFirstTimeInt == 1);
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return isFirstTime;
    }

    public boolean actualizarEmpleado(String codigoEmpleado, String nuevaContrasena, String nuevaContrasenaAleatoria) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, nuevaContrasena);
        values.put(COLUMN_IS_PRIMERA_VEZ, nuevaContrasenaAleatoria);

        // Actualizar el empleado con el código proporcionado
        int rowsAffected = db.update(TABLE_EMPLEADOS, values, COLUMN_CODIGO + " = ?", new String[]{codigoEmpleado});

        db.close();

        // Si se actualizó alguna fila, retornar true, de lo contrario, false
        return rowsAffected > 0;
    }

    public int obtenerSolicitudesPendientes(String codigoEmpleado) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            String[] projection = {COLUMN_SOLICITUD_ID};
            String selection = COLUMN_CODIGO_EMPLEADO + "=? AND " + COLUMN_ESTADO_SOLICITUD + "=?";
            String[] selectionArgs = {codigoEmpleado, "Pendiente"};
            Cursor cursor = db.query(
                    TABLE_SOLICITUDES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                count = cursor.getCount();
                cursor.close();
            }
            db.close();
        }
        return count;
    }

    public String calcularHorasTotalesTrabajadas(String codigoEmpleado) {
        double totalMinutos = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            String[] projection = {COLUMN_HORAS};
            String selection = COLUMN_EMPLEADO_CODIGO + "=?";
            String[] selectionArgs = {codigoEmpleado};
            Cursor cursor = db.query(
                    TABLE_FICHAJES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String horasString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORAS));
                    // Check if horasString is in the correct format
                    if (horasString.contains(":")) {
                        String[] partes = horasString.split(":");
                        // Ensure partes array contains at least two elements
                        if (partes.length >= 2) {
                            int horas = Integer.parseInt(partes[0]);
                            int minutos = Integer.parseInt(partes[1]);
                            // Convertir las horas a minutos y sumarlos
                            totalMinutos += horas * 60 + minutos;
                        } else {
                            // Handle incorrect format of horasString
                            // Log or throw an error, or handle it in another appropriate way
                            throw new IllegalArgumentException("Formato de horas incorrecto: " + horasString);
                        }
                    } else {
                        // Handle incorrect format of horasString
                        // Log or throw an error, or handle it in another appropriate way
                        throw new IllegalArgumentException("Formato de horas incorrecto: " + horasString);
                    }
                }
                cursor.close();
            }
            db.close();
        }
        // Convertir los minutos totales a horas y minutos
        int horasTotales = (int) (totalMinutos / 60);
        int minutosTotales = (int) (totalMinutos % 60);
        // Formatear el resultado como "00:00"
        return String.format("%02d:%02d", horasTotales, minutosTotales);
    }

}


