package com.example.khachhangarea_realfood;

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
import android.widget.Toast;

import com.example.khachhangarea_realfood.TrangThai.TrangThaiThongBao;
import com.example.khachhangarea_realfood.model.BaoCao;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.ThongBao;
import com.google.gson.Gson;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.Date;
import java.util.UUID;

public class BaoCaoShop extends AppCompatActivity {
    Spinner spLyDo;
    EditText edtLyDo;
    Button btnBaoCao;
    CuaHang cuaHang;
    ImageView ivBaoCao;
    Uri hinhAnhBaoCao;
    Firebase_Manager firebase_manager = new Firebase_Manager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao_shop);
        setControl();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String dataCuaHang = intent.getStringExtra("dataShop");
            Gson gson = new Gson();
            cuaHang = gson.fromJson(dataCuaHang, CuaHang.class);
        }
        setEvent();
        Toast.makeText(this, cuaHang.getIDCuaHang(), Toast.LENGTH_SHORT).show();
    }

    private void setEvent() {
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
                }).show(BaoCaoShop.this);
            }
        });


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
        btnBaoCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spLyDo.getSelectedItem().equals("Chọn lý do")) {
                    Toast.makeText(BaoCaoShop.this, "Vui lòng chọn lý do", Toast.LENGTH_SHORT).show();
                } else {
                    String IDBaoCao = "BC_" + UUID.randomUUID().toString();
                    String IDThongBao = UUID.randomUUID().toString();
                    if (spLyDo.getSelectedItem().equals("Khác")) {

                        String noiDung = "Người dùng " + firebase_manager.auth.getUid() + " gửi lý do báo cáo về cửa hàng " + cuaHang.getTenCuaHang() + " vì " + edtLyDo.getText().toString();
                        ThongBao thongBao = new ThongBao(IDThongBao,noiDung,"Thông báo","","admin","", TrangThaiThongBao.ChuaXem,new Date());
                        BaoCao baoCao = new BaoCao(IDBaoCao, firebase_manager.auth.getUid(), cuaHang.getIDCuaHang(), edtLyDo.getText().toString(), null,new Date());
                        firebase_manager.mDatabase.child("BaoCao").child(IDBaoCao).setValue(baoCao);
                        firebase_manager.mDatabase.child("ThongBao").child(cuaHang.getIDCuaHang()).child(IDThongBao).setValue(thongBao);
                        if(hinhAnhBaoCao !=null){
                            firebase_manager.UpImageBaoCao(hinhAnhBaoCao, cuaHang.getIDCuaHang());
                        }
                    } else {
                        String noiDung = "Người dùng " + firebase_manager.auth.getUid() + " gửi lý do báo cáo về cửa hàng " + cuaHang.getTenCuaHang() + " vì " + spLyDo.getSelectedItem().toString();
                        ThongBao thongBao = new ThongBao(IDThongBao,noiDung,"Thông báo","","admin","", TrangThaiThongBao.ChuaXem,new Date());
                        BaoCao baoCao = new BaoCao(IDBaoCao, firebase_manager.auth.getUid(), cuaHang.getIDCuaHang(), spLyDo.getSelectedItem().toString(), null,new Date());
                        firebase_manager.mDatabase.child("BaoCao").child(IDBaoCao).setValue(baoCao);
                        firebase_manager.mDatabase.child("ThongBao").child(cuaHang.getIDCuaHang()).child(IDThongBao).setValue(thongBao);
                        if(hinhAnhBaoCao != null){
                            firebase_manager.UpImageBaoCao(hinhAnhBaoCao, cuaHang.getIDCuaHang());
                        }
                    }
                    Intent intent = new Intent(BaoCaoShop.this, ChiTietCuaHang.class);
                    Gson gson = new Gson();
                    String data = gson.toJson(cuaHang);
                    intent.putExtra("dataCuaHang", data);
                    startActivity(intent);
                }
            }
        });

    }

    private void setControl() {
        spLyDo = findViewById(R.id.spLyDo);
        edtLyDo = findViewById(R.id.edtLyDo);
        btnBaoCao = findViewById(R.id.btnBaoCao);
        ivBaoCao = findViewById(R.id.ivBaoCao);
    }
}