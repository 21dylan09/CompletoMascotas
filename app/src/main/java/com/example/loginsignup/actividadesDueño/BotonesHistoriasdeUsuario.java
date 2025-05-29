package com.example.loginsignup.actividadesDueño;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.*;
import com.example.loginsignup.actividadesDueño.Geolocalizacion.ContactosConfianza;
import com.example.loginsignup.actividadesDueño.Geolocalizacion.DuenoMapaActivity;
import com.example.loginsignup.actividadesDueño.Geolocalizacion.ObtenerUbicacion;
import com.example.loginsignup.actividadesDueño.Geolocalizacion.PaseoMascotaActivity;
import com.example.loginsignup.actividadesDueño.Geolocalizacion.VerRecorridoActivity;
import com.example.loginsignup.actividadesDueño.Geolocalizacion.ZonasSeguras;
import com.example.loginsignup.actividadesDueño.registro.Mascotas_Form;
import com.example.loginsignup.actividadesDueño.registro.RitmoCardiaco;
import com.example.loginsignup.actividadesDueño.registro.RuletadeMensajes;
import com.example.loginsignup.actividadesDueño.veterinaria.MapsActivity;
import com.example.loginsignup.actividadesVeterinario.PerfilMascotaActivity;

public class BotonesHistoriasdeUsuario extends AppCompatActivity {


    private ImageButton button1, button2, button3, button4, button5, button6, button7, button9, button10, button11, button12, button13, button14, button15; // Declarar los nuevos botones
    private ImageButton boton_atras, button8;
    private TextView tvTitulo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botones);

        // Inicializa los botones
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        boton_atras = findViewById(R.id.btnBack);
        tvTitulo = findViewById(R.id.tvTitle);
        tvTitulo.setText("OPCIONES");
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button10 = findViewById(R.id.button10);
        button11 = findViewById(R.id.button11);
        button12 = findViewById(R.id.button12);
        button13 = findViewById(R.id.button13);
        button14 = findViewById(R.id.button14);
        button15 = findViewById(R.id.button15);

        // Botón para recomendacion alimentacion
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, PerfilMascotaActivity.class));
            }
        });

        // Botón para ver historial clínico
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, HistorialClinico.class));
            }
        });

        // Botón para generar restriccion mascota
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, RestriccionesMascotaActivity.class));
            }
        });

        // Botón para agregar gastos
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Lista de Compras"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, AgregarGasto.class));
            }
        });

        // Botón para notas de la mascota
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Enfermedades Crónicas"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, NotaMascotaActivity.class));
            }
        });

        // Botón para registro de comida
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Enfermedades Crónicas"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, ComidaMascotaActivity.class));
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(BotonesHistoriasdeUsuario.this, ObtenerUbicacion.class));
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, ObtenerUbicacion.class));
            }
        });

        boton_atras.setOnClickListener(v -> {
            startActivity(new Intent(BotonesHistoriasdeUsuario.this, Mascotas_Form.class));
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Recordatorio de Alimentacion"
                //startActivity(new Intent(BotonesHistoriasdeUsuario.this, RecordatoriosAlimentacion.class));
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, RecordatoriosAlimentacion.class));

            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Recordatorio de Alimentacion"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, ZonasSeguras.class));
            }
        });

        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Contactos de confianza"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, ContactosConfianza.class));
            }
        });

        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Contactos de confianza"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, DuenoMapaActivity.class));
            }
        });
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Contactos de confianza"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, RitmoCardiaco.class));
            }
        });

        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Contactos de confianza"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, MapsActivity.class));
            }
        });

        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Contactos de confianza"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, RuletadeMensajes.class));
            }
        });
        button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Contactos de confianza"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, VerRecorridoActivity.class));
            }
        });

    }
}
