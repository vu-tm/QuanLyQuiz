package DAO;

import DTO.LopDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class LopDAO {

    private static LopDAO instance;

    private LopDAO() {}

    public static LopDAO getInstance() {
        if (instance == null) {
            instance = new LopDAO();
        }
        return instance;
    }

    public ArrayList<LopDTO> selectAll() {
        ArrayList<LopDTO> ketQua = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM lop";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                LopDTO lop = new LopDTO();
                lop.setMalop(rs.getInt("malop"));
                lop.setTenlop(rs.getString("tenlop"));
                lop.setSiso(rs.getInt("siso"));
                lop.setNamhoc(rs.getInt("namhoc"));
                lop.setHocky(rs.getInt("hocky"));
                lop.setTrangthai(rs.getInt("trangthai"));
                lop.setGiangvien(rs.getString("giangvien"));
                lop.setMamonhoc(rs.getInt("mamonhoc"));
                ketQua.add(lop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public LopDTO selectById(int malop) {
        LopDTO lop = null;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM lop WHERE malop=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, malop);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lop = new LopDTO();
                lop.setMalop(rs.getInt("malop"));
                lop.setTenlop(rs.getString("tenlop"));
                lop.setSiso(rs.getInt("siso"));
                lop.setNamhoc(rs.getInt("namhoc"));
                lop.setHocky(rs.getInt("hocky"));
                lop.setTrangthai(rs.getInt("trangthai"));
                lop.setGiangvien(rs.getString("giangvien"));
                lop.setMamonhoc(rs.getInt("mamonhoc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lop;
    }

    public int insert(LopDTO lop) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO lop(tenlop, siso, namhoc, hocky, trangthai, giangvien, mamonhoc) VALUES(?,?,?,?,1,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, lop.getTenlop());
            pst.setInt(2, lop.getSiso());
            pst.setInt(3, lop.getNamhoc());
            pst.setInt(4, lop.getHocky());
            pst.setString(5, lop.getGiangvien());
            pst.setInt(6, lop.getMamonhoc());
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int update(LopDTO lop) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE lop SET tenlop=?, siso=?, namhoc=?, hocky=?, giangvien=?, mamonhoc=? WHERE malop=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, lop.getTenlop());
            pst.setInt(2, lop.getSiso());
            pst.setInt(3, lop.getNamhoc());
            pst.setInt(4, lop.getHocky());
            pst.setString(5, lop.getGiangvien());
            pst.setInt(6, lop.getMamonhoc());
            pst.setInt(7, lop.getMalop());
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int delete(int malop) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE lop SET trangthai=0 WHERE malop=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, malop);
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<LopDTO> selectByGiangVien(String magiangvien) {
        ArrayList<LopDTO> ketQua = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM lop WHERE giangvien=? AND trangthai=1";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, magiangvien);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                LopDTO lop = new LopDTO();
                lop.setMalop(rs.getInt("malop"));
                lop.setTenlop(rs.getString("tenlop"));
                lop.setSiso(rs.getInt("siso"));
                lop.setNamhoc(rs.getInt("namhoc"));
                lop.setHocky(rs.getInt("hocky"));
                lop.setTrangthai(rs.getInt("trangthai"));
                lop.setGiangvien(rs.getString("giangvien"));
                lop.setMamonhoc(rs.getInt("mamonhoc"));
                ketQua.add(lop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public ArrayList<LopDTO> selectByMonHoc(int mamonhoc) {
        ArrayList<LopDTO> ketQua = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM lop WHERE mamonhoc=? AND trangthai=1";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, mamonhoc);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                LopDTO lop = new LopDTO();
                lop.setMalop(rs.getInt("malop"));
                lop.setTenlop(rs.getString("tenlop"));
                lop.setSiso(rs.getInt("siso"));
                lop.setNamhoc(rs.getInt("namhoc"));
                lop.setHocky(rs.getInt("hocky"));
                lop.setTrangthai(rs.getInt("trangthai"));
                lop.setGiangvien(rs.getString("giangvien"));
                lop.setMamonhoc(rs.getInt("mamonhoc"));
                ketQua.add(lop);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}