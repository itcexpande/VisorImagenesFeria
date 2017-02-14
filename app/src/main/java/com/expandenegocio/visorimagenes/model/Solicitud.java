package com.expandenegocio.visorimagenes.model;

import java.io.Serializable;

/**
 * Created by Penlopjo on 01/04/2016.
 */
public class Solicitud implements Serializable {

    private String id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private Integer provincia;
    private String franq_principal;
    private String franq_secundaria;
    private String fecha;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getProvincia() {
        return provincia;
    }

    public void setProvincia(Integer provincia) {
        this.provincia = provincia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFranq_principal() {
        return franq_principal;
    }

    public void setFranq_principal(String franq_principal) {
        this.franq_principal = franq_principal;
    }

    public String getFranq_secundaria() {
        return franq_secundaria;
    }

    public void setFranq_secundaria(String franq_secundaria) {
        this.franq_secundaria = franq_secundaria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
