package com.example.khachhangarea_realfood.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.khachhangarea_realfood.fragment.DanhGiaFragment;
import com.example.khachhangarea_realfood.fragment.TatCaSanPhamFragment;

public class ViewPaperAdapter extends FragmentStateAdapter {
    private String idCuaHang;

    public ViewPaperAdapter(@NonNull FragmentActivity fragmentActivity, String idCuaHang) {
        super(fragmentActivity);
        this.idCuaHang = idCuaHang;
    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new TatCaSanPhamFragment(idCuaHang);
            case 1:
                return new DanhGiaFragment();
            default:
                return new TatCaSanPhamFragment(idCuaHang);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
