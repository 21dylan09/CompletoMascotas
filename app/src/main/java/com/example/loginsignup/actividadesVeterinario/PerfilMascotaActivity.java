package com.example.loginsignup.actividadesVeterinario;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.DueñoSeleccionado;
import com.example.loginsignup.actividadesDueño.MascotaSeleccionada;
import com.example.loginsignup.actividadesDueño.UsuarioSeleccionado;
import com.example.loginsignup.baseDatos.dao.AlergiaDao;
import com.example.loginsignup.baseDatos.dao.EnfermedadCronicaDao;
import com.example.loginsignup.baseDatos.dao.MascotaDao;
import com.example.loginsignup.baseDatos.dao.NotaMascotaDao;
import com.example.loginsignup.baseDatos.dao.RestriccionDao;
import com.example.loginsignup.baseDatos.dao.UsuarioDao;
import com.example.loginsignup.baseDatos.entidades.Alergia;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.EnfermedadCronica;
import com.example.loginsignup.baseDatos.entidades.Mascota;
import com.example.loginsignup.baseDatos.entidades.NotaMascota;
import com.example.loginsignup.baseDatos.entidades.Restriccion;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PerfilMascotaActivity extends AppCompatActivity {

    private TextView tvNombreMascota, tvTipo, tvEspecie, tvRaza, tvSexo, tvEdad, tvPeso, tvRestriccionTitulo, tvEnfermedad;
    private ShapeableImageView ivFotoMascota;
    private LinearLayout layoutNotas, layoutAlergias, layoutRestricciones;
    private TableLayout tablaEnfermedades;
    private RestriccionDao restriccionDao;
    private EditText etPeso;
    private Button btnActualizarPeso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        // Enlaces con los componentes XML
        tvNombreMascota = findViewById(R.id.tvNombreMascota);
        tvTipo = findViewById(R.id.tvTipo);
        tvEspecie = findViewById(R.id.tvEspecie);
        tvRaza = findViewById(R.id.tvRaza);
        tvSexo = findViewById(R.id.tvSexo);
        tvEdad = findViewById(R.id.tvEdad);
        tvPeso = findViewById(R.id.tvPeso);
        ivFotoMascota = findViewById(R.id.ivMascota);
        layoutNotas = findViewById(R.id.layoutNotas);
        layoutAlergias = findViewById(R.id.layoutAlergias);
        tvEnfermedad = findViewById(R.id.tvEnfermedades);
        tablaEnfermedades = findViewById(R.id.tableEnfermedades);
        tvRestriccionTitulo = findViewById(R.id.tvRestriccionesTitulo);
        layoutRestricciones = findViewById(R.id.layoutRestricciones);
        etPeso = findViewById(R.id.etPeso);
        btnActualizarPeso = findViewById(R.id.btnActualizarPeso);



        // Base de datos
        BaseDatos db = Room.databaseBuilder(getApplicationContext(),
        BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();

        MascotaDao mascotaDao = db.mascotaDao();
        NotaMascotaDao notaDao = db.notaMascotaDao();
        AlergiaDao alergiaDao = db.alergiaDao();
        UsuarioDao usuarioDao = db.usuarioDao();
        EnfermedadCronicaDao enfermedadCronicaDao = db.enfermedadCronicaDao();
        restriccionDao = db.restriccionDao();

        int idMascota = MascotaSeleccionada.getInstance().getIdMascota();

        String tipoUsuario = usuarioDao.obtenerTipoDeUsuario(UsuarioSeleccionado.getInstance().getId_Usuario());

        Mascota mascota = mascotaDao.obtenerMascotaPorId(idMascota);
        etPeso.setText(String.valueOf(mascota.getPeso()));

        if (tipoUsuario.equals("Veterinario")){

            tvRestriccionTitulo.setVisibility(View.GONE);
            layoutRestricciones.setVisibility(View.GONE);
            tablaEnfermedades.setVisibility(View.VISIBLE);
            tvEnfermedad.setVisibility(View.VISIBLE);
        }else{
            tvRestriccionTitulo.setVisibility(View.VISIBLE);
            layoutRestricciones.setVisibility(View.VISIBLE);
            tablaEnfermedades.setVisibility(View.GONE);
            tvEnfermedad.setVisibility(View.GONE);
            cargarRestricciones();
        }

        if (mascota != null) {
            tvNombreMascota.setText(mascota.getNombre());
            tvTipo.setText("Animal: " + mascota.getTipo());
            tvEspecie.setText("Especie: " + mascota.getEspecie());
            tvRaza.setText("Raza: " + mascota.getRaza());
            tvSexo.setText("Sexo: " + mascota.getSexo());
            tvEdad.setText("Edad: " + mascota.getEdad() + " años");
        }

        // Mostrar notas
        List<NotaMascota> notas = notaDao.obtenerNotasDeMascota(idMascota);
        for (NotaMascota n : notas) {
            TextView tv = new TextView(this);
            tv.setText("• " + n.getTitulo() + ": " + n.getContenido());
            tv.setTextSize(16);
            tv.setPadding(0, 8, 0, 8);
            layoutNotas.addView(tv);
        }

        List<Alergia> alergias = alergiaDao.obtenerPorIdMascota(idMascota); // Método que deberías tener en tu DAO
        for (Alergia alergia : alergias) {
            TextView tv = new TextView(this);
            tv.setText("- " + alergia.getDescripcion()); // o getDescripcion() si así lo guardaste
            tv.setTextSize(16);
            layoutAlergias.addView(tv);
        }

        List<EnfermedadCronica> enfermedades = enfermedadCronicaDao.obtenerEnfermedadesPorMascota(idMascota);

        for (EnfermedadCronica enfermedad : enfermedades) {
            TableRow row = new TableRow(this);

            TextView tvEnfermedad = new TextView(this);
            tvEnfermedad.setText(enfermedad.getEnfermedad());
            tvEnfermedad.setPadding(8, 8, 8, 8);

            TextView tvMedicamento = new TextView(this);
            tvMedicamento.setText(enfermedad.getMedicamento());
            tvMedicamento.setPadding(8, 8, 8, 8);

            row.addView(tvEnfermedad);
            row.addView(tvMedicamento);
            tablaEnfermedades.addView(row);
        }

        btnActualizarPeso.setOnClickListener(v -> {
            String nuevoPesoStr = etPeso.getText().toString().trim();
            if (!nuevoPesoStr.isEmpty()) {
                try {
                    double nuevoPeso = Double.parseDouble(nuevoPesoStr);
                    mascota.setPeso(nuevoPeso);
                    mascotaDao.actualizarMascota(mascota); // Asegúrate de tener este método
                    Toast.makeText(this, "Peso actualizado", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Peso inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ingresa un peso válido", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cargarRestricciones() {
        layoutRestricciones.removeAllViews(); // Limpiar vistas previas

        // Obtener restricciones de la base de datos
        List<Restriccion> restricciones = restriccionDao.obtenerPorIdMascota(MascotaSeleccionada.getInstance().getIdMascota());

        if (restricciones.isEmpty()) {
            // Si no hay restricciones, mostrar un mensaje
            TextView tvNoRestricciones = new TextView(this);
            tvNoRestricciones.setText("No hay restricciones registradas.");
            tvNoRestricciones.setTextSize(16);
            tvNoRestricciones.setPadding(8, 8, 8, 8);
            layoutRestricciones.addView(tvNoRestricciones);
        } else {
            // Mostrar cada restricción en el layout
            for (Restriccion r : restricciones) {
                TextView tvRestriccion = new TextView(this);
                tvRestriccion.setText("- " + r.getDescripcion());
                tvRestriccion.setTextSize(16);
                tvRestriccion.setPadding(8, 4, 8, 4);
                layoutRestricciones.addView(tvRestriccion);
            }
        }
    }

}


