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
        // Check trùng tên trước khi thêm mới (excludeId = -1 vì chưa có ID)
        if (mhDAO.checkTrungTen(mh.getTenmonhoc(), -1)) {
            return false;
        }
        boolean check = mhDAO.insert(mh) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(MonHocDTO mh) {
        if (mhDAO.checkTrungTen(mh.getTenmonhoc(), mh.getMamonhoc())) {
            return false;
        }
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

    public MonHocDTO getById(int mamonhoc) {
        for (MonHocDTO mh : listMonHoc) {
            if (mh.getMamonhoc() == mamonhoc) {
                return mh;
            }
        }
        return mhDAO.selectById(mamonhoc);
    }

    public String getTenById(int mamonhoc) {
        MonHocDTO mh = getById(mamonhoc);
        if (mh != null) {
            return mh.getTenmonhoc();
        }
        return "Không xác định";
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
