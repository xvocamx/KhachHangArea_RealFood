package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.khachhangarea_realfood.adapter.ViewPaperDonMuaAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DonMua extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    ViewPaperDonMuaAdapter viewPaperDonMuaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_mua);
        setControl();
        setEvent();
    }

    private void setEvent() {
        LoadTab();
    }

    private void LoadTab() {
        viewPaperDonMuaAdapter = new ViewPaperDonMuaAdapter(this);
        viewPager.setAdapter(viewPaperDonMuaAdapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Chờ xác nhận");
                        break;
                    case 1:
                        tab.setText("Chuẩn bị hàng");
                        break;
                    case 2:
                        tab.setText("Đang giao");
                        break;
                    case 3:
                        tab.setText("Đã nhận hàng");
                        break;
                    case 4:
                        tab.setText("Đã hủy");
                        break;
                    case 5:
                        tab.setText("Đánh giá");
                        break;
                }
            }
        }).attach();
    }

    private void setControl() {
        tabLayout = findViewById(R.id.tab_donmua);
        viewPager = findViewById(R.id.view_paper_donmua);
    }
}