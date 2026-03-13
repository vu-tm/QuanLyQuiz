package GUI.Panel;

import BUS.KyThiBUS;
import DTO.KyThiDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.KyThiDialog;
import helper.Formater;
import helper.Validation;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class KyThi extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableKyThi;
    JScrollPane scrollTableKyThi;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    KyThiBUS kythiBUS = new KyThiBUS();
    ArrayList<KyThiDTO> listHienTai = kythiBUS.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public KyThi() {
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        tableKyThi = new JTable();
        scrollTableKyThi = new JScrollPane();

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] header = new String[]{"Mã kỳ thi", "Tên kỳ thi", "Thời gian bắt đầu", "Thời gian kết thúc", "Trạng thái"};
        tblModel.setColumnIdentifiers(header);
        tableKyThi.setModel(tblModel);
        tableKyThi.setFocusable(false);
        scrollTableKyThi.setViewportView(tableKyThi);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableKyThi.getColumnCount(); i++) {
            tableKyThi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableKyThi.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableKyThi, 0, TableSorter.INTEGER_COMPARATOR);

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
        mainFunction = new MainFunction(action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã kỳ thi", "Tên kỳ thi"});
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
            listHienTai = kythiBUS.getAll();
            loadDataTable(listHienTai);
        });

        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTableKyThi, BorderLayout.CENTER);
        contentCenter.add(main, BorderLayout.CENTER);
    }

    public void thucHienTimKiem() {
        String kieuTimKiem = (String) search.cbxChoose.getSelectedItem();
        String noiDungTim = search.txtSearchForm.getText();
        listHienTai = kythiBUS.search(noiDungTim, kieuTimKiem);
        loadDataTable(listHienTai);
    }

    public void importExcel() {
        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(null);
        int countSuccess = 0;
        int countError = 0;

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = jf.getSelectedFile();
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);

                // Chạy từ dòng 1 (bỏ qua header dòng 0)
                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    if (excelRow == null) {
                        continue;
                    }

                    try {
                        String tenKyThi = excelRow.getCell(0).getStringCellValue();
                        String tgBatDauStr = excelRow.getCell(1).getStringCellValue();
                        String tgKetThucStr = excelRow.getCell(2).getStringCellValue();

                        if (Validation.isEmpty(tenKyThi)) {
                            countError++;
                            continue;
                        }

                        KyThiDTO kt = new KyThiDTO();
                        kt.setTenkythi(tenKyThi);
                        kt.setThoigianbatdau(Timestamp.valueOf(tgBatDauStr));
                        kt.setThoigianketthuc(Timestamp.valueOf(tgKetThucStr));
                        kt.setTrangthai(1); // Mặc định hoạt động

                        if (kythiBUS.add(kt)) {
                            countSuccess++;
                        } else {
                            countError++;
                        }
                    } catch (Exception e) {
                        countError++;
                    }
                }
                JOptionPane.showMessageDialog(this, "Nhập thành công " + countSuccess + " dòng. Lỗi " + countError + " dòng.");
                listHienTai = kythiBUS.getAll();
                loadDataTable(listHienTai);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!");
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e
    ) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }

    public void loadDataTable(ArrayList<KyThiDTO> danhSach) {
        tblModel.setRowCount(0);
        for (int i = 0; i < danhSach.size(); i++) {
            KyThiDTO kyThi = danhSach.get(i);
            tblModel.addRow(new Object[]{
                kyThi.getMakythi(),
                kyThi.getTenkythi(),
                Formater.FormatTime(kyThi.getThoigianbatdau()),
                Formater.FormatTime(kyThi.getThoigianketthuc()),
                kyThi.getTrangthai() == 1 ? "Hoạt động" : "Tạm dừng"
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object source = e.getSource();

        if (source == mainFunction.btn.get("create")) {
            new KyThiDialog(this, owner, "Thêm kỳ thi mới", true, "create", null);

        } else if (source == mainFunction.btn.get("update")) {
            int index = tableKyThi.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kỳ thi cần sửa!");
            } else {
                int id = (int) tableKyThi.getValueAt(index, 0);
                KyThiDTO selected = null;
                for (KyThiDTO item : listHienTai) {
                    if (item.getMakythi() == id) {
                        selected = item;
                        break;
                    }
                }
                new KyThiDialog(this, owner, "Chỉnh sửa kỳ thi", true, "update", selected);
            }

        } else if (source == mainFunction.btn.get("detail")) {
            int index = tableKyThi.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kỳ thi cần xem!");
            } else {
                int id = (int) tableKyThi.getValueAt(index, 0);
                KyThiDTO selected = null;
                for (KyThiDTO item : listHienTai) {
                    if (item.getMakythi() == id) {
                        selected = item;
                        break;
                    }
                }
                new KyThiDialog(this, owner, "Thông tin chi tiết kỳ thi", true, "view", selected);
            }

        } else if (source == mainFunction.btn.get("delete")) {
            int index = tableKyThi.getSelectedRow();
            if (index != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa kỳ thi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) tableKyThi.getValueAt(index, 0);
                    if (kythiBUS.delete(id)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        listHienTai = kythiBUS.getAll();
                        loadDataTable(listHienTai);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kỳ thi cần xóa!");
            }
        } else if (source == mainFunction.btn.get("import")) {
            importExcel();
        } else if (source == mainFunction.btn.get("export")) {
            try {
                helper.JTableExporter.exportJTableToExcel(tableKyThi);
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
