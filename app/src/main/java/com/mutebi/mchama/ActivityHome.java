package com.mutebi.mchama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void signup(View view) {

        Intent intent = new Intent(this, signUp.class);
        startActivity(intent);
    }

    public void signin(View view) {
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);

    }
}