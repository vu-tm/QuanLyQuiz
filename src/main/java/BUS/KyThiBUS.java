package BUS;

import DAO.KyThiDAO;
import DTO.KyThiDTO;
import java.util.ArrayList;

public class KyThiBUS {

    private final KyThiDAO kythiDAO = KyThiDAO.getInstance();
    private ArrayList<KyThiDTO> listKyThi;

    public KyThiBUS() {
        this.listKyThi = kythiDAO.selectAll();
    }

    public ArrayList<KyThiDTO> getAll() {
        this.listKyThi = kythiDAO.selectAll();
        return this.listKyThi;
    }

    public boolean add(KyThiDTO kt) {
        boolean check = kythiDAO.insert(kt) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(KyThiDTO kt) {
        boolean check = kythiDAO.update(kt) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean delete(int id) {
        boolean check = kythiDAO.delete(id) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public String getTenById(int makythi) {
        for (int i = 0; i < listKyThi.size(); i++) {
            if (listKyThi.get(i).getMakythi() == makythi) {
                return listKyThi.get(i).getTenkythi();
            }
        }
        return "Không xác định";
    }

    public KyThiDTO getById(int makythi) {
        for (int i = 0; i < listKyThi.size(); i++) {
            if (listKyThi.get(i).getMakythi() == makythi) {
                return listKyThi.get(i);
            }
        }
        return null;
    }

    public ArrayList<KyThiDTO> search(String text, String type) {
        ArrayList<KyThiDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (int i = 0; i < this.listKyThi.size(); i++) {
            KyThiDTO kt = listKyThi.get(i);
            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = Integer.toString(kt.getMakythi()).contains(text) || kt.getTenkythi().toLowerCase().contains(text);
                    break;
                case "Mã kỳ thi":
                    match = Integer.toString(kt.getMakythi()).contains(text);
                    break;
                case "Tên kỳ thi":
                    match = kt.getTenkythi().toLowerCase().contains(text);
                    break;
            }
            if (match) {
                result.add(kt);
            }
        }
        return result;
    }
}
