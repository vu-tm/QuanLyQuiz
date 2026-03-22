package GUI.Panel;

import BUS.MonHocBUS;
import BUS.NguoiDungBUS;
import BUS.PhanCongBUS;
import DTO.PhanCongDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.PhanCongDialog;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xssf.usermodel.*;

public class PhanCong extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius pnlMain, functionBar;
    private GUI.Main mainFrame;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable table;
    JScrollPane scrollTable;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    PhanCongBUS bus = new PhanCongBUS();
    MonHocBUS mhBUS = new MonHocBUS();
    NguoiDungBUS ndBUS = new NguoiDungBUS();
    ArrayList<PhanCongDTO> listHienTai = bus.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public PhanCong(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        table = new JTable();
        scrollTable = new JScrollPane();
        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] header = {"Mã giảng viên", "Họ tên", "Mã môn", "Tên môn học"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setFocusable(false);
        table.setRowHeight(40);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        scrollTable.setViewportView(table);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(table, 0, TableSorter.INTEGER_COMPARATOR); // Mã GV là số
        TableSorter.configureTableColumnSorter(table, 2, TableSorter.INTEGER_COMPARATOR); // Mã môn là số

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
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "8", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã giảng viên", "Họ tên", "Môn học"});
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                thucHienTimKiem();
            }
        });
        search.cbxChoose.addItemListener(this);
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        pnlMain = new PanelBorderRadius();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.add(scrollTable, BorderLayout.CENTER);
        contentCenter.add(pnlMain, BorderLayout.CENTER);
    }

    public void loadDataTable(ArrayList<PhanCongDTO> result) {
        tblModel.setRowCount(0);
        for (PhanCongDTO pc : result) {
            tblModel.addRow(new Object[]{
                pc.getManguoidung(),
                ndBUS.getById(pc.getManguoidung()).getHoten(),
                pc.getMamonhoc(),
                mhBUS.getTenById(pc.getMamonhoc())
            });
        }
    }

    public void thucHienTimKiem() {
        listHienTai = bus.search(search.txtSearchForm.getText(), (String) search.cbxChoose.getSelectedItem(), ndBUS, mhBUS);
        loadDataTable(listHienTai);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object source = e.getSource();

        if (source == mainFunction.btn.get("create")) {
            new PhanCongDialog(this, owner, "Thêm phân công mới", true, "create", null);
        } else {
            int index = table.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng dữ liệu!");
                return;
            }

            int modelRow = table.convertRowIndexToModel(index);
            PhanCongDTO selected = listHienTai.get(modelRow);

            if (source == mainFunction.btn.get("update")) {
                new PhanCongDialog(this, owner, "Chỉnh sửa phân công", true, "update", selected);
            } else if (source == mainFunction.btn.get("detail")) {
                new PhanCongDialog(this, owner, "Chi tiết phân công", true, "view", selected);
            } else if (source == mainFunction.btn.get("delete")) {
                if (JOptionPane.showConfirmDialog(this, "Xóa phân công này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == 0) {
                    if (bus.delete(selected.getMamonhoc(), selected.getManguoidung())) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        listHienTai = bus.getAll();
                        loadDataTable(listHienTai);
                    }
                }
            } else if (source == mainFunction.btn.get("export")) {
                try {
                    helper.JTableExporter.exportJTableToExcel(table);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else if (source == mainFunction.btn.get("import")) {
                importExcel();
            }
        }
    }

    private void importExcel() {
        JFileChooser jf = new JFileChooser();
        if (jf.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileInputStream fis = new FileInputStream(jf.getSelectedFile()); XSSFWorkbook wb = new XSSFWorkbook(fis)) {
                XSSFSheet sheet = wb.getSheetAt(0);
                int success = 0;
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    try {
                        int mand = (int) row.getCell(0).getNumericCellValue();
                        int mamh = (int) row.getCell(1).getNumericCellValue();
                        if (bus.add(new PhanCongDTO(mamh, mand))) {
                            success++;
                        }
                    } catch (Exception e) {
                    }
                }
                JOptionPane.showMessageDialog(this, "Nhập thành công " + success + " dòng.");
                listHienTai = bus.getAll();
                loadDataTable(listHienTai);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file!");
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
