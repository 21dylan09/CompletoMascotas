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
}
