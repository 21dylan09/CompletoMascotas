package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(
        tableName = "restriccion",
        foreignKeys = @ForeignKey(
                entity = Mascota.class,
                parentColumns = "id_mascota",
                childColumns = "id_mascota",
                onDelete = ForeignKey.CASCADE // Elimina las restricciones si la mascota es eliminada
        )
)
public class Restriccion {
    @PrimaryKey(autoGenerate = true)
    public int id_restriccion;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "id_mascota", index = true)
    public int idMascota;

    // Constructor
    public Restriccion(String descripcion, int idMascota) {
        this.descripcion = descripcion;
        this.idMascota = idMascota;
    }

    // Getters y Setters
    public int getId() {
        return id_restriccion;
    }

    public void setId(int id) {
        this.id_restriccion = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }
}