package GUI.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import GUI.Component.PanelBorderRadius;
import GUI.ThongKe.Support.Chart;
import GUI.ThongKe.Support.ModelChart;
import helper.JTableExporter;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeDiemThiTrongThang extends JPanel {

    PanelBorderRadius pnlChart;
    JPanel pnl_top;
    JMonthChooser monthChooser;
    JYearChooser yearChooser;
    Chart chart;
    JButton btnThongKe, btnReset, btnExport;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;
    private final Font font = new Font("Arial", Font.BOLD, 12);

    public ThongKeDiemThiTrongThang() {
        initComponent();
        int thang = monthChooser.getMonth() + 1;
        int nam = yearChooser.getYear();
        loadThongKe(thang, nam);
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnl_top = new JPanel(new FlowLayout());
        JLabel lblThang = new JLabel("Chọn tháng"); lblThang.setFont(font);
        JLabel lblNam = new JLabel("Chọn năm"); lblNam.setFont(font);
        monthChooser = new JMonthChooser();
        monthChooser.setPreferredSize(new Dimension(100, 25));
        yearChooser = new JYearChooser();
        yearChooser.setPreferredSize(new Dimension(60, 25));

        btnThongKe = createButton("Thống kê", new Color(72, 118, 255));
        btnReset = createButton("Làm mới", new Color(72, 118, 255));
        btnExport = createButton("Xuất Excel", new Color(76, 175, 80));

        pnl_top.add(lblThang); pnl_top.add(monthChooser);
        pnl_top.add(lblNam); pnl_top.add(yearChooser);
        pnl_top.add(btnThongKe); pnl_top.add(btnReset); pnl_top.add(btnExport);

        btnThongKe.addActionListener(e -> loadThongKe(monthChooser.getMonth() + 1, yearChooser.getYear()));
        btnReset.addActionListener(e -> {
            LocalDate now = LocalDate.now();
            monthChooser.setMonth(now.getMonthValue() - 1);
            yearChooser.setYear(now.getYear());
            loadThongKe(now.getMonthValue(), now.getYear());
        });
        btnExport.addActionListener(e -> {
            try { JTableExporter.exportJTableToExcel(tableThongKe); }
            catch (IOException ex) { Logger.getLogger(ThongKeDiemThiTrongThang.class.getName()).log(Level.SEVERE, null, ex); }
        });

        pnlChart = new PanelBorderRadius();
        pnlChart.setLayout(new BoxLayout(pnlChart, BoxLayout.Y_AXIS));
        chart = new Chart();
        chart.addLegend("Điểm cao nhất", new Color(12, 84, 175));
        chart.addLegend("Điểm thấp nhất", new Color(211, 84, 0));
        chart.addLegend("Điểm trung bình", new Color(54, 143, 4));
        pnlChart.add(chart);

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
        scrollTableThongKe.setPreferredSize(new Dimension(0, 300));

        this.add(pnl_top, BorderLayout.NORTH);
        this.add(pnlChart, BorderLayout.CENTER);
        this.add(scrollTableThongKe, BorderLayout.SOUTH);
    }

    public void loadThongKe(int thang, int nam) {
        ArrayList<ThongKeTungNgayTrongThangDTO> list = ThongKeBUS.getThongKeDiemThiTrongThang(thang, nam);

        pnlChart.remove(chart);
        chart = new Chart();
        chart.addLegend("Điểm cao nhất", new Color(12, 84, 175));
        chart.addLegend("Điểm thấp nhất", new Color(211, 84, 0));
        chart.addLegend("Điểm trung bình", new Color(54, 143, 4));

        // Nhóm 3 ngày 1 bar để tránh quá dày
        int sumCao = 0, sumThap = 0; double sumTB = 0; int count = 0;
        for (int i = 0; i < list.size(); i++) {
            ThongKeTungNgayTrongThangDTO d = list.get(i);
            sumCao = Math.max(sumCao, (int) d.getDiemCaoNhat());
            sumThap = sumThap == 0 ? (int) d.getDiemThapNhat() : Math.min(sumThap, (int) d.getDiemThapNhat());
            sumTB += d.getDiemTrungBinh();
            count++;
            if (count == 3 || i == list.size() - 1) {
                int startIdx = i - count + 2;
                chart.addData(new ModelChart("Ngày " + (startIdx) + "->" + (i + 1),
                    new double[]{sumCao, sumThap, count > 0 ? sumTB / count : 0}));
                sumCao = 0; sumThap = 0; sumTB = 0; count = 0;
            }
        }
        chart.repaint(); chart.validate();
        pnlChart.add(chart);
        pnlChart.repaint(); pnlChart.validate();

        tblModel.setRowCount(0);
        for (ThongKeTungNgayTrongThangDTO i : list) {
            tblModel.addRow(new Object[]{
                i.getNgay(),
                i.getDiemCaoNhat() > 0 ? String.format("%.2f", i.getDiemCaoNhat()) : "-",
                i.getDiemThapNhat() > 0 ? String.format("%.2f", i.getDiemThapNhat()) : "-",
                i.getDiemTrungBinh() > 0 ? String.format("%.2f", i.getDiemTrungBinh()) : "-"
            });
        }
    }

    public void refreshTable() {
        loadThongKe(monthChooser.getMonth() + 1, yearChooser.getYear());
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = bgColor;
                if (getModel().isPressed()) c = bgColor.darker();
                else if (getModel().isRollover()) c = bgColor.brighter();
                g2.setColor(c); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2); g2.dispose();
            }
        };
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); button.setBorderPainted(false);
        button.setContentAreaFilled(false); button.setOpaque(false);
        button.setPreferredSize(new Dimension(100, 25));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
}