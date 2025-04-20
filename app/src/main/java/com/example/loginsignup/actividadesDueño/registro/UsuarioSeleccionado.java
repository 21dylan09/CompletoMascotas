package com.example.loginsignup.actividadesDueño.registro;

public class  UsuarioSeleccionado {
    private static UsuarioSeleccionado instancia;
    private int id_Usuario;

    private UsuarioSeleccionado() { }

    public static UsuarioSeleccionado getInstance() {
        if (instancia == null) {
            instancia = new UsuarioSeleccionado();
        }
        return instancia;
    }

    public void setId_Usuario(int id) {
        this.id_Usuario = id;
    }

    public int getId_Usuario() {
        return id_Usuario;
    }
}