package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.khachhangarea_realfood.adapter.SanPhamAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchViewSanPham extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView rcvSanPham;
    private SanPhamAdapter sanPhamAdapter;
    private ArrayList<SanPham> sanPhams;
    private Firebase_Manager firebase_manager = new Firebase_Manager();
    private GridLayoutManager gridLayoutManager;
    private ProgressBar pbLoadTimKiemSanPham;
    private TextView tvLoc;
    private LinearLayout lnLoc;
    private Button btnMoiNhat, btnBanChay;
    private Spinner spLocGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_san_pham);
        sanPhams = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(this, R.layout.list_item_food_1, sanPhams);
        setControl();

        setEvent();
    }

    private void setEvent() {
        gridLayoutManager = new GridLayoutManager(this, 2);
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

        tvLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lnLoc.getVisibility() == View.GONE) {
                    lnLoc.setVisibility(View.VISIBLE);
                } else {
                    lnLoc.setVisibility(View.GONE);
                }
            }
        });
        btnMoiNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadSanPhamMoiNhat();
            }
        });
        btnBanChay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadSanPhamBanChay();
            }
        });
        spLocGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        LoadSanPhamThapDenCao();
                        break;
                    case 2:
                        LoadSanPhamCaoDenThap();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnMoiNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadSanPhamMoiNhat();
            }
        });
    }

    private void LoadSanPhamCaoDenThap() {
        Collections.sort(sanPhams, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham o1, SanPham o2) {
                return Integer.parseInt(o1.getGia()) > Integer.parseInt(o2.getGia()) ? -1 : 1;

            }
        });
        sanPhamAdapter.notifyDataSetChanged();
    }

    private void LoadSanPhamThapDenCao() {
        Collections.sort(sanPhams, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham o1, SanPham o2) {
                return Integer.parseInt(o1.getGia()) < Integer.parseInt(o2.getGia()) ? -1 : 1;
            }
        });
        sanPhamAdapter.notifyDataSetChanged();
    }

    private void LoadSanPhamBanChay() {
        Collections.sort(sanPhams, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham o1, SanPham o2) {
                if (o1.getSoLuongBanDuoc() < o2.getSoLuongBanDuoc()) {
                    return 1;
                }
                if (o1.getSoLuongBanDuoc() > o2.getSoLuongBanDuoc()) {
                    return -1;
                }

                return 0;
            }
        });
        sanPhamAdapter.notifyDataSetChanged();
    }

    private void LoadSanPhamMoiNhat() {
        Collections.sort(sanPhams, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham o1, SanPham o2) {
                return o1.getNgayTao().getTime() < o2.getNgayTao().getTime() ? -1 : 1;
            }
        });
        sanPhamAdapter.notifyDataSetChanged();
    }

    private void LoadSanPham() {
        firebase_manager.mDatabase.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
                if (getIntent() != null && getIntent().getExtras() != null) {
                    Intent intent = getIntent();
                    String data = intent.getStringExtra("dataTimKiem");
                    searchView.setQuery(data, true);
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
        lnLoc = findViewById(R.id.lnLoc);
        tvLoc = findViewById(R.id.tvLoc);
        btnBanChay = findViewById(R.id.btnBanChay);
        btnMoiNhat = findViewById(R.id.btnMoiNhat);
        spLocGia = findViewById(R.id.spLocGia);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}