package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;

public class DonMuaChuanBiHangAdpater extends RecyclerView.Adapter<DonMuaChuanBiHangAdpater.MyViewHolder>implements Filterable {
    Activity context;
    int resource;
    ArrayList<DonHang> donHangs,donHangsOld;
    ArrayList<DonHangInfo> donHangInfos = new ArrayList<>();
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
        this.donHangsOld = donHangs;
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
        holder.tvMaDH.setText(donHang.getIDDonHang().substring(0, 15));
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
        firebase_manager.SetColorOfStatus(donHang.getTrangThai(), holder.cvIDDonHang, holder.tvMaDH);
        holder.ivLogo.setImageResource(R.drawable.logo_shipper);
        firebase_manager.mDatabase.child("DonHangInfo").orderByChild("iddonHang").equalTo(donHang.getIDDonHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    if (donHangInfo.getIDDonHang().equals(donHang.getIDDonHang())) {
                        holder.tvSanPham.setText(holder.tvSanPham.getText() + donHangInfo.getSanPham().getTenSanPham() + "(" + donHangInfo.getSoLuong() + ")" + ", ");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        TextView tvMaDH, tvTenCuaHang, tvRating, tvAddressShop, tvTrangThai, tvXemThongTinChiTiet, tvThoiGian, tvGhiChu, tvSanPham;
        ImageView ivLogo, ivDelete;
        CardView cvIDDonHang;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDH = itemView.findViewById(R.id.tvMaDH);

            cvIDDonHang = itemView.findViewById(R.id.cvIDDonHang);

            tvTenCuaHang = itemView.findViewById(R.id.tvTenCuaHang);

            tvRating = itemView.findViewById(R.id.tvRating);

            tvAddressShop = itemView.findViewById(R.id.tvAddressShop);

            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);

            ivLogo = itemView.findViewById(R.id.ivLogo);

            ivDelete = itemView.findViewById(R.id.ivDelete);

            tvXemThongTinChiTiet = itemView.findViewById(R.id.tvXemThongTinChiTiet);
            tvXemThongTinChiTiet.setOnClickListener(this);

            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);

            tvGhiChu = itemView.findViewById(R.id.tvGhiChu);

            tvSanPham = itemView.findViewById(R.id.tvSanPham);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    donHangs = donHangsOld;
                } else {
                    ArrayList<DonHang> donHangsNew = new ArrayList<>();
                    for (DonHang donHang : donHangsOld) {
                        if (donHang.getIDDonHang().toLowerCase().contains(strSearch.toLowerCase())) {
                            donHangsNew.add(donHang);
                        }
                    }
                    donHangs = donHangsNew;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = donHangs;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                donHangs = (ArrayList<DonHang>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

