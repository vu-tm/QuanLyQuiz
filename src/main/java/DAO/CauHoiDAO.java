package DAO;

import DTO.CauHoiDTO;
import config.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CauHoiDAO {

    public List<CauHoiDTO> getAll() {
        List<CauHoiDTO> list = new ArrayList<>();
        String sql = "SELECT macauhoi, noidung, madokho, maloai, mamonhoc, nguoitao, trangthai FROM cauhoi";
        try (Connection c = JDBCUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CauHoiDTO ch = new CauHoiDTO(
                        rs.getInt("macauhoi"),
                        rs.getString("noidung"),
                        rs.getInt("madokho"),
                        rs.getInt("maloai"),
                        rs.getInt("mamonhoc"),
                        rs.getString("nguoitao"),
                        rs.getInt("trangthai")
                );
                list.add(ch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(CauHoiDTO ch) {
        String getIdSql = "SELECT COALESCE(MAX(macauhoi),0)+1 AS nextId FROM cauhoi";
        String insertSql = "INSERT INTO cauhoi(macauhoi,noidung,madokho,maloai,mamonhoc,nguoitao,trangthai) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement psId = c.prepareStatement(getIdSql);
             ResultSet rs = psId.executeQuery()) {
            int nextId = 1;
            if (rs.next()) nextId = rs.getInt("nextId");
            try (PreparedStatement ps = c.prepareStatement(insertSql)) {
                ps.setInt(1, nextId);
                ps.setString(2, ch.getNoidung());
                ps.setInt(3, ch.getMadokho());
                ps.setInt(4, ch.getMaloai());
                ps.setInt(5, ch.getMamonhoc());
                ps.setString(6, ch.getNguoitao());
                ps.setInt(7, ch.getTrangthai());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(CauHoiDTO ch) {
        String sql = "UPDATE cauhoi SET noidung = ?, madokho = ?, maloai = ?, mamonhoc = ?, nguoitao = ?, trangthai = ? WHERE macauhoi = ?";
        try (Connection c = JDBCUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ch.getNoidung());
            ps.setInt(2, ch.getMadokho());
            ps.setInt(3, ch.getMaloai());
            ps.setInt(4, ch.getMamonhoc());
            ps.setString(5, ch.getNguoitao());
            ps.setInt(6, ch.getTrangthai());
            ps.setInt(7, ch.getMacauhoi());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int macauhoi) {
        String sql = "DELETE FROM cauhoi WHERE macauhoi = ?";
        try (Connection c = JDBCUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, macauhoi);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CauHoiDTO> search(String keyword) {
        List<CauHoiDTO> list = new ArrayList<>();
        String sql = "SELECT macauhoi, noidung, madokho, maloai, mamonhoc, nguoitao, trangthai FROM cauhoi WHERE noidung LIKE ? OR nguoitao LIKE ?";
        try (Connection c = JDBCUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CauHoiDTO ch = new CauHoiDTO(
                            rs.getInt("macauhoi"),
                            rs.getString("noidung"),
                            rs.getInt("madokho"),
                            rs.getInt("maloai"),
                            rs.getInt("mamonhoc"),
                            rs.getString("nguoitao"),
                            rs.getInt("trangthai")
                    );
                    list.add(ch);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
