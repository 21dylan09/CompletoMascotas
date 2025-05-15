package com.example.loginsignup.actividadesCuidador.utils;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DireccionUtils {

    public static String getDireccionFromLatLng(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> result = geocoder.getFromLocation(lat, lng, 1);
            if (!result.isEmpty()) {
                return result.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Dirección no disponible";
    }
}

