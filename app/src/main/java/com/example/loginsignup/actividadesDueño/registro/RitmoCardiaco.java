package com.example.loginsignup.actividadesDueño.registro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsignup.R;

import java.util.Random;

public class RitmoCardiaco extends AppCompatActivity {

    private Button btnMedir;
    private TextView tvResultado, tvContador;
    private View viewPulso;
    private GraficadelRitmoCardi pulsoGrafico;

    private Handler handler = new Handler();
    private Random random = new Random();

    private boolean animando = false;
    private int tiempoMedicion = 0;
    private Runnable runnableContador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ritmo_cardiaco);

        btnMedir = findViewById(R.id.btnMedir);
        tvResultado = findViewById(R.id.tvResultado);
        tvContador = findViewById(R.id.tvContador);
        viewPulso = findViewById(R.id.viewPulso);
        pulsoGrafico = findViewById(R.id.pulsoGrafico);

        btnMedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!animando) {
                    iniciarMedicion();
                }
            }
        });
    }

    private void iniciarMedicion() {
        animando = true;
        tiempoMedicion = 0;
        tvResultado.setText("Midiendo...");
        tvContador.setText("Tiempo: 0 s");
        viewPulso.setVisibility(View.VISIBLE);
        pulsoGrafico.iniciarAnimacion();

        // Animación pulso suave
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(viewPulso, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(viewPulso, "scaleY", 1f, 1.5f);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(viewPulso, "alpha", 1f, 0.5f);

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(viewPulso, "scaleX", 1.5f, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(viewPulso, "scaleY", 1.5f, 1f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(viewPulso, "alpha", 0.5f, 1f);

        AnimatorSet pulseUp = new AnimatorSet();
        pulseUp.playTogether(scaleUpX, scaleUpY, fadeOut);
        pulseUp.setDuration(400);
        pulseUp.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet pulseDown = new AnimatorSet();
        pulseDown.playTogether(scaleDownX, scaleDownY, fadeIn);
        pulseDown.setDuration(400);
        pulseDown.setInterpolator(new AccelerateDecelerateInterpolator());

        final AnimatorSet pulse = new AnimatorSet();
        pulse.playSequentially(pulseUp, pulseDown);

        final int totalRepeticiones = 5;
        final int[] contadorRepeticiones = {0};

        pulse.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                contadorRepeticiones[0]++;
                if (contadorRepeticiones[0] < totalRepeticiones) {
                    pulse.start(); // Repetir animación
                } else {
                    terminarMedicion();
                }
            }
        });

        pulse.start();

        // Iniciar contador de tiempo (hasta 5 segundos)
        runnableContador = new Runnable() {
            @Override
            public void run() {
                tiempoMedicion++;
                tvContador.setText("Tiempo: " + tiempoMedicion + " s");
                if (tiempoMedicion < 5) {
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(runnableContador);
    }

    private void terminarMedicion() {
        animando = false;
        viewPulso.setVisibility(View.INVISIBLE);
        pulsoGrafico.detenerAnimacion();

        int ritmoSimulado = generarRitmoAleatorio(70, 140);
        tvResultado.setText("Ritmo cardíaco: " + ritmoSimulado + " latidos por minuto");
    }

    private int generarRitmoAleatorio(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
