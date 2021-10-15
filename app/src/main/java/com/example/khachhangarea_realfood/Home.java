package com.example.khachhangarea_realfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.khachhangarea_realfood.fragment.FavoriteFragment;
import com.example.khachhangarea_realfood.fragment.HomeFragment;
import com.example.khachhangarea_realfood.fragment.NotificationFragment;
import com.example.khachhangarea_realfood.fragment.SettingFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

public class Home extends AppCompatActivity {
    FrameLayout frameLayout;
    BottomBar bottomBar;
    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.getSupportActionBar().hide();
        setControl();
        setEvent();
        BottomBarTab barTab = bottomBar.getTabWithId(R.id.tab_notification);
        barTab.setBadgeCount(5);
    }

    //Load Fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setEvent() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId)
                {
                    case R.id.tab_home:
                        HomeFragment homeFragment = new HomeFragment();
                        loadFragment(homeFragment);
                        break;
                    case R.id.tab_favorite:
                        FavoriteFragment favoriteFragment = new FavoriteFragment();
                        loadFragment(favoriteFragment);
                        break;
                    case R.id.tab_notification:
                        NotificationFragment notificationFragment = new NotificationFragment();
                        loadFragment(notificationFragment);
                        break;
                    case R.id.tab_setting:
                        SettingFragment settingFragment = new SettingFragment();
                        loadFragment(settingFragment);
                        break;
                }
            }
        });
    }

    private void setControl() {
        bottomBar = findViewById(R.id.bottomBar);
        frameLayout = findViewById(R.id.fragment);
    }
}