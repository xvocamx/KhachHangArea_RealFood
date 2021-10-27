package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuaThongTin extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPhone, edtDiaChi;
    private Button btnLuu;
    private CircleImageView civAvatar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private FirebaseUser user;
    private Uri avatarKhachHang;
    private KAlertDialog kAlertDialog;
    private ProgressBar pbLoadThongTinCaNhan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_thong_tin);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        user = auth.getCurrentUser();
        setControl();
        setEvent();
        LoadInfoKhachHang();
    }

    private void LoadInfoKhachHang() {
        edtEmail.setText(user.getEmail());
        mDatabase.child("KhachHang").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                edtName.setText(khachHang.getTenKhachHang());
                edtPhone.setText(khachHang.getSoDienThoai());
                edtDiaChi.setText(khachHang.getDiaChi());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        storageRef.child("KhachHang").child(user.getUid()).child("AvatarKhachHang").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri.toString()).into(civAvatar);
            }
        });
        pbLoadThongTinCaNhan.setVisibility(View.GONE);
    }

    private void setEvent() {
        civAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        civAvatar.setImageBitmap(r.getBitmap());
                        avatarKhachHang = r.getUri();
                    }
                }).setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {

                    }
                }).show(SuaThongTin.this);
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (avatarKhachHang != null) {
                    storageRef.child("KhachHang").child(user.getUid()).child("AvatarKhachHang").putFile(avatarKhachHang);
                }
                kAlertDialog = new KAlertDialog(SuaThongTin.this, KAlertDialog.PROGRESS_TYPE).setContentText("Loading");
                kAlertDialog.show();
                KhachHang khachHang = new KhachHang(user.getUid(), edtName.getText().toString(), edtDiaChi.getText().toString(), edtPhone.getText().toString(),
                        "", edtEmail.getText().toString(), "", null);
                mDatabase.child("KhachHang").child(user.getUid()).setValue(khachHang).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        kAlertDialog.changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        kAlertDialog.setContentText("Sửa thàng công");
                        Intent intent = new Intent(SuaThongTin.this,Home.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                        kAlertDialog.setContentText("Sửa thất bại");
                    }
                });
            }
        });

    }

    private void setControl() {
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        civAvatar = findViewById(R.id.civAvatar);
        btnLuu = findViewById(R.id.btnSave);
        pbLoadThongTinCaNhan = findViewById(R.id.pbLoadThongTinCaNhan);
    }
}