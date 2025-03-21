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
    public long fecha_registro;

    public HistorialMedico(long fecha, String diagnostico, String tratamiento, int id_mascota, long fecha_registro) {
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.id_mascota = id_mascota;
        this.fecha_registro = fecha_registro;
    }

    public int getId_historial() {
        return id_historial;
    }

    public void setId_historial(int id_historial) {
        this.id_historial = id_historial;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public int getId_mascota() {
        return id_mascota;
    }

    public void setId_mascota(int id_mascota) {
        this.id_mascota = id_mascota;
    }

    public long getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(long fecha_registro) {
        this.fecha_registro = fecha_registro;
    }
}
