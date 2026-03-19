package GUI.ThongKe;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ThongKeDiemThi extends JPanel {

    JTabbedPane tabbedPane;
    ThongKeDiemThiTungNam diemThiTungNam;
    ThongKeDiemThiTungThang diemThiTungThang;
    ThongKeDiemThiTrongThang diemThiTrongThang;
    ThongKeDiemThiTuNgayDenNgay diemThiTuNgayDenNgay;

    public ThongKeDiemThi() {
        initComponent();
    }

    private void initComponent() {
        this.setLayout(new GridLayout(1, 1));
        this.setBackground(new Color(72, 118, 255));

        diemThiTungNam = new ThongKeDiemThiTungNam();
        diemThiTungThang = new ThongKeDiemThiTungThang();
        diemThiTrongThang = new ThongKeDiemThiTrongThang();
        diemThiTuNgayDenNgay = new ThongKeDiemThiTuNgayDenNgay();

        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.addTab("Thống kê theo năm", diemThiTungNam);
        tabbedPane.addTab("Thống kê từng tháng trong năm", diemThiTungThang);
        tabbedPane.addTab("Thống kê từng ngày trong tháng", diemThiTrongThang);
        tabbedPane.addTab("Thống kê từ ngày đến ngày", diemThiTuNgayDenNgay);

        this.add(tabbedPane);

        tabbedPane.addChangeListener(e -> {
            String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
            switch (title) {
                case "Thống kê theo năm":
                    diemThiTungNam.refreshTable(); break;
                case "Thống kê từng tháng trong năm":
                    diemThiTungThang.refreshTable(); break;
                case "Thống kê từng ngày trong tháng":
                    diemThiTrongThang.refreshTable(); break;
                case "Thống kê từ ngày đến ngày":
                    diemThiTuNgayDenNgay.refreshTable(); break;
            }
        });
    }

    public void refreshFirstTab() {
        diemThiTungNam.refreshTable();
        tabbedPane.setSelectedIndex(0);
    }

    public JTabbedPane getTabbedPane() { return tabbedPane; }
}