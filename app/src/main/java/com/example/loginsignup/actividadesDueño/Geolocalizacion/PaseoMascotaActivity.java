package com.example.loginsignup.actividadesDueño.Geolocalizacion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.registro.MascotaSeleccionada;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Ubicacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class PaseoMascotaActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;
    private Handler handler = new Handler();
    private Runnable locationRunnable;
    private boolean isTracking = false;

    private int mascotaId = MascotaSeleccionada.getInstance().getIdMascota();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_paseo);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button btnIniciar = findViewById(R.id.btnIniciarPaseo);
        Button btnDetener = findViewById(R.id.btnDetenerPaseo);

        btnIniciar.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                iniciarSeguimiento();
            }
        });

        btnDetener.setOnClickListener(v -> detenerSeguimiento());
    }

    private void iniciarSeguimiento() {
        isTracking = true;
        Toast.makeText(this, "Paseo iniciado", Toast.LENGTH_SHORT).show();

        locationRunnable = new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                if (!isTracking) return;

                fusedLocationClient.getLastLocation().addOnSuccessListener(PaseoMascotaActivity.this, location -> {
                    if (location != null) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            guardarUbicacion(location);
                        });
                    }
                });

                handler.postDelayed(this, 60000); // 60,000 ms = 1 minuto
            }
        };

        handler.post(locationRunnable);
    }

    private void detenerSeguimiento() {
        isTracking = false;
        handler.removeCallbacks(locationRunnable);
        Toast.makeText(this, "Paseo detenido", Toast.LENGTH_SHORT).show();
    }

    private void guardarUbicacion(Location location) {
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String hora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        Ubicacion ubicacion = new Ubicacion(fecha, hora, lat, lon, mascotaId); // Asegúrate que 'mascotaId' esté definido

        BaseDatos.getBaseDatos(getApplicationContext()).ubicacionDAO().insertar(ubicacion);
        //Toast.makeText(this, "Ubicación guardada", Toast.LENGTH_SHORT).show();
    }

    // Manejar permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarSeguimiento();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
