package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.DanhMuc;

import java.util.ArrayList;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<DanhMuc> danhMucs;

    public DanhMucAdapter(Activity context, int resource, ArrayList<DanhMuc> danhMucs) {
        this.context = context;
        this.resource = resource;
        this.danhMucs = danhMucs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView linearLayout = (CardView) context.getLayoutInflater().inflate(viewType,parent,false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhMucAdapter.MyViewHolder holder, int position) {
        DanhMuc danhMuc = danhMucs.get(position);
        if (danhMuc == null){
            return;
        }
        holder.tvTenDanhMuc.setText(danhMuc.getTenDanhMuc());

    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return danhMucs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTenDanhMuc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
        }
    }
}
