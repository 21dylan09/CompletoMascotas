package com.example.loginsignup.actividades;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.UsuarioDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Usuario;

import java.util.Calendar;

public class Signup_Form extends AppCompatActivity {

    private EditText eTNombre, eTApellido, eTEmail, eTContraseña, eTConfirmacionContraseña, eTStreet, eTTelefono;
    private Spinner rolSpinner;
    private RadioGroup generoRadioGroup;
    private Button botonRegistro;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Formulario de registro");
        }

        BaseDatos db = Room.databaseBuilder(getApplicationContext(), BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();
        usuarioDao = db.usuarioDao();

        // Inicialización de vistas
        eTNombre = findViewById(R.id.editTextNombre);
        eTApellido = findViewById(R.id.editTextApellido);
        eTEmail = findViewById(R.id.editTextEmail);
        eTContraseña = findViewById(R.id.editTextContraseña);
        eTConfirmacionContraseña = findViewById(R.id.editTextConfirmacionContraseña);
        eTStreet = findViewById(R.id.editTextStreet);
        eTTelefono = findViewById(R.id.editTextTelefono);
        rolSpinner = findViewById(R.id.roleSpinner);
        generoRadioGroup = findViewById(R.id.radioGroupGender);
        botonRegistro = findViewById(R.id.registerButton);

        // Configurar Spinner de roles
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.role_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        rolSpinner.setAdapter(adapter);
//
//        // Configurar selector de fecha
//        eTTelefono.setOnClickListener(v -> seleccionarFecha());
//
//        // Configurar el botón de registro
//        botonRegistro.setOnClickListener(v -> validarFormulario());
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario(){
        String nombre = eTNombre.getText().toString().trim();
        String apellido = eTApellido.getText().toString().trim();
        String correo = eTEmail.getText().toString().trim();
        String contraseña = eTContraseña.getText().toString().trim();
        String confirmacion = eTConfirmacionContraseña.getText().toString().trim();
        String direccion = eTStreet.getText().toString().trim();
        String telefono = eTTelefono.getText().toString().trim();
        //String rol = rolSpinner.getSelectedItem().toString();

        int generoId = generoRadioGroup.getCheckedRadioButtonId();
        RadioButton generoSeleccionado = findViewById(generoId);
        String genero = generoSeleccionado != null ? generoSeleccionado.getText().toString() : "No definido";

        if(nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || confirmacion.isEmpty() || telefono.isEmpty()){
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseña.equals(confirmacion)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(nombre, apellido, correo, contraseña, telefono, "propietario", System.currentTimeMillis());
        long idUsuario = usuarioDao.insertarUsuario(usuario);

        if (idUsuario > 0) {
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad
        } else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
        }

    }
    // Método para validar y registrar usuario
    private void validarFormulario() {
        String fullName = eTNombre.getText().toString().trim();
        String userName = eTApellido.getText().toString().trim();
        String email = eTEmail.getText().toString().trim();
        String password = eTContraseña.getText().toString().trim();
        String confirmPassword = eTConfirmacionContraseña.getText().toString().trim();
        String street = eTStreet.getText().toString().trim();
        String birthDate = eTTelefono.getText().toString().trim();
        String role = rolSpinner.getSelectedItem().toString();
        String gender = getSelectedGender();

        // Validar que todos los campos estén llenos
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(street) ||
                TextUtils.isEmpty(birthDate) || TextUtils.isEmpty(role) || TextUtils.isEmpty(gender)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar formato de correo
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Correo inválido. Debe contener @gmail.com u otro dominio válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si pasa todas las validaciones, registrar usuario (por ahora, solo mostrar mensaje)
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

        // Limpiar los campos después del registro
        limpiarCampos();
    }

    // Método para verificar si un correo tiene un formato válido
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Método para limpiar los campos después de un registro exitoso
    private void limpiarCampos() {
        eTNombre.setText("");
        eTApellido.setText("");
        eTEmail.setText("");
        eTContraseña.setText("");
        eTConfirmacionContraseña.setText("");
        eTStreet.setText("");
        eTTelefono.setText("");
        rolSpinner.setSelection(0);
        generoRadioGroup.clearCheck();
    }

    // Método para seleccionar fecha de nacimiento
    private void seleccionarFecha() {
        Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            if (!validarEdad(selectedYear, selectedMonth, selectedDayOfMonth)) {
                Toast.makeText(this, "Debes tener más de 18 años", Toast.LENGTH_SHORT).show();
            } else {
                eTTelefono.setText(selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear);
            }
        }, año, mes, dia);

        datePickerDialog.show();
    }

    // Validar si la persona tiene al menos 18 años
    private boolean validarEdad(int año, int mes, int dia) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - año;

        if (mes > today.get(Calendar.MONTH) || (mes == today.get(Calendar.MONTH) && dia > today.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age >= 18;
    }

    // Método para obtener el género seleccionado
    private String getSelectedGender() {
        int selectedId = generoRadioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        } else {
            return "";
        }
    }
}
