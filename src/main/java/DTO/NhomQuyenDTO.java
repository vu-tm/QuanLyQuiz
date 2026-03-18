package DTO;

public class NhomQuyenDTO {
    private int manhomquyen;
    private String tennhomquyen;
    private int trangthai;

    public NhomQuyenDTO() {}

    public NhomQuyenDTO(int manhomquyen, String tennhomquyen, int trangthai) {
        this.manhomquyen = manhomquyen;
        this.tennhomquyen = tennhomquyen;
        this.trangthai = trangthai;
    }
            
    // Getters, Setters
    public int getManhomquyen() {return manhomquyen;}
    public void setManhomquyen(int manhomquyen) {this.manhomquyen = manhomquyen;}

    public String getTennhomquyen() {return tennhomquyen;}
    public void setTennhomquyen(String tennhomquyen) {this.tennhomquyen = tennhomquyen;}

    public int getTrangthai() {return trangthai;}
    public void setTrangthai(int trangthai) {this.trangthai = trangthai;}
    
}