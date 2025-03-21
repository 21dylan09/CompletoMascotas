package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.example.loginsignup.baseDatos.entidades.Estado;

@Dao
public interface EstadoDao {
    @Insert
    void insertarEstado(Estado estado);

    @Query("SELECT * FROM estado")
    List<Estado> obtenerEstados();

    @Query("SELECT * FROM estado WHERE id_estado = :id")
    Estado obtenerEstadoPorId(int id);
}
