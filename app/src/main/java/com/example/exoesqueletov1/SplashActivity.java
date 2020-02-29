package com.example.exoesqueletov1;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exoesqueletov1.clases.Authentication;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class SplashActivity extends AppCompatActivity {

    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        arranque ();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!authentication.verificar()) {
            Thread timer = new Thread() {
                public void run () {
                    try { sleep(3500); }
                    catch (Exception ignored){ }
                    finally {
                        startActivity(new Intent(SplashActivity.this, LogInUpActivity.class));
                        finish();
                    }
                }
            };
            timer.start();
        }
    }

    private void arranque() {
        ImageView titulo = findViewById(R.id.titulo_image);
        ImageView logo = findViewById(R.id.logo_image);
        ProgressBar progressBar = findViewById(R.id.progress_circular);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.transition);
        titulo.startAnimation(animation);
        logo.startAnimation(animation);
        progressBar.startAnimation(animation);
        authentication = new Authentication();
    }
}
