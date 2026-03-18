package BUS;

import DAO.NhomQuyenDAO;
import DTO.NhomQuyenDTO;
import java.util.List;

public class NhomQuyenBUS {
    private NhomQuyenDAO nhomQuyenDao = new NhomQuyenDAO();

    public List<NhomQuyenDTO> getAll() {
        return nhomQuyenDao.getAll();
    }
    
    public boolean delete(int manhomquyen) {
        if (manhomquyen <= 0) return false;
        return nhomQuyenDao.delete(manhomquyen);
    }
    
    public NhomQuyenDTO getById(int manhomquyen) {
        return nhomQuyenDao.getById(manhomquyen);
    }

    public List<NhomQuyenDTO> search(String keyword) {
        return nhomQuyenDao.search(keyword);
    }

    public int insert(NhomQuyenDTO nq) {
        return nhomQuyenDao.insert(nq);
    }

    public boolean update(NhomQuyenDTO nq) {
        return nhomQuyenDao.update(nq);
    }

    public boolean insertChiTietQuyen(int manhomquyen, List<Integer> dsQuyen) {
        return nhomQuyenDao.insertChiTietQuyen(manhomquyen, dsQuyen);
    }

    public boolean deleteChiTietQuyen(int manhomquyen) {
        return nhomQuyenDao.deleteChiTietQuyen(manhomquyen);
    }

    public List<Integer> getQuyenByNhom(int manhomquyen) {
        return nhomQuyenDao.getQuyenIdsByNhom(manhomquyen);
    }
    public boolean checkExistId(int manhomquyen) {
        return nhomQuyenDao.checkExistId(manhomquyen);
    }
    public int getNextId() {
        return nhomQuyenDao.getNextId();
    }
}