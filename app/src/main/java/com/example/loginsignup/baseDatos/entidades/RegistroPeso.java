package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(
        tableName = "RegistroPeso",
        foreignKeys = @ForeignKey(
                entity = Mascota.class,
                parentColumns = "id_mascota",
                childColumns = "id_mascota",
                onDelete = ForeignKey.CASCADE
        )
)
public class RegistroPeso {
    @PrimaryKey(autoGenerate = true)
    public int id_peso;

    @ColumnInfo(name = "fecha")
    public String fecha;

    @ColumnInfo(name = "peso")
    public double peso;

    @ColumnInfo(name = "id_mascota", index = true)
    public int idMascota;

    public RegistroPeso(String fecha, double peso, int idMascota) {
        this.fecha = fecha;
        this.peso = peso;
        this.idMascota = idMascota;
    }

    public int getId_peso() {
        return id_peso;
    }

    public void setId_peso(int id_peso) {
        this.id_peso = id_peso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }
}
