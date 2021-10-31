package com.example.khachhangarea_realfood.model;

public class YeuThich {
    String IDYeuThich;
    CuaHang cuaHang;
    SanPham sanPham;

    public YeuThich(String IDYeuThich, CuaHang cuaHang, SanPham sanPham) {
        this.IDYeuThich = IDYeuThich;
        this.cuaHang = cuaHang;
        this.sanPham = sanPham;
    }

    public YeuThich() {
    }

    public String getIDYeuThich() {
        return IDYeuThich;
    }

    public void setIDYeuThich(String IDYeuThich) {
        this.IDYeuThich = IDYeuThich;
    }

    public CuaHang getCuaHang() {
        return cuaHang;
    }

    public void setCuaHang(CuaHang cuaHang) {
        this.cuaHang = cuaHang;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}
