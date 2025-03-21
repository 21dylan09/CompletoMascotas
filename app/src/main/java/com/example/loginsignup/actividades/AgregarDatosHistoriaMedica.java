package com.example.loginsignup.actividades;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsignup.R;

import java.util.Calendar;

public class AgregarDatosHistoriaMedica extends AppCompatActivity {

    private EditText editTextFecha, editTextTipoVacuna, editTextDiagnostico, editTextTratamiento;
    private Button buttonGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_datos_historia_medica);

        // Inicialización de las vistas
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextTipoVacuna = findViewById(R.id.editTextTipoVacuna);
        editTextDiagnostico = findViewById(R.id.editTextDiagnostico);
        editTextTratamiento = findViewById(R.id.editTextTratamiento);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        // Configurar el botón para mostrar el calendario
        editTextFecha.setOnClickListener(v -> mostrarCalendario());

        // Configurar el botón para guardar los datos
        buttonGuardar.setOnClickListener(v -> guardarDatos());
    }

    // Método para mostrar el calendario
    private void mostrarCalendario() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            // Establecer la fecha seleccionada en el EditText
            editTextFecha.setText(selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day);

        datePickerDialog.show();
    }

    // Método para guardar los datos del formulario
    private void guardarDatos() {
        String fecha = editTextFecha.getText().toString();
        String tipoVacuna = editTextTipoVacuna.getText().toString();
        String diagnostico = editTextDiagnostico.getText().toString();
        String tratamiento = editTextTratamiento.getText().toString();

        // Validar que todos los campos estén llenos
        if (fecha.isEmpty() || tipoVacuna.isEmpty() || diagnostico.isEmpty() || tratamiento.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Guardar los datos (esto puede incluir guardarlos en una base de datos, etc.)
            Toast.makeText(this, "Datos guardados con éxito", Toast.LENGTH_SHORT).show();
            finish();  // Cierra la actividad después de guardar los datos
        }
    }
}
