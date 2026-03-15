package DAO;

import DTO.GiaoDeThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GiaoDeThiDAO {

    public static GiaoDeThiDAO getInstance() {
        return new GiaoDeThiDAO();
    }

    public ArrayList<Integer> getMaLopByMaDe(int made) {
        ArrayList<Integer> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT malop FROM giaodethi WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("malop"));
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(GiaoDeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int deleteByMaDe(int made) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM giaodethi WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(GiaoDeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int insert(GiaoDeThiDTO gdt) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO giaodethi(made, malop) VALUES(?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, gdt.getMade());
            pst.setInt(2, gdt.getMalop());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(GiaoDeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean saveAll(int made, ArrayList<Integer> listMaLop) {
        deleteByMaDe(made);
        for (int malop : listMaLop) {
            GiaoDeThiDTO gdt = new GiaoDeThiDTO(made, malop);
            if (insert(gdt) == 0) {
                return false;
            }
        }
        return true;
    }
}