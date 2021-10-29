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

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.khachhangarea_realfood.ChiTietCuaHang;
import com.example.khachhangarea_realfood.ChiTietSanPham;
import com.example.khachhangarea_realfood.GioHang;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.SearchViewSanPham;
import com.example.khachhangarea_realfood.TrangThaiCuaHang;
import com.example.khachhangarea_realfood.adapter.CuaHangAdapter;
import com.example.khachhangarea_realfood.adapter.DanhMucAdapter;
import com.example.khachhangarea_realfood.adapter.LoaiSanPhamAdapter;
import com.example.khachhangarea_realfood.adapter.SanPhamAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.DanhMuc;
import com.example.khachhangarea_realfood.model.LoaiSanPham;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private View mView;
    private SanPhamAdapter sanPhamAdapter;
    private CuaHangAdapter cuaHangAdapter;
    private LoaiSanPhamAdapter loaiSanPhamAdapter;
    private ArrayList<SanPham> sanPhamSaleFoods, sanPhamPopularFoods;
    private ArrayList<CuaHang> cuaHangs;
    private ArrayList<LoaiSanPham> loaiSanPhams;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManagerSaleFood, linearLayoutManagerPopularShop, linearLayoutManagerPopularFood, linearLayoutManagerLoai;
    private RecyclerView rcvFoodSale, rcvPopularShop, rcvPopularFood, rcvLoai;
    private Button btnTimKiem;
    private ImageView ivMyOrder;
    private ProgressBar pbLoad;
    CuaHang cuaHang;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        cuaHangs = new ArrayList<>();
        sanPhamSaleFoods = new ArrayList<>();
        sanPhamPopularFoods = new ArrayList<>();
        loaiSanPhams = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(getActivity(), R.layout.list_item_food, sanPhamSaleFoods);
        cuaHangAdapter = new CuaHangAdapter(getActivity(), R.layout.list_item_shop, cuaHangs);
        loaiSanPhamAdapter = new LoaiSanPhamAdapter(getActivity(), R.layout.list_item_loaisanpham, loaiSanPhams);
        setControl();
        setEvent();
        return mView;

    }

    private void setEvent() {
        //Sale Food
        linearLayoutManagerSaleFood = new LinearLayoutManager(getActivity());
        linearLayoutManagerSaleFood.setOrientation(RecyclerView.HORIZONTAL);
        rcvFoodSale.setLayoutManager(linearLayoutManagerSaleFood);
        rcvFoodSale.setAdapter(sanPhamAdapter);
        getFoodSale();
        //Popular Shop
        linearLayoutManagerPopularShop = new LinearLayoutManager(getActivity());
        linearLayoutManagerPopularShop.setOrientation(RecyclerView.VERTICAL);
        rcvPopularShop.setLayoutManager(linearLayoutManagerPopularShop);
        rcvPopularShop.setAdapter(cuaHangAdapter);
        getPopularShop();
        //Popular Food
        linearLayoutManagerPopularFood = new LinearLayoutManager(getActivity());
        linearLayoutManagerPopularFood.setOrientation(RecyclerView.VERTICAL);
        rcvPopularFood.setLayoutManager(linearLayoutManagerPopularFood);
        rcvPopularFood.setAdapter(sanPhamAdapter);
        getPopularFood();
        //Danh muc
        linearLayoutManagerLoai = new LinearLayoutManager(getActivity());
        linearLayoutManagerLoai.setOrientation(RecyclerView.HORIZONTAL);
        rcvLoai.setLayoutManager(linearLayoutManagerLoai);
        rcvLoai.setAdapter(loaiSanPhamAdapter);
        getLoai();

        sanPhamAdapter.setDelegation(new SanPhamAdapter.ClickItemFoodListener() {
            @Override
            public void getInformationFood(SanPham sanPham) {
                Intent intent = new Intent(getContext(), ChiTietSanPham.class);
                Gson gson = new Gson();
                String data = gson.toJson(sanPham);
                intent.putExtra("dataSanPham", data);
                getActivity().startActivity(intent);
            }
        });
        cuaHangAdapter.setDelegation(new CuaHangAdapter.ClickItemShopListener() {
            @Override
            public void getInformationShop(CuaHang cuaHang) {
                Intent intent = new Intent(getContext(), ChiTietCuaHang.class);
                Gson gson = new Gson();
                String data = gson.toJson(cuaHang);
                intent.putExtra("dataCuaHang", data);
                getActivity().startActivity(intent);
                Toast.makeText(getContext(), cuaHang.getIDCuaHang()+"", Toast.LENGTH_SHORT).show();
            }
        });
//        loaiSanPhamAdapter.setDelegation(new LoaiSanPhamAdapter.ClickItemLoai() {
//            @Override
//            public void getLoai(LoaiSanPham loaiSanPham) {
//                Intent intent = new Intent(getContext(),LoaiSanPham.class);
//                getActivity().startActivity(intent);
//            }
//        });
        ivMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GioHang.class);
                getActivity().startActivity(intent);
            }
        });

        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchViewSanPham.class);
                getActivity().startActivity(intent);
            }
        });
    }

    public void getLoai() {
        mDatabase.child("LoaiSanPham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loaiSanPhams.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    LoaiSanPham loaiSanPham = dataSnapshot.getValue(LoaiSanPham.class);
                    loaiSanPhams.add(loaiSanPham);
                    loaiSanPhamAdapter.notifyDataSetChanged();
                }
                pbLoad.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFoodSale() {
        mDatabase.child("SanPham").limitToFirst(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = postSnapshot.getValue(SanPham.class);
                    sanPhamSaleFoods.add(sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getPopularShop() {
        mDatabase.child("CuaHang").limitToFirst(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cuaHangs.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    cuaHang = dataSnapshot.getValue(CuaHang.class);
                    if (cuaHang.getTrangThaiCuaHang() != TrangThaiCuaHang.ChuaKichHoat) {
                        cuaHangs.add(cuaHang);
                        cuaHangAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getPopularFood() {
        mDatabase.child("SanPham").limitToFirst(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    SanPham sanPham = postSnapshot.getValue(SanPham.class);
                    sanPhamPopularFoods.add(sanPham);
                    sanPhamAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void setControl() {
        rcvFoodSale = mView.findViewById(R.id.rcvFoodSale);
        rcvPopularFood = mView.findViewById(R.id.rcvPopularFood);
        rcvPopularShop = mView.findViewById(R.id.rcvPopularShop);
        rcvLoai = mView.findViewById(R.id.rcvLoai);
        ivMyOrder = mView.findViewById(R.id.ivMyOrder);
        pbLoad = mView.findViewById(R.id.pbLoad);
        btnTimKiem = mView.findViewById(R.id.btnTimKiem);
    }
}