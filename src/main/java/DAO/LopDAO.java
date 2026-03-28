package DAO;

import DTO.LopDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class LopDAO {

    public static LopDAO getInstance() {
        return new LopDAO();
    }

    public ArrayList<LopDTO> selectAll() {
        ArrayList<LopDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM lop WHERE trangthai=1 ORDER BY malop DESC";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                LopDTO lop = new LopDTO();
                lop.setMalop(rs.getInt("malop"));
                lop.setTenlop(rs.getString("tenlop"));
                lop.setSiso(rs.getInt("siso"));
                lop.setNamhoc(rs.getInt("namhoc"));
                lop.setHocky(rs.getInt("hocky"));
                lop.setTrangthai(rs.getInt("trangthai"));
                lop.setGiangvien(rs.getInt("giangvien"));
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
        String sql = "SELECT * FROM lop WHERE malop=?";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, malop);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    lop = new LopDTO();
                    lop.setMalop(rs.getInt("malop"));
                    lop.setTenlop(rs.getString("tenlop"));
                    lop.setSiso(rs.getInt("siso"));
                    lop.setNamhoc(rs.getInt("namhoc"));
                    lop.setHocky(rs.getInt("hocky"));
                    lop.setTrangthai(rs.getInt("trangthai"));
                    lop.setGiangvien(rs.getInt("giangvien"));
                    lop.setMamonhoc(rs.getInt("mamonhoc"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lop;
    }

    public int insert(LopDTO lop) {
        int result = 0;
        String sql = "INSERT INTO lop(tenlop, siso, namhoc, hocky, trangthai, giangvien, mamonhoc) VALUES(?,?,?,?,1,?,?)";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, lop.getTenlop());
            pst.setInt(2, lop.getSiso());
            pst.setInt(3, lop.getNamhoc());
            pst.setInt(4, lop.getHocky());
            pst.setInt(5, lop.getGiangvien());
            pst.setInt(6, lop.getMamonhoc());
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int update(LopDTO lop) {
        int result = 0;
        String sql = "UPDATE lop SET tenlop=?, siso=?, namhoc=?, hocky=?, giangvien=?, mamonhoc=? WHERE malop=?";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, lop.getTenlop());
            pst.setInt(2, lop.getSiso());
            pst.setInt(3, lop.getNamhoc());
            pst.setInt(4, lop.getHocky());
            pst.setInt(5, lop.getGiangvien());
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
        String sql = "UPDATE lop SET trangthai=0 WHERE malop=?";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, malop);
            result = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<LopDTO> selectByGiangVien(int magiangvien) {
        ArrayList<LopDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM lop WHERE giangvien=? AND trangthai=1";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, magiangvien);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    LopDTO lop = new LopDTO(
                            rs.getInt("malop"), rs.getString("tenlop"), rs.getInt("siso"),
                            rs.getInt("namhoc"), rs.getInt("hocky"), rs.getInt("trangthai"),
                            rs.getInt("giangvien"), rs.getInt("mamonhoc")
                    );
                    ketQua.add(lop);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public ArrayList<LopDTO> selectBySinhVien(int masinhvien) {
        ArrayList<LopDTO> ketQua = new ArrayList<>();
        String sql = "SELECT l.* FROM lop l JOIN chitietlop ctl ON l.malop = ctl.malop "
                + "WHERE ctl.manguoidung = ? AND l.trangthai = 1";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, masinhvien);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    LopDTO lop = new LopDTO();
                    lop.setMalop(rs.getInt("malop"));
                    lop.setTenlop(rs.getString("tenlop"));
                    lop.setSiso(rs.getInt("siso"));
                    lop.setNamhoc(rs.getInt("namhoc"));
                    lop.setHocky(rs.getInt("hocky"));
                    lop.setTrangthai(rs.getInt("trangthai"));
                    lop.setGiangvien(rs.getInt("giangvien"));
                    lop.setMamonhoc(rs.getInt("mamonhoc"));
                    ketQua.add(lop);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }

    public ArrayList<LopDTO> selectByMonHoc(int mamonhoc) {
        ArrayList<LopDTO> ketQua = new ArrayList<>();
        String sql = "SELECT * FROM lop WHERE mamonhoc=? AND trangthai=1";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, mamonhoc);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    LopDTO lop = new LopDTO(
                            rs.getInt("malop"), rs.getString("tenlop"), rs.getInt("siso"),
                            rs.getInt("namhoc"), rs.getInt("hocky"), rs.getInt("trangthai"),
                            rs.getInt("giangvien"), rs.getInt("mamonhoc")
                    );
                    ketQua.add(lop);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}
