package com.example.loginsignup.actividadesDueño;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Ubicacion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class VerRecorridoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText etFecha, etHoraInicio, etHoraFin;
    private Button btnFiltrar;

    private final int mascotaId = MascotaSeleccionada.getInstance().getIdMascota();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_recorrido);

        etFecha = findViewById(R.id.etFecha);
        etHoraInicio = findViewById(R.id.etHoraInicio);
        etHoraFin = findViewById(R.id.etHoraFin);
        btnFiltrar = findViewById(R.id.btnFiltrar);

        // Fecha
        etFecha.setOnClickListener(v -> {
            DatePickerFragment.showDatePicker(this, etFecha);
        });

        // Hora inicio y fin
        etHoraInicio.setOnClickListener(v -> showTimePicker(etHoraInicio));
        etHoraFin.setOnClickListener(v -> showTimePicker(etHoraFin));

        // Botón filtrar
        btnFiltrar.setOnClickListener(v -> filtrarUbicaciones());

        // Cargar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapRecorrido);
        mapFragment.getMapAsync(this);
    }

    private void showTimePicker(EditText target) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    String hora = String.format(Locale.getDefault(), "%02d:%02d:00", hourOfDay, minute);
                    target.setText(hora);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        dialog.show();
    }

    private void filtrarUbicaciones() {
        String fecha = etFecha.getText().toString();
        String horaInicio = etHoraInicio.getText().toString();
        String horaFin = etHoraFin.getText().toString();

        if (fecha.isEmpty() || horaInicio.isEmpty() || horaFin.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            List<Ubicacion> lista = BaseDatos.getBaseDatos(getApplicationContext())
                    .ubicacionDAO()
                    .obtenerUbicacionesPorRango(mascotaId, fecha, horaInicio, horaFin);

            runOnUiThread(() -> mostrarRecorridoEnMapa(lista));
        }).start();
    }

    private void mostrarRecorridoEnMapa(@NonNull List<Ubicacion> lista) {
        if (mMap == null) return;

        mMap.clear();
        if (lista.isEmpty()) {
            Toast.makeText(this, "No hay ubicaciones en ese rango", Toast.LENGTH_SHORT).show();
            return;
        }

        PolylineOptions recorrido = new PolylineOptions();

        for (Ubicacion u : lista) {
            Log.d("Mapa", "Punto: " + u.getLatitud() + ", " + u.getLongitud());
            LatLng punto = new LatLng(u.getLatitud(), u.getLongitud());
            recorrido.add(punto);
        }

        mMap.addPolyline(recorrido.width(5).color(R.color.purple_500));

        // Centrar la cámara en el primer punto
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lista.get(0).getLatitud(), lista.get(0).getLongitud()), 15));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
}
