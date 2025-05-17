package com.example.loginsignup.actividadesDueño.registro;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginsignup.R;

import java.util.Random;

public class RuletadeMensajes extends AppCompatActivity {

    private ImageView ruleta;
    private Button btnGirar, btnCompartir;
    private TextView tvConsejo;

    private Random random = new Random();
    private int ultimoGrado = 0;

    private String[] consejos = {
            "Recuerda darle agua fresca varias veces al día.",
            "Un paseo diario mejora el ánimo y salud de tu mascota.",
            "Cepilla el pelaje para evitar nudos y caída excesiva.",
            "Los juguetes estimulan la mente y previenen el aburrimiento.",
            "Revisa las patas para detectar heridas o suciedad.",
            "Dale cariño y atención; el vínculo emocional es clave.",
            "No olvides las vacunas al día para protegerla.",
            "Un ambiente limpio reduce alergias e infecciones.",
            "Controla el peso para evitar problemas de salud.",
            "Presta atención a cambios de comportamiento."
    };

    private String consejoActual = ""; // Guardamos el consejo que salió

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruleta_mensaje);

        ruleta = findViewById(R.id.ruleta);
        btnGirar = findViewById(R.id.btnGirar);
        btnCompartir = findViewById(R.id.btnCompartir);
        tvConsejo = findViewById(R.id.tvConsejo);

        btnGirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girarRuleta();
            }
        });

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compartirConsejo();
            }
        });
    }

    private void girarRuleta() {
        btnGirar.setEnabled(false);
        btnCompartir.setVisibility(View.GONE);

        int gradosAleatorios = random.nextInt(360 * 4) + 360 * 4;

        RotateAnimation rotate = new RotateAnimation(
                ultimoGrado,
                ultimoGrado + gradosAleatorios,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(4000);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new DecelerateInterpolator());

        rotate.setAnimationListener(new RotateAnimation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) { }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                ultimoGrado = (ultimoGrado + gradosAleatorios) % 360;
                mostrarConsejoSegunGrado(ultimoGrado);

                btnGirar.setEnabled(true);
                btnCompartir.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) { }
        });

        ruleta.startAnimation(rotate);
    }

    private void mostrarConsejoSegunGrado(int grado) {
        int sector = grado / 36;
        int indiceConsejo = (consejos.length - 1) - sector;
        if (indiceConsejo < 0) {
            indiceConsejo = 0;
        }

        consejoActual = consejos[indiceConsejo];
        tvConsejo.setText(consejoActual);
    }

    private void compartirConsejo() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String textoCompartir = "Consejo para papás de mascotas: " + consejoActual + " 🐾";
        intent.putExtra(Intent.EXTRA_TEXT, textoCompartir);
        startActivity(Intent.createChooser(intent, "Compartir consejo via"));
    }
}


