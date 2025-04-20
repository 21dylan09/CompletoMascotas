package com.example.loginsignup.actividadesDueño.Geolocalizacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.BotonesHistoriasdeUsuario;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class ObtenerUbicacion extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView textViewLat;
    private Button btnLocation;
    private ImageButton boton_atras;
    private TextView tvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtener_ubicacion);

        // Inicializar vistas
        textViewLat = findViewById(R.id.text_view1);
        btnLocation = findViewById(R.id.bt_location);
        boton_atras = findViewById(R.id.btnBack);
        tvTitulo = findViewById(R.id.tvTitle);
        tvTitulo.setText("MAPA");

        boton_atras.setOnClickListener(v -> {
            startActivity(new Intent(ObtenerUbicacion.this, BotonesHistoriasdeUsuario.class));
        });

        // Inicializar el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtener el SupportMapFragment y notificar cuando el mapa esté listo
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Configurar el botón para obtener ubicación
        btnLocation.setOnClickListener(v -> getLocation());
    }

    // Se llama cuando el mapa está listo
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void getLocation() {
        // Verificar permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Obtener la ubicación
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Mostrar ubicación en el mapa
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(userLocation).title("Tu ubicación"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                            // Actualizar las coordenadas en el TextView
                            textViewLat.setText("Latitud: " + location.getLatitude() + "\nLongitud: " + location.getLongitude());
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                textViewLat.setText("Permiso denegado");
            }
        }
    }
}
