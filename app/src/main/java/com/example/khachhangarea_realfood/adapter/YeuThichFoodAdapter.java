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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.DanhGia;
import com.example.khachhangarea_realfood.model.LoaiSanPham;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class YeuThichFoodAdapter extends RecyclerView.Adapter<YeuThichFoodAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<SanPham> sanPhams;
    private Firebase_Manager firebase_manager = new Firebase_Manager();
    private ClickItemFoodListener delegation;
    private KAlertDialog kAlertDialog;

    public void setDelegation(ClickItemFoodListener delegation) {
        this.delegation = delegation;
    }

    public YeuThichFoodAdapter(Activity context, int resource, ArrayList<SanPham> sanPhams) {
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
    public void onBindViewHolder(@NonNull YeuThichFoodAdapter.MyViewHolder holder, int position) {
        SanPham sanPham = sanPhams.get(position);
        if (sanPham == null) {
            return;
        }
        holder.tvNameFood.setText(sanPham.getTenSanPham());
        firebase_manager.LayTenLoai(sanPham,holder.tvTenLoai);
        holder.tvGia.setText(sanPham.getGia()+" VNĐ");
        firebase_manager.mDatabase.child("DanhGia").orderByChild("idsanPham").equalTo(sanPham.getIDSanPham()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float tong = 0f;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DanhGia danhGia = dataSnapshot.getValue(DanhGia.class);
                    tong += danhGia.getRating();
                }
                holder.tvRating.setText(snapshot.getChildrenCount() + "");
                float tbRating = (float) Math.round((tong / snapshot.getChildrenCount()) * 10) / 10;
                holder.tvTBRating.setText(tbRating + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebase_manager.storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.ivFood);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("",e.getMessage());
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kAlertDialog = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Thông báo")
                        .setContentText("Bạn có muốn xóa ra khỏi danh sách không ?")
                        .setConfirmText("Có")
                        .setCancelText("Không").setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                firebase_manager.mDatabase.child("YeuThich").child(firebase_manager.auth.getUid()).child("Food").child(sanPham.getIDSanPham()).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        kAlertDialog.dismiss();
                                    }
                                });
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

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegation !=null){
                    delegation.getInformationFood(sanPham);
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
        return sanPhams.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNameFood, tvTenLoai, tvRating,tvTBRating,tvGia;
        ImageView ivFood, ivDelete;
        LinearLayout lnRating;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            tvNameFood.setOnClickListener(this);

            tvTenLoai = itemView.findViewById(R.id.tvTenLoai);
            tvTenLoai.setOnClickListener(this);

            tvRating = itemView.findViewById(R.id.tvRating);

            lnRating = itemView.findViewById(R.id.lnRating);
            lnRating.setOnClickListener(this);

            ivFood = itemView.findViewById(R.id.ivFood);
            ivFood.setOnClickListener(this);

            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivDelete.setOnClickListener(this);

            tvGia = itemView.findViewById(R.id.tvGia);

            tvTBRating = itemView.findViewById(R.id.tvTBRating);
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
}
