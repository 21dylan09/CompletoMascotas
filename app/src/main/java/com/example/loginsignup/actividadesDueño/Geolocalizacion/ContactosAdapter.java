package com.example.loginsignup.actividadesDueño.Geolocalizacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsignup.baseDatos.dao.ContactoDao;
import com.example.loginsignup.baseDatos.entidades.Contactos;
import com.example.loginsignup.R;

import java.util.List;

public class ContactosAdapter extends ArrayAdapter<Contactos> {

    private Context context;
    private List<Contactos> contactosList;
    private ContactoDao contactoDao;

    public ContactosAdapter(Context context, List<Contactos> contactosList, ContactoDao contactoDao) {
        super(context, 0, contactosList);
        this.context = context;
        this.contactosList = contactosList;
        this.contactoDao = contactoDao;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contacto_item, parent, false);
        }

        // Obtener los elementos de la vista
        TextView contactoNombre = convertView.findViewById(R.id.contactoNombre);
        TextView contactoTelefono = convertView.findViewById(R.id.contactoTelefono);  // Mostrar el teléfono
        Button btnEliminarContacto = convertView.findViewById(R.id.btnEliminarContacto);

        // Obtener el contacto actual
        final Contactos contacto = contactosList.get(position);

        // Establecer los valores del contacto en la vista
        contactoNombre.setText(contacto.getNombre());
        contactoTelefono.setText(contacto.getTelefono());  // Mostrar teléfono en lugar de correo

        // Configurar el botón de eliminar
        btnEliminarContacto.setOnClickListener(v -> {
            // Eliminar el contacto cuando se presione el botón
            eliminarContacto(contacto);
        });

        return convertView;
    }

    private void eliminarContacto(Contactos contacto) {
        // Eliminar el contacto de la base de datos en un hilo de fondo
        new Thread(() -> {
            // Llamamos al DAO para eliminar el contacto
            contactoDao.eliminarContacto(contacto);

            // Eliminar el contacto de la lista y actualizar la UI
            contactosList.remove(contacto);

            // Actualizar la UI en el hilo principal
            ((AppCompatActivity) context).runOnUiThread(() -> {
                notifyDataSetChanged(); // Notificar al adaptador que la lista ha cambiado
            });
        }).start();
    }
}
