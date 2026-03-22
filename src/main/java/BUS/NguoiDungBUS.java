package BUS;

import DAO.NguoiDungDAO;
import DTO.NguoiDungDTO;
import DTO.NhomQuyenDTO;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungBUS {

    private final NguoiDungDAO dao = NguoiDungDAO.getInstance();
    private final NhomQuyenBUS nqBUS = new NhomQuyenBUS();
    private ArrayList<NguoiDungDTO> listNguoiDung;
    private List<NhomQuyenDTO> listNQ;

    public NguoiDungBUS() {
        this.listNguoiDung = dao.selectAll();
        this.listNQ = nqBUS.getAll();
    }

    public ArrayList<NguoiDungDTO> getAll() {
        this.listNguoiDung = dao.selectAll();
        return this.listNguoiDung;
    }

    public NguoiDungDTO getById(int id) {
        for (NguoiDungDTO u : listNguoiDung) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    public boolean add(NguoiDungDTO user) {
        boolean check = dao.insert(user) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(NguoiDungDTO user) {
        boolean check = dao.update(user) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean delete(int id) {
        boolean check = dao.delete(id) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean checkExistUsername(String username) {
        return dao.checkExistUsername(username);
    }

    public int getNextId() {
        return dao.getNextId();
    }

    public ArrayList<NguoiDungDTO> search(String text, String type) {
        ArrayList<NguoiDungDTO> result = new ArrayList<>();
        text = text.toLowerCase().trim();
        for (NguoiDungDTO u : listNguoiDung) {
            boolean match = false;
            switch (type) {
                case "Tất cả" -> {
                    String tenNQ = getTenNhomQuyen(u.getManhomquyen()).toLowerCase();
                    match = Integer.toString(u.getId()).contains(text)
                            || u.getUsername().toLowerCase().contains(text)
                            || u.getHoten().toLowerCase().contains(text)
                            || tenNQ.contains(text);
                }
                case "ID" ->
                    match = Integer.toString(u.getId()).contains(text);
                case "Username" ->
                    match = u.getUsername().toLowerCase().contains(text);
                case "Họ tên" ->
                    match = u.getHoten().toLowerCase().contains(text);
                case "Nhóm quyền" -> {
                    String tenNQ = getTenNhomQuyen(u.getManhomquyen()).toLowerCase();
                    match = tenNQ.contains(text);
                }
            }
            if (match) {
                result.add(u);
            }
        }
        return result;
    }

    public String getTenNhomQuyen(int manhomquyen) {
        List<NhomQuyenDTO> list = nqBUS.getAll();
        for (NhomQuyenDTO nq : list) {
            if (nq.getManhomquyen() == manhomquyen) {
                return nq.getTennhomquyen();
            }
        }
        return "Không xác định";
    }

    public String getGioiTinhText(boolean gioitinh) {
        return gioitinh ? "Nam" : "Nữ";
    }

    public String getTrangThaiText(int trangthai) {
        return trangthai == 1 ? "Hoạt động" : "Đã khóa";
    }
}
