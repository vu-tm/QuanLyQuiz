/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class LopDTO {
    private int malop;
    private String tenlop;
    private int siso;
    private int namhoc;
    private int hocky;
    private int trangthai;
    private String giangvien;
    private int mamonhoc;

    public LopDTO() {
    }

    public LopDTO(int malop, String tenlop, int siso, int namhoc, int hocky, int trangthai, String giangvien, int mamonhoc) {
        this.malop = malop;
        this.tenlop = tenlop;
        this.siso = siso;
        this.namhoc = namhoc;
        this.hocky = hocky;
        this.trangthai = trangthai;
        this.giangvien = giangvien;
        this.mamonhoc = mamonhoc;
    }
           
    // Getters, Setters
    public int getMalop() {
        return malop;
    }

    public void setMalop(int malop) {
        this.malop = malop;
    }

    public String getTenlop() {
        return tenlop;
    }

    public void setTenlop(String tenlop) {
        this.tenlop = tenlop;
    }

    public int getSiso() {
        return siso;
    }

    public void setSiso(int siso) {
        this.siso = siso;
    }

    public int getNamhoc() {
        return namhoc;
    }

    public void setNamhoc(int namhoc) {
        this.namhoc = namhoc;
    }

    public int getHocky() {
        return hocky;
    }

    public void setHocky(int hocky) {
        this.hocky = hocky;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public String getGiangvien() {
        return giangvien;
    }

    public void setGiangvien(String giangvien) {
        this.giangvien = giangvien;
    }

    public int getMamonhoc() {
        return mamonhoc;
    }

    public void setMamonhoc(int mamonhoc) {
        this.mamonhoc = mamonhoc;
    }
    
}
