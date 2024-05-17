package com.example.proyecto_final.Models;
import android.os.Parcel;
import android.os.Parcelable;

public class Fichaje implements Parcelable {
    private int id;
    private String empleadoCodigo;
    private String fecha;
    private String horaEntrada;
    private String horaSalida;
    private String horas;

    // Constructor
    public Fichaje(int id, String empleadoCodigo, String fecha, String horaEntrada, String horaSalida, String horas) {
        this.id = id;
        this.empleadoCodigo = empleadoCodigo;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.horas = horas;
    }

    public Fichaje() {

    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpleadoCodigo() {
        return empleadoCodigo;
    }

    public void setEmpleadoCodigo(String empleadoCodigo) {
        this.empleadoCodigo = empleadoCodigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(empleadoCodigo);
        dest.writeString(fecha);
        dest.writeString(horaEntrada);
        dest.writeString(horaSalida);
        dest.writeString(horas);
    }

    public static final Creator<Fichaje> CREATOR = new Creator<Fichaje>() {
        @Override
        public Fichaje createFromParcel(Parcel source) {
            Fichaje fichaje = new Fichaje();
            fichaje.id = source.readInt();
            fichaje.empleadoCodigo = source.readString();
            fichaje.fecha = source.readString();
            fichaje.horaEntrada = source.readString();
            fichaje.horaSalida = source.readString();
            fichaje.horas = source.readString();
            return fichaje;
        }

        @Override
        public Fichaje[] newArray(int size) {
            return new Fichaje[size];
        }
    };
}
