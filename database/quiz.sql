-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 28, 2026 at 12:40 PM
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

--
-- Dumping data for table `baithi`
--

INSERT INTO `baithi` (`mabaithi`, `made`, `manguoidung`, `diemthi`, `thoigianvaothi`, `thoigianlambai`, `socaudung`, `socausai`) VALUES
(1, 3, 7, 10, '2026-03-26 15:24:44', 82, 13, 0),
(2, 3, 8, 10, '2026-03-26 15:35:46', 42, 13, 0),
(3, 1, 8, 7, '2026-03-26 15:37:07', 123, 7, 3),
(4, 4, 13, 6.67, '2026-03-26 15:42:03', 10, 2, 1),
(5, 1, 13, 9, '2026-03-26 15:42:39', 62, 9, 1),
(6, 4, 10, 6.67, '2026-03-26 15:44:31', 6, 2, 1),
(7, 3, 10, 10, '2026-03-26 15:44:44', 37, 13, 0),
(8, 1, 16, 9, '2026-03-27 10:11:13', 67, 9, 1),
(9, 5, 9, 7, '2026-03-28 16:54:50', 52, 7, 3),
(10, 6, 9, 5.71, '2026-03-28 16:56:05', 49, 4, 3),
(11, 3, 12, 10, '2026-03-28 17:27:44', 49, 13, 0),
(12, 1, 10, 9, '2026-03-28 17:37:36', 98, 9, 1),
(13, 1, 14, 10, '2026-03-28 17:40:55', 34, 10, 0),
(14, 6, 14, 7.14, '2026-03-28 17:41:35', 27, 5, 2),
(15, 5, 14, 8, '2026-03-28 17:42:07', 34, 8, 2),
(16, 3, 16, 10, '2026-03-28 18:36:25', 33, 13, 0),
(17, 4, 16, 10, '2026-03-28 18:37:05', 6, 3, 0),
(18, 5, 16, 9, '2026-03-28 18:37:16', 39, 9, 1),
(19, 6, 16, 7.14, '2026-03-28 18:38:00', 13, 5, 2),
(20, 7, 16, 8, '2026-03-28 18:38:24', 15, 4, 1);

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
  `nguoitao` int(11) DEFAULT NULL,
  `trangthai` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `cauhoi`
--

