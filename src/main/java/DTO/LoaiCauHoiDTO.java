/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class LoaiCauHoiDTO {
    private int maloai;
    private String tenloai;
    private int trangthai;

    public LoaiCauHoiDTO() {
    }

    public LoaiCauHoiDTO(int maloai, String tenloai, int trangthai) {
        this.maloai = maloai;
        this.tenloai = tenloai;
        this.trangthai = trangthai;
    }
           
    // Getters, Setters
    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }
    
}
