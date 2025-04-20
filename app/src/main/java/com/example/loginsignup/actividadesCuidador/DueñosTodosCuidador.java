package com.example.loginsignup.actividadesCuidador;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.registro.DueñoSeleccionado;
import com.example.loginsignup.actividadesVeterinario.DueñoListAdapter;
import com.example.loginsignup.baseDatos.dao.UsuarioDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Usuario;

import java.util.List;

public class DueñosTodosCuidador extends AppCompatActivity {

    private ListView listViewDueños;
    private UsuarioDao usuarioDao;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duenos_veterinario);

        // Inicialización de la base de datos y el DAO
        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        usuarioDao = db.usuarioDao();

        // Configuración del ListView
        listViewDueños = findViewById(R.id.listViewDueños);

        // Cargar todos los dueños desde la base de datos
        List<Usuario> listaDueños = usuarioDao.obtenerUsuarios();  // Obtener todos los usuarios

        // Filtrar para que solo muestre los dueños (si tienes un campo para tipo de usuario, por ejemplo "dueño")
        listaDueños.removeIf(usuario -> !"Dueño de mascota".equalsIgnoreCase(usuario.tipo_usuario));  // Filtra solo los dueños

        // Configurar el adaptador para el ListView
        DueñoListAdapter adapter = new DueñoListAdapter(this, listaDueños);
        listViewDueños.setAdapter(adapter);

        // Configurar el evento de clic en la lista
        listViewDueños.setOnItemClickListener((parent, view, position, id) -> {
            Usuario dueñoSeleccionado = listaDueños.get(position);
            Toast.makeText(DueñosTodosCuidador.this, "Seleccionaste: " + dueñoSeleccionado.nombre + " " + dueñoSeleccionado.apellido, Toast.LENGTH_SHORT).show();

             DueñoSeleccionado.getInstance().setIdDueño(dueñoSeleccionado.id_usuario);
             startActivity(new Intent(DueñosTodosCuidador.this, MascotasVistaCuidador.class));
        });
    }
}

