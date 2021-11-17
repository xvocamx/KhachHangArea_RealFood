package com.example.khachhangarea_realfood.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khachhangarea_realfood.ChiTietDonHang;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.adapter.DonMuaAdpater;
import com.example.khachhangarea_realfood.adapter.DonMuaChuanBiHangAdpater;
import com.example.khachhangarea_realfood.adapter.ThanhToanAdapter;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.model.DonHangInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ChuanBiHangFragment extends Fragment {
    View view;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    RecyclerView rcvChuanBiHang;
    ArrayList<DonHang> donHangs;
    LinearLayoutManager linearLayoutManager;
    DonMuaChuanBiHangAdpater donMuaChuanBiHangAdpater;

    public ChuanBiHangFragment() {
    }

    public static ChuanBiHangFragment newInstance(String param1, String param2) {
        ChuanBiHangFragment fragment = new ChuanBiHangFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chuan_bi_hang, container, false);
        donHangs = new ArrayList<>();
        donMuaChuanBiHangAdpater = new DonMuaChuanBiHangAdpater(getActivity(),R.layout.list_item_donhang_chuanbihang,donHangs);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvChuanBiHang.setLayoutManager(linearLayoutManager);
        rcvChuanBiHang.setAdapter(donMuaChuanBiHangAdpater);
        LoadItemChuanBiHang();
    }

    private void LoadItemChuanBiHang() {
        firebase_manager.LoadChuanBiDonHang(donHangs,donMuaChuanBiHangAdpater);
        //xem chi tiet don hang
        donMuaChuanBiHangAdpater.setDelegation(new DonMuaChuanBiHangAdpater.ClickItemDonMuaListener() {
            @Override
            public void getInfomationDonMua(DonHang donHang) {
                Intent intent = new Intent(getActivity(), ChiTietDonHang.class);
                Gson gson = new Gson();
                String data = gson.toJson(donHang);
                intent.putExtra("dataDonHang",data);
                getActivity().startActivity(intent);
            }
        });
    }

    private void setControl() {
        rcvChuanBiHang = view.findViewById(R.id.rcvChuanBiHang);
    }
}