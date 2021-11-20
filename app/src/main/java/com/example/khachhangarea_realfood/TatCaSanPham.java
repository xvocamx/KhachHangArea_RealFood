package com.example.khachhangarea_realfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.khachhangarea_realfood.adapter.CuaHangAdapter;
import com.example.khachhangarea_realfood.adapter.SanPhamAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TatCaSanPham extends AppCompatActivity {
    SanPhamAdapter sanPhamAdapter;
    ArrayList<SanPham> sanPhams;
    GridLayoutManager gridLayoutManager;
    RecyclerView rcvTatCaSanPham;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    SearchView svTatCaSanPham;
    ProgressBar pbTatCaSanPham;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tat_ca_san_pham);
        sanPhams = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(this,R.layout.list_item_food_1,sanPhams);
        setControl();
        setEvent();
    }

    private void setEvent() {
        gridLayoutManager = new GridLayoutManager(this,2);
        rcvTatCaSanPham.setLayoutManager(gridLayoutManager);
        rcvTatCaSanPham.setAdapter(sanPhamAdapter);
        LoadItemSanPham();
        // Tim kiem san pham
        svTatCaSanPham.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        sanPhamAdapter.setDelegation(new SanPhamAdapter.ClickItemFoodListener() {
            @Override
            public void getInformationFood(SanPham sanPham) {
                Intent intent = new Intent(TatCaSanPham.this, ChiTietSanPham.class);
                Gson gson = new Gson();
                String data = gson.toJson(sanPham);
                intent.putExtra("dataSanPham", data);
                startActivity(intent);
            }
        });
    }

    private void LoadItemSanPham() {
        firebase_manager.LoadTatCaSanPham(sanPhams,sanPhamAdapter,pbTatCaSanPham);

    }

    private void setControl() {
        rcvTatCaSanPham = findViewById(R.id.rcvTatCaSanPham);
        pbTatCaSanPham = findViewById(R.id.pbLoadTimKiemSanPham);
        svTatCaSanPham = findViewById(R.id.searchViewFood);
    }
}