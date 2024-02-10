package com.example.extrahand2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.extrahand2.R;
import com.example.extrahand2.utils.FirebaseManager;

public class SplashActivity extends AppCompatActivity {


        @Override
        protected void onCreate( Bundle savedInstanceState ) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_splash);
                ScreenMove();

        }
        public void ScreenMove()
        {
                int time =1;
                new Handler().postDelayed(new Runnable(){
                        public void run(){
                                if(FirebaseManager.isSignedIn()){
                                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                                }
                                else{
                                        Intent intent =new Intent(SplashActivity.this,SignInActivity.class);
                                        startActivity(intent);
                                }
                }

                },  time*1000);
        }
}