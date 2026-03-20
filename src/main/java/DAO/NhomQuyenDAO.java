package DAO;

import DTO.NhomQuyenDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhomQuyenDAO {

    public List<NhomQuyenDTO> getAll() {
        List<NhomQuyenDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nhomquyen WHERE trangthai = 1 ORDER BY manhomquyen";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                NhomQuyenDTO nq = new NhomQuyenDTO();
                nq.setManhomquyen(rs.getInt("manhomquyen"));
                nq.setTennhomquyen(rs.getString("tennhomquyen"));
                nq.setTrangthai(rs.getInt("trangthai"));
                list.add(nq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public NhomQuyenDTO getById(int manhomquyen) {
        String sql = "SELECT * FROM nhomquyen WHERE manhomquyen = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, manhomquyen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhomQuyenDTO nq = new NhomQuyenDTO();
                    nq.setManhomquyen(rs.getInt("manhomquyen"));
                    nq.setTennhomquyen(rs.getString("tennhomquyen"));
                    nq.setTrangthai(rs.getInt("trangthai"));
                    return nq;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhomQuyenDTO> search(String keyword) {
        List<NhomQuyenDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM nhomquyen WHERE tennhomquyen LIKE ? AND trangthai = 1 ORDER BY manhomquyen";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NhomQuyenDTO nq = new NhomQuyenDTO();
                    nq.setManhomquyen(rs.getInt("manhomquyen"));
                    nq.setTennhomquyen(rs.getString("tennhomquyen"));
                    nq.setTrangthai(rs.getInt("trangthai"));
                    list.add(nq);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(NhomQuyenDTO nq) {
        String sql = "INSERT INTO nhomquyen(tennhomquyen, trangthai) VALUES (?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nq.getTennhomquyen());
            ps.setInt(2, 1); // mặc định hoạt động
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean update(NhomQuyenDTO nq) {
        String sql = "UPDATE nhomquyen SET tennhomquyen = ?, trangthai = ? WHERE manhomquyen = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nq.getTennhomquyen());
            ps.setInt(2, nq.getTrangthai());
            ps.setInt(3, nq.getManhomquyen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa - chuyển trạng thái về 0
    public boolean delete(int manhomquyen) {
        String sql = "UPDATE nhomquyen SET trangthai = 0 WHERE manhomquyen = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, manhomquyen);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Chi tiết quyền 
    public boolean insertChiTietQuyen(int manhomquyen, List<Integer> dsQuyen) {
        String sql = "INSERT INTO chitietquyen(manhomquyen, maquyen) VALUES (?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int ma : dsQuyen) {
                ps.setInt(1, manhomquyen);
                ps.setInt(2, ma);
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            return results.length == dsQuyen.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteChiTietQuyen(int manhomquyen) {
        String sql = "DELETE FROM chitietquyen WHERE manhomquyen = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, manhomquyen);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> getQuyenIdsByNhom(int manhomquyen) {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT maquyen FROM chitietquyen WHERE manhomquyen = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, manhomquyen);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getInt("maquyen"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // kiểm tra id
    public boolean checkExistId(int manhomquyen) {
        String sql = "SELECT COUNT(*) FROM nhomquyen WHERE manhomquyen = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, manhomquyen);
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
    
    public int getNextId() {
        String sql = "SELECT IFNULL(MAX(manhomquyen), 0) + 1 FROM nhomquyen";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // mặc định nếu bảng chưa có dữ liệu
    }
}