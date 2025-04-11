package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginsignup.baseDatos.entidades.Ubicacion;

import java.util.List;

@Dao
public interface UbicacionDAO {
    @Insert
    void insertar(Ubicacion ubicacion);

    @Query("SELECT * FROM Ubicacion WHERE id_mascota = :mascotaId")
    List<Ubicacion> obtenerPorMascota(int mascotaId);

    @Query("SELECT * FROM ubicacion WHERE id_mascota = :idMascota AND fecha = :fecha AND hora BETWEEN :horaInicio AND :horaFin ORDER BY hora ASC")
    List<Ubicacion> obtenerUbicacionesPorRango(int idMascota, String fecha, String horaInicio, String horaFin);

}