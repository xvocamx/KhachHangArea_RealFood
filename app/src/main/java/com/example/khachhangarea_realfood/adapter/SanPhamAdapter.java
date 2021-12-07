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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.ChiTietSanPham;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.GioHang;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DanhGia;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.LoaiSanPham;
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
    private Firebase_Manager firebase_manager = new Firebase_Manager();
    private KAlertDialog kAlertDialog;
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
        CardView view = (CardView) layoutInflater.inflate(viewType, parent, false);
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
        firebase_manager.mDatabase.child("DanhGia").orderByChild("idsanPham").equalTo(sanPham.getIDSanPham()).addValueEventListener(new ValueEventListener() {
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
        firebase_manager.LayTenLoai(sanPham,holder.tvLoaiSanPham);
        firebase_manager.LoadImageFood(sanPham,context, holder.ivSanPham);

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegation != null) {
                    delegation.getInformationFood(sanPham);
                }
            }
        };
        holder.ivMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              firebase_manager.  mDatabase.child("DonHangInfo").orderByChild("idkhachHang").equalTo(firebase_manager.auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                  @Override
                  public void onComplete(@NonNull  Task<DataSnapshot> task) {
                      boolean res = true;
                      String soLuong = "1";
                      UUID uuid = UUID.randomUUID();
                      String IDInfo = "MD_" + uuid.toString();
                      String donGia = sanPham.getGia();
                      DonHangInfo tempDonHang = new DonHangInfo();
                      DonHangInfo donHangInfo = new DonHangInfo(IDInfo, "", firebase_manager.auth.getUid(), soLuong, donGia, null, sanPham);
                      for (DataSnapshot dataSnapshot:task.getResult().getChildren()
                      ) {

                          DonHangInfo temp = dataSnapshot.getValue(DonHangInfo.class);
                          if (temp.getIDDonHang().equals(""))
                          {
                              if (donHangInfo.getSanPham().getIDSanPham().equals(temp.getSanPham().getIDSanPham()))
                              {
                                  res = false;
                                  tempDonHang = temp;
                                  break;
                              }
                          }
                      }
                      if (res == true)
                      {
                          firebase_manager.mDatabase.child("DonHangInfo").child(IDInfo).setValue(donHangInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {
                                  kAlertDialog = new KAlertDialog(context,KAlertDialog.SUCCESS_TYPE).setContentText("Thêm thành công").setConfirmText("OK");
                                  kAlertDialog.show();
                              }
                          });
                      }
                      else {
                          tempDonHang.setSoLuong((Integer.parseInt(tempDonHang.getSoLuong())+1)+"");
                          firebase_manager.mDatabase.child("DonHangInfo").child(tempDonHang.getIDInfo()).setValue(tempDonHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {
                                  kAlertDialog = new KAlertDialog(context,KAlertDialog.SUCCESS_TYPE)
                                          .setContentText("Thêm thành công 1 sản phẩm vào giỏ hàng" )
                                  .setConfirmText("OK");
                                  kAlertDialog.show();
                              }
                          });
                      }
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
        TextView tvRatings,tvTBRating;
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

    public Filter getGiaFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    arrayList = arrayListOld;
                } else {
                    ArrayList<SanPham> sanPhams = new ArrayList<>();
                    for (SanPham sanPham : arrayListOld) {
                        if (sanPham.getGia().toLowerCase().contains(strSearch.toLowerCase())) {
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