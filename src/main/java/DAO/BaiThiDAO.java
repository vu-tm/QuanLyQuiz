package DAO;

import DTO.BaiThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class BaiThiDAO {

    public static BaiThiDAO getInstance() {
        return new BaiThiDAO();
    }

    public int insert(BaiThiDTO t) {
        int result = -1;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO baithi (made, manguoidung, diemthi, thoigianvaothi, thoigianlambai, socaudung, socausai) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, t.getMade());
            pst.setString(2, t.getManguoidung());
            pst.setDouble(3, t.getDiemthi());
            pst.setTimestamp(4, t.getThoigianvaothi());
            pst.setInt(5, t.getThoigianlambai());
            pst.setInt(6, t.getSocaudung());
            pst.setInt(7, t.getSocausai());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int update(BaiThiDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE baithi SET made = ?, manguoidung = ?, diemthi = ?, thoigianvaothi = ?, thoigianlambai = ?, socaudung = ?, socausai = ? WHERE mabaithi = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMade());
            pst.setString(2, t.getManguoidung());
            pst.setDouble(3, t.getDiemthi());
            pst.setTimestamp(4, t.getThoigianvaothi());
            pst.setInt(5, t.getThoigianlambai());
            pst.setInt(6, t.getSocaudung());
            pst.setInt(7, t.getSocausai());
            pst.setInt(8, t.getMabaithi());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int mabaithi) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "DELETE FROM baithi WHERE mabaithi = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mabaithi);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<BaiThiDTO> selectAll() {
        ArrayList<BaiThiDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM baithi ORDER BY mabaithi DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                BaiThiDTO bt = new BaiThiDTO();
                bt.setMabaithi(rs.getInt("mabaithi"));
                bt.setMade(rs.getInt("made"));
                bt.setManguoidung(rs.getString("manguoidung"));
                bt.setDiemthi(rs.getDouble("diemthi"));
                bt.setThoigianvaothi(rs.getTimestamp("thoigianvaothi"));
                bt.setThoigianlambai(rs.getInt("thoigianlambai"));
                bt.setSocaudung(rs.getInt("socaudung"));
                bt.setSocausai(rs.getInt("socausai"));
                result.add(bt);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public BaiThiDTO selectById(int mabaithi) {
        BaiThiDTO result = null;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM baithi WHERE mabaithi = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mabaithi);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = new BaiThiDTO();
                result.setMabaithi(rs.getInt("mabaithi"));
                result.setMade(rs.getInt("made"));
                result.setManguoidung(rs.getString("manguoidung"));
                result.setDiemthi(rs.getDouble("diemthi"));
                result.setThoigianvaothi(rs.getTimestamp("thoigianvaothi"));
                result.setThoigianlambai(rs.getInt("thoigianlambai"));
                result.setSocaudung(rs.getInt("socaudung"));
                result.setSocausai(rs.getInt("socausai"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<BaiThiDTO> selectByUserId(String userId) {
        ArrayList<BaiThiDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM baithi WHERE manguoidung = ? ORDER BY mabaithi DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                BaiThiDTO bt = new BaiThiDTO();
                bt.setMabaithi(rs.getInt("mabaithi"));
                bt.setMade(rs.getInt("made"));
                bt.setManguoidung(rs.getString("manguoidung"));
                bt.setDiemthi(rs.getDouble("diemthi"));
                bt.setThoigianvaothi(rs.getTimestamp("thoigianvaothi"));
                bt.setThoigianlambai(rs.getInt("thoigianlambai"));
                bt.setSocaudung(rs.getInt("socaudung"));
                bt.setSocausai(rs.getInt("socausai"));
                result.add(bt);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
