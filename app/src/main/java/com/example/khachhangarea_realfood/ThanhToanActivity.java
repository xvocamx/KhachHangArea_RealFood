package com.example.khachhangarea_realfood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ThanhToanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}