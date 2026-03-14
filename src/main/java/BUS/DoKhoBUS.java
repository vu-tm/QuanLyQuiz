package BUS;

import DAO.DoKhoDAO;
import DTO.DoKhoDTO;
import java.util.ArrayList;

public class DoKhoBUS {
    private final DoKhoDAO dkDAO = new DoKhoDAO();
    public ArrayList<DoKhoDTO> listDoKho = new ArrayList<>();

    public DoKhoBUS() {
        listDoKho = dkDAO.selectAll();
    }

    public ArrayList<DoKhoDTO> getAll() {
        this.listDoKho = dkDAO.selectAll();
        return this.listDoKho;
    }

    public Boolean add(DoKhoDTO dk) {
        boolean check = dkDAO.insert(dk) != 0;
        if (check) {
            this.listDoKho.add(dk);
        }
        return check;
    }

    public Boolean delete(int madokho) {
        boolean check = dkDAO.delete(madokho) != 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public Boolean update(DoKhoDTO dk) {
        boolean check = dkDAO.update(dk) != 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public ArrayList<DoKhoDTO> search(String text, String type) {
        ArrayList<DoKhoDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (DoKhoDTO i : this.listDoKho) {
            if (type.equals("Tất cả")) {
                if (Integer.toString(i.getMadokho()).contains(text) || i.getTendokho().toLowerCase().contains(text)) {
                    result.add(i);
                }
            } else if (type.equals("Mã độ khó")) {
                if (Integer.toString(i.getMadokho()).contains(text)) {
                    result.add(i);
                }
            } else if (type.equals("Tên độ khó")) {
                if (i.getTendokho().toLowerCase().contains(text)) {
                    result.add(i);
                }
            }
        }
        return result;
    }
}