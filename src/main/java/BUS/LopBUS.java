package BUS;

import DAO.ChiTietLopDAO;
import DAO.LopDAO;
import DTO.ChiTietLopDTO;
import DTO.LopDTO;
import java.util.ArrayList;

public class LopBUS {

    private final LopDAO lopDAO = LopDAO.getInstance();
    private final ChiTietLopDAO chitietlopDAO = ChiTietLopDAO.getInstance();
    private ArrayList<LopDTO> listLop;

    public LopBUS() {
        this.listLop = lopDAO.selectAll();
    }

    public ArrayList<LopDTO> getAll() {
        this.listLop = lopDAO.selectAll();
        return this.listLop;
    }

    public LopDTO getById(int malop) {
        for (LopDTO lop : listLop) {
            if (lop.getMalop() == malop) {
                return lop;
            }
        }
        return null;
    }

    public boolean add(LopDTO lop) {
        boolean check = lopDAO.insert(lop) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(LopDTO lop) {
        boolean check = lopDAO.update(lop) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean delete(int malop) {
        boolean check = lopDAO.delete(malop) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public ArrayList<LopDTO> getByGiangVien(int magiangvien) {
        return lopDAO.selectByGiangVien(magiangvien);
    }

    public ArrayList<LopDTO> getByMonHoc(int mamonhoc) {
        return lopDAO.selectByMonHoc(mamonhoc);
    }

    public ArrayList<LopDTO> search(ArrayList<LopDTO> sourceList, String text, String type) {
        ArrayList<LopDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (LopDTO lop : sourceList) {
            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = Integer.toString(lop.getMalop()).contains(text)
                            || lop.getTenlop().toLowerCase().contains(text)
                            || Integer.toString(lop.getNamhoc()).contains(text)
                            || Integer.toString(lop.getHocky()).contains(text);
                    break;
                case "Mã lớp":
                    match = Integer.toString(lop.getMalop()).contains(text);
                    break;
                case "Tên lớp":
                    match = lop.getTenlop().toLowerCase().contains(text);
                    break;
                case "Năm học":
                    match = Integer.toString(lop.getNamhoc()).contains(text);
                    break;
                case "Học kỳ":
                    match = Integer.toString(lop.getHocky()).contains(text);
                    break;
            }
            if (match) {
                result.add(lop);
            }
        }
        return result;
    }

    // CHI TIẾT LỚP
    public ArrayList<ChiTietLopDTO> getChiTietByMaLop(int malop) {
        return chitietlopDAO.selectByMaLop(malop);
    }

    public boolean addChiTiet(ChiTietLopDTO ct) {
        if (chitietlopDAO.checkExists(ct.getMalop(), ct.getManguoidung())) {
            return chitietlopDAO.restore(ct.getMalop(), ct.getManguoidung()) > 0;
        }
        return chitietlopDAO.insert(ct) > 0;
    }

    public boolean restoreChiTiet(int malop, int manguoidung) {
        return chitietlopDAO.restore(malop, manguoidung) > 0;
    }

    public boolean deleteChiTiet(int malop, int manguoidung) {
        return chitietlopDAO.delete(malop, manguoidung) > 0;
    }

    public boolean checkExistsChiTiet(int malop, int manguoidung) {
        return chitietlopDAO.checkExists(malop, manguoidung);
    }

    public int countSiSoByMaLop(int malop) {
        return chitietlopDAO.countByMaLop(malop);
    }

    public ArrayList<Integer> getListMaLopByUser(int manguoidung) {
        ArrayList<Integer> dsMaLop = new ArrayList<>();
        ArrayList<ChiTietLopDTO> listCT = chitietlopDAO.selectByUser(manguoidung);
        for (ChiTietLopDTO ct : listCT) {
            dsMaLop.add(ct.getMalop());
        }
        return dsMaLop;
    }
}
