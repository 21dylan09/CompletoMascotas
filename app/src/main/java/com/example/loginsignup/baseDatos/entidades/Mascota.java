package com.example.loginsignup.baseDatos.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "mascotas",
        foreignKeys = {
            @ForeignKey(entity = Usuario.class,
                        parentColumns = "id_usuario",
                        childColumns = "id_dueño",
                        onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Enfermedad.class,
                        parentColumns = "id_enfermedad",
                        childColumns = "id_enfermedad",
                        onDelete = ForeignKey.SET_NULL)
        })
public class Mascota {
    @PrimaryKey(autoGenerate = true)
    public int id_mascota;
    public String nombre;
    public String tipo;
    public double peso;
    public String especie;
    public String raza;
    public String sexo;
    public int edad;
    public long fecha_registro;
    public int id_dueño;

    @ColumnInfo(name = "id_enfermedad", defaultValue = "NULL")
    public Integer idEnfermedad;

    public Mascota(String nombre, String tipo, double peso, String especie, String raza, String sexo, int edad, long fecha_registro, int id_dueño, Integer idEnfermedad) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.edad = edad;
        this.fecha_registro = fecha_registro;
        this.id_dueño = id_dueño;
        this.idEnfermedad = idEnfermedad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId_mascota() {
        return id_mascota;
    }

    public int getId_dueño() {
        return id_dueño;
    }
}
