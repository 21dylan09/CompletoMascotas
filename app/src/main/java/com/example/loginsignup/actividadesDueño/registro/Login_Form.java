package com.example.loginsignup.actividadesDueño.registro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesCuidador.DueñosTodosCuidador;
import com.example.loginsignup.actividadesVeterinario.DueñosTodos;
import com.example.loginsignup.baseDatos.dao.UsuarioDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Usuario;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
        BaseDatos db = BaseDatos.getBaseDatos(getApplicationContext());
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
            Executor executor = Executors.newSingleThreadExecutor();

            executor.execute(() -> {

                Usuario usuario = usuarioDao.autenticarUsuario(email, password);

                runOnUiThread(() -> {

                    if (usuario != null) {
                        UsuarioSeleccionado.getInstance().setId_Usuario(usuario.getId_usuario());
                        Toast.makeText(Login_Form.this, "Ingreso exitoso", Toast.LENGTH_SHORT).show();

                        String rol = usuario.tipo_usuario;
                        if ("Dueño de mascota".equalsIgnoreCase(rol)) {
                            startActivity(new Intent(getApplicationContext(), Mascotas_Form.class));
                        } else if ("Veterinario".equalsIgnoreCase(rol)) {
                            startActivity(new Intent(getApplicationContext(), DueñosTodos.class));
                        } else if ("Cuidador".equalsIgnoreCase(rol)) {
                            startActivity(new Intent(getApplicationContext(), DueñosTodosCuidador.class));
                        } else {
                            Toast.makeText(Login_Form.this, "Rol no definido o no soportado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login_Form.this, "Usuario no encontrado o datos no coinciden", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}

