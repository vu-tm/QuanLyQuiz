package DTO.ThongKe;

public class ThongKeCauHoiDTO {
    private int stt;
    private int macauhoi;
    private String noidung;
    private int tonglan;
    private double tyleDung;
    private double tyleSai;

    public ThongKeCauHoiDTO() {}

    public ThongKeCauHoiDTO(int stt, int macauhoi, String noidung, int tonglan, double tyleDung, double tyleSai) {
        this.stt = stt;
        this.macauhoi = macauhoi;
        this.noidung = noidung;
        this.tonglan = tonglan;
        this.tyleDung = tyleDung;
        this.tyleSai = tyleSai;
    }

    public int getStt() { return stt; }
    public int getMacauhoi() { return macauhoi; }
    public String getNoidung() { return noidung; }
    public int getTonglan() { return tonglan; }
    public double getTyleDung() { return tyleDung; }
    public double getTyleSai() { return tyleSai; }

    public void setStt(int stt) { this.stt = stt; }
    public void setMacauhoi(int macauhoi) { this.macauhoi = macauhoi; }
    public void setNoidung(String noidung) { this.noidung = noidung; }
    public void setTonglan(int tonglan) { this.tonglan = tonglan; }
    public void setTyleDung(double tyleDung) { this.tyleDung = tyleDung; }
    public void setTyleSai(double tyleSai) { this.tyleSai = tyleSai; }
}