package com.example.loginsignup.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.MascotaDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Mascota;

public class RegistroMascotaActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextSexo, editTextEspecie, editTextTipo, editTextEdad, editTextRaza, editTextPeso;
    private Button buttonGuardar;
    private MascotaDao mascotaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_mascota);

        // Inicialización de la base de datos y DAO
        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        mascotaDao = db.mascotaDao();

        // Inicialización de los campos de entrada
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEdad = findViewById(R.id.editTextEdad);
        editTextRaza = findViewById(R.id.editTextRaza);
        editTextPeso = findViewById(R.id.editTextPeso);
        editTextTipo= findViewById(R.id.editTextTipo);
        editTextEspecie=findViewById(R.id.editTextEspecie);
        editTextSexo=findViewById(R.id.editTextSexo);

        // Inicialización del botón guardar
        buttonGuardar = findViewById(R.id.buttonGuardar);
        buttonGuardar.setOnClickListener(v -> guardarMascota());
    }

    // Método para guardar la mascota en la base de datos
    private void guardarMascota() {
        String nombre = editTextNombre.getText().toString();
        String tipo = editTextTipo.getText().toString();
        String especie = editTextEspecie.getText().toString();
        String raza = editTextRaza.getText().toString();
        String sexo = editTextSexo.getText().toString();
        String edadStr = editTextEdad.getText().toString();
        String pesoStr = editTextPeso.getText().toString();

        int id_dueño = 1; // El ID del dueño puede ser proporcionado desde otro lugar

        // Verificación de que los campos no estén vacíos
        if (nombre.isEmpty() || tipo.isEmpty() || especie.isEmpty() || raza.isEmpty() || sexo.isEmpty() || edadStr.isEmpty() || pesoStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversión de edad a int y manejo de posibles excepciones
        int edad = 0;
        try {
            edad = Integer.parseInt(edadStr); // Convierte edad de String a int
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Edad no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversión de peso a double y manejo de posibles excepciones
        double peso = 0;
        try {
            peso = Double.parseDouble(pesoStr); // Convierte peso de String a double
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Peso no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer idEnfermedad = null; // Si no hay enfermedad, puedes dejarlo como null

        // Crear la nueva mascota
        Mascota nuevaMascota = new Mascota(
                nombre,
                tipo,
                peso,
                especie,
                raza,
                sexo,
                edad,
                System.currentTimeMillis(), // Fecha de registro actual
                id_dueño,
                idEnfermedad
        );

        // Guardar la mascota en la base de datos (suponiendo que ya tienes la implementación de mascotaDao)
        mascotaDao.insertarMascota(nuevaMascota);

        Toast.makeText(this, "Mascota registrada con éxito", Toast.LENGTH_SHORT).show();
        finish(); // Cerrar la actividad

        Intent intent = new Intent(RegistroMascotaActivity.this, Mascotas_Form.class);
        intent.putExtra("actualizar", true);
        startActivity(intent);
    }

}

