package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonMuaAdpater extends RecyclerView.Adapter<DonMuaAdpater.MyViewHolder> {
    Activity context;
    int resource;
    ArrayList<DonHang> donHangs;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    ClickItemDonMuaListener delegation;

    public void setDelegation(ClickItemDonMuaListener delegation) {
        this.delegation = delegation;
    }

    public DonMuaAdpater(Activity context, int resource, ArrayList<DonHang> donHangs) {
        this.context = context;
        this.resource = resource;
        this.donHangs = donHangs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull DonMuaAdpater.MyViewHolder holder, int position) {
        DonHang donHang = donHangs.get(position);
        if (donHang == null) {
            return;
        }
        holder.tvMaDH.setText(donHang.getIDDonHang());
        firebase_manager.mDatabase.child("CuaHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CuaHang cuaHang = dataSnapshot.getValue(CuaHang.class);
                    if (cuaHang.getIDCuaHang().equals(donHang.getIDCuaHang())) {
                        holder.tvTenCuaHang.setText(cuaHang.getTenCuaHang());
                        holder.tvRating.setText(String.valueOf(cuaHang.getRating()));
                        holder.tvAddressShop.setText(cuaHang.getDiaChi());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tvTrangThai.setText(donHang.getTrangThai().toString());
        holder.ivLogo.setImageResource(R.drawable.logo_shipper);
        //Xoa item don hang
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Su kien click vao item don hang
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.getInfomationDonMua(donHang);
                }
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return donHangs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMaDH, tvTenCuaHang, tvRating, tvAddressShop, tvTrangThai;
        ImageView ivLogo, ivDelete;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDH = itemView.findViewById(R.id.tvMaDH);
            tvMaDH.setOnClickListener(this);

            tvTenCuaHang = itemView.findViewById(R.id.tvTenCuaHang);
            tvTenCuaHang.setOnClickListener(this);

            tvRating = itemView.findViewById(R.id.tvRating);
            tvRating.setOnClickListener(this);

            tvAddressShop = itemView.findViewById(R.id.tvAddressShop);
            tvAddressShop.setOnClickListener(this);

            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvTrangThai.setOnClickListener(this);

            ivLogo = itemView.findViewById(R.id.ivLogo);
            ivLogo.setOnClickListener(this);

            ivDelete = itemView.findViewById(R.id.ivDelete);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

    public interface ClickItemDonMuaListener{
        void getInfomationDonMua(DonHang donHang);
    }
}

