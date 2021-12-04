package com.example.khachhangarea_realfood.model;

import java.util.ArrayList;
import java.util.Date;

public class SanPham {
    String IDSanPham, TenSanPham, IDLoai, IDDanhMuc, Gia, ChiTietSanPham, IDCuaHang,size;
    Double giaOld;
    Float Rating;
    ArrayList<String> images;
    int SoLuongBanDuoc;
    Date ngayTao;

    public SanPham() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public SanPham(String IDSanPham, String tenSanPham, String IDLoai, String IDDanhMuc, String gia, String chiTietSanPham, String IDCuaHang, String size, Float rating, ArrayList<String> images, Date ngayTao) {
        this.IDSanPham = IDSanPham;
        TenSanPham = tenSanPham;
        this.IDLoai = IDLoai;
        this.IDDanhMuc = IDDanhMuc;
        Gia = gia;
        ChiTietSanPham = chiTietSanPham;
        this.IDCuaHang = IDCuaHang;
        this.size = size;
        Rating = rating;
        this.images = images;
        this.ngayTao = ngayTao;
    }

    public String getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(String IDSanPham) {
        this.IDSanPham = IDSanPham;
    }

    public String getTenSanPham() {
        return TenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        TenSanPham = tenSanPham;
    }

    public String getIDLoai() {
        return IDLoai;
    }

    public void setIDLoai(String IDLoai) {
        this.IDLoai = IDLoai;
    }

    public String getIDDanhMuc() {
        return IDDanhMuc;
    }

    public void setIDDanhMuc(String IDDanhMuc) {
        this.IDDanhMuc = IDDanhMuc;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        Gia = gia;
    }

    public String getChiTietSanPham() {
        return ChiTietSanPham;
    }

    public void setChiTietSanPham(String chiTietSanPham) {
        ChiTietSanPham = chiTietSanPham;
    }

    public String getIDCuaHang() {
        return IDCuaHang;
    }

    public void setIDCuaHang(String IDCuaHang) {
        this.IDCuaHang = IDCuaHang;
    }

    public Float getRating() {
        return Rating;
    }

    public void setRating(Float rating) {
        Rating = rating;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getSoLuongBanDuoc() {
        return SoLuongBanDuoc;
    }

    public void setSoLuongBanDuoc(int soLuongBanDuoc) {
        SoLuongBanDuoc = soLuongBanDuoc;
    }

    public Double getGiaOld() {
        return giaOld;
    }

    public void setGiaOld(Double giaOld) {
        this.giaOld = giaOld;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "IDSanPham='" + IDSanPham + '\'' +
                ", TenSanPham='" + TenSanPham + '\'' +
                ", IDLoai='" + IDLoai + '\'' +
                ", IDDanhMuc='" + IDDanhMuc + '\'' +
                ", Gia='" + Gia + '\'' +
                ", ChiTietSanPham='" + ChiTietSanPham + '\'' +
                ", IDCuaHang='" + IDCuaHang + '\'' +
                ", Rating=" + Rating +
                ", images=" + images +
                '}';
    }
}
