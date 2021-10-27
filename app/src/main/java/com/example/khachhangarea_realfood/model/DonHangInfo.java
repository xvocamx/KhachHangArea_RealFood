package com.example.khachhangarea_realfood.model;

public class DonHangInfo {
    String IDInfo, IDDonHang, soLuong, donGia, maGiamGia;
    SanPham sanPham;

    public DonHangInfo(String IDInfo, String IDDonHang, String soLuong, String donGia, String maGiamGia, SanPham sanPham) {
        this.IDInfo = IDInfo;
        this.IDDonHang = IDDonHang;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.maGiamGia = maGiamGia;
        this.sanPham = sanPham;
    }

    public DonHangInfo() {
    }

    public String getIDInfo() {
        return IDInfo;
    }

    public void setIDInfo(String IDInfo) {
        this.IDInfo = IDInfo;
    }

    public String getIDDonHang() {
        return IDDonHang;
    }

    public void setIDDonHang(String IDDonHang) {
        this.IDDonHang = IDDonHang;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getDonGia() {
        return donGia;
    }

    public void setDonGia(String donGia) {
        this.donGia = donGia;
    }

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}
