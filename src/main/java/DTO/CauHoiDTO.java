/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class CauHoiDTO {
    private int macauhoi;
    private String noidung;
    private int madokho;
    private int maloai;
    private int mamonhoc;
    private int nguoitao;
    private int trangthai;

    public CauHoiDTO() {
    }

    public CauHoiDTO(int macauhoi, String noidung, int madokho, int maloai, int mamonhoc, int nguoitao, int trangthai) {
        this.macauhoi = macauhoi;
        this.noidung = noidung;
        this.madokho = madokho;
        this.maloai = maloai;
        this.mamonhoc = mamonhoc;
        this.nguoitao = nguoitao;
        this.trangthai = trangthai;
    }
      
    // Getters and Setters
    public int getMacauhoi() {
        return macauhoi;
    }

    public void setMacauhoi(int macauhoi) {
        this.macauhoi = macauhoi;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public int getMadokho() {
        return madokho;
    }

    public void setMadokho(int madokho) {
        this.madokho = madokho;
    }

    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }

    public int getMamonhoc() {
        return mamonhoc;
    }

    public void setMamonhoc(int mamonhoc) {
        this.mamonhoc = mamonhoc;
    }

    public int getNguoitao() {
        return nguoitao;
    }

    public void setNguoitao(int nguoitao) {
        this.nguoitao = nguoitao;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }
    
}