package BUS;

import DAO.DapAnDAO;
import DTO.DapAnDTO;
import java.util.ArrayList;

public class DapAnBUS {
    private final DapAnDAO dao = DapAnDAO.getInstance();

    public ArrayList<DapAnDTO> getByMaCauHoi(int macauhoi) {
        return dao.selectByMaCauHoi(macauhoi);
    }
}
