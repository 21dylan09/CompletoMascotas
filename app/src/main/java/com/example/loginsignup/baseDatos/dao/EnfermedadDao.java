package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.loginsignup.baseDatos.entidades.Enfermedad;

@Dao
public interface EnfermedadDao {
    @Insert
    long insertarEnfermedad(Enfermedad enfermedad);

    @Query("SELECT * FROM enfermedad WHERE id_enfermedad = :idEnfermedad")
    Enfermedad obtenerEnfermedadPorId(int idEnfermedad);

    @Query("SELECT * FROM enfermedad")
    List<Enfermedad> obtenerTodasLasEnfermedades();
}
