package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.DanhMuc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<DanhMuc> danhMucs;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public DanhMucAdapter(Activity context, int resource, ArrayList<DanhMuc> danhMucs) {
        this.context = context;
        this.resource = resource;
        this.danhMucs = danhMucs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView linearLayout = (CardView) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhMucAdapter.MyViewHolder holder, int position) {
        DanhMuc danhMuc = danhMucs.get(position);
        if (danhMuc == null) {
            return;
        }
        holder.tvTenDanhMuc.setText(danhMuc.getTenDanhMuc());
        storageRef.child("DanhMuc").child(danhMuc.getIDDanhMuc()).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri).into(holder.ivDanhMuc);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return danhMucs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDanhMuc;
        ImageView ivDanhMuc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
            ivDanhMuc = itemView.findViewById(R.id.ivDanhMuc);
        }
    }
}
