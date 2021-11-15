package com.example.khachhangarea_realfood.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.khachhangarea_realfood.fragment.ChoXacNhanFragment;
import com.example.khachhangarea_realfood.fragment.ChuanBiHangFragment;
import com.example.khachhangarea_realfood.fragment.DaHuyFragment;
import com.example.khachhangarea_realfood.fragment.DaNhanHangFragment;
import com.example.khachhangarea_realfood.fragment.DangGiaoFragment;
import com.example.khachhangarea_realfood.fragment.DanhGiaDonMuaFragment;
import com.example.khachhangarea_realfood.fragment.TraHangFragment;

public class ViewPaperDonMuaAdapter extends FragmentStateAdapter {

    public ViewPaperDonMuaAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChoXacNhanFragment();
            case 1:
                return new ChuanBiHangFragment();
            case 2:
                return new DangGiaoFragment();
            case 3:
                return new DaNhanHangFragment();
            case 4:
                return new DaHuyFragment();
            case 5:
                return new DanhGiaDonMuaFragment();
            default:
                return new ChoXacNhanFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
