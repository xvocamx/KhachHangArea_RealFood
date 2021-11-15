package com.example.khachhangarea_realfood.model;

import java.util.Date;

public class BinhLuan {
    String IDBinhLuan, binhLuan, tenKhachHang, avatar;
    Date ngayBinhLuan;

    public BinhLuan(String IDBinhLuan, String binhLuan, String tenKhachHang, String avatar, Date ngayBinhLuan) {
        this.IDBinhLuan = IDBinhLuan;
        this.binhLuan = binhLuan;
        this.tenKhachHang = tenKhachHang;
        this.avatar = avatar;
        this.ngayBinhLuan = ngayBinhLuan;
    }

    public BinhLuan() {
    }

    public String getIDBinhLuan() {
        return IDBinhLuan;
    }

    public void setIDBinhLuan(String IDBinhLuan) {
        this.IDBinhLuan = IDBinhLuan;
    }

    public String getBinhLuan() {
        return binhLuan;
    }

    public void setBinhLuan(String binhLuan) {
        this.binhLuan = binhLuan;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getNgayBinhLuan() {
        return ngayBinhLuan;
    }

    public void setNgayBinhLuan(Date ngayBinhLuan) {
        this.ngayBinhLuan = ngayBinhLuan;
    }
}
