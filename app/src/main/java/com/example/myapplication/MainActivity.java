package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static int SPLASH = 3000;
    TextView textView;
    ImageView imageView;
    Animation topAnimation, bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        imageView = findViewById(R.id.imageview);
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        textView.setAnimation(bottomAnimation);
        imageView.setAnimation(topAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH);

    }
}