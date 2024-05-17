package com.example.proyecto_final.Models;
import android.os.Parcel;
import android.os.Parcelable;

public class Solicitud implements Parcelable {
    private String id;
    private String codigoEmpleado;
    private String fechaInicio;
    private String fechaFin;
    private String observaciones;
    private String tipoSolicitud;
    private String dias;
    private String estadoSolicitud;

    // Constructor vacío
    public Solicitud() {
    }

    // Constructor con parámetros
    public Solicitud(String id, String codigoEmpleado, String fechaInicio, String fechaFin, String observaciones, String tipoSolicitud, String dias, String estadoSolicitud) {
        this.id = id;
        this.codigoEmpleado = codigoEmpleado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.observaciones = observaciones;
        this.tipoSolicitud = tipoSolicitud;
        this.dias = dias;
        this.estadoSolicitud = estadoSolicitud;
    }

    // Constructor
    public Solicitud(String fechaInicio, String fechaFin, String tipoSolicitud, String observaciones, String dias, String estadoSolicitud) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoSolicitud = tipoSolicitud;
        this.observaciones = observaciones;
        this.dias = dias;
        this.estadoSolicitud = estadoSolicitud;
    }

    // Métodos getter y setter para cada atributo
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(codigoEmpleado);
        dest.writeString(fechaInicio);
        dest.writeString(fechaFin);
        dest.writeString(observaciones);
        dest.writeString(tipoSolicitud);
        dest.writeString(dias);
        dest.writeString(estadoSolicitud);
    }

    public static final Creator<Solicitud> CREATOR = new Creator<Solicitud>() {
        @Override
        public Solicitud createFromParcel(Parcel source) {
            Solicitud solicitud = new Solicitud();
            solicitud.codigoEmpleado = source.readString();
            solicitud.fechaInicio = source.readString();
            solicitud.fechaFin = source.readString();
            solicitud.observaciones = source.readString();
            solicitud.tipoSolicitud = source.readString();
            solicitud.dias = source.readString();
            solicitud.estadoSolicitud = source.readString();
            return solicitud;
        }

        @Override
        public Solicitud[] newArray(int size) {
            return new Solicitud[size];
        }
    };

}
