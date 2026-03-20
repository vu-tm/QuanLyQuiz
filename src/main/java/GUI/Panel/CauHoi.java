package GUI.Panel;

import BUS.CauHoiBUS;
import BUS.DoKhoBUS;
import BUS.LoaiCauHoiBUS;
import BUS.MonHocBUS;
import DTO.CauHoiDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.AddCauHoiDialog;
import GUI.Dialog.ViewCauHoiDialog;
import helper.Validation;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CauHoi extends JPanel implements ActionListener, ItemListener {
    private final CauHoiBUS bus;
    private final MonHocBUS monHocBUS;
    private final DoKhoBUS doKhoBUS;
    private final LoaiCauHoiBUS loaiCauHoiBUS;
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollTable;
    private MainFunction mainFunction;
    private IntegratedSearch search;
    private PanelBorderRadius functionBar, main;
    private JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    private final Color backgroundColor = new Color(240, 247, 250);
    private ArrayList<CauHoiDTO> listHienTai;

    public CauHoi() {
        this.bus = new CauHoiBUS();
        this.monHocBUS = new MonHocBUS();
        this.doKhoBUS = new DoKhoBUS();
        this.loaiCauHoiBUS = new LoaiCauHoiBUS();
        this.listHienTai = new ArrayList<>(bus.load());
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        table = new JTable();
        scrollTable = new JScrollPane();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] header = {"Mã câu hỏi", "Nội dung", "Tên độ khó", "Tên loại", "Tên môn học", "Người tạo", "Trạng thái"};
        model.setColumnIdentifiers(header);
        table.setModel(model);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        table.setFocusable(false);
        table.setRowHeight(30);
        scrollTable.setViewportView(table);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(table, 0, TableSorter.INTEGER_COMPARATOR);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    SwingUtilities.invokeLater(() -> onViewDetail());
                }
            }
        });

        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
        pnlBorder1.setBackground(backgroundColor);
        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(backgroundColor);
        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(backgroundColor);
        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(backgroundColor);

        this.add(pnlBorder1, BorderLayout.NORTH);
        this.add(pnlBorder2, BorderLayout.SOUTH);
        this.add(pnlBorder3, BorderLayout.EAST);
        this.add(pnlBorder4, BorderLayout.WEST);

        contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(backgroundColor);
        this.add(contentCenter, BorderLayout.CENTER);

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        functionBar.setBackground(Color.WHITE);

        String[] action = {"create", "update", "delete", "import", "export"};
        mainFunction = new MainFunction(action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }

        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã câu hỏi", "Nội dung", "Tên môn học", "Người tạo"});
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
            listHienTai = new ArrayList<>(bus.load());
            loadDataTable(listHienTai);
        });
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTable, BorderLayout.CENTER);
        contentCenter.add(main, BorderLayout.CENTER);
    }

    public void thucHienTimKiem() {
        String kieu = String.valueOf(search.cbxChoose.getSelectedItem());
        String text = search.txtSearchForm.getText().trim().toLowerCase(Locale.ROOT);

        if (Validation.isEmpty(text)) {
            listHienTai = new ArrayList<>(bus.load());
            loadDataTable(listHienTai);
            return;
        }

        ArrayList<CauHoiDTO> filtered = new ArrayList<>();
        for (CauHoiDTO ch : bus.load()) {
            String ma = String.valueOf(ch.getMacauhoi()).toLowerCase(Locale.ROOT);
            String noiDung = String.valueOf(ch.getNoidung()).toLowerCase(Locale.ROOT);
            String tenMon = monHocBUS.getTenById(ch.getMamonhoc()).toLowerCase(Locale.ROOT);
            String nguoiTao = String.valueOf(ch.getNguoitao()).toLowerCase(Locale.ROOT);

            boolean match;
            switch (kieu) {
                case "Mã câu hỏi":
                    match = ma.contains(text);
                    break;
                case "Nội dung":
                    match = noiDung.contains(text);
                    break;
                case "Tên môn học":
                    match = tenMon.contains(text);
                    break;
                case "Người tạo":
                    match = nguoiTao.contains(text);
                    break;
                default:
                    match = ma.contains(text) || noiDung.contains(text) || tenMon.contains(text) || nguoiTao.contains(text);
                    break;
            }

            if (match) {
                filtered.add(ch);
            }
        }

        listHienTai = filtered;
        loadDataTable(listHienTai);
    }

    public void loadData() {
        listHienTai = new ArrayList<>(bus.load());
        loadDataTable(listHienTai);
    }

    public void loadDataTable(ArrayList<CauHoiDTO> danhSach) {
        model.setRowCount(0);
        for (CauHoiDTO ch : danhSach) {
            String tenDoKho = doKhoBUS.getTenDoKho(ch.getMadokho());
            String tenLoai = loaiCauHoiBUS.getTenById(ch.getMaloai());
            String tenMonHoc = monHocBUS.getTenById(ch.getMamonhoc());
            
            model.addRow(new Object[]{
                ch.getMacauhoi(),
                ch.getNoidung(),
                tenDoKho,
                tenLoai,
                tenMonHoc,
                ch.getNguoitao(),
                ch.getTrangthai() == 1 ? "Hoạt động" : "Ngưng hoạt động"
            });
        }
    }

    private CauHoiDTO getSelectedDTO() {
        int index = table.getSelectedRow();
        if (index < 0) {
            return null;
        }
        int macauhoi = Integer.parseInt(String.valueOf(table.getValueAt(index, 0)));
        for (CauHoiDTO ch : bus.load()) {
            if (ch.getMacauhoi() == macauhoi) {
                return ch;
            }
        }
        return null;
    }

    private void onAdd() {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        new AddCauHoiDialog(this, owner, "Thêm câu hỏi mới", null);
    }

    private void onViewDetail() {
        CauHoiDTO selected = getSelectedDTO();
        if (selected == null) {
            return;
        }
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        new ViewCauHoiDialog(owner, selected);
    }

    private void onUpdate() {
        CauHoiDTO selected = getSelectedDTO();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn câu hỏi để sửa!");
            return;
        }
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        new AddCauHoiDialog(this, owner, "Chỉnh sửa câu hỏi", selected);
    }

    private void onDelete() {
        CauHoiDTO selected = getSelectedDTO();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn câu hỏi để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xóa câu hỏi mã " + selected.getMacauhoi() + "?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            if (bus.remove(selected.getMacauhoi())) {
                loadData();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
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
                        String noidung = excelRow.getCell(0).getStringCellValue().trim();
                        int madokho = (int) excelRow.getCell(1).getNumericCellValue();
                        int maloai = (int) excelRow.getCell(2).getNumericCellValue();
                        int mamonhoc = (int) excelRow.getCell(3).getNumericCellValue();
                        String nguoitao = excelRow.getCell(4).getStringCellValue().trim();
                        int trangthai = 1;

                        if (excelRow.getCell(5) != null) {
                            trangthai = (int) excelRow.getCell(5).getNumericCellValue();
                        }

                        if (Validation.isEmpty(noidung) || Validation.isEmpty(nguoitao)) {
                            countError++;
                            continue;
                        }

                        CauHoiDTO ch = new CauHoiDTO();
                        ch.setNoidung(noidung);
                        ch.setMadokho(madokho);
                        ch.setMaloai(maloai);
                        ch.setMamonhoc(mamonhoc);
                        ch.setNguoitao(nguoitao);
                        ch.setTrangthai(trangthai);

                        if (bus.add(ch)) {
                            countSuccess++;
                        } else {
                            countError++;
                        }
                    } catch (Exception e) {
                        countError++;
                    }
                }
                JOptionPane.showMessageDialog(this, "Nhập thành công " + countSuccess + " dòng. Lỗi " + countError + " dòng.");
                loadData();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void exportExcel() {
        try {
            helper.JTableExporter.exportJTableToExcel(table);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Xuất file Excel thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == mainFunction.btn.get("create")) {
            onAdd();
        } else if (source == mainFunction.btn.get("update")) {
            onUpdate();
        } else if (source == mainFunction.btn.get("delete")) {
            onDelete();
        } else if (source == mainFunction.btn.get("import")) {
            importExcel();
        } else if (source == mainFunction.btn.get("export")) {
            exportExcel();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }
}
