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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangNhap extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private TextView tvDangKy;
    private Button btnDangNhap;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);
        mAuth = FirebaseAuth.getInstance();
        setControl();
        setEvent();
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
    }

    private void onClickDangNhap() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        progressDialog = new ProgressDialog(this);

        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
        } else if (!email.matches(emailPattern)) {
            edtEmail.setError("Nhập sai định dạng email");
        } else if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập password");
        } else if (password.length() < 6) {
            edtPassword.setError("Độ dài password từ 6 đến 100");
        } else {
            progressDialog.setMessage("Vui lòng đợi trong khi đăng nhập ...");
            progressDialog.setTitle("Đăng nhập");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(DangNhap.this, Home.class);
                                startActivity(intent);
                                Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(DangNhap.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }


    }

    private void setControl() {
        tvDangKy = findViewById(R.id.tvDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
    }
}