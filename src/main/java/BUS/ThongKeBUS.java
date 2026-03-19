package BUS;

import DAO.CauHoiDAO;
import DAO.ChiTietDeThiDAO;
import DAO.DeThiDAO;
import DAO.DapAnDAO;
import DAO.NguoiDungDAO;
import DTO.CauHoiDTO;
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

    // ==================== TONG QUAN ====================

    public static int getTongSoDeThi() {
        String sql = "SELECT COUNT(*) FROM dethi WHERE trangthai = 1";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public static int getTongSoCauHoi() {
        String sql = "SELECT COUNT(*) FROM cauhoi WHERE trangthai = 1";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public static int getTongSoHocSinh() {
        String sql = "SELECT COUNT(*) FROM nguoidung nd JOIN nhomquyen nq ON nd.manhomquyen = nq.manhomquyen " +
                     "WHERE LOWER(nq.tennhomquyen) LIKE '%hoc sinh%' AND nd.trangthai = 1";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        // fallback: count all active users
        String fallback = "SELECT COUNT(*) FROM nguoidung WHERE trangthai = 1";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(fallback);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    /** Lấy thống kê điểm thi 7 ngày gần nhất (mỗi ngày: cao nhất, thấp nhất, trung bình) */
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKe7NgayGanNhat() {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate past = now.minusDays(6);

        String sql = "SELECT DATE(thoigianvaothi) AS ngay, MAX(diemthi) AS cao, MIN(diemthi) AS thap, AVG(diemthi) AS tb " +
                     "FROM baithi WHERE diemthi IS NOT NULL AND DATE(thoigianvaothi) >= ? AND DATE(thoigianvaothi) <= ? " +
                     "GROUP BY DATE(thoigianvaothi)";

        ArrayList<ThongKeTungNgayTrongThangDTO> dbRows = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(past));
            ps.setDate(2, java.sql.Date.valueOf(now));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Date d = rs.getDate("ngay");
                dbRows.add(new ThongKeTungNgayTrongThangDTO(d, rs.getDouble("cao"), rs.getDouble("thap"), rs.getDouble("tb")));
            }
        } catch (Exception e) { e.printStackTrace(); }

        // Fill all 7 days (including days with no data -> 0)
        for (LocalDate i = past; i.isBefore(now) || i.isEqual(now); i = i.plusDays(1)) {
            java.sql.Date d = java.sql.Date.valueOf(i);
            boolean found = false;
            for (ThongKeTungNgayTrongThangDTO row : dbRows) {
                LocalDate rowDate = ((java.sql.Date) row.getNgayDate()).toLocalDate();
                if (rowDate.isEqual(i)) {
                    result.add(row);
                    found = true;
                    break;
                }
            }
            if (!found) {
                result.add(new ThongKeTungNgayTrongThangDTO(d, 0, 0, 0));
            }
        }
        return result;
    }

    // ==================== CAU HOI ====================

    public static ArrayList<ThongKeCauHoiDTO> getThongKeCauHoi() {
        ArrayList<ThongKeCauHoiDTO> result = new ArrayList<>();
        // Lấy câu hỏi + tỷ lệ đúng/sai từ baithi
        String sql = "SELECT ch.macauhoi, ch.noidung, " +
                     "COUNT(ctbt.macauhoi) AS tonglan, " +
                     "SUM(CASE WHEN da.ladapan = 1 AND ctbt.dapanchon = da.madapan THEN 1 ELSE 0 END) AS sodung " +
                     "FROM cauhoi ch " +
                     "LEFT JOIN chitietbaithi ctbt ON ch.macauhoi = ctbt.macauhoi " +
                     "LEFT JOIN dapan da ON da.macauhoi = ch.macauhoi " +
                     "WHERE ch.trangthai = 1 " +
                     "GROUP BY ch.macauhoi, ch.noidung";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int stt = 1;
            while (rs.next()) {
                int tonglan = rs.getInt("tonglan");
                int sodung = rs.getInt("sodung");
                double tyleDung = tonglan > 0 ? (double) sodung / tonglan * 100 : 0;
                double tyleSai = 100 - tyleDung;
                result.add(new ThongKeCauHoiDTO(
                        stt++,
                        rs.getInt("macauhoi"),
                        rs.getString("noidung"),
                        tonglan,
                        Math.round(tyleDung * 10.0) / 10.0,
                        Math.round(tyleSai * 10.0) / 10.0
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: lấy câu hỏi không có thống kê bài thi
            List<CauHoiDTO> list = new CauHoiDAO().getAll();
            int stt = 1;
            for (CauHoiDTO ch : list) {
                result.add(new ThongKeCauHoiDTO(stt++, ch.getMacauhoi(), ch.getNoidung(), 0, 0, 0));
            }
        }
        return result;
    }

    // ==================== HOC SINH ====================

    public static ArrayList<ThongKeHocSinhDTO> getThongKeHocSinh() {
        ArrayList<ThongKeHocSinhDTO> result = new ArrayList<>();
        // Lấy học sinh (role có "hoc sinh") + số đề đã làm
        String sql = "SELECT nd.id, nd.hoten, COUNT(DISTINCT bt.made) AS sode " +
                     "FROM nguoidung nd " +
                     "LEFT JOIN nhomquyen nq ON nd.manhomquyen = nq.manhomquyen " +
                     "LEFT JOIN baithi bt ON bt.manguoidung = nd.id " +
                     "WHERE nd.trangthai = 1 AND LOWER(nq.tennhomquyen) LIKE '%hoc sinh%' " +
                     "GROUP BY nd.id, nd.hoten " +
                     "ORDER BY sode DESC";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int stt = 1;
            while (rs.next()) {
                result.add(new ThongKeHocSinhDTO(stt++, rs.getString("id"), rs.getString("hoten"), rs.getInt("sode")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: lấy tất cả người dùng
            String fallback = "SELECT nd.id, nd.hoten, COUNT(DISTINCT bt.made) AS sode " +
                              "FROM nguoidung nd LEFT JOIN baithi bt ON bt.manguoidung = nd.id " +
                              "WHERE nd.trangthai = 1 GROUP BY nd.id, nd.hoten ORDER BY sode DESC";
            try (Connection con = JDBCUtil.getConnection();
                 PreparedStatement ps2 = con.prepareStatement(fallback);
                 ResultSet rs2 = ps2.executeQuery()) {
                int stt = 1;
                while (rs2.next()) {
                    result.add(new ThongKeHocSinhDTO(stt++, rs2.getString("id"), rs2.getString("hoten"), rs2.getInt("sode")));
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        return result;
    }

    // ==================== DIEM THI ====================

    /** Thống kê điểm theo năm (trả về từng năm) */
    public static ArrayList<ThongKeDiemThiDTO> getThongKeDiemThiTheoNam(int namBD, int namKT) {
        ArrayList<ThongKeDiemThiDTO> result = new ArrayList<>();
        String sql = "SELECT YEAR(thoigianvaothi) AS nam, MAX(diemthi) AS cao, MIN(diemthi) AS thap, AVG(diemthi) AS tb " +
                     "FROM baithi WHERE diemthi IS NOT NULL AND YEAR(thoigianvaothi) >= ? AND YEAR(thoigianvaothi) <= ? " +
                     "GROUP BY YEAR(thoigianvaothi) ORDER BY nam";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, namBD);
            ps.setInt(2, namKT);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new ThongKeDiemThiDTO(rs.getInt("nam"), rs.getDouble("cao"), rs.getDouble("thap"), rs.getDouble("tb")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        // Điền năm không có dữ liệu
        for (int y = namBD; y <= namKT; y++) {
            final int year = y;
            boolean found = result.stream().anyMatch(r -> r.getThoigian() == year);
            if (!found) result.add(new ThongKeDiemThiDTO(year, 0, 0, 0));
        }
        result.sort((a, b) -> Integer.compare(a.getThoigian(), b.getThoigian()));
        return result;
    }

    /** Thống kê điểm từng tháng trong năm */
    public static ArrayList<ThongKeTheoThangDTO> getThongKeDiemThiTungThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        String sql = "SELECT MONTH(thoigianvaothi) AS thang, MAX(diemthi) AS cao, MIN(diemthi) AS thap, AVG(diemthi) AS tb " +
                     "FROM baithi WHERE diemthi IS NOT NULL AND YEAR(thoigianvaothi) = ? " +
                     "GROUP BY MONTH(thoigianvaothi) ORDER BY thang";
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new ThongKeTheoThangDTO(rs.getInt("thang"), rs.getDouble("cao"), rs.getDouble("thap"), rs.getDouble("tb")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        // Fill 12 tháng
        for (int t = 1; t <= 12; t++) {
            final int thang = t;
            boolean found = result.stream().anyMatch(r -> r.getThang() == thang);
            if (!found) result.add(new ThongKeTheoThangDTO(thang, 0, 0, 0));
        }
        result.sort((a, b) -> Integer.compare(a.getThang(), b.getThang()));
        return result;
    }

    /** Thống kê điểm từng ngày trong tháng */
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeDiemThiTrongThang(int thang, int nam) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        LocalDate from = LocalDate.of(nam, thang, 1);
        LocalDate to = from.plusMonths(1).minusDays(1);

        String sql = "SELECT DATE(thoigianvaothi) AS ngay, MAX(diemthi) AS cao, MIN(diemthi) AS thap, AVG(diemthi) AS tb " +
                     "FROM baithi WHERE diemthi IS NOT NULL AND DATE(thoigianvaothi) >= ? AND DATE(thoigianvaothi) <= ? " +
                     "GROUP BY DATE(thoigianvaothi) ORDER BY ngay";

        ArrayList<ThongKeTungNgayTrongThangDTO> dbRows = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dbRows.add(new ThongKeTungNgayTrongThangDTO(rs.getDate("ngay"), rs.getDouble("cao"), rs.getDouble("thap"), rs.getDouble("tb")));
            }
        } catch (Exception e) { e.printStackTrace(); }

        for (LocalDate i = from; i.isBefore(to) || i.isEqual(to); i = i.plusDays(1)) {
            java.sql.Date d = java.sql.Date.valueOf(i);
            boolean found = false;
            for (ThongKeTungNgayTrongThangDTO row : dbRows) {
                LocalDate rowDate = ((java.sql.Date) row.getNgayDate()).toLocalDate();
                if (rowDate.isEqual(i)) { result.add(row); found = true; break; }
            }
            if (!found) result.add(new ThongKeTungNgayTrongThangDTO(d, 0, 0, 0));
        }
        return result;
    }

    /** Thống kê điểm từ ngày đến ngày */
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeDiemThiTuNgayDenNgay(Date start, Date end) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>();
        LocalDate from = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate to = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String sql = "SELECT DATE(thoigianvaothi) AS ngay, MAX(diemthi) AS cao, MIN(diemthi) AS thap, AVG(diemthi) AS tb " +
                     "FROM baithi WHERE diemthi IS NOT NULL AND DATE(thoigianvaothi) >= ? AND DATE(thoigianvaothi) <= ? " +
                     "GROUP BY DATE(thoigianvaothi) ORDER BY ngay";

        ArrayList<ThongKeTungNgayTrongThangDTO> dbRows = new ArrayList<>();
        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dbRows.add(new ThongKeTungNgayTrongThangDTO(rs.getDate("ngay"), rs.getDouble("cao"), rs.getDouble("thap"), rs.getDouble("tb")));
            }
        } catch (Exception e) { e.printStackTrace(); }

        for (LocalDate i = from; i.isBefore(to) || i.isEqual(to); i = i.plusDays(1)) {
            java.sql.Date d = java.sql.Date.valueOf(i);
            boolean found = false;
            for (ThongKeTungNgayTrongThangDTO row : dbRows) {
                LocalDate rowDate = ((java.sql.Date) row.getNgayDate()).toLocalDate();
                if (rowDate.isEqual(i)) { result.add(row); found = true; break; }
            }
            if (!found) result.add(new ThongKeTungNgayTrongThangDTO(d, 0, 0, 0));
        }
        return result;
    }
}