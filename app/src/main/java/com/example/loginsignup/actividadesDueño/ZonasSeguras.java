package com.example.loginsignup.actividadesDueño;

import android.Manifest;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.loginsignup.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ZonasSeguras extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mapa;
    private GeofencingClient clienteGeocerca;
    private GeoCercaHelper ayudanteGeocerca;
    private FusedLocationProviderClient clienteUbicacion;

    private Button btnObtenerUbicacion, btnAgregarZonaSegura;
    private TextView textoZonaSegura;

    private static final float RADIO_ZONA_SEGURA = 200f;
    private static final String ID_GEOCERCA = "ZONA_SEGURA";

    private LatLng coordenadasZonaSegura = new LatLng(4.60971, -74.08175); // Por defecto: Bogotá
    private LatLng ubicacionActual = null;

    private Marker marcadorUsuario;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        textoZonaSegura = findViewById(R.id.textoZonaSegura);
        btnObtenerUbicacion = findViewById(R.id.btnObtenerUbicacion);
        btnAgregarZonaSegura = findViewById(R.id.btnAgregarZonaSegura);

        clienteGeocerca = LocationServices.getGeofencingClient(this);
        clienteUbicacion = LocationServices.getFusedLocationProviderClient(this);
        ayudanteGeocerca = new GeoCercaHelper(this);

        SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (fragmentoMapa != null) {
            fragmentoMapa.getMapAsync(this);
        }

        btnObtenerUbicacion.setOnClickListener(v -> obtenerUbicacionActual());

        btnAgregarZonaSegura.setOnClickListener(v -> {
            if (ubicacionActual != null) {
                coordenadasZonaSegura = ubicacionActual;
                mapa.clear(); // limpiar mapa
                dibujarZonaSegura(coordenadasZonaSegura);
                agregarGeocerca(coordenadasZonaSegura, RADIO_ZONA_SEGURA);
            } else {
                Toast.makeText(this, "Primero debes obtener tu ubicación actual", Toast.LENGTH_SHORT).show();
            }
        });

        configurarCallbackUbicacion(); // ⚙️ configurar seguimiento real
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadasZonaSegura, 16f));
        dibujarZonaSegura(coordenadasZonaSegura);
        agregarGeocerca(coordenadasZonaSegura, RADIO_ZONA_SEGURA);
        iniciarUbicacionEnTiempoReal(); // 🚀 iniciar seguimiento
    }

    private void dibujarZonaSegura(LatLng latLng) {
        CircleOptions opcionesCirculo = new CircleOptions()
                .center(latLng)
                .radius(RADIO_ZONA_SEGURA)
                .strokeColor(Color.argb(255, 70, 70, 70))
                .fillColor(Color.argb(64, 150, 150, 150))
                .strokeWidth(4);
        mapa.addCircle(opcionesCirculo);

        String coordenadasTexto = String.format("Zona segura guardada:\nLatitud: %.6f\nLongitud: %.6f",
                latLng.latitude, latLng.longitude);
        textoZonaSegura.setText(coordenadasTexto);
    }

    private void agregarGeocerca(LatLng latLng, float radio) {
        Geofence geocerca = ayudanteGeocerca.crearGeocerca(
                ID_GEOCERCA,
                latLng,
                radio,
                Geofence.GEOFENCE_TRANSITION_EXIT
        );

        GeofencingRequest solicitud = ayudanteGeocerca.crearSolicitudGeocerca(geocerca);
        PendingIntent intentPendiente = ayudanteGeocerca.obtenerIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    }, 101);
            return;
        }

        clienteGeocerca.addGeofences(solicitud, intentPendiente)
                .addOnSuccessListener(unused -> Log.d("ZonasSeguras", "✅ Geocerca agregada correctamente"))
                .addOnFailureListener(e -> Log.e("ZonasSeguras", "❌ Error al agregar geocerca: " + ayudanteGeocerca.obtenerMensajeError(e)));
    }

    private void obtenerUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    102);
            return;
        }

        clienteUbicacion.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 16f));
                        Toast.makeText(this, "Ubicación actual obtenida", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 🚀 Nuevo método para configurar el callback de ubicación
    private void configurarCallbackUbicacion() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) return;

                Location location = locationResult.getLastLocation();
                LatLng nuevaUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                ubicacionActual = nuevaUbicacion;

                if (marcadorUsuario == null) {
                    marcadorUsuario = mapa.addMarker(new MarkerOptions()
                            .position(nuevaUbicacion)
                            .title("Mi ubicación")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                } else {
                    marcadorUsuario.setPosition(nuevaUbicacion);
                }

                mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(nuevaUbicacion, 17f));
            }
        };
    }

    private void iniciarUbicacionEnTiempoReal() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    103);
            return;
        }

        LocationRequest solicitud = LocationRequest.create();
        solicitud.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        solicitud.setInterval(3000);
        solicitud.setFastestInterval(2000);

        clienteUbicacion.requestLocationUpdates(solicitud, locationCallback, Looper.getMainLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
        clienteUbicacion.removeLocationUpdates(locationCallback);
    }
}
