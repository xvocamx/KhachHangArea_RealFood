package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.khachhangarea_realfood.ChiTietCuaHang;
import com.example.khachhangarea_realfood.ChiTietSanPham;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private CuaHang cuaHang;
    private ArrayList<DonHangInfo> donHangInfos;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public GioHangAdapter(Activity context, int resource, ArrayList<DonHangInfo> donHangInfos) {
        this.context = context;
        this.resource = resource;
        this.donHangInfos = donHangInfos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
        DonHangInfo donHangInfo = donHangInfos.get(position);
        if (donHangInfo == null) {
            return;
        }
        mDatabase.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    if(donHangInfo.getIDSanPham().equals(sanPham.getIDSanPham())){
                        holder.tvTenSanPham.setText(sanPham.getTenSanPham());
                        String gia = String.valueOf(Integer.valueOf(donHangInfo.getSoLuong()) * Integer.valueOf(sanPham.getGia()));
                        holder.tvGia.setText(gia);
                        storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Glide.with(context)
                                        .load(task.getResult().toString())
                                        .into(holder.ivSanPham);
                            }
                        });
                        mDatabase.child("CuaHang").child(sanPham.getIDCuaHang()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                CuaHang cuaHang = snapshot.getValue(CuaHang.class);
                                holder.tvTenCuaHang.setText(cuaHang.getTenCuaHang());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.btnSoLuong.setNumber(donHangInfo.getSoLuong());
//        holder.btnXemShop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ChiTietCuaHang.class);
//                Gson gson = new Gson();
//                String data = gson.toJson(cuaHang);
//                intent.putExtra("dataCuaHang", data);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return donHangInfos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenCuaHang, tvTenSanPham, tvGia;
        ImageView ivShop, ivSanPham;
        EditText edtMaGiamGia;
        CheckBox ckbSanPham;
        Button btnXoa, btnXemShop;
        ElegantNumberButton btnSoLuong;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSanPham = itemView.findViewById(R.id.ivSanPham);
            ivShop = itemView.findViewById(R.id.ivShop);
            tvTenCuaHang = itemView.findViewById(R.id.tvTenCuaHang);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvGia = itemView.findViewById(R.id.tvGia);
            edtMaGiamGia = itemView.findViewById(R.id.edtMaGiamGia);
            ckbSanPham = itemView.findViewById(R.id.ckbSanPham);
            btnXoa = itemView.findViewById(R.id.btnXoaSanPham);
            btnXemShop = itemView.findViewById(R.id.btnXemShop);
            btnSoLuong = itemView.findViewById(R.id.btnSoLuong);
        }
    }
}
