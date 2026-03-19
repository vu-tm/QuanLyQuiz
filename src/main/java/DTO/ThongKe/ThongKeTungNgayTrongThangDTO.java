package DTO.ThongKe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ThongKeTungNgayTrongThangDTO {
    private Date ngay;
    private double diemCaoNhat;
    private double diemThapNhat;
    private double diemTrungBinh;

    public ThongKeTungNgayTrongThangDTO() {}

    public ThongKeTungNgayTrongThangDTO(Date ngay, double diemCaoNhat, double diemThapNhat, double diemTrungBinh) {
        this.ngay = ngay;
        this.diemCaoNhat = diemCaoNhat;
        this.diemThapNhat = diemThapNhat;
        this.diemTrungBinh = diemTrungBinh;
    }

    public String getNgay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(ngay);
    }

    public Date getNgayDate() { return ngay; }
    public void setNgay(Date ngay) { this.ngay = ngay; }

    public double getDiemCaoNhat() { return diemCaoNhat; }
    public void setDiemCaoNhat(double diemCaoNhat) { this.diemCaoNhat = diemCaoNhat; }

    public double getDiemThapNhat() { return diemThapNhat; }
    public void setDiemThapNhat(double diemThapNhat) { this.diemThapNhat = diemThapNhat; }

    public double getDiemTrungBinh() { return diemTrungBinh; }
    public void setDiemTrungBinh(double diemTrungBinh) { this.diemTrungBinh = diemTrungBinh; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ThongKeTungNgayTrongThangDTO other = (ThongKeTungNgayTrongThangDTO) obj;
        return Objects.equals(this.ngay, other.ngay);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ngay);
    }
}