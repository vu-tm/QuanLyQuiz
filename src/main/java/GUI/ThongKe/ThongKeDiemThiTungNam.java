package GUI.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeDiemThiDTO;
import GUI.Component.PanelBorderRadius;
import GUI.ThongKe.Support.Chart;
import GUI.ThongKe.Support.ModelChart;
import GUI.Component.NumericDocumentFilter;
import helper.JTableExporter;
import helper.Validation;

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
import javax.swing.text.PlainDocument;

public class ThongKeDiemThiTungNam extends JPanel implements ActionListener {

    PanelBorderRadius pnlChart;
    JPanel pnl_top;
    JTextField txfNamBD, txfNamKT;
    Chart chart;
    JButton btnThongKe, btnReset, btnExport;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;
    private ArrayList<ThongKeDiemThiDTO> dataset;
    private int currentYear;
    private final Font font = new Font("Arial", Font.BOLD, 12);

    public ThongKeDiemThiTungNam() {
        this.currentYear = LocalDate.now().getYear();
        this.dataset = ThongKeBUS.getThongKeDiemThiTheoNam(currentYear - 6, currentYear);
        initComponent();
        loadDataTable(dataset);
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        pnl_top = new JPanel(new FlowLayout());
        JLabel lblBD = new JLabel("Từ năm"); lblBD.setFont(font);
        JLabel lblKT = new JLabel("Đến năm"); lblKT.setFont(font);

        txfNamBD = new JTextField("", 10);
        txfNamBD.setPreferredSize(new Dimension(100, 25));
        txfNamKT = new JTextField("", 10);
        txfNamKT.setPreferredSize(new Dimension(100, 25));

        PlainDocument docBD = (PlainDocument) txfNamBD.getDocument();
        docBD.setDocumentFilter(new NumericDocumentFilter());
        PlainDocument docKT = (PlainDocument) txfNamKT.getDocument();
        docKT.setDocumentFilter(new NumericDocumentFilter());

        btnThongKe = createButton("Thống kê", new Color(72, 118, 255));
        btnReset = createButton("Làm mới", new Color(72, 118, 255));
        btnExport = createButton("Xuất Excel", new Color(76, 175, 80));
        btnThongKe.addActionListener(this);
        btnReset.addActionListener(this);
        btnExport.addActionListener(this);

        pnl_top.add(lblBD); pnl_top.add(txfNamBD);
        pnl_top.add(lblKT); pnl_top.add(txfNamKT);
        pnl_top.add(btnThongKe); pnl_top.add(btnReset); pnl_top.add(btnExport);

        pnlChart = new PanelBorderRadius();
        pnlChart.setLayout(new BoxLayout(pnlChart, BoxLayout.Y_AXIS));
        loadDataChart(dataset);

        tableThongKe = new JTable();
        scrollTableThongKe = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = {"Năm", "Điểm cao nhất", "Điểm thấp nhất", "Điểm trung bình"};
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

    public void loadDataTable(ArrayList<ThongKeDiemThiDTO> list) {
        tblModel.setRowCount(0);
        for (ThongKeDiemThiDTO i : list) {
            tblModel.addRow(new Object[]{
                i.getThoigian(),
                String.format("%.2f", i.getDiemCaoNhat()),
                String.format("%.2f", i.getDiemThapNhat()),
                String.format("%.2f", i.getDiemTrungBinh())
            });
        }
    }

    public void loadDataChart(ArrayList<ThongKeDiemThiDTO> list) {
        pnlChart.removeAll();
        chart = new Chart();
        chart.addLegend("Điểm cao nhất", new Color(12, 84, 175));
        chart.addLegend("Điểm thấp nhất", new Color(211, 84, 0));
        chart.addLegend("Điểm trung bình", new Color(54, 143, 4));
        for (ThongKeDiemThiDTO i : list) {
            chart.addData(new ModelChart("Năm " + i.getThoigian(),
                new double[]{i.getDiemCaoNhat(), i.getDiemThapNhat(), i.getDiemTrungBinh()}));
        }
        chart.repaint(); chart.validate();
        pnlChart.add(chart);
        pnlChart.repaint(); pnlChart.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThongKe) {
            if (!new Validation().isEmpty(txfNamBD.getText()) || !new Validation().isEmpty(txfNamKT.getText())) {
                int namBD = Integer.parseInt(txfNamBD.getText());
                int namKT = Integer.parseInt(txfNamKT.getText());
                if (namBD > currentYear || namKT > currentYear) {
                    JOptionPane.showMessageDialog(this, "Năm không được lớn hơn năm hiện tại");
                } else if (namKT < namBD || namBD <= 2015 || namKT <= 2015) {
                    JOptionPane.showMessageDialog(this, "Năm kết thúc không được bé hơn năm bắt đầu và phải lớn hơn 2015");
                } else {
                    dataset = ThongKeBUS.getThongKeDiemThiTheoNam(namBD, namKT);
                    loadDataChart(dataset);
                    loadDataTable(dataset);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ!");
            }
        } else if (src == btnReset) {
            txfNamBD.setText(""); txfNamKT.setText("");
            dataset = ThongKeBUS.getThongKeDiemThiTheoNam(currentYear - 6, currentYear);
            loadDataChart(dataset); loadDataTable(dataset);
        } else if (src == btnExport) {
            try { JTableExporter.exportJTableToExcel(tableThongKe); }
            catch (IOException ex) { Logger.getLogger(ThongKeDiemThiTungNam.class.getName()).log(Level.SEVERE, null, ex); }
        }
    }

    public void refreshTable() {
        currentYear = LocalDate.now().getYear();
        dataset = ThongKeBUS.getThongKeDiemThiTheoNam(currentYear - 6, currentYear);
        loadDataChart(dataset);
        loadDataTable(dataset);
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
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
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