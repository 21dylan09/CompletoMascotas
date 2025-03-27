package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.loginsignup.baseDatos.entidades.Alergia;

import java.util.List;

@Dao
public interface AlergiaDao {

    // Insertar una nueva alergia
    @Insert
    void insertar(Alergia alergia);

    // Obtener todas las alergias
    @Query("SELECT * FROM alergia WHERE id_mascota = :idMascota")
    List<Alergia> obtenerPorIdMascota(int idMascota);

    // Eliminar una alergia
    @Delete
    void eliminar(Alergia alergia);

    // Actualizar una alergia
    @Update
    void actualizar(Alergia alergia);
}
