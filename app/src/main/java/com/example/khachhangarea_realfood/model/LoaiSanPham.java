package com.example.khachhangarea_realfood.model;

public class LoaiSanPham {
    String IDLoai, tenLoai;

    public LoaiSanPham(String IDLoai, String tenLoai) {
        this.IDLoai = IDLoai;
        this.tenLoai = tenLoai;
    }

    public LoaiSanPham() {
    }

    public String getIDLoai() {
        return IDLoai;
    }

    public void setIDLoai(String IDLoai) {
        this.IDLoai = IDLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    @Override
    public String toString() {
        return "LoaiSanPham{" +
                "IDLoai='" + IDLoai + '\'' +
                ", tenLoai='" + tenLoai + '\'' +
                '}';
    }
}
