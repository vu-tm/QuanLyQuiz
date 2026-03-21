package DAO;

import DTO.ChiTietQuyenDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietQuyenDAO {
    public static ChiTietQuyenDAO getInstance() {
        return new ChiTietQuyenDAO();
    }

    public int insert(ArrayList<ChiTietQuyenDTO> list) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO ctquyen (manhomquyen, machucnang, hanhdong) VALUES (?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            for (ChiTietQuyenDTO item : list) {
                pst.setInt(1, item.getManhomquyen());
                pst.setString(2, item.getMachucnang());
                pst.setString(3, item.getHanhdong());
                result += pst.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int manhomquyen) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "DELETE FROM ctquyen WHERE manhomquyen = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, manhomquyen);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<ChiTietQuyenDTO> selectAll(int manhomquyen) {
        ArrayList<ChiTietQuyenDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM ctquyen WHERE manhomquyen = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, manhomquyen);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(new ChiTietQuyenDTO(rs.getInt("manhomquyen"), rs.getString("machucnang"), rs.getString("hanhdong")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}