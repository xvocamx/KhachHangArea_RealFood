package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.adapter.CuaHangAdapter;
import com.example.khachhangarea_realfood.adapter.GioHangAdapter;
import com.example.khachhangarea_realfood.adapter.ItemGioHangAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class GioHang extends AppCompatActivity {
    private LinearLayout lnGhiChu;
    private EditText edtGhiChu;
    private GioHangAdapter gioHangAdapter;
    private ArrayList<DonHangInfo> donHangInfos;
    private RecyclerView rcvGioHang;
    private LinearLayoutManager linearLayoutManager;
    private DonHangInfo donHangInfo;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextView tvTongPhu, tvChiPhiVanChuyen, tvTongTien, tvNoiDung, tvTien;
    private ProgressBar pbLoadGioHang;
    private Button btnThanhToan, btnXacNhan, btnHuy;
    private KAlertDialog kAlertDialog;
    int tong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        donHangInfos = new ArrayList<>();
        donHangInfo = new DonHangInfo();
        gioHangAdapter = new GioHangAdapter(this, R.layout.list_item_giohang_sanpham, donHangInfos);
        setControl();
        setEvent();

    }

    private void LoadGioHang() {
        mDatabase.child("DonHangInfo").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donHangInfos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHangInfo donHangInfo = dataSnapshot.getValue(DonHangInfo.class);
                    donHangInfos.add(donHangInfo);
                }
                gioHangAdapter.notifyDataSetChanged();
                pbLoadGioHang.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvGioHang.setLayoutManager(linearLayoutManager);
        rcvGioHang.setAdapter(gioHangAdapter);
        LoadGioHang();


        lnGhiChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtGhiChu.getVisibility() == v.GONE) {
                    edtGhiChu.setVisibility(View.VISIBLE);
                } else {
                    edtGhiChu.setVisibility(View.GONE);
                }
            }
        });

        gioHangAdapter.setCheckBoxListener(new GioHangAdapter.CheckBoxListener() {
            @Override
            public void getGiaGioHang() {
                tong = 0;
                SparseBooleanArray sparse = gioHangAdapter.getBooleanArray();
                for (int i = 0; i < sparse.size(); i++) {
                    if (sparse.valueAt(i)) {
                        DonHangInfo donHangInfo = donHangInfos.get(sparse.keyAt(i));
                        tong += Integer.parseInt(donHangInfo.getDonGia()) * Integer.parseInt(donHangInfo.getSoLuong());
                    }

                }
                tvTongPhu.setText(tong + "");
                if (tong == 0) {

                    tvChiPhiVanChuyen.setText(0 + "");
                    tvTongTien.setText(0 + "");
                }
                else {
                    int phiVanChuyen = 30000;
                    tvChiPhiVanChuyen.setText(phiVanChuyen + "");
                    int tongTien = tong + phiVanChuyen;
                    tvTongTien.setText(tongTien + "");
                    btnThanhToan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double tien = tongTien;
                            openThanhToan(Gravity.CENTER, tien);
                        }
                    });
                }
            }
        });

    }

    private void openThanhToan(int gravity, double tien) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_thanhtoan);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);


        dialog.setCancelable(false);

        btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
        btnHuy = dialog.findViewById(R.id.btnHuy);
        tvNoiDung = dialog.findViewById(R.id.tvNoiDung);
        tvTien = dialog.findViewById(R.id.tvTien);
        String noiDung = "Chuyển tiền đến số tài khoản\n" + "1212425256\n" + "Dat\n" + "Ngân hàng: " + "Ngân hàng Thương mại TNHH MTV Đại Dương";
        tvNoiDung.setText(noiDung);
        tvTien.setText(tien + "");


        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String IDDonHang = "DH_" + UUID.randomUUID().toString();
                String ghiChu = edtGhiChu.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                mDatabase.child("KhachHang").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        KhachHang khachHang = snapshot.getValue(KhachHang.class);
                        DonHang donHang = new DonHang(IDDonHang,auth.getUid(),null,khachHang.getDiaChi(),khachHang.getSoDienThoai(),ghiChu,null,tien,currentTime,TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien);
                        mDatabase.child("DonHang").child(auth.getUid()).child(IDDonHang).setValue(donHang);
                        SparseBooleanArray sparse = gioHangAdapter.getBooleanArray();
                        for (int i = 0; i < sparse.size(); i++) {
                            if (sparse.valueAt(i)) {
                                DonHangInfo donHangInfo = donHangInfos.get(sparse.keyAt(i));
                                donHangInfo.setIDDonHang(IDDonHang);
                                mDatabase.child("DonHangInfo").child(auth.getUid()).child(donHangInfo.getIDInfo()).setValue(donHangInfo);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        dialog.show();
    }

    private void setControl() {
        lnGhiChu = findViewById(R.id.lnThemGhiChu);
        edtGhiChu = findViewById(R.id.edtGhiChu);
        rcvGioHang = findViewById(R.id.rcvGioHang);
        tvTongPhu = findViewById(R.id.tvTongPhu);
        tvChiPhiVanChuyen = findViewById(R.id.tvChiPhiVanChuyen);
        tvTongTien = findViewById(R.id.tvTongTien);
        pbLoadGioHang = findViewById(R.id.pbLoadGioHang);
        btnThanhToan = findViewById(R.id.btnThanhToan);

    }
}