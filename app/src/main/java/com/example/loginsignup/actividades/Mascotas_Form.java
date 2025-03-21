package com.example.loginsignup.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.MascotaDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Mascota;

import java.util.List;

public class Mascotas_Form extends AppCompatActivity {

    private RecyclerView recyclerViewMascotas;
    private MascotasAdapter mascotasAdapter;
    private MascotaDao mascotaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);

        // Inicialización de la base de datos y el DAO
        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        mascotaDao = db.mascotaDao();

        // Configuración del RecyclerView
        recyclerViewMascotas = findViewById(R.id.recyclerViewMascotas);
        recyclerViewMascotas.setLayoutManager(new LinearLayoutManager(this));

        // Cargar las mascotas desde la base de datos
        List<Mascota> listaMascotas = mascotaDao.obtenerMascotasDeUsuario(1); // Aquí puedes pasar el ID del usuario actual

        // Configurar el adaptador
        mascotasAdapter = new MascotasAdapter(listaMascotas, mascota -> {
            startActivity(new Intent(Mascotas_Form.this, BotonesHistoriasdeUsuario.class));
            // Acción cuando se hace clic en una mascota
            Toast.makeText(Mascotas_Form.this, "Seleccionaste: " + mascota.getNombre(), Toast.LENGTH_SHORT).show();

        });

        recyclerViewMascotas.setAdapter(mascotasAdapter);

        // Botón para registrar una nueva mascota
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            // Navegar a la actividad de registro de mascota
            startActivity(new Intent(Mascotas_Form.this, RegistroMascotaActivity.class));
        });
    }
}
