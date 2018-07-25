package com.example.viveksharma.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageView;

public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView iv = (ImageView)findViewById(R.id.iv);
        iv.setImageResource(R.mipmap.ic_launcher_my);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // It will execute after  timer is over
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}