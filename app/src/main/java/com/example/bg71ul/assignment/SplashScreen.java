package com.example.bg71ul.assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No navbar on splash screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        //  Initialise the application with a splash screen with awaiting thread.

        Thread thread = new Thread(){

            @Override
            public void run(){
                try{
                    sleep(5000);
                    Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(mainMenuIntent);
                    finish();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

        };

        thread.start();


    }
}
