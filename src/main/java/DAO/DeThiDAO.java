package DAO;

import DTO.DeThiDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

public class DeThiDAO {

    public static DeThiDAO getInstance() {
        return new DeThiDAO();
    }

    public ArrayList<DeThiDTO> selectAll() {
        ArrayList<DeThiDTO> ketQua = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM dethi";
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
                dt.setTrangthai(rs.getBoolean("trangthai"));

                ketQua.add(dt);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}
