package DAO;

import DTO.ChiTietDeThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietDeThiDAO {

    public static ChiTietDeThiDAO getInstance() {
        return new ChiTietDeThiDAO();
    }

    public int insert(ChiTietDeThiDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO chitietdethi (made, macauhoi, thutu) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMade());
            pst.setInt(2, t.getMacauhoi());
            pst.setInt(3, t.getThutu());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int deleteByMade(int made) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "DELETE FROM chitietdethi WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<ChiTietDeThiDTO> selectByMade(int made) {
        ArrayList<ChiTietDeThiDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM chitietdethi WHERE made = ? ORDER BY thutu ASC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ChiTietDeThiDTO ct = new ChiTietDeThiDTO(rs.getInt("made"), rs.getInt("macauhoi"), rs.getInt("thutu"));
                result.add(ct);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
