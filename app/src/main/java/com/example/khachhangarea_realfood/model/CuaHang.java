package com.example.khachhangarea_realfood.model;

public class CuaHang {
    String IDCuaHang;
    String TenCuaHang;
    String ChuSoHuu;
    String ThongTinChiTiet;
    String SoCMND;
    String SoDienThoai;
    String  Avatar ;
    String WallPaper;
    Float Rating;
    String Email;
    String TrangThai;
    String  CMND_MatTruoc ;
    String CMND_MatSau;

    public CuaHang() {
    }

    public CuaHang(String IDCuaHang, String tenCuaHang, String chuSoHuu, String thongTinChiTiet, String soCMND, String soDienThoai, String avatar, String wallPaper, Float rating, String email, String trangThai, String CMND_MatTruoc, String CMND_MatSau) {
        this.IDCuaHang = IDCuaHang;
        this.TenCuaHang = tenCuaHang;
        this.ChuSoHuu = chuSoHuu;
        this.ThongTinChiTiet = thongTinChiTiet;
        this.SoCMND = soCMND;
        this.SoDienThoai = soDienThoai;
        this.Avatar = avatar;
        this.WallPaper = wallPaper;
        this.Rating = rating;
        this.Email = email;
        this.TrangThai = trangThai;
        this.CMND_MatTruoc = CMND_MatTruoc;
        this.CMND_MatSau = CMND_MatSau;
    }

    public String getIDCuaHang() {
        return IDCuaHang;
    }

    public void setIDCuaHang(String IDCuaHang) {
        this.IDCuaHang = IDCuaHang;
    }

    public String getTenCuaHang() {
        return TenCuaHang;
    }

    public void setTenCuaHang(String tenCuaHang) {
        TenCuaHang = tenCuaHang;
    }

    public String getChuSoHuu() {
        return ChuSoHuu;
    }

    public void setChuSoHuu(String chuSoHuu) {
        ChuSoHuu = chuSoHuu;
    }

    public String getThongTinChiTiet() {
        return ThongTinChiTiet;
    }

    public void setThongTinChiTiet(String thongTinChiTiet) {
        ThongTinChiTiet = thongTinChiTiet;
    }

    public String getSoCMND() {
        return SoCMND;
    }

    public void setSoCMND(String soCMND) {
        SoCMND = soCMND;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getWallPaper() {
        return WallPaper;
    }

    public void setWallPaper(String wallPaper) {
        WallPaper = wallPaper;
    }

    public Float getRating() {
        return Rating;
    }

    public void setRating(Float rating) {
        Rating = rating;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public String getCMND_MatTruoc() {
        return CMND_MatTruoc;
    }

    public void setCMND_MatTruoc(String CMND_MatTruoc) {
        this.CMND_MatTruoc = CMND_MatTruoc;
    }

    public String getCMND_MatSau() {
        return CMND_MatSau;
    }

    public void setCMND_MatSau(String CMND_MatSau) {
        this.CMND_MatSau = CMND_MatSau;
    }

    @Override
    public String toString() {
        return "CuaHang{" +
                "IDCuaHang='" + IDCuaHang + '\'' +
                ", TenCuaHang='" + TenCuaHang + '\'' +
                ", ChuSoHuu='" + ChuSoHuu + '\'' +
                ", ThongTinChiTiet='" + ThongTinChiTiet + '\'' +
                ", SoCMND='" + SoCMND + '\'' +
                ", SoDienThoai='" + SoDienThoai + '\'' +
                ", Avatar='" + Avatar + '\'' +
                ", WallPaper='" + WallPaper + '\'' +
                ", Rating=" + Rating +
                ", Email='" + Email + '\'' +
                ", TrangThai='" + TrangThai + '\'' +
                ", CMND_MatTruoc='" + CMND_MatTruoc + '\'' +
                ", CMND_MatSau='" + CMND_MatSau + '\'' +
                '}';
    }
}
