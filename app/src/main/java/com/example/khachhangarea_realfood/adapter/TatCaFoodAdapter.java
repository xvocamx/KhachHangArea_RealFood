package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.LoaiSanPham;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TatCaFoodAdapter extends RecyclerView.Adapter<TatCaFoodAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<SanPham> sanPhams;
    private Firebase_Manager firebase_manager = new Firebase_Manager();

    public TatCaFoodAdapter(Activity context, int resource, ArrayList<SanPham> sanPhams) {
        this.context = context;
        this.resource = resource;
        this.sanPhams = sanPhams;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull TatCaFoodAdapter.MyViewHolder holder, int position) {
        SanPham sanPham = sanPhams.get(position);
        if (sanPham == null) {
            return;
        }
        holder.tvNameFood.setText(sanPham.getTenSanPham());
        firebase_manager.LayTenLoai(sanPham,holder.tvTenLoai);
        holder.tvRating.setText(String.valueOf(sanPham.getRating()));
        firebase_manager.LoadImageFood(sanPham,context,holder.ivFood);
    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return sanPhams.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNameFood, tvTenLoai, tvRating;
        ImageView ivFood;
        LinearLayout lnRating;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            tvNameFood.setOnClickListener(this);

            tvTenLoai = itemView.findViewById(R.id.tvTenLoai);
            tvTenLoai.setOnClickListener(this);

            tvRating = itemView.findViewById(R.id.tvRating);

            ivFood = itemView.findViewById(R.id.ivFood);

            lnRating = itemView.findViewById(R.id.lnRating);
            lnRating.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }
}
