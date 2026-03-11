package GUI.Panel;

import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.DeThiDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DeThi extends JPanel implements ActionListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableDeThi;
    JScrollPane scrollTableDeThi;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;
    Color BackgroundColor = new Color(240, 247, 250);

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));

        // Khởi tạo bảng
        tableDeThi = new JTable();
        scrollTableDeThi = new JScrollPane();
        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên ô
            }
        };
        String[] header = new String[]{"Mã đề", "Tên đề thi", "Môn học", "Thời gian", "Người tạo"};
        tblModel.setColumnIdentifiers(header);
        tableDeThi.setModel(tblModel);
        scrollTableDeThi.setViewportView(tableDeThi);
        tableDeThi.setFocusable(false);
        tableDeThi.setSelectionBackground(new Color(230, 230, 230));
        tableDeThi.setSelectionForeground(Color.BLACK);

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableDeThi.getColumnCount(); i++) {
            tableDeThi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Cấu hình sắp xếp
        tableDeThi.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableDeThi, 0, TableSorter.INTEGER_COMPARATOR);

        // Padding xung quanh
        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
        pnlBorder1.setBackground(BackgroundColor);
        this.add(pnlBorder1, BorderLayout.NORTH);
        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(BackgroundColor);
        this.add(pnlBorder2, BorderLayout.SOUTH);
        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(BackgroundColor);
        this.add(pnlBorder3, BorderLayout.EAST);
        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(BackgroundColor);
        this.add(pnlBorder4, BorderLayout.WEST);

        contentCenter = new JPanel();
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        this.add(contentCenter, BorderLayout.CENTER);

        // Thanh công cụ (Thêm, Sửa, Xóa...)
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        functionBar.setBackground(Color.WHITE);

        String[] action = {"create", "update", "delete", "detail", "import", "export"};
        mainFunction = new MainFunction(action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        // Thanh tìm kiếm
        search = new IntegratedSearch(new String[]{"Tất cả", "Mã đề", "Tên đề", "Môn học"});
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        // Phần bảng dữ liệu
        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTableDeThi, BorderLayout.CENTER);
        contentCenter.add(main, BorderLayout.CENTER);

        // Thêm dữ liệu mẫu
        loadDataDummy();
    }

    public void loadDataDummy() {
        tblModel.addRow(new Object[]{"1", "Kiểm tra Java cơ bản", "Lập trình Java", "45 phút", "Admin"});
        tblModel.addRow(new Object[]{"2", "Cuối kỳ CƠ SỞ DỮ LIỆU", "CSDL", "60 phút", "GiangVien1"});
    }

    public DeThi() {
        initComponent();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (e.getSource() == mainFunction.btn.get("create")) {
            new DeThiDialog(owner, "Thêm đề thi mới", true, "create");
        } else if (e.getSource() == mainFunction.btn.get("update")) {
            if (tableDeThi.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi cần sửa!");
            } else {
                new DeThiDialog(owner, "Chỉnh sửa đề thi", true, "update");
            }
        } else if (e.getSource() == mainFunction.btn.get("delete")) {
            if (tableDeThi.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi cần xóa!");
            } else {
                confirmDelete();
            }
        } else if (e.getSource() == mainFunction.btn.get("detail")) {
            if (tableDeThi.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi để xem!");
            } else {
                new DeThiDialog(owner, "Chi tiết đề thi", true, "view");
            }
        }
    }

    private void confirmDelete() {
        int opt = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            tblModel.removeRow(tableDeThi.getSelectedRow());
        }
    }
}
