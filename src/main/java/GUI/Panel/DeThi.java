package GUI.Panel;

import BUS.DeThiBUS;
import DTO.DeThiDTO;
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

public class DeThi extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableDeThi;
    JScrollPane scrollTableDeThi;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    // Khởi tạo BUS
    DeThiBUS dethiBUS = new DeThiBUS();
    ArrayList<DeThiDTO> listDeThi = dethiBUS.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public DeThi() {
        initComponent();
        loadDataTable(listDeThi);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // Khởi tạo Table
        tableDeThi = new JTable();
        scrollTableDeThi = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = new String[]{"Mã đề", "Tên đề thi", "Mã môn", "Số câu", "Thời gian (phút)", "Người tạo", "Trạng thái"};
        tblModel.setColumnIdentifiers(header);
        tableDeThi.setModel(tblModel);
        tableDeThi.setFocusable(false);
        scrollTableDeThi.setViewportView(tableDeThi);

        // Căn giữa nội dung các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableDeThi.getColumnCount(); i++) {
            tableDeThi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

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

        // Thanh công cụ functionBar
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
        search = new IntegratedSearch(new String[]{"Tất cả", "Mã đề", "Tên đề", "Người tạo"});
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                thucHienTimKiem();
            }
        });

        // Combobox search
        search.cbxChoose.addItemListener(this);

        // Làm mới
        search.btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search.txtSearchForm.setText("");
                search.cbxChoose.setSelectedIndex(0);
                listDeThi = dethiBUS.getAll();
                loadDataTable(listDeThi);
            }
        });

        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        // Phần bảng hiển thị dữ liệu
        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTableDeThi, BorderLayout.CENTER);
        contentCenter.add(main, BorderLayout.CENTER);
    }

    public void thucHienTimKiem() {
        String kieuTimKiem = (String) search.cbxChoose.getSelectedItem();
        String noiDungTim = search.txtSearchForm.getText();
        listDeThi = dethiBUS.search(noiDungTim, kieuTimKiem);
        loadDataTable(listDeThi);
    }

    // Hàm bắt buộc khi implement ItemListener
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }

    public void loadDataTable(ArrayList<DeThiDTO> result) {
        tblModel.setRowCount(0);
        for (int i = 0; i < result.size(); i++) {
            DeThiDTO dt = result.get(i);
            tblModel.addRow(new Object[]{
                dt.getMade(),
                dt.getTende(),
                dt.getMonthi(),
                dt.getTongsocau(), 
                dt.getThoigianthi(),
                dt.getNguoitao(),
                dt.isTrangthai() ? "Hoạt động" : "Khóa"
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (e.getSource() == mainFunction.btn.get("create")) {
            new DeThiDialog(owner, "Thêm đề thi", true, "create");
        } else if (e.getSource() == mainFunction.btn.get("update")) {
            int index = tableDeThi.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi cần sửa");
            } else {
                new DeThiDialog(owner, "Sửa đề thi", true, "update");
            }
        } else if (e.getSource() == mainFunction.btn.get("delete")) {
            int index = tableDeThi.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi cần xóa");
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa đề thi này?", "Xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tblModel.removeRow(index);
                }
            }
        }
    }
}