INSERT INTO `cauhoi` (`macauhoi`, `noidung`, `madokho`, `maloai`, `mamonhoc`, `nguoitao`, `trangthai`) VALUES
(1, 'Trong Java, biến static được sử dụng để làm gì?', 2, 1, 1, 1, 1),
(2, 'Java có hỗ trợ đa kế thừa thông qua Class không?', 1, 2, 1, 1, 1),
(3, 'Kiểu dữ liệu nào có kích thước lớn nhất trong các kiểu số nguyên của Java?', 1, 1, 1, 1, 1),
(4, 'Hiện tượng Deadlock là gì?', 1, 1, 2, 1, 1),
(5, 'Thành phần nào điều khiển việc cấp phát tài nguyên của hệ thống?', 1, 1, 2, 1, 1),
(6, 'Hệ điều hành là một phần mềm ứng dụng, đúng hay sai?', 3, 2, 2, 1, 1),
(7, 'Thẻ HTML nào được dùng để tạo một danh sách không thứ tự?', 1, 1, 3, 1, 1),
(8, 'Giao thức mặc định được sử dụng để truyền tải dữ liệu trên Web là gì?', 1, 1, 3, 1, 1),
(9, 'CSS là viết tắt của từ nào?', 2, 1, 3, 1, 1),
(10, 'Từ khóa nào được dùng để kế thừa một lớp trong Java?', 1, 3, 1, 1, 1),
(11, 'Trong Java, một lớp có thể thực thi (implement) tối đa bao nhiêu interface?', 2, 1, 1, 1, 1),
(12, 'Phương thức nào là điểm bắt đầu của mọi ứng dụng Java độc lập?', 1, 3, 1, 1, 1),
(13, 'Để so sánh hai chuỗi trong Java, ta nên dùng phương thức nào?', 2, 1, 1, 1, 1),
(14, 'Từ khóa \"final\" đặt trước một lớp có ý nghĩa là lớp đó không thể bị kế thừa. Đúng hay sai?', 1, 2, 1, 1, 1),
(15, 'Tên của bộ thu gom rác tự động trong Java là gì?', 2, 3, 1, 1, 1),
(16, 'Kiểu dữ liệu nào được dùng để lưu trữ một ký tự duy nhất?', 1, 3, 1, 1, 1),
(17, 'Lớp nào là lớp cha của tất cả các lớp trong Java?', 2, 1, 1, 1, 1),
(18, 'Câu lệnh nào dùng để thoát khỏi vòng lặp ngay lập tức?', 1, 3, 1, 1, 1),
(19, 'Java là ngôn ngữ lập trình thông dịch hoàn toàn, đúng hay sai?', 2, 2, 1, 1, 1),
(20, 'Thẻ HTML nào dùng để chèn một hình ảnh?', 1, 3, 3, 1, 1),
(21, 'Trong CSS, thuộc tính nào dùng để thay đổi màu chữ?', 1, 3, 3, 1, 1),
(22, 'Ký hiệu nào được dùng cho ID selector trong CSS?', 1, 1, 3, 1, 1),
(23, 'Sự kiện nào xảy ra khi người dùng nhấp chuột vào một phần tử HTML?', 2, 1, 3, 1, 1),
(24, 'Thẻ nào dùng để định nghĩa một liên kết (hyperlink)?', 1, 3, 3, 1, 1),
(25, 'JavaScript là ngôn ngữ lập trình chạy trên trình duyệt. Đúng hay sai?', 1, 2, 3, 1, 1),
(26, 'Trong HTML, thẻ nào dùng để tạo một ô nhập dữ liệu?', 1, 3, 3, 1, 1),
(27, 'Để căn giữa nội dung văn bản trong CSS, ta dùng thuộc tính text-align với giá trị là gì?', 2, 3, 3, 1, 1),
(28, 'Mô hình DOM viết tắt của từ gì?', 1, 1, 3, 1, 1),
(29, 'Giao thức HTTPS bảo mật hơn HTTP nhờ vào việc mã hóa dữ liệu. Đúng hay sai?', 1, 2, 3, 1, 1),
(30, 'JVM là viết tắt của cụm từ nào?', 2, 1, 1, 1, 1),
(31, 'Trong Java, một lớp (class) có thể kế thừa từ nhiều lớp cha khác nhau.', 3, 2, 1, 1, 1),
(32, 'Từ khóa _____ được dùng để xử lý ngoại lệ trong Java.', 2, 3, 1, 1, 1),
(33, 'Collection nào không cho phép lưu trữ các phần tử trùng lặp?', 1, 1, 1, 1, 1),
(34, 'Thuật toán lập lịch nào có thể gây ra hiện tượng \"đói\" (starvation)?', 3, 1, 2, 1, 1),
(35, 'Tiến trình (Process) và Luồng (Thread) là hai khái niệm hoàn toàn giống nhau.', 2, 2, 2, 1, 1),
(36, 'Trong quản lý bộ nhớ, hiện tượng _____ xảy ra khi bộ nhớ bị chia cắt thành nhiều vùng nhỏ không liên tục.', 3, 3, 2, 1, 1),
(37, 'Thành phần nào của hệ điều hành chịu trách nhiệm quản lý file?', 1, 1, 2, 1, 1);

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

--
-- Dumping data for table `chitietbaithi`
--

