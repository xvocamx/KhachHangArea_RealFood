package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.DanhGia;

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
        firebase_manager.LoadTenKhachHang(holder.tvTenKhachHang);
        firebase_manager.LoadImageKhachHang(context, holder.ivAvatar);
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
        TextView tvTenKhachHang, tvThoiGian, tvDanhGia;
        RatingBar ratingBar;
        ImageView ivAvatar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            tvDanhGia = itemView.findViewById(R.id.tvDanhGia);
            ivAvatar = itemView.findViewById(R.id.civAvatar);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
