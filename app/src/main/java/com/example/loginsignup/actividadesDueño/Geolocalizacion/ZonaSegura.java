package com.example.loginsignup.actividadesDueño.Geolocalizacion;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

public class ZonaSegura {
    private String nombre;
    private LatLng coordenadas;
    private Circle zonaSeguraCircle;  // Añadimos una referencia al círculo de la zona

    public ZonaSegura(String nombre, LatLng coordenadas, Circle zonaSeguraCircle) {
        this.nombre = nombre;
        this.coordenadas = coordenadas;
        this.zonaSeguraCircle = zonaSeguraCircle;  // Guardamos el círculo
    }

    public String getNombre() {
        return nombre;
    }

    public LatLng getCoordenadas() {
        return coordenadas;
    }

    public Circle getZonaSeguraCircle() {
        return zonaSeguraCircle;  // Devuelve el círculo
    }

    public void setZonaSeguraCircle(Circle zonaSeguraCircle) {
        this.zonaSeguraCircle = zonaSeguraCircle;  // Actualiza el círculo
    }
}


