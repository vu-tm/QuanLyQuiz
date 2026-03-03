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
    private String nguoitao;
    private String tende;
    private Timestamp thoigiantao;
    private int thoigianthi;
    private boolean hienthibailam;
    private boolean xemdiemthi;
    private boolean xemdapan;
    private boolean trangthai;

    public DeThiDTO() {
    }
    
    public DeThiDTO(int made, int makythi, int monthi, String nguoitao, String tende, Timestamp thoigiantao, int thoigianthi, boolean hienthibailam, boolean xemdiemthi, boolean xemdapan, boolean trangthai) {
        this.made = made;
        this.makythi = makythi;
        this.monthi = monthi;
        this.nguoitao = nguoitao;
        this.tende = tende;
        this.thoigiantao = thoigiantao;
        this.thoigianthi = thoigianthi;
        this.hienthibailam = hienthibailam;
        this.xemdiemthi = xemdiemthi;
        this.xemdapan = xemdapan;
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

    public String getNguoitao() {
        return nguoitao;
    }

    public void setNguoitao(String nguoitao) {
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

    public boolean isHienthibailam() {
        return hienthibailam;
    }

    public void setHienthibailam(boolean hienthibailam) {
        this.hienthibailam = hienthibailam;
    }

    public boolean isXemdiemthi() {
        return xemdiemthi;
    }

    public void setXemdiemthi(boolean xemdiemthi) {
        this.xemdiemthi = xemdiemthi;
    }

    public boolean isXemdapan() {
        return xemdapan;
    }

    public void setXemdapan(boolean xemdapan) {
        this.xemdapan = xemdapan;
    }

    public boolean isTrangthai() {
        return trangthai;
    }

    public void setTrangthai(boolean trangthai) {
        this.trangthai = trangthai;
    }
    
}
