package BUS;

import DAO.DapAnDAO;
import DTO.DapAnDTO;
import java.util.ArrayList;

public class DapAnBUS {

    private final DapAnDAO dapAnDAO = DapAnDAO.getInstance();
    private ArrayList<DapAnDTO> listDapAn;

    public DapAnBUS() {
        this.listDapAn = dapAnDAO.selectAll();
    }

    public ArrayList<DapAnDTO> getAll() {
        this.listDapAn = dapAnDAO.selectAll();
        return this.listDapAn;
    }

    public ArrayList<DapAnDTO> getDapAnDeHienThi(int macauhoi) {
        return dapAnDAO.selectByMaCauHoiHienThi(macauhoi);
    }

    public ArrayList<DapAnDTO> getDapAnDayDu(int macauhoi) {
        return dapAnDAO.selectByMaCauHoiFull(macauhoi);
    }

    public boolean add(DapAnDTO da) {
        boolean check = dapAnDAO.insert(da) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(DapAnDTO da) {
        boolean check = dapAnDAO.update(da) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean delete(int madapan) {
        boolean check = dapAnDAO.delete(madapan) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean deleteByMaCauHoi(int macauhoi) {
        boolean check = dapAnDAO.deleteByMaCauHoi(macauhoi) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public String getNoidungById(int madapan) {
        for (int i = 0; i < listDapAn.size(); i++) {
            if (listDapAn.get(i).getMadapan() == madapan) {
                return listDapAn.get(i).getNoidungtl();
            }
        }
        return "";
    }

    public DapAnDTO getById(int madapan) {
        for (int i = 0; i < listDapAn.size(); i++) {
            if (listDapAn.get(i).getMadapan() == madapan) {
                return listDapAn.get(i);
            }
        }
        return null;
    }

    public ArrayList<DapAnDTO> getDapAnDungByCauHoi(int macauhoi) {
        ArrayList<DapAnDTO> result = new ArrayList<>();
        ArrayList<DapAnDTO> all = getDapAnDayDu(macauhoi); 
        for (DapAnDTO da : all) {
            if (da.getLadapan()) {
                result.add(da);
            }
        }
        return result;
    }
}
