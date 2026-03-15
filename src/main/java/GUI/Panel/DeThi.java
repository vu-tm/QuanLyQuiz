package GUI.Panel;

import BUS.DeThiBUS;
import BUS.KyThiBUS;
import BUS.MonHocBUS;
import DTO.DeThiDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.DeThiDialog;
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

public class DeThi extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableDeThi;
    JScrollPane scrollTableDeThi;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    DeThiBUS dethiBUS = new DeThiBUS();
    KyThiBUS kyThiBUS = new KyThiBUS();
    MonHocBUS monHocBUS = new MonHocBUS();
    ArrayList<DeThiDTO> listHienTai = dethiBUS.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public DeThi() {
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        tableDeThi = new JTable();
        scrollTableDeThi = new JScrollPane();

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] header = new String[]{"Mã đề", "Tên đề thi", "Kỳ thi", "Môn học", "Thời gian (phút)", "Tổng câu", "Người tạo", "Trạng thái"};
        tblModel.setColumnIdentifiers(header);
        tableDeThi.setModel(tblModel);
        tableDeThi.setFocusable(false);
        scrollTableDeThi.setViewportView(tableDeThi);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableDeThi.getColumnCount(); i++) {
            tableDeThi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableDeThi.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableDeThi, 0, TableSorter.INTEGER_COMPARATOR);

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
            listHienTai = dethiBUS.getAll();
            loadDataTable(listHienTai);
        });

        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTableDeThi, BorderLayout.CENTER);
        contentCenter.add(main, BorderLayout.CENTER);
    }

    public void thucHienTimKiem() {
        String kieuTimKiem = (String) search.cbxChoose.getSelectedItem();
        String noiDungTim = search.txtSearchForm.getText();
        listHienTai = dethiBUS.search(noiDungTim, kieuTimKiem);
        loadDataTable(listHienTai);
    }

    public void importExcel() {
        JFileChooser jf = new JFileChooser();
        int result = jf.showOpenDialog(null);
        int countSuccess = 0;
        int countError = 0;

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File excelFile = jf.getSelectedFile();
                FileInputStream excelFIS = new FileInputStream(excelFile);
                BufferedInputStream excelBIS = new BufferedInputStream(excelFIS);
                XSSFWorkbook excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    if (excelRow == null) {
                        continue;
                    }
                    try {
                        String tenDe = excelRow.getCell(0).getStringCellValue();
                        if (Validation.isEmpty(tenDe)) {
                            countError++;
                            continue;
                        }
                        DeThiDTO dt = new DeThiDTO();
                        dt.setTende(tenDe);
                        dt.setThoigiantao(new Timestamp(System.currentTimeMillis()));
                        dt.setNguoitao("admin");
                        dt.setTrangthai(true);
                        dt.setTongsocau(0);

                        if (dethiBUS.add(dt)) {
                            countSuccess++;
                        } else {
                            countError++;
                        }
                    } catch (Exception e) {
                        countError++;
                    }
                }
                JOptionPane.showMessageDialog(this, "Nhập thành công " + countSuccess + " dòng. Lỗi " + countError + " dòng.");
                listHienTai = dethiBUS.getAll();
                loadDataTable(listHienTai);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!");
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }

    public void loadDataTable(ArrayList<DeThiDTO> result) {
        tblModel.setRowCount(0);
        for (int i = 0; i < result.size(); i++) {
            DeThiDTO dt = result.get(i);
            tblModel.addRow(new Object[]{
                dt.getMade(),
                dt.getTende(),
                kyThiBUS.getTenById(dt.getMakythi()),
                monHocBUS.getTenById(dt.getMonthi()),
                dt.getThoigianthi(),
                dt.getTongsocau(),
                dt.getNguoitao(),
                dt.isTrangthai() ? "Hoạt động" : "Khóa"
            });
        }
    }

    // Lấy DeThiDTO theo dòng đang chọn trong bảng
    private DeThiDTO getSelectedDTO() {
        int index = tableDeThi.getSelectedRow();
        if (index == -1) return null;
        int made = (int) tableDeThi.getValueAt(index, 0);
        for (int i = 0; i < listHienTai.size(); i++) {
            if (listHienTai.get(i).getMade() == made) {
                return listHienTai.get(i);
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object source = e.getSource();

        if (source == mainFunction.btn.get("create")) {
            new DeThiDialog(this, owner, "Thêm đề thi mới", true, "create", null);

        } else if (source == mainFunction.btn.get("update")) {
            DeThiDTO selected = getSelectedDTO();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi cần sửa!");
            } else {
                new DeThiDialog(this, owner, "Chỉnh sửa đề thi", true, "update", selected);
            }

        } else if (source == mainFunction.btn.get("detail")) {
            DeThiDTO selected = getSelectedDTO();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi cần xem!");
            } else {
                new DeThiDialog(this, owner, "Thông tin chi tiết đề thi", true, "view", selected);
            }

        } else if (source == mainFunction.btn.get("delete")) {
            DeThiDTO selected = getSelectedDTO();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi cần xóa!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa đề thi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (dethiBUS.delete(selected.getMade())) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        listHienTai = dethiBUS.getAll();
                        loadDataTable(listHienTai);
                    }
                }
            }

        } else if (source == mainFunction.btn.get("import")) {
            importExcel();

        } else if (source == mainFunction.btn.get("export")) {
            try {
                helper.JTableExporter.exportJTableToExcel(tableDeThi);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}