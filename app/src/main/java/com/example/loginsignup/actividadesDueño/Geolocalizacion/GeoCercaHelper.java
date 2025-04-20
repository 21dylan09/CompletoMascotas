package com.example.loginsignup.actividadesDueño.Geolocalizacion;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;

import com.google.android.gms.maps.model.LatLng;

public class GeoCercaHelper {

    private final Context contexto;

    public GeoCercaHelper(Context contexto) {
        this.contexto = contexto;
    }

    @SuppressLint("VisibleForTests")
    public Geofence crearGeocerca(String id, LatLng latLng, float radio, int tipoTransicion) {
        return new Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(latLng.latitude, latLng.longitude, radio)
                .setTransitionTypes(tipoTransicion)
                .setLoiteringDelay(30000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public GeofencingRequest crearSolicitudGeocerca(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT)
                .addGeofence(geofence)
                .build();
    }

    public PendingIntent obtenerIntent() {
        Intent intent = new Intent(contexto, GeofenceBroadcastReceiver.class);
        return PendingIntent.getBroadcast(
                contexto,
                2607,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
    }

    public String obtenerMensajeError(Exception e) {
        if (e instanceof ApiException) {
            switch (((ApiException) e).getStatusCode()) {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "Geocerca no disponible";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "Demasiadas geocercas";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "Demasiados intents";
            }
        }
        return e.getLocalizedMessage();
    }
}

