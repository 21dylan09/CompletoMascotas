package com.example.loginsignup.actividadesDueño;

import android.os.Bundle;
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
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Mascota;
import java.util.ArrayList;
import java.util.List;

public class ComidaMascotaActivity extends AppCompatActivity {

    private EditText etCantidadComida, etMarcaComida, etCantidadComidaComidaMascota;
    private Button btnAgregarComida, btnRegistrarGasto;
    private ListView lvHistorialComida;
    private ListView lvHistorialGastos;
    private TextView tvRecomendacion, tvKilosRestantes;
    private List<String> historialComida,historialGastos;
    private BaseDatos db;
    private double kilosComidaRestantes = 0;
    private int mascotaId = MascotaSeleccionada.getInstance().getIdMascota();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_comida);

        // Inicializar vistas
        etCantidadComida = findViewById(R.id.etCantidadComida);
        etMarcaComida = findViewById(R.id.etMarcaComida);
        btnAgregarComida = findViewById(R.id.btnAgregarComida);
        lvHistorialComida = findViewById(R.id.lvHistorialComida);
        lvHistorialGastos = findViewById(R.id.lvHistorialGastos);
        etCantidadComidaComidaMascota = findViewById(R.id.etCantidadComidaComidaMascota);
        btnRegistrarGasto = findViewById(R.id.btnRegistrarGasto);
        tvRecomendacion = findViewById(R.id.tvRecomendacion);
        tvKilosRestantes = findViewById(R.id.tvKilosRestantes);

        // Inicializar base de datos
        db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        // Obtener datos de la mascota (peso y edad) desde la base de datos
        Mascota mascota = db.mascotaDao().obtenerMascotaPorId(mascotaId);


        // Mostrar recomendación
        tvRecomendacion.setText("Recomendación: El peso de su mascota es " + mascota.getPeso() + " kg y su edad es " + mascota.getEdad() +
                " años, le recomendamos que le de " + calcularComidaRecomendada(mascota.getPeso()) + " kg de comida.");

        // Lista para el historial de comida
        historialComida = new ArrayList<>();
        historialGastos = new ArrayList<>();

        // Configurar el ListView con el historial de comida
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historialComida);
        lvHistorialComida.setAdapter(adapter);

        ArrayAdapter<String> adapterGastos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historialGastos);
        lvHistorialGastos.setAdapter(adapterGastos);

        // Agregar comida
        btnAgregarComida.setOnClickListener(v -> agregarComida());

        // Registrar el gasto de comida
        btnRegistrarGasto.setOnClickListener(v -> registrarGastoComida());
    }

    private void agregarComida() {
        String cantidadComida = etCantidadComida.getText().toString().trim();
        String marcaComida = etMarcaComida.getText().toString().trim();

        if (cantidadComida.isEmpty() || marcaComida.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese todos los datos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double kilosComida = Double.parseDouble(cantidadComida);
        kilosComidaRestantes += kilosComida;

        // Agregar al historial
        historialComida.add("Comida de " + marcaComida + ": " + kilosComida + " kg");

        // Actualizar el contador de kilos
        tvKilosRestantes.setText("Kilos de comida restantes: " + kilosComidaRestantes + " kg");

        // Mostrar mensaje de éxito
        Toast.makeText(this, "Comida agregada exitosamente", Toast.LENGTH_SHORT).show();

        // Actualizar el ListView
        ((ArrayAdapter) lvHistorialComida.getAdapter()).notifyDataSetChanged();
    }

    private void registrarGastoComida() {
        String cantidadComidaComidaMascota = etCantidadComidaComidaMascota.getText().toString().trim();

        if (cantidadComidaComidaMascota.isEmpty()) {
            Toast.makeText(this, "Ingrese la cantidad de comida que consumió la mascota", Toast.LENGTH_SHORT).show();
            return;
        }

        double comidaConsumida = Double.parseDouble(cantidadComidaComidaMascota);
        kilosComidaRestantes -= comidaConsumida;

        // Agregar al historial de gastos
        historialGastos.add("Gasto de comida: " + comidaConsumida + " kg");

        // Actualizar el contador de comida
        tvKilosRestantes.setText("Kilos de comida restantes: " + kilosComidaRestantes + " kg");

        // Verificar si la comida se está agotando
        if (kilosComidaRestantes <= 2) {
            Toast.makeText(this, "La comida de su mascota se está agotando", Toast.LENGTH_LONG).show();
        }
        // Actualizar el ListView de gastos
        ((ArrayAdapter) lvHistorialGastos.getAdapter()).notifyDataSetChanged();
    }

    private double calcularComidaRecomendada(double pesoMascota) {
        return pesoMascota * 0.05; // Ejemplo de cálculo: 5% del peso de la mascota
    }
}

