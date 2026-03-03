/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class ChiTietBaiThiDTO {
    private int mabaithi;
    private int macauhoi;
    private int dapanchon;
    private String noidungdienkhuyet;

    public ChiTietBaiThiDTO() {
    }

    public ChiTietBaiThiDTO(int mabaithi, int macauhoi, int dapanchon, String noidungdienkhuyet) {
        this.mabaithi = mabaithi;
        this.macauhoi = macauhoi;
        this.dapanchon = dapanchon;
        this.noidungdienkhuyet = noidungdienkhuyet;
    }
    
    // Getters and Setters
    public int getMabaithi() {
        return mabaithi;
    }

    public void setMabaithi(int mabaithi) {
        this.mabaithi = mabaithi;
    }

    public int getMacauhoi() {
        return macauhoi;
    }

    public void setMacauhoi(int macauhoi) {
        this.macauhoi = macauhoi;
    }

    public int getDapanchon() {
        return dapanchon;
    }

    public void setDapanchon(int dapanchon) {
        this.dapanchon = dapanchon;
    }

    public String getNoidungdienkhuyet() {
        return noidungdienkhuyet;
    }

    public void setNoidungdienkhuyet(String noidungdienkhuyet) {
        this.noidungdienkhuyet = noidungdienkhuyet;
    }
}