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
import android.widget.EditText;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ZonasSeguras extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mapa;
    private GeofencingClient clienteGeocerca;
    private GeoCercaHelper ayudanteGeocerca;
    private FusedLocationProviderClient clienteUbicacion;

    private Button btnObtenerUbicacion, btnAgregarZonaSegura, btnEliminarZonaSegura;
    private EditText nombreZonaSeguraInput;
    private TextView textoZonaSegura;
    private List<ZonaSegura> listaZonasSeguras; // Lista de zonas seguras guardadas
    private List<Marker> marcadoresZonas; // Para mantener el marcador de cada zona segura
    private List<Geofence> geocercas; // Para gestionar las geocercas de las zonas
    private List<Circle> circulosZonasSeguras; // Lista para los círculos de las zonas seguras
    private Marker marcadorUsuario;  // Variable para el marcador de la ubicación actual del usuario


    private static final float RADIO_ZONA_SEGURA = 200f;
    private static final String ID_GEOCERCA = "ZONA_SEGURA";

    private LatLng coordenadasZonaSegura = new LatLng(4.60971, -74.08175); // Bogotá por defecto
    private LatLng ubicacionActual = null;

    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        // Inicialización de vistas y variables
        textoZonaSegura = findViewById(R.id.textoZonaSegura);
        btnObtenerUbicacion = findViewById(R.id.btnObtenerUbicacion);
        btnAgregarZonaSegura = findViewById(R.id.btnAgregarZonaSegura);
        nombreZonaSeguraInput = findViewById(R.id.nombreZonaSeguraInput);
        listaZonasSeguras = new ArrayList<>();
        marcadoresZonas = new ArrayList<>();
        geocercas = new ArrayList<>();
        circulosZonasSeguras = new ArrayList<>();
        btnEliminarZonaSegura = findViewById(R.id.btnEliminarZonaSegura);

        clienteGeocerca = LocationServices.getGeofencingClient(this);
        clienteUbicacion = LocationServices.getFusedLocationProviderClient(this);
        ayudanteGeocerca = new GeoCercaHelper(this);

        SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (fragmentoMapa != null) {
            fragmentoMapa.getMapAsync(this);
        }

        // Botón para obtener ubicación
        btnObtenerUbicacion.setOnClickListener(v -> obtenerUbicacionActual());

        // Botón para agregar zona segura
        btnAgregarZonaSegura.setOnClickListener(v -> {
            String nombreZona = nombreZonaSeguraInput.getText().toString().trim();
            if (ubicacionActual != null && !nombreZona.isEmpty()) {
                // Verificar si el nombre ya existe
                if (nombreZonaExistente(nombreZona)) {
                    Toast.makeText(this, "El nombre de la zona ya está en la lista", Toast.LENGTH_SHORT).show();
                    return;
                }

                coordenadasZonaSegura = ubicacionActual;
                // Guardar la zona con su nombre y coordenadas
                ZonaSegura nuevaZona = new ZonaSegura(nombreZona, coordenadasZonaSegura, null);
                listaZonasSeguras.add(nuevaZona);

                // Dibujar la zona y guardar el círculo
                Circle zonaSeguraCircle = dibujarZonaSegura(coordenadasZonaSegura);
                nuevaZona.setZonaSeguraCircle(zonaSeguraCircle);

                // Actualizar la lista de zonas
                mostrarZonasGuardadas();

                // Limpiar el campo de nombre
                nombreZonaSeguraInput.setText("");
            } else {
                Toast.makeText(this, "Por favor ingresa un nombre para la zona", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para eliminar zona segura
        btnEliminarZonaSegura.setOnClickListener(v -> {
            if (!listaZonasSeguras.isEmpty()) {
                // Eliminar la última zona añadida
                ZonaSegura zonaAEliminar = listaZonasSeguras.get(listaZonasSeguras.size() - 1);
                eliminarZonaSegura(zonaAEliminar);
            }
        });

        configurarCallbackUbicacion();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadasZonaSegura, 16f));
        dibujarZonaSegura(coordenadasZonaSegura);
        agregarGeocerca(coordenadasZonaSegura, RADIO_ZONA_SEGURA);
        iniciarUbicacionEnTiempoReal(); // Iniciar el seguimiento de ubicación en tiempo real
    }

    private Circle dibujarZonaSegura(LatLng latLng) {
        // Dibujar el círculo de la zona segura
        CircleOptions opcionesCirculo = new CircleOptions()
                .center(latLng)
                .radius(RADIO_ZONA_SEGURA)
                .strokeColor(Color.argb(255, 70, 70, 70))
                .fillColor(Color.argb(64, 150, 150, 150))
                .strokeWidth(4);

        Circle zonaSeguraCircle = mapa.addCircle(opcionesCirculo);
        circulosZonasSeguras.add(zonaSeguraCircle); // Guardamos el círculo para poder eliminarlo más tarde

        return zonaSeguraCircle;
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
                .addOnSuccessListener(unused -> Log.d("ZonasSeguras", "Geocerca agregada correctamente"))
                .addOnFailureListener(e -> Log.e("ZonasSeguras", "Error al agregar geocerca: " + ayudanteGeocerca.obtenerMensajeError(e)));
    }

    private boolean nombreZonaExistente(String nombreZona) {
        for (ZonaSegura zona : listaZonasSeguras) {
            if (zona.getNombre().equalsIgnoreCase(nombreZona)) {
                return true; // Ya existe
            }
        }
        return false; // No existe
    }

    private void eliminarZonaSegura(ZonaSegura zona) {
        // Primero eliminamos la zona de la lista
        listaZonasSeguras.remove(zona);

        // Eliminar solo el círculo asociado a la zona
        if (zona.getZonaSeguraCircle() != null) {
            zona.getZonaSeguraCircle().remove();  // Eliminar el círculo del mapa
        }

        // Actualizar la lista de zonas seguras en el TextView
        mostrarZonasGuardadas();
        Toast.makeText(this, "Zona eliminada", Toast.LENGTH_SHORT).show();
    }

    private void mostrarZonasGuardadas() {
        StringBuilder texto = new StringBuilder("Zonas seguras guardadas:\n");
        for (ZonaSegura zona : listaZonasSeguras) {
            texto.append(zona.getNombre()).append(" - ").append(zona.getCoordenadas().latitude)
                    .append(", ").append(zona.getCoordenadas().longitude).append("\n");
        }
        textoZonaSegura.setText(texto.toString());
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
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 17f));
                        Toast.makeText(this, "Ubicación actual obtenida", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void configurarCallbackUbicacion() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null || mapa == null) return;

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
        solicitud.setInterval(3000); // cada 3 segundos
        solicitud.setFastestInterval(2000);

        clienteUbicacion.requestLocationUpdates(solicitud, locationCallback, Looper.getMainLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
        clienteUbicacion.removeLocationUpdates(locationCallback);
    }
}
