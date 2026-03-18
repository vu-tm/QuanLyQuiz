package DAO;

import DTO.DapAnDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class DapAnDAO {

    public static DapAnDAO getInstance() {
        return new DapAnDAO();
    }

    /**
     * Lấy danh sách đáp án theo mã câu hỏi.
     * Chỉ lấy noidungtl – KHÔNG lấy ladapan, để không lộ đáp án đúng khi in đề.
     */
    public ArrayList<DapAnDTO> selectByCauHoi(int macauhoi) {
        ArrayList<DapAnDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT madapan, macauhoi, noidungtl "
                       + "FROM dapan WHERE macauhoi = ? ORDER BY madapan ASC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, macauhoi);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                DapAnDTO da = new DapAnDTO();
                da.setMadapan(rs.getInt("madapan"));
                da.setMacauhoi(rs.getInt("macauhoi"));
                da.setNoidungtl(rs.getString("noidungtl"));
                result.add(da);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<DapAnDTO> selectAll() {
        ArrayList<DapAnDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM dapan ORDER BY macauhoi, madapan";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                DapAnDTO da = new DapAnDTO();
                da.setMadapan(rs.getInt("madapan"));
                da.setMacauhoi(rs.getInt("macauhoi"));
                da.setNoidungtl(rs.getString("noidungtl"));
                da.setLadapan(rs.getBoolean("ladapan"));
                result.add(da);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int insert(DapAnDTO da) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO dapan (macauhoi, noidungtl, ladapan) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, da.getMacauhoi());
            pst.setString(2, da.getNoidungtl());
            pst.setBoolean(3, da.isLadapan());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int update(DapAnDTO da) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE dapan SET noidungtl = ?, ladapan = ? WHERE madapan = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, da.getNoidungtl());
            pst.setBoolean(2, da.isLadapan());
            pst.setInt(3, da.getMadapan());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int madapan) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "DELETE FROM dapan WHERE madapan = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, madapan);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int deleteByMaCauHoi(int macauhoi) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "DELETE FROM dapan WHERE macauhoi = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, macauhoi);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}