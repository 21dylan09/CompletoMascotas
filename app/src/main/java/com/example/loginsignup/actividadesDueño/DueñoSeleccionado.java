package com.example.loginsignup.actividadesDueño;

public class DueñoSeleccionado {
    private static DueñoSeleccionado instancia;
    private int idDueño;

    private DueñoSeleccionado() { }

    public static DueñoSeleccionado getInstance() {
        if (instancia == null) {
            instancia = new DueñoSeleccionado();
        }
        return instancia;
    }

    public void setIdDueño(int id) {
        this.idDueño = id;
    }

    public int getIdDueño() {
        return idDueño;
    }
}