package DAO;

import DTO.KyThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class KyThiDAO {

    public static KyThiDAO getInstance() {
        return new KyThiDAO();
    }

    public int insert(KyThiDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO kythi (tenkythi, thoigianbatdau, thoigianketthuc, trangthai) VALUES (?, ?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenkythi());
            pst.setTimestamp(2, t.getThoigianbatdau());
            pst.setTimestamp(3, t.getThoigianketthuc());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int update(KyThiDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE kythi SET tenkythi = ?, thoigianbatdau = ?, thoigianketthuc = ? WHERE makythi = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenkythi());
            pst.setTimestamp(2, t.getThoigianbatdau());
            pst.setTimestamp(3, t.getThoigianketthuc());
            pst.setInt(4, t.getMakythi());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int id) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE kythi SET trangthai = 0 WHERE makythi = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<KyThiDTO> selectAll() {
        ArrayList<KyThiDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM kythi WHERE trangthai = 1 ORDER BY makythi DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                KyThiDTO kt = new KyThiDTO();
                kt.setMakythi(rs.getInt("makythi"));
                kt.setTenkythi(rs.getString("tenkythi"));
                kt.setThoigianbatdau(rs.getTimestamp("thoigianbatdau"));
                kt.setThoigianketthuc(rs.getTimestamp("thoigianketthuc"));
                kt.setTrangthai(rs.getInt("trangthai"));
                result.add(kt);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
