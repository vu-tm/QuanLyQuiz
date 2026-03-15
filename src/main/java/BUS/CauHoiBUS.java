package BUS;

import DAO.CauHoiDAO;
import DTO.CauHoiDTO;
import java.util.ArrayList;

public class CauHoiBUS {

    private final CauHoiDAO chDAO = CauHoiDAO.getInstance();
    private ArrayList<CauHoiDTO> listCauHoi;

    public CauHoiBUS() {
        this.listCauHoi = chDAO.selectAll();
    }

    public ArrayList<CauHoiDTO> getAll() {
        this.listCauHoi = chDAO.selectAll();
        return this.listCauHoi;
    }

    public ArrayList<Integer> getMaCauHoiByMaDe(int made) {
        return chDAO.selectMaCauHoiByMaDe(made);
    }

    public ArrayList<CauHoiDTO> search(String text, int madokho, int mamonhoc) {
        ArrayList<CauHoiDTO> result = new ArrayList<>();
        String lower = text.toLowerCase();
        for (int i = 0; i < listCauHoi.size(); i++) {
            CauHoiDTO ch = listCauHoi.get(i);
            boolean matchText = lower.isEmpty() || ch.getNoidung().toLowerCase().contains(lower);
            boolean matchDoKho = (madokho == 0) || (ch.getMadokho() == madokho);
            boolean matchMon = (mamonhoc == 0) || (ch.getMamonhoc() == mamonhoc);
            
            if (matchText && matchDoKho && matchMon) {
                result.add(ch);
            }
        }
        return result;
    }
}