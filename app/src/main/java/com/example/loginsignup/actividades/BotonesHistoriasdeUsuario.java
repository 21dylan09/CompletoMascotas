package com.example.loginsignup.actividades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsignup.R;

public class BotonesHistoriasdeUsuario extends AppCompatActivity {


    private ImageButton button1, button2, button3, button4, button5, button6, button7, button8; // Declarar los nuevos botones


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botones);

        // Inicializa los botones
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4); // Inicializa el botón 4
        button5 = findViewById(R.id.button5); // Inicializa el botón 5
        button6 = findViewById(R.id.button6); // Inicializa el botón 6
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);


        // Botón para agregar datos a la historia clínica
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, AgregarDatosHistoriaMedica.class));
            }
        });

        // Botón para ver historial clínico
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, HistorialClinico.class));
            }
        });

        // Botón para generar recomendación de alimentación
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, RecomendacionesAlimentacion.class));
            }
        });

        // Botón para mostrar la lista de compras
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Lista de Compras"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, AgregarGasto.class)); // Asegúrate de crear la actividad ListaComprasActivity
            }
        });

        // Botón para mostrar enfermedades crónicas
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Enfermedades Crónicas"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, EnfermedadesCronicasActivity.class)); // Asegúrate de crear la actividad EnfermedadesCronicasActivity
            }
        });

        // Botón para acceso/modificación a restricciones de la mascota
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Acceso/modificación a restricciones de la mascota"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, RestriccionesMascotaActivity.class)); // Asegúrate de crear la actividad RestriccionesMascotaActivity
            }
        });


        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Acceso/modificación a restricciones de la mascota"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, AlergiasMascotaActivity.class));

            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Acción para el botón Notas de la mascota"
                startActivity(new Intent(BotonesHistoriasdeUsuario.this, NotaMascotaActivity.class)); // Asegúrate de crear la actividad RestriccionesMascotaActivity
            }
        });


    }
}
