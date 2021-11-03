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
import com.example.khachhangarea_realfood.R;
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
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
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
        mDatabase.child("LoaiSanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiSanPham loaiSanPham = dataSnapshot.getValue(LoaiSanPham.class);
                    if (loaiSanPham.getiDLoai().equals(sanPham.getIDLoai())) {
                        holder.tvTenLoai.setText(loaiSanPham.getTenLoai());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.tvRating.setText(String.valueOf(sanPham.getRating()));
        storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                mDatabase.child("YeuThich").child(auth.getUid()).child("Food").child(sanPham.getIDSanPham()).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        kAlertDialog.dismiss();
                                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
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
        TextView tvNameFood, tvTenLoai, tvRating;
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
