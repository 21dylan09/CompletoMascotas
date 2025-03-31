package com.example.loginsignup.actividadesVeterinario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.loginsignup.R;
import com.example.loginsignup.actividadesDueño.AgregarDatosHistoriaMedica;
import com.example.loginsignup.actividadesDueño.AlergiasMascotaActivity;
import com.example.loginsignup.actividadesDueño.EnfermedadesCronicasActivity;
import com.example.loginsignup.actividadesDueño.HistorialClinico;


public class BotonesVeterinario extends AppCompatActivity {
    private ImageButton button1, button2, button3, button4; // Declarar los nuevos botones


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botones_veterinario);

        // Inicializa los botones
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4); // Inicializa el botón 4


        // Botón para agregar datos a la historia clínica
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesVeterinario.this, AgregarDatosHistoriaMedica.class));
            }
        });

        // Botón para ver historial clínico
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesVeterinario.this, HistorialClinico.class));
            }
        });

        // Botón para generar recomendación de alimentación
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesVeterinario.this, EnfermedadesCronicasActivity.class));
            }
        });

        // Botón para mostrar la lista de compras
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón "Lista de Compras"
                startActivity(new Intent(BotonesVeterinario.this, AlergiasMascotaActivity.class)); // Asegúrate de crear la actividad ListaComprasActivity
            }
        });
    }
}
