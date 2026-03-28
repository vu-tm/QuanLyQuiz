package GUI.Panel;

import BUS.CauHoiBUS;
import BUS.DoKhoBUS;
import BUS.LoaiCauHoiBUS;
import BUS.MonHocBUS;
import BUS.NguoiDungBUS;
import DTO.CauHoiDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.CauHoiDialog;
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
import javax.swing.table.TableCellRenderer;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CauHoi extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius pnlMain, functionBar;
    private GUI.Main mainFrame;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable table;
    JScrollPane scrollTable;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    private final NguoiDungBUS ndBUS = new NguoiDungBUS();
    private final CauHoiBUS bus = new CauHoiBUS();
    private final MonHocBUS monHocBUS = new MonHocBUS();
    private final DoKhoBUS doKhoBUS = new DoKhoBUS();
    private final LoaiCauHoiBUS loaiCauHoiBUS = new LoaiCauHoiBUS();

    private ArrayList<CauHoiDTO> listHienTai = bus.getAll();
    private final Color backgroundColor = new Color(240, 247, 250);

    public CauHoi(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(backgroundColor);
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

        String[] header = {"Mã CH", "Nội dung câu hỏi", "Độ khó", "Loại", "Môn học", "Người tạo"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setFocusable(false);

        // Tăng chiều cao hàng mặc định để nhìn thoáng hơn
        table.setRowHeight(40);
        scrollTable.setViewportView(table);

        // Renderer căn giữa cho các cột thông thường
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Thiết lập Renderer cho từng cột
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(new MultiLineTableCellRenderer());
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Độ rộng các cột
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // Mã CH
        table.getColumnModel().getColumn(1).setPreferredWidth(450); // Nội dung
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Độ khó
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Loại
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Môn học
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // Người tạo

        table.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(table, 0, (Object o1, Object o2) -> {
            int id1 = Integer.parseInt(o1.toString().replace("CH-", ""));
            int id2 = Integer.parseInt(o2.toString().replace("CH-", ""));
            return Integer.compare(id1, id2);
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

        String[] action = {"create", "update", "delete", "detail", "import", "export"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "1", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã câu hỏi", "Nội dung", "Người tạo"});
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
        pnlMain.add(scrollTable, BorderLayout.CENTER);
        contentCenter.add(pnlMain, BorderLayout.CENTER);
    }

    class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {

        public MultiLineTableCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setEditable(false);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setText(value != null ? value.toString() : "");

            int width = table.getColumnModel().getColumn(column).getWidth();
            setSize(new Dimension(width, getPreferredSize().height));

            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, Math.max(40, getPreferredSize().height));
            }

            return this;
        }
    }

    public void thucHienTimKiem() {
        String kieu = (String) search.cbxChoose.getSelectedItem();
        String text = search.txtSearchForm.getText();
        listHienTai = bus.search(text, kieu);
        loadDataTable(listHienTai);
    }

    public void loadDataTable(ArrayList<CauHoiDTO> danhSach) {
        this.listHienTai = danhSach;
        tblModel.setRowCount(0);
        for (CauHoiDTO ch : danhSach) {
            tblModel.addRow(new Object[]{
                "CH-" + ch.getMacauhoi(),
                ch.getNoidung(),
                doKhoBUS.getTenDoKho(ch.getMadokho()),
                loaiCauHoiBUS.getTenById(ch.getMaloai()),
                monHocBUS.getTenById(ch.getMamonhoc()),
                ndBUS.getHotenById(ch.getNguoitao())
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object source = e.getSource();

        if (source == mainFunction.btn.get("create")) {
            CauHoiDTO newCH = new CauHoiDTO();
            newCH.setNguoitao(mainFrame.getNguoiDung().getManguoidung());
            new CauHoiDialog(this, owner, "Thêm câu hỏi mới", newCH);
        } else if (source == mainFunction.btn.get("update") || source == mainFunction.btn.get("detail") || source == mainFunction.btn.get("delete")) {
            int index = table.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn câu hỏi!");
                return;
            }
            String maStr = table.getValueAt(index, 0).toString();
            int macauhoi = Integer.parseInt(maStr.replace("CH-", ""));
            bus.getAll();
            CauHoiDTO selected = bus.getById(macauhoi);

            if (source == mainFunction.btn.get("update")) {
                new CauHoiDialog(this, owner, "Chỉnh sửa câu hỏi", selected);
            } else if (source == mainFunction.btn.get("detail")) {
                new CauHoiDialog(this, owner, "Thông tin chi tiết câu hỏi", selected);
            } else if (source == mainFunction.btn.get("delete")) {
                if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa câu hỏi mã " + macauhoi + "?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (bus.delete(macauhoi)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        listHienTai = bus.getAll();
                        loadDataTable(listHienTai);
                    }
                }
            }
        } else if (source == mainFunction.btn.get("import")) {
            importExcel();
        } else if (source == mainFunction.btn.get("export")) {
            exportExcel();
        }
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

                int countSuccess = 0, countError = 0;
                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    XSSFRow excelRow = excelSheet.getRow(row);
                    if (excelRow == null) {
                        continue;
                    }

                    try {
                        String noidung = formatter.formatCellValue(excelRow.getCell(0)).trim();
                        String tenDoKho = formatter.formatCellValue(excelRow.getCell(1)).trim();
                        String tenLoai = formatter.formatCellValue(excelRow.getCell(2)).trim();
                        String tenMonHoc = formatter.formatCellValue(excelRow.getCell(3)).trim();

                        if (noidung.isEmpty()) {
                            continue;
                        }

                        int madokho = -1;
                        for (var dk : doKhoBUS.getAll()) {
                            if (dk.getTendokho().equalsIgnoreCase(tenDoKho)) {
                                madokho = dk.getMadokho();
                                break;
                            }
                        }

                        int maloai = -1;
                        for (var l : loaiCauHoiBUS.getAll()) {
                            if (l.getTenloai().equalsIgnoreCase(tenLoai)) {
                                maloai = l.getMaloai();
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

                        if (madokho != -1 && maloai != -1 && mamonhoc != -1) {
                            CauHoiDTO ch = new CauHoiDTO();
                            ch.setNoidung(noidung);
                            ch.setMadokho(madokho);
                            ch.setMaloai(maloai);
                            ch.setMamonhoc(mamonhoc);
                            ch.setNguoitao(mainFrame.getNguoiDung().getManguoidung());
                            ch.setTrangthai(1);
                            if (bus.add(ch)) {
                                countSuccess++;
                            } else {
                                countError++;
                            }
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
                excelJTableImport.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel!");
            }
        }
    }

    public void exportExcel() {
        try {
            helper.JTableExporter.exportJTableToExcel(table);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Xuất file Excel thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }
}
