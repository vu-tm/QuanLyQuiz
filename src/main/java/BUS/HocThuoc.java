package BUS;

import DAO.*;
import DTO.*;
import DTO.ThongKe.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HocThuoc {

    private static CauHoiBUS cauHoiBUS = new CauHoiBUS();
    private static NguoiDungBUS nguoiDungBUS = new NguoiDungBUS();
    private static DapAnBUS dapAnBUS = new DapAnBUS();
    private static DeThiBUS deThiBUS = new DeThiBUS();
    private static NhomQuyenBUS nhomQuyenBUS = new NhomQuyenBUS();
    private static BaiThiBUS baiThiBUS = new BaiThiBUS();
    private static ChiTietBaiThiDAO chiTietBaiThiDAO = ChiTietBaiThiDAO.getInstance();

    // TỔNG QUAN 
    public static int getTongSoDeThi() {
        return deThiBUS.getAll().size();
    }

    public static int getTongSoCauHoi() {
        return cauHoiBUS.getAll().size();
    }

    public static int getTongSoSinhVien() {
        int count = 0;
        int maSinhVien = nhomQuyenBUS.getMaNhomQuyenByTen("sinh viên");
        ArrayList<NguoiDungDTO> listND = nguoiDungBUS.getAll();
        for (NguoiDungDTO nd : listND) {
            // Sinh viên & đang hoạt động
            if (nd.getManhomquyen() == maSinhVien && nd.getTrangthai() == 1) {
                count++;
            }
        }
        return count;
    }

    /*
    put: mở map bỏ vào
    get: mở map lấy ra
     */
    // THỐNG KÊ CÂU HỎI
    public static ArrayList<ThongKeCauHoiDTO> getThongKeCauHoi() {
        ArrayList<ThongKeCauHoiDTO> result = new ArrayList<>();
        ArrayList<CauHoiDTO> listCH = cauHoiBUS.getAll();
        ArrayList<DapAnDTO> listDA = dapAnBUS.getAll();
        ArrayList<ChiTietBaiThiDTO> listCTBT = chiTietBaiThiDAO.selectAll();

        /*
        CauHoiDTO: {macauhoi, noidung, madokho, maloai, mamonhoc, nguoitao, trangthai}
        DapAnDTO: {madapan, macauhoi, noidungtl, ladapan} 
        ChiTietBaiThiDTO: {mabaithi, macauhoi, dapanchon, noidungdienkhuyet}
        --
        mapDapAnDung: [key:macauhoi, value:DapAnDTO{madapan, macauhoi, noidungtl, ladapan} ]
        mapChiTietTheoCauHoi: [key:macauhoi, value:ChiTietBaiThiDTO{mabaithi, macauhoi, dapanchon, noidungdienkhuyet}]
         */
        // Tạo map tra cứu đáp án đúng của từng câu hỏi (key: maCauHoi, value: Đối tượng đáp án đúng của câu hỏi đó)
        Map<Integer, DapAnDTO> mapDapAnDung = new HashMap<>(); // mapDapAnDung = {} 
        for (DapAnDTO da : listDA) { // da là nguyên cái DapAnDTO{...}
            if (da.getLadapan()) {
                mapDapAnDung.put(da.getMacauhoi(), da); // Thế là có map key câu hỏi, value đáp án
            }
        }

        // Tạo map gom chi tiết bài thi theo từng câu hỏi (key: maCauHoi, value: Đối tượng chi tiết chứa lượt trả lời)
        Map<Integer, ArrayList<ChiTietBaiThiDTO>> mapChiTietTheoCauHoi = new HashMap<>();
        for (ChiTietBaiThiDTO ct : listCTBT) { // Duyệt từng câu trả lời của sv cho từng bài
            int maCH_HienTai = ct.getMacauhoi(); // Mã câu hỏi sv vừa trả lời

            // Lấy danh sách của câu hỏi, chưa thì null
            ArrayList<ChiTietBaiThiDTO> danhSachNay = mapChiTietTheoCauHoi.get(maCH_HienTai);

            // Tạo ds cho câu hỏi đấy, đưa vào map với key là maCauHoi
            if (danhSachNay == null) {
                danhSachNay = new ArrayList<>();
                mapChiTietTheoCauHoi.put(maCH_HienTai, danhSachNay); // lưu danh sách vào map
            }

            danhSachNay.add(ct); // thêm value cho danh sách câu hỏi
        }

        int stt = 1;
        for (CauHoiDTO ch : listCH) {
            if (ch.getTrangthai() != 1) {
                continue;
            }

            int maCH = ch.getMacauhoi();
            int loaiCH = ch.getMaloai();
            int tongLan = 0, soDung = 0;
            ArrayList<ChiTietBaiThiDTO> dsTraLoi = mapChiTietTheoCauHoi.get(maCH);
            DapAnDTO daDung = mapDapAnDung.get(maCH);

            if (dsTraLoi != null && daDung != null) {
                for (ChiTietBaiThiDTO ct : dsTraLoi) {
                    tongLan++;

                    if (loaiCH == 1 || loaiCH == 2) {
                        // Trắc nghiệm & Đúng sai: So sánh ID đáp án chọn
                        if (ct.getDapanchon() != 0 && ct.getDapanchon() == daDung.getMadapan()) {
                            soDung++;
                        }
                    } else if (loaiCH == 3) {
                        // Điền khuyết: So sánh nội dung chữ (ignore case, trim khoảng trắng)
                        String userAns = ct.getNoidungdienkhuyet();
                        String correctAns = daDung.getNoidungtl();
                        if (userAns != null && correctAns != null
                                && userAns.trim().equalsIgnoreCase(correctAns.trim())) {
                            soDung++;
                        }
                    }
                }
            }

            // Tính toán tỷ lệ
            double tyleDung = tongLan > 0 ? (double) Math.round((double) soDung / tongLan * 1000) / 10 : 0;
            double tyleSai = tongLan > 0 ? (double) Math.round((100 - tyleDung) * 10) / 10 : 0;

            result.add(new ThongKeCauHoiDTO(stt++, maCH, ch.getNoidung(), tongLan, tyleDung, tyleSai));
        }
        return result;
    }

    // THỐNG KÊ SINH VIÊN
    public static ArrayList<ThongKeSinhVienDTO> getThongKeSinhVien() {
        ArrayList<ThongKeSinhVienDTO> result = new ArrayList<>();   // arr chứa thống kê sinh viên final
        ArrayList<NguoiDungDTO> listND = nguoiDungBUS.getAll();     // arr chứa toàn bộ người dùng
        ArrayList<BaiThiDTO> listBT = baiThiBUS.getAll();        // arr chứa toàn bộ bài thi
        int maSinhVien = nhomQuyenBUS.getMaNhomQuyenByTen("sinh viên");

        Map<Integer, Integer> mapSoLanThi = new HashMap<>(); // key = manguoidung, value=solanthi
        for (BaiThiDTO bt : listBT) { // Duyệt qua tất cả bài thi
            int manguoidung = bt.getManguoidung(); // mã ng dùng của bài thi hiện tại
            /* đếm số lần thi
            Mới vào sẽ mặc định tất cả là 0. 
            Duyệt bài đầu tiên chắc chắn là 0, rồi +1 thành 1 vì bài đó đã đc duyệt
            Nếu có rồi thì lấy số lần thi đang lưu hiện tại
             */
            int soLanThiHienTai = mapSoLanThi.getOrDefault(manguoidung, 0);
            mapSoLanThi.put(manguoidung, soLanThiHienTai + 1);
        }
        // -> mapSoLanThi trả về người dùng thi mấy lần

        int stt = 1;
        for (NguoiDungDTO nd : listND) {
            if (nd.getManhomquyen() == maSinhVien && nd.getTrangthai() == 1) { // lọc ra sinh viên và hoạt động
                int soDe = mapSoLanThi.getOrDefault(nd.getManguoidung(), 0);
                result.add(new ThongKeSinhVienDTO(stt++, nd.getManguoidung(), nd.getHoten(), soDe));
            }
        }
        return result;
    }

    // CÁC HÀM THỐNG KÊ ĐIỂM THI
    // 1. Thống kê từ ngày đến ngày
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeDiemThiTuNgayDenNgay(Date start, Date end) {
        ArrayList<ThongKeTungNgayTrongThangDTO> result = new ArrayList<>(); // arr chứa kết quả thống kê
        ArrayList<BaiThiDTO> listBT = baiThiBUS.getAll();                   // arr chứa bài thi

        // Đổi Date sang LocalDate để thao tác +-*/ với ngày
        LocalDate from = new java.sql.Date(start.getTime()).toLocalDate();
        LocalDate to = new java.sql.Date(end.getTime()).toLocalDate();

        /** Bắt đầu từ ngày from đến ngày hiện tại
            from là ngày bắt đầu
            to là ngày kết thúc
            isAfter là phía sau -> ngày bắt đầu có nằm sau ngày kết thúc không ? tiếp tục : dừng lại
         */
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            double max = 0, min = Double.MAX_VALUE, tong = 0;       // điểm
            int count = 0;                                          // số lượng bài thi

            for (BaiThiDTO bt : listBT) {
                if (bt.getThoigianvaothi() == null) {
                    continue;
                }
                /* Lấy ngày thi dựa vào thời gian vào thi   VD: 2024-03-25 14:30:00
                .toLocalDateTime(): Chuyển Date thành LocalDateTime
                .toLocalDate(): Lấy phần ngày bỏ phần time
                 */
                LocalDate ngay = bt.getThoigianvaothi().toLocalDateTime().toLocalDate();

                if (ngay.isEqual(date)) {
                    double diem = bt.getDiemthi();
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
            // Chuyển LocalDate thành Date để lưu vô ThongKeTungNgayTrongThangDTO
            // Vì đầu vào là Date nên cần chuyển sang Date
            Date d = java.sql.Date.valueOf(date);

            result.add(new ThongKeTungNgayTrongThangDTO(d,
                    count > 0 ? max : 0,
                    count > 0 ? min : 0,
                    count > 0 ? tong / count : 0));
        }
        return result;
    }

    // 2. Thống kê theo 7 ngày gần nhất
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKe7NgayGanNhat() {
        LocalDate bayNgayTruoc = LocalDate.now().minusDays(6);
        LocalDate homNay = LocalDate.now();
        return getThongKeDiemThiTuNgayDenNgay(
                java.sql.Date.valueOf(bayNgayTruoc),
                java.sql.Date.valueOf(homNay)
        );
    }

    // 3. Thống kê trong 1 tháng cụ thể
    public static ArrayList<ThongKeTungNgayTrongThangDTO> getThongKeDiemThiTrongThang(int thang, int nam) {
        LocalDate from = LocalDate.of(nam, thang, 1);
        LocalDate to = from.plusMonths(1).minusDays(1);

        return getThongKeDiemThiTuNgayDenNgay(
                java.sql.Date.valueOf(from),
                java.sql.Date.valueOf(to)
        );
    }

    // 4. Thống kê từng tháng trong năm
    public static ArrayList<ThongKeTheoThangDTO> getThongKeDiemThiTungThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> result = new ArrayList<>();
        ArrayList<BaiThiDTO> listBT = baiThiBUS.getAll();

        for (int m = 1; m <= 12; m++) {
            double max = 0, min = Double.MAX_VALUE, tong = 0;
            int count = 0;
            for (BaiThiDTO bt : listBT) {
                if (bt.getThoigianvaothi() == null) {
                    continue;
                }

                // getYear() - getMonthValue() là của LocalDate
                LocalDate ngay = bt.getThoigianvaothi().toLocalDateTime().toLocalDate();
                if (ngay.getYear() == nam && ngay.getMonthValue() == m) {
                    double diem = bt.getDiemthi();
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

    // 5. Thống kê theo năm
    public static ArrayList<ThongKeDiemThiDTO> getThongKeDiemThiTheoNam(int namBD, int namKT) {
        ArrayList<ThongKeDiemThiDTO> result = new ArrayList<>();
        ArrayList<BaiThiDTO> listBT = baiThiBUS.getAll();

        for (int nam = namBD; nam <= namKT; nam++) {
            double max = 0, min = Double.MAX_VALUE, tong = 0;
            int count = 0;
            for (BaiThiDTO bt : listBT) {
                if (bt.getThoigianvaothi() == null) {
                    continue;
                }

                LocalDate ngay = bt.getThoigianvaothi().toLocalDateTime().toLocalDate();
                if (ngay.getYear() == nam) {
                    double diem = bt.getDiemthi();
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