INSERT INTO `chitietbaithi` (`mabaithi`, `macauhoi`, `dapanchon`, `noidungdienkhuyet`) VALUES
(1, 7, 21, ''),
(1, 8, 27, ''),
(1, 9, 29, ''),
(1, 20, NULL, '<img>'),
(1, 21, NULL, 'color'),
(1, 22, 54, ''),
(1, 23, 57, ''),
(1, 24, NULL, '<a>'),
(1, 25, 60, ''),
(1, 26, NULL, '<input>'),
(1, 27, NULL, 'center'),
(1, 28, 64, ''),
(1, 29, 67, ''),
(2, 7, 21, ''),
(2, 8, 27, ''),
(2, 9, 29, ''),
(2, 20, NULL, '<img>'),
(2, 21, NULL, 'color'),
(2, 22, 54, ''),
(2, 23, 57, ''),
(2, 24, NULL, '<a>'),
(2, 25, 60, ''),
(2, 26, NULL, '<input>'),
(2, 27, NULL, 'center'),
(2, 28, 64, ''),
(2, 29, 67, ''),
(3, 10, NULL, 'extends'),
(3, 11, 35, ''),
(3, 12, NULL, 'main'),
(3, 13, 40, ''),
(3, 14, 41, ''),
(3, 15, NULL, 'Gabage Collector'),
(3, 16, NULL, 'char'),
(3, 17, 47, ''),
(3, 18, NULL, 'break'),
(3, 19, 50, ''),
(4, 4, 11, ''),
(4, 5, 15, ''),
(4, 6, 19, ''),
(5, 10, NULL, 'extends'),
(5, 11, 35, ''),
(5, 12, NULL, 'Main'),
(5, 13, 39, ''),
(5, 14, 41, ''),
(5, 15, NULL, 'Garbage Collector'),
(5, 16, NULL, 'char'),
(5, 17, 47, ''),
(5, 18, NULL, 'break'),
(5, 19, 50, ''),
(6, 4, 11, ''),
(6, 5, 15, ''),
(6, 6, 19, ''),
(7, 7, 21, ''),
(7, 8, 27, ''),
(7, 9, 29, ''),
(7, 20, NULL, '<img>'),
(7, 21, NULL, 'color'),
(7, 22, 54, ''),
(7, 23, 57, ''),
(7, 24, NULL, '<a>'),
(7, 25, 60, ''),
(7, 26, NULL, '<input>'),
(7, 27, NULL, 'center'),
(7, 28, 64, ''),
(7, 29, 67, ''),
(8, 10, NULL, 'extends'),
(8, 11, 35, ''),
(8, 12, NULL, 'Main'),
(8, 13, 39, ''),
(8, 14, 41, ''),
(8, 15, NULL, 'Garbade Collecter'),
(8, 16, NULL, 'char'),
(8, 17, 45, ''),
(8, 18, NULL, 'break'),
(8, 19, 50, ''),
(9, 1, 1, ''),
(9, 2, 6, ''),
(9, 3, 9, ''),
(9, 17, 45, ''),
(9, 18, NULL, 'break'),
(9, 19, 50, ''),
(9, 30, 72, ''),
(9, 31, 73, ''),
(9, 32, NULL, 'exception'),
(9, 33, 77, ''),
(10, 4, 11, ''),
(10, 5, 15, ''),
(10, 6, 19, ''),
(10, 34, 83, ''),
(10, 35, 85, ''),
(10, 36, NULL, 'dead'),
(10, 37, 87, ''),
(11, 7, 21, ''),
(11, 8, 27, ''),
(11, 9, 29, ''),
(11, 20, NULL, '<img>'),
(11, 21, NULL, 'color'),
(11, 22, 54, ''),
(11, 23, 57, ''),
(11, 24, NULL, '<a>'),
(11, 25, 60, ''),
(11, 26, NULL, '<input>'),
(11, 27, NULL, 'center'),
(11, 28, 64, ''),
(11, 29, 67, ''),
(12, 10, NULL, 'extends'),
(12, 11, 35, ''),
(12, 12, NULL, 'main'),
(12, 13, 39, ''),
(12, 14, 41, ''),
(12, 15, NULL, 'Garbade Collector'),
(12, 16, NULL, 'char'),
(12, 17, 45, ''),
(12, 18, NULL, 'break'),
(12, 19, 50, ''),
(13, 10, NULL, 'extends'),
(13, 11, 35, ''),
(13, 12, NULL, 'main'),
(13, 13, 39, ''),
(13, 14, 41, ''),
(13, 15, NULL, 'Garbage collector'),
(13, 16, NULL, 'char'),
(13, 17, 45, ''),
(13, 18, NULL, 'break'),
(13, 19, 50, ''),
(14, 4, 11, ''),
(14, 5, 15, ''),
(14, 6, 20, ''),
(14, 34, 83, ''),
(14, 35, 85, ''),
(14, 36, NULL, 'stavation'),
(14, 37, 87, ''),
(15, 1, 1, ''),
(15, 2, 6, ''),
(15, 3, 9, ''),
(15, 17, 45, ''),
(15, 18, NULL, 'break'),
(15, 19, 50, ''),
(15, 30, 72, ''),
(15, 31, 73, ''),
(15, 32, NULL, 'try-catch'),
(15, 33, 77, ''),
(16, 7, 21, ''),
(16, 8, 27, ''),
(16, 9, 29, ''),
(16, 20, NULL, '<img>'),
(16, 21, NULL, 'color'),
(16, 22, 54, ''),
(16, 23, 57, ''),
(16, 24, NULL, '<a>'),
(16, 25, 60, ''),
(16, 26, NULL, '<input>'),
(16, 27, NULL, 'center'),
(16, 28, 64, ''),
(16, 29, 67, ''),
(17, 4, 11, ''),
(17, 5, 15, ''),
(17, 6, 20, ''),
(18, 1, 1, ''),
(18, 2, 6, ''),
(18, 3, 9, ''),
(18, 17, 45, ''),
(18, 18, NULL, 'break'),
(18, 19, 50, ''),
(18, 30, 69, ''),
(18, 31, 73, ''),
(18, 32, NULL, 'try-catch'),
(18, 33, 77, ''),
(19, 4, 11, ''),
(19, 5, 15, ''),
(19, 6, 20, ''),
(19, 34, 83, ''),
(19, 35, 85, ''),
(19, 36, NULL, '1'),
(19, 37, 87, ''),
(20, 7, 21, ''),
(20, 26, NULL, '<input>'),
(20, 27, NULL, 'center'),
(20, 28, 64, ''),
(20, 29, 68, '');

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
(1, 10, 5),
(1, 11, 6),
(1, 12, 7),
(1, 13, 8),
(1, 14, 9),
(1, 15, 10),
(1, 16, 1),
(1, 17, 2),
(1, 18, 3),
(1, 19, 4),
(2, 4, 1),
(2, 5, 2),
(2, 6, 3),
(3, 7, 1),
(3, 8, 2),
(3, 9, 3),
(3, 20, 4),
(3, 21, 5),
(3, 22, 6),
(3, 23, 7),
(3, 24, 8),
(3, 25, 9),
(3, 26, 10),
(3, 27, 11),
(3, 28, 12),
(3, 29, 13),
(4, 4, 1),
(4, 5, 2),
(4, 6, 3),
(5, 1, 3),
(5, 2, 4),
(5, 3, 5),
(5, 17, 6),
(5, 18, 7),
(5, 19, 8),
(5, 30, 9),
(5, 31, 10),
(5, 32, 1),
(5, 33, 2),
(6, 4, 4),
(6, 5, 6),
(6, 6, 7),
(6, 34, 1),
(6, 35, 2),
(6, 36, 3),
(6, 37, 5),
(7, 7, 1),
(7, 26, 2),
(7, 27, 3),
(7, 28, 4),
(7, 29, 5);

