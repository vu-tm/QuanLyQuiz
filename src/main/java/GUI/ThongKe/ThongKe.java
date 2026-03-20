package GUI.ThongKe;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

public class ThongKe extends JPanel {

    JTabbedPane tabbedPane;
    ThongKeTongQuan tongQuan;
    ThongKeCauHoi cauHoi;
    ThongKeHocSinh hocSinh;
    ThongKeDiemThi diemThi;
    Color primaryColor = new Color(6, 101, 208);

    public ThongKe() {
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new GridLayout(1, 1));
        this.setBackground(new Color(72, 118, 255));

        tongQuan = new ThongKeTongQuan();
        cauHoi = new ThongKeCauHoi();
        hocSinh = new ThongKeHocSinh();
        diemThi = new ThongKeDiemThi();

        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.addTab("Tổng quan", tongQuan);
        tabbedPane.addTab("Câu hỏi", cauHoi);
        tabbedPane.addTab("Học sinh", hocSinh);
        tabbedPane.addTab("Điểm thi", diemThi);

        // đậm tab dc chọn
        tabbedPane.addChangeListener((ChangeEvent e) -> {
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (i == tabbedPane.getSelectedIndex()) {
                    tabbedPane.setForegroundAt(i, primaryColor);
                } else {
                    tabbedPane.setForegroundAt(i, Color.BLACK);
                }
            }
            refreshCurrentTab();
        });
        
        this.add(tabbedPane);
        updateTabStyles();
    }

        private void updateTabStyles() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String title = tabbedPane.getTitleAt(i);
            if (i == tabbedPane.getSelectedIndex()) {
                tabbedPane.setTitleAt(i, "<html><b>" + title + "</b></html>");
            } else {
                tabbedPane.setTitleAt(i, "<html>" + title + "</html>");
            }
        }
    }

    private void refreshCurrentTab() {
        updateTabStyles();
        int index = tabbedPane.getSelectedIndex();
        switch (index) {
            case 0 -> tongQuan.refreshData();
            case 1 -> cauHoi.refreshTable();
            case 2 -> hocSinh.refreshTable();
            case 3 -> diemThi.refreshFirstTab();
        }
    }

    public void refreshTongQuan() {
        tongQuan.refreshData();
    }

    public JTabbedPane getTabbedPane() { return tabbedPane; }
    public ThongKeTongQuan getTongQuan() { return tongQuan; }
    public ThongKeCauHoi getCauHoi() { return cauHoi; }
    public ThongKeHocSinh getHocSinh() { return hocSinh; }
    public ThongKeDiemThi getDiemThi() { return diemThi; }
}