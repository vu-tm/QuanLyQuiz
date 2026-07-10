package GUI.Panel;

import BUS.DeThiBUS;
import BUS.KyThiBUS;
import BUS.MonHocBUS;
import BUS.NguoiDungBUS;
import DTO.DeThiDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PaginatedTable;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietDeThiDialog;
import GUI.Dialog.DeThiDialog;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DeThi extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius pnlMain, functionBar;
    private GUI.Main mainFrame;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable table;
    PaginatedTable paginatedTable;
    MainFunction mainFunction;
    IntegratedSearch search;

    private final NguoiDungBUS ndBUS = new NguoiDungBUS();
    private final DeThiBUS bus = new DeThiBUS();
    private final KyThiBUS kyThiBUS = new KyThiBUS();
    private final MonHocBUS monHocBUS = new MonHocBUS();

    private ArrayList<DeThiDTO> listHienTai = bus.getAll();
    private final Color backgroundColor = new Color(240, 247, 250);

    public DeThi(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        String[] header = {"Mã đề", "Tên đề thi", "Kỳ thi", "Môn học", "Thời gian", "Tổng câu", "Người tạo"};
        paginatedTable = new PaginatedTable(header);
        table = paginatedTable.getTable();

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setFocusable(false);
        table.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(70);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);

        // Cấu hình sắp xếp toàn bộ dữ liệu
        @SuppressWarnings("unchecked")
        Comparator<Object>[] comps = new Comparator[7];
        comps[0] = TableSorter.INTEGER_COMPARATOR;          // Mã đề
        comps[1] = TableSorter.STRING_COMPARATOR;           // Tên đề
        comps[2] = TableSorter.STRING_COMPARATOR;           // Kỳ thi
        comps[3] = TableSorter.STRING_COMPARATOR;           // Môn học
        comps[4] = TableSorter.STRING_COMPARATOR;           // Thời gian (có " phút")
        comps[5] = TableSorter.INTEGER_COMPARATOR;          // Tổng câu
        comps[6] = TableSorter.STRING_COMPARATOR;           // Người tạo
        paginatedTable.enableFullDataSorting(comps);

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

        String[] action = {"create", "update", "delete", "detail", "import", "export"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "2", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã đề", "Tên đề", "Người tạo"});
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
            listHienTai = bus.getAll();
            loadDataTable(listHienTai);
        });
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        pnlMain = new PanelBorderRadius();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.add(paginatedTable, BorderLayout.CENTER);
        contentCenter.add(pnlMain, BorderLayout.CENTER);
    }

    public void thucHienTimKiem() {
        String kieu = (String) search.cbxChoose.getSelectedItem();
        String text = search.txtSearchForm.getText();
        listHienTai = bus.search(text, kieu);
        loadDataTable(listHienTai);
    }

    public void loadDataTable(ArrayList<DeThiDTO> danhSach) {
        this.listHienTai = danhSach;
        List<Object[]> data = new ArrayList<>();
        for (DeThiDTO dt : danhSach) {
            data.add(new Object[]{
                dt.getMade(),
                dt.getTende(),
                kyThiBUS.getTenById(dt.getMakythi()),
                monHocBUS.getTenById(dt.getMonthi()),
                dt.getThoigianthi() + " phút",
                dt.getTongsocau(),
                ndBUS.getHotenById(dt.getNguoitao())
            });
        }
        paginatedTable.setData(data);
    }

    public void importExcel() {
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File excelFile = jf.getSelectedFile();
                FileInputStream excelFIS = new FileInputStream(excelFile);
                BufferedInputStream excelBIS = new BufferedInputStream(excelFIS);
                XSSFWorkbook excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);

                org.apache.poi.ss.usermodel.DataFormatter formatter = new org.apache.poi.ss.usermodel.DataFormatter();

                int countSuccess = 0;
                int countError = 0;

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    if (excelRow == null) {
                        continue;
                    }

                    try {
                        String tende = formatter.formatCellValue(excelRow.getCell(0)).trim();
                        String tenKyThi = formatter.formatCellValue(excelRow.getCell(1)).trim();
                        String tenMonHoc = formatter.formatCellValue(excelRow.getCell(2)).trim();
                        String thoiGianStr = formatter.formatCellValue(excelRow.getCell(3)).trim();

                        if (tende.isEmpty()) {
                            continue;
                        }

                        int makythi = -1;
                        for (var kt : kyThiBUS.getAll()) {
                            if (kt.getTenkythi().equalsIgnoreCase(tenKyThi)) {
                                makythi = kt.getMakythi();
                                break;
                            }
                        }

                        int mamonhoc = -1;
                        for (var mh : monHocBUS.getAll()) {
                            if (mh.getTenmonhoc().equalsIgnoreCase(tenMonHoc)) {
                                mamonhoc = mh.getMamonhoc();
                                break;
                            }
                        }

                        int thoigianthi = Integer.parseInt(thoiGianStr);

                        if (makythi != -1 && mamonhoc != -1) {
                            DeThiDTO dt = new DeThiDTO();
                            dt.setTende(tende);
                            dt.setMakythi(makythi);
                            dt.setMonthi(mamonhoc);
                            dt.setThoigianthi(thoigianthi);
                            dt.setNguoitao(mainFrame.getNguoiDung().getManguoidung());
                            dt.setThoigiantao(new java.sql.Timestamp(System.currentTimeMillis()));
                            dt.setTongsocau(0);
                            dt.setTrangthai(true);

                            if (bus.add(dt) > 0) {
                                countSuccess++;
                            } else {
                                countError++;
                            }
                        } else {
                            countError++;
                        }
                    } catch (Exception ex) {
                        countError++;
                    }
                }
                JOptionPane.showMessageDialog(this, "Nhập thành công " + countSuccess + " dòng. Lỗi " + countError + " dòng.");
                listHienTai = bus.getAll();
                loadDataTable(listHienTai);
                excelJTableImport.close();
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
            DeThiDTO newDe = new DeThiDTO();
            newDe.setNguoitao(mainFrame.getNguoiDung().getManguoidung());
            new DeThiDialog(this, owner, "Thêm đề thi mới", true, "create", newDe);
            // Load lại dữ liệu sau khi đóng dialog
            listHienTai = bus.getAll();
            loadDataTable(listHienTai);
        } else if (source == mainFunction.btn.get("update") || source == mainFunction.btn.get("detail") || source == mainFunction.btn.get("delete")) {
            int index = table.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi!");
                return;
            }
            int modelIndex = table.convertRowIndexToModel(index);
            int made = (int) table.getModel().getValueAt(modelIndex, 0);
            DeThiDTO selected = bus.getById(made);
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu đề thi!");
                return;
            }

            if (source == mainFunction.btn.get("update")) {
                new DeThiDialog(this, owner, "Chỉnh sửa đề thi", true, "update", selected);
                // Load lại dữ liệu sau khi đóng dialog
                listHienTai = bus.getAll();
                loadDataTable(listHienTai);
            } else if (source == mainFunction.btn.get("detail")) {
                new ChiTietDeThiDialog(owner, "Chi tiết đề thi", true, selected);
            } else if (source == mainFunction.btn.get("delete")) {
                if (JOptionPane.showConfirmDialog(this, "Xóa đề thi " + selected.getTende() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (bus.delete(selected.getMade())) {
                        listHienTai = bus.getAll();
                        loadDataTable(listHienTai);
                    }
                }
            }
        } else if (source == mainFunction.btn.get("import")) {
            importExcel();
        } else if (source == mainFunction.btn.get("export")) {
            try {
                helper.JTableExporter.exportJTableToExcel(table);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Xuất file Excel thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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