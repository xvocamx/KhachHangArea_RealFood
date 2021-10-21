package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class ChiTietSanPham extends AppCompatActivity {
    private TextView tvNameFood, tvGia, tvRating, tvMoTa, tvTenCuaHang;
    private ImageView ivFood;
    private SanPham sanPham;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataSanPham = intent.getStringExtra("dataSanPham");
            Gson gson = new Gson();
            sanPham = gson.fromJson(dataSanPham, SanPham.class);
        }
        LoadInfoSanPham();
        setEvent();
    }

    private void LoadInfoSanPham() {
        if (sanPham != null) {
            tvNameFood.setText(sanPham.getTenSanPham());
            tvGia.setText(sanPham.getGia());
            Float rating = sanPham.getRating();
            tvRating.setText(rating.toString());
            tvMoTa.setText(sanPham.getChiTietSanPham());
            storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Glide.with(getApplicationContext())
                            .load(task.getResult().toString())
                            .into(ivFood);
                }
            });
            //tvTenCuaHang.setText(sanPham.getIDCuaHang());
        }
    }

    private void setEvent() {

    }

    private void setControl() {
        tvNameFood = findViewById(R.id.tvTenSanPham);
        tvGia = findViewById(R.id.tvGia);
        tvRating = findViewById(R.id.tvRating);
        tvMoTa = findViewById(R.id.tvMoTa);
        tvTenCuaHang = findViewById(R.id.tvTenCuaHang);
        ivFood = findViewById(R.id.ivFood);
    }
}