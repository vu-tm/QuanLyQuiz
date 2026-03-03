/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class ChiTietQuyenDTO {
    private int manhomquyen;
    private String chucnang;
    private String hanhdong;

    public ChiTietQuyenDTO() {
    }

    public ChiTietQuyenDTO(int manhomquyen, String chucnang, String hanhdong) {
        this.manhomquyen = manhomquyen;
        this.chucnang = chucnang;
        this.hanhdong = hanhdong;
    }
            
    // Getters, Setters
    public int getManhomquyen() {
        return manhomquyen;
    }

    public void setManhomquyen(int manhomquyen) {
        this.manhomquyen = manhomquyen;
    }

    public String getChucnang() {
        return chucnang;
    }

    public void setChucnang(String chucnang) {
        this.chucnang = chucnang;
    }

    public String getHanhdong() {
        return hanhdong;
    }

    public void setHanhdong(String hanhdong) {
        this.hanhdong = hanhdong;
    }
    
}
