package DAO;

import DTO.MonHocDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MonHocDAO {

    public static MonHocDAO getInstance() {
        return new MonHocDAO();
    }

    public int insert(MonHocDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO monhoc (tenmonhoc, sotinchi, trangthai) VALUES (?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenmonhoc());
            pst.setInt(2, t.getSotinchi());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MonHocDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int update(MonHocDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE monhoc SET tenmonhoc = ?, sotinchi = ? WHERE mamonhoc = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenmonhoc());
            pst.setInt(2, t.getSotinchi());
            pst.setInt(3, t.getMamonhoc());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MonHocDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int delete(int mamonhoc) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE monhoc SET trangthai = 0 WHERE mamonhoc = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mamonhoc);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MonHocDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<MonHocDTO> selectAll() {
        ArrayList<MonHocDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM monhoc WHERE trangthai = 1 ORDER BY mamonhoc DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                MonHocDTO mh = new MonHocDTO();
                mh.setMamonhoc(rs.getInt("mamonhoc"));
                mh.setTenmonhoc(rs.getString("tenmonhoc"));
                mh.setSotinchi(rs.getInt("sotinchi"));
                mh.setTrangthai(rs.getInt("trangthai"));
                result.add(mh);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(MonHocDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}