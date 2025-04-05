package com.example.loginsignup.actividadesDueño;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private ImageButton boton_atras;
    private TextView tvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restricciones_mascota);

        etRestriccion = findViewById(R.id.etRestriccion);
        btnAgregar = findViewById(R.id.btnAgregar);
        listViewRestricciones = findViewById(R.id.listViewRestricciones);
        boton_atras = findViewById(R.id.btnBack);
        tvTitulo = findViewById(R.id.tvTitle);
        tvTitulo.setText("RESTRICCIONES");

        boton_atras.setOnClickListener(v -> {
            startActivity(new Intent(RestriccionesMascotaActivity.this, BotonesHistoriasdeUsuario.class));
        });

        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        restriccionesDao = db.restriccionDao();

        listaRestricciones = new ArrayList<>(restriccionesDao.obtenerPorIdMascota(idMascota));

        adapter = new CustomAdapter();
        listViewRestricciones.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> agregarRestriccion());

        // Evento para editar restricción cuando se haga clic
        listViewRestricciones.setOnItemClickListener((parent, view, position, id) -> editarRestriccion(listaRestricciones.get(position)));
    }

    private void agregarRestriccion() {
        String descripcion = etRestriccion.getText().toString().trim();

        if (!descripcion.isEmpty()) {
            Restriccion nuevaRestriccion = new Restriccion(descripcion, idMascota);
            restriccionesDao.insertar(nuevaRestriccion);

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

        listaRestricciones.clear();
        listaRestricciones.addAll(restriccionesDao.obtenerPorIdMascota(idMascota));
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Restricción eliminada", Toast.LENGTH_SHORT).show();
    }

    private void editarRestriccion(Restriccion restriccion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Restricción");

        // Agregar un EditText al diálogo
        final EditText input = new EditText(this);
        input.setText(restriccion.getDescripcion());
        builder.setView(input);

        // Botón para guardar los cambios
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevaDescripcion = input.getText().toString().trim();
            if (!nuevaDescripcion.isEmpty()) {
                restriccion.setDescripcion(nuevaDescripcion);
                restriccionesDao.actualizar(restriccion);

                listaRestricciones.clear();
                listaRestricciones.addAll(restriccionesDao.obtenerPorIdMascota(idMascota));
                adapter.notifyDataSetChanged();

                Toast.makeText(this, "Restricción actualizada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "La restricción no puede estar vacía", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para cancelar
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
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

            Restriccion restriccion = getItem(position);
            if (restriccion != null) {
                tvRestriccion.setText(restriccion.getDescripcion());

                btnEliminar.setOnClickListener(v -> eliminarRestriccion(restriccion));
            }

            return convertView;
        }
    }
}
