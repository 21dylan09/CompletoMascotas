package com.example.loginsignup.actividadesDueño.registro;

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