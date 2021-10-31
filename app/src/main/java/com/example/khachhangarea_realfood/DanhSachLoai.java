package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.khachhangarea_realfood.adapter.SanPhamAdapter;
import com.example.khachhangarea_realfood.model.LoaiSanPham;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DanhSachLoai extends AppCompatActivity {
    private RecyclerView rcvLoai;
    private SearchView svLoai;
    private LoaiSanPham loaiSanPham;
    private ArrayList<SanPham> sanPhams;
    private SanPhamAdapter sanPhamAdapter;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar pbLoadLoai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loai_san_pham);
        sanPhams = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(this,R.layout.list_item_food_1,sanPhams);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataLoai = intent.getStringExtra("dataLoai");
            Gson gson = new Gson();
            loaiSanPham = gson.fromJson(dataLoai, LoaiSanPham.class);
        }
        LoadItemLoai();
        setEvent();
    }

    private void LoadItemLoai(){
        mDatabase.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                sanPhams.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    if(sanPham.getIDLoai().equals(loaiSanPham.getiDLoai())){
                        sanPhams.add(sanPham);
                        sanPhamAdapter.notifyDataSetChanged();
                    }
                }
                pbLoadLoai.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvLoai.setLayoutManager(linearLayoutManager);
        rcvLoai.setAdapter(sanPhamAdapter);
        LoadItemLoai();

        svLoai.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sanPhamAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sanPhamAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setControl() {
        rcvLoai = findViewById(R.id.rcvLoai);
        svLoai = findViewById(R.id.searchViewLoai);
        pbLoadLoai = findViewById(R.id.pbLoadLoai);
    }
}