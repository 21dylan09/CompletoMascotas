package com.example.loginsignup.actividadesDueño;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Geocoder;
import android.location.Address;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.ContactoDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZonasSeguras extends FragmentActivity implements OnMapReadyCallback {

    private static final int SMS_PERMISSION_CODE = 101;
    private ContactoDao contactoDao;
    private GoogleMap mapa;
    private GeofencingClient clienteGeocerca;
    private GeoCercaHelper ayudanteGeocerca;
    private FusedLocationProviderClient clienteUbicacion;

    private Button  btnAgregarZonaSegura, btnEliminarZonaSegura;
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

    private boolean mensajeAutomaticoEnviado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        // Inicialización de vistas y variables
        textoZonaSegura = findViewById(R.id.textoZonaSegura);
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

        BaseDatos db = BaseDatos.getBaseDatos(getApplicationContext());
        contactoDao = db.contactoDao();


        // Solicitar permisos para enviar SMS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }
        SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (fragmentoMapa != null) {
            fragmentoMapa.getMapAsync(this);
        }

        // Mostrar la alerta de ubicación al iniciar
        mostrarAlertaUbicacion();

        Button btnReenviarAlerta = findViewById(R.id.btnReactivarAlerta);
        btnReenviarAlerta.setOnClickListener(v -> {
            if (ubicacionActual != null) {
                String direccion = obtenerDireccion(ubicacionActual.latitude, ubicacionActual.longitude);
                enviarMensajeAMascotasFueraDeZona(direccion);
                Toast.makeText(this, "Mensaje reenviado manualmente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ubicación no disponible", Toast.LENGTH_SHORT).show();
            }
        });


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
    // Solicitar permisos para enviar SMS
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void mostrarAlertaUbicacion() {
        // Crear y mostrar la alerta de uso de ubicación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Uso de ubicación en tiempo real")
                .setMessage("Esta aplicación está utilizando la ubicación en tiempo real para ofrecer una mejor experiencia.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción a realizar al aceptar la alerta
                    }
                })
                .setCancelable(false)  // Evita que se cierre tocando fuera de la alerta
                .show();
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

    private void enviarMensajeAMascotasFueraDeZona(String direccion) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
            return;
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<String> telefonos = contactoDao.obtenerTelefonosDeContactos();

            if (telefonos != null && !telefonos.isEmpty()) {
                for (String telefono : telefonos) {
                    String mensaje = "🚨 ¡Tu mascota ha salido de la zona segura! Última ubicación: " + direccion;
                    enviarSms(telefono, mensaje);
                }
            }
        });
    }



    private void enviarSms(String phoneNumber, String messageText) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> parts = smsManager.divideMessage(messageText);  // Divide si es largo
                smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
                Log.d("ZonasSeguras", "Mensaje enviado a: " + phoneNumber);
            } else {
                Log.e("ZonasSeguras", "Permiso para enviar SMS no concedido");
            }
        } catch (Exception e) {
            Log.e("ZonasSeguras", "Error al enviar el mensaje: " + e.getMessage());
        }
    }

    // Método para obtener la dirección completa a partir de las coordenadas
    private String obtenerDireccion(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        try {
            // Obtener la dirección a partir de las coordenadas
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            // Si se encontró una dirección, devolverla
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Concatenar la dirección completa
                StringBuilder direccion = new StringBuilder();

                // Agregar la dirección completa
                if (address.getThoroughfare() != null) { // Calle
                    direccion.append(address.getThoroughfare()).append(", ");
                }
                if (address.getSubThoroughfare() != null) { // Número de la calle
                    direccion.append(address.getSubThoroughfare()).append(", ");
                }
                if (address.getLocality() != null) { // Ciudad
                    direccion.append(address.getLocality()).append(", ");
                }
                if (address.getAdminArea() != null) { // Estado/Provincia
                    direccion.append(address.getAdminArea()).append(", ");
                }
                if (address.getCountryName() != null) { // País
                    direccion.append(address.getCountryName());
                }

                // Retirar la coma final si hay alguna
                if (direccion.toString().endsWith(", ")) {
                    direccion.setLength(direccion.length() - 2);
                }

                return direccion.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Geocoder", "Error al obtener dirección: " + e.getMessage());
        }

        return null; // En caso de error o si no se encontró dirección
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
        Geocoder geocoder = new Geocoder(this);

        for (ZonaSegura zona : listaZonasSeguras) {
            // Obtener las coordenadas de la zona
            LatLng coordenadas = zona.getCoordenadas();

            // Convertir coordenadas en dirección
            String direccion = obtenerDireccion(coordenadas.latitude, coordenadas.longitude);

            if (direccion != null) {
                texto.append(zona.getNombre()).append(" - ").append(direccion).append("\n");
            } else {
                texto.append(zona.getNombre()).append(" - Dirección no disponible\n");
            }
        }

        textoZonaSegura.setText(texto.toString());
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
        float[] distancia = new float[1];
        Location.distanceBetween(
                ubicacionMascota.latitude, ubicacionMascota.longitude,
                zonaSegura.latitude, zonaSegura.longitude,
                distancia
        );

        if (distancia[0] > RADIO_ZONA_SEGURA && !mensajeAutomaticoEnviado) {
            mensajeAutomaticoEnviado = true; // Evita más mensajes automáticos
            String direccion = obtenerDireccion(ubicacionMascota.latitude, ubicacionMascota.longitude);
            enviarMensajeAMascotasFueraDeZona(direccion);
        }
    }


}
