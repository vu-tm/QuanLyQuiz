package DAO;

import DTO.DoKhoDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class DoKhoDAO {

    public static DoKhoDAO getInstance() {
        return new DoKhoDAO();
    }

    public int insert(DoKhoDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO dokho (tendokho, trangthai) VALUES (?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTendokho());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int update(DoKhoDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE dokho SET tendokho = ? WHERE madokho = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTendokho());
            pst.setInt(2, t.getMadokho());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int id) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE dokho SET trangthai = 0 WHERE madokho = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<DoKhoDTO> selectAll() {
        ArrayList<DoKhoDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM dokho WHERE trangthai = 1 ORDER BY madokho DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                DoKhoDTO dk = new DoKhoDTO();
                dk.setMadokho(rs.getInt("madokho"));
                dk.setTendokho(rs.getString("tendokho"));
                dk.setTrangthai(rs.getInt("trangthai"));
                result.add(dk);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean checkTrungTen(String ten, int excludeId) {
        String sql = "SELECT COUNT(*) FROM dokho WHERE LOWER(tendokho) = ? AND trangthai = 1 AND madokho != ?";
        try (Connection c = JDBCUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ten.toLowerCase().trim());
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public DoKhoDTO selectById(int id) {
        DoKhoDTO result = null;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM dokho WHERE madokho = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = new DoKhoDTO();
                result.setMadokho(rs.getInt("madokho"));
                result.setTendokho(rs.getString("tendokho"));
                result.setTrangthai(rs.getInt("trangthai"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
