package GUI.ThongKe;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ThongKe extends JPanel {

    JTabbedPane tabbedPane;
    ThongKeTongQuan tongQuan;
    ThongKeCauHoi cauHoi;
    ThongKeHocSinh hocSinh;
    ThongKeDiemThi diemThi;

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

        this.add(tabbedPane);

        tabbedPane.addChangeListener(e -> {
            String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
            switch (title) {
                case "Tổng quan":
                    tongQuan.refreshData(); break;
                case "Câu hỏi":
                    cauHoi.refreshTable(); break;
                case "Học sinh":
                    hocSinh.refreshTable(); break;
                case "Điểm thi":
                    diemThi.refreshFirstTab(); break;
            }
        });
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