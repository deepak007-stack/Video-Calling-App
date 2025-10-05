package com.layout.my_duo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends AppCompatActivity {

    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private final Runnable navigateToSignupRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainThreadHandler.postDelayed(navigateToSignupRunnable, 800);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainThreadHandler.removeCallbacks(navigateToSignupRunnable);
    }
}