package com.example.khachhangarea_realfood.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiDonHang;
import com.example.khachhangarea_realfood.adapter.DonMuaAdpater;
import com.example.khachhangarea_realfood.model.DonHang;
import com.example.khachhangarea_realfood.ChiTietDonHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ChoXacNhanFragment extends Fragment {
    View view;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    RecyclerView rcvChoXacNhan;
    DonMuaAdpater donMuaAdpater;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DonHang> donHangs;

    public ChoXacNhanFragment() {

    }

    public static ChoXacNhanFragment newInstance(String param1, String param2) {
        ChoXacNhanFragment fragment = new ChoXacNhanFragment();
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
        view = inflater.inflate(R.layout.fragment_cho_xac_nhan, container, false);
        donHangs = new ArrayList<>();
        donMuaAdpater = new DonMuaAdpater(getActivity(), R.layout.list_item_donhang, donHangs);
        setControl();
        setEvent();
        return view;
    }

    private void LoadDonHangChoXacNhan() {
        firebase_manager.LoadDonHangChoXacNhan(donHangs,donMuaAdpater);
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvChoXacNhan.setLayoutManager(linearLayoutManager);
        rcvChoXacNhan.setAdapter(donMuaAdpater);
        LoadDonHangChoXacNhan();

        //xem chi tiet don hang
        donMuaAdpater.setDelegation(new DonMuaAdpater.ClickItemDonMuaListener() {
            @Override
            public void getInfomationDonMua(DonHang donHang) {
                Intent intent = new Intent(getActivity(),ChiTietDonHang.class);
                Gson gson = new Gson();
                String data = gson.toJson(donHang);
                intent.putExtra("dataDonHang",data);
                getActivity().startActivity(intent);
            }
        });
    }

    private void setControl() {
        rcvChoXacNhan = view.findViewById(R.id.rcvChoXacNhan);
    }
}