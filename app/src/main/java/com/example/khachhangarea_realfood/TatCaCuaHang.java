package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.khachhangarea_realfood.adapter.CuaHangAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TatCaCuaHang extends AppCompatActivity {
    CuaHangAdapter cuaHangAdapter;
    ArrayList<CuaHang> cuaHangs;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rcvTatCaCuaHang;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    SearchView svTatCaCuaHang;
    ProgressBar pbTatCaCuaHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tat_ca_cua_hang);
        cuaHangs = new ArrayList<>();
        cuaHangAdapter = new CuaHangAdapter(this, R.layout.list_item_shop, cuaHangs);
        setControl();
        setEvent();
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvTatCaCuaHang.setLayoutManager(linearLayoutManager);
        rcvTatCaCuaHang.setAdapter(cuaHangAdapter);
        LoadItemCuaHang();

        svTatCaCuaHang.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cuaHangAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cuaHangAdapter.getFilter().filter(newText);
                return false;
            }
        });

        cuaHangAdapter.setDelegation(new CuaHangAdapter.ClickItemShopListener() {
            @Override
            public void getInformationShop(CuaHang cuaHang) {
                Intent intent = new Intent(TatCaCuaHang.this, ChiTietCuaHang.class);
                Gson gson = new Gson();
                String data = gson.toJson(cuaHang);
                intent.putExtra("dataCuaHang", data);
                startActivity(intent);
            }
        });
    }

    private void LoadItemCuaHang() {
        firebase_manager.LoadTatCaCuaHang(cuaHangs, cuaHangAdapter,pbTatCaCuaHang);
    }

    private void setControl() {
        rcvTatCaCuaHang = findViewById(R.id.rcvTatCaCuaHang);
        svTatCaCuaHang = findViewById(R.id.searchViewShop);
        pbTatCaCuaHang = findViewById(R.id.pbLoadTimKiemCuaHang);
    }
}