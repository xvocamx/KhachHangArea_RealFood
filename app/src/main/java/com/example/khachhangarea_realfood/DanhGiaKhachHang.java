package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.khachhangarea_realfood.adapter.DanhGiaAdapter;
import com.example.khachhangarea_realfood.model.DanhGia;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

public class DanhGiaKhachHang extends AppCompatActivity {
    Firebase_Manager firebase_manager = new Firebase_Manager();
    String IDDanhGia, binhLuan;
    DanhGiaAdapter danhGiaAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DonHangInfo> donHangInfos;
    RecyclerView rcvDanhGiaSanPham;
    DonHang donHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);
        donHangInfos = new ArrayList<>();
        danhGiaAdapter = new DanhGiaAdapter(this,R.layout.list_item_danhgia_sanpham,donHangInfos);
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataDonHangInfo = intent.getStringExtra("dataDonHang");
            Gson gson = new Gson();
            donHang = gson.fromJson(dataDonHangInfo, DonHang.class);
        }
        setEvent();
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvDanhGiaSanPham.setLayoutManager(linearLayoutManager);
        rcvDanhGiaSanPham.setAdapter(danhGiaAdapter);
        LoadItemDanhGia();
    }

    private void LoadItemDanhGia() {
        firebase_manager.mDatabase.child("DonHangInfo").orderByChild("iddonHang").equalTo(donHang.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    donHangInfos.add(donHangInfo);
                    danhGiaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    private void setControl() {
        rcvDanhGiaSanPham = findViewById(R.id.rcvDanhGiaSanPham);
    }
}