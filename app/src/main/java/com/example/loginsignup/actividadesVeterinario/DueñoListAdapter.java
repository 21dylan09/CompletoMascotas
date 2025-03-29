package com.example.loginsignup.actividadesVeterinario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.entidades.Usuario;

import java.util.List;

public class DueñoListAdapter extends ArrayAdapter<Usuario> {

    public DueñoListAdapter(Context context, List<Usuario> dueños) {
        super(context, 0, dueños);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dueno, parent, false);
        }

        // Obtener la mascota actual
        Usuario dueño = getItem(position);

        // Configurar el TextView con el nombre de la mascota
        TextView textViewMascota = convertView.findViewById(R.id.textViewDueño);
        textViewMascota.setText(dueño.getNombre());

        return convertView;
    }
}

