package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ContactosConfianza")
public class Contactos {

    @PrimaryKey(autoGenerate = true)
    public int id_contacto;
    public String nombre;
    public String correo;
    public String telefono;

    public Contactos(int id_contacto, String nombre, String correo, String telefono) {
        this.id_contacto = id_contacto;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    public int getId_contacto() {
        return id_contacto;
    }

    public void setId_contacto(int id_contacto) {
        this.id_contacto = id_contacto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
