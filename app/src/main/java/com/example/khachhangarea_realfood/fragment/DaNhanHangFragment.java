package com.example.khachhangarea_realfood.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.khachhangarea_realfood.ChiTietDonHang;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.adapter.DonMuaAdpater;
import com.example.khachhangarea_realfood.adapter.DonMuaDaNhanHangAdpater;
import com.example.khachhangarea_realfood.model.DonHang;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DaNhanHangFragment extends Fragment {
    View view;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    LinearLayoutManager linearLayoutManager;
    RecyclerView rcvDaNhanHang;
    ArrayList<DonHang> donHangs;
    DonMuaDaNhanHangAdpater donMuaDaNhanHangAdpater;
    SearchView searchView;

    public DaNhanHangFragment() {
    }

    public static DaNhanHangFragment newInstance(String param1, String param2) {
        DaNhanHangFragment fragment = new DaNhanHangFragment();
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
        view = inflater.inflate(R.layout.fragment_da_nhan_hang, container, false);
        donHangs = new ArrayList<>();
        donMuaDaNhanHangAdpater = new DonMuaDaNhanHangAdpater(getActivity(), R.layout.list_item_donhang_nhanhang, donHangs);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvDaNhanHang.setLayoutManager(linearLayoutManager);
        rcvDaNhanHang.setAdapter(donMuaDaNhanHangAdpater);
        LoadDaNhanDonHang();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                donMuaDaNhanHangAdpater.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                donMuaDaNhanHangAdpater.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void LoadDaNhanDonHang() {
        firebase_manager.LoadDaNhanDonHang(donHangs, donMuaDaNhanHangAdpater);
        donMuaDaNhanHangAdpater.setDelegation(new DonMuaDaNhanHangAdpater.ClickItemDonMuaListener() {
            @Override
            public void getInfomationDonMua(DonHang donHang) {
                Intent intent = new Intent(getActivity(), ChiTietDonHang.class);
                Gson gson = new Gson();
                String data = gson.toJson(donHang);
                intent.putExtra("dataDonHang", data);
                getActivity().startActivity(intent);
            }
        });
    }

    private void setControl() {
        rcvDaNhanHang = view.findViewById(R.id.rcvDaNhanHang);
        searchView = view.findViewById(R.id.searchViewDonHang);
    }
}