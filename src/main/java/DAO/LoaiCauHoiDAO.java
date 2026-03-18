package DAO;

import DTO.LoaiCauHoiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class LoaiCauHoiDAO {

    public static LoaiCauHoiDAO getInstance() {
        return new LoaiCauHoiDAO();
    }

    public int insert(LoaiCauHoiDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO loaicauhoi (tenloai, trangthai) VALUES (?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenloai());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int update(LoaiCauHoiDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE loaicauhoi SET tenloai = ? WHERE maloai = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTenloai());
            pst.setInt(2, t.getMaloai());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int id) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE loaicauhoi SET trangthai = 0 WHERE maloai = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<LoaiCauHoiDTO> selectAll() {
        ArrayList<LoaiCauHoiDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM loaicauhoi WHERE trangthai = 1 ORDER BY maloai DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                LoaiCauHoiDTO lch = new LoaiCauHoiDTO();
                lch.setMaloai(rs.getInt("maloai"));
                lch.setTenloai(rs.getString("tenloai"));
                lch.setTrangthai(rs.getInt("trangthai"));
                result.add(lch);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
