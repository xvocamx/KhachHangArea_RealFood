package com.example.khachhangarea_realfood.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GioHangDisplay implements Parcelable {
    String idCuaHang;
    ArrayList <DonHangInfo>donHangInfos = new ArrayList<>();

    public GioHangDisplay(String idCuaHang, ArrayList<DonHangInfo> donHangInfos) {
        this.idCuaHang = idCuaHang;
        this.donHangInfos = donHangInfos;
    }

    public GioHangDisplay() {

    }

    protected GioHangDisplay(Parcel in) {
        idCuaHang = in.readString();
    }

    public static final Creator<GioHangDisplay> CREATOR = new Creator<GioHangDisplay>() {
        @Override
        public GioHangDisplay createFromParcel(Parcel in) {
            return new GioHangDisplay(in);
        }

        @Override
        public GioHangDisplay[] newArray(int size) {
            return new GioHangDisplay[size];
        }
    };

    public String getIdCuaHang() {
        return idCuaHang;
    }

    public void setIdCuaHang(String idCuaHang) {
        this.idCuaHang = idCuaHang;
    }

    public ArrayList<DonHangInfo> getSanPhams() {
        return donHangInfos;
    }

    public void setSanPhams(ArrayList<DonHangInfo> sanPhams) {
        this.donHangInfos = sanPhams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idCuaHang);
    }
}
