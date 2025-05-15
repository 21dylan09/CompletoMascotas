package com.example.loginsignup.actividadesDueño.Geolocalizacion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesCuidador.adapters.CuidadorAdapter;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.UbicacionCuidador;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DuenoMapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RecyclerView recyclerCuidadores;
    private CuidadorAdapter adapter;
    private List<UbicacionCuidador> listaCuidadores = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dueno_mapa);

        recyclerCuidadores = findViewById(R.id.recyclerCuidadores);
        recyclerCuidadores.setLayoutManager(new LinearLayoutManager(this));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapDueno);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        new Thread(() -> {
            listaCuidadores = BaseDatos.getBaseDatos(this).UbicacionCuidadorDAO().obtenerTodos();

            runOnUiThread(() -> {
                if (!listaCuidadores.isEmpty()) {
                    // Pintar pines en el mapa
                    for (UbicacionCuidador cuidador : listaCuidadores) {
                        LatLng pos = new LatLng(cuidador.latitud, cuidador.longitud);
                        mMap.addMarker(new MarkerOptions().position(pos).title("Cuidador").snippet(cuidador.direccionTexto));
                    }

                    // Centrar mapa
                    LatLng primera = new LatLng(listaCuidadores.get(0).latitud, listaCuidadores.get(0).longitud);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(primera, 13));

                    // Cargar lista en RecyclerView
                    adapter = new CuidadorAdapter(listaCuidadores, this);
                    recyclerCuidadores.setAdapter(adapter);
                }
            });
        }).start();
    }
}

