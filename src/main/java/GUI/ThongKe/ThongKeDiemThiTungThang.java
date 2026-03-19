package GUI.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeTheoThangDTO;
import GUI.Component.PanelBorderRadius;
import GUI.ThongKe.Support.Chart;
import GUI.ThongKe.Support.ModelChart;
import helper.JTableExporter;

import com.toedter.calendar.JYearChooser;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeDiemThiTungThang extends JPanel implements ActionListener {

    PanelBorderRadius pnlChart;
    JPanel pnl_top;
    JYearChooser yearChooser;
    Chart chart;
    JButton btnExport;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;
    private final Font font = new Font("Arial", Font.BOLD, 12);

    public ThongKeDiemThiTungThang() {
        initComponent();
        loadThongKeThang(yearChooser.getYear());
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnl_top = new JPanel(new FlowLayout());
        JLabel lblNam = new JLabel("Chọn năm thống kê"); lblNam.setFont(font);
        yearChooser = new JYearChooser();
        yearChooser.setPreferredSize(new Dimension(60, 25));
        yearChooser.addPropertyChangeListener("year", (PropertyChangeEvent e) -> {
            loadThongKeThang((Integer) e.getNewValue());
        });

        btnExport = createButton("Xuất Excel", new Color(76, 175, 80));
        btnExport.addActionListener(this);
        pnl_top.add(lblNam); pnl_top.add(yearChooser); pnl_top.add(btnExport);

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
        String[] header = {"Tháng", "Điểm cao nhất", "Điểm thấp nhất", "Điểm trung bình"};
        tblModel.setColumnIdentifiers(header);
        tableThongKe.setModel(tblModel);
        tableThongKe.setRowHeight(30);
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

    public void loadThongKeThang(int nam) {
        ArrayList<ThongKeTheoThangDTO> list = ThongKeBUS.getThongKeDiemThiTungThang(nam);

        pnlChart.remove(chart);
        chart = new Chart();
        chart.addLegend("Điểm cao nhất", new Color(12, 84, 175));
        chart.addLegend("Điểm thấp nhất", new Color(211, 84, 0));
        chart.addLegend("Điểm trung bình", new Color(54, 143, 4));
        for (ThongKeTheoThangDTO i : list) {
            chart.addData(new ModelChart("Tháng " + i.getThang(),
                new double[]{i.getDiemCaoNhat(), i.getDiemThapNhat(), i.getDiemTrungBinh()}));
        }
        chart.repaint(); chart.validate();
        pnlChart.add(chart);
        pnlChart.repaint(); pnlChart.validate();

        tblModel.setRowCount(0);
        for (ThongKeTheoThangDTO i : list) {
            tblModel.addRow(new Object[]{
                "Tháng " + i.getThang(),
                String.format("%.2f", i.getDiemCaoNhat()),
                String.format("%.2f", i.getDiemThapNhat()),
                String.format("%.2f", i.getDiemTrungBinh())
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExport) {
            try { JTableExporter.exportJTableToExcel(tableThongKe); }
            catch (IOException ex) { Logger.getLogger(ThongKeDiemThiTungThang.class.getName()).log(Level.SEVERE, null, ex); }
        }
    }

    public void refreshTable() {
        loadThongKeThang(yearChooser.getYear());
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