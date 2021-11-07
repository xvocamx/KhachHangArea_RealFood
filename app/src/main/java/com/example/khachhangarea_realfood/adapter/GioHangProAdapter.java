package com.example.khachhangarea_realfood.adapter;
        import android.app.Activity;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Build;
        import android.util.Log;
        import android.util.SparseBooleanArray;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.annotation.RequiresApi;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
        import com.developer.kalert.KAlertDialog;
        import com.example.khachhangarea_realfood.ChiTietCuaHang;
        import com.example.khachhangarea_realfood.ChiTietSanPham;
        import com.example.khachhangarea_realfood.Firebase_Manager;
        import com.example.khachhangarea_realfood.R;
        import com.example.khachhangarea_realfood.model.CuaHang;
        import com.example.khachhangarea_realfood.model.DonHangInfo;
        import com.example.khachhangarea_realfood.model.GioHangDisplay;
        import com.example.khachhangarea_realfood.model.SanPham;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.gson.Gson;

        import java.util.ArrayList;
        import java.util.Collection;
        import java.util.Map;
        import java.util.Set;

public class GioHangProAdapter extends RecyclerView.Adapter<GioHangProAdapter.MyViewHolder> {
    private Activity context;
    int tong =0;
    private int resource;
    private ArrayList<GioHangDisplay> gioHangDisplays;
    private Firebase_Manager firebase_manager = new Firebase_Manager();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private KAlertDialog kAlertDialog;
    GioHangAdapter.CheckBoxListener checkBoxListener;

    public GioHangAdapter.CheckBoxListener getCheckBoxListener() {
        return checkBoxListener;
    }

    public void setCheckBoxListener(GioHangAdapter.CheckBoxListener checkBoxListener) {
        this.checkBoxListener = checkBoxListener;
    }

    public GioHangProAdapter(Activity context, int resource, ArrayList<GioHangDisplay> gioHangDisplays) {
        this.context = context;
        this.resource = resource;
        this.gioHangDisplays = gioHangDisplays;

    }






    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) context.getLayoutInflater().inflate(viewType, parent, false);
        return new MyViewHolder(linearLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull GioHangProAdapter.MyViewHolder holder, int position) {
        GioHangDisplay gioHangDisplay = gioHangDisplays.get(position);


        if (gioHangDisplay == null) {
            return;
        }

        firebase_manager.mDatabase.child("CuaHang").child(gioHangDisplay.getIdCuaHang()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CuaHang cuaHang = snapshot.getValue(CuaHang.class);
                holder.tvTenCuaHang.setText(cuaHang.getTenCuaHang());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        firebase_manager.storageRef.child("CuaHang").child(gioHangDisplay.getIdCuaHang()).child("Avatar").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(holder.ivShop);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", e.getMessage());
            }
        });

        ArrayList<DonHangInfo> donHangInfos = gioHangDisplay.getSanPhams();
        GioHangAdapter gioHangAdapter = new GioHangAdapter(context,R.layout.list_item_giohang_sanpham,donHangInfos);
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        holder.rcSanPham.setLayoutManager(linearLayoutManager);
        holder.rcSanPham.setAdapter(gioHangAdapter);
        gioHangAdapter.setCheckBoxListener(checkBoxListener);
        holder.ckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ckAll.isChecked())
                {
                    for (DonHangInfo donHangInfo:donHangInfos
                         ) {
                        donHangInfo.setSelected(true);
                    }
                    gioHangAdapter.notifyDataSetChanged();
                    checkBoxListener.getGiaGioHang();
                }
                else {
                    for (DonHangInfo donHangInfo:donHangInfos
                    ) {
                        donHangInfo.setSelected(false);
                    }
                    gioHangAdapter.notifyDataSetChanged();
                    checkBoxListener.getGiaGioHang();
                }
            }
        });
    }



    @Override
    public int getItemViewType(int position) {
        return resource;
    }

    @Override
    public int getItemCount() {
        return gioHangDisplays.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenCuaHang;
        ImageView ivShop;
        ProgressBar pbLoadItemGioHang;
        RecyclerView rcSanPham;
        CheckBox ckAll;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivShop = itemView.findViewById(R.id.ivShop);
            tvTenCuaHang = itemView.findViewById(R.id.tvNameShop);
            pbLoadItemGioHang = itemView.findViewById(R.id.pbLoadItemGioHang);
            rcSanPham = itemView.findViewById(R.id.rcvItemGiohang);
            ckAll = itemView.findViewById(R.id.chkCheckAll);
            this.setIsRecyclable(false);
        }
    }

    public interface CheckBoxListener {
        void getGiaGioHang();
    }


}
