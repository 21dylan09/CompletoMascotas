package com.example.loginsignup.actividadesDueño;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.List;
import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.NotaMascotaDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.NotaMascota;


public class NotaMascotaActivity extends AppCompatActivity {

    private NotaMascotaDao notaMascotaDao;
    private ListView listViewNotas;
    private NotasMascotaAdapter adapter;
    private int idMascotaSeleccionada;
    private ImageButton boton_atras;
    private TextView tvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_notas_mascota);

        // Obtener ID de la mascota seleccionada
        idMascotaSeleccionada = MascotaSeleccionada.getInstance().getIdMascota();

        // Inicializar base de datos y DAO
        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db")
                .allowMainThreadQueries()
                .build();
        notaMascotaDao = db.notaMascotaDao();

        // Configurar UI
        listViewNotas = findViewById(R.id.listViewNotas);
        EditText editTitulo = findViewById(R.id.editTitulo);
        EditText editContenido = findViewById(R.id.editContenido);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        boton_atras = findViewById(R.id.btnBack);
        tvTitulo = findViewById(R.id.tvTitle);
        tvTitulo.setText("NOTAS");

        boton_atras.setOnClickListener(v -> {
            startActivity(new Intent(NotaMascotaActivity.this, BotonesHistoriasdeUsuario.class));
        });

        // Cargar notas existentes
        cargarNotas();

        // Guardar nueva nota
        btnGuardar.setOnClickListener(v -> {
            String titulo = editTitulo.getText().toString();
            String contenido = editContenido.getText().toString();

            if (!titulo.isEmpty() && !contenido.isEmpty()) {
                NotaMascota nuevaNota = new NotaMascota(idMascotaSeleccionada, titulo, contenido);
                notaMascotaDao.insertarNota(nuevaNota);
                cargarNotas(); // Recargar lista
                editTitulo.setText("");
                editContenido.setText("");
                Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarNotas() {
        List<NotaMascota> listaNotas = notaMascotaDao.obtenerNotasDeMascota(idMascotaSeleccionada);
        adapter = new NotasMascotaAdapter(this, listaNotas);
        listViewNotas.setAdapter(adapter);
    }
}
