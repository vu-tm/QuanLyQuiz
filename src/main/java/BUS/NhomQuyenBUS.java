package BUS;

import DAO.ChiTietQuyenDAO;
import DAO.NhomQuyenDAO;
import DTO.ChiTietQuyenDTO;
import DTO.NhomQuyenDTO;
import java.util.ArrayList;

public class NhomQuyenBUS {

    private final NhomQuyenDAO nhomquyenDAO = NhomQuyenDAO.getInstance();
    private final ChiTietQuyenDAO chitietquyenDAO = ChiTietQuyenDAO.getInstance();
    private ArrayList<NhomQuyenDTO> listNhomQuyen;

    public NhomQuyenBUS() {
        this.listNhomQuyen = nhomquyenDAO.selectAll();
    }

    public ArrayList<NhomQuyenDTO> getAll() {
        this.listNhomQuyen = nhomquyenDAO.selectAll();
        return this.listNhomQuyen;
    }

    public boolean add(NhomQuyenDTO nq, ArrayList<ChiTietQuyenDTO> ctList) {
        boolean check = nhomquyenDAO.insert(nq) > 0;
        if (check) {
            chitietquyenDAO.insert(ctList);
        }
        return check;
    }

    public boolean update(NhomQuyenDTO nq, ArrayList<ChiTietQuyenDTO> ctList) {
        boolean check = nhomquyenDAO.update(nq) > 0;
        if (check) {
            chitietquyenDAO.delete(nq.getManhomquyen());
            chitietquyenDAO.insert(ctList);
        }
        return check;
    }

    public boolean delete(int id) {
        return nhomquyenDAO.delete(id) > 0;
    }

    public ArrayList<ChiTietQuyenDTO> getChiTietQuyen(int ma) {
        return chitietquyenDAO.selectAll(ma);
    }

    public NhomQuyenDTO getById(int manhomquyen) {
        return nhomquyenDAO.selectAll().stream().filter(n -> n.getManhomquyen() == manhomquyen).findFirst().orElse(null);
    }

    public int getAutoIncrement() {
        return nhomquyenDAO.getAutoIncrement();
    }

    public ArrayList<NhomQuyenDTO> search(String text, String type) {
        ArrayList<NhomQuyenDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (NhomQuyenDTO nq : this.listNhomQuyen) {
            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = Integer.toString(nq.getManhomquyen()).contains(text)
                            || nq.getTennhomquyen().toLowerCase().contains(text);
                    break;
                case "Mã nhóm":
                    match = Integer.toString(nq.getManhomquyen()).contains(text);
                    break;
                case "Tên nhóm":
                    match = nq.getTennhomquyen().toLowerCase().contains(text);
                    break;
            }
            if (match) {
                result.add(nq);
            }
        }
        return result;
    }

    public boolean checkPermisson(int manhomquyen, String chucnang, String hanhdong) {
        ArrayList<ChiTietQuyenDTO> ctquyen = chitietquyenDAO.selectAll(manhomquyen);
        for (ChiTietQuyenDTO ct : ctquyen) {
            if (ct.getMachucnang().equals(chucnang) && ct.getHanhdong().equals(hanhdong)) {
                return true;
            }
        }
        return false;
    }

    public int getMaNhomQuyenByTen(String ten) {
        for (NhomQuyenDTO nq : getAll()) {
            if (nq.getTennhomquyen().equalsIgnoreCase(ten)) {
                return nq.getManhomquyen();
            }
        }
        return -1;
    }
}
