package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.TrangThai.LoaiThongBao;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiBaoCao;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiThongBao;
import com.example.khachhangarea_realfood.model.BaoCaoShop;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.example.khachhangarea_realfood.model.ThongBao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.Date;
import java.util.UUID;

public class BaoCaoShopActivity extends AppCompatActivity {
    Spinner spLyDo;
    EditText edtLyDo;
    Button btnBaoCao;
    CuaHang cuaHang;
    ImageView ivBaoCao;
    Uri hinhAnhBaoCao;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    KAlertDialog kAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao_shop);
        setControl();
        // Lay du lieu cua hang
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataCuaHang = intent.getStringExtra("dataShop");
            Gson gson = new Gson();
            cuaHang = gson.fromJson(dataCuaHang, CuaHang.class);
        }
        setEvent();
    }

    // Set su kien
    private void setEvent() {
        //Chon hinh anh de bao cao
        ivBaoCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        ivBaoCao.setImageBitmap(r.getBitmap());
                        hinhAnhBaoCao = r.getUri();
                    }
                }).setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {

                    }
                }).show(BaoCaoShopActivity.this);
            }
        });
        //Chon ly do bao cao
        spLyDo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spLyDo.getSelectedItem().equals("Khác")) {
                    edtLyDo.setVisibility(View.VISIBLE);
                } else {
                    edtLyDo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Xu ly button bao cao
        btnBaoCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spLyDo.getSelectedItem().equals("Chọn lý do")) {
                    kAlertDialog = new KAlertDialog(BaoCaoShopActivity.this, KAlertDialog.SUCCESS_TYPE).setContentText("Vui lòng chọn lý do");
                    kAlertDialog.show();
                } else {
                    String IDBaoCao = "BC_" + UUID.randomUUID().toString();
                    String IDThongBao = UUID.randomUUID().toString();
                    firebase_manager.mDatabase.child("KhachHang").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            KhachHang khachHang = snapshot.getValue(KhachHang.class);
                            String tenKhachHang = khachHang.getTenKhachHang();
                            firebase_manager.storageRef.child("KhachHang").child(firebase_manager.auth.getUid()).child("AvatarKhachHang").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    try {
                                        // Dung Edit text Ly do
                                        if (spLyDo.getSelectedItem().equals("Khác")) {
                                            //Gui thong bao va bao cao cho admin
                                            String noiDung = "Người dùng " + tenKhachHang + " gửi lý do báo cáo về cửa hàng " + cuaHang.getTenCuaHang() + " vì " + edtLyDo.getText().toString();
                                            ThongBao thongBao = new ThongBao(IDThongBao, noiDung, "Báo cáo", "", "admin", uri.toString(), TrangThaiThongBao.ChuaXem, new Date());
                                            BaoCaoShop baoCaoShop = new BaoCaoShop(IDBaoCao, firebase_manager.auth.getUid(), cuaHang.getIDCuaHang(), noiDung, "Báo cáo", null, new Date(), TrangThaiBaoCao.ChuaXem);
                                            thongBao.setLoaiThongBao(LoaiThongBao.BaoCaoCuaHang);
                                            thongBao.setBaoCaoShop(baoCaoShop);
                                            //Up len firebase
                                            firebase_manager.mDatabase.child("BaoCao").child(IDBaoCao).setValue(baoCaoShop);
                                            firebase_manager.mDatabase.child("ThongBao").child("admin").child(IDThongBao).setValue(thongBao);
                                            //kiem tra hinh anh bao cao
                                            if (hinhAnhBaoCao != null) {
                                                firebase_manager.UpImageBaoCao(hinhAnhBaoCao, IDBaoCao);
                                            }
                                        }
                                        //Dung Spinner ly do
                                        else {
                                            //Gui thong bao va bao cao cho admin
                                            String noiDung = "Người dùng " + tenKhachHang + " gửi lý do báo cáo về cửa hàng " + cuaHang.getTenCuaHang() + " vì " + spLyDo.getSelectedItem().toString();
                                            ThongBao thongBao = new ThongBao(IDThongBao, noiDung, "Báo cáo", "", "admin", uri.toString(), TrangThaiThongBao.ChuaXem, new Date());
                                            BaoCaoShop baoCaoShop = new BaoCaoShop(IDBaoCao, firebase_manager.auth.getUid(), cuaHang.getIDCuaHang(), noiDung, "Báo cáo", null, new Date(), TrangThaiBaoCao.ChuaXem);
                                            thongBao.setLoaiThongBao(LoaiThongBao.BaoCaoCuaHang);
                                            thongBao.setBaoCaoShop(baoCaoShop);
                                            //Up len firebase
                                            firebase_manager.mDatabase.child("BaoCao").child(IDBaoCao).setValue(baoCaoShop);
                                            firebase_manager.mDatabase.child("ThongBao").child("admin").child(IDThongBao).setValue(thongBao);
                                            //Kiem tra hinh anh bao cao
                                            if (hinhAnhBaoCao != null) {
                                                firebase_manager.UpImageBaoCao(hinhAnhBaoCao, IDBaoCao);
                                            }
                                        }
                                    } catch (Exception ex) {

                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Intent intent = new Intent(BaoCaoShopActivity.this, ChiTietCuaHang.class);
                    Gson gson = new Gson();
                    String data = gson.toJson(cuaHang);
                    intent.putExtra("dataCuaHang", data);
                    startActivity(intent);
                }
            }
        });

    }

    //Anh xa
    private void setControl() {
        spLyDo = findViewById(R.id.spLyDo);
        edtLyDo = findViewById(R.id.edtLyDo);
        btnBaoCao = findViewById(R.id.btnBaoCao);
        ivBaoCao = findViewById(R.id.ivBaoCao);
    }
}