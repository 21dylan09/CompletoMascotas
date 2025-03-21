package com.example.loginsignup.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
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

                // Si la autenticación es exitosa, navegar a la siguiente actividad
                // Ejemplo: empezar actividad principal
                 startActivity(new Intent(getApplicationContext(),Mascotas_Form.class));
            } else {
                // Si no se encuentra el usuario o las credenciales son incorrectas
                Toast.makeText(this, "Usuario no encontrado o datos no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

