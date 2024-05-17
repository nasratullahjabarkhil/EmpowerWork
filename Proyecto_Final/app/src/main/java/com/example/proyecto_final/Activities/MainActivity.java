package com.example.proyecto_final.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproyect.R;


public class MainActivity extends AppCompatActivity {

    ImageView img;
    TextView nombre, desarrollado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        img = findViewById(R.id.logoImage);
        nombre = findViewById(R.id.nombre);
        desarrollado = findViewById(R.id.desarrolloTxt);

        img.setAnimation(anim1);
        nombre.setAnimation(anim2);
        desarrollado.setAnimation(anim2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                startActivity(intent);
                finish();
                // verificarYRedirigirSesion();
            }
        }, 3000);
    }

}

