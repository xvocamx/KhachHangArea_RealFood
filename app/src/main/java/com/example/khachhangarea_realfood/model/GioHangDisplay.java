package com.example.khachhangarea_realfood.model;

import java.util.ArrayList;

public class GioHangDisplay {
    String idCuaHang;
    ArrayList <DonHangInfo>donHangInfos = new ArrayList<>();

    public GioHangDisplay(String idCuaHang, ArrayList<DonHangInfo> donHangInfos) {
        this.idCuaHang = idCuaHang;
        this.donHangInfos = donHangInfos;
    }

    public GioHangDisplay() {

    }

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
}
