package com.example.loginsignup.baseDatos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import com.example.loginsignup.baseDatos.entidades.Usuario;

@Dao
public interface UsuarioDao {
    @Insert
    long insertarUsuario(Usuario usuario);

    @Query("SELECT * FROM usuarios")
    List<Usuario> obtenerUsuarios();

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contraseña = :contraseña LIMIT 1")
    Usuario autenticarUsuario(String correo, String contraseña);

    @Query("SELECT id_usuario FROM usuarios WHERE correo = :correo AND contraseña = :contraseña LIMIT 1")
    int obtenerIdUsuario(String correo, String contraseña);

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    Usuario obtenerUsuarioPorCorreo(String correo);

    @Query("SELECT tipo_usuario FROM usuarios WHERE id_usuario = :idUsuario LIMIT 1")
    String obtenerTipoDeUsuario(int idUsuario);

}
