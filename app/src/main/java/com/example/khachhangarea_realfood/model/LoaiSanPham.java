package com.example.khachhangarea_realfood.model;

public class LoaiSanPham {
    String iDLoai, sTT, tenLoai;

    public LoaiSanPham(String iDLoai, String sTT, String tenLoai) {
        this.iDLoai = iDLoai;
        this.sTT = sTT;
        this.tenLoai = tenLoai;
    }

    public LoaiSanPham() {
    }

    public String getiDLoai() {
        return iDLoai;
    }

    public void setiDLoai(String iDLoai) {
        this.iDLoai = iDLoai;
    }

    public String getsTT() {
        return sTT;
    }

    public void setsTT(String sTT) {
        this.sTT = sTT;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
}
