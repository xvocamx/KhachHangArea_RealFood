package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiGioHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ThanhToanAdapter extends RecyclerView.Adapter<ThanhToanAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<DonHangInfo> donHangInfos;
    private Firebase_Manager firebase_manager = new Firebase_Manager();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private KAlertDialog kAlertDialog;
    private CheckBoxListener checkBoxListener;
    private TrangThaiGioHang trangThaiGioHang = TrangThaiGioHang.GIO_HANG;


    public ThanhToanAdapter(Activity context, int resource, ArrayList<DonHangInfo> donHangInfos) {
        this.context = context;
        this.resource = resource;
        this.donHangInfos = donHangInfos;
    }

    private SparseBooleanArray booleanArray = new SparseBooleanArray();

    public SparseBooleanArray getBooleanArray() {
        return booleanArray;
    }

    public void setBooleanArray(SparseBooleanArray booleanArray) {
        this.booleanArray = booleanArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhToanAdapter.MyViewHolder holder, int position) {
        DonHangInfo donHangInfo = donHangInfos.get(position);
        if(donHangInfo == null){
            return;
        }
        holder.tvTenSanPham.setText(donHangInfo.getSanPham().getTenSanPham());
        String gia = String.valueOf(Double.valueOf(donHangInfo.getSanPham().getGia()));
        if(donHangInfo.getSanPham().getGiaOld() != null){
            holder.lnGiaOld.setVisibility(View.VISIBLE);
            holder.tvGiaOld.setText(String.valueOf(donHangInfo.getSanPham().getGiaOld()));
        }
        holder.tvGia.setText(gia);
        holder.tvSoLuong.setText("X "+ donHangInfo.getSoLuong());
        firebase_manager.storageRef.child("SanPham").child(donHangInfo.getSanPham().getIDCuaHang()).child(donHangInfo.getSanPham().getIDSanPham()).child(donHangInfo.getSanPham().getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try{
                    Glide.with(context)
                            .load(task.getResult().toString())
                            .into(holder.ivSanPham);
                }catch (Exception ex){

                }

                holder.pbLoadItemGioHang.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return donHangInfos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  tvTenSanPham, tvGia,tvGiaOld,tvSoLuong;
        ImageView  ivSanPham;
        ProgressBar pbLoadItemGioHang;
        LinearLayout lnGiaOld;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSanPham = itemView.findViewById(R.id.ivSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvGia = itemView.findViewById(R.id.tvGia);
            tvGiaOld = itemView.findViewById(R.id.tvGiaOld);
            pbLoadItemGioHang = itemView.findViewById(R.id.pbLoadItemGioHang);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            lnGiaOld = itemView.findViewById(R.id.lnGiaOld);
        }
    }

    public interface CheckBoxListener {
        void getGiaGioHang();
    }



}
