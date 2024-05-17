package com.example.proyecto_final.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.finalproyect.BBDD.DatabaseHelper;
import com.example.finalproyect.MainPages.MainPageEmpleado;
import com.example.finalproyect.Models.Empleados;
import com.example.finalproyect.Models.PasswordEmailValidator;
import com.example.finalproyect.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FragmentoMiPerfil extends Fragment {
    private EditText editTextNombre, editTextApellido, editTextPuesto, editTextEmail, editTextPassword, repetirPassword;
    private Button buttonGuardarCambios;
    private ImageView imageViewPerfil;

    private String codigoEmpleado;

    Boolean passVisible = false;
    private static final int SELECT_IMAGE = 1;

    public FragmentoMiPerfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mi_perfil, container, false);

        // Obtener referencias de los elementos de la interfaz
        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextApellido = view.findViewById(R.id.editTextApellido);
        editTextPuesto = view.findViewById(R.id.editTextPuesto);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        repetirPassword = view.findViewById(R.id.editTextPasswordRepeat);
        buttonGuardarCambios = view.findViewById(R.id.buttonGuardarCambios);
        imageViewPerfil = view.findViewById(R.id.imageViewPerfil);
        cargarImagenEmpleado();

        // Obtener la referencia del botón de selección de imagen
        Button buttonSeleccionarImagen = view.findViewById(R.id.buttonSeleccionarImagen);

// Configurar un OnClickListener para el botón
        buttonSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                seleccionarImagen(v);
            }
        });

        // Obtener el ID del empleado de los argumentos del fragmento
        MainPageEmpleado mainPageEmpleado = (MainPageEmpleado) getActivity();
        codigoEmpleado = mainPageEmpleado.getCodigoEmpleado();

        if (codigoEmpleado != null) {
            cargarDatosEmpleado(codigoEmpleado);
        }

        // Maneja el evento de tocar en el ícono de visibilidad de contraseña
        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX() >= editTextPassword.getRight()-editTextPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selecionado = editTextPassword.getSelectionEnd();
                        if(passVisible){
                            editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_visibility_off_black_24, 0);
                            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible = false;
                        }else{
                            editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.eye_24, 0);
                            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible = true;
                        }
                        editTextPassword.setSelection(selecionado);
                        return true;
                    }
                }
                return false;
            }
        });

        // Maneja el evento de tocar en el ícono de visibilidad de contraseña
        repetirPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(event.getRawX() >= repetirPassword.getRight()-repetirPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selecionado = repetirPassword.getSelectionEnd();
                        if(passVisible){
                            repetirPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.baseline_visibility_off_black_24, 0);
                            repetirPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible = false;
                        }else{
                            repetirPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.eye_24, 0);
                            repetirPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible = true;
                        }
                        repetirPassword.setSelection(selecionado);
                        return true;
                    }
                }
                return false;
            }
        });

        // Asignar un listener al botón de guardar cambios
        buttonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codigoEmpleado != null) {
                    guardarCambiosEmpleado();
                    MainPageEmpleado mainPageEmpleado = (MainPageEmpleado) getActivity();
                    mainPageEmpleado.cargarImagenEmpleado();

                } else {
                    Toast.makeText(getContext(), "Error en guardar cambios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // Método para cargar los datos del empleado en los campos de texto
    private void cargarDatosEmpleado(String empleadoCodigo) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Empleados empleado = dbHelper.obtenerEmpleadoPorCodigo(empleadoCodigo);
        if (empleado != null) {
            editTextNombre.setText(empleado.getNombre());
            editTextApellido.setText(empleado.getApellido());
            editTextPuesto.setText(empleado.getPuesto());
            editTextEmail.setText(empleado.getEmail());
            editTextPassword.setText(empleado.getPassword());
            repetirPassword.setText(empleado.getPassword());
            // Mostrar la imagen del empleado si está disponible
            if (empleado.getRutaImagen() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(empleado.getRutaImagen().getBytes(), 0, empleado.getRutaImagen().length());
                imageViewPerfil.setImageBitmap(bitmap);
            }
        }
    }
    private void cargarImagenEmpleado() {
        // Obtener el código del empleado
        // Obtener el ID del empleado de los argumentos del fragmento
        MainPageEmpleado mainPageEmpleado = (MainPageEmpleado) getActivity();
       String  codigoEmpleado1 = mainPageEmpleado.getCodigoEmpleado();

        // Crear una instancia de DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

        // Cargar la imagen del empleado desde la base de datos
        byte[] imagenEmpleado = databaseHelper.cargarImagenEmpleado(codigoEmpleado1);

        // Verificar si se obtuvo una imagen válida
        if (imagenEmpleado != null) {
            // Convertir el byte[] en un Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagenEmpleado, 0, imagenEmpleado.length);

            // Establecer el Bitmap en el ImageView
            imageViewPerfil.setImageBitmap(bitmap);
        } else {
            // Si no hay imagen, puedes establecer una imagen de perfil predeterminada
            imageViewPerfil.setImageResource(R.drawable.empleado1);
        }
    }
    private void guardarCambiosEmpleado() {
        String nombre = editTextNombre.getText().toString();
        String apellido = editTextApellido.getText().toString();
        String puesto = editTextPuesto.getText().toString();
        String email = editTextEmail.getText().toString().toLowerCase();
        String password = editTextPassword.getText().toString();
        String repeatPassword = repetirPassword.getText().toString();

        // Validación de la contraseña
        if (!PasswordEmailValidator.isPasswordValid(password)) {
            Toast.makeText(getContext(), "La contraseña no cumple con los requisitos de seguridad.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de la repetición de la contraseña
        if (!password.equals(repeatPassword)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la imagen del ImageView y convertirla en un arreglo de bytes
        byte[] imagen = convertirImagenABytes(imageViewPerfil);

        // Actualizar los datos del empleado en la base de datos
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        dbHelper.actualizarEmpleadoPorCodigo(codigoEmpleado, nombre, apellido, puesto, email, password, imagen);
        Toast.makeText(getContext(), "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
    }

    private byte[] convertirImagenABytes(ImageView imageView) {
        if (imageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            // Redimensionar la imagen a un tamaño específico (por ejemplo, 512x512)
            int maxWidth = 512;
            int maxHeight = 512;
            float scale = Math.min(((float) maxWidth / bitmap.getWidth()), ((float) maxHeight / bitmap.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // Convertir la imagen redimensionada en un arreglo de bytes
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        } else {
            // Manejar el caso en que no hay ningún Drawable establecido en el ImageView
            Toast.makeText(getContext(), "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
            return null;
        }
    }



    public void seleccionarImagen(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageViewPerfil.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
