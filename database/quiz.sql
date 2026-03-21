-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 21, 2026 at 12:03 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `quiz`
--

-- --------------------------------------------------------

--
-- Table structure for table `baithi`
--

CREATE TABLE `baithi` (
  `mabaithi` int(11) NOT NULL,
  `made` int(11) NOT NULL,
  `manguoidung` int(11) NOT NULL,
  `diemthi` double DEFAULT NULL,
  `thoigianvaothi` datetime DEFAULT NULL,
  `thoigianlambai` int(11) DEFAULT NULL,
  `socaudung` int(11) DEFAULT NULL,
  `socausai` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `cauhoi`
--

CREATE TABLE `cauhoi` (
  `macauhoi` int(11) NOT NULL,
  `noidung` varchar(500) NOT NULL,
  `madokho` int(11) NOT NULL,
  `maloai` int(11) NOT NULL,
  `mamonhoc` int(11) NOT NULL,
  `nguoitao` varchar(50) DEFAULT NULL,
  `trangthai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `cauhoi`
--

INSERT INTO `cauhoi` (`macauhoi`, `noidung`, `madokho`, `maloai`, `mamonhoc`, `nguoitao`, `trangthai`) VALUES
(1, 'Trong Java, biến static được sử dụng để làm gì?', 2, 1, 1, 'admin', 1),
(2, 'Java có hỗ trợ đa kế thừa thông qua Class không?', 1, 2, 1, 'admin', 1),
(3, 'Kiểu dữ liệu nào có kích thước lớn nhất trong các kiểu số nguyên của Java?', 1, 1, 1, 'admin', 1),
(4, 'Hiện tượng Deadlock là gì?', 3, 1, 2, 'admin', 1),
(5, 'Thành phần nào điều khiển việc cấp phát tài nguyên của hệ thống?', 1, 1, 2, 'admin', 1),
(6, 'Hệ điều hành là một phần mềm ứng dụng, đúng hay sai?', 1, 2, 2, 'admin', 1),
(7, 'Thẻ HTML nào được dùng để tạo một danh sách không thứ tự?', 1, 1, 3, 'admin', 1),
(8, 'Giao thức mặc định được sử dụng để truyền tải dữ liệu trên Web là gì?', 1, 1, 3, 'admin', 1),
(9, 'CSS là viết tắt của từ nào?', 2, 1, 3, 'admin', 1);

-- --------------------------------------------------------

--
-- Table structure for table `chitietbaithi`
--

CREATE TABLE `chitietbaithi` (
  `mabaithi` int(11) NOT NULL,
  `macauhoi` int(11) NOT NULL,
  `dapanchon` int(11) DEFAULT NULL,
  `noidungdienkhuyet` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chitietdethi`
--

CREATE TABLE `chitietdethi` (
  `made` int(11) NOT NULL,
  `macauhoi` int(11) NOT NULL,
  `thutu` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chitietdethi`
--

INSERT INTO `chitietdethi` (`made`, `macauhoi`, `thutu`) VALUES
(1, 1, 1),
(1, 2, 2),
(1, 3, 3),
(2, 4, 1),
(2, 5, 2),
(2, 6, 3),
(3, 7, 1),
(3, 8, 2),
(3, 9, 3);

-- --------------------------------------------------------

--
-- Table structure for table `chitietlop`
--

CREATE TABLE `chitietlop` (
  `malop` int(11) NOT NULL,
  `manguoidung` int(11) NOT NULL,
  `hienthi` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chitietquyen`
--

CREATE TABLE `chitietquyen` (
  `manhomquyen` int(11) NOT NULL,
  `machucnang` int(11) NOT NULL,
  `hanhdong` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chitietquyen`
--

INSERT INTO `chitietquyen` (`manhomquyen`, `machucnang`, `hanhdong`) VALUES
(1, 1, 'create'),
(1, 1, 'delete'),
(1, 1, 'update'),
(1, 1, 'view'),
(1, 2, 'create'),
(1, 2, 'delete'),
(1, 2, 'update'),
(1, 2, 'view'),
(1, 3, 'create'),
(1, 3, 'delete'),
(1, 3, 'update'),
(1, 3, 'view'),
(1, 4, 'create'),
(1, 4, 'delete'),
(1, 4, 'update'),
(1, 4, 'view'),
(1, 5, 'create'),
(1, 5, 'delete'),
(1, 5, 'update'),
(1, 5, 'view'),
(1, 6, 'create'),
(1, 6, 'delete'),
(1, 6, 'update'),
(1, 6, 'view'),
(1, 7, 'create'),
(1, 7, 'delete'),
(1, 7, 'update'),
(1, 7, 'view'),
(1, 8, 'create'),
(1, 8, 'delete'),
(1, 8, 'update'),
(1, 8, 'view'),
(1, 9, 'create'),
(1, 9, 'delete'),
(1, 9, 'update'),
(1, 9, 'view'),
(1, 10, 'create'),
(1, 10, 'delete'),
(1, 10, 'update'),
(1, 10, 'view'),
(1, 11, 'create'),
(1, 11, 'delete'),
(1, 11, 'update'),
(1, 11, 'view');

-- --------------------------------------------------------

--
-- Table structure for table `danhmucchucnang`
--

CREATE TABLE `danhmucchucnang` (
  `machucnang` int(11) NOT NULL,
  `tenchucnang` varchar(255) DEFAULT NULL,
  `trangthai` int(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `danhmucchucnang`
--

INSERT INTO `danhmucchucnang` (`machucnang`, `tenchucnang`, `trangthai`) VALUES
(1, 'Quản lý câu hỏi', 1),
(2, 'Quản lý đề thi', 1),
(3, 'Quản lý kỳ thi', 1),
(4, 'Quản lý môn học', 1),
(5, 'Quản lý lớp học', 1),
(6, 'Quản lý người dùng', 1),
(7, 'Quản lý nhóm quyền', 1),
(8, 'Phân công giảng dạy', 1),
(9, 'Thống kê báo cáo', 1),
(10, 'Quản lý độ khó', 1),
(11, 'Quản lý bài thi', 1);

-- --------------------------------------------------------

--
-- Table structure for table `dapan`
--

CREATE TABLE `dapan` (
  `madapan` int(11) NOT NULL,
  `macauhoi` int(11) NOT NULL,
  `noidungtl` varchar(500) NOT NULL,
  `ladapan` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `dapan`
--

INSERT INTO `dapan` (`madapan`, `macauhoi`, `noidungtl`, `ladapan`) VALUES
(1, 1, 'Dùng chung cho tất cả các đối tượng của lớp', 1),
(2, 1, 'Mỗi đối tượng có một bản sao riêng', 0),
(3, 1, 'Để khai báo một hằng số', 0),
(4, 1, 'Để giải phóng bộ nhớ', 0),
(5, 2, 'Đúng', 0),
(6, 2, 'Sai', 1),
(7, 3, 'int', 0),
(8, 3, 'short', 0),
(9, 3, 'long', 1),
(10, 3, 'byte', 0),
(11, 4, 'Tình trạng hai hoặc nhiều tiến trình chờ đợi lẫn nhau mãi mãi', 1),
(12, 4, 'Tình trạng hệ thống bị thiếu bộ nhớ RAM', 0),
(13, 4, 'Tình trạng CPU chạy quá tải', 0),
(14, 4, 'Lỗi ổ cứng không đọc được dữ liệu', 0),
(15, 5, 'Kernel (Nhân)', 1),
(16, 5, 'Trình soạn thảo văn bản', 0),
(17, 5, 'Trình duyệt web', 0),
(18, 5, 'Bảng tính Excel', 0),
(19, 6, 'Đúng', 0),
(20, 6, 'Sai (Là phần mềm hệ thống)', 1),
(21, 7, '<ul>', 1),
(22, 7, '<ol>', 0),
(23, 7, '<li>', 0),
(24, 7, '<list>', 0),
(25, 8, 'FTP', 0),
(26, 8, 'SMTP', 0),
(27, 8, 'HTTP', 1),
(28, 8, 'TCP/IP', 0),
(29, 9, 'Cascading Style Sheets', 1),
(30, 9, 'Creative Style System', 0),
(31, 9, 'Computer Style Sheets', 0),
(32, 9, 'Colorful Style Sheets', 0);

-- --------------------------------------------------------

--
-- Table structure for table `dethi`
--

CREATE TABLE `dethi` (
  `made` int(11) NOT NULL,
  `makythi` int(11) NOT NULL,
  `monthi` int(11) DEFAULT NULL,
  `nguoitao` varchar(50) DEFAULT NULL,
  `tende` varchar(255) DEFAULT NULL,
  `thoigiantao` datetime DEFAULT NULL,
  `thoigianthi` int(11) DEFAULT NULL,
  `tongsocau` int(11) DEFAULT 0,
  `trangthai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `dethi`
--

INSERT INTO `dethi` (`made`, `makythi`, `monthi`, `nguoitao`, `tende`, `thoigiantao`, `thoigianthi`, `tongsocau`, `trangthai`) VALUES
(1, 1, 1, 'admin', 'Đề thi Java cơ bản', '2025-03-17 08:00:00', 60, 10, 1),
(2, 1, 2, 'admin', 'Đề thi hệ điều hành giữa kỳ', '2025-03-17 09:00:00', 45, 10, 1),
(3, 2, 3, 'admin', 'Đề thi Lập trình web 15 phút', '2025-03-17 10:00:00', 15, 3, 1);

-- --------------------------------------------------------

--
-- Table structure for table `dokho`
--

CREATE TABLE `dokho` (
  `madokho` int(11) NOT NULL,
  `tendokho` varchar(100) NOT NULL,
  `trangthai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `dokho`
--

INSERT INTO `dokho` (`madokho`, `tendokho`, `trangthai`) VALUES
(1, 'Dễ', 1),
(2, 'Trung bình', 1),
(3, 'Khó', 1);

-- --------------------------------------------------------

--
-- Table structure for table `giaodethi`
--

CREATE TABLE `giaodethi` (
  `made` int(11) NOT NULL,
  `malop` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kythi`
--

CREATE TABLE `kythi` (
  `makythi` int(11) NOT NULL,
  `tenkythi` varchar(255) NOT NULL,
  `thoigianbatdau` datetime NOT NULL,
  `thoigianketthuc` datetime NOT NULL,
  `trangthai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `kythi`
--

INSERT INTO `kythi` (`makythi`, `tenkythi`, `thoigianbatdau`, `thoigianketthuc`, `trangthai`) VALUES
(1, 'Thi Giữa Kỳ 1', '2026-03-01 07:30:00', '2026-03-30 17:00:00', 1),
(2, 'Kiểm tra 15 phút', '2026-03-17 02:00:00', '2026-03-17 02:15:00', 1);

-- --------------------------------------------------------

--
-- Table structure for table `loaicauhoi`
--

CREATE TABLE `loaicauhoi` (
  `maloai` int(11) NOT NULL,
  `tenloai` varchar(100) NOT NULL,
  `trangthai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `loaicauhoi`
--

INSERT INTO `loaicauhoi` (`maloai`, `tenloai`, `trangthai`) VALUES
(1, 'Trắc nghiệm (4 lựa chọn)', 1),
(2, 'Đúng/Sai', 1);

-- --------------------------------------------------------

--
-- Table structure for table `lop`
--

CREATE TABLE `lop` (
  `malop` int(11) NOT NULL,
  `tenlop` varchar(255) NOT NULL,
  `siso` int(11) DEFAULT NULL,
  `namhoc` int(11) DEFAULT NULL,
  `hocky` int(11) DEFAULT NULL,
  `trangthai` tinyint(1) DEFAULT NULL,
  `giangvien` int(11) NOT NULL,
  `mamonhoc` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `monhoc`
--

CREATE TABLE `monhoc` (
  `mamonhoc` int(11) NOT NULL,
  `tenmonhoc` varchar(255) NOT NULL,
  `sotinchi` int(11) DEFAULT NULL,
  `trangthai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `monhoc`
--

INSERT INTO `monhoc` (`mamonhoc`, `tenmonhoc`, `sotinchi`, `trangthai`) VALUES
(1, 'Lập trình Java', 3, 1),
(2, 'Hệ điều hành', 3, 1),
(3, 'Lập trình web và ứng dụng', 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `nguoidung`
--

CREATE TABLE `nguoidung` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `hoten` varchar(255) NOT NULL,
  `gioitinh` tinyint(1) DEFAULT NULL,
  `ngaysinh` date DEFAULT NULL,
  `matkhau` varchar(60) DEFAULT NULL,
  `trangthai` int(11) NOT NULL,
  `manhomquyen` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `nguoidung`
--

INSERT INTO `nguoidung` (`id`, `username`, `hoten`, `gioitinh`, `ngaysinh`, `matkhau`, `trangthai`, `manhomquyen`) VALUES
(1, 'admin', 'Trương Minh Vũ', 1, '2000-04-01', '123', 1, 1),
(2, 'gv1', 'Nguyễn Thanh Hưng', 1, '1985-05-20', '123', 1, 2),
(3, 'gv2', 'Võ Nguyễn Thảo Nguyên', 0, '1988-10-15', '123', 1, 2),
(4, 'gv3', 'Phạm Hồng Phú', 1, '1982-03-12', '123', 1, 2),
(5, 'gv4', 'Đoàn Thị Thùy Linh', 0, '1992-07-08', '123', 1, 2),
(6, 'gv5', 'Dương Long Hải', 1, '1980-12-25', '123', 1, 2),
(7, 'hs1', 'Hà Huy Tuấn', 1, '2006-02-14', '123', 1, 3),
(8, 'hs2', 'Nguyễn Huy Hiếu', 1, '2007-09-30', '123', 1, 3),
(9, 'hs3', 'Huỳnh Quí', 1, '2005-11-11', '123', 1, 3),
(10, 'hs4', 'Vũ Việt Bách', 1, '2002-06-21', '123', 1, 3),
(11, 'hs5', 'Trần Văn Nhiều', 1, '2004-01-05', '123', 1, 3),
(12, 'hs6', 'Huỳnh Ái Quốc', 1, '2005-03-10', '123', 1, 3),
(13, 'hs7', 'Nguyễn Đình Trường', 1, '2006-05-15', '123', 1, 3),
(14, 'hs8', 'Phan Sỹ Hùng', 1, '2005-12-20', '123', 1, 3),
(15, 'hs9', 'Nguyễn Phan Duy Hiếu', 1, '2007-01-25', '123', 1, 3),
(16, 'hs10', 'Ngô Văn Nghiêm', 1, '2004-08-30', '123', 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `nhomquyen`
--

CREATE TABLE `nhomquyen` (
  `manhomquyen` int(11) NOT NULL,
  `tennhomquyen` varchar(50) NOT NULL,
  `trangthai` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `nhomquyen`
--

INSERT INTO `nhomquyen` (`manhomquyen`, `tennhomquyen`, `trangthai`) VALUES
(1, 'Admin', 1),
(2, 'Giáo viên', 1),
(3, 'Học sinh', 1);

-- --------------------------------------------------------

--
-- Table structure for table `phancong`
--

CREATE TABLE `phancong` (
  `mamonhoc` int(11) NOT NULL,
  `manguoidung` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `baithi`
--
ALTER TABLE `baithi`
  ADD PRIMARY KEY (`mabaithi`),
  ADD KEY `made` (`made`),
  ADD KEY `manguoidung` (`manguoidung`);

--
-- Indexes for table `cauhoi`
--
ALTER TABLE `cauhoi`
  ADD PRIMARY KEY (`macauhoi`),
  ADD KEY `madokho` (`madokho`),
  ADD KEY `maloai` (`maloai`),
  ADD KEY `mamonhoc` (`mamonhoc`);

--
-- Indexes for table `chitietbaithi`
--
ALTER TABLE `chitietbaithi`
  ADD PRIMARY KEY (`mabaithi`,`macauhoi`),
  ADD KEY `macauhoi` (`macauhoi`);

--
-- Indexes for table `chitietdethi`
--
ALTER TABLE `chitietdethi`
  ADD PRIMARY KEY (`made`,`macauhoi`),
  ADD KEY `macauhoi` (`macauhoi`);

--
-- Indexes for table `chitietlop`
--
ALTER TABLE `chitietlop`
  ADD PRIMARY KEY (`malop`,`manguoidung`),
  ADD KEY `manguoidung` (`manguoidung`);

--
-- Indexes for table `chitietquyen`
--
ALTER TABLE `chitietquyen`
  ADD PRIMARY KEY (`manhomquyen`,`machucnang`,`hanhdong`),
  ADD KEY `chucnang` (`machucnang`);

--
-- Indexes for table `danhmucchucnang`
--
ALTER TABLE `danhmucchucnang`
  ADD PRIMARY KEY (`machucnang`);

--
-- Indexes for table `dapan`
--
ALTER TABLE `dapan`
  ADD PRIMARY KEY (`madapan`),
  ADD KEY `macauhoi` (`macauhoi`);

--
-- Indexes for table `dethi`
--
ALTER TABLE `dethi`
  ADD PRIMARY KEY (`made`),
  ADD KEY `makythi` (`makythi`);

--
-- Indexes for table `dokho`
--
ALTER TABLE `dokho`
  ADD PRIMARY KEY (`madokho`);

--
-- Indexes for table `giaodethi`
--
ALTER TABLE `giaodethi`
  ADD PRIMARY KEY (`made`,`malop`),
  ADD KEY `malop` (`malop`);

--
-- Indexes for table `kythi`
--
ALTER TABLE `kythi`
  ADD PRIMARY KEY (`makythi`);

--
-- Indexes for table `loaicauhoi`
--
ALTER TABLE `loaicauhoi`
  ADD PRIMARY KEY (`maloai`);

--
-- Indexes for table `lop`
--
ALTER TABLE `lop`
  ADD PRIMARY KEY (`malop`),
  ADD KEY `giangvien` (`giangvien`),
  ADD KEY `mamonhoc` (`mamonhoc`);

--
-- Indexes for table `monhoc`
--
ALTER TABLE `monhoc`
  ADD PRIMARY KEY (`mamonhoc`);

--
-- Indexes for table `nguoidung`
--
ALTER TABLE `nguoidung`
  ADD PRIMARY KEY (`id`),
  ADD KEY `manhomquyen` (`manhomquyen`);

--
-- Indexes for table `nhomquyen`
--
ALTER TABLE `nhomquyen`
  ADD PRIMARY KEY (`manhomquyen`);

--
-- Indexes for table `phancong`
--
ALTER TABLE `phancong`
  ADD PRIMARY KEY (`mamonhoc`,`manguoidung`),
  ADD KEY `manguoidung` (`manguoidung`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `baithi`
--
ALTER TABLE `baithi`
  MODIFY `mabaithi` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `cauhoi`
--
ALTER TABLE `cauhoi`
  MODIFY `macauhoi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `dapan`
--
ALTER TABLE `dapan`
  MODIFY `madapan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `dethi`
--
ALTER TABLE `dethi`
  MODIFY `made` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `dokho`
--
ALTER TABLE `dokho`
  MODIFY `madokho` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `kythi`
--
ALTER TABLE `kythi`
  MODIFY `makythi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `loaicauhoi`
--
ALTER TABLE `loaicauhoi`
  MODIFY `maloai` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `lop`
--
ALTER TABLE `lop`
  MODIFY `malop` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `monhoc`
--
ALTER TABLE `monhoc`
  MODIFY `mamonhoc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `nguoidung`
--
ALTER TABLE `nguoidung`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `nhomquyen`
--
ALTER TABLE `nhomquyen`
  MODIFY `manhomquyen` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `baithi`
--
ALTER TABLE `baithi`
  ADD CONSTRAINT `baithi_ibfk_1` FOREIGN KEY (`made`) REFERENCES `dethi` (`made`),
  ADD CONSTRAINT `baithi_ibfk_2` FOREIGN KEY (`manguoidung`) REFERENCES `nguoidung` (`id`);

--
-- Constraints for table `cauhoi`
--
ALTER TABLE `cauhoi`
  ADD CONSTRAINT `cauhoi_ibfk_1` FOREIGN KEY (`madokho`) REFERENCES `dokho` (`madokho`),
  ADD CONSTRAINT `cauhoi_ibfk_2` FOREIGN KEY (`maloai`) REFERENCES `loaicauhoi` (`maloai`),
  ADD CONSTRAINT `cauhoi_ibfk_3` FOREIGN KEY (`mamonhoc`) REFERENCES `monhoc` (`mamonhoc`);

--
-- Constraints for table `chitietbaithi`
--
ALTER TABLE `chitietbaithi`
  ADD CONSTRAINT `chitietbaithi_ibfk_1` FOREIGN KEY (`mabaithi`) REFERENCES `baithi` (`mabaithi`),
  ADD CONSTRAINT `chitietbaithi_ibfk_2` FOREIGN KEY (`macauhoi`) REFERENCES `cauhoi` (`macauhoi`);

--
-- Constraints for table `chitietdethi`
--
ALTER TABLE `chitietdethi`
  ADD CONSTRAINT `chitietdethi_ibfk_1` FOREIGN KEY (`made`) REFERENCES `dethi` (`made`),
  ADD CONSTRAINT `chitietdethi_ibfk_2` FOREIGN KEY (`macauhoi`) REFERENCES `cauhoi` (`macauhoi`);

--
-- Constraints for table `chitietlop`
--
ALTER TABLE `chitietlop`
  ADD CONSTRAINT `chitietlop_ibfk_1` FOREIGN KEY (`malop`) REFERENCES `lop` (`malop`),
  ADD CONSTRAINT `chitietlop_ibfk_2` FOREIGN KEY (`manguoidung`) REFERENCES `nguoidung` (`id`);

--
-- Constraints for table `chitietquyen`
--
ALTER TABLE `chitietquyen`
  ADD CONSTRAINT `chitietquyen_ibfk_1` FOREIGN KEY (`manhomquyen`) REFERENCES `nhomquyen` (`manhomquyen`);

--
-- Constraints for table `dapan`
--
ALTER TABLE `dapan`
  ADD CONSTRAINT `dapan_ibfk_1` FOREIGN KEY (`macauhoi`) REFERENCES `cauhoi` (`macauhoi`);

--
-- Constraints for table `dethi`
--
ALTER TABLE `dethi`
  ADD CONSTRAINT `dethi_ibfk_1` FOREIGN KEY (`makythi`) REFERENCES `kythi` (`makythi`);

--
-- Constraints for table `giaodethi`
--
ALTER TABLE `giaodethi`
  ADD CONSTRAINT `giaodethi_ibfk_1` FOREIGN KEY (`made`) REFERENCES `dethi` (`made`),
  ADD CONSTRAINT `giaodethi_ibfk_2` FOREIGN KEY (`malop`) REFERENCES `lop` (`malop`);

--
-- Constraints for table `lop`
--
ALTER TABLE `lop`
  ADD CONSTRAINT `lop_ibfk_1` FOREIGN KEY (`giangvien`) REFERENCES `nguoidung` (`id`),
  ADD CONSTRAINT `lop_ibfk_2` FOREIGN KEY (`mamonhoc`) REFERENCES `monhoc` (`mamonhoc`);

--
-- Constraints for table `nguoidung`
--
ALTER TABLE `nguoidung`
  ADD CONSTRAINT `nguoidung_ibfk_1` FOREIGN KEY (`manhomquyen`) REFERENCES `nhomquyen` (`manhomquyen`);

--
-- Constraints for table `phancong`
--
ALTER TABLE `phancong`
  ADD CONSTRAINT `phancong_ibfk_1` FOREIGN KEY (`mamonhoc`) REFERENCES `monhoc` (`mamonhoc`),
  ADD CONSTRAINT `phancong_ibfk_2` FOREIGN KEY (`manguoidung`) REFERENCES `nguoidung` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
