package com.example.loginsignup.actividadesVeterinario;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.MascotaSeleccionada;
import com.example.loginsignup.baseDatos.dao.AlergiaDao;
import com.example.loginsignup.baseDatos.dao.EnfermedadCronicaDao;
import com.example.loginsignup.baseDatos.dao.MascotaDao;
import com.example.loginsignup.baseDatos.dao.NotaMascotaDao;
import com.example.loginsignup.baseDatos.entidades.Alergia;
import com.example.loginsignup.baseDatos.entidades.BaseDatos;
import com.example.loginsignup.baseDatos.entidades.EnfermedadCronica;
import com.example.loginsignup.baseDatos.entidades.Mascota;
import com.example.loginsignup.baseDatos.entidades.NotaMascota;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PerfilMascotaActivity extends AppCompatActivity {

    private TextView tvNombreMascota, tvTipo, tvEspecie, tvRaza, tvSexo, tvEdad, tvPeso;
    private ShapeableImageView ivFotoMascota;
    private LinearLayout layoutNotas, layoutAlergias;
    private TableLayout tablaEnfermedades;

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
        tablaEnfermedades = findViewById(R.id.tableEnfermedades);

        // Base de datos
        BaseDatos db = Room.databaseBuilder(getApplicationContext(),
                BaseDatos.class, "aplicacion_db").allowMainThreadQueries().build();

        MascotaDao mascotaDao = db.mascotaDao();
        NotaMascotaDao notaDao = db.notaMascotaDao();
        AlergiaDao alergiaDao = db.alergiaDao();
        EnfermedadCronicaDao enfermedadCronicaDao = db.enfermedadCronicaDao();

        int idMascota = MascotaSeleccionada.getInstance().getIdMascota();

        Mascota mascota = mascotaDao.obtenerMascotaPorId(idMascota);
        if (mascota != null) {
            tvNombreMascota.setText(mascota.getNombre());
            tvTipo.setText("Animal: " + mascota.getTipo());
            tvEspecie.setText("Especie: " + mascota.getEspecie());
            tvRaza.setText("Raza: " + mascota.getRaza());
            tvSexo.setText("Sexo: " + mascota.getSexo());
            tvEdad.setText("Edad: " + mascota.getEdad() + " años");
            tvPeso.setText("Peso: " + mascota.getPeso());
            // Cargar imagen si existe
            //if (mascota.getFoto() != null && mascota.getFoto().length > 0) {
                //ivFotoMascota.setImageBitmap(BitmapFactory.decodeByteArray(
                      //  mascota.getFoto(), 0, mascota.getFoto().length));
            //}
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
    }
}


