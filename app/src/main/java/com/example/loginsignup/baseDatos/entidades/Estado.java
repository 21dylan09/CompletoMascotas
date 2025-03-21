package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "estado")
public class Estado {
    @PrimaryKey(autoGenerate = true)
    public int id_estado;
    public String tipo_estado;

    public Estado(int id_estado, String tipo_estado) {
        this.id_estado = id_estado;
        this.tipo_estado = tipo_estado;
    }
}