package com.example.khachhangarea_realfood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.adapter.ThongBaoAdapter;
import com.example.khachhangarea_realfood.model.ThongBao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {
    View view;
    RecyclerView rcvThongBao;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ThongBao> thongBaos;
    ThongBaoAdapter thongBaoAdapter;
    Firebase_Manager firebase_manager = new Firebase_Manager();

    public NotificationFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();

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
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        thongBaos = new ArrayList<>();
        thongBaoAdapter = new ThongBaoAdapter(getActivity(), R.layout.list_item_notifications, thongBaos);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvThongBao.setLayoutManager(linearLayoutManager);
        rcvThongBao.setAdapter(thongBaoAdapter);
        LoadItemThongBao();
    }

    private void LoadItemThongBao() {
        firebase_manager.mDatabase.child("ThongBao").child(firebase_manager.auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                thongBaos.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ThongBao thongBao = dataSnapshot.getValue(ThongBao.class);
                    thongBaos.add(thongBao);
                    thongBaoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        rcvThongBao = view.findViewById(R.id.rcvThongBao);

    }
}