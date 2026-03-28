/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class DapAnDTO {
    private int madapan;
    private int macauhoi;
    private String noidungtl;
    private boolean ladapan;

    public DapAnDTO() {
    }

    public DapAnDTO(int madapan, int macauhoi, String noidungtl, boolean ladapan) {
        this.madapan = madapan;
        this.macauhoi = macauhoi;
        this.noidungtl = noidungtl;
        this.ladapan = ladapan;
    }
      
    // Getters and Setters
    public int getMadapan() {
        return madapan;
    }

    public void setMadapan(int madapan) {
        this.madapan = madapan;
    }

    public int getMacauhoi() {
        return macauhoi;
    }

    public void setMacauhoi(int macauhoi) {
        this.macauhoi = macauhoi;
    }

    public String getNoidungtl() {
        return noidungtl;
    }

    public void setNoidungtl(String noidungtl) {
        this.noidungtl = noidungtl;
    }

    public boolean getLadapan() {
        return ladapan;
    }

    public void setLadapan(boolean ladapan) {
        this.ladapan = ladapan;
    }
}
