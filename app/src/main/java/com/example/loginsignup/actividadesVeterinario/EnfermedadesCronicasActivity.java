package com.example.loginsignup.actividadesVeterinario;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.MascotaSeleccionada;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.EnfermedadCronica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnfermedadesCronicasActivity extends AppCompatActivity {

    private Spinner spinnerEnfermedades, spinnerEnfermedadesGuardadas;
    private EditText etPesoMascota;
    private TextView tvDosisRecomendada, tvEnfermedades;
    private Button btnGuardar, btnEliminar;
    private BaseDatos db;
    private int mascotaId = MascotaSeleccionada.getInstance().getIdMascota(); // ID de la mascota (puedes cambiarlo dinámicamente)

    // Mapeo de enfermedades a medicamentos y dosis por kg
    private final Map<String, String> enfermedadMedicamento = new HashMap<String, String>() {{
        put("Diabetes", "Insulina - 0.5 UI/kg cada 12h");
        put("Insuficiencia Renal", "Suplemento Renal - 1 ml/kg cada 24h");
        put("Artritis", "Meloxicam - 0.1 mg/kg cada 24h");
    }};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enfermedades_cronicas);

        spinnerEnfermedades = findViewById(R.id.spinnerEnfermedades);
        spinnerEnfermedadesGuardadas = findViewById(R.id.spinnerEnfermedadesGuardadas);
        etPesoMascota = findViewById(R.id.etPesoMascota);
        tvDosisRecomendada = findViewById(R.id.tvDosisRecomendada);
        tvEnfermedades = findViewById(R.id.tvEnfermedades);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Inicializar la base de datos
        db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        // Configurar el Spinner de enfermedades
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, enfermedadMedicamento.keySet().toArray(new String[0]));
        spinnerEnfermedades.setAdapter(adapter);

        // Obtener el peso de la mascota desde la base de datos
        obtenerPesoMascota();

        // Cargar enfermedades guardadas
        cargarEnfermedades();

        // Configurar el botón de guardar
        btnGuardar.setOnClickListener(v -> calcularYGuardar());

        // Configurar el botón de eliminar enfermedad
        btnEliminar.setOnClickListener(v -> eliminarEnfermedad());
    }

    private void obtenerPesoMascota() {
        // Obtener el peso desde la base de datos
        float peso = db.mascotaDao().obtenerPeso(mascotaId);
        etPesoMascota.setText(String.valueOf(peso));
    }

    private void calcularYGuardar() {
        String enfermedadSeleccionada = spinnerEnfermedades.getSelectedItem().toString();
        String pesoTexto = etPesoMascota.getText().toString().trim();

        if (pesoTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el peso de la mascota", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si la enfermedad ya está registrada
        if (existeEnfermedad(mascotaId, enfermedadSeleccionada)) {
            Toast.makeText(this, "Esta enfermedad ya está registrada para esta mascota", Toast.LENGTH_SHORT).show();
            return;
        }

        double peso = Double.parseDouble(pesoTexto);
        String medicamentoBase = enfermedadMedicamento.get(enfermedadSeleccionada);
        String[] partes = medicamentoBase.split(" - ");
        String medicamento = partes[0];
        String dosisBase = partes[1];

        // Calcular dosis real
        String dosisCalculada = dosisBase.replace("kg", String.format("%.2f", peso));

        // Mostrar en la UI
        tvDosisRecomendada.setText("Dosis recomendada: " + dosisCalculada);

        // Guardar en la base de datos
        EnfermedadCronica enfermedad = new EnfermedadCronica(mascotaId, enfermedadSeleccionada, medicamento, dosisCalculada);
        db.enfermedadCronicaDao().insertarEnfermedad(enfermedad);

        Toast.makeText(this, "Tratamiento guardado", Toast.LENGTH_SHORT).show();

        // Recargar enfermedades
        cargarEnfermedades();
    }

    // Método para verificar si la enfermedad ya existe en la base de datos
    private boolean existeEnfermedad(int mascotaId, String enfermedad) {
        List<EnfermedadCronica> listaEnfermedades = db.enfermedadCronicaDao().obtenerEnfermedadesPorMascota(mascotaId);
        for (EnfermedadCronica e : listaEnfermedades) {
            if (e.getEnfermedad().equalsIgnoreCase(enfermedad) && e.getId_mascota() == MascotaSeleccionada.getInstance().getIdMascota()) {
                return true;
            }
        }
        return false;
    }

    private void cargarEnfermedades() {
        // Obtener la lista de enfermedades de la mascota desde la BD
        List<EnfermedadCronica> listaEnfermedades = db.enfermedadCronicaDao().obtenerEnfermedadesPorMascota(MascotaSeleccionada.getInstance().getIdMascota());

        if (listaEnfermedades.isEmpty()) {
            tvEnfermedades.setText("Sin enfermedades registradas");
        } else {
            // Extraer nombres de enfermedades y mostrarlas
            List<String> nombresEnfermedades = new ArrayList<>();
            StringBuilder enfermedadesTexto = new StringBuilder();

            for (EnfermedadCronica enfermedad : listaEnfermedades) {
                nombresEnfermedades.add(enfermedad.getEnfermedad());
                enfermedadesTexto.append(enfermedad.getEnfermedad()).append(" - ").append(enfermedad.getMedicamento())
                        .append(": ").append(enfermedad.getDosis()).append("\n");
            }

            // Mostrar en el TextView
            tvEnfermedades.setText(enfermedadesTexto.toString());

            // Configurar el Spinner con las enfermedades obtenidas
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombresEnfermedades);
            spinnerEnfermedadesGuardadas.setAdapter(adapter);
        }
    }

    private void eliminarEnfermedad() {
        String enfermedadSeleccionada = spinnerEnfermedadesGuardadas.getSelectedItem().toString();

        if (enfermedadSeleccionada != null) {
            db.enfermedadCronicaDao().eliminarEnfermedad(mascotaId, enfermedadSeleccionada);
            Toast.makeText(this, "Enfermedad eliminada", Toast.LENGTH_SHORT).show();

            // Recargar enfermedades
            cargarEnfermedades();
        } else {
            Toast.makeText(this, "Seleccione una enfermedad a eliminar", Toast.LENGTH_SHORT).show();
        }
    }
}


