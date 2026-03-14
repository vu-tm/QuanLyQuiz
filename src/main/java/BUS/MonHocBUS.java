package BUS;

import DAO.MonHocDAO;
import DTO.MonHocDTO;
import java.util.ArrayList;

public class MonHocBUS {
    private final MonHocDAO mhDAO = MonHocDAO.getInstance();
    private ArrayList<MonHocDTO> listMonHoc = new ArrayList<>();

    public MonHocBUS() {
        this.listMonHoc = mhDAO.selectAll();
    }

    public ArrayList<MonHocDTO> getAll() {
        this.listMonHoc = mhDAO.selectAll();
        return this.listMonHoc;
    }

    public boolean add(MonHocDTO mh) {
        return mhDAO.insert(mh) != 0;
    }

    public boolean update(MonHocDTO mh) {
        return mhDAO.update(mh) != 0;
    }

    public boolean delete(int mamonhoc) {
        return mhDAO.delete(mamonhoc) != 0;
    }

    public ArrayList<MonHocDTO> search(String text, String type) {
        ArrayList<MonHocDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (int i = 0; i < this.listMonHoc.size(); i++) {
            MonHocDTO mh = this.listMonHoc.get(i);
            String maStr = Integer.toString(mh.getMamonhoc()).toLowerCase();
            String tenStr = mh.getTenmonhoc().toLowerCase();

            if (type.equals("Tất cả")) {
                if (maStr.contains(text) || tenStr.contains(text)) {
                    result.add(mh);
                }
            } else if (type.equals("Mã môn học")) {
                if (maStr.contains(text)) {
                    result.add(mh);
                }
            } else if (type.equals("Tên môn học")) {
                if (tenStr.contains(text)) {
                    result.add(mh);
                }
            }
        }
        return result;
    }
}