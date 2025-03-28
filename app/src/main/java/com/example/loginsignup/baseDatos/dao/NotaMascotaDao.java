package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.example.loginsignup.baseDatos.entidades.NotaMascota;

import java.util.List;

@Dao
public interface NotaMascotaDao {

    @Insert
    void insertarNota(NotaMascota nota);

    @Query("SELECT * FROM notas_mascota WHERE id_mascota = :idMascota")
    List<NotaMascota> obtenerNotasDeMascota(int idMascota);

    @Delete
    void eliminarNota(NotaMascota nota);
}
