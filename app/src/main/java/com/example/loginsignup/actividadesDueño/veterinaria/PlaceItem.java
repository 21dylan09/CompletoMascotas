package com.example.loginsignup.actividadesDueño.veterinaria;

import com.google.android.gms.maps.model.LatLng;

public class PlaceItem {
    private String placeId;
    private String name;
    private LatLng location;

    public PlaceItem(String placeId, String name, LatLng location) {
        this.placeId = placeId;
        this.name = name;
        this.location = location;
    }

    public String getPlaceId() { return placeId; }
    public String getName() { return name; }
    public LatLng getLocation() { return location; }
}
