package BUS;

import DAO.ChiTietDeThiDAO;
import DAO.DeThiDAO;
import DTO.ChiTietDeThiDTO;
import DTO.DeThiDTO;
import java.util.ArrayList;

public class DeThiBUS {

    private final DeThiDAO dethiDAO = DeThiDAO.getInstance();
    private final ChiTietDeThiDAO ctdtDAO = ChiTietDeThiDAO.getInstance();
    private ArrayList<DeThiDTO> listDeThi;

    public DeThiBUS() {
        this.listDeThi = dethiDAO.selectAll();
    }

    public ArrayList<DeThiDTO> getAll() {
        this.listDeThi = dethiDAO.selectAll();
        return this.listDeThi;
    }

    public boolean add(DeThiDTO dt) {
        boolean check = dethiDAO.insert(dt) > 0;
        if (check) getAll();
        return check;
    }

    public boolean update(DeThiDTO dt) {
        boolean check = dethiDAO.update(dt) > 0;
        if (check) getAll();
        return check;
    }

    public boolean delete(int made) {
        ctdtDAO.deleteByMade(made);
        boolean check = dethiDAO.delete(made) > 0;
        if (check) getAll();
        return check;
    }

    public ArrayList<DeThiDTO> search(String text, String type) {
        ArrayList<DeThiDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (int i = 0; i < this.listDeThi.size(); i++) {
            DeThiDTO dt = listDeThi.get(i);
            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = Integer.toString(dt.getMade()).contains(text)
                            || dt.getTende().toLowerCase().contains(text)
                            || dt.getNguoitao().toLowerCase().contains(text);
                    break;
                case "Mã đề":
                    match = Integer.toString(dt.getMade()).contains(text);
                    break;
                case "Tên đề":
                    match = dt.getTende().toLowerCase().contains(text);
                    break;
                case "Người tạo":
                    match = dt.getNguoitao().toLowerCase().contains(text);
                    break;
            }
            if (match) {
                result.add(dt);
            }
        }
        return result;
    }

    public boolean saveChiTiet(int made, ArrayList<Integer> listMaCauHoi) {
        ctdtDAO.deleteByMade(made);
        for (int i = 0; i < listMaCauHoi.size(); i++) {
            int macauhoi = listMaCauHoi.get(i);
            ChiTietDeThiDTO ct = new ChiTietDeThiDTO(made, macauhoi, i + 1);
            ctdtDAO.insert(ct);
        }
        return true;
    }

    public ArrayList<Integer> getMaCauHoiByMade(int made) {
        ArrayList<ChiTietDeThiDTO> list = ctdtDAO.selectByMade(made);
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            result.add(list.get(i).getMacauhoi());
        }
        return result;
    }
}