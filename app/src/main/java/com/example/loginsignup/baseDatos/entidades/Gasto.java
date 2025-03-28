package com.example.loginsignup.baseDatos.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Gasto",
        foreignKeys = @ForeignKey(
                entity = Usuario.class,
                parentColumns = "id_usuario",
                childColumns = "id_dueño",
                onDelete = ForeignKey.CASCADE // Elimina las restricciones si el dueño es eliminado
        )
)
public class Gasto {

    @PrimaryKey(autoGenerate = true)
    public int id_gasto;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "monto")
    public String monto;

    @ColumnInfo(name = "id_dueño", index = true)
    public int idDueño;

    public Gasto(String descripcion, String monto, int idDueño) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.idDueño = idDueño;
    }

    public int getId_gasto() {
        return id_gasto;
    }

    public void setId_gasto(int id_gasto) {
        this.id_gasto = id_gasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public int getIdDueño() {
        return idDueño;
    }

    public void setIdMascota(int idDueño) {
        this.idDueño = idDueño;
    }
}
