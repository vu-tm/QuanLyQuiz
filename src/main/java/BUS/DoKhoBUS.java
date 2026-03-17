package BUS;

import DAO.DoKhoDAO;
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
        boolean check = dkDAO.insert(dk) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(DoKhoDTO dk) {
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
        for (int i = 0; i < listDoKho.size(); i++) {
            if (listDoKho.get(i).getMadokho() == madokho) {
                return listDoKho.get(i).getTendokho();
            }
        }
        return "Không xác định";
    }

    public DoKhoDTO getById(int madokho) {
        for (int i = 0; i < listDoKho.size(); i++) {
            if (listDoKho.get(i).getMadokho() == madokho) {
                return listDoKho.get(i);
            }
        }
        return null;
    }
}
