package com.example.proyecto_final.Models;
import android.os.Parcel;
import android.os.Parcelable;

public class Empleados implements Parcelable {
    String nombre;
    String apellido;
    String puesto;
    String email;
    String password;
    String codigo;
    String rutaImagen; // Atributo para la ruta de la imagen
    String contrasenaAleatoria;


    public Empleados() {

    }


    // Constructor con parámetros
    public Empleados(String nombre, String apellido, String puesto, String email, String password, String codigo, String rutaImagen) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.puesto = puesto;
        this.email = email;
        this.password = password;
        this.codigo = codigo;
        this.rutaImagen = rutaImagen; // Asigna la ruta de la imagen
        this.contrasenaAleatoria = contrasenaAleatoria;

    }

    public String getContrasenaAleatoria() {
        return contrasenaAleatoria;
    }

    public void setContrasenaAleatoria(String contrasenaAleatoria) {
        this.contrasenaAleatoria = contrasenaAleatoria;
    }
    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(puesto);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(codigo);
        dest.writeString(rutaImagen);
        dest.writeString(contrasenaAleatoria);
    }

    public static final Creator<Empleados> CREATOR = new Creator<Empleados>() {
        @Override
        public Empleados createFromParcel(Parcel source) {
            Empleados empleado = new Empleados();
            empleado.nombre = source.readString();
            empleado.apellido = source.readString();
            empleado.puesto = source.readString();
            empleado.email = source.readString();
            empleado.password = source.readString();
            empleado.codigo = source.readString();
            empleado.rutaImagen = source.readString();
            empleado.contrasenaAleatoria = source.readString();
            return empleado;
        }

        @Override
        public Empleados[] newArray(int size) {
            return new Empleados[size];
        }
    };
}
