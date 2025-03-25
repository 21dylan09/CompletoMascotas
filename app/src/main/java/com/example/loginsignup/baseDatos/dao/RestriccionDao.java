package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.loginsignup.baseDatos.entidades.Restriccion;

import java.util.List;

@Dao
public interface RestriccionDao {

    // Insertar una nueva restricción
    @Insert
    void insertar(Restriccion restriccion);

    // Obtener todas las restricciones
    @Query("SELECT * FROM restriccion")
    List<Restriccion> obtenerTodas();

    // Obtener restricciones por ID de la mascota
    @Query("SELECT * FROM restriccion WHERE id_mascota = :idMascota")
    List<Restriccion> obtenerPorIdMascota(int idMascota);

    // Actualizar una restricción
    @Update
    void actualizar(Restriccion restriccion);

    // Eliminar una restricción
    @Delete
    void eliminar(Restriccion restriccion);
}
