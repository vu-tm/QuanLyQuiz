/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author Windows
 */
import DTO.KyThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class KyThiDAO {

    public static KyThiDAO getInstance() {
        return new KyThiDAO();
    }

    public ArrayList<KyThiDTO> selectAll() {
        ArrayList<KyThiDTO> ketQua = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM kythi where trangthai = 1";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                KyThiDTO kt = new KyThiDTO();
                kt.setMakythi(rs.getInt("makythi"));
                kt.setTenkythi(rs.getString("tenkythi"));
                kt.setThoigianbatdau(rs.getTimestamp("thoigianbatdau"));
                kt.setThoigianketthuc(rs.getTimestamp("thoigianketthuc"));
                kt.setTrangthai(rs.getInt("trangthai"));
                ketQua.add(kt);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public int insert(KyThiDTO kt) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO kythi(tenkythi, thoigianbatdau, thoigianketthuc, trangthai) VALUES(?,?,?,1)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, kt.getTenkythi());
            pst.setTimestamp(2, kt.getThoigianbatdau());
            pst.setTimestamp(3, kt.getThoigianketthuc());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int update(KyThiDTO kt) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE kythi SET tenkythi=?, thoigianbatdau=?, thoigianketthuc=? WHERE makythi=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, kt.getTenkythi());
            pst.setTimestamp(2, kt.getThoigianbatdau());
            pst.setTimestamp(3, kt.getThoigianketthuc());
            pst.setInt(4, kt.getMakythi());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int delete(int id) {
        int result = 0;
        try {
            Connection conn = JDBCUtil.getConnection();
            String sql = "UPDATE kythi SET trangthai = 0 WHERE makythi = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}