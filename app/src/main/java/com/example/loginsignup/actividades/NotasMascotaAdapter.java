package com.example.loginsignup.actividades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;
import com.example.loginsignup.R;

import com.example.loginsignup.baseDatos.entidades.NotaMascota;

import java.util.List;

public class NotasMascotaAdapter extends BaseAdapter {
    private Context context;
    private List<NotaMascota> notas;

    public NotasMascotaAdapter(Context context, List<NotaMascota> notas) {
        this.context = context;
        this.notas = notas;
    }

    @Override
    public int getCount() {
        return notas.size();
    }

    @Override
    public Object getItem(int position) {
        return notas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        }

        TextView titulo = convertView.findViewById(R.id.textViewTitulo);
        TextView contenido = convertView.findViewById(R.id.textViewContenido);

        NotaMascota nota = notas.get(position);
        titulo.setText(nota.titulo);
        contenido.setText(nota.contenido);

        return convertView;
    }
}

