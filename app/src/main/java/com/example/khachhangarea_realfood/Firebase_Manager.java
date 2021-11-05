package com.example.khachhangarea_realfood;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.adapter.CuaHangAdapter;
import com.example.khachhangarea_realfood.adapter.LoaiSanPhamAdapter;
import com.example.khachhangarea_realfood.adapter.SanPhamAdapter;
import com.example.khachhangarea_realfood.adapter.YeuThichFoodAdapter;
import com.example.khachhangarea_realfood.adapter.YeuThichShopAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.LoaiSanPham;
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

import java.util.ArrayList;

public class Firebase_Manager {
    public DatabaseReference mDatabase;
    public StorageReference storageRef;
    public FirebaseAuth auth;
    public FirebaseUser user;

    public Firebase_Manager() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public void LoadImageLoai(LoaiSanPham loaiSanPham, Context context, ImageView ivLoai) {
        storageRef.child("LoaiSanPham").child(loaiSanPham.getiDLoai()).child("Loại sản phẩm").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(ivLoai);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });
    }

    public void LoadImageFood(SanPham sanPham, Context context, ImageView ivFood) {
        storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Glide.with(context)
                        .load(task.getResult().toString())
                        .into(ivFood);
            }
        });
    }

    public void LoadWallPaperCuaHang(CuaHang cuaHang, Context context, ImageView ivWallPaper) {
        storageRef.child("CuaHang").child(cuaHang.getIDCuaHang()).child("WallPaper").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(ivWallPaper);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });
    }

    public void LoadLogoCuaHang(CuaHang cuaHang, Context context, ImageView civAvatar) {
        storageRef.child("CuaHang").child(cuaHang.getIDCuaHang()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(civAvatar);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });
    }

    public void LoadYeuThichShop(ArrayList<CuaHang> cuaHangs, YeuThichShopAdapter yeuThichShopAdapter, ProgressBar pb) {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Shop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cuaHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CuaHang cuaHangAll = dataSnapshot.getValue(CuaHang.class);
                    cuaHangs.add(cuaHangAll);
                    yeuThichShopAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadYeuThichFood(ArrayList<SanPham> sanPhams, YeuThichFoodAdapter yeuThichFoodAdapter, ProgressBar pb) {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    yeuThichFoodAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetSanPhamTheoLoai(ArrayList<SanPham> sanPhams, SanPhamAdapter sanPhamAdapter, LoaiSanPham loaiSanPham, ProgressBar pb) {
        mDatabase.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    if (sanPham.getIDLoai().equals(loaiSanPham.getiDLoai())) {
                        sanPhams.add(sanPham);
                        sanPhamAdapter.notifyDataSetChanged();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetLoaiSanPham(ArrayList<LoaiSanPham> loaiSanPhams, LoaiSanPhamAdapter loaiSanPhamAdapter, ProgressBar pb) {
        mDatabase.child("LoaiSanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loaiSanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiSanPham loaiSanPham = dataSnapshot.getValue(LoaiSanPham.class);
                    loaiSanPhams.add(loaiSanPham);
                    loaiSanPhamAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetPopularShop(ArrayList<CuaHang> cuaHangs, CuaHangAdapter cuaHangAdapter) {
        mDatabase.child("CuaHang").limitToFirst(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cuaHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CuaHang cuaHang = dataSnapshot.getValue(CuaHang.class);
                    if (cuaHang.getTrangThaiCuaHang() != TrangThaiCuaHang.ChuaKichHoat) {
                        cuaHangs.add(cuaHang);
                        cuaHangAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetPopularFood(ArrayList<SanPham> sanPhams, SanPhamAdapter sanPhamAdapter) {
        mDatabase.child("SanPham").limitToFirst(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetSaleFood(ArrayList<SanPham> sanPhams, SanPhamAdapter sanPhamAdapter) {
        mDatabase.child("SanPham").limitToFirst(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
