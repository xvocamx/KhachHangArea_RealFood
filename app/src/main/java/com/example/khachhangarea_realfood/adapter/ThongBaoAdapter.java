package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.example.khachhangarea_realfood.model.ThongBao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.MyViewHolder> {
    Activity context;
    int resource;
    ArrayList<ThongBao> thongBaos;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    ClickItemThongBaoListener delegation;

    public void setDelegation(ClickItemThongBaoListener delegation) {
        this.delegation = delegation;
    }

    public ThongBaoAdapter(Activity context, int resource, ArrayList<ThongBao> thongBaos) {
        this.context = context;
        this.resource = resource;
        this.thongBaos = thongBaos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView linearLayout = (CardView) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoAdapter.MyViewHolder holder, int position) {
        ThongBao thongBao = thongBaos.get(position);
        if (thongBao == null) {
            return;
        }
        firebase_manager.LoadTenKhachHang(holder.tvTenKhachHang);
        holder.tvNoiDungThongBao.setText(thongBao.getNoiDung());
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(thongBao.getDate());
        holder.tvThoiGianThongBao.setText(date);
        holder.tvTrangThai.setText(firebase_manager.GetStringTrangThaiThongBao(thongBao.getTrangThaiThongBao()));

        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegation != null) {
                    delegation.getInformationThongBao(thongBao);
                }
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return thongBaos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View.OnClickListener onClickListener;
        TextView tvTenKhachHang, tvThoiGianThongBao, tvNoiDungThongBao, tvTrangThai;
        LinearLayout lnThongBao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvThoiGianThongBao = itemView.findViewById(R.id.tvThoiGian);
            tvNoiDungThongBao = itemView.findViewById(R.id.tvNoiDung);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            lnThongBao = itemView.findViewById(R.id.lnThongBao);
            lnThongBao.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

    public interface ClickItemThongBaoListener {
        void getInformationThongBao(ThongBao thongBao);
    }
}
