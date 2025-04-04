package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(tableName = "notas_mascota",
        foreignKeys = @ForeignKey(entity = Mascota.class,
                parentColumns = "id_mascota",
                childColumns = "id_mascota",
                onDelete = ForeignKey.CASCADE))
public class NotaMascota {
    @PrimaryKey(autoGenerate = true)
    public int id_nota;

    @ColumnInfo(name = "id_mascota")
    public int idMascota;

    @ColumnInfo(name = "titulo")
    public String titulo;

    @ColumnInfo(name = "contenido")
    public String contenido;

    public NotaMascota(int idMascota, String titulo, String contenido) {
        this.idMascota = idMascota;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public int getId_nota() {
        return id_nota;
    }

    public void setId_nota(int id_nota) {
        this.id_nota = id_nota;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
