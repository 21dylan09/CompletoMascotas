package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.loginsignup.baseDatos.entidades.RecomendacionAlimentacion;

import java.util.List;
@Dao
public interface RecomendacionAlimentosDao {
    @Insert
    void insertarRecomendacion(RecomendacionAlimentacion recomendacionAlimentacion);

    @Query("SELECT * FROM recomendacion_alimentos WHERE id_mascota = :idMascota")
    List<RecomendacionAlimentacion> obtenerRecomendacionporMascota(int idMascota);

    @Query("DELETE FROM recomendacion_alimentos WHERE id_mascota = :mascotaId AND recomendacion = :recomendacion")
    void eliminarRecomendacion(int mascotaId, String recomendacion);
}
