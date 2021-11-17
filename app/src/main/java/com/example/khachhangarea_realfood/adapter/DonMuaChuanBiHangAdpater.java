package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonMuaChuanBiHangAdpater extends RecyclerView.Adapter<DonMuaChuanBiHangAdpater.MyViewHolder> {
    Activity context;
    int resource;
    ArrayList<DonHang> donHangs;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    ClickItemDonMuaListener delegation;
    KAlertDialog kAlertDialog;

    public void setDelegation(ClickItemDonMuaListener delegation) {
        this.delegation = delegation;
    }

    public DonMuaChuanBiHangAdpater(Activity context, int resource, ArrayList<DonHang> donHangs) {
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
    public void onBindViewHolder(@NonNull DonMuaChuanBiHangAdpater.MyViewHolder holder, int position) {
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
        holder.tvTrangThai.setText(firebase_manager.GetStringTrangThaiDonHang(donHang.getTrangThai()));
        holder.ivLogo.setImageResource(R.drawable.logo_shipper);
        //Xoa item don hang
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kAlertDialog = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Thông báo")
                        .setContentText("Bạn có muốn hủy đơn hàng này ?")
                        .setConfirmText("Có")
                        .setCancelText("Không")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                kAlertDialog.dismiss();
                                donHang.setTrangThai(TrangThaiDonHang.KhachHang_HuyDon);
                                firebase_manager.mDatabase.child("DonHang").child(donHang.getIDDonHang()).setValue(donHang);
                            }
                        }).setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                kAlertDialog.dismiss();
                            }
                        });
                kAlertDialog.show();
            }
        });
        //Su kien click vao item don hang
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegation != null) {
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
        TextView tvMaDH, tvTenCuaHang, tvRating, tvAddressShop, tvTrangThai,tvXemThongTinChiTiet;
        ImageView ivLogo, ivDelete;
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

            ivDelete = itemView.findViewById(R.id.ivDelete);

            tvXemThongTinChiTiet = itemView.findViewById(R.id.tvXemThongTinChiTiet);
            tvXemThongTinChiTiet.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

    public interface ClickItemDonMuaListener {
        void getInfomationDonMua(DonHang donHang);
    }
}

