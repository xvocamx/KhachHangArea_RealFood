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
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DanhGia;
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

public class YeuThichShopAdapter extends RecyclerView.Adapter<YeuThichShopAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<CuaHang> cuaHangs;
    private Firebase_Manager firebase_manager = new Firebase_Manager();
    private KAlertDialog kAlertDialog;
    private ClickItemShopListener delegation;

    public void setDelegation(ClickItemShopListener delegation) {
        this.delegation = delegation;
    }

    public YeuThichShopAdapter(Activity context, int resource, ArrayList<CuaHang> cuaHangs) {
        this.context = context;
        this.resource = resource;
        this.cuaHangs = cuaHangs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull YeuThichShopAdapter.MyViewHolder holder, int position) {
        CuaHang cuaHang = cuaHangs.get(position);
        if (cuaHang == null) {
            return;
        }
        holder.ivShop.setImageResource(R.drawable.logo_pizza);
        holder.tvTenCuaHang.setText(cuaHang.getTenCuaHang());
        holder.tvDiaChi.setText(cuaHang.getDiaChi());
        firebase_manager.mDatabase.child("DanhGia").orderByChild("idcuaHang").equalTo(cuaHang.getIDCuaHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float tong = 0f;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DanhGia danhGia = dataSnapshot.getValue(DanhGia.class);
                    tong += danhGia.getRating();
                }
                holder.tvRatings.setText(snapshot.getChildrenCount() + "");
                float tbRating = (float) Math.round((tong / snapshot.getChildrenCount()) * 10) / 10;
                holder.tvTBRating.setText(tbRating + "");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebase_manager.storageRef.child("CuaHang").child(cuaHang.getIDCuaHang()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.ivShop);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });
        //Xoa item ra khoi list
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kAlertDialog = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Th??ng b??o")
                        .setContentText("B???n c?? mu???n x??a ra kh???i danh s??ch kh??ng ?")
                        .setConfirmText("C??")
                        .setCancelText("Kh??ng").setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                firebase_manager.mDatabase.child("YeuThich").child(firebase_manager.auth.getUid()).child("Shop").child(cuaHang.getIDCuaHang()).removeValue(new DatabaseReference.CompletionListener() {
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
                if(delegation != null){
                    delegation.getInformationShop(cuaHang);
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
        TextView tvTenCuaHang, tvDiaChi, tvRatings,tvTBRating;
        ImageView ivShop, ivDelete;
        LinearLayout lnRating;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenCuaHang = itemView.findViewById(R.id.tvNameShop);
            tvTenCuaHang.setOnClickListener(this);

            tvDiaChi = itemView.findViewById(R.id.tvAddressShop);
            tvDiaChi.setOnClickListener(this);

            tvRatings = itemView.findViewById(R.id.tvRating);

            lnRating = itemView.findViewById(R.id.lnRating);
            lnRating.setOnClickListener(this);

            ivShop = itemView.findViewById(R.id.ivShop);
            ivShop.setOnClickListener(this);

            ivDelete = itemView.findViewById(R.id.ivDelete);

            tvTBRating = itemView.findViewById(R.id.tvTBRating);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }
    public interface ClickItemShopListener {
        void getInformationShop(CuaHang cuaHang);
    }
}
