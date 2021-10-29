package com.example.khachhangarea_realfood.model;

public class YeuThich {
    CuaHang cuaHang;
    SanPham sanPham;

    public YeuThich(CuaHang cuaHang, SanPham sanPham) {
        this.cuaHang = cuaHang;
        this.sanPham = sanPham;
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
