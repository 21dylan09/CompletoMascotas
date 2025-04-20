package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginsignup.baseDatos.entidades.RegistroPeso;

import java.util.List;

@Dao
public interface RegistroPesoDAO {

    @Insert
    void insertar(RegistroPeso registro);

    @Query("SELECT * FROM RegistroPeso WHERE id_mascota = :idMascota ORDER BY id_peso DESC")
    List<RegistroPeso> obtenerRegistrosPorMascota(int idMascota);
}
