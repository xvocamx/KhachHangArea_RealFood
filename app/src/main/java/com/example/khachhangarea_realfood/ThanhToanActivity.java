package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.TrangThai.LoaiThongBao;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiThongBao;
import com.example.khachhangarea_realfood.adapter.GioHangAdapter;
import com.example.khachhangarea_realfood.adapter.GioHangProAdapter;
import com.example.khachhangarea_realfood.adapter.ThanhToanAdapter;
import com.example.khachhangarea_realfood.adapter.ThanhToanProAdapter;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.GioHangDisplay;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.example.khachhangarea_realfood.model.TaiKhoanNganHang;
import com.example.khachhangarea_realfood.model.ThongBao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ThanhToanActivity extends AppCompatActivity {

    TextView tvTenNguoiNhan, tvTongTien;
    private ArrayList<DonHangInfo> donHangInfos;
    private RecyclerView rcvThanhToan;
    private LinearLayoutManager linearLayoutManager;
    private DonHangInfo donHangInfo;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText edtDiaChi, edtSoDienThoai;
    private ProgressBar pbLoadGioHang;
    private Button btnThanhToan;
    String diaChi, soDienThoai;
    ThanhToanProAdapter thanhToanProAdapter;
    ArrayList<GioHangDisplay> gioHangDisplays = new ArrayList<>();
    ArrayList<String> idCuaHang = new ArrayList<>();

    Firebase_Manager firebase_manager = new Firebase_Manager();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        donHangInfos = new ArrayList<>();
        donHangInfo = new DonHangInfo();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            String data = bundle.getString("data");
            gioHangDisplays = new Gson().fromJson(data, new TypeToken<ArrayList<GioHangDisplay>>() {
            }.getType());
            for (GioHangDisplay gioHangDisplay : gioHangDisplays) {
                gioHangDisplay.setSanPhams((ArrayList<DonHangInfo>) gioHangDisplay.getSanPhams().stream().filter(donHangInfo1 -> donHangInfo1.isSelected() == true).collect(Collectors.toList()));
            }
            gioHangDisplays.removeIf(gioHangDisplay -> gioHangDisplay.getSanPhams().size() == 0);
            thanhToanProAdapter = new ThanhToanProAdapter(this, R.layout.list_item_thanhtoan, gioHangDisplays);

        }


        setControl();
        setEvent();
        LoadData();

    }

    private void LoadData() {
        firebase_manager.mDatabase.child("KhachHang").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                tvTenNguoiNhan.setText(khachHang.getTenKhachHang());
                edtDiaChi.setText(khachHang.getDiaChi());
                edtSoDienThoai.setText(khachHang.getSoDienThoai());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        TinhTongTien(gioHangDisplays);
    }

    private void TinhTongTien(ArrayList<GioHangDisplay> displays) {
        int tong = 0;

        for (GioHangDisplay gioHangDisplay : displays) {
            for (DonHangInfo donHangInfo : gioHangDisplay.getSanPhams()
            ) {
                tong += Double.parseDouble(donHangInfo.getSanPham().getGia()) * Integer.parseInt(donHangInfo.getSoLuong());
            }
        }

        tvTongTien.setText(tong + "");
    }

    private void LoadGioHang() {
        mDatabase.child("DonHangInfo").orderByChild("idkhachHang").equalTo(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangInfos.clear();
                gioHangDisplays.clear();
                idCuaHang.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    if (donHangInfo.getIDDonHang().isEmpty()) {
                        donHangInfos.add(donHangInfo);
                    }
                }
                pbLoadGioHang.setVisibility(View.GONE);
                for (int i = 0; i < donHangInfos.size(); i++) {
                    String id = donHangInfos.get(i).getSanPham().getIDCuaHang();
                    for (int j = 0; j < donHangInfos.size(); j++) {
                        if (donHangInfos.get(j).getSanPham().getIDCuaHang() == id) {
                            if (!CheckExitID(id)) {
                                idCuaHang.add(id);
                            }
                        }
                    }
                }
                for (String id : idCuaHang) {
                    GioHangDisplay gioHangDisplay = new GioHangDisplay();
                    gioHangDisplay.setIdCuaHang(id);
                    ArrayList<DonHangInfo> temp = new ArrayList<>();
                    for (DonHangInfo donHangInfo : donHangInfos) {
                        if (donHangInfo.getSanPham().getIDCuaHang().equals(id)) {
                            temp.add(donHangInfo);
                        }
                    }
                    gioHangDisplay.setSanPhams(temp);
                    gioHangDisplays.add(gioHangDisplay);
                    thanhToanProAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean CheckExitID(String id) {
        for (String temp : idCuaHang) {
            if (temp.equals(id)) {
                return true;
            }
        }
        return false;
    }


    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvThanhToan.setLayoutManager(linearLayoutManager);
        rcvThanhToan.setAdapter(thanhToanProAdapter);
        thanhToanProAdapter.setDelegation(new ThanhToanProAdapter.CLickMaGiamGia() {
            @Override
            public void getGiaGioHang(ArrayList<GioHangDisplay> gioHangDisplays) {
                TinhTongTien(gioHangDisplays);
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaChi = edtDiaChi.getText().toString();
                soDienThoai = edtSoDienThoai.getText().toString();
                if (diaChi.isEmpty()) {
                    edtDiaChi.setError("Vui lòng nhập địa chỉ");
                } else if (soDienThoai.isEmpty()) {
                    edtSoDienThoai.setError("Vui lòng nhập số điện thoại");
                } else {
                    KAlertDialog kAlertDialog = new KAlertDialog(ThanhToanActivity.this, KAlertDialog.PROGRESS_TYPE);
                    kAlertDialog.show();
                    for (GioHangDisplay gioHangDisplay : gioHangDisplays) {
                        int tongTien = 0;
                        for (DonHangInfo donHangInfo : gioHangDisplay.getSanPhams()) {
                            tongTien += Double.parseDouble(donHangInfo.getSanPham().getGia()) * Integer.parseInt(donHangInfo.getSoLuong());
                        }
                        String IDDonHang = "DH_" + UUID.randomUUID().toString();
                        DonHang donHang = new DonHang(IDDonHang, gioHangDisplay.getIdCuaHang(), firebase_manager.auth.getUid()
                                , "", diaChi, soDienThoai, gioHangDisplay.getGhiChu(), "", tongTien, new Date(), null, TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien
                        );
                        firebase_manager.mDatabase.child("DonHang").child(IDDonHang).setValue(donHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                for (DonHangInfo donHangInfo : gioHangDisplay.getSanPhams()) {
                                    donHangInfo.setIDDonHang(IDDonHang);
                                    firebase_manager.mDatabase.child("DonHangInfo").child(donHangInfo.getIDInfo()).setValue(donHangInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                            kAlertDialog.setContentText("Đặt hàng thành công!");
                                            kAlertDialog.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog kAlertDialog) {
                                                    finish();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                            kAlertDialog.setContentText(e.getMessage());
                                        }
                                    });
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                kAlertDialog.setContentText(e.getMessage());
                            }
                        });
                        String tenSanPham = "";
                        for (DonHangInfo donHangInfo : gioHangDisplay.getSanPhams()) {
                            tenSanPham = donHangInfo.getSanPham().getTenSanPham() + ", " + tenSanPham;
                        }
                        String IDThongBao = UUID.randomUUID().toString();
                        String finalTenSanPham = tenSanPham;
                        firebase_manager.mDatabase.child("TaiKhoanNganHang").orderByChild("idTaiKhoan").equalTo(donHang.getIDCuaHang()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    //Thong bao don hang cho khach hang
                                    TaiKhoanNganHang taiKhoanNganHang = dataSnapshot.getValue(TaiKhoanNganHang.class);
                                    double soTien = Math.round(donHang.getTongTien() * 0.1 * 10) / 10;
                                    String noiDung = "Bạn đã đặt hàng thành công " + donHang.getIDDonHang().substring(0, 10)
                                            + "\nVui lòng chuyển khoản với cú pháp: \n" + donHang.getIDDonHang().substring(0, 10) + " " + soTien
                                            + "   đến số tài khoản " + taiKhoanNganHang.getSoTaiKhoan() + " ,"
                                            + taiKhoanNganHang.getTenChuTaiKhoan() + " ," + taiKhoanNganHang.getTenNganHang();
                                    Alerter.create(ThanhToanActivity.this)
                                            .setTitle("Thông báo")
                                            .setText(noiDung)
                                            .setBackgroundColorRes(R.color.success_stroke_color) // or setBackgroundColorInt(Color.CYAN)
                                            .show();
                                    ThongBao thongBao = new ThongBao(IDThongBao, noiDung, "Thông báo", "", firebase_manager.auth.getUid(), "", TrangThaiThongBao.ChuaXem, new Date());
                                    thongBao.setLoaiThongBao(LoaiThongBao.DONHANG_MOI);
                                    thongBao.setDonHang(donHang);
                                    firebase_manager.mDatabase.child("ThongBao").child(firebase_manager.auth.getUid()).child(IDThongBao).setValue(thongBao);

                                    //Thong bao don hang cho shop
                                    String noiDungShop = "Đơn hàng mới " + donHang.getIDDonHang().substring(0, 15) + " : " + finalTenSanPham + " " + donHang.getTongTien() + "VND";
                                    ThongBao thongBaoShop = new ThongBao(IDThongBao, noiDungShop, "Thông báo", "", donHang.getIDCuaHang(), "", TrangThaiThongBao.ChuaXem, new Date());
                                    thongBaoShop.setLoaiThongBao(LoaiThongBao.DONHANG_MOI);
                                    thongBaoShop.setDonHang(donHang);
                                    firebase_manager.mDatabase.child("ThongBao").child(donHang.getIDCuaHang()).child(IDThongBao).setValue(thongBaoShop);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

            }
        });

    }


    private void setControl() {
        rcvThanhToan = findViewById(R.id.rcvThanhToan);
        tvTongTien = findViewById(R.id.tvTongTien);
        pbLoadGioHang = findViewById(R.id.pbLoadGioHang);
        edtDiaChi = findViewById(R.id.edtDiaChiNhanHang);
        tvTenNguoiNhan = findViewById(R.id.txtTenNguoiNhan);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
    }
}