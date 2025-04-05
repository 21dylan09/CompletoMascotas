package com.example.loginsignup.actividadesCuidador;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.loginsignup.R;
import com.example.loginsignup.actividadesVeterinario.AgregarDatosHistoriaMedica;
import com.example.loginsignup.actividadesVeterinario.PerfilMascotaActivity;


public class BotonesCuidador extends AppCompatActivity{
    private ImageButton button1, button2;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botones_cuidador);

        // Inicializa los botones
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        // Botón para agregar datos a la historia clínica
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesCuidador.this, AgregarDatosHistoriaMedica.class));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BotonesCuidador.this, PerfilMascotaActivity.class));
            }
        });

    }
}
