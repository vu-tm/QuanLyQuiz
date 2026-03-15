package DAO;

import DTO.DeThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeThiDAO {

    public static DeThiDAO getInstance() {
        return new DeThiDAO();
    }

    public int insert(DeThiDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO dethi (makythi, monthi, nguoitao, tende, thoigiantao, thoigianthi, tongsocau, trangthai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMakythi());
            pst.setInt(2, t.getMonthi());
            pst.setString(3, t.getNguoitao());
            pst.setString(4, t.getTende());
            pst.setTimestamp(5, t.getThoigiantao());
            pst.setInt(6, t.getThoigianthi());
            pst.setInt(7, t.getTongsocau());
            pst.setInt(8, t.isTrangthai() ? 1 : 0);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int update(DeThiDTO t) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE dethi SET makythi = ?, monthi = ?, tende = ?, thoigianthi = ?, tongsocau = ?, trangthai = ? WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMakythi());
            pst.setInt(2, t.getMonthi());
            pst.setString(3, t.getTende());
            pst.setInt(4, t.getThoigianthi());
            pst.setInt(5, t.getTongsocau());
            pst.setInt(6, t.isTrangthai() ? 1 : 0);
            pst.setInt(7, t.getMade());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public int delete(int made) {
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "DELETE FROM dethi WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public ArrayList<DeThiDTO> selectAll() {
        ArrayList<DeThiDTO> result = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM dethi ORDER BY made DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                DeThiDTO dt = new DeThiDTO();
                dt.setMade(rs.getInt("made"));
                dt.setMakythi(rs.getInt("makythi"));
                dt.setMonthi(rs.getInt("monthi"));
                dt.setNguoitao(rs.getString("nguoitao"));
                dt.setTende(rs.getString("tende"));
                dt.setThoigiantao(rs.getTimestamp("thoigiantao"));
                dt.setThoigianthi(rs.getInt("thoigianthi"));
                dt.setTongsocau(rs.getInt("tongsocau"));
                dt.setTrangthai(rs.getInt("trangthai") == 1);
                result.add(dt);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException ex) {
            Logger.getLogger(DeThiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}