package com.example.loginsignup.actividadesVeterinario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.registro.MascotaSeleccionada;
import com.example.loginsignup.baseDatos.dao.AlergiaDao;
import com.example.loginsignup.baseDatos.entidades.Alergia;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;

import java.util.ArrayList;
import java.util.List;

public class AlergiasMascotaActivity extends AppCompatActivity {

    private EditText etAlergia;
    private Button btnAgregar;
    private ListView listViewAlergias;
    private ArrayList<Alergia> listaAlergias;
    private CustomAdapter adapter;
    private AlergiaDao alergiaDao;
    private int idMascota = MascotaSeleccionada.getInstance().getIdMascota();
    private Alergia alergiaSeleccionada;  // Para almacenar la alergia seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alergias_mascota);

        etAlergia = findViewById(R.id.etAlergia);
        btnAgregar = findViewById(R.id.btnAgregar);
        listViewAlergias = findViewById(R.id.listViewAlergias);

        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        alergiaDao = db.alergiaDao();

        // Crear la lista de alergias
        listaAlergias = new ArrayList<>(alergiaDao.obtenerPorIdMascota(idMascota));

        // Crear el adaptador personalizado para el ListView
        adapter = new CustomAdapter();
        listViewAlergias.setAdapter(adapter);

        // Configurar el botón de agregar alergia
        btnAgregar.setOnClickListener(v -> agregarAlergia());
    }

    private void agregarAlergia() {
        String descripcion = etAlergia.getText().toString().trim();

        if (!descripcion.isEmpty()) {
            // Crear nueva alergia y agregar a la base de datos
            Alergia nuevaAlergia = new Alergia(descripcion, idMascota);
            alergiaDao.insertar(nuevaAlergia);

            // Actualizar la lista
            listaAlergias.add(nuevaAlergia); // No limpiar toda la lista, solo agregar la nueva alergia
            adapter.notifyDataSetChanged();

            etAlergia.setText("");
        } else {
            Toast.makeText(this, "Por favor ingresa una alergia", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarAlergia(Alergia alergia) {
        alergiaDao.eliminar(alergia);

        // Eliminar solo la alergia eliminada de la lista
        listaAlergias.remove(alergia);
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Alergia eliminada", Toast.LENGTH_SHORT).show();
    }

    private void modificarAlergia(Alergia alergia) {
        // Establecer la alergia seleccionada para modificar
        alergiaSeleccionada = alergia;

        // Poner la descripción de la alergia en el EditText para modificarla
        etAlergia.setText(alergia.getDescripcion());

        // Cambiar el texto del botón "Modificar" a "Guardar"
        btnAgregar.setText("Guardar cambios");

        // Cuando el usuario hace clic en "Guardar cambios"
        btnAgregar.setOnClickListener(v -> {
            String nuevaDescripcion = etAlergia.getText().toString().trim();
            if (!nuevaDescripcion.isEmpty()) {
                alergiaSeleccionada.setDescripcion(nuevaDescripcion);
                alergiaDao.actualizar(alergiaSeleccionada);

                // Actualizar solo la alergia modificada en la lista (sin eliminar las otras alergias)
                int index = listaAlergias.indexOf(alergiaSeleccionada);
                if (index != -1) {
                    listaAlergias.set(index, alergiaSeleccionada);
                    adapter.notifyDataSetChanged(); // Notificar al adaptador de los cambios
                }

                etAlergia.setText("");  // Limpiar el EditText
                btnAgregar.setText("Agregar Alergia");  // Restaurar texto del botón
                Toast.makeText(AlergiasMascotaActivity.this, "Alergia modificada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AlergiasMascotaActivity.this, "Por favor ingresa una descripción válida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CustomAdapter extends ArrayAdapter<Alergia> {
        private List<Alergia> alergiasList;

        public CustomAdapter() {
            super(AlergiasMascotaActivity.this, R.layout.list_item_alergia, listaAlergias);
            this.alergiasList = listaAlergias;
        }

        @Override
        public int getCount() {
            return alergiasList.size();
        }

        @Override
        public Alergia getItem(int position) {
            return alergiasList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_alergia, parent, false);
            }

            TextView tvAlergia = convertView.findViewById(R.id.tvAlergia);
            Button btnEliminar = convertView.findViewById(R.id.btnEliminar);
            Button btnModificar = convertView.findViewById(R.id.btnModificar);

            // Obtener la alergia actual
            Alergia alergia = getItem(position);
            if (alergia != null) {
                tvAlergia.setText(alergia.getDescripcion());

                // Configurar el botón de eliminar
                btnEliminar.setOnClickListener(v -> eliminarAlergia(alergia));

                // Configurar el botón de modificar
                btnModificar.setOnClickListener(v -> modificarAlergia(alergia));
            }

            return convertView;
        }
    }

}


