package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.loginsignup.baseDatos.entidades.Mascota;

import java.util.List;

@Dao
public interface MascotaDao {

    @Insert
    long insertarMascota(Mascota mascota);

    @Query("SELECT * FROM mascotas WHERE id_dueño = :idUsuario")
    List<Mascota> obtenerMascotasDeUsuario(int idUsuario);

    @Query("SELECT peso FROM mascotas WHERE id_mascota = :mascotaId")
    float obtenerPeso(int mascotaId);

    default float calcularDosis(int mascotaId, float dosisPorKg) {
        float peso = obtenerPeso(mascotaId);
        return peso * dosisPorKg;
    }

    @Query("SELECT * FROM mascotas WHERE id_mascota = :idMascota LIMIT 1")
    Mascota obtenerMascotaPorId(int idMascota);

    @Update
    void actualizarMascota(Mascota mascota);



    @Query("SELECT * FROM mascotas WHERE nombre = :nombre AND id_dueño = :idDueño LIMIT 1")
    Mascota obtenerPorNombreYDueno(String nombre, int idDueño);

}
