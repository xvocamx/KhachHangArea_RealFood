package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.adapter.ViewPaperAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class ChiTietCuaHang extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private ViewPaperAdapter viewPaperAdapter;
    private TextView tvTenCuaHang, tvEmail, tvDiaChi, tvPhone, tvMota;
    private ImageView ivShop;
    private CuaHang cuaHang;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietcuahang);
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataCuaHang = intent.getStringExtra("dataCuaHang");
            Gson gson = new Gson();
            cuaHang = gson.fromJson(dataCuaHang, CuaHang.class);
        }
        LoadInfoCuaHang();
        setControl();
        setEvent();
    }

    private void LoadInfoCuaHang() {
        if (cuaHang != null) {
            tvTenCuaHang.setText(cuaHang.getTenCuaHang());
            tvEmail.setText(cuaHang.getEmail());
            tvDiaChi.setText(cuaHang.getDiaChi());
            tvPhone.setText(cuaHang.getSoDienThoai());
            tvMota.setText(cuaHang.getThongTinChiTiet());
            storageRef.child("CuaHang").child(cuaHang.getIDCuaHang()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(ivShop);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("",e.getMessage());
                }
            });
        }
    }

    private void setEvent() {
        viewPaperAdapter = new ViewPaperAdapter(this);
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
    }

    private void setControl() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_paper);
        tvTenCuaHang = findViewById(R.id.tvTenCuaHang);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvMota = findViewById(R.id.tvMoTa);
        ivShop = findViewById(R.id.ivShop);
    }
}