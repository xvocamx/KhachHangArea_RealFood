package com.example.khachhangarea_realfood;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

public class BaoCaoShop extends AppCompatActivity {
    Spinner spLyDo;
    EditText edtLyDo;
    Button btnBaoCao;
    ImageView ivBaoCao;
    Uri hinhAnhBaoCao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao_shop);
        setControl();
        setEvent();
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
        String lydo = edtLyDo.getText().toString().trim();
        String lyDo1 = "";
        spLyDo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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