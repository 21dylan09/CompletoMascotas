package com.example.loginsignup.actividadesDueño.veterinaria;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.loginsignup.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_REQUEST_CODE = 1000;
    private static final int RADIUS = 5000; // 5km
    private static final String PLACE_TYPE = "veterinary_care";

    private String apiKey = "AIzaSyCFvJ8UcSVZoLGRCYmd-NZDNbojZpzbNrQ";

    // Para guardar place_id de cada marcador
    private HashMap<Marker, String> markerPlaceIdMap = new HashMap<>();
    // Para guardar URLs de fotos por place_id (cache)
    private HashMap<String, String> placePhotoUrlMap = new HashMap<>();

    private RecyclerView recyclerPlaces;
    private PlacesAdapter adapter;
    private List<PlaceItem> placesList = new ArrayList<>();
    private HashMap<String, Marker> placeIdMarkerMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        recyclerPlaces = findViewById(R.id.recyclerPlaces);
        recyclerPlaces.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlacesAdapter(placesList, position -> {
            PlaceItem place = placesList.get(position);
            Marker marker = placeIdMarkerMap.get(place.getPlaceId());
            if (marker != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLocation(), 15));
                marker.showInfoWindow();
            }
        });
        recyclerPlaces.setAdapter(adapter);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Pedir permisos ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
                buscarVeterinariasCercanas(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMarkerClickListener(this);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            // InfoWindow view
            @Override
            public View getInfoWindow(Marker marker) {
                return null; // Dejar que getInfoContents se encargue
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                TextView tvTitle = view.findViewById(R.id.title);
                ImageView ivPhoto = view.findViewById(R.id.photo);

                tvTitle.setText(marker.getTitle());

                String placeId = markerPlaceIdMap.get(marker);
                if (placeId != null && placePhotoUrlMap.containsKey(placeId)) {
                    // Cargar imagen con Glide desde cache
                    Glide.with(MapsActivity.this)
                            .load(placePhotoUrlMap.get(placeId))
                            .placeholder(R.drawable.logo_mascota)
                            .into(ivPhoto);
                } else {
                    // Imagen por defecto mientras se carga o si no hay foto
                    ivPhoto.setImageResource(R.drawable.logo_mascota);
                }
                return view;
            }
        });
    }

    private void buscarVeterinariasCercanas(double lat, double lng) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + lat + "," + lng +
                "&radius=" + RADIUS +
                "&type=" + PLACE_TYPE +
                "&key=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject place = results.getJSONObject(i);

                            JSONObject geometry = place.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");

                            double placeLat = location.getDouble("lat");
                            double placeLng = location.getDouble("lng");
                            String name = place.getString("name");
                            String placeId = place.getString("place_id");

                            LatLng position = new LatLng(placeLat, placeLng);
                            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(name));
                            markerPlaceIdMap.put(marker, placeId);
                            placeIdMarkerMap.put(placeId, marker);

                            PlaceItem placeItem = new PlaceItem(placeId, name, position);
                            placesList.add(placeItem);
                            adapter.notifyDataSetChanged();


                            markerPlaceIdMap.put(marker, placeId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error procesando datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error en la consulta: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    // Cuando se toca un marcador, se hace la petición Place Details para obtener foto
    @Override
    public boolean onMarkerClick(Marker marker) {
        String placeId = markerPlaceIdMap.get(marker);
        if (placeId != null) {
            // Obtenemos la foto si no está en cache
            if (!placePhotoUrlMap.containsKey(placeId)) {
                obtenerFotoPlaceDetails(placeId, marker, true);
            } else {
                // Mostrar diálogo con datos y foto cacheada
                mostrarDialogoLugar(marker, placePhotoUrlMap.get(placeId));
            }
        }
        return true; // Evento manejado
    }


    private void obtenerFotoPlaceDetails(String placeId, Marker marker, boolean mostrarDialogo) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?" +
                "place_id=" + placeId +
                "&fields=photos" +
                "&key=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject result = response.getJSONObject("result");

                        String photoUrl = null;
                        if (result.has("photos")) {
                            JSONArray photos = result.getJSONArray("photos");
                            if (photos.length() > 0) {
                                JSONObject photo = photos.getJSONObject(0);
                                String photoRef = photo.getString("photo_reference");

                                photoUrl = "https://maps.googleapis.com/maps/api/place/photo?" +
                                        "maxwidth=400" +
                                        "&photoreference=" + photoRef +
                                        "&key=" + apiKey;

                                placePhotoUrlMap.put(placeId, photoUrl);
                            }
                        }
                        if (mostrarDialogo) {
                            mostrarDialogoLugar(marker, photoUrl);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error obteniendo foto", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error en Place Details: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    private void mostrarDialogoLugar(Marker marker, String photoUrl) {
        LatLng pos = marker.getPosition();
        PlaceInfoDialog dialog = new PlaceInfoDialog(marker.getTitle(), photoUrl, pos.latitude, pos.longitude);
        dialog.show(getSupportFragmentManager(), "PlaceInfoDialog");
    }



    // Permisos ubicación
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (mMap != null) {
                        mMap.setMyLocationEnabled(true);
                        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
                                buscarVeterinariasCercanas(location.getLatitude(), location.getLongitude());
                            }
                        });
                    }
                }
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
