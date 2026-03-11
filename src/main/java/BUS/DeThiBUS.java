package BUS;

import DAO.DeThiDAO;
import DTO.DeThiDTO;
import java.util.ArrayList;

public class DeThiBUS {
    private final DeThiDAO dethiDAO = DeThiDAO.getInstance();
    private ArrayList<DeThiDTO> listDeThi;

    public DeThiBUS() {
        this.listDeThi = dethiDAO.selectAll();
    }

    public ArrayList<DeThiDTO> getAll() {
        return dethiDAO.selectAll();
    }
}