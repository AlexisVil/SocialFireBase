package com.xtremecorp.socialfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PantallaDeCarga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_de_carga);
        final int DURACION = 2500;

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            //Bloque de ejecucion pasada la duracion
            Intent intent = new Intent(PantallaDeCarga.this, Inicio.class);
            //Inicia la actividad y se pasa a la MainActivity
            startActivity(intent);
            finish();
        },DURACION);

    }
}