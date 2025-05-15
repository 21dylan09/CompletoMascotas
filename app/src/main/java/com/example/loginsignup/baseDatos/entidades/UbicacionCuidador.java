package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ubicacion_cuidador")
public class UbicacionCuidador {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String direccionTexto;
    public double latitud;
    public double longitud;
    public String telefono;  // NUEVO

    public UbicacionCuidador(String direccionTexto, double latitud, double longitud, String telefono) {
        this.direccionTexto = direccionTexto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.telefono = telefono;
    }
}

