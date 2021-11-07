package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.GioHangDisplay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThanhToanProAdapter extends RecyclerView.Adapter<ThanhToanProAdapter.MyViewHolder> {
    private Activity context;

    private int resource;
    private ArrayList<GioHangDisplay> gioHangDisplays;
    private Firebase_Manager firebase_manager = new Firebase_Manager();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private KAlertDialog kAlertDialog;









    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull ThanhToanProAdapter.MyViewHolder holder, int position) {
        GioHangDisplay gioHangDisplay = gioHangDisplays.get(position);


        if (gioHangDisplay == null) {
            return;
        }

        firebase_manager.mDatabase.child("CuaHang").child(gioHangDisplay.getIdCuaHang()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CuaHang cuaHang = snapshot.getValue(CuaHang.class);
                holder.tvTenCuaHang.setText(cuaHang.getTenCuaHang());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebase_manager.storageRef.child("CuaHang").child(gioHangDisplay.getIdCuaHang()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.ivShop);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });

        ArrayList<DonHangInfo> donHangInfos = gioHangDisplay.getSanPhams();
        ThanhToanAdapter gioHangAdapter = new ThanhToanAdapter(context,R.layout.list_item_thanhtoan,donHangInfos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.rcSanPham.setLayoutManager(linearLayoutManager);
        holder.rcSanPham.setAdapter(gioHangAdapter);
    }



    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return gioHangDisplays.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenCuaHang;
        ImageView ivShop;
        ProgressBar pbLoadItemGioHang;
        RecyclerView rcSanPham;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivShop = itemView.findViewById(R.id.ivShop);
            tvTenCuaHang = itemView.findViewById(R.id.tvNameShop);
            pbLoadItemGioHang = itemView.findViewById(R.id.pbLoadItemGioHang);
            rcSanPham = itemView.findViewById(R.id.rcvItemGiohang);
            this.setIsRecyclable(false);
        }
    }

    public interface CheckBoxListener {
        void getGiaGioHang();
    }


}
