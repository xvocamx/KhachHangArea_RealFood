package com.example.khachhangarea_realfood;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiCuaHang;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.khachhangarea_realfood.adapter.CuaHangAdapter;
import com.example.khachhangarea_realfood.adapter.DonMuaAdpater;
import com.example.khachhangarea_realfood.adapter.DonMuaChuanBiHangAdpater;
import com.example.khachhangarea_realfood.adapter.DonMuaDaNhanHangAdpater;
import com.example.khachhangarea_realfood.adapter.LoaiSanPhamAdapter;
import com.example.khachhangarea_realfood.adapter.SanPhamAdapter;
import com.example.khachhangarea_realfood.adapter.YeuThichFoodAdapter;
import com.example.khachhangarea_realfood.adapter.YeuThichShopAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.example.khachhangarea_realfood.model.LoaiSanPham;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Firebase_Manager {
    public DatabaseReference mDatabase;
    public StorageReference storageRef;
    public FirebaseAuth auth;
    public FirebaseUser user;

    public Firebase_Manager() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public String GetStringTrangThaiDonHang(TrangThaiDonHang trangThaiDonHang) {
        String res = "";
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_HuyDonHang) {
            res = "Đã hủy";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien) {
            res = "Chờ xác nhận chuyển tiền cọc";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DaGiaoChoBep) {
            res = "Đã giao đơn hàng cho bếp";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DangChuanBihang) {
            res = "Đang chuẩn bị hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DaChuanBiXong) {
            res = "Đã chuẩn bị xong";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_DangGiaoShipper) {
            res = "Đang giao shipper đi phát";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoShipperLayHang) {
            res = "Chờ shipper lấy hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.SHOP_ChoXacNhanGiaoHangChoShipper) {
            res = "Chờ Shop xác nhận giao hàng cho Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_Tien) {
            res = "Chờ Shop xác nhận đã nhận tiền hàng từ Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.ChoShopXacNhan_TraHang) {
            res = "Chờ Shop xác nhận đã nhận hàng trả về từ Shipper";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaLayHang) {
            res = "Shipper đã lấy hàng đi giao";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_KhongNhanGiaoHang) {
            res = "Shipper không nhận giao hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaTraHang) {
            res = "Đơn hàng đã được hoàn về";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DaChuyenTien) {
            res = "Đơn hàng đã thanh toán thành công";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_GiaoKhongThanhCong) {
            res = "Giao hàng không thành công";
        }
        if (trangThaiDonHang == TrangThaiDonHang.Shipper_DangGiaoHang) {
            res = "Shipper đang giao hàng";
        }
        if (trangThaiDonHang == TrangThaiDonHang.KhachHang_HuyDon) {
            res = "Khách hàng hủy đơn";
        }


        return res;
    }

    public void LoadImageKhachHang(Context context, ImageView civAvatar) {
        storageRef.child("KhachHang").child(user.getUid()).child("AvatarKhachHang").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(civAvatar);
            }
        });
    }

    public void LoadTenKhachHang(TextView tvHoTen) {
        mDatabase.child("KhachHang").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                tvHoTen.setText(khachHang.getTenKhachHang());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadDaHuyDonHang(ArrayList<DonHang> donHangs, DonMuaAdpater donMuaAdpater) {
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getTrangThai().toString().equals(TrangThaiDonHang.SHOP_HuyDonHang.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.Shipper_GiaoKhongThanhCong.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.Shipper_DaTraHang.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.ChoShopXacNhan_TraHang.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.KhachHang_HuyDon.toString())) {
                        donHangs.add(donHang);
                        donMuaAdpater.notifyDataSetChanged();
                    }
                }

                //Sap sep theo thoi gian
                Collections.sort(donHangs, new Comparator<DonHang>() {
                    @Override
                    public int compare(DonHang o1, DonHang o2) {
                        return o2.getNgayTao().compareTo(o1.getNgayTao());
                    }
                });
                if (donHangs.size() == 0) {
                    donHangs.clear();
                    donMuaAdpater.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadDaNhanDonHang(ArrayList<DonHang> donHangs, DonMuaDaNhanHangAdpater donMuaDaNhanHangAdpater) {
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getTrangThai().toString().equals(TrangThaiDonHang.Shipper_GiaoThanhCong.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.Shipper_DaChuyenTien.toString())) {
                        donHangs.add(donHang);
                        donMuaDaNhanHangAdpater.notifyDataSetChanged();
                    }
                }

                //Sap sep theo thoi gian
                Collections.sort(donHangs, new Comparator<DonHang>() {
                    @Override
                    public int compare(DonHang o1, DonHang o2) {
                        return o2.getNgayTao().compareTo(o1.getNgayTao());
                    }
                });
                if (donHangs.size() == 0) {
                    donHangs.clear();
                    donMuaDaNhanHangAdpater.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadDangGiaoDonHang(ArrayList<DonHang> donHangs, DonMuaAdpater donMuaAdpater) {
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getTrangThai().toString().equals(TrangThaiDonHang.SHOP_DangGiaoShipper.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.SHOP_ChoShipperLayHang.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.Shipper_DaLayHang.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.Shipper_DangGiaoHang.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.Shipper_KhongNhanGiaoHang.toString())
                    ) {
                        donHangs.add(donHang);
                        donMuaAdpater.notifyDataSetChanged();
                    }
                }

                //Sap sep theo thoi gian
                Collections.sort(donHangs, new Comparator<DonHang>() {
                    @Override
                    public int compare(DonHang o1, DonHang o2) {
                        return o2.getNgayTao().compareTo(o1.getNgayTao());
                    }
                });
                if (donHangs.size() == 0) {
                    donHangs.clear();
                    donMuaAdpater.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadChuanBiDonHang(ArrayList<DonHang> donHangs, DonMuaChuanBiHangAdpater donMuaChuanBiHangAdpater) {
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getTrangThai().toString().equals(TrangThaiDonHang.SHOP_DangChuanBihang.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.SHOP_DaChuanBiXong.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.SHOP_DaGiaoChoBep.toString()) ||
                            donHang.getTrangThai().toString().equals(TrangThaiDonHang.Bep_DaHuyDonHang.toString())) {
                        donHangs.add(donHang);
                        donMuaChuanBiHangAdpater.notifyDataSetChanged();
                    }
                }
                //Sap sep theo thoi gian
                Collections.sort(donHangs, new Comparator<DonHang>() {
                    @Override
                    public int compare(DonHang o1, DonHang o2) {
                        return o2.getNgayTao().compareTo(o1.getNgayTao());
                    }
                });
                if (donHangs.size() == 0) {
                    donHangs.clear();
                    donMuaChuanBiHangAdpater.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadDonHangChoXacNhan(ArrayList<DonHang> donHangs, DonMuaAdpater donMuaAdpater, Context context) {
        mDatabase.child("DonHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    if (donHang.getTrangThai().toString().equals(TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien.toString())) {
                        donHangs.add(donHang);
                        donMuaAdpater.notifyDataSetChanged();

                    }
                }
                //Sap sep theo thoi gian
                Collections.sort(donHangs, new Comparator<DonHang>() {
                    @Override
                    public int compare(DonHang o1, DonHang o2) {
                        return o2.getNgayTao().compareTo(o1.getNgayTao());
                    }
                });
                if (donHangs.size() == 0) {
                    donHangs.clear();
                    donMuaAdpater.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ThemVaoGioHang(DonHangInfo donHangInfo, String IDInfo, Context context) {

        mDatabase.child("DonHangInfo").orderByChild("idkhachHang").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean res = true;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()
                ) {

                    DonHangInfo temp = dataSnapshot.getValue(DonHangInfo.class);
                    if (temp.getIDDonHang().equals("")) {
                        if (donHangInfo.getSanPham().getIDSanPham().equals(temp.getSanPham().getIDSanPham())) {
                            res = false;
                            break;
                        }
                    }
                }
                if (res == true) {
                    mDatabase.child("DonHangInfo").child(IDInfo).setValue(donHangInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public UploadTask UpImageBaoCao(Uri image, String cuaHang) {
        return storageRef.child("BaoCao").child(cuaHang).child("ImageBaoCao").putFile(image);
    }

    public void ThemYeuThichCuaHang(CuaHang cuaHang) {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Shop").child(cuaHang.getIDCuaHang()).setValue(cuaHang);
    }

    public void XoaYeuThichCuaHang(CuaHang cuaHang) {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Shop").child(cuaHang.getIDCuaHang()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });
    }

    public void ThemYeuThichFood(SanPham sanPham) {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Food").child(sanPham.getIDSanPham()).setValue(sanPham);
    }

    public void XoaYeuThichFood(SanPham sanPham) {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Food").child(sanPham.getIDSanPham()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });
    }


    public void LayTenLoai(SanPham sanPham, TextView tvTenLoai) {
        mDatabase.child("LoaiSanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiSanPham loaiSanPham = dataSnapshot.getValue(LoaiSanPham.class);
                    if (loaiSanPham.getiDLoai().equals(sanPham.getIDLoai())) {
                        tvTenLoai.setText(loaiSanPham.getTenLoai());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadImageLoai(LoaiSanPham loaiSanPham, Context context, ImageView ivLoai) {
        storageRef.child("LoaiSanPham").child(loaiSanPham.getiDLoai()).child("Loại sản phẩm").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(context).load(uri).into(ivLoai);
                } catch (Exception ex) {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });
    }

    public void LoadImageFood(SanPham sanPham, Context context, ImageView ivFood) {
        storageRef.child("SanPham").child(sanPham.getIDCuaHang()).child(sanPham.getIDSanPham()).child(sanPham.getImages().get(0)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(context).load(uri).into(ivFood);
                } catch (Exception exception) {

                }
            }
        });
    }

    public void LoadWallPaperCuaHang(CuaHang cuaHang, Context context, ImageView ivWallPaper) {
        storageRef.child("CuaHang").child(cuaHang.getIDCuaHang()).child("WallPaper").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(context).load(uri).into(ivWallPaper);
                } catch (Exception exception) {

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });
    }

    public void LoadLogoCuaHang(CuaHang cuaHang, Context context, ImageView civAvatar) {
        storageRef.child("CuaHang").child(cuaHang.getIDCuaHang()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide.with(context).load(uri).into(civAvatar);
                } catch (Exception ex) {

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });
    }

    public void LoadYeuThichShop(ArrayList<CuaHang> cuaHangs, YeuThichShopAdapter yeuThichShopAdapter, ProgressBar pb) {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Shop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cuaHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CuaHang cuaHangAll = dataSnapshot.getValue(CuaHang.class);
                    cuaHangs.add(cuaHangAll);
                    yeuThichShopAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void LoadYeuThichFood(ArrayList<SanPham> sanPhams, YeuThichFoodAdapter yeuThichFoodAdapter, ProgressBar pb) {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    yeuThichFoodAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetSanPhamTheoLoai(ArrayList<SanPham> sanPhams, SanPhamAdapter sanPhamAdapter, LoaiSanPham loaiSanPham, ProgressBar pb) {
        mDatabase.child("SanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    if (sanPham.getIDLoai().equals(loaiSanPham.getiDLoai())) {
                        sanPhams.add(sanPham);
                        sanPhamAdapter.notifyDataSetChanged();
                    }
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetLoaiSanPham(ArrayList<LoaiSanPham> loaiSanPhams, LoaiSanPhamAdapter loaiSanPhamAdapter, ProgressBar pb) {
        mDatabase.child("LoaiSanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loaiSanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LoaiSanPham loaiSanPham = dataSnapshot.getValue(LoaiSanPham.class);
                    loaiSanPhams.add(loaiSanPham);
                    loaiSanPhamAdapter.notifyDataSetChanged();
                }
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetPopularShop(ArrayList<CuaHang> cuaHangs, CuaHangAdapter cuaHangAdapter) {
        mDatabase.child("CuaHang").limitToFirst(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cuaHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CuaHang cuaHang = dataSnapshot.getValue(CuaHang.class);
                    if (cuaHang.getTrangThaiCuaHang() != TrangThaiCuaHang.ChuaKichHoat) {
                        cuaHangs.add(cuaHang);
                        cuaHangAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetPopularFood(ArrayList<SanPham> sanPhams, SanPhamAdapter sanPhamAdapter) {
        mDatabase.child("SanPham").limitToFirst(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void GetSaleFood(ArrayList<SanPham> sanPhams, SanPhamAdapter sanPhamAdapter) {
        mDatabase.child("SanPham").limitToFirst(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
