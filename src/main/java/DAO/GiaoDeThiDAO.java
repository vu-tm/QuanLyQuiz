package DAO;

import DTO.GiaoDeThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class GiaoDeThiDAO {

    public static GiaoDeThiDAO getInstance() {
        return new GiaoDeThiDAO();
    }

    public int insert(GiaoDeThiDTO gdt) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO giaodethi(made, malop) VALUES(?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, gdt.getMade());
            pst.setInt(2, gdt.getMalop());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int deleteByMaDe(int made) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "DELETE FROM giaodethi WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<Integer> getMaLopByMaDe(int made) {
        ArrayList<Integer> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT malop FROM giaodethi WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("malop"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
