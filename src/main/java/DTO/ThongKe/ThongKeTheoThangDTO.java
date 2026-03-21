package DTO.ThongKe;

public class ThongKeTheoThangDTO {
    private int thang;
    private double diemCaoNhat;
    private double diemThapNhat;
    private double diemTrungBinh;

    public ThongKeTheoThangDTO() {}

    public ThongKeTheoThangDTO(int thang, double diemCaoNhat, double diemThapNhat, double diemTrungBinh) {
        this.thang = thang;
        this.diemCaoNhat = diemCaoNhat;
        this.diemThapNhat = diemThapNhat;
        this.diemTrungBinh = diemTrungBinh;
    }

    public int getThang() { return thang; }
    public double getDiemCaoNhat() { return diemCaoNhat; }
    public double getDiemThapNhat() { return diemThapNhat; }
    public double getDiemTrungBinh() { return diemTrungBinh; }

    public void setThang(int thang) { this.thang = thang; }
    public void setDiemCaoNhat(double diemCaoNhat) { this.diemCaoNhat = diemCaoNhat; }
    public void setDiemThapNhat(double diemThapNhat) { this.diemThapNhat = diemThapNhat; }
    public void setDiemTrungBinh(double diemTrungBinh) { this.diemTrungBinh = diemTrungBinh; }
}