package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
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
import com.example.khachhangarea_realfood.model.SanPham;
import com.example.khachhangarea_realfood.model.Voucher;
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
    private CLickMaGiamGia delegation;

    public void setDelegation(CLickMaGiamGia delegation) {
        this.delegation = delegation;
    }

    public ThanhToanProAdapter(Activity context, int resource, ArrayList<GioHangDisplay> gioHangDisplays) {
        this.context = context;
        this.resource = resource;
        this.gioHangDisplays = gioHangDisplays;

    }


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

        if (gioHangDisplay == null || gioHangDisplay.getSanPhams().size() == 0) {
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
        ThanhToanAdapter gioHangAdapter = new ThanhToanAdapter(context, R.layout.list_item_thanhtoan_sanpham, donHangInfos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.rcSanPham.setLayoutManager(linearLayoutManager);
        holder.rcSanPham.setAdapter(gioHangAdapter);

        holder.lnThemGhiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.edtGhiChu.getVisibility() == View.GONE) {
                    holder.edtGhiChu.setVisibility(View.VISIBLE);
                } else {
                    holder.edtGhiChu.setVisibility(View.GONE);
                }
            }
        });
        if (gioHangDisplay.getGiamPhanTram() != 0) {
            holder.lnMaGiamGia.setVisibility(View.GONE);
        }
        if (gioHangDisplay.getGiaGiam() != 0) {
            holder.lnMaGiamGia.setVisibility(View.GONE);
        }
        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maGiamGia = holder.edtMaGiamGia.getText().toString().trim();

                firebase_manager.mDatabase.child("Voucher").orderByChild("maGiamGia").equalTo(maGiamGia).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Voucher voucher = dataSnapshot.getValue(Voucher.class);
                            if (voucher.getPhanTramGiam() != 0) {
                                for (int i = 0; i < gioHangDisplay.getSanPhams().size(); i++) {
                                    if (gioHangDisplay.getSanPhams().get(i).getSanPham().getIDSanPham().equals(voucher.getSanPham().getIDSanPham())) {
                                        gioHangDisplay.setGiamPhanTram(voucher.getPhanTramGiam());
                                        SanPham sanPham = gioHangDisplay.getSanPhams().get(i).getSanPham();
                                        double giaSanPham = Double.parseDouble(sanPham.getGia());
                                        double giaSanPhamGiamGia = giaSanPham - (giaSanPham * ((double) voucher.getPhanTramGiam() / 100));
                                        sanPham.setGia(giaSanPhamGiamGia + "");
                                        DonHangInfo donHangInfo = gioHangDisplay.getSanPhams().get(i);
                                        ArrayList<DonHangInfo> arrayList = gioHangDisplay.getSanPhams();
                                        donHangInfo.setSanPham(sanPham);
                                        arrayList.set(i, donHangInfo);
                                        gioHangDisplay.setSanPhams(arrayList);
                                        GioHangDisplay display = gioHangDisplay;
                                        gioHangDisplays.set(position, display);
                                        if (delegation != null) {
                                            delegation.getGiaGioHang(gioHangDisplays);
                                        }
                                        notifyDataSetChanged();
                                    }
                                }
                            }
                            if (voucher.getGiaGiam() != 0) {
                                for (int i = 0; i < gioHangDisplay.getSanPhams().size(); i++) {
                                    if (gioHangDisplay.getSanPhams().get(i).getSanPham().getIDSanPham().equals(voucher.getSanPham().getIDSanPham())) {
                                        gioHangDisplay.setGiamPhanTram(voucher.getGiaGiam());
                                        SanPham sanPham = gioHangDisplay.getSanPhams().get(i).getSanPham();
                                        double giaSanPham = Double.parseDouble(sanPham.getGia()) - voucher.getGiaGiam();
                                        sanPham.setGia(giaSanPham + "");
                                        DonHangInfo donHangInfo = gioHangDisplay.getSanPhams().get(i);
                                        ArrayList<DonHangInfo> arrayList = gioHangDisplay.getSanPhams();
                                        donHangInfo.setSanPham(sanPham);
                                        arrayList.set(i, donHangInfo);
                                        gioHangDisplay.setSanPhams(arrayList);
                                        GioHangDisplay display = gioHangDisplay;
                                        gioHangDisplays.set(position, display);
                                        if (delegation != null) {
                                            delegation.getGiaGioHang(gioHangDisplays);
                                        }
                                        notifyDataSetChanged();
                                    }
                                }
                            }

                        }
                        if (gioHangDisplay.getGiamPhanTram() == 0 && gioHangDisplay.getGiaGiam() == 0) {
                            kAlertDialog = new KAlertDialog(context, KAlertDialog.ERROR_TYPE).setConfirmText("OK");
                            kAlertDialog.show();

                        }
                        if (gioHangDisplay.getGiamPhanTram() != 0 || gioHangDisplay.getGiaGiam() != 0) {
                            kAlertDialog = new KAlertDialog(context, KAlertDialog.SUCCESS_TYPE).setConfirmText("OK");
                            kAlertDialog.show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
        return gioHangDisplays.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenCuaHang;
        Button btnSave;
        LinearLayout lnThemGhiChu;
        EditText edtGhiChu, edtMaGiamGia;
        ImageView ivShop;
        ProgressBar pbLoadItemGioHang;
        RecyclerView rcSanPham;
        LinearLayout lnMaGiamGia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivShop = itemView.findViewById(R.id.ivShop);
            tvTenCuaHang = itemView.findViewById(R.id.tvNameShop);
            pbLoadItemGioHang = itemView.findViewById(R.id.pbLoadItemGioHang);
            rcSanPham = itemView.findViewById(R.id.rcvItemGiohang);
            lnThemGhiChu = itemView.findViewById(R.id.lnThemGhiChu);
            edtGhiChu = itemView.findViewById(R.id.edtGhiChu);
            edtMaGiamGia = itemView.findViewById(R.id.edtMaGiamGia);
            btnSave = itemView.findViewById(R.id.btnSave);
            lnMaGiamGia = itemView.findViewById(R.id.lnMaGiamGia);
            this.setIsRecyclable(false);
        }
    }

    public interface CLickMaGiamGia {
        void getGiaGioHang(ArrayList<GioHangDisplay> gioHangDisplays);
    }


}
