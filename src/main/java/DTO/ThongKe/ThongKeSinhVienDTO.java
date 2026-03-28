package DTO.ThongKe;

public class ThongKeSinhVienDTO {

    private int stt;
    private int manguoidung;
    private String hoten;
    private int soDedalLam;

    public ThongKeSinhVienDTO() {
    }

    public ThongKeSinhVienDTO(int stt, int manguoidung, String hoten, int soDedalLam) {
        this.stt = stt;
        this.manguoidung = manguoidung;
        this.hoten = hoten;
        this.soDedalLam = soDedalLam;
    }

    public int getStt() {
        return stt;
    }

    public int getManguoidung() {
        return manguoidung;
    }

    public String getHoten() {
        return hoten;
    }

    public int getSoDedalLam() {
        return soDedalLam;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public void setManguoidung(int manguoidung) {
        this.manguoidung = manguoidung;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public void setSoDedalLam(int soDedalLam) {
        this.soDedalLam = soDedalLam;
    }
}
