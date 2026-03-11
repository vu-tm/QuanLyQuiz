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
    private int tongcaudung;
    private int tongcausai;
    private int trangthai;

    public MonHocDTO() {
    }

    public MonHocDTO(int mamonhoc, String tenmonhoc, int sotinchi, int tongcaudung, int tongcausai, int trangthai) {
        this.mamonhoc = mamonhoc;
        this.tenmonhoc = tenmonhoc;
        this.sotinchi = sotinchi;
        this.tongcaudung = tongcaudung;
        this.tongcausai = tongcausai;
        this.trangthai = trangthai;
    }

    // Getters and Setters
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

    public int getTongcaudung() {
        return tongcaudung;
    }

    public void setTongcaudung(int tongcaudung) {
        this.tongcaudung = tongcaudung;
    }

    public int getTongcausai() {
        return tongcausai;
    }

    public void setTongcausai(int tongcausai) {
        this.tongcausai = tongcausai;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }
}
