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
import android.widget.Toast;

import com.example.khachhangarea_realfood.ChiTietCuaHang;
import com.example.khachhangarea_realfood.ChiTietSanPham;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.adapter.YeuThichFoodAdapter;
import com.example.khachhangarea_realfood.adapter.YeuThichShopAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


public class YeuThichFoodFragment extends Fragment {
    private View view;
    private RecyclerView rcvYeuThichFood;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private YeuThichFoodAdapter yeuThichFoodAdapter;
    private ArrayList<SanPham> sanPhams;
    private ProgressBar pbFavoriteFood;

    public YeuThichFoodFragment() {
    }


    public static YeuThichFoodFragment newInstance(String param1, String param2) {
        YeuThichFoodFragment fragment = new YeuThichFoodFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_yeu_thich_food, container, false);
        sanPhams = new ArrayList<>();
        yeuThichFoodAdapter = new YeuThichFoodAdapter(getActivity(),R.layout.list_item_food_yeuthich,sanPhams);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvYeuThichFood.setLayoutManager(linearLayoutManager);
        rcvYeuThichFood.setAdapter(yeuThichFoodAdapter);
        LoadFavoriteFood();

        yeuThichFoodAdapter.setDelegation(new YeuThichFoodAdapter.ClickItemFoodListener() {
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

    private void LoadFavoriteFood() {
        mDatabase.child("YeuThich").child(auth.getUid()).child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                sanPhams.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(sanPham);
                    yeuThichFoodAdapter.notifyDataSetChanged();
                }
                pbFavoriteFood.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        rcvYeuThichFood = view.findViewById(R.id.rcvFavoriteFood);
        pbFavoriteFood = view.findViewById(R.id.pbFavoriteFood);
    }
}