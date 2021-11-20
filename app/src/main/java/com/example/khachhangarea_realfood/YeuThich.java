package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.khachhangarea_realfood.adapter.ViewPaperYeuThichAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class YeuThich extends AppCompatActivity {
    private TabLayout tableLayout;
    private ViewPager2 viewPager;
    private ViewPaperYeuThichAdapter viewPaperYeuThichAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeu_thich);
        setControl();
        setEvent();
        LoadTab();
    }

    private void setEvent() {

    }

    private void LoadTab() {
        viewPaperYeuThichAdapter = new ViewPaperYeuThichAdapter(YeuThich.this);
        viewPager.setAdapter(viewPaperYeuThichAdapter);
        new TabLayoutMediator(tableLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Food");
                    case 1:
                        tab.setText("Shop");
                }
            }
        }).attach();
    }

    private void setControl() {
        tableLayout = findViewById(R.id.tab_favorite);
        viewPager = findViewById(R.id.view_paper_yeuthich);
    }
}