package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CuaHangAdapter extends RecyclerView.Adapter<CuaHangAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<CuaHang> cuaHangs;
    private ClickItemShopListener delegation;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public void setDelegation(ClickItemShopListener delegation) {
        this.delegation = delegation;
    }

    public CuaHangAdapter(Activity context, int resource, ArrayList<CuaHang> cuaHangs) {
        this.context = context;
        this.resource = resource;
        this.cuaHangs = cuaHangs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull CuaHangAdapter.MyViewHolder holder, int position) {
        CuaHang cuaHang = cuaHangs.get(position);
        if (cuaHang == null) {
            return;
        }
        holder.tvTenCuaHang.setText(cuaHang.getTenCuaHang());
        holder.tvDiaChi.setText(cuaHang.getDiaChi());
        Float rating = Float.valueOf(cuaHang.getRating());
        holder.tvRatings.setText(rating.toString());

        storageRef.child("CuaHang").child(cuaHang.getIDCuaHang()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.ivShop);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("",e.getMessage());
            }
        });
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation != null){
                    delegation.getInformationShop(cuaHang);
                }
                else {
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
        return cuaHangs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTenCuaHang, tvDiaChi, tvRatings;
        ImageView ivShop;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenCuaHang = itemView.findViewById(R.id.tvNameShop);
            tvTenCuaHang.setOnClickListener(this);

            tvDiaChi = itemView.findViewById(R.id.tvAddressShop);
            tvDiaChi.setOnClickListener(this);

            tvRatings = itemView.findViewById(R.id.tvRating);
            tvRatings.setOnClickListener(this);

            ivShop = itemView.findViewById(R.id.ivShop);
            ivShop.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onClickListener !=null){
                onClickListener.onClick(v);
            }
        }

    }
    public interface ClickItemShopListener {
        void getInformationShop(CuaHang cuaHang);
    }
}
