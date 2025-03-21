package com.example.loginsignup.actividades;

public class MascotaSeleccionada {
    private static MascotaSeleccionada instancia;
    private int idMascota;

    private MascotaSeleccionada() { }

    public static MascotaSeleccionada getInstance() {
        if (instancia == null) {
            instancia = new MascotaSeleccionada();
        }
        return instancia;
    }

    public void setIdMascota(int id) {
        this.idMascota = id;
    }

    public int getIdMascota() {
        return idMascota;
    }
}