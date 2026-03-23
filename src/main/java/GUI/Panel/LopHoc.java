package GUI.Panel;

import BUS.LopBUS;
import BUS.NguoiDungBUS;
import BUS.MonHocBUS;
import DTO.LopDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietLopDialog;
import GUI.Dialog.LopDialog;
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

public class LopHoc extends JPanel implements ActionListener, ItemListener {

    private PanelBorderRadius pnlMain, functionBar;
    private GUI.Main mainFrame;
    private JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    private JTable tableLop;
    private JScrollPane scrollTable;
    private MainFunction mainFunction;
    private IntegratedSearch search;
    private DefaultTableModel tblModel;

    private LopBUS lopBUS = new LopBUS();
    private NguoiDungBUS nguoiDungBUS = new NguoiDungBUS();
    private MonHocBUS monHocBUS = new MonHocBUS();

    private ArrayList<LopDTO> listHienTai;

    private Color BackgroundColor = new Color(240, 247, 250);

    public LopHoc(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
        listHienTai = getListTheoRole();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // TABLE
        tableLop = new JTable();
        scrollTable = new JScrollPane();
        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] header = {"Mã lớp", "Tên lớp", "Môn học", "Giảng viên", "Sĩ số", "Năm học", "Học kỳ"};
        tblModel.setColumnIdentifiers(header);
        tableLop.setModel(tblModel);
        tableLop.setFocusable(false);
        tableLop.setRowHeight(30);
        tableLop.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableLop.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableLop.getColumnCount(); i++) {
            tableLop.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableLop.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableLop, 0, TableSorter.INTEGER_COMPARATOR);
        scrollTable.setViewportView(tableLop);

        // PADDING BORDERS
        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
        pnlBorder1.setBackground(BackgroundColor);
        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(BackgroundColor);
        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(BackgroundColor);
        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(BackgroundColor);

        this.add(pnlBorder1, BorderLayout.NORTH);
        this.add(pnlBorder2, BorderLayout.SOUTH);
        this.add(pnlBorder3, BorderLayout.EAST);
        this.add(pnlBorder4, BorderLayout.WEST);

        // CONTENT CENTER
        contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BackgroundColor);
        this.add(contentCenter, BorderLayout.CENTER);

        // FUNCTION BAR
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        functionBar.setBackground(Color.WHITE);

        String[] action = {"create", "update", "delete", "detail", "import", "export"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "5", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        // SEARCH BAR
        search = new IntegratedSearch(new String[]{"Tất cả", "Mã lớp", "Tên lớp", "Năm học"});
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
            listHienTai = getListTheoRole();
            loadDataTable(listHienTai);
        });
        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        // MAIN TABLE PANEL
        pnlMain = new PanelBorderRadius();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.add(scrollTable, BorderLayout.CENTER);
        contentCenter.add(pnlMain, BorderLayout.CENTER);
    }

    public void loadDataTable(ArrayList<LopDTO> danhSach) {
        tblModel.setRowCount(0);
        for (LopDTO lop : danhSach) {
            String tenGV = "Không xác định";
            if (nguoiDungBUS.getById(lop.getGiangvien()) != null) {
                tenGV = nguoiDungBUS.getById(lop.getGiangvien()).getHoten();
            }

            String tenMH = "Không xác định";
            if (monHocBUS.getById(lop.getMamonhoc()) != null) {
                tenMH = monHocBUS.getById(lop.getMamonhoc()).getTenmonhoc();
            }

            tblModel.addRow(new Object[]{
                lop.getMalop(),
                lop.getTenlop(),
                tenMH,
                tenGV,
                lopBUS.countSiSoByMaLop(lop.getMalop()),
                lop.getNamhoc(),
                lop.getHocky()
            });
        }
    }

    public void thucHienTimKiem() {
        String kieu = (String) search.cbxChoose.getSelectedItem();
        String text = search.txtSearchForm.getText();
        ArrayList<LopDTO> sourceList = getListTheoRole();
        listHienTai = lopBUS.search(sourceList, text, kieu);
        loadDataTable(listHienTai);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object source = e.getSource();

        if (source == mainFunction.btn.get("create")) {
            new LopDialog(this, owner, "Thêm lớp học mới", true, "create", null, mainFrame.getNguoiDung());

        } else if (source == mainFunction.btn.get("update")) {
            int index = tableLop.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp học cần sửa!");
            } else {
                int modelRow = tableLop.convertRowIndexToModel(index);
                int malop = (int) tblModel.getValueAt(modelRow, 0);
                LopDTO selected = lopBUS.getById(malop);
                new LopDialog(this, owner, "Chỉnh sửa lớp học", true, "update", selected, mainFrame.getNguoiDung());
            }

        } else if (source == mainFunction.btn.get("detail")) {
            int index = tableLop.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp học để xem chi tiết!");
            } else {
                int modelRow = tableLop.convertRowIndexToModel(index);
                int malop = (int) tblModel.getValueAt(modelRow, 0);
                LopDTO selected = lopBUS.getById(malop);
                new ChiTietLopDialog(owner, "Chi tiết lớp học", true, selected);
            }

        } else if (source == mainFunction.btn.get("delete")) {
            int index = tableLop.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp học cần xóa!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa lớp này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int modelRow = tableLop.convertRowIndexToModel(index);
                    int malop = (int) tblModel.getValueAt(modelRow, 0);
                    if (lopBUS.delete(malop)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        listHienTai = getListTheoRole();
                        loadDataTable(listHienTai);
                    }
                }
            }
        } else if (source == mainFunction.btn.get("import")) {
            importExcel();
        } else if (source == mainFunction.btn.get("export")) {
            try {
                helper.JTableExporter.exportJTableToExcel(tableLop);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void importExcel() {
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (FileInputStream excelFIS = new FileInputStream(jf.getSelectedFile()); BufferedInputStream excelBIS = new BufferedInputStream(excelFIS); XSSFWorkbook excelJTableImport = new XSSFWorkbook(excelBIS)) {

                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);
                int countSuccess = 0, countError = 0;

                ArrayList<DTO.MonHocDTO> dsMonHoc = monHocBUS.getAll();
                ArrayList<DTO.NguoiDungDTO> dsNguoiDung = nguoiDungBUS.getAll();

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    if (excelRow == null) {
                        continue;
                    }

                    try {
                        String tenlop = excelRow.getCell(0).getStringCellValue();
                        int siso = (int) excelRow.getCell(1).getNumericCellValue();
                        int namhoc = (int) excelRow.getCell(2).getNumericCellValue();
                        int hocky = (int) excelRow.getCell(3).getNumericCellValue();

                        String tenGVExcel = excelRow.getCell(4).getStringCellValue().trim();
                        int magiangvien = -1;
                        for (DTO.NguoiDungDTO nd : dsNguoiDung) {
                            if (nd.getHoten().equalsIgnoreCase(tenGVExcel)) {
                                magiangvien = nd.getId();
                                break;
                            }
                        }

                        String tenMHExcel = excelRow.getCell(5).getStringCellValue().trim();
                        int mamonhoc = -1;
                        for (DTO.MonHocDTO mh : dsMonHoc) {
                            if (mh.getTenmonhoc().equalsIgnoreCase(tenMHExcel)) {
                                mamonhoc = mh.getMamonhoc();
                                break;
                            }
                        }

                        if (mamonhoc == -1 || magiangvien == -1) {
                            System.out.println("Lỗi dòng " + row + ": Không tìm thấy GV hoặc Môn học");
                            countError++;
                            continue;
                        }

                        LopDTO lop = new LopDTO(0, tenlop, siso, namhoc, hocky, 1, magiangvien, mamonhoc);

                        if (lopBUS.add(lop)) {
                            countSuccess++;
                        } else {
                            countError++;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        countError++;
                    }
                }
                JOptionPane.showMessageDialog(this, "Hoàn tất!\nThành công: " + countSuccess + "\nThất bại: " + countError);
                refreshData();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!");
            }
        }
    }

    private ArrayList<LopDTO> getListTheoRole() {
        int manhomquyen = mainFrame.getNguoiDung().getManhomquyen();
        if (manhomquyen == 2) {
            return lopBUS.getByGiangVien(mainFrame.getNguoiDung().getId());
        }
        return lopBUS.getAll();
    }

    public void refreshData() {
        listHienTai = getListTheoRole();
        loadDataTable(listHienTai);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }
}
