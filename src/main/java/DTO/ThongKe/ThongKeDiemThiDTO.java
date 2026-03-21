package DTO.ThongKe;

public class ThongKeDiemThiDTO {
    private int thoigian; // nam, thang, ngay
    private double diemCaoNhat;
    private double diemThapNhat;
    private double diemTrungBinh;

    public ThongKeDiemThiDTO() {}

    public ThongKeDiemThiDTO(int thoigian, double diemCaoNhat, double diemThapNhat, double diemTrungBinh) {
        this.thoigian = thoigian;
        this.diemCaoNhat = diemCaoNhat;
        this.diemThapNhat = diemThapNhat;
        this.diemTrungBinh = diemTrungBinh;
    }

    public int getThoigian() { return thoigian; }
    public double getDiemCaoNhat() { return diemCaoNhat; }
    public double getDiemThapNhat() { return diemThapNhat; }
    public double getDiemTrungBinh() { return diemTrungBinh; }

    public void setThoigian(int thoigian) { this.thoigian = thoigian; }
    public void setDiemCaoNhat(double diemCaoNhat) { this.diemCaoNhat = diemCaoNhat; }
    public void setDiemThapNhat(double diemThapNhat) { this.diemThapNhat = diemThapNhat; }
    public void setDiemTrungBinh(double diemTrungBinh) { this.diemTrungBinh = diemTrungBinh; }
}