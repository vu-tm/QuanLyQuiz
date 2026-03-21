package BUS;

import DAO.CauHoiDAO;
import DAO.DapAnDAO;
import DAO.DeThiDAO;
import DAO.NguoiDungDAO;
import DTO.CauHoiDTO;
import DTO.DapAnDTO;
import DTO.DeThiDTO;
import DTO.NguoiDungDTO;
import DTO.ThongKe.ThongKeCauHoiDTO;
import DTO.ThongKe.ThongKeDiemThiDTO;
import DTO.ThongKe.ThongKeHocSinhDTO;
import DTO.ThongKe.ThongKeTheoThangDTO;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import config.JDBCUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThongKeBUS {

    private static final int MA_QUYEN_HOC_SINH = 3;

    /** Load toàn bộ bài thi có điểm vào RAM */
    private static ArrayList<Object[]> loadAllBaiThi() {
        // Object[]: [mabaithi, manguoidung, diemthi, thoigianvaothi(LocalDate)]
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT mabaithi, manguoidung, diemthi, thoigianvaothi FROM baithi WHERE diemthi IS NOT NULL";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("thoigianvaothi");
                LocalDate ngay = ts != null ? ts.toLocalDateTime().toLocalDate() : null;
                list.add(new Object[]{
                    rs.getInt("mabaithi"),
                    rs.getString("manguoidung"),
                    rs.getDouble("diemthi"),
                    ngay
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Load toàn bộ chi tiết bài thi vào RAM */
    private static ArrayList<Object[]> loadAllChiTietBaiThi() {
        // Object[]: [mabaithi, macauhoi, dapanchon]
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT mabaithi, macauhoi, dapanchon FROM chitietbaithi";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("mabaithi"),
                    rs.getInt("macauhoi"),
                    rs.getInt("dapanchon")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==================== TONG QUAN ====================
    public static int getTongSoDeThi() {
        return DeThiDAO.getInstance().selectAll().size();
    }

    public static int getTongSoCauHoi() {
        return new CauHoiDAO().getAll().size();
    }

    public static int getTongSoHocSinh() {
        // Đếm số user có manhomquyen = hoc sinh và trangthai = 1
        int count = 0;
        for (NguoiDungDTO nd : NguoiDungDAO.getInstance().getAll()) {
            if (nd.getManhomquyen() == MA_QUYEN_HOC_SINH && nd.getTrangthai() == 1) {
                count++;
            }
        }
        return count;
    }

    /**
     * Thống kê điểm thi 7 ngày gần nhất - giống getThongKe7NgayGanNhat() code mẫu
     * Load bài thi vào RAM, vòng lặp Java để tính max/min/avg theo từng ngày
     */
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKe7NgayGanNhat() {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        ArrayList<Object[]> allBaiThi = loadAllBaiThi(); // Load 1 lần vào RAM

        LocalDate now = LocalDate.now();
        LocalDate past = now.minusDays(6); // 7 ngày gồm cả hôm nay

        for (LocalDate i = past; i.isBefore(now) || i.isEqual(now); i = i.plusDays(1)) {
            double cao = 0, thap = Double.MAX_VALUE, tongDiem = 0;
            int soLuong = 0;

            for (Object[] bt : allBaiThi) {
                LocalDate ngay = (LocalDate) bt[3];
                if (ngay != null && ngay.isEqual(i)) {
                    double diem = (double) bt[2];
                    if (diem > cao) {
                        cao = diem;
                    }
                    if (diem < thap) {
                        thap = diem;
                    }
                    tongDiem += diem;
                    soLuong++;
                }
            }

            double diemThap = soLuong > 0 ? thap : 0;
            double diemTB = soLuong > 0 ? tongDiem / soLuong : 0;
            double diemCao = soLuong > 0 ? cao : 0;

            Date d = Date.from(i.atStartOfDay(ZoneId.systemDefault()).toInstant());
            result.add(new ThongKeTungNgayTrongThangDTO(d, diemCao, diemThap, diemTB));
        }
        return result;
    }

    // ==================== CAU HOI ====================
    /**
     * Thống kê câu hỏi - giống getTonKho() code mẫu
     * Load câu hỏi + đáp án + chi tiết bài thi vào RAM, tính tỷ lệ đúng/sai bằng Java
     */
    public static ArrayList<ThongKeCauHoiDTO> getThongKeCauHoi() {
        ArrayList<ThongKeCauHoiDTO> result = new ArrayList<>();

        // Load tất cả vào RAM
        List<CauHoiDTO> danhSachCauHoi = new CauHoiDAO().getAll();
        ArrayList<DapAnDTO> danhSachDapAn = new DapAnDAO().selectAll(); // cần thêm hàm selectAll vào DapAnDAO
        ArrayList<Object[]> chiTietBaiThi = loadAllChiTietBaiThi();

        int stt = 1;
        for (CauHoiDTO ch : danhSachCauHoi) {
            if (ch.getTrangthai() != 1) {
                continue;
            }

            // Tìm madapan đúng của câu hỏi này
            int maDapAnDung = -1;
            for (DapAnDTO da : danhSachDapAn) {
                if (da.getMacauhoi() == ch.getMacauhoi() && da.isLadapan()) {
                    maDapAnDung = da.getMadapan();
                    break;
                }
            }

            // Đếm số lần thi và số lần đúng
            int tongLan = 0, soDung = 0;
            for (Object[] ct : chiTietBaiThi) {
                int maCauHoiCT = (int) ct[1];
                int dapAnChon = (int) ct[2];
                if (maCauHoiCT == ch.getMacauhoi()) {
                    tongLan++;
                    if (dapAnChon == maDapAnDung) {
                        soDung++;
                    }
                }
            }

            double tyleDung = tongLan > 0 ? Math.round((double) soDung / tongLan * 1000.0) / 10.0 : 0;
            double tyleSai = tongLan > 0 ? Math.round((100.0 - tyleDung) * 10.0) / 10.0 : 0;

            result.add(new ThongKeCauHoiDTO(stt++, ch.getMacauhoi(), ch.getNoidung(), tongLan, tyleDung, tyleSai));
        }
        return result;
    }

    // ==================== HOC SINH ====================
    public static ArrayList<ThongKeHocSinhDTO> getThongKeHocSinh() {
        ArrayList<ThongKeHocSinhDTO> result = new ArrayList<>();

        // Load vào RAM
        List<NguoiDungDTO> danhSachND = NguoiDungDAO.getInstance().getAll();
        ArrayList<Object[]> allBaiThi = loadAllBaiThi();

        int stt = 1;
        for (NguoiDungDTO nd : danhSachND) {
            // Chỉ lấy những người dùng là Học sinh và đang hoạt động
            if (nd.getManhomquyen() != MA_QUYEN_HOC_SINH || nd.getTrangthai() != 1) {
                continue;
            }

            int soDe = 0;
            for (Object[] bt : allBaiThi) {
                String maNguoiDung = (String) bt[1];
                // Đếm tất cả bài thi của học sinh này (không check ngày)
                if (nd.getId().equals(maNguoiDung)) {
                    soDe++;
                }
            }
            result.add(new ThongKeHocSinhDTO(stt++, nd.getId(), nd.getHoten(), soDe));
        }

        // Sắp xếp giảm dần theo số đề đã làm
        result.sort((a, b) -> Integer.compare(b.getSoDedalLam(), a.getSoDedalLam()));
        return result;
    }
    // ==================== DIEM THI ====================

    /**
     * Thống kê điểm từng ngày trong tháng - giống getThongKeTungNgayTrongThang() code mẫu
     */
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeDiemThiTrongThang(int thang, int nam) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        ArrayList<Object[]> allBaiThi = loadAllBaiThi();

        LocalDate from = LocalDate.of(nam, thang, 1);
        LocalDate to = from.plusMonths(1).minusDays(1);

        for (LocalDate i = from; i.isBefore(to) || i.isEqual(to); i = i.plusDays(1)) {
            double cao = 0, thap = Double.MAX_VALUE, tongDiem = 0;
            int soLuong = 0;

            for (Object[] bt : allBaiThi) {
                LocalDate ngay = (LocalDate) bt[3];
                if (ngay != null && ngay.isEqual(i)) {
                    double diem = (double) bt[2];
                    if (diem > cao) {
                        cao = diem;
                    }
                    if (diem < thap) {
                        thap = diem;
                    }
                    tongDiem += diem;
                    soLuong++;
                }
            }

            double diemThap = soLuong > 0 ? thap : 0;
            double diemTB = soLuong > 0 ? tongDiem / soLuong : 0;
            double diemCao = soLuong > 0 ? cao : 0;

            Date d = Date.from(i.atStartOfDay(ZoneId.systemDefault()).toInstant());
            result.add(new ThongKeTungNgayTrongThangDTO(d, diemCao, diemThap, diemTB));
        }
        return result;
    }

    /**
     * Thống kê điểm từng tháng trong năm - giống getThongKeTheoThang() code mẫu
     */
    public static ArrayList<ThongKeTheoThangDTO> getThongKeDiemThiTungThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        ArrayList<Object[]> allBaiThi = loadAllBaiThi();

        LocalDate start = LocalDate.of(nam, 1, 1);
        LocalDate end = LocalDate.of(nam, 12, 31);

        for (LocalDate i = start; i.isBefore(end) || i.isEqual(end); i = i.plusMonths(1)) {
            LocalDate lastDayOfMonth = i.plusMonths(1).minusDays(1);
            double cao = 0, thap = Double.MAX_VALUE, tongDiem = 0;
            int soLuong = 0;

            for (Object[] bt : allBaiThi) {
                LocalDate ngay = (LocalDate) bt[3];
                // Giống code mẫu: isEqual(i) || isEqual(lastDay) || (isBefore(lastDay) && isAfter(i))
                if (ngay != null && (ngay.isEqual(i) || ngay.isEqual(lastDayOfMonth)
                        || (ngay.isBefore(lastDayOfMonth) && ngay.isAfter(i)))) {
                    double diem = (double) bt[2];
                    if (diem > cao) {
                        cao = diem;
                    }
                    if (diem < thap) {
                        thap = diem;
                    }
                    tongDiem += diem;
                    soLuong++;
                }
            }

            double diemThap = soLuong > 0 ? thap : 0;
            double diemTB = soLuong > 0 ? tongDiem / soLuong : 0;
            double diemCao = soLuong > 0 ? cao : 0;

            result.add(new ThongKeTheoThangDTO(i.getMonthValue(), diemCao, diemThap, diemTB));
        }
        return result;
    }

    /**
     * Thống kê điểm theo năm - giống getThongKeTheoNam() code mẫu
     * Gọi lại getThongKeDiemThiTungThang() cho mỗi năm rồi tổng hợp
     */
    public static ArrayList<ThongKeDiemThiDTO> getThongKeDiemThiTheoNam(int namBD, int namKT) {
        ArrayList<ThongKeDiemThiDTO> result = new ArrayList<>();
        ArrayList<Object[]> allBaiThi = loadAllBaiThi(); // Load 1 lần dùng cho tất cả năm

        for (int nam = namBD; nam <= namKT; nam++) {
            double cao = 0, thap = Double.MAX_VALUE, tongDiem = 0;
            int soLuong = 0;

            LocalDate startOfYear = LocalDate.of(nam, 1, 1);
            LocalDate endOfYear = LocalDate.of(nam, 12, 31);

            for (Object[] bt : allBaiThi) {
                LocalDate ngay = (LocalDate) bt[3];
                if (ngay != null && !ngay.isBefore(startOfYear) && !ngay.isAfter(endOfYear)) {
                    double diem = (double) bt[2];
                    if (diem > cao) {
                        cao = diem;
                    }
                    if (diem < thap) {
                        thap = diem;
                    }
                    tongDiem += diem;
                    soLuong++;
                }
            }

            double diemThap = soLuong > 0 ? thap : 0;
            double diemTB = soLuong > 0 ? tongDiem / soLuong : 0;
            double diemCao = soLuong > 0 ? cao : 0;

            result.add(new ThongKeDiemThiDTO(nam, diemCao, diemThap, diemTB));
        }
        return result;
    }

    /**
     * Thống kê điểm từ ngày đến ngày - giống getThongKeTuNgayDenNgay() code mẫu
     */
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeDiemThiTuNgayDenNgay(Date start, Date end) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        ArrayList<Object[]> allBaiThi = loadAllBaiThi();

        LocalDate from = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate to = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (LocalDate i = from; i.isBefore(to) || i.isEqual(to); i = i.plusDays(1)) {
            double cao = 0, thap = Double.MAX_VALUE, tongDiem = 0;
            int soLuong = 0;

            for (Object[] bt : allBaiThi) {
                LocalDate ngay = (LocalDate) bt[3];
                if (ngay != null && ngay.isEqual(i)) {
                    double diem = (double) bt[2];
                    if (diem > cao) {
                        cao = diem;
                    }
                    if (diem < thap) {
                        thap = diem;
                    }
                    tongDiem += diem;
                    soLuong++;
                }
            }

            double diemThap = soLuong > 0 ? thap : 0;
            double diemTB = soLuong > 0 ? tongDiem / soLuong : 0;
            double diemCao = soLuong > 0 ? cao : 0;

            Date d = Date.from(i.atStartOfDay(ZoneId.systemDefault()).toInstant());
            result.add(new ThongKeTungNgayTrongThangDTO(d, diemCao, diemThap, diemTB));
        }
        return result;
    }
}
