package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id_usuario;
    public String nombre;
    public String apellido;
    public String correo;
    public String contraseña;
    public String telefono;
    public String tipo_usuario;
    public long fecha_registro;

    public Usuario(String nombre, String apellido, String correo, String contraseña, String telefono, String tipo_usuario, long fecha_registro) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.tipo_usuario = tipo_usuario;
        this.fecha_registro = fecha_registro;
    }
}
