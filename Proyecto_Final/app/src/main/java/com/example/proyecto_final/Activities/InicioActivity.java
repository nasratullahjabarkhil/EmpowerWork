package com.example.proyecto_final.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.BBDD.EmpleadoDataSource;
import com.example.finalproyect.EmailSender;
import com.example.finalproyect.MainPages.MainPageAdmin;
import com.example.finalproyect.MainPages.MainPageEmpleado;
import com.example.finalproyect.Models.Empleados;
import com.example.finalproyect.Models.PasswordEmailValidator;
import com.example.finalproyect.R;

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


public class InicioActivity extends AppCompatActivity {
    Button btnLogin;
    EditText editTextUsuario, editTextContrasena;
    EmpleadoDataSource empleadoDataSource;
    Boolean passVisible = false;
    String ADMIN_NOMBRE = "nashjabarkhil@gmail.com", CONTRASENA = "Nash@1234";

    TextView olvidarPassword;

    int authenticationCode;
    private static final int MIN_CODIGO = 100000;
    private static final int MAX_CODIGO = 999999;

    Empleados empleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inicio);
        empleadoDataSource = new EmpleadoDataSource(this);
        empleadoDataSource.open();

        btnLogin = findViewById(R.id.login);
        editTextUsuario = findViewById(R.id.usuario);
        editTextContrasena = findViewById(R.id.password);
        olvidarPassword = findViewById(R.id.olvidarPassword);

        guardarEstado();
        olvidarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarEmpleado();
            }
        });

        // Agrega un TextWatcher para validar el formato del correo electrónico mientras se escribe
        editTextUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String email = editable.toString();
                if (PasswordEmailValidator.isEmailValid(email)) {
                    editTextUsuario.setTextColor(Color.BLACK);
                } else {
                    editTextUsuario.setTextColor(Color.RED);
                }
            }
        });

        // En el método onClick del botón de inicio de sesión en la clase InicioActivity
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el usuario y la contraseña ingresados
                String usuario = editTextUsuario.getText().toString().toLowerCase();
                String contrasena = editTextContrasena.getText().toString();



                // Validar si el correo y/o la contraseña están vacíos
                if (usuario.isEmpty() && contrasena.isEmpty()) {
                    showToast("Por favor ingresa tu correo electrónico y contraseña.");
                    return;
                } else if (usuario.isEmpty()) {
                    showToast("Por favor ingresa tu correo electrónico.");
                    return;
                } else if (contrasena.isEmpty()) {
                    showToast("Por favor ingresa tu contraseña.");
                    return;
                }

                // Validar el formato del correo electrónico y la contraseña
                if (!PasswordEmailValidator.isEmailValid(usuario)) {
                    showToast("La dirección de correo electrónico no es válida.");
                    return;
                }

                if (!PasswordEmailValidator.isPasswordValid(contrasena)) {
                    showToast("La contraseña no cumple con los requisitos de seguridad.");
                    return;
                }

                // Verificar si el empleado existe y autenticar las credenciales
                 empleado = empleadoDataSource.login(usuario, contrasena);



                if (empleado != null) {
                    // Verificar si es la primera vez que el empleado inicia sesión
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());

                    boolean isFirstTime = db.isPrimeraVez(usuario);

                    if (isFirstTime) {
                        // Redirigir a la actividad para cambiar la contraseña
                        mostrarDialogoCambiarContrasena(empleado);
                    } else {

                        authenticationCode = generarCodigo();
                        sendAuthemticationCode(usuario, authenticationCode);
                        mostrarDialogoCodigo();

                    }
                } else {
                    // Si el empleado no existe, verifica si es un administrador
                    if (usuario.equals(ADMIN_NOMBRE) && contrasena.equals(CONTRASENA)) {
                        // Redirige a la actividad principal del administrador
                        Intent intent = new Intent(InicioActivity.this, MainPageAdmin.class);
                        startActivity(intent);
                    } else {
                        showToast("Error. Verifica tu correo electrónico y contraseña.");
                    }
                }
            }
        });



        // Maneja el evento de tocar en el ícono de visibilidad de contraseña
        editTextContrasena.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX() >= editTextContrasena.getRight()-editTextContrasena.getCompoundDrawables()[Right].getBounds().width()){
                        int selecionado = editTextContrasena.getSelectionEnd();
                        if(passVisible){
                            editTextContrasena.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_visibility_off_black_24, 0);
                            editTextContrasena.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible = false;
                        }else{
                            editTextContrasena.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.eye_24, 0);
                            editTextContrasena.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible = true;
                        }
                        editTextContrasena.setSelection(selecionado);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    // Método para mostrar un Toast
    private void showToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        empleadoDataSource.close();
    }

    private void buscarEmpleado() {
        // Inflar el diseño personalizado para el AlertDialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialogo_olvidar_contrasena, null);
        EditText codigoEditText = dialogView.findViewById(R.id.codigoEmpleado);
        EditText correoEditText = dialogView.findViewById(R.id.correoEmpleado);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Recuperar Contraseña")
                .setMessage("Introduce tus datos para recuperar tu contraseña")
                .setPositiveButton("Enviar", null) // Set null para manejar manualmente el evento onClick
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Sobrescribe el onClickListener del botón positivo después de mostrar el diálogo
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigoEmpleado = codigoEditText.getText().toString().trim();
                String correoEmpleado = correoEditText.getText().toString().trim();
                if (codigoEmpleado.isEmpty() || correoEmpleado.isEmpty()) {
                    showToast("Por favor, complete todos los campos.");
                    return;
                }
                // Verificar si el código del empleado tiene la longitud adecuada
                if (codigoEmpleado.length() != 6) {
                    showToast("Longitud del código de empleado incorrecta.");
                    return;
                }
                // Verificar si el correo electrónico tiene un formato válido
                if (!Patterns.EMAIL_ADDRESS.matcher(correoEmpleado).matches()) {
                    showToast("Formato de correo electrónico inválido.");
                    return;
                }

                 empleado = empleadoDataSource.buscarEmpleadoPorCodigoYCorreo(codigoEmpleado, correoEmpleado);

                if (empleado == null) {
                    showToast("El empleado no existe");
                } else {
                    enviarCorreoElectronico(empleado.getNombre(), empleado.getEmail(), empleado.getPassword());
                    // Cerrar el diálogo después de enviar el correo electrónico
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void enviarCorreoElectronico(String nombre, String email, String password) {
        // Propiedades para la conexión SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", EmailSender.Gmail_Host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Crear una sesión SMTP
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Credenciales de autenticación del remitente
                return new PasswordAuthentication(EmailSender.Sender_Email_Address, EmailSender.Sender_Email_Password);
            }
        });

        // Crear un mensaje Mime
        MimeMessage message = new MimeMessage(session);
        try {
            // Establecer el destinatario, el asunto y el contenido del mensaje
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Recuperar contraseña EmpowerWork");
            String mensaje = "Hola " + nombre + ",\n\n"
                    + "Te enviamos tu usuario y la contraseña por recibir una solicitud\n\n"
                    + "Nombre: " + nombre + "\n"
                    + "Correo electrónico: " + email + "\n"
                    + "Contraseña: " + password + "\n\n"
                    + "¡Gracias!";
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
            // Mostrar mensaje de éxito
            showToast("Correo electrónico enviado correctamente. Revise su bandeja de entrada.");
        } catch (MessagingException e) {
            e.printStackTrace();
            showToast("Error al enviar el correo electrónico.");
        }
    }


    private static int generarCodigo() {
        Random random = new Random();
        return random.nextInt(MAX_CODIGO - MIN_CODIGO + 1) + MIN_CODIGO;
    }
    private void sendAuthemticationCode(String email, int codigo) {
        // Propiedades para la conexión SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", EmailSender.Gmail_Host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Crear una sesión SMTP
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Credenciales de autenticación del remitente
                return new PasswordAuthentication(EmailSender.Sender_Email_Address, EmailSender.Sender_Email_Password);
            }
        });

        // Crear un mensaje Mime
        MimeMessage message = new MimeMessage(session);
        try {
            // Establecer el destinatario, el asunto y el contenido del mensaje
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Código de verificación EmpowerWork");
            String mensaje = "Hola,\n\n"
                    + "Aquí tienes tu código de verificación:\n\n"
                    + "Código: " + codigo + "\n\n"
                    + "¡Gracias!";
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
            // Mostrar mensaje de éxito
            showToast("Código enviado correctamente. Revise su bandeja de entrada.");
        } catch (MessagingException e) {
            e.printStackTrace();
            showToast("Error al enviar el código.");
        }
    }


    private void mostrarDialogoCodigo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialogo_codigo_autenticacion, null);
        EditText codigo = view.findViewById(R.id.editTextCodigo);


        builder.setView(view)
                .setTitle("Codigo de autenticación")
                .setPositiveButton("Aceptar", null) // Dejamos el botón positivo como null inicialmente
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoIngresado = codigo.getText().toString().trim();
                if (codigoIngresado.equals(String.valueOf(authenticationCode))) {
                    // Código correcto
                    showToast("Código correcto. Acceso concedido.");
                    // Redirige a la actividad principal del empleado
                    Intent intent = new Intent(InicioActivity.this, MainPageEmpleado.class);
                    intent.putExtra("CODIGO_EMPLEADO", empleado.getCodigo());
                    intent.putExtra("NOMBRE_EMPLEADO", empleado.getNombre());
                    intent.putExtra("APELLIDO_EMPLEADO", empleado.getApellido());
                    startActivity(intent);
                    alertDialog.dismiss();
                } else {
                    // Código incorrecto
                    showToast("Código incorrecto. Por favor, intenta nuevamente.");
                    // No cierres el diálogo aquí para permitir que el usuario intente nuevamente
                }
            }
        });

        // Configurar el botón de "Cancelar"
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

    }


    private void mostrarDialogoCambiarContrasena(Empleados empleado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_cambiar_contrasena, null);
        EditText nuevaContrasenaEditText = view.findViewById(R.id.nuevaContrasena);
        EditText confirmarContrasenaEditText = view.findViewById(R.id.confirmarContrasena);

        builder.setView(view)
                .setTitle("Cambiar Contraseña")
                .setPositiveButton("Guardar", null) // Dejamos el botón positivo como null inicialmente
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        // Obtener una referencia al botón "Guardar"
        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevaContrasena = nuevaContrasenaEditText.getText().toString().trim();
                String confirmarContrasena = confirmarContrasenaEditText.getText().toString().trim();

                // Validar la nueva contraseña
                if (!PasswordEmailValidator.isPasswordValid(nuevaContrasena)) {
                    showToast("La nueva contraseña no cumple con los requisitos de seguridad.");
                    return;
                }

                // Validar que la nueva contraseña tenga al menos 8 caracteres
                if (nuevaContrasena.length() < 8) {
                    showToast("La nueva contraseña debe tener al menos 8 caracteres.");
                    return;
                }
                // Validar que las contraseñas coincidan
                if (!nuevaContrasena.equals(confirmarContrasena)) {
                    showToast("Las contraseñas no coinciden.");
                    return;
                }

                // Actualizar la contraseña del empleado en la base de datos
                empleado.setPassword(nuevaContrasena);
                empleado.setContrasenaAleatoria("0"); // Establecer la contraseña aleatoria en 0

                // Obtener una instancia de EmpleadoDataSource
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());

                // Actualizar el empleado en la base de datos
                if (db.actualizarEmpleado(empleado.getCodigo(), nuevaContrasena, "0")) {
                    showToast("Contraseña cambiada exitosamente.");
                    alertDialog.dismiss(); // Cerrar el diálogo solo si la operación es exitosa
                } else {
                    showToast("Error al cambiar la contraseña.");
                }
            }
        });
    }

    public void guardarEstado(){
        SharedPreferences perefrences = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        boolean estado = true;
        SharedPreferences.Editor editor = perefrences.edit();
        editor.putBoolean("estado_usuario", estado);
        editor.commit();
    }


}
