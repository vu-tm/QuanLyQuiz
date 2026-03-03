/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class DoKhoDTO {
    private int madokho;
    private String tendokho;
    private int trangthai;

    public DoKhoDTO() {
    }

    public DoKhoDTO(int madokho, String tendokho, int trangthai) {
        this.madokho = madokho;
        this.tendokho = tendokho;
        this.trangthai = trangthai;
    }
            
    // Getters, Setters
    public int getMadokho() {
        return madokho;
    }

    public void setMadokho(int madokho) {
        this.madokho = madokho;
    }

    public String getTendokho() {
        return tendokho;
    }

    public void setTendokho(String tendokho) {
        this.tendokho = tendokho;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }
    
}