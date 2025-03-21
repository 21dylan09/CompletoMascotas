package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.loginsignup.baseDatos.entidades.Mascota;

import java.util.List;

@Dao
public interface MascotaDao {

    // Insertar una nueva mascota
    @Insert
    void insertarMascota(Mascota mascota);

    // Obtener las mascotas de un usuario específico
    @Query("SELECT * FROM mascotas WHERE id_dueño = :idUsuario")
    List<Mascota> obtenerMascotasDeUsuario(int idUsuario);
}
