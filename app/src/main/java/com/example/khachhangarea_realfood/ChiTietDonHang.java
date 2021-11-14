package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ChiTietDonHang extends AppCompatActivity {
    RecyclerView rcvDonHangInfo;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    DonHang donHang;
    TextView tvIDDonHang, tvTrangThai, tvHoTen, tvDiaChi, tvSDT, tvTongTien, tvThoiGian;
    ArrayList<DonHangInfo> donHangInfos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataDonHangInfo = intent.getStringExtra("dataDonHang");
            Gson gson = new Gson();
            donHang = gson.fromJson(dataDonHangInfo, DonHang.class);
        }
        LoadImformationDonHang();
        setEvent();
    }

    private void LoadImformationDonHang() {
        tvIDDonHang.setText(donHang.getIDDonHang());
        
        //tvThoiGian.setText(String.valueOf(donHang.getNgayTao()));
        tvTrangThai.setText(donHang.getTrangThai().toString());
        firebase_manager.LoadThongTinKhachHang(tvHoTen, tvDiaChi, tvSDT);
        tvTongTien.setText(String.valueOf(donHang.getTongTien()));
    }

    private void setEvent() {
    }

    private void setControl() {
        tvIDDonHang = findViewById(R.id.tvIDDonHang);
        tvTrangThai = findViewById(R.id.tvTrangThai);
        tvHoTen = findViewById(R.id.tvTenKhachHang);
        tvSDT = findViewById(R.id.tvSoDienThoai);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvTongTien = findViewById(R.id.tvTongTien);
        rcvDonHangInfo = findViewById(R.id.rcvDonHangInfo);
    }
}