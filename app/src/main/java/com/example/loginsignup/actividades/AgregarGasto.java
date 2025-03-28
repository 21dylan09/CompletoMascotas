package com.example.loginsignup.actividades;

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
import androidx.room.Entity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.GastoDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;

import com.example.loginsignup.baseDatos.entidades.Gasto;

import java.util.ArrayList;
import java.util.List;

public class AgregarGasto extends AppCompatActivity {

    private EditText etDescripcionGasto, etMontoGasto;
    private TextView textViewTotalGastos;
    private Button btnAgregarGasto;
    private ListView listViewGastos;
    private ArrayList<Gasto> listaGastos;
    private CustomAdapter adapter;
    private GastoDao gastoDao;
    private int idMascota = MascotaSeleccionada.getInstance().getIdMascota(); // Obtener el ID de la mascota
    private int idDueño =  DueñoSeleccionado.getInstance().getIdMascota();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_gasto);

        etDescripcionGasto = findViewById(R.id.etDescripcionGasto);
        etMontoGasto = findViewById(R.id.etMontoGasto);
        btnAgregarGasto = findViewById(R.id.btnAgregarGasto);
        listViewGastos = findViewById(R.id.listViewGastos);
        textViewTotalGastos = findViewById(R.id.textViewTotalGastos);

        // Configurar la base de datos y el DAO
        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        gastoDao = db.gastoDao();

        // Crear la lista de gastos para la mascota seleccionada
        listaGastos = new ArrayList<>(gastoDao.obtenerPorIdDueño(idDueño));

        // Crear el adaptador personalizado para el ListView
        adapter = new CustomAdapter();
        listViewGastos.setAdapter(adapter);

        // Configurar el botón de agregar gasto
        btnAgregarGasto.setOnClickListener(v -> agregarGasto());
    }

    private void agregarGasto() {
        String descripcion = etDescripcionGasto.getText().toString().trim();
        String montoString = etMontoGasto.getText().toString().trim();

        if (!descripcion.isEmpty() && !montoString.isEmpty()) {
            double monto = Double.parseDouble(montoString); // Convertir el monto a double
            Gasto nuevoGasto = new Gasto(descripcion, montoString, idMascota); // Crear un nuevo gasto

            // Insertar el gasto en la base de datos
            gastoDao.insertar(nuevoGasto);

            // Actualizar la lista de gastos
            listaGastos.clear();
            listaGastos.addAll(gastoDao.obtenerPorIdDueño(idDueño));
            adapter.notifyDataSetChanged();

            // Limpiar campos de texto
            etDescripcionGasto.setText("");
            etMontoGasto.setText("");

            Toast.makeText(this, "Gasto registrado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarGasto(Gasto gasto) {
        gastoDao.eliminar(gasto);

        // Actualizar la lista de gastos
        listaGastos.clear();
        listaGastos.addAll(gastoDao.obtenerPorIdDueño(idDueño));
        adapter.notifyDataSetChanged();

        actualizarMontoTotal();
        Toast.makeText(this, "Gasto eliminado", Toast.LENGTH_SHORT).show();
    }

    private void actualizarMontoTotal() {
        // Calcular el monto total de los gastos
        double montoTotal = 0;
        for (Gasto gasto : listaGastos) {
            montoTotal += Double.parseDouble(gasto.getMonto());
        }

        // Mostrar el monto total en el TextView
        textViewTotalGastos.setText("Monto Total: $" + montoTotal);
    }

    private class CustomAdapter extends ArrayAdapter<Gasto> {
        private List<Gasto> gastosList;

        public CustomAdapter() {
            super(AgregarGasto.this, R.layout.list_item_gasto, listaGastos);
            this.gastosList = listaGastos;
        }

        @Override
        public int getCount() {
            return gastosList.size();
        }

        @Override
        public Gasto getItem(int position) {
            return gastosList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_gasto, parent, false);
            }

            TextView tvDescripcionGasto = convertView.findViewById(R.id.tvDescripcionGasto);
            TextView tvMonto = convertView.findViewById(R.id.tvMontoGasto);
            Button btnEliminar = convertView.findViewById(R.id.btnEliminarGasto);

            // Obtener el gasto actual
            Gasto gasto = getItem(position);
            if (gasto != null) {
                tvDescripcionGasto.setText(gasto.getDescripcion());
                tvMonto.setText(gasto.getMonto());

                // Configurar el botón de eliminar
                btnEliminar.setOnClickListener(v -> eliminarGasto(gasto));
            }

            return convertView;
        }
    }
}
