package com.example.loginsignup.actividadesDueño.registro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.BotonesHistoriasdeUsuario;
import com.example.loginsignup.baseDatos.dao.MascotaDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Mascota;

import java.util.List;

public class Mascotas_Form extends AppCompatActivity {

    private ListView listViewMascotas;
    private MascotaDao mascotaDao;
    private ImageButton boton_atras;
    private TextView tvTitulo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);

        // Inicialización de la base de datos y el DAO
        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        mascotaDao = db.mascotaDao();

        // Configuración del ListView
        listViewMascotas = findViewById(R.id.listViewMascotas);
        boton_atras = findViewById(R.id.btnBack);
        tvTitulo = findViewById(R.id.tvTitle);
        tvTitulo.setText("MASCOTAS");
        // Cargar las mascotas desde la base de datos
        List<Mascota> listaMascotas = mascotaDao.obtenerMascotasDeUsuario(UsuarioSeleccionado.getInstance().getId_Usuario());

        // Configurar el adaptador para el ListView
        MascotasListAdapter adapter = new MascotasListAdapter(this, listaMascotas);
        listViewMascotas.setAdapter(adapter);

        // Configurar el evento de clic en la lista
        listViewMascotas.setOnItemClickListener((parent, view, position, id) -> {
            Mascota mascotaSeleccionada = listaMascotas.get(position);
            MascotaSeleccionada.getInstance().setIdMascota(mascotaSeleccionada.getId_mascota());

            // Acción al hacer clic en una mascota
            startActivity(new Intent(Mascotas_Form.this, BotonesHistoriasdeUsuario.class));
            Toast.makeText(Mascotas_Form.this, "Seleccionaste: " + MascotaSeleccionada.getInstance().getIdMascota(), Toast.LENGTH_SHORT).show();
        });

        // Botón para registrar una nueva mascota
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(Mascotas_Form.this, RegistroMascotaActivity.class));
        });

        boton_atras.setOnClickListener(v -> {
            startActivity(new Intent(Mascotas_Form.this, Login_Form.class));
        });
    }
}

