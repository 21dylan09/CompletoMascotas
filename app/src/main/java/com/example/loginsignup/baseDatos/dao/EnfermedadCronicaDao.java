package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginsignup.baseDatos.entidades.EnfermedadCronica;

import java.util.List;

@Dao
public interface EnfermedadCronicaDao {

    @Insert
    void insertarEnfermedad(EnfermedadCronica enfermedadCronica);

    @Query("SELECT * FROM enfermedades_cronicas WHERE id_mascota = :idMascota")
    List<EnfermedadCronica> obtenerEnfermedadesPorMascota(int idMascota);

    @Query("DELETE FROM enfermedades_cronicas WHERE id_mascota = :mascotaId AND enfermedad = :enfermedad")
    void eliminarEnfermedad(int mascotaId, String enfermedad);
}