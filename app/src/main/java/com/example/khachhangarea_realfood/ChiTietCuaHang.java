package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.adapter.GiamGiaAdapter;
import com.example.khachhangarea_realfood.adapter.ViewPaperAdapter;
import com.example.khachhangarea_realfood.fragment.TatCaSanPhamFragment;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DanhGia;
import com.example.khachhangarea_realfood.model.SanPham;
import com.example.khachhangarea_realfood.model.Voucher;
import com.example.khachhangarea_realfood.model.YeuThich;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChiTietCuaHang extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private ViewPaperAdapter viewPaperAdapter;
    private TextView tvTenCuaHang, tvEmail, tvDiaChi, tvPhone, tvMota, tvTongSanPham, tvRating, tvTBRating;
    private Button btnYeuThich;
    private ImageView ivWallPaper;
    private CircleImageView civAvatar;
    private CuaHang cuaHang;
    private ProgressBar pbLoadChiTietCuaHang;
    private Firebase_Manager firebase_manager = new Firebase_Manager();
    private ArrayList<CuaHang> cuaHangs;
    private ArrayList<Voucher> vouchers;
    private GiamGiaAdapter giamGiaAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rcvGiamGia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietcuahang);
        cuaHangs = new ArrayList<>();
        vouchers = new ArrayList<>();
        giamGiaAdapter = new GiamGiaAdapter(this, R.layout.list_item_magiamgia, vouchers);
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataCuaHang = intent.getStringExtra("dataCuaHang");
            Gson gson = new Gson();
            cuaHang = gson.fromJson(dataCuaHang, CuaHang.class);
            LoadInfoCuaHang();
            LoadItemGiamGia();
        }

        setEvent();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_baocao:
                Intent intentBaoCao = new Intent(ChiTietCuaHang.this, BaoCaoShop.class);
                Gson gson = new Gson();
                String data = gson.toJson(cuaHang);
                intentBaoCao.putExtra("dataShop", data);
                startActivity(intentBaoCao);
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadInfoCuaHang() {
        if (cuaHang != null) {
            tvTenCuaHang.setText(cuaHang.getTenCuaHang());
            tvEmail.setText(cuaHang.getEmail());
            tvDiaChi.setText(cuaHang.getDiaChi());
            tvPhone.setText(cuaHang.getSoDienThoai());
            tvMota.setText(cuaHang.getThongTinChiTiet());

            firebase_manager.mDatabase.child("SanPham").orderByChild("idcuaHang").equalTo(cuaHang.getIDCuaHang()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tvTongSanPham.setText(snapshot.getChildrenCount() + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            firebase_manager.LoadLogoCuaHang(cuaHang, getApplicationContext(), civAvatar);

            firebase_manager.LoadWallPaperCuaHang(cuaHang, getApplicationContext(), ivWallPaper);

            firebase_manager.mDatabase.child("YeuThich").child(firebase_manager.auth.getUid()).child("Shop").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CuaHang cuaHangAll = dataSnapshot.getValue(CuaHang.class);
                        if (cuaHangAll.getIDCuaHang().equals(cuaHang.getIDCuaHang())) {
                            btnYeuThich.setSelected(true);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            pbLoadChiTietCuaHang.setVisibility(View.GONE);
        }
    }

    private void setEvent() {
        viewPaperAdapter = new ViewPaperAdapter(this, cuaHang.getIDCuaHang());
        mViewPager.setAdapter(viewPaperAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Tất cả sản phẩm");

                        break;
                    case 1:
                        tab.setText("Đánh giá");
                        break;
                }
            }
        }).attach();
        btnYeuThich.setOnClickListener(new View.OnClickListener() {
            int check = 1;

            @Override
            public void onClick(View v) {
                if (check == 1 && !btnYeuThich.isSelected()) {
                    btnYeuThich.setSelected(true);
                    check = 0;
                    firebase_manager.ThemYeuThichCuaHang(cuaHang);

                } else {
                    btnYeuThich.setSelected(false);
                    check = 1;
                    firebase_manager.XoaYeuThichCuaHang(cuaHang);
                }
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcvGiamGia.setLayoutManager(linearLayoutManager);
        rcvGiamGia.setAdapter(giamGiaAdapter);
        LoadItemGiamGia();

        firebase_manager.mDatabase.child("DanhGia").orderByChild("idcuaHang").equalTo(cuaHang.getIDCuaHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float tongRating = 0f;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DanhGia danhGia = dataSnapshot.getValue(DanhGia.class);
                    tongRating += danhGia.getRating();
                }
                tvRating.setText(snapshot.getChildrenCount() + "");
                float tbRating = (float) Math.round((tongRating / snapshot.getChildrenCount()) * 10) / 10;
                tvTBRating.setText(tbRating + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadItemGiamGia() {
        firebase_manager.mDatabase.child("Voucher").orderByChild("idcuaHang").equalTo(cuaHang.getIDCuaHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vouchers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);
                    Date currentDateandTime = new Date();
                    if (currentDateandTime.compareTo(voucher.getHanSuDung()) > 0) {
                        vouchers.add(voucher);
                        giamGiaAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_paper);
        tvTenCuaHang = findViewById(R.id.tvTenCuaHang);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvMota = findViewById(R.id.tvMoTa);
        tvTongSanPham = findViewById(R.id.tvTotalSanPham);
        civAvatar = findViewById(R.id.civAvatar);
        ivWallPaper = findViewById(R.id.ivWallpaper);
        btnYeuThich = findViewById(R.id.btnYeuThich);
        pbLoadChiTietCuaHang = findViewById(R.id.pbLoadChiTietCuaHang);
        rcvGiamGia = findViewById(R.id.rcvMaGiamGia);
        tvRating = findViewById(R.id.tvRating);
        tvTBRating = findViewById(R.id.tvTBRating);
    }
}