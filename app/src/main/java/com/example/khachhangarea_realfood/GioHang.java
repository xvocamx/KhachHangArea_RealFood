package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.khachhangarea_realfood.adapter.CuaHangAdapter;
import com.example.khachhangarea_realfood.adapter.GioHangAdapter;
import com.example.khachhangarea_realfood.adapter.ItemGioHangAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class GioHang extends AppCompatActivity {
    private LinearLayout lnGhiChu;
    private EditText edtGhiChu;
    private GioHangAdapter gioHangAdapter;
    private ArrayList<DonHangInfo> donHangInfos;
    private RecyclerView rcvGioHang;
    private LinearLayoutManager linearLayoutManager;
    private DonHangInfo donHangInfo;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        donHangInfos = new ArrayList<>();
        donHangInfo = new DonHangInfo();
        gioHangAdapter = new GioHangAdapter(this,R.layout.list_item_giohang_sanpham,donHangInfos);
        setControl();
        setEvent();

    }

    private void LoadGioHang(){
        mDatabase.child("DonHangInfo").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangInfos.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    donHangInfos.add(donHangInfo);
                }
                gioHangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvGioHang.setLayoutManager(linearLayoutManager);
        rcvGioHang.setAdapter(gioHangAdapter);
        LoadGioHang();


        lnGhiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtGhiChu.getVisibility() == v.GONE){
                    edtGhiChu.setVisibility(View.VISIBLE);
                }
                else {
                    edtGhiChu.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setControl() {
        lnGhiChu = findViewById(R.id.lnThemGhiChu);
        edtGhiChu = findViewById(R.id.edtGhiChu);
        rcvGioHang = findViewById(R.id.rcvGioHang);
    }
}