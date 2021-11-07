package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class ItemGioHangAdapter extends RecyclerView.Adapter<ItemGioHangAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<DonHangInfo> donHangInfos;
    private Firebase_Manager firebase_manager = new Firebase_Manager();

    public ItemGioHangAdapter(Activity context, int resource, ArrayList<DonHangInfo> donHangInfos) {
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
    public void onBindViewHolder(@NonNull ItemGioHangAdapter.MyViewHolder holder, int position) {
        DonHangInfo donHangInfo = donHangInfos.get(position);
        if (donHangInfo == null) {
            return;
        }
        holder.btnSoLuong.setNumber(donHangInfo.getSoLuong()+"");
        firebase_manager.mDatabase.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    holder.tvTenSanPham.setText(sanPham.getTenSanPham());
                    String gia = String.valueOf(Integer.valueOf(donHangInfo.getSoLuong()) * Integer.valueOf(sanPham.getGia()));
                    holder.tvGia.setText(gia);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        TextView tvTenSanPham, tvGia;
        ElegantNumberButton btnSoLuong;
        Button btnXoa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvGia = itemView.findViewById(R.id.tvGia);
            btnSoLuong = itemView.findViewById(R.id.btnSoLuong);
            btnXoa = itemView.findViewById(R.id.btnXoaSanPham);
        }
    }
}
