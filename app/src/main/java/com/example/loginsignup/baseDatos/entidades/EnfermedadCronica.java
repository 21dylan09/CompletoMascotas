package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(tableName = "enfermedades_cronicas",
        foreignKeys = @ForeignKey(entity = Mascota.class,
                parentColumns = "id_mascota",
                childColumns = "id_mascota",
                onDelete = ForeignKey.CASCADE))
public class EnfermedadCronica {
    @PrimaryKey(autoGenerate = true)
    private int id_enfermedad_cronica;
    private int id_mascota;
    private String enfermedad;
    private String medicamento;
    private String dosis;

    public EnfermedadCronica(int id_mascota, String enfermedad, String medicamento, String dosis) {
        this.id_mascota = id_mascota;
        this.enfermedad = enfermedad;
        this.medicamento = medicamento;
        this.dosis = dosis;
    }

    public int getId_enfermedad_cronica() { return id_enfermedad_cronica; }
    public void setId_enfermedad_cronica(int id) { this.id_enfermedad_cronica = id; }
    public int getId_mascota() { return id_mascota; }
    public void setId_mascota(int id_mascota) { this.id_mascota = id_mascota; }
    public String getEnfermedad() { return enfermedad; }
    public void setEnfermedad(String enfermedad) { this.enfermedad = enfermedad; }
    public String getMedicamento() { return medicamento; }
    public void setMedicamento(String medicamento) { this.medicamento = medicamento; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
}
