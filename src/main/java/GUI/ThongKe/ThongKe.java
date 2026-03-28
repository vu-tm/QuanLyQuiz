package GUI.ThongKe;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ThongKe extends JPanel {

    JTabbedPane tabbedPane;
    ThongKeTongQuan tongQuan;
    ThongKeCauHoi cauHoi;
    ThongKeSinhVien SinhVien;
    ThongKeDiemThi diemThi;

    public ThongKe() {
        initComponent();
    }

    public void initComponent() {
        this.setLayout(new GridLayout(1, 1));
        this.setBackground(new Color(240, 247, 250));

        tongQuan = new ThongKeTongQuan();
        cauHoi   = new ThongKeCauHoi();
        SinhVien  = new ThongKeSinhVien();
        diemThi  = new ThongKeDiemThi();

        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        tabbedPane.addTab("Tổng quan", tongQuan);
        tabbedPane.addTab("Câu hỏi",   cauHoi);
        tabbedPane.addTab("Sinh viên",  SinhVien);
        tabbedPane.addTab("Điểm thi",  diemThi);

        tabbedPane.addChangeListener(e -> refreshCurrentTab());

        this.add(tabbedPane);
    }

    private void refreshCurrentTab() {
        int index = tabbedPane.getSelectedIndex();
        switch (index) {
            case 0 -> tongQuan.refreshData();
            case 1 -> cauHoi.refreshTable();
            case 2 -> SinhVien.refreshTable();
            case 3 -> diemThi.refreshFirstTab();
        }
    }

    public void refreshTongQuan() {
        tongQuan.refreshData();
    }

    public JTabbedPane getTabbedPane() { return tabbedPane; }
    public ThongKeTongQuan getTongQuan() { return tongQuan; }
    public ThongKeCauHoi getCauHoi()    { return cauHoi; }
    public ThongKeSinhVien getSinhVien() { return SinhVien; }
    public ThongKeDiemThi getDiemThi() { return diemThi; }
}