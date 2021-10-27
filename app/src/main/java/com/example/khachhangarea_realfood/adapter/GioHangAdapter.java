package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.ChiTietCuaHang;
import com.example.khachhangarea_realfood.ChiTietSanPham;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    private Activity context;
    private int resource;
    private ArrayList<DonHangInfo> donHangInfos;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private KAlertDialog kAlertDialog;
    private CheckBoxListener checkBoxListener;

    public void setCheckBoxListener(CheckBoxListener checkBoxListener) {
        this.checkBoxListener = checkBoxListener;
    }

    public GioHangAdapter(Activity context, int resource, ArrayList<DonHangInfo> donHangInfos) {
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
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
        DonHangInfo donHangInfo = donHangInfos.get(position);
        if (donHangInfo == null) {
            return;
        }
        holder.tvTenSanPham.setText(donHangInfo.getSanPham().getTenSanPham());
        String gia = String.valueOf(Integer.valueOf(donHangInfo.getSanPham().getGia()));
        holder.tvGia.setText(gia);
        storageRef.child("SanPham").child(donHangInfo.getSanPham().getIDCuaHang()).child(donHangInfo.getSanPham().getIDSanPham()).child(donHangInfo.getSanPham().getImages().get(0)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Glide.with(context)
                        .load(task.getResult().toString())
                        .into(holder.ivSanPham);
                holder.pbLoadItemGioHang.setVisibility(View.GONE);
            }
        });
        mDatabase.child("CuaHang").child(donHangInfo.getSanPham().getIDCuaHang()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CuaHang cuaHang = snapshot.getValue(CuaHang.class);
                holder.tvTenCuaHang.setText(cuaHang.getTenCuaHang());
                holder.btnXemShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ChiTietCuaHang.class);
                        Gson gson = new Gson();
                        String data = gson.toJson(cuaHang);
                        intent.putExtra("dataCuaHang", data);
                        context.startActivity(intent);
                        Toast.makeText(context, cuaHang.getIDCuaHang() + "", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabase.child("DonHangInfo").child(auth.getUid()).child(donHangInfo.getIDInfo()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.btnSoLuong.setNumber(donHangInfo.getSoLuong());
        holder.btnSoLuong.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if (newValue == 0) {
                    kAlertDialog =
                            new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Thông báo")
                                    .setContentText("Bạn có muốn xóa sản phẩm này không??")
                                    .setConfirmText("Có")
                                    .setCancelText("Không").setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    mDatabase.child("DonHangInfo").child(auth.getUid()).child(donHangInfo.getIDInfo()).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            kAlertDialog.dismiss();
                                            LoadChecked(position,holder);
                                        }
                                    });
                                }
                            }).setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    holder.btnSoLuong.setNumber(1 + "");
                                    kAlertDialog.dismiss();
                                    LoadChecked(position,holder);
                                }
                            });

                    kAlertDialog.show();
                } else {

                    donHangInfo.setSoLuong(String.valueOf(newValue));
                    mDatabase.child("DonHangInfo").child(auth.getUid()).child(donHangInfo.getIDInfo()).setValue(donHangInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            LoadChecked(position,holder);
                        }
                    });
                }
                Toast.makeText(context, booleanArray.size()+"", Toast.LENGTH_SHORT).show();
            }
        });


        holder.ckbSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!booleanArray.get(position, false)) {
                    holder.ckbSanPham.setChecked(true);
                    booleanArray.put(position, true);

                } else {
                    holder.ckbSanPham.setChecked(false);
                    booleanArray.put(position, false);
                }
                checkBoxListener.getGiaGioHang();
            }
        });
        LoadChecked(position,holder);

    }

    private void LoadChecked(int position, MyViewHolder holder) {

        SparseBooleanArray sparse = booleanArray;
        for (int i = 0; i < sparse.size(); i++) {
            if (sparse.valueAt(i)) {
                Log.d("position of key",""+sparse.indexOfKey(i));
                Log.d("position ",""+position);

                if (sparse.keyAt(i)==position)
                {
                    holder.ckbSanPham.setChecked(true);
                    checkBoxListener.getGiaGioHang();
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return donHangInfos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTenCuaHang, tvTenSanPham, tvGia;
        ImageView ivShop, ivSanPham;
        EditText edtMaGiamGia;
        CheckBox ckbSanPham;
        Button btnXoa, btnXemShop;
        ElegantNumberButton btnSoLuong;
        ProgressBar pbLoadItemGioHang;
        View.OnClickListener onClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSanPham = itemView.findViewById(R.id.ivSanPham);
            ivShop = itemView.findViewById(R.id.ivShop);
            tvTenCuaHang = itemView.findViewById(R.id.tvTenCuaHang);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvGia = itemView.findViewById(R.id.tvGia);
            edtMaGiamGia = itemView.findViewById(R.id.edtMaGiamGia);
            ckbSanPham = itemView.findViewById(R.id.ckbSanPham);
            ckbSanPham.setOnClickListener(this);
            btnXoa = itemView.findViewById(R.id.btnXoaSanPham);
            btnXemShop = itemView.findViewById(R.id.btnXemShop);
            btnSoLuong = itemView.findViewById(R.id.btnSoLuong);
            pbLoadItemGioHang = itemView.findViewById(R.id.pbLoadItemGioHang);
            this.setIsRecyclable(false);
        }


        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }

    }

    public interface CheckBoxListener {
        void getGiaGioHang();
    }


}