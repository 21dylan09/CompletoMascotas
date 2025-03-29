package com.example.loginsignup.baseDatos.entidades;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

@Entity(tableName = "recomendacion_alimentos",
        foreignKeys = @ForeignKey(entity = Mascota.class,
                parentColumns = "id_mascota",
                childColumns = "id_mascota",
                onDelete = ForeignKey.CASCADE))
public class RecomendacionAlimentacion {

    private int id_reco_alimento;
    private int id_mascota;
    private String recomendacion;

    public RecomendacionAlimentacion(int id_mascota, int id_reco_alimento, String recomendacion) {
        this.id_mascota = id_mascota;
        this.id_reco_alimento = id_reco_alimento;
        this.recomendacion = recomendacion;
    }

    public int getId_reco_alimento() {
        return id_reco_alimento;
    }

    public void setId_reco_alimento(int id_reco_alimento) {
        this.id_reco_alimento = id_reco_alimento;
    }

    public int getId_mascota() {
        return id_mascota;
    }

    public void setId_mascota(int id_mascota) {
        this.id_mascota = id_mascota;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }
}
