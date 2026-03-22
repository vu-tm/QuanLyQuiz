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
        return lopDAO.selectById(malop);
    }

    public boolean add(LopDTO lop) {
        return lopDAO.insert(lop) > 0;
    }

    public boolean update(LopDTO lop) {
        return lopDAO.update(lop) > 0;
    }

    public boolean delete(int malop) {
        return lopDAO.delete(malop) > 0;
    }

    public ArrayList<LopDTO> getByGiangVien(String magiangvien) {
        return lopDAO.selectByGiangVien(magiangvien);
    }

    public ArrayList<LopDTO> getByMonHoc(int mamonhoc) {
        return lopDAO.selectByMonHoc(mamonhoc);
    }

    public ArrayList<LopDTO> search(String text, String type) {
        ArrayList<LopDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (LopDTO lop : this.listLop) {
            String maStr     = Integer.toString(lop.getMalop()).toLowerCase();
            String tenStr    = lop.getTenlop().toLowerCase();
            String namhocStr = Integer.toString(lop.getNamhoc()).toLowerCase();
            String hockyStr  = Integer.toString(lop.getHocky()).toLowerCase();
            boolean match    = false;
            switch (type) {
                case "Tất cả":
                    match = maStr.contains(text) || tenStr.contains(text) || namhocStr.contains(text) || hockyStr.contains(text);
                    break;
                case "Mã lớp":
                    match = maStr.contains(text);
                    break;
                case "Tên lớp":
                    match = tenStr.contains(text);
                    break;
                case "Năm học":
                    match = namhocStr.contains(text);
                    break;
                case "Học kỳ":
                    match = hockyStr.contains(text);
                    break;
            }
            if (match) result.add(lop);
        }
        return result;
    }

    public ArrayList<ChiTietLopDTO> getChiTietByMaLop(int malop) {
        return chitietlopDAO.selectByMaLop(malop);
    }

    public ArrayList<ChiTietLopDTO> getChiTietByMaNguoiDung(String manguoidung) {
        return chitietlopDAO.selectByMaNguoiDung(manguoidung);
    }

    public boolean addChiTiet(ChiTietLopDTO ct) {
        if (chitietlopDAO.checkExists(ct.getMalop(), ct.getManguoidung())) {
            return false;
        }
        return chitietlopDAO.insert(ct) > 0;
    }

    public boolean restoreChiTiet(int malop, String manguoidung) {
        return chitietlopDAO.restore(malop, manguoidung) > 0;
    }

    public boolean deleteChiTiet(int malop, String manguoidung) {
        return chitietlopDAO.delete(malop, manguoidung) > 0;
    }

    public boolean checkExistsChiTiet(int malop, String manguoidung) {
        return chitietlopDAO.checkExists(malop, manguoidung);
    }

    public int countSiSoByMaLop(int malop) {
        return chitietlopDAO.countByMaLop(malop);
    }
}