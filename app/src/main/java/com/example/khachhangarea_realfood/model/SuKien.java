package com.example.khachhangarea_realfood.model;

import android.net.Uri;

public class SuKien {
    String idSuKien, tenSuKien;

    public SuKien(String idSuKien, String tenSuKien) {
        this.idSuKien = idSuKien;
        this.tenSuKien = tenSuKien;
    }

    public SuKien() {
    }

    public String getIdSuKien() {
        return idSuKien;
    }

    public void setIdSuKien(String idSuKien) {
        this.idSuKien = idSuKien;
    }

    public String getTenSuKien() {
        return tenSuKien;
    }

    public void setTenSuKien(String tenSuKien) {
        this.tenSuKien = tenSuKien;
    }

}
