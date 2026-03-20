package DAO;

import DTO.PhanCongDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class PhanCongDAO {
    public static PhanCongDAO getInstance() {
        return new PhanCongDAO();
    }

    public int insert(PhanCongDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO phancong (mamonhoc, manguoidung) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMamonhoc());
            pst.setString(2, t.getManguoidung());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int update(PhanCongDTO old, PhanCongDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE phancong SET mamonhoc = ?, manguoidung = ? WHERE mamonhoc = ? AND manguoidung = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMamonhoc());
            pst.setString(2, t.getManguoidung());
            pst.setInt(3, old.getMamonhoc());
            pst.setString(4, old.getManguoidung());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int mamonhoc, String manguoidung) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "DELETE FROM phancong WHERE mamonhoc = ? AND manguoidung = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mamonhoc);
            pst.setString(2, manguoidung);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<PhanCongDTO> selectAll() {
        ArrayList<PhanCongDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM phancong";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(new PhanCongDTO(rs.getInt("mamonhoc"), rs.getString("manguoidung")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}