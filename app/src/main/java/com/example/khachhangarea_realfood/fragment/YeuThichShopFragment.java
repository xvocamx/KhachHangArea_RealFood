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
import android.widget.ProgressBar;

import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.adapter.YeuThichShopAdapter;
import com.example.khachhangarea_realfood.ChiTietCuaHang;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class YeuThichShopFragment extends Fragment {
    private View view;
    private RecyclerView rcvYeuThichShop;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private YeuThichShopAdapter yeuThichShopAdapter;
    private ArrayList<CuaHang> cuaHangs;
    private ProgressBar pbFavoriteShop;

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
        cuaHangs = new ArrayList<>();
        yeuThichShopAdapter = new YeuThichShopAdapter(getActivity(), R.layout.list_item_shop_yeuthich, cuaHangs);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvYeuThichShop.setLayoutManager(linearLayoutManager);
        rcvYeuThichShop.setAdapter(yeuThichShopAdapter);
        LoadFavoriteShop();

        yeuThichShopAdapter.setDelegation(new YeuThichShopAdapter.ClickItemShopListener() {
            @Override
            public void getInformationShop(CuaHang cuaHang) {
                Intent intent = new Intent(getActivity(),ChiTietCuaHang.class);
                Gson gson = new Gson();
                String data = gson.toJson(cuaHang);
                intent.putExtra("dataCuaHang", data);
                getActivity().startActivity(intent);
            }
        });
    }

    private void LoadFavoriteShop() {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Shop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cuaHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CuaHang cuaHangAll =dataSnapshot.getValue(CuaHang.class);
                    cuaHangs.add(cuaHangAll);
                    yeuThichShopAdapter.notifyDataSetChanged();
                }
                pbFavoriteShop.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        rcvYeuThichShop = view.findViewById(R.id.rcvFavoriteShop);
        pbFavoriteShop = view.findViewById(R.id.pbFavoriteShop);
    }
}