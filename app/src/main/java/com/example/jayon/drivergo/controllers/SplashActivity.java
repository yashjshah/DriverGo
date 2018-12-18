package com.example.jayon.drivergo.controllers;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.jayon.drivergo.R;

public class SplashActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        linearLayout=(LinearLayout)findViewById(R.id.LinearLayout);
        linearLayout.setAlpha(0f);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.animate().alpha(1f).setDuration(1000);

        new CountDownTimer(3500,1500){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                startActivity(new Intent(getApplicationContext(),IntroActivity.class));

            }
        }.start();

    }
}
