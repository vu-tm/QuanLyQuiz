package helper;

import BUS.DapAnBUS;
import BUS.DeThiBUS;
import BUS.KyThiBUS;
import BUS.MonHocBUS;
import DAO.DapAnDAO;
import DTO.CauHoiDTO;
import DTO.DapAnDTO;
import DTO.DeThiDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.FileDialog;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class writePDF {

    private final DeThiBUS dethiBus = new DeThiBUS();
    private final KyThiBUS kythiBus = new KyThiBUS();
    private final MonHocBUS monhocBus = new MonHocBUS();
    private final DapAnBUS dapanBus = new DapAnBUS();

    private final JFrame jf = new JFrame();
    private final FileDialog fd = new FileDialog(jf, "Xuất PDF đề thi", FileDialog.SAVE);
    private static final char[] LABELS = {'A', 'B', 'C', 'D'};

    // Fonts
    private Font fontNormal10;
    private Font fontNormal12;
    private Font fontBold12;
    private Font fontBold14;
    private Font fontBold20;
    private Font fontItalic10;

    private static final String[] ROBOTO_NORMAL_PATHS = {
        "com/formdev/flatlaf/fonts/roboto/Roboto-Regular.ttf",
        "/com/formdev/flatlaf/fonts/roboto/Roboto-Regular.ttf",
        "Roboto-Regular.ttf"
    };
    private static final String[] ROBOTO_BOLD_PATHS = {
        "com/formdev/flatlaf/fonts/roboto/Roboto-Bold.ttf",
        "/com/formdev/flatlaf/fonts/roboto/Roboto-Bold.ttf",
        "Roboto-Bold.ttf"
    };
    private static final String[] ROBOTO_ITALIC_PATHS = {
        "com/formdev/flatlaf/fonts/roboto/Roboto-BoldItalic.ttf",
        "/com/formdev/flatlaf/fonts/roboto/Roboto-BoldItalic.ttf",
        "Roboto-BoldItalic.ttf"
    };

    // ────────────────────────────────────────────────────────────────────────
    public writePDF() {
        BaseFont bfNormal = loadFontFromPaths(ROBOTO_NORMAL_PATHS);
        BaseFont bfBold = loadFontFromPaths(ROBOTO_BOLD_PATHS);
        BaseFont bfItalic = loadFontFromPaths(ROBOTO_ITALIC_PATHS);

        if (bfNormal != null && bfBold != null && bfItalic != null) {
            fontNormal10 = new Font(bfNormal, 10, Font.NORMAL);
            fontNormal12 = new Font(bfNormal, 12, Font.NORMAL);
            fontBold12 = new Font(bfBold, 12, Font.NORMAL);
            fontBold14 = new Font(bfBold, 14, Font.NORMAL);
            fontBold20 = new Font(bfBold, 20, Font.NORMAL);
            fontItalic10 = new Font(bfItalic, 10, Font.NORMAL);
        } else {
            fontNormal10 = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            fontNormal12 = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            fontBold12 = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            fontBold14 = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            fontBold20 = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            fontItalic10 = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
        }
    }

    private BaseFont loadFontFromPaths(String[] paths) {
        for (String path : paths) {
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream(path);
                if (is != null) {
                    byte[] bytes = is.readAllBytes();
                    is.close();
                    return BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes, null);
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    // helper
    private String getFile(String defaultName) {
        fd.pack();
        fd.setSize(800, 600);
        fd.validate();
        java.awt.Rectangle rect = jf.getContentPane().getBounds();
        double w = fd.getBounds().getWidth();
        double h = fd.getBounds().getHeight();
        Point p = new Point();
        p.setLocation(rect.getCenterX() - w / 2, rect.getCenterY() - h / 2);
        fd.setLocation(p);
        fd.setFile(defaultName);
        fd.setVisible(true);
        String url = fd.getDirectory() + fd.getFile();
        return url.equals("nullnull") ? null : url;
    }

    private void openFile(String filePath) {
        try {
            java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private PdfPCell noBorder(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(2f);
        return cell;
    }

    // main
    public void writeDeThi(int made) {
        String url = null;
        try {
            fd.setTitle("In đề thi");
            fd.setLocationRelativeTo(null);
            url = getFile("DeThi_" + made);
            if (url == null) {
                return;
            }
            url = url + ".pdf";

            DeThiDTO dt = dethiBus.getById(made);
            if (dt == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy đề thi mã: " + made);
                return;
            }
            String tenKyThi = kythiBus.getTenById(dt.getMakythi());
            String tenMonHoc = monhocBus.getTenById(dt.getMonthi());
            ArrayList<CauHoiDTO> dsCauHoi = dethiBus.getDanhSachCauHoiByMade(made);

            // ── Document A4 ──────────────────────────────────────────────────
            Document document = new Document(PageSize.A4, 54, 54, 54, 54);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(url));
            document.open();

            // header
            PdfPTable tblHeader = new PdfPTable(2);
            tblHeader.setWidthPercentage(100);
            tblHeader.setWidths(new float[]{42f, 58f});
            tblHeader.setSpacingAfter(4f);

            // Cột trái
            PdfPCell cellLeft = new PdfPCell();
            cellLeft.setBorder(Rectangle.NO_BORDER);
            cellLeft.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Paragraph pBo = new Paragraph("BỘ GIÁO DỤC VÀ ĐÀO TẠO", fontBold12);
            pBo.setAlignment(Element.ALIGN_CENTER);
            Paragraph pDe = new Paragraph("ĐỀ THI CHÍNH THỨC", fontBold12);
            pDe.setAlignment(Element.ALIGN_CENTER);
            Paragraph pSoCau = new Paragraph("(Đề thi có " + dsCauHoi.size() + " câu)", fontItalic10);
            pSoCau.setAlignment(Element.ALIGN_CENTER);
            cellLeft.addElement(pBo);
            cellLeft.addElement(pDe);
            cellLeft.addElement(pSoCau);

            // Cột phải
            PdfPCell cellRight = new PdfPCell();
            cellRight.setBorder(Rectangle.NO_BORDER);
            cellRight.setVerticalAlignment(Element.ALIGN_MIDDLE);

            Paragraph pKyThi = new Paragraph(tenKyThi.toUpperCase(), fontBold20);
            pKyThi.setAlignment(Element.ALIGN_CENTER);
            Paragraph pMon = new Paragraph("Bài thi: " + tenMonHoc, fontBold14);
            pMon.setAlignment(Element.ALIGN_CENTER);
            Paragraph pTime = new Paragraph(
                    "Thời gian làm bài: " + dt.getThoigianthi()
                    + " phút, không kể thời gian phát đề", fontItalic10);
            pTime.setAlignment(Element.ALIGN_CENTER);
            cellRight.addElement(pKyThi);
            cellRight.addElement(pMon);
            cellRight.addElement(pTime);

            tblHeader.addCell(cellLeft);
            tblHeader.addCell(cellRight);
            document.add(tblHeader);

            // Đường kẻ
            document.add(new Paragraph(" ", fontNormal10));
            document.add(new Chunk(new LineSeparator(1f, 100f, BaseColor.BLACK, Element.ALIGN_CENTER, -2)));
            document.add(new Paragraph(" ", fontNormal10));

            Paragraph pMaDe = new Paragraph("Mã đề thi " + String.format("%03d", made), fontBold14);
            pMaDe.setAlignment(Element.ALIGN_RIGHT);
            document.add(pMaDe);

            PdfPTable tblInfo = new PdfPTable(2);
            tblInfo.setWidthPercentage(100);
            tblInfo.setSpacingBefore(6f);
            tblInfo.setSpacingAfter(10f);
            tblInfo.addCell(noBorder(
                    "Họ, tên thí sinh: .............................................", fontNormal12));
            tblInfo.addCell(noBorder(
                    "Số báo danh: ..........................................", fontNormal12));
            document.add(tblInfo);

            // Câu hỏi
            for (int i = 0; i < dsCauHoi.size(); i++) {
                CauHoiDTO ch = dsCauHoi.get(i);

                Paragraph pCau = new Paragraph("Câu " + (i + 1) + ": " + ch.getNoidung(), fontBold12);
                pCau.setSpacingBefore(8f);
                document.add(pCau);

                ArrayList<DapAnDTO> dsDapAn = dapanBus.getDapAnDeHienThi(ch.getMacauhoi());
                int numAns = Math.min(dsDapAn.size(), 4);
                if (numAns == 0) {
                    continue;
                }

                int cols = (numAns <= 2) ? 2 : 4;
                PdfPTable tblAns = new PdfPTable(cols);
                tblAns.setWidthPercentage(100);
                tblAns.setSpacingBefore(3f);

                for (int j = 0; j < numAns; j++) {
                    tblAns.addCell(noBorder(
                            LABELS[j] + ". " + dsDapAn.get(j).getNoidungtl(), fontNormal12));
                }
                int rem = numAns % cols;
                if (rem != 0) {
                    for (int k = 0; k < (cols - rem); k++) {
                        tblAns.addCell(noBorder("", fontNormal12));
                    }
                }
                document.add(tblAns);
            }

            // footer
            document.add(new Paragraph(" ", fontNormal10));
            document.add(new Chunk(new LineSeparator(1f, 100f, BaseColor.BLACK, Element.ALIGN_CENTER, -2)));

            Paragraph pEnd = new Paragraph("--- HẾT ---", fontBold12);
            pEnd.setAlignment(Element.ALIGN_CENTER);
            document.add(pEnd);

            Paragraph pFooter = new Paragraph(
                    "Trang 1/" + writer.getPageNumber()
                    + " - Mã đề thi " + String.format("%03d", made), fontItalic10);
            pFooter.setAlignment(Element.ALIGN_RIGHT);
            document.add(pFooter);

            document.close();
            writer.close();
            openFile(url);

        } catch (DocumentException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi ghi file: " + url);
            ex.printStackTrace();
        }
    }
}
