package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DangNhap extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnDangNhap;
    private TextView tvQuenMatKhau, tvDangKy;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    KAlertDialog kAlertDialog;
    Firebase_Manager firebase_manager = new Firebase_Manager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        this.getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        setControl();
        setEvent();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            edtEmail.setText(mAuth.getCurrentUser().getEmail());
            startActivity(intent);
        }
    }

    private void setEvent() {
        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivity(intent);
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDangNhap();
            }
        });
        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhap.this, QuenMatKhau.class);
                startActivity(intent);
            }
        });
    }

    private void onClickDangNhap() {

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (email.isEmpty()) {
            edtEmail.setError("Vui l??ng nh???p email");
        } else if (!email.matches(emailPattern)) {
            edtEmail.setError("Nh???p sai ?????nh d???ng email");
        } else if (password.isEmpty()) {
            edtPassword.setError("Vui l??ng nh???p password");
        } else if (password.length() < 6) {
            edtPassword.setError("????? d??i password t??? 6 ?????n 100");
        } else {
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Vui l??ng ?????i trong khi ????ng nh???p ...");
//            progressDialog.setTitle("????ng nh???p");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
            kAlertDialog = new KAlertDialog(DangNhap.this, KAlertDialog.PROGRESS_TYPE)
                    .setTitleText("????ng nh???p")
                    .setContentText("Vui l??ng ?????i trong khi ????ng nh???p ...");
            kAlertDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebase_manager.mDatabase.child("KhachHang").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    kAlertDialog.dismiss();
                                    Intent intent = new Intent(DangNhap.this, Home.class);
                                    startActivity(intent);
                                } else {
                                    kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                                    kAlertDialog.setTitleText("????ng nh???p");
                                    kAlertDialog.setContentText("T??i kho???n kh??ng kh??? d???ng");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        // Sign in success, update UI with the signed-in user's information

                    } else {
                        // If sign in fails, display a message to the user.
                        kAlertDialog.changeAlertType(KAlertDialog.ERROR_TYPE);
                        kAlertDialog.setTitleText("????ng nh???p");
                        kAlertDialog.setContentText("T??i kho???n ho???c m???t kh???u sai");
                    }
                }
            });
        }


    }

    private void setControl() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        tvDangKy = findViewById(R.id.tvDangKy);
        tvQuenMatKhau = findViewById(R.id.tvQuenMatKhau);
    }
}