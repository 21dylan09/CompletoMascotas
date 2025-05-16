package com.example.loginsignup.actividadesDueño.veterinaria;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.loginsignup.R;

public class PlaceInfoDialog extends DialogFragment {

    private String placeName;
    private String photoUrl;
    private double latitude;
    private double longitude;

    public PlaceInfoDialog(String placeName, String photoUrl, double latitude, double longitude) {
        this.placeName = placeName;
        this.photoUrl = photoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_place_info, null);

        TextView tvPlaceName = view.findViewById(R.id.tvPlaceName);
        ImageView ivPlacePhoto = view.findViewById(R.id.ivPlacePhoto);
        Button btnNavigate = view.findViewById(R.id.btnNavigate);

        tvPlaceName.setText(placeName);

        if (photoUrl != null && !photoUrl.isEmpty()) {
            Glide.with(this).load(photoUrl).into(ivPlacePhoto);
        } else {
            ivPlacePhoto.setImageResource(R.drawable.logo_mascota);
        }

        btnNavigate.setOnClickListener(v -> {
            // Crear URI para navegación en Google Maps
            String uri = "google.navigation:q=" + latitude + "," + longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
            dismiss();
        });

        builder.setView(view);

        return builder.create();
    }
}
