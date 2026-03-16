package DTO;

public class QuyenDTO {
    private int maquyen;
    private String chucnang;
    private String tendoituong;
    private String hanhdong;

    public QuyenDTO() {}

    public QuyenDTO(int maquyen, String chucnang, String tendoituong, String hanhdong) {
        this.maquyen = maquyen;
        this.chucnang = chucnang;
        this.tendoituong = tendoituong;
        this.hanhdong = hanhdong;
    }

    // getters & setters
    public int getMaquyen() { return maquyen; }
    public void setMaquyen(int maquyen) { this.maquyen = maquyen; }
    public String getChucnang() { return chucnang; }
    public void setChucnang(String chucnang) { this.chucnang = chucnang; }
    public String getTendoituong() { return tendoituong; }
    public void setTendoituong(String tendoituong) { this.tendoituong = tendoituong; }
    public String getHanhdong() { return hanhdong; }
    public void setHanhdong(String hanhdong) { this.hanhdong = hanhdong; }
}