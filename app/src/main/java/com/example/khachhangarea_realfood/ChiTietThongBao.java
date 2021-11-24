package com.example.khachhangarea_realfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.ThongBao;
import com.google.gson.Gson;

import java.text.DateFormat;

public class ChiTietThongBao extends AppCompatActivity {
    Firebase_Manager firebase_manager = new Firebase_Manager();
    ThongBao thongBao;
    TextView tvIDThongBao,tvTenKhachHang,tvNgayThongBao,tvNoiDung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_thong_bao);
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String data = intent.getStringExtra("dataNotifications");
            Gson gson = new Gson();
            thongBao = gson.fromJson(data, ThongBao.class);
            LoadInfoThongBao();
        }
        setEvent();
    }

    private void LoadInfoThongBao() {
        tvIDThongBao.setText(thongBao.getIDThongBao());
        tvNoiDung.setText(thongBao.getNoiDung());
        String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(thongBao.getDate());
        tvNgayThongBao.setText(date);
        firebase_manager.LoadTenKhachHang(tvTenKhachHang);
    }

    private void setEvent() {
    }

    private void setControl() {
        tvIDThongBao = findViewById(R.id.tvIDThongBao);
        tvTenKhachHang = findViewById(R.id.tvTenKhachHang);
        tvNgayThongBao = findViewById(R.id.tvNgayThongBao);
        tvNoiDung = findViewById(R.id.tvNoiDung);
    }
}