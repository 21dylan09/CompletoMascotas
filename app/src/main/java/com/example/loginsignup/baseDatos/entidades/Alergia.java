package com.example.loginsignup.baseDatos.entidades;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(
        tableName = "alergia",
        foreignKeys = @ForeignKey(
                entity = Mascota.class,
                parentColumns = "id_mascota",
                childColumns = "id_mascota",
                onDelete = ForeignKey.CASCADE
        )
)
public class Alergia {
    @PrimaryKey(autoGenerate = true)
    public int id_alergia;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "id_mascota", index = true)
    public int idMascota;

    // Constructor
    public Alergia(String descripcion, int idMascota) {
        this.descripcion = descripcion;
        this.idMascota = idMascota;
    }

    // Getters y Setters
    public int getId() {
        return id_alergia;
    }

    public void setId(int id) {
        this.id_alergia = id;
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

