package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginsignup.baseDatos.entidades.UbicacionCuidador;

import java.util.List;

@Dao
public interface UbicacionCuidadorDAO {

    @Insert
    void insertar(UbicacionCuidador ubicacion);

    @Query("SELECT * FROM ubicacion_cuidador")
    List<UbicacionCuidador> obtenerTodos();
}
