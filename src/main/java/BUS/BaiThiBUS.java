package BUS;

import DAO.BaiThiDAO;
import DAO.ChiTietBaiThiDAO;
import DTO.BaiThiDTO;
import DTO.CauHoiDTO;
import DTO.ChiTietBaiThiDTO;
import DTO.DapAnDTO;
import DTO.DeThiDTO;
import java.sql.Timestamp;
import java.util.ArrayList;

public class BaiThiBUS {
    private ArrayList<BaiThiDTO> listBaiThi;
    private BaiThiDAO bDao = BaiThiDAO.getInstance();
    private ChiTietBaiThiDAO ctDao = ChiTietBaiThiDAO.getInstance();

    public BaiThiBUS() {
        this.listBaiThi = bDao.selectAll();
    }

    public ArrayList<BaiThiDTO> getAll() {
        return bDao.selectAll();
    }

    public BaiThiDTO getById(int mabaithi) {
        return bDao.selectById(mabaithi);
    }

    public ArrayList<ChiTietBaiThiDTO> getChiTietByMaBaiThi(int mabaithi) {
        return ctDao.selectAll(mabaithi);
    }

    public int add(BaiThiDTO bt) {
        int result = bDao.insert(bt);
        if (result > 0) {
            bt.setMabaithi(result);
            listBaiThi.add(bt);
        }
        return result;
    }

    public int delete(int mabaithi) {
        int result = bDao.delete(mabaithi);
        if (result > 0) {
            listBaiThi.removeIf(bt -> bt.getMabaithi() == mabaithi);
        }
        return result;
    }

    public ArrayList<BaiThiDTO> search(String keyword) {
        ArrayList<BaiThiDTO> result = new ArrayList<>();
        for (BaiThiDTO bt : listBaiThi) {
            // Có thể mở rộng search theo ID người dùng hoặc mã đề
            if (bt.getManguoidung().toLowerCase().contains(keyword.toLowerCase()) || 
                String.valueOf(bt.getMabaithi()).contains(keyword)) {
                result.add(bt);
            }
        }
        return result;
    }

    public boolean hasTakenExam(String userId, int made) {
        ArrayList<BaiThiDTO> list = getByUser(userId);
        for (BaiThiDTO bt : list) {
            if (bt.getMade() == made) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<BaiThiDTO> getByUser(String userId) {
        return bDao.selectByUserId(userId);
    }

    public int addChiTiet(ChiTietBaiThiDTO ct) {
        return ctDao.insert(ct);
    }

    public double gradeAndSave(DeThiDTO deThi, String userId, ArrayList<String> answers, int timeSpent) {
        DeThiBUS deThiBUS = new DeThiBUS();
        DapAnBUS dapAnBUS = new DapAnBUS();
        ArrayList<CauHoiDTO> dsCauHoi = deThiBUS.getDanhSachCauHoiByMade(deThi.getMade());
        
        int soCauDung = 0;
        for (int i = 0; i < dsCauHoi.size(); i++) {
            CauHoiDTO ch = dsCauHoi.get(i);
            ArrayList<DapAnDTO> listDA = dapAnBUS.getByMaCauHoi(ch.getMacauhoi());
            String userAnswer = i < answers.size() ? answers.get(i) : "";
            
            if (userAnswer == null || userAnswer.trim().isEmpty()) continue;

            if (ch.getMaloai() == 1) { // Trắc nghiệm
                try {
                    int chosenId = Integer.parseInt(userAnswer);
                    for (DapAnDTO da : listDA) {
                        if (da.isLadapan() && da.getMadapan() == chosenId) {
                            soCauDung++;
                            break;
                        }
                    }
                } catch (NumberFormatException e) {}
            } else if (ch.getMaloai() == 2) { // Điền khuyết
                for (DapAnDTO da : listDA) {
                    if (da.isLadapan() && da.getNoidungtl().trim().equalsIgnoreCase(userAnswer.trim())) {
                        soCauDung++;
                        break;
                    }
                }
            }
        }
        
        double diem = (double) soCauDung * 10 / dsCauHoi.size();
        diem = Math.round(diem * 100.0) / 100.0;
        
        BaiThiDTO bt = new BaiThiDTO();
        bt.setMade(deThi.getMade());
        bt.setManguoidung(userId);
        bt.setDiemthi(diem);
        bt.setThoigianvaothi(new Timestamp(System.currentTimeMillis()));
        bt.setThoigianlambai(timeSpent);
        bt.setSocaudung(soCauDung);
        bt.setSocausai(dsCauHoi.size() - soCauDung);
        
        int maBaiThi = add(bt);
        if (maBaiThi > 0) {
            for (int i = 0; i < dsCauHoi.size(); i++) {
                CauHoiDTO ch = dsCauHoi.get(i);
                String userAnswer = i < answers.size() ? answers.get(i) : "";
                
                ChiTietBaiThiDTO ct = new ChiTietBaiThiDTO();
                ct.setMabaithi(maBaiThi);
                ct.setMacauhoi(ch.getMacauhoi());
                
                if (ch.getMaloai() == 1 && !userAnswer.isEmpty()) {
                    ct.setDapanchon(Integer.parseInt(userAnswer));
                    ct.setNoidungdienkhuyet(null);
                } else if (ch.getMaloai() == 2) {
                    ct.setDapanchon(0);
                    ct.setNoidungdienkhuyet(userAnswer);
                }
                addChiTiet(ct);
            }
        }
        return diem;
    }
}
