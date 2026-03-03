/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author Windows
 */
public class DanhMucChucNangDTO {
    private String chucnang;
    private String tenchucnang;

    public DanhMucChucNangDTO() {
    }

    public DanhMucChucNangDTO(String chucnang, String tenchucnang) {
        this.chucnang = chucnang;
        this.tenchucnang = tenchucnang;
    }   
        
    // Getters, Setters
    public String getChucnang() {
        return chucnang;
    }

    public void setChucnang(String chucnang) {
        this.chucnang = chucnang;
    }

    public String getTenchucnang() {
        return tenchucnang;
    }

    public void setTenchucnang(String tenchucnang) {
        this.tenchucnang = tenchucnang;
    }
    
}