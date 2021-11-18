package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DanhGia;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DanhGiaSanPhamAdapter extends RecyclerView.Adapter<DanhGiaSanPhamAdapter.MyViewHolder> {
    Activity context;
    int resource;
    ArrayList<DanhGia> danhGias;
    Firebase_Manager firebase_manager = new Firebase_Manager();

    public DanhGiaSanPhamAdapter(Activity context, int resource, ArrayList<DanhGia> danhGias) {
        this.context = context;
        this.resource = resource;
        this.danhGias = danhGias;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhGiaSanPhamAdapter.MyViewHolder holder, int position) {
        DanhGia danhGia = danhGias.get(position);
        if (danhGia == null) {
            return;
        }
        holder.tvDanhGia.setText(danhGia.getNoiDung());
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(danhGia.getNgayDanhGia());
        holder.tvThoiGian.setText(date);
        holder.ratingBar.setRating(danhGia.getRating());
        firebase_manager.mDatabase.child("KhachHang").child(danhGia.getIDKhachHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                holder.tvTenKhachHang.setText(khachHang.getTenKhachHang());
                firebase_manager.storageRef.child("KhachHang").child(khachHang.getIDKhachHang()).child("AvatarKhachHang").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri.toString()).into(holder.ivAvatar);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        if(danhGia.getNoiDungShopTraLoi().equals("")){
            holder.lnDanhGiaShop.setVisibility(View.GONE);
        }
        else {
            holder.lnDanhGiaShop.setVisibility(View.VISIBLE);
        }

        firebase_manager.mDatabase.child("CuaHang").child(danhGia.getIDCuaHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CuaHang cuaHang = snapshot.getValue(CuaHang.class);
                holder.tvTenShop.setText(cuaHang.getTenCuaHang());
                firebase_manager.LoadLogoCuaHang(cuaHang, context, holder.ivShop);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tvDanhGiaShop.setText(danhGia.getNoiDungShopTraLoi());
        String dateShop = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(danhGia.getNgayShopTraLoi());
        holder.tvThoiGianShop.setText(dateShop);
    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return danhGias.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenKhachHang, tvThoiGian, tvDanhGia, tvTenShop, tvDanhGiaShop, tvThoiGianShop;
        RatingBar ratingBar;
        ImageView ivAvatar, ivShop;
        LinearLayout lnDanhGiaShop;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            tvDanhGia = itemView.findViewById(R.id.tvDanhGia);
            ivAvatar = itemView.findViewById(R.id.civAvatar);
            ivShop = itemView.findViewById(R.id.civShop);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvTenShop = itemView.findViewById(R.id.tvTenShop);
            tvDanhGiaShop = itemView.findViewById(R.id.tvDanhGiaShop);
            tvThoiGianShop = itemView.findViewById(R.id.tvThoiGianShop);
            lnDanhGiaShop = itemView.findViewById(R.id.lnDanhGiaShop);
        }
    }
}
