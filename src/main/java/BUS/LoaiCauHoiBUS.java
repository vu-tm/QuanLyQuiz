package BUS;

import DAO.LoaiCauHoiDAO;
import DTO.LoaiCauHoiDTO;
import java.util.ArrayList;

public class LoaiCauHoiBUS {

    private final LoaiCauHoiDAO loaiDAO = LoaiCauHoiDAO.getInstance();
    private ArrayList<LoaiCauHoiDTO> listLoai;

    public static final String LOAI_TRAC_NGHIEM = "Trắc nghiệm";
    public static final String LOAI_DIEN_KHUYET = "Điền khuyết";
    public static final String LOAI_DUNG_SAI = "Đúng sai";
    public static final String[] DS_LOAI_HO_TRO = {LOAI_TRAC_NGHIEM, LOAI_DIEN_KHUYET, LOAI_DUNG_SAI};

    public LoaiCauHoiBUS() {
        this.listLoai = filterSupportedLoai(loaiDAO.selectAll());
    }

    public ArrayList<LoaiCauHoiDTO> getAll() {
        this.listLoai = filterSupportedLoai(loaiDAO.selectAll());
        return this.listLoai;
    }

    public boolean add(LoaiCauHoiDTO lch) {
        String tenLoai = normalizeTenLoai(lch.getTenloai());
        if (!isSupportedLoai(tenLoai) || isExistedTenLoai(tenLoai, -1)) {
            return false;
        }
        lch.setTenloai(tenLoai);
        boolean check = loaiDAO.insert(lch) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean update(LoaiCauHoiDTO lch) {
        String tenLoai = normalizeTenLoai(lch.getTenloai());
        if (!isSupportedLoai(tenLoai) || isExistedTenLoai(tenLoai, lch.getMaloai())) {
            return false;
        }
        lch.setTenloai(tenLoai);
        boolean check = loaiDAO.update(lch) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public boolean delete(int maloai) {
        boolean check = loaiDAO.delete(maloai) > 0;
        if (check) {
            getAll();
        }
        return check;
    }

    public LoaiCauHoiDTO getById(int maloai) {
        for (int i = 0; i < listLoai.size(); i++) {
            if (listLoai.get(i).getMaloai() == maloai) {
                return listLoai.get(i);
            }
        }
        return null;
    }

    public String getTenById(int maloai) {
        for (int i = 0; i < listLoai.size(); i++) {
            if (listLoai.get(i).getMaloai() == maloai) {
                return normalizeTenLoai(listLoai.get(i).getTenloai());
            }
        }
        return "Không xác định";
    }

    public ArrayList<LoaiCauHoiDTO> search(String text, String type) {
        ArrayList<LoaiCauHoiDTO> result = new ArrayList<>();
        text = text.toLowerCase();
        for (int i = 0; i < this.listLoai.size(); i++) {
            LoaiCauHoiDTO lch = listLoai.get(i);
            String tenLoai = normalizeTenLoai(lch.getTenloai());
            boolean match = false;
            switch (type) {
                case "Tất cả":
                    match = Integer.toString(lch.getMaloai()).contains(text) || tenLoai.toLowerCase().contains(text);
                    break;
                case "Mã loại":
                    match = Integer.toString(lch.getMaloai()).contains(text);
                    break;
                case "Tên loại":
                    match = tenLoai.toLowerCase().contains(text);
                    break;
            }
            if (match) {
                lch.setTenloai(tenLoai);
                result.add(lch);
            }
        }
        return result;
    }

    public boolean isSupportedLoai(String tenLoai) {
        String normalized = normalizeTenLoai(tenLoai);
        for (String loai : DS_LOAI_HO_TRO) {
            if (loai.equalsIgnoreCase(normalized)) {
                return true;
            }
        }
        return false;
    }

    public String normalizeTenLoai(String tenLoai) {
        if (tenLoai == null) {
            return "";
        }
        String value = tenLoai.trim();
        if (value.equalsIgnoreCase("Trắc nghiệm") || value.equalsIgnoreCase("Trắc nghiệm (4 lựa chọn)")) {
            return LOAI_TRAC_NGHIEM;
        }
        if (value.equalsIgnoreCase("Trắc nghiệm 4 phương án")) {
            return LOAI_TRAC_NGHIEM;
        }
        if (value.equalsIgnoreCase("Điền khuyết")) {
            return LOAI_DIEN_KHUYET;
        }
        if (value.equalsIgnoreCase("Điền vào chỗ trống")) {
            return LOAI_DIEN_KHUYET;
        }
        if (value.equalsIgnoreCase("Đúng sai") || value.equalsIgnoreCase("Đúng/Sai")) {
            return LOAI_DUNG_SAI;
        }
        return value;
    }

    private boolean isExistedTenLoai(String tenLoai, int exceptId) {
        String normalized = normalizeTenLoai(tenLoai);
        for (int i = 0; i < listLoai.size(); i++) {
            LoaiCauHoiDTO current = listLoai.get(i);
            if (current.getMaloai() == exceptId) {
                continue;
            }
            if (normalizeTenLoai(current.getTenloai()).equalsIgnoreCase(normalized)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<LoaiCauHoiDTO> filterSupportedLoai(ArrayList<LoaiCauHoiDTO> src) {
        ArrayList<LoaiCauHoiDTO> filtered = new ArrayList<>();
        for (int i = 0; i < src.size(); i++) {
            LoaiCauHoiDTO current = src.get(i);
            String normalized = normalizeTenLoai(current.getTenloai());
            if (isSupportedLoai(normalized)) {
                current.setTenloai(normalized);
                filtered.add(current);
            }
        }
        return filtered;
    }
}
