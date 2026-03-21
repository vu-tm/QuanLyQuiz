package BUS;

import DAO.PhanCongDAO;
import DTO.PhanCongDTO;
import java.util.ArrayList;

public class PhanCongBUS {

    private final PhanCongDAO pcDAO = PhanCongDAO.getInstance();
    private ArrayList<PhanCongDTO> listPhanCong;

    public PhanCongBUS() {
        this.listPhanCong = pcDAO.selectAll();
    }

    public ArrayList<PhanCongDTO> getAll() {
        this.listPhanCong = pcDAO.selectAll();
        return this.listPhanCong;
    }

    public boolean add(PhanCongDTO pc) {
        boolean check = pcDAO.insert(pc) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(PhanCongDTO old, PhanCongDTO pc) {
        boolean check = pcDAO.update(old, pc) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean delete(int mamonhoc, int manguoidung) {
        boolean check = pcDAO.delete(mamonhoc, manguoidung) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public ArrayList<PhanCongDTO> search(String text, String type, NguoiDungBUS ndBUS, MonHocBUS mhBUS) {
        ArrayList<PhanCongDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (PhanCongDTO pc : this.listPhanCong) {
            String tenND = ndBUS.getById(pc.getManguoidung()).getHoten().toLowerCase();
            String tenMH = mhBUS.getTenById(pc.getMamonhoc()).toLowerCase();
            String maNDStr = Integer.toString(pc.getManguoidung());

            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = maNDStr.contains(text) || tenND.contains(text) || tenMH.contains(text);
                    break;
                case "Mã giảng viên":
                    match = maNDStr.contains(text);
                    break;
                case "Họ tên":
                    match = tenND.contains(text);
                    break;
                case "Môn học":
                    match = tenMH.contains(text);
                    break;
            }
            if (match) {
                result.add(pc);
            }
        }
        return result;
    }
}