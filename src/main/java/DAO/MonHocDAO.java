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
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO monhoc (tenmonhoc, sotinchi, trangthai) VALUES (?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenmonhoc());
            pst.setInt(2, t.getSotinchi());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int update(MonHocDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE monhoc SET tenmonhoc = ?, sotinchi = ? WHERE mamonhoc = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenmonhoc());
            pst.setInt(2, t.getSotinchi());
            pst.setInt(3, t.getMamonhoc());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int mamonhoc) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE monhoc SET trangthai = 0 WHERE mamonhoc = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mamonhoc);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<MonHocDTO> selectAll() {
        ArrayList<MonHocDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public MonHocDTO selectById(int mamonhoc) {
        MonHocDTO mh = null;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM monhoc WHERE mamonhoc = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mamonhoc);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                mh = new MonHocDTO();
                mh.setMamonhoc(rs.getInt("mamonhoc"));
                mh.setTenmonhoc(rs.getString("tenmonhoc"));
                mh.setSotinchi(rs.getInt("sotinchi"));
                mh.setTrangthai(rs.getInt("trangthai"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return mh;
    }

    public boolean checkTrungTen(String ten, int excludeId) {
        String sql = "SELECT COUNT(*) FROM monhoc WHERE LOWER(tenmonhoc) = ? AND trangthai = 1 AND mamonhoc != ?";
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
}
