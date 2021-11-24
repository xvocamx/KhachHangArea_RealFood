package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhangarea_realfood.DanhGiaKhachHang;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DanhGia;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;

public class DonMuaDaNhanHangAdpater extends RecyclerView.Adapter<DonMuaDaNhanHangAdpater.MyViewHolder> {
    Activity context;
    int resource;
    ArrayList<DonHang> donHangs;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    ClickItemDonMuaListener delegation;

    public void setDelegation(ClickItemDonMuaListener delegation) {
        this.delegation = delegation;
    }

    public DonMuaDaNhanHangAdpater(Activity context, int resource, ArrayList<DonHang> donHangs) {
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
    public void onBindViewHolder(@NonNull DonMuaDaNhanHangAdpater.MyViewHolder holder, int position) {
        DonHang donHang = donHangs.get(position);
        if (donHang == null) {
            return;
        }
        holder.tvMaDH.setText(donHang.getIDDonHang().substring(0,15));
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(donHang.getNgayTao());
        holder.tvThoiGian.setText(date);
        holder.tvGhiChu.setText(donHang.getGhiChu_KhachHang());
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
        holder.tvTrangThai.setText(firebase_manager.GetStringTrangThaiDonHang(donHang.getTrangThai()));
        holder.ivLogo.setImageResource(R.drawable.logo_shipper);

        //Xu ly an nhan button khi da nhan xet
        firebase_manager.mDatabase.child("DonHangInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    if(donHangInfo.getIDDonHang().equals(donHang.getIDDonHang())){
                        firebase_manager.mDatabase.child("DanhGia").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                    DanhGia danhGia = dataSnapshot1.getValue(DanhGia.class);
                                    if(danhGia.getIDInfo().equals(donHangInfo.getIDInfo())){
                                        holder.btnNhanXet.setVisibility(View.GONE);
                                    }
                                }
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

        //Nhan xet cua khach hang
        holder.btnNhanXet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DanhGiaKhachHang.class);
                Gson gson = new Gson();
                String data = gson.toJson(donHang);
                intent.putExtra("dataDonHang",data);
                context.startActivity(intent);
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
        TextView tvMaDH, tvTenCuaHang, tvRating, tvAddressShop, tvTrangThai,tvXemThongTinChiTiet,tvThoiGian,tvGhiChu;
        ImageView ivLogo;
        Button btnNhanXet;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDH = itemView.findViewById(R.id.tvMaDH);
            //tvMaDH.setOnClickListener(this);

            tvTenCuaHang = itemView.findViewById(R.id.tvTenCuaHang);
            //tvTenCuaHang.setOnClickListener(this);

            tvRating = itemView.findViewById(R.id.tvRating);
            //tvRating.setOnClickListener(this);

            tvAddressShop = itemView.findViewById(R.id.tvAddressShop);
            //tvAddressShop.setOnClickListener(this);

            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            //tvTrangThai.setOnClickListener(this);

            ivLogo = itemView.findViewById(R.id.ivLogo);
            //ivLogo.setOnClickListener(this);

            btnNhanXet = itemView.findViewById(R.id.btnNhanXet);

            tvXemThongTinChiTiet = itemView.findViewById(R.id.tvXemThongTinChiTiet);
            tvXemThongTinChiTiet.setOnClickListener(this);

            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);

            tvGhiChu = itemView.findViewById(R.id.tvGhiChu);
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

