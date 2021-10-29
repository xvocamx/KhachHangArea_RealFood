package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.LoaiSanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LoaiSanPhamAdapter extends RecyclerView.Adapter<LoaiSanPhamAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<LoaiSanPham> loaiSanPhams;
    private ClickItemLoai delegation;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public void setDelegation(ClickItemLoai delegation) {
        this.delegation = delegation;
    }

    public LoaiSanPhamAdapter(Activity context, int resource, ArrayList<LoaiSanPham> loaiSanPhams) {
        this.context = context;
        this.resource = resource;
        this.loaiSanPhams = loaiSanPhams;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView linearLayout = (CardView) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiSanPhamAdapter.MyViewHolder holder, int position) {
        LoaiSanPham loaiSanPham = loaiSanPhams.get(position);
        if (loaiSanPham == null) {
            return;
        }
        holder.tvTenLoai.setText(loaiSanPham.getTenLoai());

        storageRef.child("LoaiSanPham").child(loaiSanPham.getiDLoai()).child("Loại sản phẩm").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.ivLoai);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegation != null) {
                    delegation.getLoai(loaiSanPham);
                } else {
                    Toast.makeText(context, "You must set delegation before", Toast.LENGTH_SHORT).show();
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
        return loaiSanPhams.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTenLoai;
        ImageView ivLoai;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLoai = itemView.findViewById(R.id.ivLoai);
            ivLoai.setOnClickListener(this);
            tvTenLoai = itemView.findViewById(R.id.tvTenLoai);
            tvTenLoai.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

    public interface ClickItemLoai {
        void getLoai(LoaiSanPham loaiSanPham);
    }
}
