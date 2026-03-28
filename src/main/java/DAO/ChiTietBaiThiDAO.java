package DAO;

import DTO.ChiTietBaiThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietBaiThiDAO {

    public static ChiTietBaiThiDAO getInstance() {
        return new ChiTietBaiThiDAO();
    }

    public int insert(ChiTietBaiThiDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO chitietbaithi (mabaithi, macauhoi, dapanchon, noidungdienkhuyet) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMabaithi());
            pst.setInt(2, t.getMacauhoi());
            if (t.getDapanchon() == 0) {
                pst.setNull(3, java.sql.Types.INTEGER);
            } else {
                pst.setInt(3, t.getDapanchon());
            }
            pst.setString(4, t.getNoidungdienkhuyet());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int mabaithi) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "DELETE FROM chitietbaithi WHERE mabaithi = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mabaithi);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<ChiTietBaiThiDTO> selectAll() {
        ArrayList<ChiTietBaiThiDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM chitietbaithi";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                ChiTietBaiThiDTO ct = new ChiTietBaiThiDTO();
                ct.setMabaithi(rs.getInt("mabaithi"));
                ct.setMacauhoi(rs.getInt("macauhoi"));
                ct.setDapanchon(rs.getInt("dapanchon"));
                ct.setNoidungdienkhuyet(rs.getString("noidungdienkhuyet"));
                result.add(ct);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<ChiTietBaiThiDTO> selectAll(int mabaithi) {
        ArrayList<ChiTietBaiThiDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM chitietbaithi WHERE mabaithi = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mabaithi);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ChiTietBaiThiDTO ct = new ChiTietBaiThiDTO();
                ct.setMabaithi(rs.getInt("mabaithi"));
                ct.setMacauhoi(rs.getInt("macauhoi"));
                ct.setDapanchon(rs.getInt("dapanchon"));
                ct.setNoidungdienkhuyet(rs.getString("noidungdienkhuyet"));
                result.add(ct);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
