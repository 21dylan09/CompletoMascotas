package com.example.loginsignup.actividadesDueño;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.entidades.Mascota;

import java.util.List;

public class MascotasListAdapter extends ArrayAdapter<Mascota> {

    public MascotasListAdapter(Context context, List<Mascota> mascotas) {
        super(context, 0, mascotas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_mascota, parent, false);
        }

        // Obtener la mascota actual
        Mascota mascota = getItem(position);

        // Configurar el TextView con el nombre de la mascota
        TextView textViewMascota = convertView.findViewById(R.id.textViewMascota);
        textViewMascota.setText(mascota.getNombre());

        return convertView;
    }
}

