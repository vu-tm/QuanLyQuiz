# AI_CONTEXT.md

Tài liệu này là “bản đồ dự án” dành riêng cho AI/agent để hiểu nhanh hệ thống mà không cần quét lại toàn bộ source code mỗi lần.

---

## 1) Tổng quan dự án

- Tên dự án: **QuanLyQuiz**
- Kiểu dự án: **Java Maven desktop app** (Swing GUI), mô hình phân tầng theo package.
- Mục tiêu nghiệp vụ (suy ra từ tên class/package): quản lý ngân hàng câu hỏi, đề thi, kỳ thi, lớp học, người dùng, nhóm quyền và phân quyền.

---

## 2) Cấu trúc thư mục chính

```text
pom.xml
README.md
database/
  quiz.sql
  sample_*.sql
excel/
src/main/java/
  BUS/
  config/
  DAO/
  DTO/
  GUI/
  helper/
src/main/resources/
  icon/
  img/
target/
```

### Ý nghĩa từng lớp package

1. **DTO/**
   - Chứa object dữ liệu thuần cho từng thực thể nghiệp vụ.
   - Đặt tên theo `*DTO` (ví dụ: `CauHoiDTO`, `DeThiDTO`, `NguoiDungDTO`).

2. **DAO/**
   - Truy cập dữ liệu DB (CRUD, query, mapping ResultSet -> DTO).
   - Đặt tên theo `*DAO` tương ứng thực thể.

3. **BUS/**
   - Xử lý nghiệp vụ tầng trung gian giữa GUI và DAO.
   - Thường dùng để validate business rule, điều phối gọi DAO.

4. **GUI/**
   - Giao diện Swing.
   - `Main.java` là điểm vào ứng dụng.
   - Chia nhỏ theo `Component/`, `Dialog/`, `Panel/`.

5. **config/**
   - Cấu hình kết nối DB (`JDBCUtil.java`).

6. **helper/**
   - Tiện ích dùng chung: validate, format, xuất file, PDF, date, icon, ...

7. **database/**
   - SQL khởi tạo schema + dữ liệu mẫu.

---

## 3) Danh sách domain chính (suy ra từ tên file)

- Câu hỏi (`CauHoi`)
- Đáp án (`DapAn`)
- Độ khó (`DoKho`)
- Loại câu hỏi (`LoaiCauHoi`)
- Môn học (`MonHoc`)
- Đề thi (`DeThi`, `ChiTietDeThi`, `GiaoDeThi`)
- Bài thi (`BaiThi`, `ChiTietBaiThi`)
- Kỳ thi (`KyThi`)
- Lớp (`Lop`, `ChiTietLop`)
- Người dùng (`NguoiDung`)
- Nhóm quyền / quyền (`NhomQuyen`, `Quyen`, `ChiTietQuyen`, `PhanCong`, `DanhMucChucNang`)

=> Kiến trúc dữ liệu theo hướng **quản trị hệ thống thi/quiz có phân quyền**.

---

## 4) Quy ước đặt tên & coding style hiện hữu

### 4.1 Naming
- Class theo PascalCase tiếng Việt không dấu + hậu tố tầng:
  - DTO: `XxxDTO`
  - DAO: `XxxDAO`
  - BUS: `XxxBUS`
- Package in lowercase: `BUS`, `DAO`, `DTO`, `GUI`, `helper`, `config`.

### 4.2 Tổ chức theo tầng
- GUI **không nên** truy cập DB trực tiếp.
- Luồng chuẩn: `GUI -> BUS -> DAO -> DB`.
- DTO dùng để truyền dữ liệu giữa các tầng.

### 4.3 Định dạng code
- Duy trì style hiện có của file khi sửa (indent, bracket, naming).
- Tránh reformat toàn file khi chỉ sửa logic cục bộ.

---

## 5) Entry point & vận hành

- Entry point: `src/main/java/GUI/Main.java`.
- Build system: Maven (`pom.xml`).
- Tài nguyên tĩnh (icon/image): `src/main/resources/icon`, `src/main/resources/img`.

---

## 6) CSDL & dữ liệu mẫu

- Script chính: `database/quiz.sql`.
- Dữ liệu mẫu tách theo bảng: `sample_cauhoi.sql`, `sample_dokho.sql`, `sample_monhoc.sql`, `sample_nguoidung.sql`, ...
- Khi khởi tạo local:
  1) chạy `quiz.sql`
  2) nạp các `sample_*.sql` theo thứ tự phụ thuộc khóa ngoại.

---

## 7) Bản đồ phụ thuộc giữa module (mức khái quát)

- `GUI.Component` cung cấp widget tái sử dụng cho `GUI.Dialog` và `GUI.Panel`.
- `GUI.Dialog/Panel` thao tác nghiệp vụ qua `BUS`.
- `BUS` dùng `DAO` để truy xuất dữ liệu.
- `DAO` dùng `config.JDBCUtil`.
- `helper` được gọi xuyên suốt nhiều tầng cho tiện ích chung.

---

## 8) Quy tắc dành cho AI khi sửa code dự án này

1. **Tôn trọng kiến trúc tầng**: không bypass BUS/DAO.
2. **Không đổi tên class/package hàng loạt** nếu không có yêu cầu migration rõ ràng.
3. **Giữ tương thích dữ liệu**: thay đổi DTO/DAO phải so khớp schema SQL.
4. **Sửa tối thiểu**: chỉ chạm file liên quan trực tiếp yêu cầu.
5. **Ưu tiên tái sử dụng helper/component sẵn có** trước khi tạo mới.
6. **Nếu thêm domain mới**: tạo đủ bộ `DTO + DAO + BUS + GUI` (nếu có màn hình).
7. **Với phân quyền**: rà soát các lớp `NhomQuyen`, `Quyen`, `ChiTietQuyen`, `PhanCong` trước khi sửa.

---

## 9) Checklist đọc nhanh cho AI mới vào dự án

1. Đọc `pom.xml` để xác nhận Java version + dependency.
2. Đọc `config/JDBCUtil.java` để biết cách kết nối DB.
3. Đọc 1 flow mẫu hoàn chỉnh theo thực thể:
   - `DTO/XxxDTO.java`
   - `DAO/XxxDAO.java`
   - `BUS/XxxBUS.java`
   - GUI liên quan trong `GUI/Dialog` hoặc `GUI/Panel`
4. Đọc `database/quiz.sql` để map cột bảng với DTO.
5. Kiểm tra helper dùng nhiều (`Validation`, `DatabaseHelper`, `FileHelper`, `DateHelper`).

---

## 10) Gợi ý mở rộng tài liệu này (khi có thời gian)

Để AI hiểu sâu hơn nữa, có thể bổ sung thêm các mục sau:
- ERD (quan hệ bảng) dạng text.
- Bảng mapping `table.column -> DTO.field`.
- Danh sách API/query quan trọng trong từng DAO.
- Luồng nghiệp vụ theo use-case (tạo đề, giao đề, chấm điểm, phân quyền).
- Quy chuẩn xử lý lỗi và transaction.

---

## 11) Trạng thái tài liệu

- Phiên bản: `v1`
- Ngày tạo: `2026-03-18`
- Phạm vi: tổng hợp từ cấu trúc workspace hiện tại.

> Khi có thay đổi lớn về schema/package/luồng nghiệp vụ, hãy cập nhật lại file này trước để các AI agent khác kế thừa ngữ cảnh chính xác.
