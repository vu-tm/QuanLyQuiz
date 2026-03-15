package DAO;

import DTO.CauHoiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CauHoiDAO {

    public static CauHoiDAO getInstance() {
        return new CauHoiDAO();
    }

    public ArrayList<CauHoiDTO> selectAll() {
        ArrayList<CauHoiDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM cauhoi WHERE trangthai = 1 ORDER BY macauhoi DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                CauHoiDTO ch = new CauHoiDTO();
                ch.setMacauhoi(rs.getInt("macauhoi"));
                ch.setNoidung(rs.getString("noidung"));
                ch.setMadokho(rs.getInt("madokho"));
                ch.setMaloai(rs.getInt("maloai"));
                ch.setMamonhoc(rs.getInt("mamonhoc"));
                ch.setNguoitao(rs.getString("nguoitao"));
                ch.setTrangthai(rs.getInt("trangthai"));
                result.add(ch);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(CauHoiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<Integer> selectMaCauHoiByMaDe(int made) {
        ArrayList<Integer> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT macauhoi FROM chitietdethi WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("macauhoi"));
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(CauHoiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}