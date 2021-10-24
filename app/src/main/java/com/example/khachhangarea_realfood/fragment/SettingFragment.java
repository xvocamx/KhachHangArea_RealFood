package com.example.khachhangarea_realfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.khachhangarea_realfood.DangNhap;
import com.example.khachhangarea_realfood.DoiMatKhau;
import com.example.khachhangarea_realfood.DonMua;
import com.example.khachhangarea_realfood.R;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private View mView;
    private TextView tvDoiMatKhau;
    private Button btnDangXuat;
    private LinearLayout lnDonMua;

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
        setControl();
        setEvent();
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
    }

    private void setControl() {
        tvDoiMatKhau = mView.findViewById(R.id.tvDoiMatKhauMoi);
        btnDangXuat = mView.findViewById(R.id.btnDangXuat);
        lnDonMua = mView.findViewById(R.id.lnDonMua);
    }


}