package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class QuenMatKhau extends AppCompatActivity {
    private Button btnGui;
    private EditText edtEmail;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quenmatkhau);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGui();
            }
        });
    }

    private void onClickGui() {
        String email = edtEmail.getText().toString().trim();
        if(email.isEmpty()){
            edtEmail.setError("Vui lòng nhập email");
        }else if(!email.matches(emailPattern)){
            edtEmail.setError("Nhập sai định dạng email");
        }else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Vui lòng đợi trong khi gửi email ...");
            progressDialog.setTitle("Gửi email");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(QuenMatKhau.this, "Đã gửi email.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(QuenMatKhau.this,DangNhap.class);
                                startActivity(intent);
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(QuenMatKhau.this, "Email không tồn tại.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void setControl() {
        btnGui = findViewById(R.id.btnGui);
        edtEmail = findViewById(R.id.edtEmail);
    }
}