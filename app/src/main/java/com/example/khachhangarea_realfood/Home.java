package com.example.khachhangarea_realfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.khachhangarea_realfood.fragment.FavoriteFragment;
import com.example.khachhangarea_realfood.fragment.HomeFragment;
import com.example.khachhangarea_realfood.fragment.NotificationFragment;
import com.example.khachhangarea_realfood.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;


public class Home extends AppCompatActivity {
    FrameLayout frameLayout;
    BottomBar bottomBar;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.getSupportActionBar().hide();
        setControl();
        setEvent();
        HomeFragment homeFragment = new HomeFragment();
        loadFragment(homeFragment);
        firebase_manager.mDatabase.child("ThongBao").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BottomBarTab barTab = bottomBar.getTabWithId(R.id.tab_notification);
                barTab.setBadgeCount((int) snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                switch (tabId) {
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