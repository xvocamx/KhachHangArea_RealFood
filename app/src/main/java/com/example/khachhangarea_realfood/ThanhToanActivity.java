package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import com.example.khachhangarea_realfood.adapter.ThanhToanAdapter;
import com.example.khachhangarea_realfood.adapter.ThanhToanProAdapter;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.example.khachhangarea_realfood.model.GioHangDisplay;
import com.example.khachhangarea_realfood.model.KhachHang;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ThanhToanActivity extends AppCompatActivity {

    TextView txtTenNguoiNhan,tvDiaChi;
    private ArrayList<DonHangInfo> donHangInfos;
    private RecyclerView rcvThanhToan;
    private LinearLayoutManager linearLayoutManager;
    private DonHangInfo donHangInfo;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextView  tvTongTien;
    private ProgressBar pbLoadGioHang;
    private Button btnThanhToan;
    String diaChi,soDienThoai;
    ThanhToanProAdapter thanhToanProAdapter;
    ArrayList<GioHangDisplay> gioHangDisplays = new ArrayList<>();
    ArrayList<String> idCuaHang = new ArrayList<>();
    int tong = 0;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        donHangInfos = new ArrayList<>();
        donHangInfo = new DonHangInfo();
        if (getIntent()!=null)
        {
            Bundle bundle = getIntent().getExtras();
            String data = bundle.getString("data");
            gioHangDisplays = new Gson().fromJson(data, new TypeToken<ArrayList<GioHangDisplay>>(){}.getType());
            for (GioHangDisplay gioHangDisplay:gioHangDisplays
                 ) {
                gioHangDisplay.setSanPhams((ArrayList<DonHangInfo>) gioHangDisplay.getSanPhams().stream().filter(donHangInfo1 -> donHangInfo1.isSelected()==true).collect(Collectors.toList()));
            }
            gioHangDisplays.removeIf(gioHangDisplay -> gioHangDisplay.getSanPhams().size()==0);
            thanhToanProAdapter = new ThanhToanProAdapter(this, R.layout.list_item_thanhtoan, gioHangDisplays);

        }


        setControl();
        setEvent();
        LoadData();

    }

    private void LoadData() {
        firebase_manager.mDatabase.child("KhachHang").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                txtTenNguoiNhan.setText( khachHang.getTenKhachHang());
                tvDiaChi.setText( khachHang.getDiaChi());
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        for (GioHangDisplay gioHangDisplay:gioHangDisplays) {
            for (DonHangInfo donHangInfo:gioHangDisplay.getSanPhams()
            ) {
                tong+=Integer.parseInt(donHangInfo.getDonGia())*Integer.parseInt(donHangInfo.getSoLuong());
            }
        }

        tvTongTien.setText(tong+"");
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
                    Toast.makeText(ThanhToanActivity.this, "Size" + gioHangDisplay.getSanPhams().size(), Toast.LENGTH_SHORT).show();
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
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KAlertDialog kAlertDialog = new KAlertDialog(ThanhToanActivity.this,KAlertDialog.PROGRESS_TYPE);
                kAlertDialog.show();
                for (GioHangDisplay gioHangDisplay:gioHangDisplays
                     ) {
                    String IDDonHang = "DH_" + UUID.randomUUID().toString();
                    DonHang donHang = new DonHang(IDDonHang, gioHangDisplay.getIdCuaHang(), firebase_manager.auth.getUid()
                            , "",  diaChi,  soDienThoai, "", "", tong, new Date(), TrangThaiDonHang.SHOP_ChoXacNhanChuyenTien
                    );
                    firebase_manager.mDatabase.child("DonHang").child(IDDonHang).setValue(donHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            for (DonHangInfo donHangInfo: gioHangDisplay.getSanPhams()) {

                                donHangInfo.setIDDonHang(IDDonHang);
                                firebase_manager.mDatabase.child("DonHangInfo").child(donHangInfo.getIDInfo()).setValue(donHangInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {


                                        kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        kAlertDialog.setContentText("Đặt hàng thành công!");


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                        kAlertDialog.setContentText(e.getMessage());
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                            kAlertDialog.setContentText(e.getMessage());
                        }
                    });
                }
            }
        });

    }



    private void setControl() {
        rcvThanhToan = findViewById(R.id.rcvThanhToan);
        tvTongTien = findViewById(R.id.tvTongTien);
        pbLoadGioHang = findViewById(R.id.pbLoadGioHang);
        tvDiaChi = findViewById(R.id.txtDiaChiNhanHang);
        txtTenNguoiNhan = findViewById(R.id.txtTenNguoiNhan);
        btnThanhToan = findViewById(R.id.btnThanhToan);
    }
}