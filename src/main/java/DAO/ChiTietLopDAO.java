package DAO;

import DTO.ChiTietLopDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietLopDAO {

    public static ChiTietLopDAO getInstance() {
        return new ChiTietLopDAO();
    }

    public ArrayList<ChiTietLopDTO> selectByMaLop(int malop) {
        ArrayList<ChiTietLopDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM chitietlop WHERE malop=? AND hienthi=1";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, malop);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ChiTietLopDTO ct = new ChiTietLopDTO();
                    ct.setMalop(rs.getInt("malop"));
                    ct.setManguoidung(rs.getInt("manguoidung"));
                    ct.setHienthi(rs.getInt("hienthi"));
                    ketQua.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public int insert(ChiTietLopDTO ct) {
        int result = 0;
        String sql = "INSERT INTO chitietlop(malop, manguoidung, hienthi) VALUES(?,?,1)";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, ct.getMalop());
            pst.setInt(2, ct.getManguoidung());
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int delete(int malop, int manguoidung) {
        int result = 0;
        String sql = "UPDATE chitietlop SET hienthi=0 WHERE malop=? AND manguoidung=?";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, malop);
            pst.setInt(2, manguoidung);
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int restore(int malop, int manguoidung) {
        int result = 0;
        String sql = "UPDATE chitietlop SET hienthi=1 WHERE malop=? AND manguoidung=?";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, malop);
            pst.setInt(2, manguoidung);
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean checkExists(int malop, int manguoidung) {
        String sql = "SELECT 1 FROM chitietlop WHERE malop=? AND manguoidung=?";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, malop);
            pst.setInt(2, manguoidung);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countByMaLop(int malop) {
        String sql = "SELECT COUNT(*) as total FROM chitietlop WHERE malop=? AND hienthi=1";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, malop);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<ChiTietLopDTO> selectByUser(int manguoidung) {
        ArrayList<ChiTietLopDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM chitietlop WHERE manguoidung=? AND hienthi=1";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, manguoidung);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ChiTietLopDTO ct = new ChiTietLopDTO();
                    ct.setMalop(rs.getInt("malop"));
                    ct.setManguoidung(rs.getInt("manguoidung"));
                    ct.setHienthi(rs.getInt("hienthi"));
                    ketQua.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}
