-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 03, 2026 at 02:53 AM
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
  `manguoidung` varchar(50) NOT NULL,
  `diemthi` double DEFAULT NULL,
  `thoigianvaothi` datetime DEFAULT NULL,
  `thoigianlambai` int(11) DEFAULT NULL,
  `socaudung` int(11) DEFAULT NULL
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

-- --------------------------------------------------------

--
-- Table structure for table `chitietlop`
--

CREATE TABLE `chitietlop` (
  `malop` int(11) NOT NULL,
  `manguoidung` varchar(50) NOT NULL,
  `hienthi` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chitietquyen`
--

CREATE TABLE `chitietquyen` (
  `manhomquyen` int(11) NOT NULL,
  `chucnang` varchar(50) NOT NULL,
  `hanhdong` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `danhmucchucnang`
--

CREATE TABLE `danhmucchucnang` (
  `chucnang` varchar(50) NOT NULL,
  `tenchucnang` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `hienthibailam` tinyint(1) DEFAULT NULL,
  `xemdiemthi` tinyint(1) DEFAULT NULL,
  `xemdapan` tinyint(1) DEFAULT NULL,
  `trangthai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `dokho`
--

CREATE TABLE `dokho` (
  `madokho` int(11) NOT NULL,
  `tendokho` varchar(100) NOT NULL,
  `trangthai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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

-- --------------------------------------------------------

--
-- Table structure for table `loaicauhoi`
--

CREATE TABLE `loaicauhoi` (
  `maloai` int(11) NOT NULL,
  `tenloai` varchar(100) NOT NULL,
  `trangthai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `giangvien` varchar(50) NOT NULL,
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
  `sotietlythuyet` int(11) DEFAULT NULL,
  `sotietthuchanh` int(11) DEFAULT NULL,
  `trangthai` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
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
  `manhomquyen` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `nhomquyen`
--

CREATE TABLE `nhomquyen` (
  `manhomquyen` int(11) NOT NULL,
  `tennhomquyen` varchar(50) NOT NULL,
  `trangthai` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `phancong`
--

CREATE TABLE `phancong` (
  `mamonhoc` int(11) NOT NULL,
  `manguoidung` varchar(50) NOT NULL
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
  ADD PRIMARY KEY (`manhomquyen`,`chucnang`,`hanhdong`),
  ADD KEY `chucnang` (`chucnang`);

--
-- Indexes for table `danhmucchucnang`
--
ALTER TABLE `danhmucchucnang`
  ADD PRIMARY KEY (`chucnang`);

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
  MODIFY `macauhoi` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `dapan`
--
ALTER TABLE `dapan`
  MODIFY `madapan` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `dethi`
--
ALTER TABLE `dethi`
  MODIFY `made` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `dokho`
--
ALTER TABLE `dokho`
  MODIFY `madokho` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `kythi`
--
ALTER TABLE `kythi`
  MODIFY `makythi` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `loaicauhoi`
--
ALTER TABLE `loaicauhoi`
  MODIFY `maloai` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lop`
--
ALTER TABLE `lop`
  MODIFY `malop` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `monhoc`
--
ALTER TABLE `monhoc`
  MODIFY `mamonhoc` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `nhomquyen`
--
ALTER TABLE `nhomquyen`
  MODIFY `manhomquyen` int(11) NOT NULL AUTO_INCREMENT;

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
  ADD CONSTRAINT `chitietquyen_ibfk_1` FOREIGN KEY (`manhomquyen`) REFERENCES `nhomquyen` (`manhomquyen`),
  ADD CONSTRAINT `chitietquyen_ibfk_2` FOREIGN KEY (`chucnang`) REFERENCES `danhmucchucnang` (`chucnang`);

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
