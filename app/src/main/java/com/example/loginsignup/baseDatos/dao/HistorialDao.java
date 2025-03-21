package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginsignup.baseDatos.entidades.HistorialMedico;

import java.util.List;

@Dao
public interface HistorialDao {

    @Insert
    long insertarHistorial(HistorialMedico historial);

    @Query("SELECT * FROM historial_medico WHERE id_mascota = :idMascota")
    List<HistorialMedico> obtenerHistorialPorMascota(int idMascota);

    @Query("SELECT * FROM historial_medico WHERE fecha BETWEEN :fechaInicio AND :fechaFin")
    List<HistorialMedico> buscarPorFecha(long fechaInicio, long fechaFin);

    @Query("SELECT * FROM historial_medico WHERE diagnostico LIKE '%' || :diagnostico || '%'")
    List<HistorialMedico> buscarPorDiagnostico(String diagnostico);
}