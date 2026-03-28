package BUS;

import DAO.CauHoiDAO;
import DAO.LoaiCauHoiDAO;
import DTO.LoaiCauHoiDTO;
import java.util.ArrayList;

public class LoaiCauHoiBUS {

    private final LoaiCauHoiDAO loaiDAO = LoaiCauHoiDAO.getInstance();
    private ArrayList<LoaiCauHoiDTO> listLoai;

    public LoaiCauHoiBUS() {
        this.listLoai = loaiDAO.selectAll();
    }

    public ArrayList<LoaiCauHoiDTO> getAll() {
        this.listLoai = loaiDAO.selectAll();
        return this.listLoai;
    }

    public boolean delete(int maloai) {
        boolean check = loaiDAO.delete(maloai) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public LoaiCauHoiDTO getById(int maloai) {
        for (LoaiCauHoiDTO lch : listLoai) {
            if (lch.getMaloai() == maloai) {
                return lch;
            }
        }
        return loaiDAO.selectById(maloai);
    }

    public String getTenById(int maloai) {
        for (LoaiCauHoiDTO lch : listLoai) {
            if (lch.getMaloai() == maloai) {
                return lch.getTenloai();
            }
        }
        LoaiCauHoiDTO lch = loaiDAO.selectById(maloai);
        if (lch != null) {
            return lch.getTenloai();
        }
        return "Không xác định";
    }

    public ArrayList<LoaiCauHoiDTO> search(String text, String type) {
        ArrayList<LoaiCauHoiDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (LoaiCauHoiDTO lch : this.listLoai) {
            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = Integer.toString(lch.getMaloai()).contains(text) || lch.getTenloai().toLowerCase().contains(text);
                    break;
                case "Mã loại":
                    match = Integer.toString(lch.getMaloai()).contains(text);
                    break;
                case "Tên loại":
                    match = lch.getTenloai().toLowerCase().contains(text);
                    break;
            }
            if (match) {
                result.add(lch);
            }
        }
        return result;
    }
}
