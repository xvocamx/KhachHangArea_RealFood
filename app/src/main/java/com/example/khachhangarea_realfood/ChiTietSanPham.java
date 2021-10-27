package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class ChiTietSanPham extends AppCompatActivity {
    private TextView tvNameFood, tvGia, tvRating, tvMoTa, tvTenCuaHang, tvAddressShop;
    private ImageView ivFood, ivShop;
    private SanPham sanPham;
    private CuaHang cuaHang;
    private ProgressBar pbLoadChiTietSanPham;
    private Button btnXemShop, btnDatHang;
    private ElegantNumberButton btnSoLuong;
    private ArrayList<CuaHang> cuaHangs = new ArrayList<>();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

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

            mDatabase.child("CuaHang").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CuaHang cuaHang = dataSnapshot.getValue(CuaHang.class);
                        cuaHangs.add(cuaHang);
                        if (sanPham.getIDCuaHang().equals(cuaHang.getIDCuaHang())) {
                            tvTenCuaHang.setText(cuaHang.getTenCuaHang());
                            tvAddressShop.setText(cuaHang.getDiaChi());
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
                                    Log.d("", e.getMessage());
                                }
                            });
                            btnXemShop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChiTietSanPham.this, ChiTietCuaHang.class);
                                    Gson gson = new Gson();
                                    String data = gson.toJson(cuaHang);
                                    intent.putExtra("dataCuaHang", data);
                                    startActivity(intent);
                                    Toast.makeText(ChiTietSanPham.this, cuaHang.getIDCuaHang() + "", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void setEvent() {

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGioHang();
            }
        });
    }

    private void addGioHang() {
        String soLuong = btnSoLuong.getNumber();
        UUID uuid = UUID.randomUUID();
        String IDInfo = "MD_" + uuid.toString();
        String donGia = sanPham.getGia();
        DonHangInfo donHangInfo = new DonHangInfo(IDInfo, "", soLuong, donGia, null, sanPham);
        mDatabase.child("DonHangInfo").child(user.getUid()).child(IDInfo).setValue(donHangInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(ChiTietSanPham.this, GioHang.class);
                startActivity(intent);
                Toast.makeText(ChiTietSanPham.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setControl() {
        tvNameFood = findViewById(R.id.tvTenSanPham);
        tvGia = findViewById(R.id.tvGia);
        tvRating = findViewById(R.id.tvRating);
        tvMoTa = findViewById(R.id.tvMoTa);
        tvTenCuaHang = findViewById(R.id.tvTenCuaHang);
        ivFood = findViewById(R.id.ivFood);
        ivShop = findViewById(R.id.ivShop);
        btnXemShop = findViewById(R.id.btnXemShop);
        tvAddressShop = findViewById(R.id.tvAddressShop);
        btnSoLuong = findViewById(R.id.btnSoLuong);
        btnDatHang = findViewById(R.id.btnDatHang);
    }
}