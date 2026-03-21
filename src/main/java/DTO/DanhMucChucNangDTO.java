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

    private String machucnang;
    private String tenchucnang;

    public DanhMucChucNangDTO() {
    }

    public DanhMucChucNangDTO(String machucnang, String tenchucnang) {
        this.machucnang = machucnang;
        this.tenchucnang = tenchucnang;
    }

    public String getMachucnang() {
        return machucnang;
    }

    public void setMachucnang(String machucnang) {
        this.machucnang = machucnang;
    }

    public String getTenchucnang() {
        return tenchucnang;
    }

    public void setTenchucnang(String tenchucnang) {
        this.tenchucnang = tenchucnang;
    }
}
