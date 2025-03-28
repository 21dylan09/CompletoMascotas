package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.loginsignup.baseDatos.entidades.Gasto;

import java.util.List;

@Dao
public interface GastoDao {
    @Insert
    void insertar(Gasto gasto);

    @Query("SELECT * FROM Gasto")
    List<Gasto> obtenerTodas();

    @Query("SELECT * FROM Gasto WHERE id_dueño = :idDueño")
    List<Gasto> obtenerPorIdDueño(int idDueño);

    @Update
    void actualizar(Gasto gasto);

    // Eliminar una gasto
    @Delete
    void eliminar(Gasto gasto);
}
