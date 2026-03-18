/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.ChiTietLopDAO;
import DTO.ChiTietLopDTO;
import java.util.ArrayList;

/**
 *
 * @author Windows
 */
public class ChiTietLopBUS {
    private final ChiTietLopDAO chitietlopDAO = ChiTietLopDAO.getInstance();
    private ArrayList<ChiTietLopDTO> listChiTietLop;
    
    public ChiTietLopBUS() {
    }
    
    public ArrayList<ChiTietLopDTO> getByMaLop(int malop) {
        this.listChiTietLop = chitietlopDAO.selectByMaLop(malop);
        return this.listChiTietLop;
    }
    
    public ArrayList<ChiTietLopDTO> getByMaNguoiDung(String manguoidung) {
        this.listChiTietLop = chitietlopDAO.selectByMaNguoiDung(manguoidung);
        return this.listChiTietLop;
    }
    
    public boolean add(ChiTietLopDTO ct) {
        if (chitietlopDAO.checkExists(ct.getMalop(), ct.getManguoidung())) {
            return false;
        }
        return chitietlopDAO.insert(ct) > 0;
    }
    
    public boolean delete(int malop, String manguoidung) {
        return chitietlopDAO.delete(malop, manguoidung) > 0;
    }
    
    public boolean checkExists(int malop, String manguoidung) {
        return chitietlopDAO.checkExists(malop, manguoidung);
    }
    
    public int countByMaLop(int malop) {
        return chitietlopDAO.countByMaLop(malop);
    }
}