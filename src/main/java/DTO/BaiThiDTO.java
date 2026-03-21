/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.sql.Timestamp;

/**
 *
 * @author Windows
 */
public class BaiThiDTO {

    private int mabaithi;
    private int made;
    private int manguoidung;
    private double diemthi;
    private Timestamp thoigianvaothi;
    private int thoigianlambai;
    private int socaudung;
    private int socausai;

    public BaiThiDTO() {
    }

    public BaiThiDTO(int mabaithi, int made, int manguoidung, double diemthi, Timestamp thoigianvaothi, int thoigianlambai, int socaudung, int socausai) {
        this.mabaithi = mabaithi;
        this.made = made;
        this.manguoidung = manguoidung;
        this.diemthi = diemthi;
        this.thoigianvaothi = thoigianvaothi;
        this.thoigianlambai = thoigianlambai;
        this.socaudung = socaudung;
        this.socausai = socausai;
    }

    // Getters and Setters
    public int getMabaithi() {
        return mabaithi;
    }

    public void setMabaithi(int mabaithi) {
        this.mabaithi = mabaithi;
    }

    public int getMade() {
        return made;
    }

    public void setMade(int made) {
        this.made = made;
    }

    public int getManguoidung() {
        return manguoidung;
    }

    public void setManguoidung(int manguoidung) {
        this.manguoidung = manguoidung;
    }

    public double getDiemthi() {
        return diemthi;
    }

    public void setDiemthi(double diemthi) {
        this.diemthi = diemthi;
    }

    public Timestamp getThoigianvaothi() {
        return thoigianvaothi;
    }

    public void setThoigianvaothi(Timestamp thoigianvaothi) {
        this.thoigianvaothi = thoigianvaothi;
    }

    public int getThoigianlambai() {
        return thoigianlambai;
    }

    public void setThoigianlambai(int thoigianlambai) {
        this.thoigianlambai = thoigianlambai;
    }

    public int getSocaudung() {
        return socaudung;
    }

    public void setSocaudung(int socaudung) {
        this.socaudung = socaudung;
    }

    public int getSocausai() {
        return socausai;
    }

    public void setSocausai(int socausai) {
        this.socausai = socausai;
    }
}
