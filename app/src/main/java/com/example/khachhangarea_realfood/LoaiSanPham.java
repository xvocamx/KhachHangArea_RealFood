package com.example.khachhangarea_realfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

public class LoaiSanPham extends AppCompatActivity {
    RecyclerView rcvLoai;
    SearchView svLoai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_san_pham);
        setControl();
        setEvent();
    }

    private void setEvent() {

    }

    private void setControl() {
        rcvLoai = findViewById(R.id.rcvLoai);
        svLoai = findViewById(R.id.searchViewLoai);
    }
}