package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "cita_veterinarias",
        foreignKeys = {
          @ForeignKey(entity = Mascota.class,
                      parentColumns = "id_mascota",
                      childColumns = "id_mascota",
                      onDelete = ForeignKey.CASCADE),
          @ForeignKey(entity = Estado.class,
                      parentColumns = "id_estado",
                      childColumns = "id_estado")
        })
public class CitaVeterinaria {
    @PrimaryKey(autoGenerate = true)
    public int id_cita;
    public long fecha_hora;
    public long fecha_registro;
    public int id_mascota;
    public int id_estado;
    public int id_veterinario;
}
