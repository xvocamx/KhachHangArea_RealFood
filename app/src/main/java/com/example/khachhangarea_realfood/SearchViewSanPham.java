package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
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
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

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
    private Button btnMoiNhat, btnBanChay, btnApDung, btnHuyApDung;
    private Spinner spLocGia;
    private EditText edtGiaTu, edtGiaDen;
    private ArrayList<SanPham> source = new ArrayList<>();

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
        btnApDung.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if (!edtGiaTu.getText().toString().isEmpty() && !edtGiaDen.getText().toString().isEmpty()) {
                    int min = Integer.parseInt(edtGiaTu.getText().toString());
                    int max = Integer.parseInt(edtGiaDen.getText().toString());
                    if (min < max) {
                        sanPhams = (ArrayList<SanPham>) sanPhams.stream().filter(sanPham -> Integer.parseInt(sanPham.getGia()) >= min).collect(Collectors.toList());
                        sanPhams = (ArrayList<SanPham>) sanPhams.stream().filter(sanPham -> Integer.parseInt(sanPham.getGia()) <= max).collect(Collectors.toList());
                        sanPhamAdapter = new SanPhamAdapter(SearchViewSanPham.this, R.layout.list_item_food_1, sanPhams);
                        rcvSanPham.setAdapter(sanPhamAdapter);
                        Collections.sort(sanPhams, new Comparator<SanPham>() {
                            @Override
                            public int compare(SanPham o1, SanPham o2) {
                                return Integer.parseInt(o1.getGia()) < Integer.parseInt(o2.getGia()) ? -1 : 1;
                            }
                        });
                    } else {
                        Alerter.create(SearchViewSanPham.this)
                                .setTitle("Thông báo")
                                .setText("Số tiền không hợp lệ")
                                .setBackgroundColorRes(R.color.error_stroke_color)
                                .show();
                    }

                } else {
                    Alerter.create(SearchViewSanPham.this)
                            .setTitle("Thông báo")
                            .setText("Vui lòng không để trống !!!!")
                            .setBackgroundColorRes(R.color.error_stroke_color)
                            .show();
                }
            }
        });
        btnHuyApDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanPhams = source;
                sanPhamAdapter = new SanPhamAdapter(SearchViewSanPham.this, R.layout.list_item_food_1, sanPhams);
                rcvSanPham.setAdapter(sanPhamAdapter);
                edtGiaTu.setText("");
                edtGiaDen.setText("");
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
                if(o1.getNgayTao() !=null && o2.getNgayTao() != null){
                    return o1.getNgayTao().compareTo(o2.getNgayTao());
                }
                return 0;
            }
        });
        Collections.reverse(sanPhams);
        sanPhamAdapter.notifyDataSetChanged();
    }

    private void LoadSanPham() {
        firebase_manager.mDatabase.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    source.add(sanPham);
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
        edtGiaDen = findViewById(R.id.edtGiaDen);
        edtGiaTu = findViewById(R.id.edtGiaTu);
        btnApDung = findViewById(R.id.btnApDung);
        btnHuyApDung = findViewById(R.id.btnHuyApDung);
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