package GUI.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import GUI.Component.ButtonCustom;
import helper.JTableExporter;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeDiemThiTuNgayDenNgay extends JPanel {

    JPanel pnl_top;
    JDateChooser dateFrom, dateTo;
    ButtonCustom btnThongKe, btnReset, btnExport;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;
    private final Font font = new Font("Arial", Font.BOLD, 12);
    private final Date currentDate = new Date();

    public ThongKeDiemThiTuNgayDenNgay() {
        initComponent();
        loadThongKe(currentDate, currentDate);
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnl_top = new JPanel(new FlowLayout());
        JLabel lblFrom = new JLabel("Từ ngày");
        lblFrom.setFont(font);
        JLabel lblTo = new JLabel("Đến ngày");
        lblTo.setFont(font);

        dateFrom = new JDateChooser();
        dateFrom.setPreferredSize(new Dimension(110, 25));
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateTo = new JDateChooser();
        dateTo.setPreferredSize(new Dimension(110, 25));
        dateTo.setDateFormatString("dd/MM/yyyy");

        btnThongKe = new ButtonCustom("Thống kê", "warning", 14, 100, 30);
        btnReset = new ButtonCustom("Làm mới", "success", 14, 100, 30);
        btnExport = new ButtonCustom("Xuất Excel", "excel", 14, 120, 30);

        pnl_top.add(lblFrom);
        pnl_top.add(dateFrom);
        pnl_top.add(lblTo);
        pnl_top.add(dateTo);
        pnl_top.add(btnThongKe);
        pnl_top.add(btnReset);
        pnl_top.add(btnExport);

        btnThongKe.addActionListener(e -> {
            try {
                if (validateDate() && dateFrom.getDate() != null && dateTo.getDate() != null) {
                    loadThongKe(dateFrom.getDate(), dateTo.getDate());
                }
            } catch (ParseException ex) {
                Logger.getLogger(ThongKeDiemThiTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnReset.addActionListener(e -> {
            dateFrom.setDate(currentDate);
            dateTo.setDate(currentDate);
            loadThongKe(currentDate, currentDate);
        });

        btnExport.addActionListener(e -> {
            try {
                JTableExporter.exportJTableToExcel(tableThongKe);
            } catch (IOException ex) {
                Logger.getLogger(ThongKeDiemThiTuNgayDenNgay.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        tableThongKe = new JTable();
        scrollTableThongKe = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = {"Ngày", "Điểm cao nhất", "Điểm thấp nhất", "Điểm trung bình"};
        tblModel.setColumnIdentifiers(header);
        tableThongKe.setModel(tblModel);
        tableThongKe.setRowHeight(37);
        tableThongKe.setAutoCreateRowSorter(true);
        tableThongKe.setDefaultEditor(Object.class, null);
        scrollTableThongKe.setViewportView(tableThongKe);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableThongKe.setDefaultRenderer(Object.class, centerRenderer);
        tableThongKe.setFocusable(false);
        scrollTableThongKe.setPreferredSize(new Dimension(0, 400));

        dateFrom.setDate(currentDate);
        dateTo.setDate(currentDate);

        this.add(pnl_top, BorderLayout.NORTH);
        this.add(scrollTableThongKe, BorderLayout.CENTER);
    }

    public boolean validateDate() throws ParseException {
        Date start = dateFrom.getDate();
        Date end = dateTo.getDate();
        if (start == null || end == null) {
            return false;
        }
        Date now = new Date();
        if (start.after(now)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu không được lớn hơn ngày hiện tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dateFrom.setDate(now);
            return false;
        }
        if (end.after(now)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc không được lớn hơn ngày hiện tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dateTo.setDate(now);
            return false;
        }
        LocalDate s = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate e2 = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (s.isAfter(e2)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dateTo.setDate(now);
            return false;
        }
        return true;
    }

    public void loadThongKe(Date start, Date end) {
        ArrayList<ThongKeTungNgayTrongThangDTO> list = ThongKeBUS.getThongKeDiemThiTuNgayDenNgay(start, end);
        tblModel.setRowCount(0);
        for (ThongKeTungNgayTrongThangDTO i : list) {
            tblModel.addRow(new Object[]{
                i.getNgay(),
                String.format("%.2f", i.getDiemCaoNhat()),
                String.format("%.2f", i.getDiemThapNhat()),
                String.format("%.2f", i.getDiemTrungBinh())
            });
        }
    }

    public void refreshTable() {
        loadThongKe(currentDate, currentDate);
    }
}