-- --------------------------------------------------------

--
-- Table structure for table `chitietlop`
--

CREATE TABLE `chitietlop` (
  `malop` int(11) NOT NULL,
  `manguoidung` int(11) NOT NULL,
  `hienthi` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chitietlop`
--

INSERT INTO `chitietlop` (`malop`, `manguoidung`, `hienthi`) VALUES
(1, 7, 1),
(1, 8, 1),
(1, 9, 1),
(1, 10, 1),
(1, 11, 0),
(1, 12, 1),
(1, 13, 1),
(1, 14, 1),
(1, 15, 1),
(1, 16, 1),
(2, 7, 1),
(2, 8, 1),
(2, 9, 1),
(2, 10, 1),
(2, 11, 1),
(2, 12, 1),
(2, 13, 1),
(2, 14, 1),
(2, 15, 1),
(2, 16, 1),
(3, 7, 1),
(3, 8, 1),
(3, 9, 1),
(3, 10, 1),
(3, 11, 1),
(3, 12, 1),
(3, 13, 1),
(3, 14, 1),
(3, 15, 1),
(3, 16, 1),
(4, 7, 1),
(4, 8, 1),
(4, 9, 1),
(4, 10, 1),
(4, 11, 1),
(4, 12, 1),
(4, 13, 1),
(4, 14, 1),
(4, 15, 1),
(4, 16, 1);

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
(1, 11, 'delete'),
(1, 11, 'view'),
(1, 12, 'view'),
(2, 2, 'create'),
(2, 2, 'delete'),
(2, 2, 'update'),
(2, 2, 'view'),
(2, 5, 'create'),
(2, 5, 'delete'),
(2, 5, 'update'),
(2, 5, 'view'),
(3, 0, 'create'),
(3, 0, 'delete'),
(3, 0, 'update'),
(3, 0, 'view'),
(3, 11, 'view');

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
(0, 'Làm bài', 1),
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
(11, 'Quản lý bài thi', 1),
(12, 'Quản lý loại câu hỏi', 1);

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
(32, 9, 'Colorful Style Sheets', 0),
(33, 10, 'extends', 1),
(34, 11, 'Chỉ 1', 0),
(35, 11, 'Không giới hạn', 1),
(36, 11, 'Tối đa 2', 0),
(37, 12, 'main', 1),
(38, 13, '==', 0),
(39, 13, 'equals()', 1),
(40, 13, 'compare()', 0),
(41, 14, 'Đúng', 1),
(42, 14, 'Sai', 0),
(43, 15, 'Garbage Collector', 1),
(44, 16, 'char', 1),
(45, 17, 'Object', 1),
(46, 17, 'String', 0),
(47, 17, 'Class', 0),
(48, 18, 'break', 1),
(49, 19, 'Đúng', 0),
(50, 19, 'Sai (Kết hợp biên dịch và thông dịch)', 1),
(51, 20, '<img>', 1),
(52, 21, 'color', 1),
(53, 22, '.', 0),
(54, 22, '#', 1),
(55, 22, '*', 0),
(56, 23, 'onchange', 0),
(57, 23, 'onclick', 1),
(58, 23, 'onmouseover', 0),
(59, 24, '<a>', 1),
(60, 25, 'Đúng', 1),
(61, 25, 'Sai', 0),
(62, 26, '<input>', 1),
(63, 27, 'center', 1),
(64, 28, 'Document Object Model', 1),
(65, 28, 'Data Object Management', 0),
(66, 28, 'Digital Object Model', 0),
(67, 29, 'Đúng', 1),
(68, 29, 'Sai', 0),
(69, 30, 'Java Virtual Machine', 1),
(70, 30, 'Java Variable Method', 0),
(71, 30, 'Java Visual Model', 0),
(72, 30, 'Java Virtual Model', 0),
(73, 31, 'Đúng', 0),
(74, 31, 'Sai (Java chỉ hỗ trợ đơn kế thừa với lớp)', 1),
(75, 32, 'try-catch', 1),
(76, 33, 'ArrayList', 0),
(77, 33, 'Set', 1),
(78, 33, 'List', 0),
(79, 33, 'LinkedList', 0),
(80, 34, 'First-Come, First-Served (FCFS)', 0),
(81, 34, 'Round Robin', 0),
(82, 34, 'Lập lịch theo độ ưu tiên (Priority Scheduling)', 1),
(83, 34, 'Tất cả các phương án trên', 0),
(84, 35, 'Đúng', 0),
(85, 35, 'Sai (Tiến trình chứa một hoặc nhiều luồng)', 1),
(86, 36, 'phân mảnh', 1),
(87, 37, 'File System', 1),
(88, 37, 'CPU Scheduler', 0),
(89, 37, 'Memory Manager', 0),
(90, 37, 'Device Driver', 0);

-- --------------------------------------------------------

--
-- Table structure for table `dethi`
--

CREATE TABLE `dethi` (
  `made` int(11) NOT NULL,
  `makythi` int(11) NOT NULL,
  `monthi` int(11) DEFAULT NULL,
  `nguoitao` int(11) DEFAULT NULL,
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
(1, 1, 1, 1, 'Đề thi Java cơ bản', '2025-03-17 08:00:00', 60, 10, 1),
(2, 3, 2, 2, 'Đề thi hệ điều hành giữa kỳ', '2025-03-17 09:00:00', 45, 3, 1),
(3, 2, 3, 1, 'Đề thi Lập trình web 15 phút', '2025-03-17 10:00:00', 15, 13, 1),
(4, 4, 2, 1, 'Hệ điều hành học kỳ 1', '2026-03-24 16:53:42', 30, 3, 1),
(5, 4, 1, 1, 'Cuối kỳ java', '2026-03-28 16:43:03', 30, 10, 1),
(6, 4, 2, 1, 'Hệ điều hành cuối kỳ', '2026-03-28 16:43:25', 30, 7, 1),
(7, 2, 3, 1, 'Kiểm tra nhanh web', '2026-03-28 18:36:10', 15, 5, 1);

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

--
-- Dumping data for table `giaodethi`
--

INSERT INTO `giaodethi` (`made`, `malop`) VALUES
(1, 2),
(1, 3),
(2, 3),
(3, 1),
(4, 3),
(5, 2),
(6, 3),
(6, 4),
(7, 1);

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
(1, 'Thi Giữa Kỳ 1', '2026-03-01 07:30:00', '2026-04-30 17:00:00', 1),
(2, 'Kiểm tra 15 phút', '2026-03-20 02:00:00', '2026-04-24 02:15:00', 1),
(3, 'Kiểm tra tốt nghiệp', '2026-04-01 07:00:00', '2026-04-30 14:30:00', 1),
(4, 'Kiểm tra 30 phút', '2025-11-06 02:00:00', '2026-04-15 02:30:00', 1);

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
(1, 'Trắc nghiệm', 1),
(2, 'Đúng Sai', 1),
(3, 'Điền khuyết', 1);

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

--
-- Dumping data for table `lop`
--

INSERT INTO `lop` (`malop`, `tenlop`, `siso`, `namhoc`, `hocky`, `trangthai`, `giangvien`, `mamonhoc`) VALUES
(1, 'Web1', 0, 2026, 2, 1, 1, 3),
(2, 'Java1', 0, 2026, 1, 1, 1, 1),
(3, 'Hệ điều hành 1', 0, 2026, 2, 1, 1, 2),
(4, 'Hệ điều hành 2', 10, 2025, 2, 1, 1, 2);

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
(2, 'Giảng viên', 1),
(3, 'Sinh viên', 1);

-- --------------------------------------------------------

--
-- Table structure for table `phancong`
--

CREATE TABLE `phancong` (
  `mamonhoc` int(11) NOT NULL,
  `manguoidung` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `phancong`
--

INSERT INTO `phancong` (`mamonhoc`, `manguoidung`) VALUES
(2, 2),
(3, 2);

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
  ADD KEY `mamonhoc` (`mamonhoc`),
  ADD KEY `cauhoi_ibfk_4` (`nguoitao`);

--
-- Indexes for table `chitietbaithi`
--
ALTER TABLE `chitietbaithi`
  ADD PRIMARY KEY (`mabaithi`,`macauhoi`),
  ADD KEY `macauhoi` (`macauhoi`),
  ADD KEY `chitietbaithi_ibfk_3` (`dapanchon`);

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
  ADD KEY `makythi` (`makythi`),
  ADD KEY `dethi_ibfk_2` (`nguoitao`),
  ADD KEY `dethi_ibfk_3` (`monthi`);

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
  MODIFY `mabaithi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `cauhoi`
--
ALTER TABLE `cauhoi`
  MODIFY `macauhoi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT for table `dapan`
--
ALTER TABLE `dapan`
  MODIFY `madapan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT for table `dethi`
--
ALTER TABLE `dethi`
  MODIFY `made` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `dokho`
--
ALTER TABLE `dokho`
  MODIFY `madokho` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `kythi`
--
ALTER TABLE `kythi`
  MODIFY `makythi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `loaicauhoi`
--
ALTER TABLE `loaicauhoi`
  MODIFY `maloai` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `lop`
--
ALTER TABLE `lop`
  MODIFY `malop` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
  ADD CONSTRAINT `cauhoi_ibfk_3` FOREIGN KEY (`mamonhoc`) REFERENCES `monhoc` (`mamonhoc`),
  ADD CONSTRAINT `cauhoi_ibfk_4` FOREIGN KEY (`nguoitao`) REFERENCES `nguoidung` (`id`);

--
-- Constraints for table `chitietbaithi`
--
ALTER TABLE `chitietbaithi`
  ADD CONSTRAINT `chitietbaithi_ibfk_1` FOREIGN KEY (`mabaithi`) REFERENCES `baithi` (`mabaithi`),
  ADD CONSTRAINT `chitietbaithi_ibfk_2` FOREIGN KEY (`macauhoi`) REFERENCES `cauhoi` (`macauhoi`),
  ADD CONSTRAINT `chitietbaithi_ibfk_3` FOREIGN KEY (`dapanchon`) REFERENCES `dapan` (`madapan`);

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
  ADD CONSTRAINT `chitietquyen_ibfk_1` FOREIGN KEY (`manhomquyen`) REFERENCES `nhomquyen` (`manhomquyen`),
  ADD CONSTRAINT `chitietquyen_ibfk_2` FOREIGN KEY (`machucnang`) REFERENCES `danhmucchucnang` (`machucnang`);

--
-- Constraints for table `dapan`
--
ALTER TABLE `dapan`
  ADD CONSTRAINT `dapan_ibfk_1` FOREIGN KEY (`macauhoi`) REFERENCES `cauhoi` (`macauhoi`);

--
-- Constraints for table `dethi`
--
ALTER TABLE `dethi`
  ADD CONSTRAINT `dethi_ibfk_1` FOREIGN KEY (`makythi`) REFERENCES `kythi` (`makythi`),
  ADD CONSTRAINT `dethi_ibfk_2` FOREIGN KEY (`nguoitao`) REFERENCES `nguoidung` (`id`),
  ADD CONSTRAINT `dethi_ibfk_3` FOREIGN KEY (`monthi`) REFERENCES `monhoc` (`mamonhoc`);

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
