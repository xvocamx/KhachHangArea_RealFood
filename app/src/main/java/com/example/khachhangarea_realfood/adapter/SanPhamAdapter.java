package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.ChiTietSanPham;
import com.example.khachhangarea_realfood.GioHang;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.MyViewHolder> implements Filterable {
    private Activity context;
    private int resource;
    private ArrayList<SanPham> arrayList, arrayListOld;
    private ClickItemFoodListener delegation;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public void setDelegation(ClickItemFoodListener delegation) {
        this.delegation = delegation;
    }

    public SanPhamAdapter(Activity context, int resource, ArrayList<SanPham> arrayList) {
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        this.arrayListOld = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        LinearLayout view = (LinearLayout) layoutInflater.inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamAdapter.MyViewHolder holder, int position) {
        SanPham sanPham = arrayList.get(position);
        if (sanPham == null) {
            return;
        }
        holder.tvTenSanPham.setText(sanPham.getTenSanPham());
        holder.tvGia.setText(sanPham.getGia());
        Float rating = Float.valueOf(sanPham.getRating());
        holder.tvRatings.setText(rating.toString());
        holder.tvLoaiSanPham.setText(sanPham.getIDLoai());
        storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Glide.with(context)
                        .load(task.getResult().toString())
                        .into(holder.ivSanPham);
            }
        });

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegation != null) {
                    delegation.getInformationFood(sanPham);
                } else {
                    Toast.makeText(context, "You must set delegation before", Toast.LENGTH_SHORT).show();
                }
            }
        };
        holder.ivMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soLuong = "1";
                UUID uuid = UUID.randomUUID();
                String IDInfo = "MD_" + uuid.toString();
                String donGia = sanPham.getGia();
                DonHangInfo donHangInfo = new DonHangInfo(IDInfo, "", soLuong, donGia, null, sanPham);
                mDatabase.child("DonHangInfo").child(auth.getUid()).child(IDInfo).setValue(donHangInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(context, GioHang.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTenSanPham;
        TextView tvLoaiSanPham;
        TextView tvRatings;
        TextView tvGia;
        ImageView ivSanPham, ivMuaNgay;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvTenSanPham.setOnClickListener(this);

            tvLoaiSanPham = itemView.findViewById(R.id.tvLoaiSanPham);
            tvLoaiSanPham.setOnClickListener(this);

            tvRatings = itemView.findViewById(R.id.tvRating);
            tvRatings.setOnClickListener(this);

            tvGia = itemView.findViewById(R.id.tvGia);
            tvGia.setOnClickListener(this);

            ivSanPham = itemView.findViewById(R.id.ivFood);
            ivSanPham.setOnClickListener(this);

            ivMuaNgay = itemView.findViewById(R.id.ivMuaNgay);
            ivMuaNgay.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

    public interface ClickItemFoodListener {
        void getInformationFood(SanPham sanPham);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    arrayList = arrayListOld;
                } else {
                    ArrayList<SanPham> sanPhams = new ArrayList<>();
                    for (SanPham sanPham : arrayListOld) {
                        if (sanPham.getTenSanPham().toLowerCase().contains(strSearch.toLowerCase())) {
                            sanPhams.add(sanPham);
                        }
                    }
                    arrayList = sanPhams;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<SanPham>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}