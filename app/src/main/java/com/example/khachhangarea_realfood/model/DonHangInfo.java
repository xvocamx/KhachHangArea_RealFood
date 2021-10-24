package com.example.khachhangarea_realfood.model;

public class DonHangInfo {
    String IDInfo,IDDonHang,IDSanPham,soLuong,donGia,maGiamGia;

    public DonHangInfo(String IDInfo, String IDDonHang, String IDSanPham, String soLuong, String donGia, String maGiamGia) {
        this.IDInfo = IDInfo;
        this.IDDonHang = IDDonHang;
        this.IDSanPham = IDSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.maGiamGia = maGiamGia;
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

    public String getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(String IDSanPham) {
        this.IDSanPham = IDSanPham;
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
}
