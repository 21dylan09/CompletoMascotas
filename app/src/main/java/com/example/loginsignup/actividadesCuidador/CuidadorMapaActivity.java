package com.example.loginsignup.actividadesCuidador;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.UbicacionCuidador;
import com.example.loginsignup.actividadesCuidador.utils.DireccionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CuidadorMapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng ubicacionActual;
    private FusedLocationProviderClient fusedLocationClient;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuidador_mapa);

        btnGuardar = findViewById(R.id.btnGuardarUbicacion);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapCuidador);
        mapFragment.getMapAsync(this);

        EditText etTelefono = findViewById(R.id.etTelefono);

        btnGuardar.setOnClickListener(v -> {
            String telefono = etTelefono.getText().toString().trim();

            if (ubicacionActual != null && !telefono.isEmpty()) {
                String direccion = DireccionUtils.getDireccionFromLatLng(this, ubicacionActual.latitude, ubicacionActual.longitude);
                UbicacionCuidador ubicacion = new UbicacionCuidador(direccion, ubicacionActual.latitude, ubicacionActual.longitude, telefono);

                new Thread(() -> {
                    BaseDatos.getBaseDatos(this).UbicacionCuidadorDAO().insertar(ubicacion);
                    runOnUiThread(() -> Toast.makeText(this, "Ubicación guardada", Toast.LENGTH_SHORT).show());
                }).start();
            } else {
                Toast.makeText(this, "Falta dirección o teléfono", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                ubicacionActual = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionActual, 15));
                mMap.addMarker(new MarkerOptions().position(ubicacionActual).title("Tu ubicación"));
            }
        });
    }
}
