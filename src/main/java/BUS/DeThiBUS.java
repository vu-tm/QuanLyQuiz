package BUS;

import DAO.ChiTietDeThiDAO;
import DAO.DeThiDAO;
import DTO.CauHoiDTO;
import DTO.ChiTietDeThiDTO;
import DTO.DeThiDTO;
import java.util.ArrayList;

public class DeThiBUS {

    private final DeThiDAO dethiDAO = DeThiDAO.getInstance();
    private final NguoiDungBUS ndBUS = new NguoiDungBUS();
    private final ChiTietDeThiDAO ctdtDAO = ChiTietDeThiDAO.getInstance();
    private ArrayList<DeThiDTO> listDeThi;

    public DeThiBUS() {
        this.listDeThi = dethiDAO.selectAll();
    }

    public ArrayList<DeThiDTO> getAll() {
        this.listDeThi = dethiDAO.selectAll();
        return this.listDeThi;
    }

    public boolean add(DeThiDTO dt) {
        boolean check = dethiDAO.insert(dt) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(DeThiDTO dt) {
        boolean check = dethiDAO.update(dt) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean delete(int made) {
        boolean check = dethiDAO.delete(made) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public ArrayList<DeThiDTO> search(String text, String type) {
        ArrayList<DeThiDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (int i = 0; i < this.listDeThi.size(); i++) {
            DeThiDTO dt = listDeThi.get(i);
            boolean match = false;

            String tenNguoiTao = ndBUS.getHotenById(dt.getNguoitao());
            if (tenNguoiTao == null) {
                tenNguoiTao = "";
            }
            String tenNguoiTaoLower = tenNguoiTao.toLowerCase();

            switch (type) {
                case "Tất cả":
                    match = Integer.toString(dt.getMade()).contains(text)
                            || dt.getTende().toLowerCase().contains(text)
                            || tenNguoiTaoLower.contains(text);
                    break;
                case "Mã đề":
                    match = Integer.toString(dt.getMade()).contains(text);
                    break;
                case "Tên đề":
                    match = dt.getTende().toLowerCase().contains(text);
                    break;
                case "Người tạo":
                    match = tenNguoiTaoLower.contains(text);
                    break;
            }
            if (match) {
                result.add(dt);
            }
        }
        return result;
    }

    public void saveChiTiet(int made, ArrayList<Integer> listMaCauHoi) {
        ctdtDAO.deleteByMade(made);
        for (int i = 0; i < listMaCauHoi.size(); i++) {
            ChiTietDeThiDTO ct = new ChiTietDeThiDTO(made, listMaCauHoi.get(i), i + 1);
            ctdtDAO.insert(ct);
        }
    }

    public ArrayList<Integer> getMaCauHoiByMade(int made) {
        ArrayList<ChiTietDeThiDTO> list = ctdtDAO.selectByMade(made);
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            result.add(list.get(i).getMacauhoi());
        }
        return result;
    }

    public ArrayList<CauHoiDTO> getDanhSachCauHoiByMade(int made) {
        return dethiDAO.getDanhSachCauHoiByMade(made);
    }

    public DeThiDTO getById(int made) {
        for (int i = 0; i < listDeThi.size(); i++) {
            if (listDeThi.get(i).getMade() == made) {
                return listDeThi.get(i);
            }
        }
        return null;
    }

    public ArrayList<DeThiDTO> getDeThiChoSinhVien(int manguoidung) {
        LopBUS lopBus = new LopBUS();
        ArrayList<Integer> listMaLop = lopBus.getListMaLopByUser(manguoidung);

        DAO.GiaoDeThiDAO giaoDeThiDAO = DAO.GiaoDeThiDAO.getInstance();
        java.util.HashSet<Integer> setMaDe = new java.util.HashSet<>();

        for (int malop : listMaLop) {
            ArrayList<Integer> dsMaDeCuaLop = giaoDeThiDAO.getMaDeByMaLop(malop);
            setMaDe.addAll(dsMaDeCuaLop);
        }
        ArrayList<DeThiDTO> result = new ArrayList<>();
        for (int made : setMaDe) {
            DeThiDTO dt = getById(made);
            if (dt != null && dt.isTrangthai()) {
                result.add(dt);
            }
        }
        return result;
    }
}
