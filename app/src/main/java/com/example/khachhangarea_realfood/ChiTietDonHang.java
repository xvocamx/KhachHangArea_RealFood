package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khachhangarea_realfood.adapter.ThanhToanAdapter;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;

public class ChiTietDonHang extends AppCompatActivity {
    RecyclerView rcvDonHangInfo;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    DonHang donHang;
    TextView tvIDDonHang, tvTrangThai, tvHoTen, tvDiaChi, tvSDT, tvTongTien, tvThoiGian;
    ArrayList<DonHangInfo> donHangInfos;
    LinearLayoutManager linearLayoutManager;
    ThanhToanAdapter thanhToanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        donHangInfos = new ArrayList<>();
        thanhToanAdapter = new ThanhToanAdapter(this, R.layout.list_item_thanhtoan_sanpham, donHangInfos);
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataDonHangInfo = intent.getStringExtra("dataDonHang");
            Gson gson = new Gson();
            donHang = gson.fromJson(dataDonHangInfo, DonHang.class);
            LoadImformationDonHang();
        }
        setEvent();
    }

    private void LoadImformationDonHang() {
        tvIDDonHang.setText(donHang.getIDDonHang().substring(0, 15));
        String date = DateFormat.getDateInstance(DateFormat.SHORT).format(donHang.getNgayTao());
        tvThoiGian.setText(date);
        tvTrangThai.setText(firebase_manager.GetStringTrangThaiDonHang(donHang.getTrangThai()));
        tvSDT.setText(donHang.getSoDienThoai());
        tvDiaChi.setText(donHang.getDiaChi());
        firebase_manager.LoadTenKhachHang(tvHoTen);
        tvTongTien.setText(String.valueOf(donHang.getTongTien()));
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvDonHangInfo.setLayoutManager(linearLayoutManager);
        rcvDonHangInfo.setAdapter(thanhToanAdapter);
        LoadItemDonHang();
    }

    private void LoadItemDonHang() {
        firebase_manager.mDatabase.child("DonHangInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    if (donHang.getIDDonHang().equals(donHangInfo.getIDDonHang())) {
                        donHangInfos.add(donHangInfo);
                        thanhToanAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setControl() {
        tvIDDonHang = findViewById(R.id.tvIDDonHang);
        tvTrangThai = findViewById(R.id.tvTrangThai);
        tvHoTen = findViewById(R.id.tvTenKhachHang);
        tvSDT = findViewById(R.id.tvSoDienThoai);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvTongTien = findViewById(R.id.tvTongTien);
        tvThoiGian = findViewById(R.id.tvThoiGian);
        rcvDonHangInfo = findViewById(R.id.rcvDonHangInfo);
    }
}