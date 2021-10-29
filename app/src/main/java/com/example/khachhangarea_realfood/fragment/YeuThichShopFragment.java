package com.example.khachhangarea_realfood.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khachhangarea_realfood.R;


public class YeuThichShopFragment extends Fragment {
    private View view;
    
    public YeuThichShopFragment() {
        // Required empty public constructor
    }

    
    public static YeuThichShopFragment newInstance(String param1, String param2) {
        YeuThichShopFragment fragment = new YeuThichShopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yeu_thich_shop, container, false);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
    }

    private void setControl() {
        
    }
}