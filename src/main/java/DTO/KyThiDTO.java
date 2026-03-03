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
public class KyThiDTO {
    private int makythi;
    private String tenkythi;
    private Timestamp thoigianbatdau;
    private Timestamp thoigianketthuc;
    private int trangthai;

    public KyThiDTO() {
    }
    
    public KyThiDTO(int makythi, String tenkythi, Timestamp thoigianbatdau, Timestamp thoigianketthuc, int trangthai) {
        this.makythi = makythi;
        this.tenkythi = tenkythi;
        this.thoigianbatdau = thoigianbatdau;
        this.thoigianketthuc = thoigianketthuc;
        this.trangthai = trangthai;
    }
        
    // Getters, Setters
    public int getMakythi() {
        return makythi;
    }

    public void setMakythi(int makythi) {
        this.makythi = makythi;
    }

    public String getTenkythi() {
        return tenkythi;
    }

    public void setTenkythi(String tenkythi) {
        this.tenkythi = tenkythi;
    }

    public Timestamp getThoigianbatdau() {
        return thoigianbatdau;
    }

    public void setThoigianbatdau(Timestamp thoigianbatdau) {
        this.thoigianbatdau = thoigianbatdau;
    }

    public Timestamp getThoigianketthuc() {
        return thoigianketthuc;
    }

    public void setThoigianketthuc(Timestamp thoigianketthuc) {
        this.thoigianketthuc = thoigianketthuc;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }
    
}