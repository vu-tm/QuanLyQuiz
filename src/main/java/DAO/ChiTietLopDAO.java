/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.ChiTietLopDTO;
import config.JDBCUtil;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Windows
 */
public class ChiTietLopDAO {
    
    public static ChiTietLopDAO getInstance(){
        return new ChiTietLopDAO();
    }
    
    public ArrayList<ChiTietLopDTO> selectByMaLop(int malop){
        ArrayList<ChiTietLopDTO> ketQua = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM chitietlop WHERE malop=? AND hienthi=1";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, malop);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                ChiTietLopDTO ct = new ChiTietLopDTO();
                ct.setMalop(rs.getInt("malop"));
                ct.setManguoidung(rs.getString("manguoidung"));
                ct.setHienthi(rs.getInt("hienthi"));
                ketQua.add(ct);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
    
    public ArrayList<ChiTietLopDTO> selectByMaNguoiDung(String manguoidung){
        ArrayList<ChiTietLopDTO> ketQua = new ArrayList<>();
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM chitietlop WHERE manguoidung=? AND hienthi=1";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, manguoidung);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                ChiTietLopDTO ct = new ChiTietLopDTO();
                ct.setMalop(rs.getInt("malop"));
                ct.setManguoidung(rs.getString("manguoidung"));
                ct.setHienthi(rs.getInt("hienthi"));
                ketQua.add(ct);
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
    
    public int insert(ChiTietLopDTO ct){
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "INSERT INTO chitietlop(malop, manguoidung, hienthi) VALUES(?,?,1)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, ct.getMalop());
            pst.setString(2, ct.getManguoidung());
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int delete(int malop, String manguoidung){
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "UPDATE chitietlop SET hienthi=0 WHERE malop=? AND manguoidung=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, malop);
            pst.setString(2, manguoidung);
            result = pst.executeUpdate();
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public boolean checkExists(int malop, String manguoidung){
        boolean result = false;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT * FROM chitietlop WHERE malop=? AND manguoidung=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, malop);
            pst.setString(2, manguoidung);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                result = true;
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int countByMaLop(int malop){
        int result = 0;
        try {
            Connection con = JDBCUtil.getConnection();
            String sql = "SELECT COUNT(*) as total FROM chitietlop WHERE malop=? AND hienthi=1";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, malop);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getInt("total");
            }
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}