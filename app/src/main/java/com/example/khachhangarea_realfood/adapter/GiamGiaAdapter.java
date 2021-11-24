package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.Voucher;

import java.text.DateFormat;
import java.util.ArrayList;

public class GiamGiaAdapter extends RecyclerView.Adapter<GiamGiaAdapter.MyViewHolder> {
    Activity context;
    int resource;
    ArrayList<Voucher> vouchers;

    public GiamGiaAdapter(Activity context, int resource, ArrayList<Voucher> vouchers) {
        this.context = context;
        this.resource = resource;
        this.vouchers = vouchers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull GiamGiaAdapter.MyViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);
        if (voucher == null) {
            return;
        }
        holder.tvMaGiamGia.setText(voucher.getMaGiamGia());
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(voucher.getHanSuDung());
        holder.tvHSD.setText(date);
        holder.tvTenSanPham.setText(voucher.getSanPham().getTenSanPham());
    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaGiamGia, tvHSD,tvTenSanPham;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaGiamGia = itemView.findViewById(R.id.tvMaGiamGia);
            tvHSD = itemView.findViewById(R.id.tvHSD);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
        }
    }
}
