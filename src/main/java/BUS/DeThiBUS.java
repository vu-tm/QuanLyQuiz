package BUS;

import DAO.DeThiDAO;
import DTO.DeThiDTO;
import java.util.ArrayList;

public class DeThiBUS {

    private final DeThiDAO dethiDAO = DeThiDAO.getInstance();
    private ArrayList<DeThiDTO> listDeThi;

    public DeThiBUS() {
        this.listDeThi = dethiDAO.selectAll();
    }

    public ArrayList<DeThiDTO> getAll() {
        return dethiDAO.selectAll();
    }

    public ArrayList<DeThiDTO> search(String text, String type) {
        ArrayList<DeThiDTO> result = new ArrayList<>();
        text = text.toLowerCase();

        for (int i = 0; i < this.listDeThi.size(); i++) {
            DeThiDTO dt = this.listDeThi.get(i);
            String madeStr = Integer.toString(dt.getMade()).toLowerCase();
            String tenStr = dt.getTende().toLowerCase();
            String nguoitaoStr = dt.getNguoitao().toLowerCase();

            if (type.equals("Tất cả")) {
                if (madeStr.contains(text) || tenStr.contains(text) || nguoitaoStr.contains(text)) {
                    result.add(dt);
                }
            } else if (type.equals("Mã đề")) {
                if (madeStr.contains(text)) {
                    result.add(dt);
                }
            } else if (type.equals("Tên đề")) {
                if (tenStr.contains(text)) {
                    result.add(dt);
                }
            } else if (type.equals("Người tạo")) {
                if (nguoitaoStr.contains(text)) {
                    result.add(dt);
                }
            }
        }
        return result;
    }
}
