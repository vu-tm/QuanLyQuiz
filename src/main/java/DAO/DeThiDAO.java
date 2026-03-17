package DAO;

import DTO.CauHoiDTO;
import DTO.DeThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class DeThiDAO {

    public static DeThiDAO getInstance() {
        return new DeThiDAO();
    }

    public int insert(DeThiDTO t) {
        int result = -1;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO dethi (makythi, monthi, nguoitao, tende, thoigiantao, thoigianthi, tongsocau, trangthai) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, t.getMakythi());
            pst.setInt(2, t.getMonthi());
            pst.setString(3, t.getNguoitao());
            pst.setString(4, t.getTende());
            pst.setTimestamp(5, t.getThoigiantao());
            pst.setInt(6, t.getThoigianthi());
            pst.setInt(7, t.getTongsocau());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int update(DeThiDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE dethi SET makythi = ?, monthi = ?, tende = ?, thoigianthi = ?, tongsocau = ? WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t.getMakythi());
            pst.setInt(2, t.getMonthi());
            pst.setString(3, t.getTende());
            pst.setInt(4, t.getThoigianthi());
            pst.setInt(5, t.getTongsocau());
            pst.setInt(6, t.getMade());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int made) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE dethi SET trangthai = 0 WHERE made = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<DeThiDTO> selectAll() {
        ArrayList<DeThiDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM dethi WHERE trangthai = 1 ORDER BY made DESC";
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<CauHoiDTO> getDanhSachCauHoiByMade(int made) {
        ArrayList<CauHoiDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT ch.* FROM cauhoi ch JOIN chitietdethi ct ON ch.macauhoi = ct.macauhoi WHERE ct.made = ? ORDER BY ct.thutu ASC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, made);
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
