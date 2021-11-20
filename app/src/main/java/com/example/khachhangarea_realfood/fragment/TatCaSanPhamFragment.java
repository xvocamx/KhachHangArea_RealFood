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

import com.example.khachhangarea_realfood.ChiTietSanPham;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.TatCaSanPham;
import com.example.khachhangarea_realfood.adapter.SanPhamAdapter;
import com.example.khachhangarea_realfood.adapter.TatCaFoodAdapter;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TatCaSanPhamFragment extends Fragment {
    private View view;
    private RecyclerView rcvTatCaSanPham;
    private Firebase_Manager firebase_manager;
    private ArrayList<SanPham> sanPhams;
    private LinearLayoutManager linearLayoutManager;
    private TatCaFoodAdapter tatCaFoodAdapter;
    private String idCuaHang;
    public TatCaSanPhamFragment(String idCuaHang) {
        this.idCuaHang = idCuaHang;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tatcasanpham, container, false);
        sanPhams = new ArrayList<>();
        tatCaFoodAdapter = new TatCaFoodAdapter(getActivity(), R.layout.list_item_tatcasanpham, sanPhams);
        firebase_manager = new Firebase_Manager();
        setControl();
        setEvent();
        return view;
    }

    private void LoadTatCaSanPham() {
        firebase_manager.mDatabase.child("SanPham").orderByChild("idcuaHang").equalTo(idCuaHang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    if(tatCaFoodAdapter!=null){
                        tatCaFoodAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvTatCaSanPham.setLayoutManager(linearLayoutManager);
        rcvTatCaSanPham.setAdapter(tatCaFoodAdapter);
        LoadTatCaSanPham();

        tatCaFoodAdapter.setDelegation(new SanPhamAdapter.ClickItemFoodListener() {
            @Override
            public void getInformationFood(SanPham sanPham) {
                Intent intent = new Intent(getActivity(), ChiTietSanPham.class);
                Gson gson = new Gson();
                String data = gson.toJson(sanPham);
                intent.putExtra("dataSanPham", data);
                getActivity().startActivity(intent);
            }
        });
    }

    private void setControl() {
        rcvTatCaSanPham = view.findViewById(R.id.rcvTatCaSanPham);
    }
}