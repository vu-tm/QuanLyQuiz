package BUS;

import DAO.QuyenDAO;
import DTO.QuyenDTO;
import java.util.List;

public class QuyenBUS {
    private QuyenDAO quyenDao = new QuyenDAO();
    public List<QuyenDTO> getAll() {
        return quyenDao.getAll();
    }
}