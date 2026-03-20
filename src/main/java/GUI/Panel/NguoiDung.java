package GUI.Panel;

import BUS.NguoiDungBUS;
import DTO.NguoiDungDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietNguoiDungDialog;
import GUI.Dialog.AddNguoiDungDialog;
import helper.Formater;
import helper.Validation;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class NguoiDung extends JPanel implements ActionListener, ItemListener {

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private NguoiDungBUS bus = new NguoiDungBUS();

    private PanelBorderRadius main, functionBar;
    private JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    private JScrollPane scrollTable;
    private MainFunction mainFunction;
    private IntegratedSearch search;

    private List<NguoiDungDTO> listHienTai = bus.getAll();
    private Color BackgroundColor = new Color(240, 247, 250);

    public NguoiDung() {
        initComponent();
        loadDataTable();
    }

    private void initComponent() {
        setBackground(BackgroundColor);
        setLayout(new BorderLayout(0, 0));

        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
        pnlBorder1.setBackground(BackgroundColor);
        add(pnlBorder1, BorderLayout.NORTH);

        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(BackgroundColor);
        add(pnlBorder2, BorderLayout.SOUTH);

        JButton btnAdd = new JButton("+ Thêm mới");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setBackground(new Color(0, 120, 215));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnAdd.addActionListener(e -> themNguoiDung()); // NÚT THÊM NGƯỜI DÙNG
        northPanel.add(btnAdd, BorderLayout.EAST);

        add(northPanel, BorderLayout.NORTH);

        // BẢNG VÀ SEARCH BAR
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // PANEL TÌM KIẾM + ICON SEARCH
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);

        JLabel searchIconLabel = new JLabel();
        ImageIcon searchIcon = IconHelper.loadIcon("find.png", 20, 20);
        if (searchIcon != null) {
            searchIconLabel.setIcon(searchIcon);
            searchIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        }

        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm người dùng...");
        searchField.setPreferredSize(new Dimension(0, 35));
        searchPanel.add(searchIconLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Username", "Họ tên", "Giới tính", "Ngày sinh", "Nhóm quyền", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(240, 240, 240));
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        table.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(table, 0, TableSorter.INTEGER_COMPARATOR);

        scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTable, BorderLayout.CENTER);

        contentCenter.add(main, BorderLayout.CENTER);
    }

    private void thucHienTimKiem() {
        String kieuTim = (String) search.cbxChoose.getSelectedItem();
        String tuKhoa = searchField.getText().trim().toLowerCase();
        List<NguoiDungDTO> all = bus.getAll();
        List<NguoiDungDTO> result = new ArrayList<>();

        if (tuKhoa.isEmpty()) {
            result = all;
        } else {
            for (NguoiDungDTO user : all) {
                boolean match = false;
                switch (kieuTim) {
                    case "ID":
                        match = user.getId().toLowerCase().contains(tuKhoa);
                        break;
                    case "Username":
                        match = user.getUsername().toLowerCase().contains(tuKhoa);
                        break;
                    case "Họ tên":
                        match = user.getHoten().toLowerCase().contains(tuKhoa);
                        break;
                    case "Nhóm quyền":
                        String tenNhom = bus.getTenNhomQuyen(user.getManhomquyen());
                        match = tenNhom != null && tenNhom.toLowerCase().contains(tuKhoa);
                        break;
                    case "Giới tính":
                        String gioiTinh = user.isGioitinh() ? "nam" : "nữ";
                        match = gioiTinh.contains(tuKhoa);
                        break;
                    case "Năm sinh":
                        if (user.getNgaysinh() != null) {
                            String namSinh = String.valueOf(user.getNgaysinh().toLocalDate().getYear());
                            match = namSinh.contains(tuKhoa);
                        }
                        break;
                    default: // Tất cả
                        String tenNhomAll = bus.getTenNhomQuyen(user.getManhomquyen());
                        String gioiTinhAll = user.isGioitinh() ? "nam" : "nữ";
                        String namSinhAll = user.getNgaysinh() != null
                                ? String.valueOf(user.getNgaysinh().toLocalDate().getYear()) : "";
                        match = user.getId().toLowerCase().contains(tuKhoa)
                                || user.getUsername().toLowerCase().contains(tuKhoa)
                                || user.getHoten().toLowerCase().contains(tuKhoa)
                                || (tenNhomAll != null && tenNhomAll.toLowerCase().contains(tuKhoa))
                                || gioiTinhAll.contains(tuKhoa)
                                || namSinhAll.contains(tuKhoa);
                        break;
                }
                if (match) {
                    result.add(user);
                }
            }
        }
        listHienTai = result;
        loadDataTable();
    }

    private void loadDataTable() {
        tableModel.setRowCount(0);
        for (NguoiDungDTO user : listHienTai) {
            String gioiTinh = user.isGioitinh() ? "Nam" : "Nữ";
            String trangThai = user.getTrangthai() == 1 ? "Hoạt động" : "Đã khóa";
            String nhomQuyen = bus.getTenNhomQuyen(user.getManhomquyen());
            String ngaySinh = user.getNgaysinh() != null ? Formater.FormatDate(user.getNgaysinh()) : "N/A";

            tableModel.addRow(new Object[]{
                user.getId(),
                user.getUsername(),
                user.getHoten(),
                gioiTinh,
                ngaySinh,
                nhomQuyen,
                trangThai
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object src = e.getSource();

        if (src == mainFunction.btn.get("create")) {
            themNguoiDung(owner);
        } else if (src == mainFunction.btn.get("update")) {
            suaNguoiDung(owner);
        } else if (src == mainFunction.btn.get("delete")) {
            xoaNguoiDung();
        } else if (src == mainFunction.btn.get("detail")) {
            xemChiTiet(owner);
        } else if (src == mainFunction.btn.get("import")) {
            importExcel();
        } else if (src == mainFunction.btn.get("export")) {
            exportExcel();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }

    private void themNguoiDung(JFrame owner) {
        AddNguoiDungDialog dialog = new AddNguoiDungDialog(owner, "Thêm người dùng", null, () -> {
            listHienTai = bus.getAll();
            loadDataTable();
        });
        dialog.setVisible(true);
    }

    private void suaNguoiDung(JFrame owner) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần sửa!");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        NguoiDungDTO user = bus.getById(id);
        if (user != null) {
            AddNguoiDungDialog dialog = new AddNguoiDungDialog(owner, "Sửa người dùng", user, () -> {
                listHienTai = bus.getAll();
                loadDataTable();
            });
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy người dùng!");
        }
    }

    private void xoaNguoiDung() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần xóa!");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        String hoten = (String) tableModel.getValueAt(modelRow, 2);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa người dùng \"" + hoten + "\"?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bus.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                listHienTai = bus.getAll();
                loadDataTable();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void xemChiTiet(JFrame owner) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần xem!");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        NguoiDungDTO user = bus.getById(id);
        if (user != null) {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            AddNguoiDung dialog = new AddNguoiDung(
                parentFrame,
                "Sửa người dùng",
                user,
                this::loadDataToTable
            );
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ICON THAO TÁC
    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton btnEdit = new JButton();
        private JButton btnDelete = new JButton();

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
            setBackground(Color.WHITE);

            btnEdit.setBorderPainted(false);
            btnEdit.setContentAreaFilled(false);
            btnEdit.setFocusPainted(false);

            btnDelete.setBorderPainted(false);
            btnDelete.setContentAreaFilled(false);
            btnDelete.setFocusPainted(false);

            ImageIcon editIcon = IconHelper.loadIcon("edit.png", 22, 22);
            ImageIcon deleteIcon = IconHelper.loadIcon("delete.png", 22, 22);

            if (editIcon != null) btnEdit.setIcon(editIcon);
            else btnEdit.setText("Sửa");

            if (deleteIcon != null) btnDelete.setIcon(deleteIcon);
            else btnDelete.setText("Xóa");

            btnEdit.setPreferredSize(new Dimension(30, 30));
            btnDelete.setPreferredSize(new Dimension(30, 30));

            btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnEdit.setBackground(new Color(230, 230, 230));
                    btnEdit.setContentAreaFilled(true);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnEdit.setContentAreaFilled(false);
                }
            });

            btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnDelete.setBackground(new Color(255, 200, 200));
                    btnDelete.setContentAreaFilled(true);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnDelete.setContentAreaFilled(false);
                }
            });

            add(btnEdit);
            add(btnDelete);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(new Color(240, 240, 240));
            } else {
                setBackground(Color.WHITE);
            }
            setOpaque(true);
            return this;
        }
    }

    // SỰ KIỆN CLICK
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        private JButton btnEdit = new JButton();
        private JButton btnDelete = new JButton();
        private int currentRow;

        public ButtonEditor() {
            super(new JCheckBox());

            panel.setBackground(Color.WHITE);

            btnEdit.setBorderPainted(false);
            btnEdit.setContentAreaFilled(false);
            btnEdit.setFocusPainted(false);

            btnDelete.setBorderPainted(false);
            btnDelete.setContentAreaFilled(false);
            btnDelete.setFocusPainted(false);

            ImageIcon editIcon = IconHelper.loadIcon("edit.png", 22, 22);
            ImageIcon deleteIcon = IconHelper.loadIcon("delete.png", 22, 22);

            if (editIcon != null) btnEdit.setIcon(editIcon);
            else btnEdit.setText("Sửa");

            if (deleteIcon != null) btnDelete.setIcon(deleteIcon);
            else btnDelete.setText("Xóa");

            btnEdit.setPreferredSize(new Dimension(30, 30));
            btnDelete.setPreferredSize(new Dimension(30, 30));

            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                String id = (String) table.getValueAt(currentRow, 0);
                suaNguoiDung(id);
            });

            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                String id = (String) table.getValueAt(currentRow, 0);
                String hoten = (String) table.getValueAt(currentRow, 2);
                xoaNguoiDung(id, hoten);
            });

            panel.add(btnEdit);
            panel.add(btnDelete);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }
}