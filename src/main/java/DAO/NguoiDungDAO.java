package DAO;

import DTO.NguoiDungDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class NguoiDungDAO {

    public static NguoiDungDAO getInstance() {
        return new NguoiDungDAO();
    }

    public ArrayList<NguoiDungDTO> selectAll() {
        ArrayList<NguoiDungDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM nguoidung WHERE trangthai = 0 OR trangthai = 1";
        try (Connection con = JDBCUtil.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql); 
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                NguoiDungDTO u = new NguoiDungDTO();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setHoten(rs.getString("hoten"));
                u.setGioitinh(rs.getBoolean("gioitinh"));
                u.setNgaysinh(rs.getDate("ngaysinh"));
                u.setMatkhau(rs.getString("matkhau"));
                u.setTrangthai(rs.getInt("trangthai"));
                u.setManhomquyen(rs.getInt("manhomquyen"));
                result.add(u);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public NguoiDungDTO selectById(int id) {
        String sql = "SELECT * FROM nguoidung WHERE id = ?";
        try (Connection con = JDBCUtil.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    NguoiDungDTO u = new NguoiDungDTO();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setHoten(rs.getString("hoten"));
                    u.setGioitinh(rs.getBoolean("gioitinh"));
                    u.setNgaysinh(rs.getDate("ngaysinh"));
                    u.setMatkhau(rs.getString("matkhau"));
                    u.setTrangthai(rs.getInt("trangthai"));
                    u.setManhomquyen(rs.getInt("manhomquyen"));
                    return u;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int insert(NguoiDungDTO t) {
        String sql = "INSERT INTO nguoidung (id, username, hoten, gioitinh, ngaysinh, matkhau, trangthai, manhomquyen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = JDBCUtil.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, t.getId());
            pst.setString(2, t.getUsername());
            pst.setString(3, t.getHoten());
            pst.setBoolean(4, t.isGioitinh());
            pst.setDate(5, t.getNgaysinh());
            pst.setString(6, t.getMatkhau());
            pst.setInt(7, t.getTrangthai());
            pst.setInt(8, t.getManhomquyen());
            return pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int update(NguoiDungDTO t) {
        String sql = "UPDATE nguoidung SET username=?, hoten=?, gioitinh=?, ngaysinh=?, matkhau=?, manhomquyen=?, trangthai=? WHERE id=?";
        try (Connection con = JDBCUtil.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, t.getUsername());
            pst.setString(2, t.getHoten());
            pst.setBoolean(3, t.isGioitinh());
            pst.setDate(4, t.getNgaysinh());
            pst.setString(5, t.getMatkhau());
            pst.setInt(6, t.getManhomquyen());
            pst.setInt(7, t.getTrangthai());
            pst.setInt(8, t.getId());
            return pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int delete(int id) {
        String sql = "UPDATE nguoidung SET trangthai = -1 WHERE id = ?";
        try (Connection con = JDBCUtil.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            return pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public boolean checkExistUsername(String username) {
        String sql = "SELECT COUNT(*) FROM nguoidung WHERE username = ?";
        try (Connection con = JDBCUtil.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public int getNextId() {
        String sql = "SELECT MAX(id) FROM nguoidung";
        try (Connection con = JDBCUtil.getConnection(); 
             PreparedStatement pst = con.prepareStatement(sql); 
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }
}