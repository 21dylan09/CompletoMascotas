package com.example.loginsignup.actividadesDueño;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Contactos;
import com.example.loginsignup.baseDatos.dao.ContactoDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactosConfianza extends AppCompatActivity {

    private EditText inputNombreContacto, inputTelefonoContacto, inputCorreoContacto;
    private Button btnAgregarContacto;
    private ListView listaContactos;
    private ContactosAdapter contactosAdapter;
    private List<Contactos> listaDeContactos;

    private ContactoDao contactoDao;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        // Inicializar vistas
        inputNombreContacto = findViewById(R.id.inputNombreContacto);
        inputTelefonoContacto = findViewById(R.id.inputTelefonoContacto);
        inputCorreoContacto = findViewById(R.id.inputCorreoContacto);
        btnAgregarContacto = findViewById(R.id.btnAgregarContacto);
        listaContactos = findViewById(R.id.listaContactos);

        // Obtener instancia de la base de datos
        BaseDatos db = BaseDatos.getBaseDatos(getApplicationContext());
        contactoDao = db.contactoDao();

        // Crear un ExecutorService para manejar hilos en segundo plano
        executorService = Executors.newSingleThreadExecutor();

        // Inicializar la lista de contactos
        listaDeContactos = new ArrayList<>();

        // Configurar el adaptador para mostrar los contactos
        contactosAdapter = new ContactosAdapter(this, listaDeContactos, contactoDao);
        listaContactos.setAdapter(contactosAdapter);

        // Mostrar los contactos existentes al inicio
        cargarContactos();

        // Agregar un nuevo contacto
        btnAgregarContacto.setOnClickListener(v -> {
            String nombre = inputNombreContacto.getText().toString().trim();
            String correo = inputCorreoContacto.getText().toString().trim();
            String telefono = inputTelefonoContacto.getText().toString().trim();


            if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar si el contacto ya existe por teléfono
            verificarExistenciaContacto(telefono);
        });
    }

    private void cargarContactos() {
        // Realizar la consulta en segundo plano
        executorService.execute(() -> {
            // Obtener los contactos desde la base de datos
            listaDeContactos = contactoDao.obtenerContactos();

            // Ejecutar la actualización de la UI en el hilo principal
            runOnUiThread(() -> {
                // Limpiar la lista actual
                contactosAdapter.clear();

                // Agregar los nuevos contactos a la lista
                contactosAdapter.addAll(listaDeContactos);
                contactosAdapter.notifyDataSetChanged();  // Asegurarse de que la vista se actualice
            });
        });
    }

    private void verificarExistenciaContacto(String telefono) {
        executorService.execute(() -> {
            // Verificar si ya existe un contacto con el mismo teléfono
            Contactos contactoExistente = contactoDao.obtenerContactoPorCorreoOCelular(null, telefono);

            runOnUiThread(() -> {
                if (contactoExistente != null) {
                    // Si existe, mostrar un mensaje de error
                    Toast.makeText(ContactosConfianza.this, "Ya existe un contacto con este teléfono", Toast.LENGTH_SHORT).show();
                } else {
                    // Si no existe, proceder a agregar el nuevo contacto
                    agregarNuevoContacto(telefono);
                }
            });
        });
    }

    private void agregarNuevoContacto(String telefono) {
        String nombre = inputNombreContacto.getText().toString().trim();
        String correo = inputCorreoContacto.getText().toString().trim();
        // Crear un nuevo contacto
        Contactos nuevoContacto = new Contactos(0, nombre, correo, telefono);

        // Insertar el contacto en la base de datos en segundo plano
        executorService.execute(() -> {
            long id = contactoDao.insertarContacto(nuevoContacto);

            // Ejecutar la actualización de la UI en el hilo principal
            runOnUiThread(() -> {
                if (id > 0) {
                    Toast.makeText(this, "Contacto agregado con éxito", Toast.LENGTH_SHORT).show();
                    cargarContactos();  // Recargar la lista de contactos
                } else {
                    Toast.makeText(this, "Error al agregar el contacto", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cerrar el ExecutorService cuando ya no lo necesites
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
