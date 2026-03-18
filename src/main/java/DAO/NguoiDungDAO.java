package DAO;

import DTO.NguoiDungDTO;
import helper.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAO {
    
    public List<NguoiDungDTO> getAll() {
        List<NguoiDungDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoidung ORDER BY hoten ASC";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NguoiDungDTO user = new NguoiDungDTO();
                user.setId(rs.getString("id"));
                user.setUsername(rs.getString("username"));
                user.setHoten(rs.getString("hoten"));
                user.setGioitinh(rs.getBoolean("gioitinh"));
                user.setNgaysinh(rs.getDate("ngaysinh"));
                user.setMatkhau(rs.getString("matkhau"));
                user.setTrangthai(rs.getInt("trangthai"));
                user.setManhomquyen(rs.getInt("manhomquyen"));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy người dùng theo ID (chỉ lấy user có trạng thái = 1)
    public NguoiDungDTO getById(String id) {
        String sql = "SELECT * FROM nguoidung WHERE id = ? AND trangthai = 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NguoiDungDTO user = new NguoiDungDTO();
                    user.setId(rs.getString("id"));
                    user.setUsername(rs.getString("username"));
                    user.setHoten(rs.getString("hoten"));
                    user.setGioitinh(rs.getBoolean("gioitinh"));
                    user.setNgaysinh(rs.getDate("ngaysinh"));
                    user.setMatkhau(rs.getString("matkhau"));
                    user.setTrangthai(rs.getInt("trangthai"));
                    user.setManhomquyen(rs.getInt("manhomquyen"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy người dùng theo ID (không phân biệt trạng thái) - dùng cho sửa
    public NguoiDungDTO getByIdAll(String id) {
        String sql = "SELECT * FROM nguoidung WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NguoiDungDTO user = new NguoiDungDTO();
                    user.setId(rs.getString("id"));
                    user.setUsername(rs.getString("username"));
                    user.setHoten(rs.getString("hoten"));
                    user.setGioitinh(rs.getBoolean("gioitinh"));
                    user.setNgaysinh(rs.getDate("ngaysinh"));
                    user.setMatkhau(rs.getString("matkhau"));
                    user.setTrangthai(rs.getInt("trangthai"));
                    user.setManhomquyen(rs.getInt("manhomquyen"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Lấy người dùng theo username (chỉ lấy user có trạng thái = 1)
    public NguoiDungDTO getByUsername(String username) {
        String sql = "SELECT * FROM nguoidung WHERE username = ? AND trangthai = 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NguoiDungDTO user = new NguoiDungDTO();
                    user.setId(rs.getString("id"));
                    user.setUsername(rs.getString("username"));
                    user.setHoten(rs.getString("hoten"));
                    user.setGioitinh(rs.getBoolean("gioitinh"));
                    user.setNgaysinh(rs.getDate("ngaysinh"));
                    user.setMatkhau(rs.getString("matkhau"));
                    user.setTrangthai(rs.getInt("trangthai"));
                    user.setManhomquyen(rs.getInt("manhomquyen"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Thêm mới người dùng
    public boolean insert(NguoiDungDTO user) {
        String sql = "INSERT INTO nguoidung (id, username, hoten, gioitinh, ngaysinh, matkhau, trangthai, manhomquyen) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getHoten());
            ps.setBoolean(4, user.isGioitinh());
            ps.setDate(5, user.getNgaysinh());
            ps.setString(6, user.getMatkhau());
            ps.setInt(7, user.getTrangthai());
            ps.setInt(8, user.getManhomquyen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật thông tin người dùng (không cập nhật mật khẩu ở đây, có thể thêm sau)
    public boolean update(NguoiDungDTO user) {
        String sql = "UPDATE nguoidung SET username = ?, hoten = ?, gioitinh = ?, " +
                     "ngaysinh = ?, matkhau = ?, manhomquyen = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getHoten());
            ps.setBoolean(3, user.isGioitinh());
            ps.setDate(4, user.getNgaysinh());
            ps.setString(5, user.getMatkhau());
            ps.setInt(6, user.getManhomquyen());
            ps.setString(7, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa mềm (cập nhật trạng thái = 0)
    public boolean delete(String id) {
        String sql = "UPDATE nguoidung SET trangthai = 0 WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa cứng khỏi database
    public boolean deleteHard(String id) {
        String sql = "DELETE FROM nguoidung WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Tìm kiếm theo từ khóa (chỉ user có trạng thái = 1)
    public List<NguoiDungDTO> search(String keyword) {
        List<NguoiDungDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoidung WHERE trangthai = 1 AND " +
                     "(id LIKE ? OR username LIKE ? OR hoten LIKE ?) " +
                     "ORDER BY hoten ASC";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NguoiDungDTO user = new NguoiDungDTO();
                    user.setId(rs.getString("id"));
                    user.setUsername(rs.getString("username"));
                    user.setHoten(rs.getString("hoten"));
                    user.setGioitinh(rs.getBoolean("gioitinh"));
                    user.setNgaysinh(rs.getDate("ngaysinh"));
                    user.setMatkhau(rs.getString("matkhau"));
                    user.setTrangthai(rs.getInt("trangthai"));
                    user.setManhomquyen(rs.getInt("manhomquyen"));
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Kiểm tra tồn tại username (chỉ user có trạng thái = 1)
    public boolean checkExistUsername(String username) {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE username = ? AND trangthai = 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra tồn tại username (không phân biệt trạng thái) - dùng cho thêm mới
    public boolean checkExistUsernameAll(String username) {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra tồn tại ID (chỉ user có trạng thái = 1)
    public boolean checkExistId(String id) {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE id = ? AND trangthai = 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Kiểm tra tồn tại ID (không phân biệt trạng thái) - dùng cho thêm mới
    public boolean checkExistIdAll(String id) {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // đếm số lượng role trên user
    public int countByRole(int manhomquyen) {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE manhomquyen = ? AND trangthai = 1";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, manhomquyen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}