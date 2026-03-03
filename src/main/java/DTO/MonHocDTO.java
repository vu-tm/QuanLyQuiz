/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class MonHocDTO {
    private int mamonhoc;
    private String tenmonhoc;
    private int sotinchi;
    private int sotietlythuyet;
    private int sotietthuchanh;
    private int trangthai;

    public MonHocDTO() {
    }

    public MonHocDTO(int mamonhoc, String tenmonhoc, int sotinchi, int sotietlythuyet, int sotietthuchanh, int trangthai) {
        this.mamonhoc = mamonhoc;
        this.tenmonhoc = tenmonhoc;
        this.sotinchi = sotinchi;
        this.sotietlythuyet = sotietlythuyet;
        this.sotietthuchanh = sotietthuchanh;
        this.trangthai = trangthai;
    }
            
    // Getters, Setters
    public int getMamonhoc() {
        return mamonhoc;
    }

    public void setMamonhoc(int mamonhoc) {
        this.mamonhoc = mamonhoc;
    }

    public String getTenmonhoc() {
        return tenmonhoc;
    }

    public void setTenmonhoc(String tenmonhoc) {
        this.tenmonhoc = tenmonhoc;
    }

    public int getSotinchi() {
        return sotinchi;
    }

    public void setSotinchi(int sotinchi) {
        this.sotinchi = sotinchi;
    }

    public int getSotietlythuyet() {
        return sotietlythuyet;
    }

    public void setSotietlythuyet(int sotietlythuyet) {
        this.sotietlythuyet = sotietlythuyet;
    }

    public int getSotietthuchanh() {
        return sotietthuchanh;
    }

    public void setSotietthuchanh(int sotietthuchanh) {
        this.sotietthuchanh = sotietthuchanh;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }
    
}