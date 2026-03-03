/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class ChiTietDeThiDTO {
    private int made;
    private int macauhoi;
    private int thutu;

    public ChiTietDeThiDTO() {
    }

    public ChiTietDeThiDTO(int made, int macauhoi, int thutu) {
        this.made = made;
        this.macauhoi = macauhoi;
        this.thutu = thutu;
    }   
        
    // Getters, Setters
    public int getMade() {
        return made;
    }

    public void setMade(int made) {
        this.made = made;
    }

    public int getMacauhoi() {
        return macauhoi;
    }

    public void setMacauhoi(int macauhoi) {
        this.macauhoi = macauhoi;
    }

    public int getThutu() {
        return thutu;
    }

    public void setThutu(int thutu) {
        this.thutu = thutu;
    }
    
}
