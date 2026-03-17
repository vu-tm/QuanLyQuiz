package BUS;

import DAO.MonHocDAO;
import DTO.MonHocDTO;
import java.util.ArrayList;

public class MonHocBUS {

    private final MonHocDAO mhDAO = MonHocDAO.getInstance();
    private ArrayList<MonHocDTO> listMonHoc;

    public MonHocBUS() {
        this.listMonHoc = mhDAO.selectAll();
    }

    public ArrayList<MonHocDTO> getAll() {
        this.listMonHoc = mhDAO.selectAll();
        return this.listMonHoc;
    }

    public boolean add(MonHocDTO mh) {
        boolean check = mhDAO.insert(mh) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(MonHocDTO mh) {
        boolean check = mhDAO.update(mh) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean delete(int mamonhoc) {
        boolean check = mhDAO.delete(mamonhoc) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public String getTenById(int mamonhoc) {
        for (int i = 0; i < listMonHoc.size(); i++) {
            if (listMonHoc.get(i).getMamonhoc() == mamonhoc) {
                return listMonHoc.get(i).getTenmonhoc();
            }
        }
        return "Không xác định";
    }

    public MonHocDTO getById(int mamonhoc) {
        for (int i = 0; i < listMonHoc.size(); i++) {
            if (listMonHoc.get(i).getMamonhoc() == mamonhoc) {
                return listMonHoc.get(i);
            }
        }
        return null;
    }

    public ArrayList<MonHocDTO> search(String text, String type) {
        ArrayList<MonHocDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (int i = 0; i < this.listMonHoc.size(); i++) {
            MonHocDTO mh = listMonHoc.get(i);
            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = Integer.toString(mh.getMamonhoc()).contains(text) || mh.getTenmonhoc().toLowerCase().contains(text);
                    break;
                case "Mã môn học":
                    match = Integer.toString(mh.getMamonhoc()).contains(text);
                    break;
                case "Tên môn học":
                    match = mh.getTenmonhoc().toLowerCase().contains(text);
                    break;
            }
            if (match) {
                result.add(mh);
            }
        }
        return result;
    }
}
