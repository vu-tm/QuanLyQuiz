/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class ChiTietLopDTO {
    private int malop;
    private int manguoidung;
    private int hienthi;

    public ChiTietLopDTO() {
    }

    public ChiTietLopDTO(int malop, int manguoidung, int hienthi) {
        this.malop = malop;
        this.manguoidung = manguoidung;
        this.hienthi = hienthi;
    }
            
    // Getters, Setters
    public int getMalop() {
        return malop;
    }

    public void setMalop(int malop) {
        this.malop = malop;
    }

    public int getManguoidung() {
        return manguoidung;
    }

    public void setManguoidung(int manguoidung) {
        this.manguoidung = manguoidung;
    }

    public int getHienthi() {
        return hienthi;
    }

    public void setHienthi(int hienthi) {
        this.hienthi = hienthi;
    }
    
}