package com.example.khachhangarea_realfood.model;

import java.util.ArrayList;

public class DonHang {
    String IDDonHang,IDKhachHang,IDShipper,diaChi,soDienThoai,trangThai,ghiChu;
    int tongTien;

    public DonHang() {
    }

    public DonHang(String IDDonHang, String IDKhachHang, String IDShipper, String diaChi, String soDienThoai, String trangThai, String ghiChu, int tongTien) {
        this.IDDonHang = IDDonHang;
        this.IDKhachHang = IDKhachHang;
        this.IDShipper = IDShipper;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
        this.tongTien = tongTien;
    }

    public String getIDDonHang() {
        return IDDonHang;
    }

    public void setIDDonHang(String IDDonHang) {
        this.IDDonHang = IDDonHang;
    }

    public String getIDKhachHang() {
        return IDKhachHang;
    }

    public void setIDKhachHang(String IDKhachHang) {
        this.IDKhachHang = IDKhachHang;
    }

    public String getIDShipper() {
        return IDShipper;
    }

    public void setIDShipper(String IDShipper) {
        this.IDShipper = IDShipper;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }
}
