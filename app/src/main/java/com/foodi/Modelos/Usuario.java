package com.foodi.Modelos;

public class Usuario {
    private String nombres, apellidos, telefono, latitudHogar, longitudHogar,latitudActual, longitudActual, tipo, idUsuario;

    public Usuario(String nombres, String apellidos, String telefono, String latitudHogar, String longitudHogar, String latitudActual, String longitudActual, String tipo, String idUsuario) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.latitudHogar = latitudHogar;
        this.longitudHogar = longitudHogar;
        this.latitudActual = latitudActual;
        this.longitudActual = longitudActual;
        this.tipo = tipo;
        this.idUsuario = idUsuario;
    }

    public Usuario(String idUsuario, String latitudActual, String longitudActual,String latitudHogar, String longitudHogar) {
        this.latitudHogar = latitudHogar;
        this.longitudHogar = longitudHogar;
        this.latitudActual = latitudActual;
        this.longitudActual = longitudActual;
        this.idUsuario = idUsuario;
    }

    public String getLatitudHogar() {
        return latitudHogar;
    }

    public void setLatitudHogar(String latitudHogar) {
        this.latitudHogar = latitudHogar;
    }

    public String getLongitudHogar() {
        return longitudHogar;
    }

    public void setLongitudHogar(String longitudHogar) {
        this.longitudHogar = longitudHogar;
    }

    public String getLatitudActual() {
        return latitudActual;
    }

    public void setLatitudActual(String latitudActual) {
        this.latitudActual = latitudActual;
    }

    public String getLongitudActual() {
        return longitudActual;
    }

    public void setLongitudActual(String longitudActual) {
        this.longitudActual = longitudActual;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
