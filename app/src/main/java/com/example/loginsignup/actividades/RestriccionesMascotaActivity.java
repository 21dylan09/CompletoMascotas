package com.example.loginsignup.actividades;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.widget.TextView;
import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.RestriccionDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Restriccion;

import java.util.ArrayList;
import java.util.List;

public class RestriccionesMascotaActivity extends AppCompatActivity {

    private EditText etRestriccion;
    private Button btnAgregar;
    private ListView listViewRestricciones;
    private ArrayList<Restriccion> listaRestricciones;
    private CustomAdapter adapter;
    private RestriccionDao restriccionesDao;
    private int idMascota = MascotaSeleccionada.getInstance().getIdMascota();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restricciones_mascota);

        etRestriccion = findViewById(R.id.etRestriccion);
        btnAgregar = findViewById(R.id.btnAgregar);
        listViewRestricciones = findViewById(R.id.listViewRestricciones);

        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        restriccionesDao = db.restriccionDao();

        // Crear la lista de restricciones
        listaRestricciones = new ArrayList<>(restriccionesDao.obtenerPorIdMascota(idMascota));

        // Crear el adaptador personalizado para el ListView
        adapter = new CustomAdapter();
        listViewRestricciones.setAdapter(adapter);

        // Configurar el botón de agregar restricción
        btnAgregar.setOnClickListener(v -> agregarRestriccion());
    }

    private void agregarRestriccion() {
        String descripcion = etRestriccion.getText().toString().trim();

        if (!descripcion.isEmpty()) {
            // Crear nueva restricción y agregar a la base de datos
            Restriccion nuevaRestriccion = new Restriccion(descripcion, idMascota);
            restriccionesDao.insertar(nuevaRestriccion);

            // Actualizar la lista
            listaRestricciones.clear();
            listaRestricciones.addAll(restriccionesDao.obtenerPorIdMascota(idMascota));
            adapter.notifyDataSetChanged();

            etRestriccion.setText("");
        } else {
            Toast.makeText(this, "Por favor ingresa una restricción", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarRestriccion(Restriccion restriccion) {
        restriccionesDao.eliminar(restriccion);

        // Actualizar la lista
        listaRestricciones.clear();
        listaRestricciones.addAll(restriccionesDao.obtenerPorIdMascota(idMascota));
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Restricción eliminada", Toast.LENGTH_SHORT).show();
    }

    private class CustomAdapter extends ArrayAdapter<Restriccion> {
        private List<Restriccion> restriccionesList;

        public CustomAdapter() {
            super(RestriccionesMascotaActivity.this, R.layout.list_item_restriction, listaRestricciones);
            this.restriccionesList = listaRestricciones;
        }

        @Override
        public int getCount() {
            return restriccionesList.size();
        }

        @Override
        public Restriccion getItem(int position) {
            return restriccionesList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_restriction, parent, false);
            }

            TextView tvRestriccion = convertView.findViewById(R.id.tvRestriccion);
            Button btnEliminar = convertView.findViewById(R.id.btnEliminar);

            // Obtener la restricción actual
            Restriccion restriccion = getItem(position);
            if (restriccion != null) {
                tvRestriccion.setText(restriccion.getDescripcion());

                // Configurar el botón de eliminar
                btnEliminar.setOnClickListener(v -> eliminarRestriccion(restriccion));
            }

            return convertView;
        }
    }
}