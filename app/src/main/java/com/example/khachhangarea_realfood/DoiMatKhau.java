package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DoiMatKhau extends AppCompatActivity {
    private EditText edtPassword, edtNewPassword, edtConfirmNewPassword;
    private Button btnLuu;
    private FirebaseUser mUser;
    private ProgressDialog progressDialog;
    private AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doimatkhau);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChangePassword();
            }
        });
    }

    private void setControl() {
        edtPassword = findViewById(R.id.edtPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword);
        btnLuu = findViewById(R.id.btnLuu);
    }

    private void onClickChangePassword() {
        progressDialog = new ProgressDialog(this);
        String password = edtPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmNewPassword = edtConfirmNewPassword.getText().toString().trim();
        final String email = mUser.getEmail().toString().trim();

        if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
        } else if (newPassword.isEmpty()) {
            edtNewPassword.setError("Vui lòng nhập mật khẩu mới");
        } else if (newPassword.length() < 6) {
            edtNewPassword.setError("Độ dài mật khẩu từ 6 đến 100");
        } else if (confirmNewPassword.isEmpty()) {
            edtConfirmNewPassword.setError("Vui lòng nhập lại mật khẩu mới ");
        } else if (!newPassword.equals(confirmNewPassword)) {
            edtConfirmNewPassword.setError("Mật khẩu không trùng nhau");
        } else {
            credential = EmailAuthProvider.getCredential(email, password);
            mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (newPassword.isEmpty()) {
                            edtNewPassword.setError("Vui lòng nhập mật khẩu mới");
                        } else if (newPassword.length() < 6) {
                            edtNewPassword.setError("Độ dài mật khẩu từ 6 đến 100");
                        } else if (confirmNewPassword.isEmpty()) {
                            edtConfirmNewPassword.setError("Vui lòng nhập lại mật khẩu mới ");
                        } else if (!newPassword.equals(confirmNewPassword)) {
                            edtConfirmNewPassword.setError("Mật khẩu không trùn nhau");
                        } else {
                            progressDialog.setMessage("Vui lòng đợi trong khi đổi mật khẩu ...");
                            progressDialog.setTitle("Đổi mật khẩu");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            mUser.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(DoiMatKhau.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(DoiMatKhau.this, Home.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(DoiMatKhau.this, "Mật khẩu cũ sai", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}