package DTO.ThongKe;

public class ThongKeHocSinhDTO {
    private int stt;
    private String manguoidung;
    private String hoten;
    private int soDedalLam;

    public ThongKeHocSinhDTO() {}

    public ThongKeHocSinhDTO(int stt, String manguoidung, String hoten, int soDedalLam) {
        this.stt = stt;
        this.manguoidung = manguoidung;
        this.hoten = hoten;
        this.soDedalLam = soDedalLam;
    }

    public int getStt() { return stt; }
    public String getManguoidung() { return manguoidung; }
    public String getHoten() { return hoten; }
    public int getSoDedalLam() { return soDedalLam; }

    public void setStt(int stt) { this.stt = stt; }
    public void setManguoidung(String manguoidung) { this.manguoidung = manguoidung; }
    public void setHoten(String hoten) { this.hoten = hoten; }
    public void setSoDedalLam(int soDedalLam) { this.soDedalLam = soDedalLam; }
}