package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
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
import com.example.khachhangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.khachhangarea_realfood.adapter.GioHangAdapter;
import com.example.khachhangarea_realfood.adapter.GioHangProAdapter;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.GioHangDisplay;
import com.example.khachhangarea_realfood.model.KhachHang;
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
    GioHangProAdapter gioHangProAdapter;
    ArrayList<GioHangDisplay> gioHangDisplays = new ArrayList<>();
    ArrayList<DonHangInfo> tempGioHangs = new ArrayList<>();
    ArrayList<String> idCuaHang = new ArrayList<>();
    int tong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        donHangInfos = new ArrayList<>();
        donHangInfo = new DonHangInfo();
        gioHangProAdapter = new GioHangProAdapter(this, R.layout.list_item_giohang, gioHangDisplays);
        gioHangAdapter = new GioHangAdapter(this, R.layout.list_item_giohang_sanpham, donHangInfos);
        setControl();
        TongTien();
        setEvent();

    }

    @Override
    protected void onResume() {
        TongTien();
        super.onResume();
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
                            if (idCuaHang.size() == 0) {
                                idCuaHang.add(id);
                            }
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
                    gioHangProAdapter.notifyDataSetChanged();

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
        rcvGioHang.setLayoutManager(linearLayoutManager);
        rcvGioHang.setAdapter(gioHangProAdapter);
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

        gioHangProAdapter.setCheckBoxListener(new GioHangAdapter.CheckBoxListener() {
            @Override
            public void getGiaGioHang() {
                TongTien();
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelectedItem()) {
                    String string = new Gson().toJson(gioHangDisplays);
                    Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("data", string);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                        kAlertDialog = new KAlertDialog(GioHang.this,KAlertDialog.WARNING_TYPE).setTitleText("Thông báo")
                                .setContentText("Vui lòng chọn sản phẩm!!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        kAlertDialog.dismiss();
                                    }
                                });
                        kAlertDialog.show();
                }
            }
        });

    }

    private void TongTien() {
        tong = 0;
        if (gioHangDisplays.size() == 0) {
            gioHangDisplays.clear();
            gioHangProAdapter.notifyDataSetChanged();
        }
        for (GioHangDisplay gioHangDisplay : gioHangDisplays
        ) {

            for (DonHangInfo donHangInfo : gioHangDisplay.getSanPhams()
            ) {
                if (donHangInfo.isSelected()) {
                    tong += Integer.parseInt(donHangInfo.getSanPham().getGia()) * Integer.parseInt(donHangInfo.getSoLuong());
                }
            }
        }
        tvTongTien.setText(tong + "");
    }

    private boolean checkSelectedItem() {
        for (GioHangDisplay gioHangDisplay : gioHangDisplays) {
            for (DonHangInfo donHangInfo : gioHangDisplay.getSanPhams()) {
                if (donHangInfo.isSelected()) {
                    return true;
                }
            }
        }
        return false;
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