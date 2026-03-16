package DAO;

import DTO.QuyenDTO;
import helper.DatabaseHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuyenDAO {

    public List<QuyenDTO> getAll() {
        List<QuyenDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM quyen ORDER BY maquyen";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                QuyenDTO q = new QuyenDTO();
                q.setMaquyen(rs.getInt("maquyen"));
                q.setChucnang(rs.getString("chucnang"));
                q.setTendoituong(rs.getString("tendoituong"));
                q.setHanhdong(rs.getString("hanhdong"));
                list.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}