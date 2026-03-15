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
        if (check) getAll();
        return check;
    }

    public boolean update(KyThiDTO kt) {
        boolean check = kythiDAO.update(kt) > 0;
        if (check) getAll();
        return check;
    }

    public boolean delete(int id) {
        boolean check = kythiDAO.delete(id) > 0;
        if (check) getAll();
        return check;
    }

    public ArrayList<KyThiDTO> search(String text, String type) {
        ArrayList<KyThiDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (int i = 0; i < this.listKyThi.size(); i++) {
            KyThiDTO kt = listKyThi.get(i);
            String maStr = Integer.toString(kt.getMakythi());
            String tenStr = kt.getTenkythi().toLowerCase();
            boolean match = false;
            
            switch (type) {
                case "Tất cả":
                    match = maStr.contains(text) || tenStr.contains(text);
                    break;
                case "Mã kỳ thi":
                    match = maStr.contains(text);
                    break;
                case "Tên kỳ thi":
                    match = tenStr.contains(text);
                    break;
            }
            if (match) {
                result.add(kt);
            }
        }
        return result;
    }
}