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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(BackgroundColor);
        add(pnlBorder3, BorderLayout.EAST);

        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(BackgroundColor);
        add(pnlBorder4, BorderLayout.WEST);

        contentCenter = new JPanel();
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        add(contentCenter, BorderLayout.CENTER);

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        functionBar.setBackground(Color.WHITE);

        String[] actions = {"create", "update", "delete", "detail", "import", "export"};
        mainFunction = new MainFunction(actions);
        for (String ac : actions) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "ID", "Username", "Họ tên"});
        searchField = search.txtSearchForm;
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                thucHienTimKiem();
            }
        });
        search.cbxChoose.addItemListener(this);
        search.btnReset.addActionListener(e -> {
            searchField.setText("");
            search.cbxChoose.setSelectedIndex(0);
            listHienTai = bus.getAll();
            loadDataTable();
        });
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

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
                    default: // Tất cả
                        match = user.getId().toLowerCase().contains(tuKhoa)
                                || user.getUsername().toLowerCase().contains(tuKhoa)
                                || user.getHoten().toLowerCase().contains(tuKhoa);
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

    private void xoaNguoiDung() { // chuyen trang thai ve 0
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
            new ChiTietNguoiDungDialog(owner, "Thông tin người dùng", true, user);
        }
    }

    // IMPORT EXCEL
    private void importExcel() {
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(this);
        int countSuccess = 0, countError = 0;

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = jf.getSelectedFile();
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                // Bỏ qua header (dòng 0)
                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    if (excelRow == null) continue;
                    try {
                        String id = excelRow.getCell(0).getStringCellValue();
                        String username = excelRow.getCell(1).getStringCellValue();
                        String hoten = excelRow.getCell(2).getStringCellValue();
                        String gioiTinhStr = excelRow.getCell(3).getStringCellValue();
                        String ngaySinhStr = excelRow.getCell(4).getStringCellValue();
                        int manhomquyen = (int) excelRow.getCell(5).getNumericCellValue();
                        int trangthai = (int) excelRow.getCell(6).getNumericCellValue();

                        // Kiểm tra dữ liệu bắt buộc
                        if (Validation.isEmpty(id) || Validation.isEmpty(username) || Validation.isEmpty(hoten)) {
                            countError++;
                            continue;
                        }
                        // Kiểm tra trùng
                        if (bus.checkExistId(id) || bus.checkExistUsername(username)) {
                            countError++;
                            continue;
                        }

                        boolean gioitinh = "Nam".equalsIgnoreCase(gioiTinhStr);

                        java.sql.Date ngaySinh = null;
                        if (!Validation.isEmpty(ngaySinhStr)) {
                            try {
                                java.util.Date utilDate = dateFormat.parse(ngaySinhStr);
                                ngaySinh = new java.sql.Date(utilDate.getTime());
                            } catch (ParseException e) {
                                countError++;
                                continue;
                            }
                        }

                        // Giá trị mặc định cho mật khẩu (có thể cho nhập từ file nếu muốn)
                        String matkhau = "123";

                        NguoiDungDTO nd = new NguoiDungDTO(id, username, hoten, gioitinh, ngaySinh, matkhau, trangthai, manhomquyen);
                        if (bus.insert(nd)) {
                            countSuccess++;
                        } else {
                            countError++;
                        }
                    } catch (Exception e) {
                        countError++;
                    }
                }
                JOptionPane.showMessageDialog(this, "Nhập thành công " + countSuccess + " dòng. Lỗi " + countError + " dòng.");
                listHienTai = bus.getAll();
                loadDataTable();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!");
            } finally {
                try {
                    if (excelJTableImport != null) excelJTableImport.close();
                    if (excelBIS != null) excelBIS.close();
                    if (excelFIS != null) excelFIS.close();
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
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Xuất Excel thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}