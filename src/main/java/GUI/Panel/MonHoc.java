package GUI.Panel;

import BUS.MonHocBUS;
import DTO.MonHocDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PaginatedTable;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.MonHocDialog;
import helper.Validation;
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

public class MonHoc extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius pnlMain, functionBar;
    private GUI.Main mainFrame;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable table;
    PaginatedTable paginatedTable;
    MainFunction mainFunction;
    IntegratedSearch search;

    MonHocBUS bus = new MonHocBUS();
    ArrayList<MonHocDTO> listHienTai = bus.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public MonHoc(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        String[] header = {"Mã môn học", "Tên môn học", "Số tín chỉ"};
        paginatedTable = new PaginatedTable(header);
        table = paginatedTable.getTable();

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setFocusable(false);
        table.setRowHeight(40);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);

        table.setAutoCreateRowSorter(false);
        Comparator<Object>[] comps = new Comparator[3];
        comps[0] = (Object o1, Object o2) -> {
            int id1 = Integer.parseInt(o1.toString().replace("MH-", ""));
            int id2 = Integer.parseInt(o2.toString().replace("MH-", ""));
            return Integer.compare(id1, id2);
        };
        comps[1] = TableSorter.STRING_COMPARATOR;
        comps[2] = (Object o1, Object o2) -> {
            int c1 = Integer.parseInt(o1.toString());
            int c2 = Integer.parseInt(o2.toString());
            return Integer.compare(c1, c2);
        };
        paginatedTable.enableFullDataSorting(comps);

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

        contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BackgroundColor);
        this.add(contentCenter, BorderLayout.CENTER);

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        functionBar.setBackground(Color.WHITE);

        String[] action = {"create", "update", "delete", "detail", "import", "export"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "4", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }

        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã môn học", "Tên môn học"});
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

    public void importExcel() {
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(null);
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
                        String tenMonHoc = excelRow.getCell(0).getStringCellValue();
                        int soTinChi = (int) excelRow.getCell(1).getNumericCellValue();

                        if (Validation.isEmpty(tenMonHoc)) {
                            countError++;
                            continue;
                        }

                        MonHocDTO mh = new MonHocDTO();
                        mh.setTenmonhoc(tenMonHoc);
                        mh.setSotinchi(soTinChi);
                        mh.setTrangthai(1);

                        if (bus.add(mh)) {
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
                loadDataTable(listHienTai);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!");
            }
        }
    }

    public void loadDataTable(ArrayList<MonHocDTO> danhSach) {
        this.listHienTai = danhSach;
        List<Object[]> data = new ArrayList<>();
        for (MonHocDTO mh : danhSach) {
            data.add(new Object[]{
                "MH-" + mh.getMamonhoc(),
                mh.getTenmonhoc(),
                mh.getSotinchi()
            });
        }
        paginatedTable.setData(data);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object source = e.getSource();

        if (source == mainFunction.btn.get("create")) {
            new MonHocDialog(this, owner, "Thêm môn học mới", true, "create", null);
        } else if (source == mainFunction.btn.get("update") || source == mainFunction.btn.get("detail") || source == mainFunction.btn.get("delete")) {
            int index = table.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn môn học!");
                return;
            }
            String maStr = table.getValueAt(index, 0).toString();
            int mamonhoc = Integer.parseInt(maStr.replace("MH-", ""));
            MonHocDTO selected = bus.getById(mamonhoc);

            if (source == mainFunction.btn.get("update")) {
                new MonHocDialog(this, owner, "Chỉnh sửa môn học", true, "update", selected);
            } else if (source == mainFunction.btn.get("detail")) {
                new MonHocDialog(this, owner, "Thông tin chi tiết môn học", true, "view", selected);
            } else if (source == mainFunction.btn.get("delete")) {
                if (JOptionPane.showConfirmDialog(this, "Xóa môn học " + selected.getTenmonhoc() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (bus.delete(selected.getMamonhoc())) {
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