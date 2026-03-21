package DTO;

public class ChiTietQuyenDTO {

    private int manhomquyen;
    private String machucnang;
    private String hanhdong;

    public ChiTietQuyenDTO() {
    }

    public ChiTietQuyenDTO(int manhomquyen, String machucnang, String hanhdong) {
        this.manhomquyen = manhomquyen;
        this.machucnang = machucnang;
        this.hanhdong = hanhdong;
    }

    public int getManhomquyen() {
        return manhomquyen;
    }

    public void setManhomquyen(int manhomquyen) {
        this.manhomquyen = manhomquyen;
    }

    public String getMachucnang() {
        return machucnang;
    }

    public void setMachucnang(String machucnang) {
        this.machucnang = machucnang;
    }

    public String getHanhdong() {
        return hanhdong;
    }

    public void setHanhdong(String hanhdong) {
        this.hanhdong = hanhdong;
    }
}
