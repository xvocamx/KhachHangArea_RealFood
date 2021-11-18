package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.DanhGia;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class DanhGiaAdapter extends RecyclerView.Adapter<DanhGiaAdapter.MyViewHolder> {
    Activity context;
    int resource;
    ArrayList<DonHangInfo> donHangInfos;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    Uri uri;

    public DanhGiaAdapter(Activity context, int resource, ArrayList<DonHangInfo> donHangInfos) {
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
    public void onBindViewHolder(@NonNull DanhGiaAdapter.MyViewHolder holder, int position) {
        DonHangInfo donHangInfo = donHangInfos.get(position);
        if (donHangInfo == null) {
            return;
        }
        firebase_manager.mDatabase.child("SanPham").child(donHangInfo.getSanPham().getIDSanPham()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                holder.tvTenSanPham.setText(sanPham.getTenSanPham());
                firebase_manager.LayTenLoai(sanPham, holder.tvLoaiSanPham);
                firebase_manager.LoadImageFood(sanPham, context, holder.ivSanPham);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebase_manager.mDatabase.child("DonHang").child(donHangInfo.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                String date = DateFormat.getDateInstance(DateFormat.SHORT).format(donHang.getNgayTao());
                holder.tvThoiGian.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tvSoLuong.setText(donHangInfo.getSoLuong());

        //Xu ly su kien
        holder.btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = holder.ratingBar.getRating();
                String noiDung = holder.edtDanhGia.getText().toString();
                String IDDanhGia = "DG_" + UUID.randomUUID().toString();
                String IDSanPham = donHangInfo.getSanPham().getIDSanPham();
                String IDKhachHang = firebase_manager.auth.getUid();
                String IDCuaHang = donHangInfo.getSanPham().getIDCuaHang();
                DanhGia danhGia = new DanhGia(IDDanhGia, IDSanPham, IDCuaHang, IDKhachHang, noiDung,"", donHangInfo.getIDInfo(), rating, new Date(),null);
                firebase_manager.mDatabase.child("DanhGia").child(IDDanhGia).setValue(danhGia);
                holder.lnDanhGia.setVisibility(View.GONE);
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
        TextView tvTenSanPham, tvLoaiSanPham, tvThoiGian, tvSoLuong;
        LinearLayout lnDanhGia;
        ImageView ivSanPham;
        EditText edtDanhGia;
        Button btnGui;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvLoaiSanPham = itemView.findViewById(R.id.tvLoaiSanPham);
            ivSanPham = itemView.findViewById(R.id.ivSanPham);
            edtDanhGia = itemView.findViewById(R.id.edtDanhGia);
            btnGui = itemView.findViewById(R.id.btnGui);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            lnDanhGia = itemView.findViewById(R.id.lnDanhGia);
        }
    }
}
