package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import com.example.loginsignup.baseDatos.entidades.Contactos;

@Dao
public interface ContactoDao {

    // Insertar un nuevo contacto
    @Insert
    long insertarContacto(Contactos contactos);

    // Obtener todos los contactos
    @Query("SELECT * FROM ContactosConfianza")
    List<Contactos> obtenerContactos();

    // Obtener un contacto por su ID
    @Query("SELECT telefono FROM ContactosConfianza")
    List<String> obtenerTelefonosDeContactos();

    @Query("SELECT * FROM ContactosConfianza WHERE correo = :correo OR telefono = :telefono LIMIT 1")
    Contactos obtenerContactoPorCorreoOCelular(String correo, String telefono);

    // Actualizar un contacto existente
    @Update
    int actualizarContacto(Contactos contactos);

    // Eliminar un contacto por su ID
    @Delete
    void eliminarContacto(Contactos contactos);
}
