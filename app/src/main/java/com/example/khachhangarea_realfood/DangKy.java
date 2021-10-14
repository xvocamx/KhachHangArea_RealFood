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

public class DangKy extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPhone, edtAddress, edtPassword, edtConfirmPassword;
    private TextView tvDangNhap;
    private Button btnDangKy;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        mAuth = FirebaseAuth.getInstance();
        setControl();
        setEvent();
    }

    private void setEvent() {
        tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKy.this, DangNhap.class);
                startActivity(intent);
            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDangKy();
            }
        });
    }

    private void onClickDangKy() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        progressDialog = new ProgressDialog(this);

        if (name.isEmpty()) {
            edtName.setError("Vui lòng nhập họ tên");
        } else if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
        } else if (!email.matches(emailPattern)) {
            edtEmail.setError("Nhập sai định dạng email");
        } else if (phone.isEmpty()) {
            edtPhone.setError("Vui lòng nhập số điện thoại");
        } else if (address.isEmpty()) {
            edtAddress.setError("Vui lòng nhập địa chỉ");
        } else if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập password");
        } else if (password.length() < 6) {
            edtPassword.setError("Độ dài password từ 6 đến 100");
        } else if (confirmPassword.isEmpty()) {
            edtConfirmPassword.setError("Vui lòng nhập password");
        } else if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Mật khẩu không trùng nhau");
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Vui lòng đợi trong khi đăng ký ...");
            progressDialog.setTitle("Đăng ký");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                progressDialog.dismiss();
                                Toast.makeText(DangKy.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DangKy.this, Home.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                progressDialog.dismiss();
                                Toast.makeText(DangKy.this, "Đăng ký không thành công", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void setControl() {
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        tvDangNhap = findViewById(R.id.tvDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);
    }
}