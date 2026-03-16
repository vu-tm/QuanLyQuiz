package GUI.Panel;

import GUI.Main;
import BUS.NguoiDungBUS;
import DTO.NguoiDungDTO;
import helper.IconHelper;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class NguoiDung extends JPanel {
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private NguoiDungBUS bus = new NguoiDungBUS();

    public NguoiDung() {
        initComponent();
        loadDataToTable();
        setupEvents();
    }

    private void initComponent() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // HEADER
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Tất cả người dùng");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(titleLabel, BorderLayout.WEST);

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

        // DATA TABLE
        String[] columns = {"ID", "Username", "Họ tên", "Giới tính", "Ngày sinh", "Nhóm quyền", "Trạng thái", "Hành động"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
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
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor());

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(70);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(80);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupEvents() {
        searchField.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            List<NguoiDungDTO> list = bus.search(keyword);
            loadDataToTable(list);
        });
    }

    private void loadDataToTable() {
        List<NguoiDungDTO> list = bus.getAll();
        loadDataToTable(list);
    }

    private void loadDataToTable(List<NguoiDungDTO> list) {
        tableModel.setRowCount(0);
        for (NguoiDungDTO user : list) {
            String gioiTinh = user.isGioitinh() ? "Nam" : "Nữ";
            String trangThai = user.getTrangthai() == 1 ? "Hoạt động" : "Đã khóa";
            String nhomQuyen = bus.getTenNhomQuyen(user.getManhomquyen()); // Dùng bus
            String ngaySinh = user.getNgaysinh() != null ? user.getNgaysinh().toString() : "N/A";

            tableModel.addRow(new Object[]{
                user.getId(),
                user.getUsername(),
                user.getHoten(),
                gioiTinh,
                ngaySinh,
                nhomQuyen,
                trangThai,
                ""
            });
        }
    }

    // SỬA NGƯỜI DÙNG
    private void suaNguoiDung(String id) {
        NguoiDungDTO user = bus.getById(id);
        if (user != null) {
            Main mainFrame = (Main) SwingUtilities.getWindowAncestor(this);
            AddNguoiDung editPanel = new AddNguoiDung(user, () -> {
                mainFrame.setPanel(new NguoiDung());
            });
            mainFrame.setPanel(editPanel);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // XOÁ NGƯỜI DÙNG
    private void xoaNguoiDung(String id, String hoten) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa người dùng \"" + hoten + "\"",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bus.deleteHard(id); // Xóa cứng
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
    
    // THÊM NGƯỜI DÙNG
    private void themNguoiDung() {
        Main mainFrame = (Main) SwingUtilities.getWindowAncestor(this);
        AddNguoiDung addPanel = new AddNguoiDung(null, () -> {
            mainFrame.setPanel(new NguoiDung());
        });
        mainFrame.setPanel(addPanel);
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