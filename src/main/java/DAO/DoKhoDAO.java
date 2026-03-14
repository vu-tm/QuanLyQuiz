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
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO `dokho`(`tendokho`, `trangthai`) VALUES (?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTendokho());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int update(DoKhoDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE `dokho` SET `tendokho`=? WHERE `madokho`=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTendokho());
            pst.setInt(2, t.getMadokho());
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
            String sql = "UPDATE `dokho` SET trangthai=0 WHERE `madokho` = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<DoKhoDTO> selectAll() {
        ArrayList<DoKhoDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM dokho WHERE trangthai=1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int madokho = rs.getInt("madokho");
                String tendokho = rs.getString("tendokho");
                int trangthai = rs.getInt("trangthai");
                DoKhoDTO dk = new DoKhoDTO(madokho, tendokho, trangthai);
                result.add(dk);
            }
            JDBCUtil.closeConnection(con);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return result;
    }
}