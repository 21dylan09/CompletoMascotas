package com.example.loginsignup.actividades;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsignup.R;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.HistorialDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.HistorialMedico;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AgregarDatosHistoriaMedica extends AppCompatActivity {

    private EditText editTextTipoVacuna, editTextDiagnostico, editTextTratamiento;
    private Button buttonGuardar;
    private TextView editTextFecha;
    private long fechaSeleccionadaTimestamp = 0; // Para almacenar la fecha en formato timestamp
    private HistorialDao historialDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_datos_historia_medica);

        //BaseDatos bd = BaseDatos.getBaseDatos(this);
        BaseDatos bd = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        historialDao = bd.historiaDao();

        // Inicialización de las vistas
        editTextFecha = findViewById(R.id.textViewFecha);
        editTextTipoVacuna = findViewById(R.id.editTextTipoVacuna);
        editTextDiagnostico = findViewById(R.id.editTextDiagnostico);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        // Bloquear la edición manual de la fecha
        editTextFecha.setFocusable(false);
        editTextFecha.setClickable(true);

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
            // Guardar la fecha seleccionada como timestamp
            Calendar fechaSeleccionada = Calendar.getInstance();
            fechaSeleccionada.set(selectedYear, selectedMonth, selectedDayOfMonth);
            fechaSeleccionadaTimestamp = fechaSeleccionada.getTimeInMillis();

            // Formatear la fecha y mostrarla en el EditText
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = formato.format(fechaSeleccionada.getTime());
            editTextFecha.setText(fechaFormateada);
        }, year, month, day);

        datePickerDialog.show();
    }

    // Método para guardar los datos del formulario
    private void guardarDatos() {
        String tipoVacuna = editTextTipoVacuna.getText().toString();
        String diagnostico = editTextDiagnostico.getText().toString();
        String tratamiento = editTextTipoVacuna.getText().toString();

        // Validar que todos los campos estén llenos y que se haya seleccionado una fecha
        if (fechaSeleccionadaTimestamp == 0) {
            Toast.makeText(this, "Por favor, selecciona una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tipoVacuna.isEmpty() || diagnostico.isEmpty() || tratamiento.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto HistorialMedico
        HistorialMedico historial = new HistorialMedico(fechaSeleccionadaTimestamp, diagnostico, tratamiento, MascotaSeleccionada.getInstance().getIdMascota(), System.currentTimeMillis());
        historialDao.insertarHistorial(historial);
        // Aquí podrías insertar historial en la base de datos
        Toast.makeText(this, "Datos guardados con éxito", Toast.LENGTH_SHORT).show();
        finish();  // Cierra la actividad después de guardar los datos
    }
}

