package com.example.loginsignup.actividadesDueño;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

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
    private List<ZonaSegura> listaZonasSeguras;
    private List<Marker> marcadoresZonas;
    private List<Geofence> geocercas;
    private List<Circle> circulosZonasSeguras;
    private Marker marcadorUsuario;

    private static final float RADIO_ZONA_SEGURA = 200f;
    private static final String ID_GEOCERCA = "ZONA_SEGURA";

    private LatLng coordenadasZonaSegura = new LatLng(4.60971, -74.08175); // Bogotá por defecto
    private LatLng ubicacionActual = null;

    private LocationCallback locationCallback;

    private static final String CHANNEL_ID = "mascota_perdida_channel"; // Canal para notificaciones

    private EditText inputNombreContacto, inputCorreoContacto;
    private Button btnAgregarContacto;
    private ListView listaContactosView;
    private ArrayAdapter<String> contactosAdapter;
    private List<Contacto> listaContactos = new ArrayList<>();

    public static class Contacto {
        String nombre;
        String correo;

        public Contacto(String nombre, String correo) {
            this.nombre = nombre;
            this.correo = correo;
        }
    }

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

        inputNombreContacto = findViewById(R.id.inputNombreContacto);
        inputCorreoContacto = findViewById(R.id.inputCorreoContacto);
        btnAgregarContacto = findViewById(R.id.btnAgregarContacto);
        listaContactosView = findViewById(R.id.listaContactos);

        contactosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaContactosView.setAdapter(contactosAdapter);

        btnAgregarContacto.setOnClickListener(v -> {
            String nombre = inputNombreContacto.getText().toString().trim();
            String correo = inputCorreoContacto.getText().toString().trim();

            if (nombre.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Contacto c : listaContactos) {
                if (c.correo.equalsIgnoreCase(correo)) {
                    Toast.makeText(this, "Este contacto ya existe", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Contacto nuevo = new Contacto(nombre, correo);
            listaContactos.add(nuevo);
            contactosAdapter.add(nombre + " (" + correo + ")");
            inputNombreContacto.setText("");
            inputCorreoContacto.setText("");
        });


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
        crearCanalNotificacion(); // Crear el canal de notificación
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

    private void notificarMascotaPerdida(LatLng ubicacion) {
        // Notificación local
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Construcción de la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_mascota)  // Reemplaza con el icono adecuado
                .setContentTitle("¡Tu mascota está perdida!")
                .setContentText("Salió de la zona segura. Última ubicación: " + ubicacion.latitude + ", " + ubicacion.longitude)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Mostrar la notificación
        notificationManager.notify(1, builder.build());

        // Enviar correo a cada contacto en la lista de contactos
        for (Contacto contacto : listaContactos) {
            new Thread(() -> {
                try {
                    MailSender.enviarCorreo(
                            getApplicationContext(), // Asegúrate de pasar el contexto
                            contacto.correo,
                            "🚨 Alerta: Mascota perdida",
                            "Hola " + contacto.nombre + ",\n\nLa mascota ha salido de su zona segura.\nÚltima ubicación conocida:\nLat: " +
                                    ubicacion.latitude + "\nLng: " + ubicacion.longitude
                    );
                } catch (Exception e) {
                    e.printStackTrace();  // Manejo de errores al enviar correo
                }
            }).start();
        }
    }



    private void crearCanalNotificacion() {
        // Crear el canal de notificación para Android 8 y versiones posteriores
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Zona Segura";
            String description = "Notificaciones sobre la mascota perdida";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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

                // Comprobar si la mascota está fuera de la zona segura
                comprobarSiEstaFueraDeLaZona(nuevaUbicacion, coordenadasZonaSegura);

                // Actualizar el marcador de la ubicación del usuario en el mapa
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

    private void comprobarSiEstaFueraDeLaZona(LatLng ubicacionMascota, LatLng zonaSegura) {
        // Calcular la distancia entre la ubicación de la mascota y el centro de la zona segura
        float[] distancia = new float[1];
        Location.distanceBetween(
                ubicacionMascota.latitude, ubicacionMascota.longitude,
                zonaSegura.latitude, zonaSegura.longitude,
                distancia);

        // Si la distancia es mayor al radio de la zona segura, la mascota está fuera de la zona
        if (distancia[0] > RADIO_ZONA_SEGURA) {
            // Notificar que la mascota está fuera de la zona segura
            notificarMascotaPerdida(ubicacionMascota);
        }
    }

}
