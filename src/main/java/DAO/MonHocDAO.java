package DAO;

import DTO.MonHocDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class MonHocDAO {

    public static MonHocDAO getInstance() {
        return new MonHocDAO();
    }

    public int insert(MonHocDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO `monhoc`(`tenmonhoc`, `sotinchi`, `trangthai`) VALUES (?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenmonhoc());
            pst.setInt(2, t.getSotinchi());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int update(MonHocDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE `monhoc` SET `tenmonhoc`=?, `sotinchi`=? WHERE `mamonhoc`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenmonhoc());
            pst.setInt(2, t.getSotinchi());
            pst.setInt(3, t.getMamonhoc());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int delete(int t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE `monhoc` SET trangthai=0 WHERE `mamonhoc` = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<MonHocDTO> selectAll() {
        ArrayList<MonHocDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM monhoc WHERE trangthai=1";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getAutoIncrement() {
        int result = -1;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quiz' AND TABLE_NAME = 'monhoc'";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt("AUTO_INCREMENT");
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}