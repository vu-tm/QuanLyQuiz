package DAO;

import DTO.DapAnDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class DapAnDAO {

    public static DapAnDAO getInstance() {
        return new DapAnDAO();
    }

    public ArrayList<DapAnDTO> selectByMaCauHoi(int macauhoi) {
        ArrayList<DapAnDTO> result = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection()) {
            String sql = "SELECT * FROM dapan WHERE macauhoi = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, macauhoi);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                DapAnDTO da = new DapAnDTO();
                da.setMadapan(rs.getInt("madapan"));
                da.setMacauhoi(rs.getInt("macauhoi"));
                da.setNoidungtl(rs.getString("noidungtl"));
                da.setLadapan(rs.getBoolean("ladapan"));
                result.add(da);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
