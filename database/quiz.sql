-- Database: `quiz`
-- --------------------------------------------------------
DROP DATABASE IF EXISTS `quiz`;
CREATE DATABASE `quiz` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `quiz`;

-- --------------------------------------------------------
-- Table structure for table `nhomquyen`
--
CREATE TABLE `nhomquyen` (
  `manhomquyen` int(11) NOT NULL AUTO_INCREMENT,
  `tennhomquyen` varchar(50) NOT NULL,
  `trangthai` tinyint(1) NOT NULL,
  PRIMARY KEY (`manhomquyen`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `nhomquyen` (`manhomquyen`, `tennhomquyen`, `trangthai`) VALUES
(1, 'Admin', 1),
(2, 'Giáo viên', 1),
(3, 'Học sinh', 1);

-- --------------------------------------------------------
-- Table structure for table `danhmucchucnang`
--
CREATE TABLE `danhmucchucnang` (
  `chucnang` varchar(50) NOT NULL,
  `tenchucnang` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`chucnang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `danhmucchucnang` (`chucnang`, `tenchucnang`) VALUES
('monhoc', 'Quản lý môn học'),
('cauhoi', 'Quản lý câu hỏi'),
('dethi', 'Quản lý đề thi'),
('lop', 'Quản lý lớp'),
('nguoidung', 'Quản lý người dùng'),
('nhomquyen', 'Quản lý nhóm quyền'),
('baithi', 'Quản lý bài thi'),
('thongke', 'Thống kê');

-- --------------------------------------------------------
-- Table structure for table `chitietquyen`
--
CREATE TABLE `chitietquyen` (
  `manhomquyen` int(11) NOT NULL,
  `chucnang` varchar(50) NOT NULL,
  `hanhdong` varchar(50) NOT NULL,
  PRIMARY KEY (`manhomquyen`,`chucnang`,`hanhdong`),
  KEY `chucnang` (`chucnang`),
  CONSTRAINT `chitietquyen_ibfk_1` FOREIGN KEY (`manhomquyen`) REFERENCES `nhomquyen` (`manhomquyen`),
  CONSTRAINT `chitietquyen_ibfk_2` FOREIGN KEY (`chucnang`) REFERENCES `danhmucchucnang` (`chucnang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Admin permissions (Full access)
INSERT INTO `chitietquyen` (`manhomquyen`, `chucnang`, `hanhdong`) VALUES
(1, 'monhoc', 'view'), (1, 'monhoc', 'add'), (1, 'monhoc', 'edit'), (1, 'monhoc', 'delete'),
(1, 'cauhoi', 'view'), (1, 'cauhoi', 'add'), (1, 'cauhoi', 'edit'), (1, 'cauhoi', 'delete'),
(1, 'dethi', 'view'), (1, 'dethi', 'add'), (1, 'dethi', 'edit'), (1, 'dethi', 'delete'),
(1, 'lop', 'view'), (1, 'lop', 'add'), (1, 'lop', 'edit'), (1, 'lop', 'delete'),
(1, 'nguoidung', 'view'), (1, 'nguoidung', 'add'), (1, 'nguoidung', 'edit'), (1, 'nguoidung', 'delete'),
(1, 'nhomquyen', 'view'), (1, 'nhomquyen', 'add'), (1, 'nhomquyen', 'edit'), (1, 'nhomquyen', 'delete'),
(1, 'baithi', 'view'), (1, 'baithi', 'delete'),
(1, 'thongke', 'view');

-- Giáo viên permissions
INSERT INTO `chitietquyen` (`manhomquyen`, `chucnang`, `hanhdong`) VALUES
(2, 'monhoc', 'view'),
(2, 'cauhoi', 'view'), (2, 'cauhoi', 'add'), (2, 'cauhoi', 'edit'), (2, 'cauhoi', 'delete'),
(2, 'dethi', 'view'), (2, 'dethi', 'add'), (2, 'dethi', 'edit'), (2, 'dethi', 'delete'),
(2, 'lop', 'view'), (2, 'lop', 'add'), (2, 'lop', 'edit'),
(2, 'baithi', 'view'),
(2, 'thongke', 'view');

-- Học sinh permissions
INSERT INTO `chitietquyen` (`manhomquyen`, `chucnang`, `hanhdong`) VALUES
(3, 'dethi', 'view'),
(3, 'baithi', 'view');

-- --------------------------------------------------------
-- Table structure for table `nguoidung`
--
CREATE TABLE `nguoidung` (
  `id` varchar(50) NOT NULL,
  `username` varchar(255) NOT NULL,
  `hoten` varchar(255) NOT NULL,
  `gioitinh` tinyint(1) DEFAULT NULL,
  `ngaysinh` date DEFAULT NULL,
  `matkhau` varchar(60) DEFAULT NULL,
  `trangthai` int(11) NOT NULL,
  `manhomquyen` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `manhomquyen` (`manhomquyen`),
  CONSTRAINT `nguoidung_ibfk_1` FOREIGN KEY (`manhomquyen`) REFERENCES `nhomquyen` (`manhomquyen`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `nguoidung` (`id`, `username`, `hoten`, `gioitinh`, `ngaysinh`, `matkhau`, `trangthai`, `manhomquyen`) VALUES
('ND001', 'admin', 'Quản trị viên', 1, '1990-01-01', 'admin', 1, 1),
('ND002', 'gv1', 'Nguyễn Văn Giáo', 1, '1985-05-15', '123', 1, 2),
('ND003', 'gv2', 'Trần Thị Giảng', 0, '1988-10-20', '123', 1, 2),
('ND004', 'hs1', 'Lê Văn Học', 1, '2005-03-10', '123', 1, 3),
('ND005', 'hs2', 'Phạm Thị Sinh', 0, '2005-07-25', '123', 1, 3),
('ND006', 'hs3', 'Hoàng Văn Tập', 1, '2005-12-30', '123', 1, 3);

-- --------------------------------------------------------
-- Table structure for table `monhoc`
--
CREATE TABLE `monhoc` (
  `mamonhoc` int(11) NOT NULL AUTO_INCREMENT,
  `tenmonhoc` varchar(255) NOT NULL,
  `sotinchi` int(11) DEFAULT NULL,
  `trangthai` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`mamonhoc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `monhoc` (`mamonhoc`, `tenmonhoc`, `sotinchi`, `trangthai`) VALUES
(1, 'Toán cao cấp', 3, 1),
(2, 'Lập trình Java', 4, 1),
(3, 'Tiếng Anh chuyên ngành', 2, 1),
(4, 'Cơ sở dữ liệu', 3, 1);

-- --------------------------------------------------------
-- Table structure for table `dokho`
--
CREATE TABLE `dokho` (
  `madokho` int(11) NOT NULL AUTO_INCREMENT,
  `tendokho` varchar(100) NOT NULL,
  `trangthai` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`madokho`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `dokho` (`madokho`, `tendokho`, `trangthai`) VALUES
(1, 'Dễ', 1),
(2, 'Trung bình', 1),
(3, 'Khó', 1);

-- --------------------------------------------------------
-- Table structure for table `loaicauhoi`
--
CREATE TABLE `loaicauhoi` (
  `maloai` int(11) NOT NULL AUTO_INCREMENT,
  `tenloai` varchar(100) NOT NULL,
  `trangthai` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`maloai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `loaicauhoi` (`maloai`, `tenloai`, `trangthai`) VALUES
(1, 'Trắc nghiệm', 1),
(2, 'Điền khuyết', 1);

-- --------------------------------------------------------
-- Table structure for table `cauhoi`
--
CREATE TABLE `cauhoi` (
  `macauhoi` int(11) NOT NULL AUTO_INCREMENT,
  `noidung` varchar(500) NOT NULL,
  `madokho` int(11) NOT NULL,
  `maloai` int(11) NOT NULL,
  `mamonhoc` int(11) NOT NULL,
  `nguoitao` varchar(50) DEFAULT NULL,
  `trangthai` int(11) DEFAULT NULL,
  PRIMARY KEY (`macauhoi`),
  KEY `madokho` (`madokho`),
  KEY `maloai` (`maloai`),
  KEY `mamonhoc` (`mamonhoc`),
  CONSTRAINT `cauhoi_ibfk_1` FOREIGN KEY (`madokho`) REFERENCES `dokho` (`madokho`),
  CONSTRAINT `cauhoi_ibfk_2` FOREIGN KEY (`maloai`) REFERENCES `loaicauhoi` (`maloai`),
  CONSTRAINT `cauhoi_ibfk_3` FOREIGN KEY (`mamonhoc`) REFERENCES `monhoc` (`mamonhoc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `cauhoi` (`macauhoi`, `noidung`, `madokho`, `maloai`, `mamonhoc`, `nguoitao`, `trangthai`) VALUES
-- Java Questions
(1, 'Java là ngôn ngữ lập trình loại nào?', 1, 1, 2, 'ND002', 1),
(2, 'Từ khóa nào dùng để kế thừa trong Java?', 1, 1, 2, 'ND002', 1),
(3, 'JDK là viết tắt của cụm từ nào?', 1, 1, 2, 'ND002', 1),
(4, 'Trong Java, biến local được khai báo ở đâu?', 2, 1, 2, 'ND002', 1),
(5, 'Lớp nào là lớp cha của tất cả các lớp trong Java?', 2, 1, 2, 'ND002', 1),
-- Math Questions
(6, 'Đạo hàm của sin(x) là gì?', 1, 1, 1, 'ND003', 1),
(7, 'Kết quả của 2^10 là bao nhiêu?', 1, 1, 1, 'ND003', 1),
(8, 'Số Pi xấp xỉ bằng bao nhiêu?', 1, 1, 1, 'ND003', 1),
(9, 'Ma trận vuông là ma trận có số hàng (...) số cột.', 2, 2, 1, 'ND003', 1);

-- --------------------------------------------------------
-- Table structure for table `dapan`
--
CREATE TABLE `dapan` (
  `madapan` int(11) NOT NULL AUTO_INCREMENT,
  `macauhoi` int(11) NOT NULL,
  `noidungtl` varchar(500) NOT NULL,
  `ladapan` tinyint(1) NOT NULL,
  PRIMARY KEY (`madapan`),
  KEY `macauhoi` (`macauhoi`),
  CONSTRAINT `dapan_ibfk_1` FOREIGN KEY (`macauhoi`) REFERENCES `cauhoi` (`macauhoi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `dapan` (`madapan`, `macauhoi`, `noidungtl`, `ladapan`) VALUES
-- Q1
(1, 1, 'Hướng đối tượng', 1),
(2, 1, 'Hướng thủ tục', 0),
(3, 1, 'Ngôn ngữ máy', 0),
(4, 1, 'Scripting', 0),
-- Q2
(5, 2, 'extends', 1),
(6, 2, 'implements', 0),
(7, 2, 'inherit', 0),
(8, 2, 'super', 0),
-- Q3
(9, 3, 'Java Development Kit', 1),
(10, 3, 'Java Deployment Kit', 0),
(11, 3, 'Java Design Kit', 0),
(12, 3, 'Java Digital Kit', 0),
-- Q4
(13, 4, 'Trong phương thức', 1),
(14, 4, 'Trong lớp', 0),
(15, 4, 'Ngoài phương thức', 0),
(16, 4, 'Trong khối static', 0),
-- Q5
(17, 5, 'Object', 1),
(18, 5, 'Class', 0),
(19, 5, 'System', 0),
(20, 5, 'Base', 0),
-- Q6
(21, 6, 'cos(x)', 1),
(22, 6, '-cos(x)', 0),
(23, 6, 'tan(x)', 0),
(24, 6, 'sin(x)', 0),
-- Q7
(25, 7, '1024', 1),
(26, 7, '512', 0),
(27, 7, '2048', 0),
(28, 7, '1000', 0),
-- Q8
(29, 8, '3.14159', 1),
(30, 8, '3.15', 0),
(31, 8, '3.12', 0),
(32, 8, '3.16', 0),
-- Q9 (Fill in blank)
(33, 9, 'bằng', 1);

-- --------------------------------------------------------
-- Table structure for table `lop`
--
CREATE TABLE `lop` (
  `malop` int(11) NOT NULL AUTO_INCREMENT,
  `tenlop` varchar(255) NOT NULL,
  `siso` int(11) DEFAULT NULL,
  `namhoc` int(11) DEFAULT NULL,
  `hocky` int(11) DEFAULT NULL,
  `trangthai` tinyint(1) DEFAULT NULL,
  `giangvien` varchar(50) NOT NULL,
  `mamonhoc` int(11) NOT NULL,
  PRIMARY KEY (`malop`),
  KEY `giangvien` (`giangvien`),
  KEY `mamonhoc` (`mamonhoc`),
  CONSTRAINT `lop_ibfk_1` FOREIGN KEY (`giangvien`) REFERENCES `nguoidung` (`id`),
  CONSTRAINT `lop_ibfk_2` FOREIGN KEY (`mamonhoc`) REFERENCES `monhoc` (`mamonhoc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `lop` (`malop`, `tenlop`, `siso`, `namhoc`, `hocky`, `trangthai`, `giangvien`, `mamonhoc`) VALUES
(1, 'Lớp Java 01', 30, 2025, 2, 1, 'ND002', 2),
(2, 'Lớp Toán 01', 40, 2025, 2, 1, 'ND003', 1);

-- --------------------------------------------------------
-- Table structure for table `chitietlop`
--
CREATE TABLE `chitietlop` (
  `malop` int(11) NOT NULL,
  `manguoidung` varchar(50) NOT NULL,
  `hienthi` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`malop`,`manguoidung`),
  KEY `manguoidung` (`manguoidung`),
  CONSTRAINT `chitietlop_ibfk_1` FOREIGN KEY (`malop`) REFERENCES `lop` (`malop`),
  CONSTRAINT `chitietlop_ibfk_2` FOREIGN KEY (`manguoidung`) REFERENCES `nguoidung` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `chitietlop` (`malop`, `manguoidung`, `hienthi`) VALUES
(1, 'ND004', 1),
(1, 'ND005', 1),
(2, 'ND004', 1),
(2, 'ND006', 1);

-- --------------------------------------------------------
-- Table structure for table `phancong`
--
CREATE TABLE `phancong` (
  `mamonhoc` int(11) NOT NULL,
  `manguoidung` varchar(50) NOT NULL,
  PRIMARY KEY (`mamonhoc`,`manguoidung`),
  KEY `manguoidung` (`manguoidung`),
  CONSTRAINT `phancong_ibfk_1` FOREIGN KEY (`mamonhoc`) REFERENCES `monhoc` (`mamonhoc`),
  CONSTRAINT `phancong_ibfk_2` FOREIGN KEY (`manguoidung`) REFERENCES `nguoidung` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `phancong` (`mamonhoc`, `manguoidung`) VALUES
(2, 'ND002'),
(1, 'ND003');

-- --------------------------------------------------------
-- Table structure for table `kythi`
--
CREATE TABLE `kythi` (
  `makythi` int(11) NOT NULL AUTO_INCREMENT,
  `tenkythi` varchar(255) NOT NULL,
  `thoigianbatdau` datetime NOT NULL,
  `thoigianketthuc` datetime NOT NULL,
  `trangthai` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`makythi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `kythi` (`makythi`, `tenkythi`, `thoigianbatdau`, `thoigianketthuc`, `trangthai`) VALUES
(1, 'Kiểm tra giữa kỳ II', '2026-03-01 08:00:00', '2026-03-31 23:59:59', 1),
(2, 'Thi kết thúc học kỳ II', '2026-06-15 07:30:00', '2026-06-30 17:00:00', 1);

-- --------------------------------------------------------
-- Table structure for table `dethi`
--
CREATE TABLE `dethi` (
  `made` int(11) NOT NULL AUTO_INCREMENT,
  `makythi` int(11) NOT NULL,
  `monthi` int(11) DEFAULT NULL,
  `nguoitao` varchar(50) DEFAULT NULL,
  `tende` varchar(255) DEFAULT NULL,
  `thoigiantao` datetime DEFAULT NULL,
  `thoigianthi` int(11) DEFAULT NULL,
  `tongsocau` int(11) DEFAULT 0,
  `trangthai` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`made`),
  KEY `makythi` (`makythi`),
  CONSTRAINT `dethi_ibfk_1` FOREIGN KEY (`makythi`) REFERENCES `kythi` (`makythi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `dethi` (`made`, `makythi`, `monthi`, `nguoitao`, `tende`, `thoigiantao`, `thoigianthi`, `tongsocau`, `trangthai`) VALUES
(1, 1, 2, 'ND002', 'Đề kiểm tra Java số 1', '2026-03-10 10:00:00', 45, 5, 1),
(2, 1, 1, 'ND003', 'Đề kiểm tra Toán số 1', '2026-03-12 14:00:00', 60, 4, 1);

-- --------------------------------------------------------
-- Table structure for table `chitietdethi`
--
CREATE TABLE `chitietdethi` (
  `made` int(11) NOT NULL,
  `macauhoi` int(11) NOT NULL,
  `thutu` int(11) DEFAULT NULL,
  PRIMARY KEY (`made`,`macauhoi`),
  KEY `macauhoi` (`macauhoi`),
  CONSTRAINT `chitietdethi_ibfk_1` FOREIGN KEY (`made`) REFERENCES `dethi` (`made`),
  CONSTRAINT `chitietdethi_ibfk_2` FOREIGN KEY (`macauhoi`) REFERENCES `cauhoi` (`macauhoi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `chitietdethi` (`made`, `macauhoi`, `thutu`) VALUES
-- Exam 1 (Java)
(1, 1, 1), (1, 2, 2), (1, 3, 3), (1, 4, 4), (1, 5, 5),
-- Exam 2 (Math)
(2, 6, 1), (2, 7, 2), (2, 8, 3), (2, 9, 4);

-- --------------------------------------------------------
-- Table structure for table `giaodethi`
--
CREATE TABLE `giaodethi` (
  `made` int(11) NOT NULL,
  `malop` int(11) NOT NULL,
  PRIMARY KEY (`made`,`malop`),
  KEY `malop` (`malop`),
  CONSTRAINT `giaodethi_ibfk_1` FOREIGN KEY (`made`) REFERENCES `dethi` (`made`),
  CONSTRAINT `giaodethi_ibfk_2` FOREIGN KEY (`malop`) REFERENCES `lop` (`malop`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `giaodethi` (`made`, `malop`) VALUES
(1, 1),
(2, 2);

-- --------------------------------------------------------
-- Table structure for table `baithi`
--
CREATE TABLE `baithi` (
  `mabaithi` int(11) NOT NULL AUTO_INCREMENT,
  `made` int(11) NOT NULL,
  `manguoidung` varchar(50) NOT NULL,
  `diemthi` double DEFAULT NULL,
  `thoigianvaothi` datetime DEFAULT NULL,
  `thoigianlambai` int(11) DEFAULT NULL,
  `socaudung` int(11) DEFAULT NULL,
  `socausai` int(11) DEFAULT 0,
  PRIMARY KEY (`mabaithi`),
  KEY `made` (`made`),
  KEY `manguoidung` (`manguoidung`),
  CONSTRAINT `baithi_ibfk_1` FOREIGN KEY (`made`) REFERENCES `dethi` (`made`),
  CONSTRAINT `baithi_ibfk_2` FOREIGN KEY (`manguoidung`) REFERENCES `nguoidung` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `baithi` (`mabaithi`, `made`, `manguoidung`, `diemthi`, `thoigianvaothi`, `thoigianlambai`, `socaudung`, `socausai`) VALUES
(1, 1, 'ND004', 8.0, '2026-03-15 09:00:00', 30, 4, 1);

-- --------------------------------------------------------
-- Table structure for table `chitietbaithi`
--
CREATE TABLE `chitietbaithi` (
  `mabaithi` int(11) NOT NULL,
  `macauhoi` int(11) NOT NULL,
  `dapanchon` int(11) DEFAULT NULL,
  `noidungdienkhuyet` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`mabaithi`,`macauhoi`),
  KEY `macauhoi` (`macauhoi`),
  CONSTRAINT `chitietbaithi_ibfk_1` FOREIGN KEY (`mabaithi`) REFERENCES `baithi` (`mabaithi`),
  CONSTRAINT `chitietbaithi_ibfk_2` FOREIGN KEY (`macauhoi`) REFERENCES `cauhoi` (`macauhoi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `chitietbaithi` (`mabaithi`, `macauhoi`, `dapanchon`, `noidungdienkhuyet`) VALUES
(1, 1, 1, NULL),
(1, 2, 5, NULL),
(1, 3, 9, NULL),
(1, 4, 13, NULL),
(1, 5, 18, NULL); -- Wrong answer (Should be 17)

COMMIT;
