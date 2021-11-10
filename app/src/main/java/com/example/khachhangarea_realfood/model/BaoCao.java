package com.example.khachhangarea_realfood.model;

import android.widget.ImageView;

public class BaoCao {
    String IDBaoCao,IDKhachHang,IDCuaHang,lyDo;
    ImageView ivBaoCao;

    public BaoCao() {
    }

    public BaoCao(String IDBaoCao, String IDKhachHang, String IDCuaHang, String lyDo, ImageView ivBaoCao) {
        this.IDBaoCao = IDBaoCao;
        this.IDKhachHang = IDKhachHang;
        this.IDCuaHang = IDCuaHang;
        this.lyDo = lyDo;
        this.ivBaoCao = ivBaoCao;
    }

    public String getIDBaoCao() {
        return IDBaoCao;
    }

    public void setIDBaoCao(String IDBaoCao) {
        this.IDBaoCao = IDBaoCao;
    }

    public String getIDKhachHang() {
        return IDKhachHang;
    }

    public void setIDKhachHang(String IDKhachHang) {
        this.IDKhachHang = IDKhachHang;
    }

    public String getIDCuaHang() {
        return IDCuaHang;
    }

    public void setIDCuaHang(String IDCuaHang) {
        this.IDCuaHang = IDCuaHang;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public ImageView getIvBaoCao() {
        return ivBaoCao;
    }

    public void setIvBaoCao(ImageView ivBaoCao) {
        this.ivBaoCao = ivBaoCao;
    }
}
