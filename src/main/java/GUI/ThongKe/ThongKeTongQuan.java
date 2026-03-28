package GUI.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeTungNgayTrongThangDTO;
import GUI.Component.itemTaskbar;
import GUI.ThongKe.Support.Chart;
import GUI.ThongKe.Support.ModelChart;
import GUI.Component.TableSorter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeTongQuan extends JPanel {

    JPanel jp_top, jp_center, pnlChart;
    itemTaskbar[] listitem;
    Chart chart;
    private JTable tableThongKe;
    private JScrollPane scrollTableThongKe;
    private DefaultTableModel tblModel;
    ArrayList<ThongKeTungNgayTrongThangDTO> dataset;

    String[][] getSt = {
        {"Tổng số đề thi", "dethi.svg", Integer.toString(ThongKeBUS.getTongSoDeThi())},
        {"Tổng số câu hỏi", "question.svg", Integer.toString(ThongKeBUS.getTongSoCauHoi())},
        {"Tổng số sinh viên", "nguoidung.svg", Integer.toString(ThongKeBUS.getTongSoSinhVien())}
    };

    public ThongKeTongQuan() {
        this.dataset = ThongKeBUS.getThongKe7NgayGanNhat();
        initComponent();
        loadDataTable(this.dataset);
    }

    public itemTaskbar[] getListitem() {
        return listitem;
    }

    public JPanel getJp_top() {
        return jp_top;
    }

    public DefaultTableModel getTblModel() {
        return tblModel;
    }

    public void loadDataChart() {
        for (ThongKeTungNgayTrongThangDTO i : dataset) {
            chart.addData(new ModelChart(i.getNgay() + "", new double[]{
                i.getDiemCaoNhat(), i.getDiemThapNhat(), i.getDiemTrungBinh()
            }));
        }
    }

    public void loadDataTable(ArrayList<ThongKeTungNgayTrongThangDTO> list) {
        tblModel.setRowCount(0);
        for (ThongKeTungNgayTrongThangDTO i : dataset) {
            tblModel.addRow(new Object[]{
                i.getNgay(),
                String.format("%.2f", i.getDiemCaoNhat()),
                String.format("%.2f", i.getDiemThapNhat()),
                String.format("%.2f", i.getDiemTrungBinh())
            });
        }
        tableThongKe.setRowHeight(28);
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // TOP: 3 card
        jp_top = new JPanel();
        jp_top.setLayout(new GridLayout(1, 3, 20, 0));
        jp_top.setOpaque(false);
        jp_top.setBorder(new EmptyBorder(10, 10, 10, 10));
        jp_top.setPreferredSize(new Dimension(0, 120));

        listitem = new itemTaskbar[getSt.length];
        for (int i = 0; i < getSt.length; i++) {
            listitem[i] = new itemTaskbar(getSt[i][1], getSt[i][2], getSt[i][0], 0);
            listitem[i].setBackground(Color.WHITE);
            listitem[i].setForeground(new Color(6, 101, 208));
            jp_top.add(listitem[i]);
        }

        // CENTER: Chart
        jp_center = new JPanel(new BorderLayout());
        jp_center.setBackground(Color.WHITE);

        JPanel jp_center_top = new JPanel(new FlowLayout());
        jp_center_top.setBorder(new EmptyBorder(10, 0, 0, 10));
        jp_center_top.setOpaque(false);
        JLabel txtChartName = new JLabel("Thống kê điểm thi 7 ngày gần nhất");
        txtChartName.putClientProperty("FlatLaf.style", "font: 150% $medium.font");
        jp_center_top.add(txtChartName);

        pnlChart = new JPanel();
        pnlChart.setOpaque(false);
        pnlChart.setLayout(new BorderLayout(0, 0));

        chart = new Chart();
        chart.addLegend("Điểm cao nhất", new Color(12, 84, 175));
        chart.addLegend("Điểm thấp nhất", new Color(211, 84, 0));
        chart.addLegend("Điểm trung bình", new Color(54, 143, 4));
        loadDataChart();
        pnlChart.add(chart, BorderLayout.CENTER);

        jp_center.add(jp_center_top, BorderLayout.NORTH);
        jp_center.add(pnlChart, BorderLayout.CENTER);

        // SOUTH: Table
        tableThongKe = new JTable();
        scrollTableThongKe = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Ngày", "Điểm cao nhất", "Điểm thấp nhất", "Điểm trung bình"};
        tblModel.setColumnIdentifiers(header);
        tableThongKe.setModel(tblModel);
        tableThongKe.setAutoCreateRowSorter(true);
        tableThongKe.setDefaultEditor(Object.class, null);
        scrollTableThongKe.setViewportView(tableThongKe);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableThongKe.setDefaultRenderer(Object.class, centerRenderer);
        tableThongKe.setFocusable(false);
        scrollTableThongKe.setPreferredSize(new Dimension(0, 250));

        TableSorter.configureTableColumnSorter(tableThongKe, 0, TableSorter.DATE_COMPARATOR);

        this.add(jp_top, BorderLayout.NORTH);
        this.add(jp_center, BorderLayout.CENTER);
        this.add(scrollTableThongKe, BorderLayout.SOUTH);
    }

    public void refreshData() {
        this.dataset = ThongKeBUS.getThongKe7NgayGanNhat();
        loadDataTable(this.dataset);
        tableThongKe.revalidate();
        tableThongKe.repaint();
    }
}
