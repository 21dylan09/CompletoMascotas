package com.example.loginsignup.actividadesDueño;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context contexto, Intent intent) {
        GeofencingEvent evento = GeofencingEvent.fromIntent(intent);

        if (evento.hasError()) {
            Log.e("GeofenceReceiver", "Error en evento: " + evento.getErrorCode());
            return;
        }

        int tipoTransicion = evento.getGeofenceTransition();
        if (tipoTransicion == Geofence.GEOFENCE_TRANSITION_EXIT) {
            enviarAlerta(contexto);
        }
    }

    private void enviarAlerta(Context contexto) {
        Toast.makeText(contexto, "⚠️ La mascota ha salido de la zona segura", Toast.LENGTH_LONG).show();
        // Aquí puedes disparar una notificación o acción adicional
    }
}

