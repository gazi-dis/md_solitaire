package com.example.md_solitaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Acilis_Ekrani extends AppCompatActivity {

    ImageView image;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis_ekrani);

        image = findViewById(R.id.imageViewAcilis);
        title = findViewById(R.id.textViewAcilis);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.acilis);
        title.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =  new Intent(Acilis_Ekrani.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        }, 5000);
    }
}