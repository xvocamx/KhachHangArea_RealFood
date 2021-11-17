package com.example.khachhangarea_realfood.fragment;

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
import com.example.khachhangarea_realfood.adapter.DanhGiaSanPhamAdapter;
import com.example.khachhangarea_realfood.adapter.DonMuaAdpater;
import com.example.khachhangarea_realfood.model.DanhGia;
import com.example.khachhangarea_realfood.model.DonHang;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DanhGiaDonMuaFragment extends Fragment {
    View view;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    RecyclerView rcvDanhGia;
    DanhGiaSanPhamAdapter danhGiaSanPhamAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<DanhGia> danhGias;

    public DanhGiaDonMuaFragment() {
        // Required empty public constructor
    }


    public static DanhGiaDonMuaFragment newInstance(String param1, String param2) {
        DanhGiaDonMuaFragment fragment = new DanhGiaDonMuaFragment();
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
        view = inflater.inflate(R.layout.fragment_danh_gia_don_mua, container, false);
        danhGias = new ArrayList<>();
        danhGiaSanPhamAdapter = new DanhGiaSanPhamAdapter(getActivity(),R.layout.list_item_comment,danhGias);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvDanhGia.setLayoutManager(linearLayoutManager);
        rcvDanhGia.setAdapter(danhGiaSanPhamAdapter);
        LoadItemDanhGia();
    }

    private void LoadItemDanhGia() {
        firebase_manager.mDatabase.child("DanhGia").orderByChild("idkhachHang").equalTo(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhGias.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DanhGia danhGia = dataSnapshot.getValue(DanhGia.class);
                    danhGias.add(danhGia);
                    danhGiaSanPhamAdapter.notifyDataSetChanged();
                }
                if(danhGias.size() == 0){
                    danhGias.clear();
                    danhGiaSanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        rcvDanhGia = view.findViewById(R.id.rcvDanhGia);
    }
}