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
import com.example.khachhangarea_realfood.model.CuaHang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class YeuThichShopAdapter extends RecyclerView.Adapter<YeuThichShopAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<CuaHang> cuaHangs;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
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
        holder.tvTenCuaHang.setText(cuaHang.getTenCuaHang());
        holder.tvDiaChi.setText(cuaHang.getDiaChi());
        holder.tvRatings.setText(String.valueOf(cuaHang.getRating()));
        storageRef.child("CuaHang").child(cuaHang.getIDCuaHang()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                        .setTitleText("Thông báo")
                        .setContentText("Bạn có muốn xóa ra khỏi danh sách không ?")
                        .setConfirmText("Có")
                        .setCancelText("Không").setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                mDatabase.child("YeuThich").child(auth.getUid()).child("Shop").child(cuaHang.getIDCuaHang()).removeValue(new DatabaseReference.CompletionListener() {
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
        TextView tvTenCuaHang, tvDiaChi, tvRatings;
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
