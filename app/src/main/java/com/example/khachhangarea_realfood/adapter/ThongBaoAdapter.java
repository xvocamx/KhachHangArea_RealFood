package com.example.khachhangarea_realfood.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.developer.kalert.KAlertDialog;
import com.example.khachhangarea_realfood.Firebase_Manager;
import com.example.khachhangarea_realfood.R;
import com.example.khachhangarea_realfood.TrangThai.LoaiThongBao;
import com.example.khachhangarea_realfood.TrangThai.TrangThaiThongBao;
import com.example.khachhangarea_realfood.model.CuaHang;
import com.example.khachhangarea_realfood.model.KhachHang;
import com.example.khachhangarea_realfood.model.TaiKhoanNganHang;
import com.example.khachhangarea_realfood.model.ThongBao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.MyViewHolder> {
    Activity context;
    int resource;
    ArrayList<ThongBao> thongBaos;
    Firebase_Manager firebase_manager = new Firebase_Manager();
    ClickItemThongBaoListener delegation;
    ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    KAlertDialog kAlertDialog;

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
        View linearLayout = context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoAdapter.MyViewHolder holder, int position) {
        ThongBao thongBao = thongBaos.get(position);
        if (thongBao == null) {
            return;
        }
        viewBinderHelper.bind(holder.swipeRevealLayout, thongBao.getIDThongBao());
        firebase_manager.LoadTenKhachHang(holder.tvTenKhachHang);
        holder.tvNoiDungThongBao.setText(thongBao.getNoiDung());
        String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(thongBao.getDate());
        holder.tvThoiGianThongBao.setText(date);
        holder.tvIDDonHang.setText(thongBao.getDonHang().getIDDonHang());
        holder.lnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLog(Gravity.CENTER, thongBao.getNoiDung());
            }
        });
        holder.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegation != null) {
                    delegation.getInformationThongBao(thongBao);
                }
            }
        };
        if (thongBao.getTrangThaiThongBao().equals(TrangThaiThongBao.DaXem)) {
            holder.ivThongBao.setImageResource(R.drawable.ic_baseline_brightness_green_24);
        }

    }

    private void openDiaLog(int gravity, String noiDung) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_thanhtoan);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true);
        }

        TextView tvNoiDung = dialog.findViewById(R.id.tvNoiDung);
        tvNoiDung.setText(noiDung);

        dialog.show();
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
        SwipeRevealLayout swipeRevealLayout;
        LinearLayout lnInfo;
        ImageView ivThongBao;
        TextView tvTenKhachHang, tvThoiGianThongBao, tvNoiDungThongBao, tvIDDonHang;
        LinearLayout lnThongBao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvThoiGianThongBao = itemView.findViewById(R.id.tvThoiGian);
            tvNoiDungThongBao = itemView.findViewById(R.id.tvNoiDung);
            lnInfo = itemView.findViewById(R.id.lnInfo);
            lnThongBao = itemView.findViewById(R.id.lnThongBao);
            tvIDDonHang = itemView.findViewById(R.id.tvIDDonHang);
            swipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout);
            ivThongBao = itemView.findViewById(R.id.ivThongBao);
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
