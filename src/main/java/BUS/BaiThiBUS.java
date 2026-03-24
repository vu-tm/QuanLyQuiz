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

    public ArrayList<BaiThiDTO> search(String text, String type, ArrayList<BaiThiDTO> list) {
        ArrayList<BaiThiDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (BaiThiDTO bt : list) {
            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = String.valueOf(bt.getMabaithi()).contains(text)
                            || String.valueOf(bt.getMade()).contains(text)
                            || String.valueOf(bt.getManguoidung()).contains(text);
                    break;
                case "Mã bài thi":
                    match = String.valueOf(bt.getMabaithi()).contains(text);
                    break;
                case "Mã đề":
                    match = String.valueOf(bt.getMade()).contains(text);
                    break;
                case "Mã người dùng":
                    match = String.valueOf(bt.getManguoidung()).contains(text);
                    break;
            }
            if (match) {
                result.add(bt);
            }
        }
        return result;
    }

    public ArrayList<BaiThiDTO> getByUser(int userId) {
        return bDao.selectByUserId(userId);
    }

    public int addChiTiet(ChiTietBaiThiDTO ct) {
        return ctDao.insert(ct);
    }

    public double gradeAndSave(DeThiDTO deThi, int userId, ArrayList<String> answers, int timeSpent) {
        DeThiBUS deThiBUS = new DeThiBUS();
        DapAnBUS dapAnBUS = new DapAnBUS();
        ArrayList<CauHoiDTO> dsCauHoi = deThiBUS.getDanhSachCauHoiByMade(deThi.getMade());

        int soCauDung = 0;
        for (int i = 0; i < dsCauHoi.size(); i++) {
            CauHoiDTO ch = dsCauHoi.get(i);
            ArrayList<DapAnDTO> listDA = dapAnBUS.getDapAnDayDu(ch.getMacauhoi());
            String userAnswer = i < answers.size() ? answers.get(i) : "";

            if (userAnswer == null || userAnswer.trim().isEmpty()) {
                continue;
            }

            if (ch.getMaloai() == 1 || ch.getMaloai() == 2) {
                try {
                    int chosenId = Integer.parseInt(userAnswer);
                    for (DapAnDTO da : listDA) {
                        if (da.isLadapan() && da.getMadapan() == chosenId) {
                            soCauDung++;
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                }
            } else if (ch.getMaloai() == 3) {
                for (DapAnDTO da : listDA) {
                    if (da.isLadapan() && da.getNoidungtl().trim().equalsIgnoreCase(userAnswer.trim())) {
                        soCauDung++;
                        break;
                    }
                }
            }
        }

        int tongSoCau = dsCauHoi.size();
        double diem = tongSoCau > 0 ? (double) soCauDung / tongSoCau * 10.0 : 0;
        diem = Math.round(diem * 100.0) / 100.0;

        BaiThiDTO bt = new BaiThiDTO();
        bt.setMade(deThi.getMade());
        bt.setManguoidung(userId);
        bt.setDiemthi(diem);
        bt.setThoigianvaothi(new Timestamp(System.currentTimeMillis()));
        bt.setThoigianlambai(timeSpent);
        bt.setSocaudung(soCauDung);
        bt.setSocausai(tongSoCau - soCauDung);

        int maBaiThi = add(bt);
        if (maBaiThi > 0) {
            for (int i = 0; i < dsCauHoi.size(); i++) {
                CauHoiDTO ch = dsCauHoi.get(i);
                String userAnswer = i < answers.size() ? answers.get(i) : "";

                ChiTietBaiThiDTO ct = new ChiTietBaiThiDTO();
                ct.setMabaithi(maBaiThi);
                ct.setMacauhoi(ch.getMacauhoi());

                if ((ch.getMaloai() == 1 || ch.getMaloai() == 2) && !userAnswer.isEmpty()) {
                    try {
                        ct.setDapanchon(Integer.parseInt(userAnswer));
                    } catch (NumberFormatException e) {
                        ct.setDapanchon(0);
                    }
                    ct.setNoidungdienkhuyet(null);
                } // Lưu cho Điền khuyết
                else if (ch.getMaloai() == 3) {
                    ct.setDapanchon(0);
                    ct.setNoidungdienkhuyet(userAnswer);
                }
                addChiTiet(ct);
            }
        }
        return diem;
    }

    public String evaluateAnswer(ChiTietBaiThiDTO ct) {
        CauHoiBUS cauHoiBUS = new CauHoiBUS();
        DapAnBUS dapAnBUS = new DapAnBUS();
        CauHoiDTO ch = cauHoiBUS.getById(ct.getMacauhoi());
        if (ch == null) {
            return "Sai";
        }

        // Kiểm tra xem có làm bài không
        if (ct.getDapanchon() == 0 && (ct.getNoidungdienkhuyet() == null || ct.getNoidungdienkhuyet().trim().isEmpty())) {
            return "Chưa làm";
        }

        ArrayList<DapAnDTO> listDA = dapAnBUS.getDapAnDayDu(ch.getMacauhoi());

        // Trắc nghiệm (1) & Đúng sai (2)
        if (ch.getMaloai() == 1 || ch.getMaloai() == 2) {
            for (DapAnDTO da : listDA) {
                if (da.isLadapan() && da.getMadapan() == ct.getDapanchon()) {
                    return "Đúng";
                }
            }
        } // Điền khuyết (3)
        else if (ch.getMaloai() == 3) {
            for (DapAnDTO da : listDA) {
                if (da.isLadapan() && da.getNoidungtl().trim().equalsIgnoreCase(ct.getNoidungdienkhuyet().trim())) {
                    return "Đúng";
                }
            }
        }
        return "Sai";
    }

    public String getAnswerText(ChiTietBaiThiDTO ct) {
        CauHoiBUS cauHoiBUS = new CauHoiBUS();
        DapAnBUS dapAnBUS = new DapAnBUS();
        CauHoiDTO ch = cauHoiBUS.getById(ct.getMacauhoi());

        if (ch == null) {
            return "N/A";
        }

        if (ct.getDapanchon() == 0 && (ct.getNoidungdienkhuyet() == null || ct.getNoidungdienkhuyet().trim().isEmpty())) {
            return "Chưa chọn";
        }
        if (ch.getMaloai() == 1 || ch.getMaloai() == 2) {
            ArrayList<DapAnDTO> listDA = dapAnBUS.getDapAnDayDu(ch.getMacauhoi());
            for (DapAnDTO da : listDA) {
                if (da.getMadapan() == ct.getDapanchon()) {
                    return da.getNoidungtl();
                }
            }
        } else if (ch.getMaloai() == 3) {
            return ct.getNoidungdienkhuyet();
        }
        return "N/A";
    }
}
