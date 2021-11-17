package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.khachhangarea_realfood.adapter.SanPhamAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SearchViewSanPham extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView rcvSanPham;
    private SanPhamAdapter sanPhamAdapter;
    private ArrayList<SanPham> sanPhams;
    private DatabaseReference mDatabase;
    private GridLayoutManager gridLayoutManager;
    private ProgressBar pbLoadTimKiemSanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_san_pham);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sanPhams = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(this,R.layout.list_item_food_1,sanPhams);
        setControl();

        setEvent();
    }

    private void setEvent() {
        gridLayoutManager = new GridLayoutManager(this,2);
        rcvSanPham.setLayoutManager(gridLayoutManager);
        rcvSanPham.setAdapter(sanPhamAdapter);
        LoadSanPham();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void LoadSanPham(){
        mDatabase.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
                if (getIntent() != null && getIntent().getExtras() != null) {
                    Intent intent = getIntent();
                    String data = intent.getStringExtra("dataTimKiem");
                    searchView.setQuery(data,true);
                    sanPhamAdapter.getFilter().filter(data);
                }
                pbLoadTimKiemSanPham.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        rcvSanPham = findViewById(R.id.rcvSanPham);
        searchView = findViewById(R.id.searchViewFood);
        pbLoadTimKiemSanPham = findViewById(R.id.pbLoadTimKiemSanPham);
        sanPhamAdapter.setDelegation(new SanPhamAdapter.ClickItemFoodListener() {
            @Override
            public void getInformationFood(SanPham sanPham) {
                Intent intent = new Intent(SearchViewSanPham.this, ChiTietSanPham.class);
                Gson gson = new Gson();
                String data = gson.toJson(sanPham);
                intent.putExtra("dataSanPham", data);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}