package DTO;

import java.sql.Date;

/**
 *
 * @author Windows
 */
public class NguoiDungDTO {
    private int id;
    private String username;
    private String hoten;
    private boolean gioitinh;
    private Date ngaysinh;
    private String matkhau;
    private int trangthai;
    private int manhomquyen;

    public NguoiDungDTO() {
    }

    public NguoiDungDTO(int id, String username, String hoten, boolean gioitinh, Date ngaysinh, String matkhau, int trangthai, int manhomquyen) {
        this.id = id;
        this.username = username;
        this.hoten = hoten;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        this.matkhau = matkhau;
        this.trangthai = trangthai;
        this.manhomquyen = manhomquyen;
    }
        
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public boolean isGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(boolean gioitinh) {
        this.gioitinh = gioitinh;
    }

    public Date getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public int getManhomquyen() {
        return manhomquyen;
    }

    public void setManhomquyen(int manhomquyen) {
        this.manhomquyen = manhomquyen;
    }
    
}
