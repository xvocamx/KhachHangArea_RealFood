package com.example.khachhangarea_realfood.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.khachhangarea_realfood.DangNhap;
import com.example.khachhangarea_realfood.DoiMatKhau;
import com.example.khachhangarea_realfood.DonMua;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.SuaThongTin;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private View mView;
    private TextView tvDoiMatKhau,tvSua;
    private Button btnDangXuat;
    private LinearLayout lnDonMua;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;
    private FirebaseUser user;
    private TextView tvName,tvEmail,tvPhone,tvDiaChi;
    private CircleImageView civAvatar;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        mView = inflater.inflate(R.layout.fragment_setting, container, false);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        user = auth.getCurrentUser();
        setControl();
        setEvent();
        LoadInfoKhachHang();
        return mView;
    }

    private void setEvent() {
        tvDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoiMatKhau.class);
                startActivity(intent);
            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), DangNhap.class);
                startActivity(intent);

            }
        });
        lnDonMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DonMua.class);
                startActivity(intent);
            }
        });
        tvSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SuaThongTin.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        tvDoiMatKhau = mView.findViewById(R.id.tvDoiMatKhauMoi);
        btnDangXuat = mView.findViewById(R.id.btnDangXuat);
        lnDonMua = mView.findViewById(R.id.lnDonMua);
        tvSua = mView.findViewById(R.id.tvSua);
        tvName = mView.findViewById(R.id.tvName);
        tvEmail = mView.findViewById(R.id.tvEmail);
        tvPhone = mView.findViewById(R.id.tvPhone);
        tvDiaChi = mView.findViewById(R.id.tvAddress);
        civAvatar = mView.findViewById(R.id.civAvatar);
    }
    private void LoadInfoKhachHang() {
        tvEmail.setText(user.getEmail());
        mDatabase.child("KhachHang").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                KhachHang khachHang = snapshot.getValue(KhachHang.class);
                tvName.setText(khachHang.getTenKhachHang());
                tvPhone.setText(khachHang.getSoDienThoai());
                tvDiaChi.setText(khachHang.getDiaChi());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        storageRef.child("KhachHang").child(user.getUid()).child("AvatarKhachHang").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getActivity()).load(uri.toString()).into(civAvatar);
            }
        });
    }

}