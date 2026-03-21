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
    private int manguoidung;

    public PhanCongDTO() {
    }

    public PhanCongDTO(int mamonhoc, int manguoidung) {
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

    public int getManguoidung() {
        return manguoidung;
    }

    public void setManguoidung(int manguoidung) {
        this.manguoidung = manguoidung;
    }
    
}
