package com.example.khachhangarea_realfood.model;

public class DanhMuc {
    String IDCuaHang,IDDanhMuc,TenDanhMuc;

    public DanhMuc() {
    }

    public DanhMuc(String IDCuaHang, String IDDanhMuc, String tenDanhMuc) {
        this.IDCuaHang = IDCuaHang;
        this.IDDanhMuc = IDDanhMuc;
        TenDanhMuc = tenDanhMuc;
    }

    public String getIDCuaHang() {
        return IDCuaHang;
    }

    public void setIDCuaHang(String IDCuaHang) {
        this.IDCuaHang = IDCuaHang;
    }

    public String getIDDanhMuc() {
        return IDDanhMuc;
    }

    public void setIDDanhMuc(String IDDanhMuc) {
        this.IDDanhMuc = IDDanhMuc;
    }

    public String getTenDanhMuc() {
        return TenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        TenDanhMuc = tenDanhMuc;
    }

    @Override
    public String toString() {
        return "DanhMuc{" +
                "IDCuaHang='" + IDCuaHang + '\'' +
                ", IDDanhMuc='" + IDDanhMuc + '\'' +
                ", TenDanhMuc='" + TenDanhMuc + '\'' +
                '}';
    }
}
