package DTO;

import java.util.ArrayList;
import java.util.Objects;

public class NhomQuyenDTO {

    private int manhomquyen;
    private String tennhomquyen;

    public NhomQuyenDTO() {
    }

    public NhomQuyenDTO(int manhomquyen, String tennhomquyen) {
        this.manhomquyen = manhomquyen;
        this.tennhomquyen = tennhomquyen;
    }

    public int getManhomquyen() {
        return manhomquyen;
    }

    public void setManhomquyen(int manhomquyen) {
        this.manhomquyen = manhomquyen;
    }

    public String getTennhomquyen() {
        return tennhomquyen;
    }

    public void setTennhomquyen(String tennhomquyen) {
        this.tennhomquyen = tennhomquyen;
    }
}
