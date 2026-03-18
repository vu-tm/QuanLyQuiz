/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.LopDAO;
import DTO.LopDTO;
import java.util.ArrayList;

/**
 *
 * @author Windows
 */
public class LopBUS {
    private final LopDAO lopDAO = LopDAO.getInstance();
    private ArrayList<LopDTO> listLop;
    
    public LopBUS() {
        this.listLop = lopDAO.selectAll();
    }
    
    public ArrayList<LopDTO> getAll() {
        this.listLop = lopDAO.selectAll();
        return this.listLop;
    }
    
    public LopDTO getById(int malop) {
        return lopDAO.selectById(malop);
    }
    
    public boolean add(LopDTO lop) {
        return lopDAO.insert(lop) > 0;
    }
    
    public boolean update(LopDTO lop) {
        return lopDAO.update(lop) > 0;
    }
    
    public boolean delete(int malop) {
        return lopDAO.delete(malop) > 0;
    }
    
    public ArrayList<LopDTO> getByGiangVien(String magiangvien) {
        return lopDAO.selectByGiangVien(magiangvien);
    }
    
    public ArrayList<LopDTO> getByMonHoc(int mamonhoc) {
        return lopDAO.selectByMonHoc(mamonhoc);
    }
    
    public ArrayList<LopDTO> search(String text, String type) {
        ArrayList<LopDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (LopDTO lop : this.listLop) {
            String maStr = Integer.toString(lop.getMalop()).toLowerCase();
            String tenStr = lop.getTenlop().toLowerCase();
            String namhocStr = Integer.toString(lop.getNamhoc()).toLowerCase();
            String hockyStr = Integer.toString(lop.getHocky()).toLowerCase();
            if (type.equals("Tất cả")) {
                if (maStr.contains(text) || tenStr.contains(text) || namhocStr.contains(text) || hockyStr.contains(text)) {
                    result.add(lop);
                }
            } else if (type.equals("Mã lớp")) {
                if (maStr.contains(text)) {
                    result.add(lop);
                }
            } else if (type.equals("Tên lớp")) {
                if (tenStr.contains(text)) {
                    result.add(lop);
                }
            } else if (type.equals("Năm học")) {
                if (namhocStr.contains(text)) {
                    result.add(lop);
                }
            } else if (type.equals("Học kỳ")) {
                if (hockyStr.contains(text)) {
                    result.add(lop);
                }
            }
        }
        return result;
    }
}