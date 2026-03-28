package BUS;

import DAO.CauHoiDAO;
import DAO.DoKhoDAO;
import DTO.CauHoiDTO;
import DTO.DoKhoDTO;
import java.util.ArrayList;

public class DoKhoBUS {

    private final DoKhoDAO dkDAO = DoKhoDAO.getInstance();
    private ArrayList<DoKhoDTO> listDoKho;

    public DoKhoBUS() {
        this.listDoKho = dkDAO.selectAll();
    }

    public ArrayList<DoKhoDTO> getAll() {
        this.listDoKho = dkDAO.selectAll();
        return this.listDoKho;
    }

    public boolean add(DoKhoDTO dk) {
        if (dkDAO.checkTrungTen(dk.getTendokho(), -1)) {
            return false;
        }
        boolean check = dkDAO.insert(dk) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(DoKhoDTO dk) {
        if (dkDAO.checkTrungTen(dk.getTendokho(), dk.getMadokho())) {
            return false;
        }
        boolean check = dkDAO.update(dk) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean delete(int madokho) {
        boolean check = dkDAO.delete(madokho) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public ArrayList<DoKhoDTO> search(String text, String type) {
        ArrayList<DoKhoDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (int i = 0; i < this.listDoKho.size(); i++) {
            DoKhoDTO dk = listDoKho.get(i);
            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = Integer.toString(dk.getMadokho()).contains(text) || dk.getTendokho().toLowerCase().contains(text);
                    break;
                case "Mã độ khó":
                    match = Integer.toString(dk.getMadokho()).contains(text);
                    break;
                case "Tên độ khó":
                    match = dk.getTendokho().toLowerCase().contains(text);
                    break;
            }
            if (match) {
                result.add(dk);
            }
        }
        return result;
    }

    public String getTenDoKho(int madokho) {
        for (DoKhoDTO dk : listDoKho) {
            if (dk.getMadokho() == madokho) {
                return dk.getTendokho();
            }
        }
        DoKhoDTO dk = dkDAO.selectById(madokho);
        if (dk != null) {
            return dk.getTendokho();
        }
        return "Không xác định";
    }

    public DoKhoDTO getById(int madokho) {
        for (DoKhoDTO dk : listDoKho) {
            if (dk.getMadokho() == madokho) {
                return dk;
            }
        }
        return dkDAO.selectById(madokho);
    }
}
