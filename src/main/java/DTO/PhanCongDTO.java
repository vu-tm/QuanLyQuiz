/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class PhanCongDTO {
    private int mamonhoc;
    private String manguoidung;

    public PhanCongDTO() {
    }

    public PhanCongDTO(int mamonhoc, String manguoidung) {
        this.mamonhoc = mamonhoc;
        this.manguoidung = manguoidung;
    }
            
    // Getters, Setters
    public int getMamonhoc() {
        return mamonhoc;
    }

    public void setMamonhoc(int mamonhoc) {
        this.mamonhoc = mamonhoc;
    }

    public String getManguoidung() {
        return manguoidung;
    }

    public void setManguoidung(String manguoidung) {
        this.manguoidung = manguoidung;
    }
    
}
