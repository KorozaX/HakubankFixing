package com.climawan.comp6844001.pertemuan5.hakubank.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.climawan.comp6844001.pertemuan5.hakubank.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        startActivity(
                new Intent(this, LoginActivity.class)
        );
    }
}