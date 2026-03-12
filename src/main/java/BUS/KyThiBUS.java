/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

/**
 *
 * @author Windows
 */
import DAO.KyThiDAO;
import DTO.KyThiDTO;
import java.util.ArrayList;

public class KyThiBUS {

    private final KyThiDAO kythiDAO = KyThiDAO.getInstance();
    private ArrayList<KyThiDTO> listKyThi;

    public KyThiBUS() {
        this.listKyThi = kythiDAO.selectAll();
    }

    public ArrayList<KyThiDTO> getAll() {
        this.listKyThi = kythiDAO.selectAll();
        return this.listKyThi;
    }

    public boolean add(KyThiDTO kt) {
        return kythiDAO.insert(kt) > 0;
    }
    
    public boolean update(KyThiDTO kt) {
        return kythiDAO.update(kt) > 0;
    }
    
    public boolean delete(int id) {
        return kythiDAO.delete(id) > 0;
    }
    
    public ArrayList<KyThiDTO> search(String text, String type) {
        ArrayList<KyThiDTO> result = new ArrayList<>();
        text = text.toLowerCase();

        for (KyThiDTO kt : this.listKyThi) {
            String maStr = Integer.toString(kt.getMakythi()).toLowerCase();
            String tenStr = kt.getTenkythi().toLowerCase();

            if (type.equals("Tất cả")) {
                if (maStr.contains(text) || tenStr.contains(text)) {
                    result.add(kt);
                }
            } else if (type.equals("Mã kỳ thi")) {
                if (maStr.contains(text)) {
                    result.add(kt);
                }
            } else if (type.equals("Tên kỳ thi")) {
                if (tenStr.contains(text)) {
                    result.add(kt);
                }
            }
        }
        return result;
    }
}
