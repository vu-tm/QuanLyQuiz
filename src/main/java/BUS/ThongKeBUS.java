package BUS;

import DAO.*;
import DTO.*;
import DTO.ThongKe.*;
import config.JDBCUtil;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongKeBUS {

    private static final int MA_QUYEN_HOC_SINH = 3;

    private static ArrayList<Object[]> loadAllBaiThi() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT mabaithi, manguoidung, diemthi, thoigianvaothi FROM baithi WHERE diemthi IS NOT NULL";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("thoigianvaothi");
                LocalDate ngay = ts != null ? ts.toLocalDateTime().toLocalDate() : null;
                list.add(new Object[]{
                    rs.getInt("mabaithi"), // [0]
                    rs.getInt("manguoidung"), // [1]
                    rs.getDouble("diemthi"), // [2]
                    ngay // [3]
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static ArrayList<Object[]> loadAllChiTietBaiThi() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT mabaithi, macauhoi, dapanchon FROM chitietbaithi";
        try (Connection con = JDBCUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("mabaithi"), // [0]
                    rs.getInt("macauhoi"), // [1]
                    rs.getInt("dapanchon") // [2]
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // TỔNG QUAN 
    public static int getTongSoDeThi() {
        return DeThiDAO.getInstance().selectAll().size();
    }

    public static int getTongSoCauHoi() {
        return new CauHoiDAO().getAll().size();
    }

    public static int getTongSoHocSinh() {
        int count = 0;
        for (NguoiDungDTO nd : NguoiDungDAO.getInstance().getAll()) {
            if (nd.getManhomquyen() == MA_QUYEN_HOC_SINH && nd.getTrangthai() == 1) {
                count++;
            }
        }
        return count;
    }

    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKe7NgayGanNhat() {
        return getThongKeDiemThiTuNgayDenNgay(
                Date.from(LocalDate.now().minusDays(6).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
    }

    // THỐNG KÊ CÂU HỎI
    public static ArrayList<ThongKeCauHoiDTO> getThongKeCauHoi() {
        ArrayList<ThongKeCauHoiDTO> result = new ArrayList<>();

        List<CauHoiDTO> listCH = new CauHoiDAO().getAll();
        ArrayList<DapAnDTO> listDA = new DapAnDAO().selectAll();
        ArrayList<Object[]> listCTBT = loadAllChiTietBaiThi();

        Map<Integer, Integer> mapDapAnDung = new HashMap<>();
        for (DapAnDTO da : listDA) {
            if (da.isLadapan()) {
                mapDapAnDung.put(da.getMacauhoi(), da.getMadapan());
            }
        }

        int stt = 1;
        for (CauHoiDTO ch : listCH) {
            if (ch.getTrangthai() != 1) {
                continue;
            }

            int maCH = ch.getMacauhoi();
            int maDADung = mapDapAnDung.getOrDefault(maCH, -1);
            int tongLan = 0, soDung = 0;

            for (Object[] ct : listCTBT) {
                if ((int) ct[1] == maCH) {
                    tongLan++;
                    if ((int) ct[2] == maDADung) {
                        soDung++;
                    }
                }
            }

            double tyleDung = tongLan > 0 ? (double) Math.round((double) soDung / tongLan * 1000) / 10 : 0;
            double tyleSai = tongLan > 0 ? (double) Math.round((100 - tyleDung) * 10) / 10 : 0;

            result.add(new ThongKeCauHoiDTO(stt++, maCH, ch.getNoidung(), tongLan, tyleDung, tyleSai));
        }
        return result;
    }

    // THỐNG KÊ HỌC SINH
    public static ArrayList<ThongKeHocSinhDTO> getThongKeHocSinh() {
        ArrayList<ThongKeHocSinhDTO> result = new ArrayList<>();
        List<NguoiDungDTO> listND = NguoiDungDAO.getInstance().getAll();
        ArrayList<Object[]> listBT = loadAllBaiThi();

        Map<Integer, Integer> mapSoLanThi = new HashMap<>();
        for (Object[] bt : listBT) {
            int manguoidung = (int) bt[1];
            mapSoLanThi.put(manguoidung, mapSoLanThi.getOrDefault(manguoidung, 0) + 1);
        }

        int stt = 1;
        for (NguoiDungDTO nd : listND) {
            if (nd.getManhomquyen() == MA_QUYEN_HOC_SINH && nd.getTrangthai() == 1) {
                int soDe = mapSoLanThi.getOrDefault(nd.getId(), 0);
                result.add(new ThongKeHocSinhDTO(stt++, nd.getId(), nd.getHoten(), soDe));
            }
        }

        result.sort((a, b) -> Integer.compare(b.getSoDedalLam(), a.getSoDedalLam()));
        return result;
    }

    // THỐNG KÊ ĐIỂM THI
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeDiemThiTuNgayDenNgay(Date start, Date end) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        ArrayList<Object[]> allBaiThi = loadAllBaiThi();

        LocalDate from = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate to = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            double max = 0, min = Double.MAX_VALUE, tong = 0;
            int count = 0;

            for (Object[] bt : allBaiThi) {
                LocalDate ngayThi = (LocalDate) bt[3];
                if (ngayThi != null && ngayThi.isEqual(date)) {
                    double diem = (double) bt[2];
                    if (diem > max) {
                        max = diem;
                    }
                    if (diem < min) {
                        min = diem;
                    }
                    tong += diem;
                    count++;
                }
            }

            Date d = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            result.add(new ThongKeTungNgayTrongThangDTO(d,
                    count > 0 ? max : 0,
                    count > 0 ? min : 0,
                    count > 0 ? tong / count : 0));
        }
        return result;
    }

    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeDiemThiTrongThang(int thang, int nam) {
        LocalDate from = LocalDate.of(nam, thang, 1);
        LocalDate to = from.plusMonths(1).minusDays(1);
        return getThongKeDiemThiTuNgayDenNgay(
                Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant())
        );
    }

    public static ArrayList<ThongKeTheoThangDTO> getThongKeDiemThiTungThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        ArrayList<Object[]> allBaiThi = loadAllBaiThi();

        for (int m = 1; m <= 12; m++) {
            double max = 0, min = Double.MAX_VALUE, tong = 0;
            int count = 0;

            for (Object[] bt : allBaiThi) {
                LocalDate ngay = (LocalDate) bt[3];
                if (ngay != null && ngay.getYear() == nam && ngay.getMonthValue() == m) {
                    double diem = (double) bt[2];
                    if (diem > max) {
                        max = diem;
                    }
                    if (diem < min) {
                        min = diem;
                    }
                    tong += diem;
                    count++;
                }
            }
            result.add(new ThongKeTheoThangDTO(m,
                    count > 0 ? max : 0,
                    count > 0 ? min : 0,
                    count > 0 ? tong / count : 0));
        }
        return result;
    }

    public static ArrayList<ThongKeDiemThiDTO> getThongKeDiemThiTheoNam(int namBD, int namKT) {
        ArrayList<ThongKeDiemThiDTO> result = new ArrayList<>();
        ArrayList<Object[]> allBaiThi = loadAllBaiThi();

        for (int nam = namBD; nam <= namKT; nam++) {
            double max = 0, min = Double.MAX_VALUE, tong = 0;
            int count = 0;

            for (Object[] bt : allBaiThi) {
                LocalDate ngay = (LocalDate) bt[3];
                if (ngay != null && ngay.getYear() == nam) {
                    double diem = (double) bt[2];
                    if (diem > max) {
                        max = diem;
                    }
                    if (diem < min) {
                        min = diem;
                    }
                    tong += diem;
                    count++;
                }
            }
            result.add(new ThongKeDiemThiDTO(nam,
                    count > 0 ? max : 0,
                    count > 0 ? min : 0,
                    count > 0 ? tong / count : 0));
        }
        return result;
    }
}
