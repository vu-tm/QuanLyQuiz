package BUS;

import DAO.NguoiDungDAO;
import DTO.NguoiDungDTO;
import java.util.List;
import java.util.ArrayList;

public class NguoiDungBUS {
    private NguoiDungDAO dao = new NguoiDungDAO();

    // Lấy tất cả người dùng (kể cả đã khóa)
    public List<NguoiDungDTO> getAll() {
        return dao.getAll();
    }

    // Lấy người dùng theo ID (không phân biệt trạng thái) - dùng cho sửa
    public NguoiDungDTO getById(String id) {
        return dao.getById(id);
    }

    // Kiểm tra tồn tại ID (không phân biệt trạng thái) - dùng cho thêm mới
    public boolean checkExistId(String id) {
        return dao.checkExistIdAll(id);
    }

    // Kiểm tra tồn tại username (không phân biệt trạng thái) - dùng cho thêm mới
    public boolean checkExistUsername(String username) {
        return dao.checkExistUsernameAll(username);
    }

    // Thêm mới người dùng
    public boolean insert(NguoiDungDTO user) {
        return dao.insert(user);
    }

    // Cập nhật người dùng
    public boolean update(NguoiDungDTO user) {
        return dao.update(user);
    }

    // Xóa mềm (cập nhật trạng thái = 0)
    public boolean delete(String id) {
        return dao.delete(id);
    }

    // Xóa cứng khỏi database
    public boolean deleteHard(String id) {
        return dao.deleteHard(id);
    }

    // Tìm kiếm người dùng theo từ khóa (trên tất cả dữ liệu)
    public List<NguoiDungDTO> search(String keyword) {
        List<NguoiDungDTO> all = dao.getAll();
        if (keyword == null || keyword.trim().isEmpty()) {
            return all;
        }
        String kw = keyword.toLowerCase().trim();
        List<NguoiDungDTO> result = new ArrayList<>();
        for (NguoiDungDTO user : all) {
            if (user.getId().toLowerCase().contains(kw) ||
                user.getUsername().toLowerCase().contains(kw) ||
                user.getHoten().toLowerCase().contains(kw)) {
                result.add(user);
            }
        }
        return result;
    }

    // Lấy tên nhóm quyền theo mã
    public String getTenNhomQuyen(int manhomquyen) {
        switch (manhomquyen) {
            case 1: return "Admin";
            case 2: return "Giảng viên";
            case 3: return "Sinh viên";
            default: return "Không xác định";
        }
    }

    // Chuyển giới tính thành text
    public String getGioiTinhText(boolean gioitinh) {
        return gioitinh ? "Nam" : "Nữ";
    }

    // Chuyển trạng thái thành text
    public String getTrangThaiText(int trangthai) {
        return trangthai == 1 ? "Hoạt động" : "Đã khóa";
    }
    
    public String getNextId() {
        return dao.getNextId();
    }
}