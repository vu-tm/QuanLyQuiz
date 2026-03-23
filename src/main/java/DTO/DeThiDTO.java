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
public class DeThiDTO {
    private int made;
    private int makythi;
    private int monthi;
    private int nguoitao;
    private String tende;
    private Timestamp thoigiantao;
    private int thoigianthi;
    private int tongsocau;
    private boolean trangthai;

    public DeThiDTO() {
    }

    public DeThiDTO(int made, int makythi, int monthi, int nguoitao, String tende, Timestamp thoigiantao, int thoigianthi, int tongsocau, boolean trangthai) {
        this.made = made;
        this.makythi = makythi;
        this.monthi = monthi;
        this.nguoitao = nguoitao;
        this.tende = tende;
        this.thoigiantao = thoigiantao;
        this.thoigianthi = thoigianthi;
        this.tongsocau = tongsocau;
        this.trangthai = trangthai;
    }

    // Getters and Setters
    public int getMade() {
        return made;
    }

    public void setMade(int made) {
        this.made = made;
    }

    public int getMakythi() {
        return makythi;
    }

    public void setMakythi(int makythi) {
        this.makythi = makythi;
    }

    public int getMonthi() {
        return monthi;
    }

    public void setMonthi(int monthi) {
        this.monthi = monthi;
    }

    public int getNguoitao() {
        return nguoitao;
    }

    public void setNguoitao(int nguoitao) {
        this.nguoitao = nguoitao;
    }

    public String getTende() {
        return tende;
    }

    public void setTende(String tende) {
        this.tende = tende;
    }

    public Timestamp getThoigiantao() {
        return thoigiantao;
    }

    public void setThoigiantao(Timestamp thoigiantao) {
        this.thoigiantao = thoigiantao;
    }

    public int getThoigianthi() {
        return thoigianthi;
    }

    public void setThoigianthi(int thoigianthi) {
        this.thoigianthi = thoigianthi;
    }

    public int getTongsocau() {
        return tongsocau;
    }

    public void setTongsocau(int tongsocau) {
        this.tongsocau = tongsocau;
    }

    public boolean isTrangthai() {
        return trangthai;
    }

    public void setTrangthai(boolean trangthai) {
        this.trangthai = trangthai;
    }
}