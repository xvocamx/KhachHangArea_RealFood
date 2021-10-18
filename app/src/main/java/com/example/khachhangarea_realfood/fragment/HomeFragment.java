package com.example.khachhangarea_realfood.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.adapter.SanPhamAdapter;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private View mView;
    private SanPhamAdapter sanPhamAdapter;
    private ArrayList<SanPham> sanPhams;
    private ArrayList<CuaHang> cuaHangs;
    private DatabaseReference mDatabase;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rcvFoodSale;
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
        sanPhams = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(getActivity(), R.layout.list_item_food, sanPhams);
        setControl();
        setEvent();
        return mView;

    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvFoodSale.setLayoutManager(linearLayoutManager);
        rcvFoodSale.setAdapter(sanPhamAdapter);
        getFoodSale();

    }

    public void getFoodSale() {
        mDatabase.child("CuaHang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    cuaHang = dataSnapshot.getValue(CuaHang.class);
                    cuaHangs.add(cuaHang);
                }
                for (CuaHang cuaHang:cuaHangs) {
                    mDatabase.child("SanPham").child(cuaHang.getIDCuaHang()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                SanPham sanPham = postSnapshot.getValue(SanPham.class);
                                sanPhams.add(sanPham);
                                sanPhamAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                Toast.makeText(getActivity(), sanPhams.size()+"Food", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        rcvFoodSale = mView.findViewById(R.id.rcvFoodSale);
    }
}