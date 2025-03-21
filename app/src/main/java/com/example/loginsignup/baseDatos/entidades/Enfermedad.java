package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "enfermedad")
public class Enfermedad {
    @PrimaryKey(autoGenerate = true)
    public int id_enfermedad;

    public String nombreEnfermedad;

    public Enfermedad(String nombreEnfermedad) {
        this.nombreEnfermedad = nombreEnfermedad;
    }
}
