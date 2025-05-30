package com.example.loginsignup.actividadesDueño.registro;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.baseDatos.dao.UsuarioDao;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.Usuario;

import java.util.Calendar;

public class Signup_Form extends AppCompatActivity {

    private EditText eTNombre, eTApellido, eTEmail, eTContraseña, eTConfirmacionContraseña, eTStreet, eTTelefono, eTFecha;
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
        eTFecha = findViewById(R.id.editTextFecha);

        // Configurar el DatePickerDialog para el campo de fecha de nacimiento
        eTFecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Solo se ejecuta si el usuario toca el campo
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showDatePickerDialog();  // Llamamos al método que abre el calendario
                    return true;  // Indica que hemos manejado el toque
                }
                return false;  // Si no, no hacemos nada
            }
        });



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.role_array, R.layout.spinner_item);  // Usamos el layout personalizado

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rolSpinner.setAdapter(adapter);


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
        String rol = rolSpinner.getSelectedItem().toString();



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

        Usuario usuarioExistente = usuarioDao.obtenerUsuarioPorCorreo(correo);
        if(usuarioExistente != null){
            Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Correo inválido. Debe tener un formato correcto (ejemplo@dominio.com)", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(nombre, apellido, correo, contraseña, telefono, rol, System.currentTimeMillis());
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
    // Método para mostrar el DatePickerDialog
    private void showDatePickerDialog() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Signup_Form.this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Mostrar la fecha seleccionada en el EditText
                    String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    eTFecha.setText(selectedDate);  // Establecer la fecha en el campo
                },
                year, month, dayOfMonth
        );

        // Mostrar el dialogo de selección de fecha
        datePickerDialog.show();
    }


}
