package com.example.loginsignup.actividades;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsignup.R;

import java.util.Calendar;

public class HistorialClinico extends AppCompatActivity {

    private Spinner spinner;
    private EditText editTextFecha, editTextDiagnostico;
    private Button buttonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_clinico);

        // Inicializa las vistas
        spinner = findViewById(R.id.spinnerSeleccion);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextDiagnostico = findViewById(R.id.editTextDiagnostico);
        buttonBuscar = findViewById(R.id.buttonBuscar);

        // Al hacer clic en fecha, se muestra el calendario
        editTextFecha.setOnClickListener(v -> mostrarCalendario());

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

    // Acción para el botón "Buscar"
    private void buscar() {
        String fecha = editTextFecha.getText().toString();
        String diagnostico = editTextDiagnostico.getText().toString();

        if (spinner.getSelectedItemPosition() == 0 && fecha.isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona una fecha", Toast.LENGTH_SHORT).show();
        } else if (spinner.getSelectedItemPosition() == 1 && diagnostico.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un diagnóstico", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Búsqueda realizada", Toast.LENGTH_SHORT).show();
        }
    }
}
