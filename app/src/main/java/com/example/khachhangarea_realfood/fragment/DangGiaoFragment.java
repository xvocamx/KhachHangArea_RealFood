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
import com.example.khachhangarea_realfood.model.DonHang;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DangGiaoFragment extends Fragment {
    View view;
    RecyclerView rcvDangGiao;
    LinearLayoutManager linearLayoutManager;
    DonMuaAdpater donMuaAdpater;
    ArrayList<DonHang> donHangs;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    SearchView searchView;

    public DangGiaoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DangGiaoFragment newInstance(String param1, String param2) {
        DangGiaoFragment fragment = new DangGiaoFragment();
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
        view = inflater.inflate(R.layout.fragment_dang_giao, container, false);
        donHangs = new ArrayList<>();
        donMuaAdpater = new DonMuaAdpater(getActivity(), R.layout.list_item_donhang, donHangs);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvDangGiao.setLayoutManager(linearLayoutManager);
        rcvDangGiao.setAdapter(donMuaAdpater);
        LoadItemDangGiao();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                donMuaAdpater.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                donMuaAdpater.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void LoadItemDangGiao() {
        firebase_manager.LoadDangGiaoDonHang(donHangs, donMuaAdpater);
        donMuaAdpater.setDelegation(new DonMuaAdpater.ClickItemDonMuaListener() {
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
        rcvDangGiao = view.findViewById(R.id.rcvDangGiao);
        searchView = view.findViewById(R.id.searchViewDonHang);
    }
}