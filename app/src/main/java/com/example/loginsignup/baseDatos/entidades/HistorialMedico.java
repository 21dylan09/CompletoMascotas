package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "historial_medico",
        foreignKeys = @ForeignKey(entity = Mascota.class,
                                  parentColumns = "id_mascota",
                                  childColumns = "id_mascota",
                                  onDelete = ForeignKey.CASCADE))
public class HistorialMedico {
    @PrimaryKey(autoGenerate = true)
    private int id_historial;
    public long fecha;
    public String diagnostico;
    public String tratamiento;
    public int id_mascota;
}
