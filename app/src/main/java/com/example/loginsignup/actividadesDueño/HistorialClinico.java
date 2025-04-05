package com.example.loginsignup.actividadesDueño;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.HistorialDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.HistorialMedico;

import java.util.Calendar;
import java.util.List;

public class HistorialClinico extends AppCompatActivity {

    private Spinner spinner;
    private EditText editTextFecha, editTextDiagnostico;
    private Button buttonBuscar;
    private HistorialDao historialDao;
    private ImageButton boton_atras;
    private TextView tvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_clinico);

        BaseDatos bd = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        historialDao = bd.historiaDao();

        // Inicializa las vistas
        spinner = findViewById(R.id.spinnerSeleccion);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextDiagnostico = findViewById(R.id.editTextDiagnostico);
        buttonBuscar = findViewById(R.id.buttonBuscar);
        boton_atras = findViewById(R.id.btnBack);
        tvTitulo = findViewById(R.id.tvTitle);
        tvTitulo.setText("HISTORIAL");

        // Al hacer clic en fecha, se muestra el calendario
        editTextFecha.setOnClickListener(v -> mostrarCalendario());

        boton_atras.setOnClickListener(v -> {
            startActivity(new Intent(HistorialClinico.this, BotonesHistoriasdeUsuario.class));
        });

        //  spinner que maneja la selección de "Fecha" o "Diagnóstico"
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) { // "Fecha" seleccionado
                    editTextFecha.setVisibility(View.VISIBLE);
                    editTextDiagnostico.setVisibility(View.GONE);
                } else if (position == 1) { // "Diagnóstico" seleccionado
                    editTextFecha.setVisibility(View.GONE);
                    editTextDiagnostico.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hace nada si no se selecciona ninguna opción
            }
        });

        // Configura el botón de búsqueda
        buttonBuscar.setOnClickListener(v -> buscar());
    }

    // Mostrar el calendario cuando se hace clic en el campo de fecha
    private void mostrarCalendario() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            editTextFecha.setText(selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void buscar() {
        String diagnostico = editTextDiagnostico.getText().toString();
        String idMascotaStr = String.valueOf(MascotaSeleccionada.getInstance().getIdMascota());

        // Verifica que ambos campos no estén vacíos
        if (diagnostico.isEmpty() || idMascotaStr.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el ID de la mascota y un diagnóstico", Toast.LENGTH_SHORT).show();
            return;
        }

        int idMascota = Integer.parseInt(idMascotaStr);

        // Realizar la consulta en la base de datos
        List<HistorialMedico> resultados = historialDao.buscarPorIdMascotaYDiagnostico(idMascota, diagnostico);

        if (resultados.isEmpty()) {
            Toast.makeText(this, "No se encontraron registros", Toast.LENGTH_SHORT).show();
        } else {
            mostrarResultados(resultados);
        }
    }

    private void mostrarResultados(List<HistorialMedico> resultados) {
        TextView textViewResultados = findViewById(R.id.textViewResultados);

        StringBuilder builder = new StringBuilder();
        for (HistorialMedico h : resultados) {
            builder.append("Fecha: ").append(h.getFecha()).append("\n");
            builder.append("Diagnóstico: ").append(h.getDiagnostico()).append("\n\n");
            builder.append("Tratamiento: ").append(h.getTratamiento()).append("\n\n");

        }

        textViewResultados.setText(builder.toString());
    }

}

