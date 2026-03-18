package GUI.Panel;

import GUI.Main;
import BUS.NguoiDungBUS;
import DTO.NguoiDungDTO;
import helper.IconHelper;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton btnImport = new JButton("Nhập Excel");
        btnImport.setFont(new Font("Arial", Font.BOLD, 14));
        btnImport.setBackground(new Color(76, 175, 80));
        btnImport.setForeground(Color.WHITE);
        btnImport.setFocusPainted(false);
        btnImport.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnImport.addActionListener(e -> importExcel());

        JButton btnExport = new JButton("Xuất Excel");
        btnExport.setFont(new Font("Arial", Font.BOLD, 14));
        btnExport.setBackground(new Color(33, 150, 243));
        btnExport.setForeground(Color.WHITE);
        btnExport.setFocusPainted(false);
        btnExport.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnExport.addActionListener(e -> exportExcel());

        actionPanel.add(btnImport);
        actionPanel.add(btnExport);
        actionPanel.add(btnAdd);
        northPanel.add(actionPanel, BorderLayout.EAST);

        add(northPanel, BorderLayout.NORTH);

        // BẢNG VÀ SEARCH BAR
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // PANEL TÌM KIẾM + ICON SEARCH
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);

        JLabel searchIconLabel = new JLabel();
        ImageIcon searchIcon = IconHelper.loadIcon("find.svg", 20, 20);
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
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddNguoiDung dialog = new AddNguoiDung(
            parentFrame,
            "Thêm người dùng",
            null,
            this::loadDataToTable
        );
        dialog.setVisible(true);
    }

    // SỬA NGƯỜI DÙNG
    private void suaNguoiDung(String id) {
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

    private void importExcel() {
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(this);
        int countSuccess = 0, countError = 0;
        DataFormatter formatter = new DataFormatter();

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = jf.getSelectedFile();
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    if (excelRow == null) {
                        continue;
                    }
                    try {
                        String id = formatter.formatCellValue(excelRow.getCell(0)).trim();
                        String username = formatter.formatCellValue(excelRow.getCell(1)).trim();
                        String hoten = formatter.formatCellValue(excelRow.getCell(2)).trim();
                        String gioiTinhText = formatter.formatCellValue(excelRow.getCell(3)).trim();
                        Date ngaysinh = parseExcelDate(excelRow.getCell(4), formatter);
                        String matkhau = formatter.formatCellValue(excelRow.getCell(5)).trim();
                        int manhomquyen = Integer.parseInt(formatter.formatCellValue(excelRow.getCell(6)).trim());
                        int trangthai = 1;

                        if (excelRow.getCell(7) != null) {
                            String trangthaiStr = formatter.formatCellValue(excelRow.getCell(7)).trim();
                            if (!trangthaiStr.isEmpty()) {
                                trangthai = Integer.parseInt(trangthaiStr);
                            }
                        }

                        if (id.isEmpty() || username.isEmpty() || hoten.isEmpty() || ngaysinh == null) {
                            countError++;
                            continue;
                        }

                        if (bus.checkExistId(id) || bus.checkExistUsername(username)) {
                            countError++;
                            continue;
                        }

                        NguoiDungDTO user = new NguoiDungDTO();
                        user.setId(id);
                        user.setUsername(username);
                        user.setHoten(hoten);
                        user.setGioitinh(parseGender(gioiTinhText));
                        user.setNgaysinh(ngaysinh);
                        user.setMatkhau(matkhau);
                        user.setTrangthai(trangthai);
                        user.setManhomquyen(manhomquyen);

                        if (bus.insert(user)) {
                            countSuccess++;
                        } else {
                            countError++;
                        }
                    } catch (Exception ex) {
                        countError++;
                    }
                }

                JOptionPane.showMessageDialog(this, "Nhập thành công " + countSuccess + " dòng. Lỗi " + countError + " dòng.");
                loadDataToTable();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!");
            } finally {
                try {
                    if (excelJTableImport != null) {
                        excelJTableImport.close();
                    }
                    if (excelBIS != null) {
                        excelBIS.close();
                    }
                    if (excelFIS != null) {
                        excelFIS.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void exportExcel() {
        try {
            helper.JTableExporter.exportJTableToExcel(table);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Xuất file Excel thất bại!");
        }
    }

    private boolean parseGender(String value) {
        String normalized = value == null ? "" : value.trim().toLowerCase();
        return normalized.equals("1") || normalized.equals("true") || normalized.equals("nam") || normalized.equals("male");
    }

    private Date parseExcelDate(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return new Date(cell.getDateCellValue().getTime());
        }

        String value = formatter.formatCellValue(cell).trim();
        if (value.isEmpty()) {
            return null;
        }

        try {
            return Date.valueOf(LocalDate.parse(value)); // yyyy-MM-dd
        } catch (Exception e) {
            try {
                String[] parts = value.split("/");
                if (parts.length == 3) {
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);
                    return Date.valueOf(LocalDate.of(year, month, day));
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
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