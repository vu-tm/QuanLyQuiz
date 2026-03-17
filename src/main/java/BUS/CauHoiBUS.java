package BUS;

import DAO.CauHoiDAO;
import DTO.CauHoiDTO;

import java.util.List;

public class CauHoiBUS {
    private CauHoiDAO dao;

    public CauHoiBUS() {
        dao = new CauHoiDAO();
    }

    public List<CauHoiDTO> load() {
        return dao.getAll();
    }

    public boolean add(CauHoiDTO ch) {
        return dao.insert(ch);
    }

    public boolean edit(CauHoiDTO ch) {
        return dao.update(ch);
    }

    public boolean remove(int macauhoi) {
        return dao.delete(macauhoi);
    }

    public List<CauHoiDTO> search(String keyword) {
        return dao.search(keyword);
    }

    public CauHoiDTO getById(int macauhoi) {
        return dao.getById(macauhoi);
    }
}