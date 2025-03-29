package com.example.loginsignup.actividadesDueño;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesVeterinario.DueñosTodos;
import com.example.loginsignup.baseDatos.dao.UsuarioDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Usuario;

public class Login_Form extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        // Inicialización de vistas
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);

        // Inicializar la base de datos
        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        usuarioDao = db.usuarioDao();
    }
    // Método para manejar el clic del botón "Register"

    public void btn_signupForm(View view) {
        startActivity(new Intent(getApplicationContext(), Signup_Form.class));
    }

    // Método para manejar el clic del botón de login
    public void btn_loginForm(View view) {
        // Obtener los valores de correo y contraseña
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // Verificar si los campos están vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show();
        } else {
            // Verificar si el usuario está en la base de datos
            Usuario usuario = usuarioDao.autenticarUsuario(email, password);

            if (usuario != null) {
                // Si el usuario existe y las credenciales coinciden
                Toast.makeText(this, "Ingreso exitoso", Toast.LENGTH_SHORT).show();
                DueñoSeleccionado.getInstance().setIdDueño(usuario.id_usuario);
                String rol = usuario.tipo_usuario;  // Obtener el tipo de usuario (rol)

                if ("Dueño de mascota".equalsIgnoreCase(rol)) {
                    // Si el rol es "dueño", abrir la actividad de Mascotas
                    startActivity(new Intent(getApplicationContext(), Mascotas_Form.class));
                } else if ("Veterinario".equalsIgnoreCase(rol)) {
                    // Si el rol es "veterinario", abrir la actividad de AgregarGasto
                    startActivity(new Intent(getApplicationContext(), DueñosTodos.class));
                } else if ("Cuidador".equalsIgnoreCase(rol)) {
                    // Si el rol es "cuidador", abrir la actividad AlergiasMascotaActivity
                    startActivity(new Intent(getApplicationContext(), AlergiasMascotaActivity.class));
                } else {
                    // Si el rol es otro o no está definido
                    Toast.makeText(this, "Rol no definido o no soportado", Toast.LENGTH_SHORT).show();
                }

            } else {
                // Si no se encuentra el usuario o las credenciales son incorrectas
                Toast.makeText(this, "Usuario no encontrado o datos no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

