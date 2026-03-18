package GUI.Panel;

import BUS.NhomQuyenBUS;
import DTO.NhomQuyenDTO;
import GUI.Panel.AddNhomQuyen;
import helper.IconHelper;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class NhomQuyen extends JPanel {
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;

    private NhomQuyenBUS nhomQuyenBUS = new NhomQuyenBUS();

    public NhomQuyen() {
        initComponent();
        loadDataToTable();
    }

    private void initComponent() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // NORTH: Tiêu đề + nút Thêm mới
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Danh sách nhóm quyền");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(titleLabel, BorderLayout.WEST);

        JButton btnAdd = new JButton("+ Thêm mới");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setBackground(new Color(0, 120, 215));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnAdd.addActionListener(e -> themMoi());
        northPanel.add(btnAdd, BorderLayout.EAST);

        add(northPanel, BorderLayout.NORTH);

        // CENTER: Bảng + search bar
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);

        JLabel searchIconLabel = new JLabel();
        ImageIcon searchIcon = IconHelper.loadIcon("find.svg", 20, 20);
        if (searchIcon != null) {
            searchIconLabel.setIcon(searchIcon);
            searchIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        }

        searchField = new JTextField();
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm nhóm quyền...");
        searchField.setPreferredSize(new Dimension(0, 35));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                timKiem(searchField.getText());
            }
        });
        searchPanel.add(searchIconLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] columns = {"Mã nhóm quyền", "Tên nhóm", "Trạng thái", "Hành động"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(50);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(240, 240, 240));
        table.setSelectionForeground(Color.BLACK);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(245, 245, 245));
        header.setPreferredSize(new Dimension(0, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor());

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<NhomQuyenDTO> list = nhomQuyenBUS.getAll();
        for (NhomQuyenDTO g : list) {
            String trangThai = (g.getTrangthai() == 1) ? "Hoạt động" : "Đóng";
            tableModel.addRow(new Object[]{
                g.getManhomquyen(),
                g.getTennhomquyen(),
                trangThai,
                ""
            });
        }
    }

    private void timKiem(String keyword) {
        List<NhomQuyenDTO> list = nhomQuyenBUS.search(keyword);
        tableModel.setRowCount(0);
        for (NhomQuyenDTO g : list) {
            String trangThai = (g.getTrangthai() == 1) ? "Hoạt động" : "Đóng";
            tableModel.addRow(new Object[]{
                g.getManhomquyen(),
                g.getTennhomquyen(),
                trangThai,
                ""
            });
        }
    }

    private void themMoi() {
        AddNhomQuyen dialog = new AddNhomQuyen(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            "Thêm nhóm quyền",
            null,
            nhomQuyenBUS,
            this::loadDataToTable
        );
        dialog.setVisible(true);
    }

    private void suaNhomQuyen(int maNhomQuyen) {
        NhomQuyenDTO dto = nhomQuyenBUS.getById(maNhomQuyen);
        if (dto != null) {
            AddNhomQuyen dialog = new AddNhomQuyen(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Sửa nhóm quyền",
                dto,
                nhomQuyenBUS,
                this::loadDataToTable
            );
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhóm quyền!");
        }
    }

    private void xoaNhomQuyen(int maNhomQuyen, String tenNhomQuyen) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhóm quyền \"" + tenNhomQuyen + "\"?\n(Xóa nhóm quyền sẽ xóa tất cả phân quyền liên quan)",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = nhomQuyenBUS.delete(maNhomQuyen);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa nhóm quyền thành công!");
                loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Có lỗi xảy ra.");
            }
        }
    }

    // === Lớp ButtonRenderer (giữ nguyên) ===
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

            ImageIcon editIcon = IconHelper.loadIcon("edit.svg", 22, 22);
            ImageIcon deleteIcon = IconHelper.loadIcon("delete.svg", 22, 22);

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

    // === Lớp ButtonEditor (giữ nguyên) ===
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

            ImageIcon editIcon = IconHelper.loadIcon("edit.svg", 22, 22);
            ImageIcon deleteIcon = IconHelper.loadIcon("delete.svg", 22, 22);

            if (editIcon != null) btnEdit.setIcon(editIcon);
            else btnEdit.setText("Sửa");

            if (deleteIcon != null) btnDelete.setIcon(deleteIcon);
            else btnDelete.setText("Xóa");

            btnEdit.setPreferredSize(new Dimension(30, 30));
            btnDelete.setPreferredSize(new Dimension(30, 30));

            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                int maNhomQuyen = (int) table.getValueAt(currentRow, 0);
                suaNhomQuyen(maNhomQuyen);
            });

            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                int maNhomQuyen = (int) table.getValueAt(currentRow, 0);
                String tenNhomQuyen = (String) table.getValueAt(currentRow, 1);
                xoaNhomQuyen(maNhomQuyen, tenNhomQuyen);
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