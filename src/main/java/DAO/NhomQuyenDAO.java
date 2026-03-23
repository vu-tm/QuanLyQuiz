package DAO;

import DTO.NhomQuyenDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class NhomQuyenDAO {

    public static NhomQuyenDAO getInstance() {
        return new NhomQuyenDAO();
    }

    public int insert(NhomQuyenDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "INSERT INTO nhomquyen (tennhomquyen, trangthai) VALUES (?, 1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTennhomquyen());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int update(NhomQuyenDTO t) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE nhomquyen SET tennhomquyen = ? WHERE manhomquyen = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, t.getTennhomquyen());
            pst.setInt(2, t.getManhomquyen());
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int delete(int id) {
        int result = 0;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "UPDATE nhomquyen SET trangthai = 0 WHERE manhomquyen = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            result = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<NhomQuyenDTO> selectAll() {
        ArrayList<NhomQuyenDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM nhomquyen WHERE trangthai = 1 ORDER BY manhomquyen DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                NhomQuyenDTO nq = new NhomQuyenDTO();
                nq.setManhomquyen(rs.getInt("manhomquyen"));
                nq.setTennhomquyen(rs.getString("tennhomquyen"));
                result.add(nq);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public NhomQuyenDTO selectById(int t) {
        NhomQuyenDTO result = null;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM nhomquyen WHERE manhomquyen=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, t);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int manhomquyen = rs.getInt("manhomquyen");
                String tennhomquyen = rs.getString("tennhomquyen");
                result = new NhomQuyenDTO(manhomquyen, tennhomquyen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getAutoIncrement() {
        int result = -1;
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'quiz' AND TABLE_NAME = 'nhomquyen'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                result = rs.getInt("AUTO_INCREMENT");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
