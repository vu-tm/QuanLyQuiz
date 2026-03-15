package DAO;

import DTO.ChiTietDeThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChiTietDeThiDAO {

    public static ChiTietDeThiDAO getInstance() {
        return new ChiTietDeThiDAO();
    }

    public int insert(ChiTietDeThiDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO chitietdethi (made, macauhoi, thutu) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMade());
            pst.setInt(2, t.getMacauhoi());
            pst.setInt(3, t.getThutu());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietDeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int deleteByMade(int made) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM chitietdethi WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietDeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<ChiTietDeThiDTO> selectByMade(int made) {
        ArrayList<ChiTietDeThiDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM chitietdethi WHERE made = ? ORDER BY thutu ASC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ChiTietDeThiDTO ct = new ChiTietDeThiDTO();
                ct.setMade(rs.getInt("made"));
                ct.setMacauhoi(rs.getInt("macauhoi"));
                ct.setThutu(rs.getInt("thutu"));
                result.add(ct);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietDeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}