package com.example.cuahangdientuonline.model;

import java.io.Serializable;

public class Sanphamdadat implements Serializable {
    public String Tensp;
    public String hinhanh;
    public Integer giasp;
    public int sl;
    public Integer thanhtien;

    public Sanphamdadat(String tensp, String hinhanh, Integer giasp, int sl, Integer thanhtien) {
        Tensp = tensp;
        this.hinhanh = hinhanh;
        this.giasp = giasp;
        this.sl = sl;
        this.thanhtien = thanhtien;
    }

    public String getTensp() {
        return Tensp;
    }

    public void setTensp(String tensp) {
        Tensp = tensp;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public Integer getGiasp() {
        return giasp;
    }

    public void setGiasp(Integer giasp) {
        this.giasp = giasp;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public Integer getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(Integer thanhtien) {
        this.thanhtien = thanhtien;
    }
}
