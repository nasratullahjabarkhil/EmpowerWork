package com.example.proyecto_final.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.EmailSender;
import com.example.finalproyect.Models.PasswordEmailValidator;
import com.example.finalproyect.R;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;




public class FragmentoAlta extends Fragment {



    DatabaseHelper dbHelper;
    EditText editTextNombre, editTextApellido, editTextPuesto, editTextEmail;

    Button btnaAlta;



    public FragmentoAlta() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmento_alta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());

        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextApellido = view.findViewById(R.id.editTextApellido);
        editTextPuesto = view.findViewById(R.id.editTextPuesto);
        editTextEmail = view.findViewById(R.id.editTextEmail);


        btnaAlta = view.findViewById(R.id.buttonAltaEmpleado);
        btnaAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //editTextEmail.setTextColor(Color.BLACK);
                // Obtén los datos de los EditTexts
                String nombre = editTextNombre.getText().toString();
                String apellido = editTextApellido.getText().toString();
                String puesto = editTextPuesto.getText().toString();
                String email = editTextEmail.getText().toString().toLowerCase();

                // Validar que ninguno de los campos esté vacío
                if (nombre.isEmpty() || apellido.isEmpty() || puesto.isEmpty() || email.isEmpty()) {
                    showToast("Por favor completa todos los campos.", "");
                    return;
                }


                // Validar la dirección de correo electrónico
                if (!PasswordEmailValidator.isEmailValid(email)) {
                    showToast("La dirección de correo electrónico no es válida.", "");

                    return;
                }


                // Genera un código aleatorio
                String codigoEmpleado = generarCodigo();

                // Verifica si el código ya existe en la base de datos
                if (codigoExistente(codigoEmpleado)) {
                    showToast("Código ya existe. Generando uno nuevo.", "");
                    codigoEmpleado = generarCodigo(); // Genera un nuevo código
                }

                String contrasena = generarContrasena(8);
                Log.d("Contraseña Generada", contrasena);
                byte[] iconoBytes = convertirIconoABytes(R.drawable.empleado);


                // Inserta los datos en la base de datos junto con el código aleatorio
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_CODIGO, codigoEmpleado);
                values.put(DatabaseHelper.COLUMN_NOMBRE, nombre);
                values.put(DatabaseHelper.COLUMN_APELLIDO, apellido);
                values.put(DatabaseHelper.COLUMN_PUESTO, puesto);
                values.put(DatabaseHelper.COLUMN_EMAIL, email);
                values.put(DatabaseHelper.COLUMN_PASSWORD, contrasena);
                values.put(DatabaseHelper.COLUMN_IMAGEN, iconoBytes);
                values.put(DatabaseHelper.COLUMN_IS_PRIMERA_VEZ, 1);// Aquí se agrega la ruta de la imagen


                long newRowId = db.insert(DatabaseHelper.TABLE_EMPLEADOS, null, values);


                if (newRowId != -1) {
                    // Registro exitoso, muestra un mensaje de éxito con el código del empleado
                    showToast("Empleado registrado correctamente con el código: ", codigoEmpleado);

                    // Envía un correo electrónico al empleado con sus datos
                    try {
                        send_email(nombre, apellido, puesto, email, codigoEmpleado, contrasena);
                        editTextNombre.setText("");
                        editTextApellido.setText("");
                        editTextEmail.setText("");
                        editTextPuesto.setText("");
                        Toast.makeText(getActivity(), "Datos enviado al correo indicado", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Error en enviar correo", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }

                } else {
                    // Registro fallido, muestra un mensaje de error
                    showToast("Error: El correo ya existe", "");
                }
            }
        });

        // Agregar un TextWatcher para detectar cambios en el texto del correo electrónico
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // No se necesita implementación
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // No se necesita implementación
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Validar la dirección de correo electrónico y actualizar el color del texto
                String email = editable.toString();
                if (PasswordEmailValidator.isEmailValid(email)) {
                    editTextEmail.setTextColor(Color.BLACK); // Cambiar a negro si es válido
                } else {
                    editTextEmail.setTextColor(Color.RED); // Cambiar a rojo si no es válido
                }
            }
        });


    }
    private void showToast(String message, String codigoEmpleado) {
        Toast.makeText(getActivity().getApplicationContext(), message  + codigoEmpleado, Toast.LENGTH_LONG).show();
    }

    private String generarCodigo() {
        // Longitud del código deseado
        int longitudCodigo = 6;

        // Caracteres posibles en el código
        String caracteres = "0123456789";

        // Crear un objeto Random
        Random random = new Random();

        // Crear una cadena para almacenar el código generado
        StringBuilder codigoGenerado = new StringBuilder();

        // Generar el código aleatorio
        for (int i = 0; i < longitudCodigo; i++) {
            int indice = random.nextInt(caracteres.length());
            codigoGenerado.append(caracteres.charAt(indice));
        }

        // Devolver el código generado
        return codigoGenerado.toString();
    }

    private boolean codigoExistente(String codigo) {
        // Realiza una consulta en la base de datos para verificar si el código ya existe
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DatabaseHelper.COLUMN_CODIGO};
        String selection = DatabaseHelper.COLUMN_CODIGO + " = ?";
        String[] selectionArgs = {codigo};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_EMPLEADOS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean existe = cursor.getCount() > 0;

        // Cierra el cursor después de usarlo
        cursor.close();

        return existe;
    }




    public static String generarContrasena(int longitud) {
        SecureRandom random = new SecureRandom();
        StringBuilder contrasenaGenerada = new StringBuilder();

        // Caracteres posibles en la contraseña
        String letrasMayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String letrasMinusculas = "abcdefghijklmnopqrstuvwxyz";
        String numeros = "0123456789";
        String caracteresEspeciales = "!@#$%&*+";

        // Añadir al menos un carácter de cada tipo
        contrasenaGenerada.append(letrasMayusculas.charAt(random.nextInt(letrasMayusculas.length())));
        contrasenaGenerada.append(letrasMinusculas.charAt(random.nextInt(letrasMinusculas.length())));
        contrasenaGenerada.append(numeros.charAt(random.nextInt(numeros.length())));
        contrasenaGenerada.append(caracteresEspeciales.charAt(random.nextInt(caracteresEspeciales.length())));

        // Generar el resto de la contraseña
        for (int i = 4; i < longitud; i++) {
            String conjuntoCaracteres = letrasMayusculas + letrasMinusculas + numeros + caracteresEspeciales;
            int indice = random.nextInt(conjuntoCaracteres.length());
            contrasenaGenerada.append(conjuntoCaracteres.charAt(indice));
        }

        return contrasenaGenerada.toString();
    }

    private void send_email(String nombre, String apellido, String puesto, String email, String codigoEmpleado, String password) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", EmailSender.Gmail_Host);
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailSender.Sender_Email_Address, EmailSender.Sender_Email_Password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Bienvenido a EmpowerWork");
            String mensaje = "Hola " + nombre + " " + apellido + ",\n\n"
                    + "Te damos la bienvenida a nuestra empresa. Aquí están tus detalles:\n\n"
                    + "Código de empleado: " + codigoEmpleado + "\n"
                    + "Nombre: " + nombre + "\n"
                    + "Apellido: " + apellido + "\n"
                    + "Puesto: " + puesto + "\n"
                    + "Correo electrónico: " + email + "\n"
                    + "Contraseña: " + password + "\n\n"
                    + "¡Gracias por unirte a nosotros!";
            message.setText(mensaje);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public byte[] convertirIconoABytes(int idDrawable) {
        try {
            Drawable iconoDrawable = getResources().getDrawable(idDrawable);  // Usa getDrawable para recursos drawable
            Bitmap iconoBitmap = ((BitmapDrawable) iconoDrawable).getBitmap();

            // Redimensiona si es necesario (mantiene el código de redimensionamiento igual)
            int maxWidth = 100;
            int maxHeight = 100;
            float scale = Math.min(((float) maxWidth / iconoBitmap.getWidth()), ((float) maxHeight / iconoBitmap.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            Bitmap resizedBitmap = Bitmap.createBitmap(iconoBitmap, 0, 0, iconoBitmap.getWidth(), iconoBitmap.getHeight(), matrix, true);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] iconoBytes = outputStream.toByteArray();

            return iconoBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}