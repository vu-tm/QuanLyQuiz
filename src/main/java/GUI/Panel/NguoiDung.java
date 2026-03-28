package GUI.Panel;

import BUS.NguoiDungBUS;
import DAO.NguoiDungDAO;
import DTO.NguoiDungDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.NguoiDungDialog;
import helper.Formater;
import helper.Validation;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class NguoiDung extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius pnlMain, functionBar;
    private GUI.Main mainFrame;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableNguoiDung;
    JScrollPane scrollTableNguoiDung;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    NguoiDungBUS nguoidungBUS = new NguoiDungBUS();
    ArrayList<NguoiDungDTO> listHienTai = nguoidungBUS.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public NguoiDung(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        tableNguoiDung = new JTable();
        scrollTableNguoiDung = new JScrollPane();

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] header = new String[]{"Mã người dùng", "Tên đăng nhập", "Họ tên", "Giới tính", "Ngày sinh", "Nhóm quyền", "Trạng thái"};
        tblModel.setColumnIdentifiers(header);
        tableNguoiDung.setModel(tblModel);
        tableNguoiDung.setFocusable(false);
        tableNguoiDung.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableNguoiDung.getTableHeader().setPreferredSize(new Dimension(0, 40));
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableNguoiDung.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scrollTableNguoiDung.setViewportView(tableNguoiDung);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableNguoiDung.getColumnCount(); i++) {
            tableNguoiDung.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableNguoiDung.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableNguoiDung, 0, TableSorter.INTEGER_COMPARATOR);

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

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        functionBar.setBackground(Color.WHITE);

        String[] action = {"create", "update", "delete", "detail", "import", "export"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "6", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã", "Tài khoản", "Họ tên", "Nhóm quyền"});
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                thucHienTimKiem();
            }
        });
        search.cbxChoose.addItemListener(this);
        search.btnReset.addActionListener(e -> {
            search.txtSearchForm.setText("");
            search.cbxChoose.setSelectedIndex(0);
            listHienTai = (ArrayList<NguoiDungDTO>) nguoidungBUS.getAll();
            loadDataTable(listHienTai);
        });

        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        pnlMain = new PanelBorderRadius();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.add(scrollTableNguoiDung, BorderLayout.CENTER);
        contentCenter.add(pnlMain, BorderLayout.CENTER);
    }

    public void thucHienTimKiem() {
        String kieuTimKiem = (String) search.cbxChoose.getSelectedItem();
        String noiDungTim = search.txtSearchForm.getText();
        listHienTai = (ArrayList<NguoiDungDTO>) nguoidungBUS.search(noiDungTim, kieuTimKiem);
        loadDataTable(listHienTai);
    }

    public void loadDataTable(ArrayList<NguoiDungDTO> danhSach) {
        tblModel.setRowCount(0);
        for (NguoiDungDTO user : danhSach) {
            String trangThaiText = "";
            int tt = user.getTrangthai();
            if (tt == 1) {
                trangThaiText = "Hoạt động";
            } else if (tt == 0) {
                trangThaiText = "Ngưng hoạt động";
            }

            tblModel.addRow(new Object[]{
                user.getManguoidung(),
                user.getUsername(),
                user.getHoten(),
                nguoidungBUS.getGioiTinhText(user.isGioitinh()),
                user.getNgaysinh() != null ? Formater.FormatDate(user.getNgaysinh()) : "",
                nguoidungBUS.getTenNhomQuyen(user.getManhomquyen()),
                trangThaiText
            });
        }
    }

    public void importExcel() {
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(this);
        int countSuccess = 0;
        int countError = 0;

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = jf.getSelectedFile();
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);
                org.apache.poi.ss.usermodel.DataFormatter formatter = new org.apache.poi.ss.usermodel.DataFormatter();

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    if (excelRow == null) {
                        continue;
                    }

                    try {
                        int id = Integer.parseInt(formatter.formatCellValue(excelRow.getCell(0)));
                        String username = formatter.formatCellValue(excelRow.getCell(1));
                        String hoten = formatter.formatCellValue(excelRow.getCell(2));
                        String gioiTinhStr = formatter.formatCellValue(excelRow.getCell(3));

                        if (Validation.isEmpty(username) || Validation.isEmpty(hoten)) {
                            countError++;
                            continue;
                        }

                        NguoiDungDTO user = new NguoiDungDTO();
                        user.setManguoidung(id);
                        user.setUsername(username);
                        user.setHoten(hoten);
                        user.setGioitinh(gioiTinhStr.equalsIgnoreCase("Nam"));
                        user.setMatkhau("123"); // Default
                        user.setTrangthai(1);
                        user.setManhomquyen(3); // Default Sinh viên

                        if (nguoidungBUS.add(user)) {
                            countSuccess++;
                        } else {
                            countError++;
                        }
                    } catch (Exception e) {
                        countError++;
                    }
                }
                JOptionPane.showMessageDialog(this, "Nhập thành công " + countSuccess + " dòng. Lỗi " + countError + " dòng.");
                listHienTai = (ArrayList<NguoiDungDTO>) nguoidungBUS.getAll();
                loadDataTable(listHienTai);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object source = e.getSource();

        if (source == mainFunction.btn.get("create")) {
            new NguoiDungDialog(this, owner, "Thêm người dùng mới", true, "create", null);
        } else if (source == mainFunction.btn.get("update")) {
            int index = tableNguoiDung.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần sửa!");
            } else {
                int modelRow = tableNguoiDung.convertRowIndexToModel(index);
                int id = (int) tblModel.getValueAt(modelRow, 0);
                NguoiDungDTO selected = nguoidungBUS.getById(id);
                new NguoiDungDialog(this, owner, "Chỉnh sửa người dùng", true, "update", selected);
            }
        } else if (source == mainFunction.btn.get("detail")) {
            int index = tableNguoiDung.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần xem!");
            } else {
                int modelRow = tableNguoiDung.convertRowIndexToModel(index);
                int id = (int) tblModel.getValueAt(modelRow, 0);
                NguoiDungDTO selected = nguoidungBUS.getById(id);
                new NguoiDungDialog(this, owner, "Thông tin chi tiết", true, "view", selected);
            }
        } else if (source == mainFunction.btn.get("delete")) {
            int index = tableNguoiDung.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn người dùng cần xóa!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int modelRow = tableNguoiDung.convertRowIndexToModel(index);
                    int id = (int) tblModel.getValueAt(modelRow, 0);
                    if (nguoidungBUS.delete(id)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        listHienTai = nguoidungBUS.getAll();
                        loadDataTable(listHienTai);
                    }
                }
            }
        } else if (source == mainFunction.btn.get("import")) {
            importExcel();
        } else if (source == mainFunction.btn.get("export")) {
            try {
                helper.JTableExporter.exportJTableToExcel(tableNguoiDung);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }
}
