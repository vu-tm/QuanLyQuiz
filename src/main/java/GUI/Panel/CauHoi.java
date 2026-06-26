package GUI.Panel;

import BUS.CauHoiBUS;
import BUS.DapAnBUS;
import BUS.DoKhoBUS;
import BUS.LoaiCauHoiBUS;
import BUS.MonHocBUS;
import BUS.NguoiDungBUS;
import DTO.CauHoiDTO;
import DTO.DapAnDTO;
import DTO.DoKhoDTO;
import DTO.LoaiCauHoiDTO;
import DTO.MonHocDTO;
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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import javax.swing.filechooser.FileNameExtensionFilter;

// cauhoi-level0, cautraloi-level1 (word)
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

        String[] action = {"create", "update", "delete", "detail", "import", "export", "importword"};
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
        } else if (source == mainFunction.btn.get("importword")) {
            importWord();
        }
    }

    public void importWord() {
        // 1. Chọn file
        JFileChooser jf = new JFileChooser();
        jf.setFileFilter(new FileNameExtensionFilter("Word Documents (.docx)", "docx"));
        int result = jf.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = jf.getSelectedFile();

        // 2. Tạo Form chọn cấu hình Import (Môn, Độ khó, Loại)
        JPanel pnlConfig = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlConfig.setBorder(new EmptyBorder(10, 10, 10, 10));

        JComboBox<MonHocDTO> cbMonHoc = new JComboBox<>(new DefaultComboBoxModel<>(monHocBUS.getAll().toArray(new MonHocDTO[0])));
        JComboBox<DoKhoDTO> cbDoKho = new JComboBox<>(new DefaultComboBoxModel<>(doKhoBUS.getAll().toArray(new DoKhoDTO[0])));
        JComboBox<LoaiCauHoiDTO> cbLoai = new JComboBox<>(new DefaultComboBoxModel<>(loaiCauHoiBUS.getAll().toArray(new LoaiCauHoiDTO[0])));

        // Renderer để hiển thị tên thay vì Object
        cbMonHoc.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value.getTenmonhoc()));
        cbDoKho.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value.getTendokho()));
        cbLoai.setRenderer((list, value, index, isSelected, cellHasFocus) -> new JLabel(value.getTenloai()));

        pnlConfig.add(new JLabel("Chọn môn học:"));
        pnlConfig.add(cbMonHoc);
        pnlConfig.add(new JLabel("Chọn độ khó:"));
        pnlConfig.add(cbDoKho);
        pnlConfig.add(new JLabel("Chọn loại CH:"));
        pnlConfig.add(cbLoai);

        int configResult = JOptionPane.showConfirmDialog(this, pnlConfig, "Cấu hình thông tin nhập câu hỏi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (configResult != JOptionPane.OK_OPTION) {
            return;
        }

        // Lấy giá trị đã chọn từ ComboBox
        int selectedMonHoc = ((MonHocDTO) cbMonHoc.getSelectedItem()).getMamonhoc();
        int selectedDoKho = ((DoKhoDTO) cbDoKho.getSelectedItem()).getMadokho();
        int selectedLoai = ((LoaiCauHoiDTO) cbLoai.getSelectedItem()).getMaloai();

        // 3. Tiến hành đọc file và lưu
        try (FileInputStream fis = new FileInputStream(file); XWPFDocument document = new XWPFDocument(fis)) {
            DapAnBUS daBUS = new DapAnBUS();
            CauHoiDTO currentCH = null;
            ArrayList<DapAnDTO> currentListDA = new ArrayList<>();
            int countSuccess = 0;

            for (XWPFParagraph para : document.getParagraphs()) {
                String text = para.getText().trim();
                if (text.isEmpty()) {
                    continue;
                }

                org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr numPr = null;
                if (para.getCTP().getPPr() != null) {
                    numPr = para.getCTP().getPPr().getNumPr();
                }

                boolean isList = (numPr != null && numPr.getNumId() != null && numPr.getNumId().getVal().intValue() > 0);
                int listLevel = isList && numPr.getIlvl() != null ? numPr.getIlvl().getVal().intValue() : -1;

                if (isList && listLevel == 0) {
                    // Lưu câu trước đó nếu có
                    if (currentCH != null && !currentListDA.isEmpty()) {
                        if (saveToDatabase(currentCH, currentListDA, daBUS)) {
                            countSuccess++;
                        }
                    }
                    // Tạo câu mới với thông tin đã CHỌN từ Dialog
                    currentCH = new CauHoiDTO();
                    currentCH.setNoidung(text);
                    currentCH.setMamonhoc(selectedMonHoc);
                    currentCH.setMadokho(selectedDoKho);
                    currentCH.setMaloai(selectedLoai);
                    currentCH.setNguoitao(mainFrame.getNguoiDung().getManguoidung());
                    currentCH.setTrangthai(1);
                    currentListDA = new ArrayList<>();
                } else if (isList && listLevel == 1) {
                    if (currentCH == null) {
                        continue;
                    }
                    // Kiểm tra in đậm để xác định đáp án đúng
                    boolean isCorrect = false;
                    for (XWPFRun run : para.getRuns()) {
                        if (run.isBold()) {
                            isCorrect = true;
                            break;
                        }
                    }
                    currentListDA.add(new DapAnDTO(0, 0, text, isCorrect));
                } else if (!isList && currentCH != null && currentListDA.isEmpty()) {
                    currentCH.setNoidung(currentCH.getNoidung() + " " + text);
                }
            }

            // Lưu câu cuối
            if (currentCH != null && !currentListDA.isEmpty()) {
                if (saveToDatabase(currentCH, currentListDA, daBUS)) {
                    countSuccess++;
                }
            }

            JOptionPane.showMessageDialog(this, "Nhập thành công " + countSuccess + " câu hỏi!", "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
            loadDataTable(bus.getAll());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi đọc file Word: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Kiểm tra paragraph có thực sự in đậm không.
     * Với list bullet, runs có thể inherit bold từ style → cần kiểm tra rPr trực tiếp.
     */
    private boolean isParagraphBold(XWPFParagraph para) {
        String styleId = para.getStyle();
        return styleId != null && styleId.toLowerCase().contains("heading");
    }

// Hàm hỗ trợ lưu câu hỏi và danh sách đáp án
    private boolean saveToDatabase(CauHoiDTO ch, ArrayList<DapAnDTO> listDA, DapAnBUS daBUS) {
        if (ch.getNoidung().isEmpty() || listDA.isEmpty()) {
            return false;
        }

        int generatedId = bus.addReturnId(ch);
        if (generatedId != -1) {
            for (DapAnDTO da : listDA) {
                da.setMacauhoi(generatedId);
                daBUS.add(da);
            }
            return true;
        }
        return false;
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
                DapAnBUS daBUS = new DapAnBUS();

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

                        // Lấy ID mapping
                        int madokho = -1;
                        for (var dk : doKhoBUS.getAll()) {
                            if (dk.getTendokho().equalsIgnoreCase(tenDoKho)) {
                                madokho = dk.getMadokho();
                                break;
                            }
                        }
                        int maloai = -1;
                        for (var l : loaiCauHoiBUS.getAll()) {
                            if (l.getTenloai().equalsIgnoreCase(tenLoai) || l.getTenloai().replace("/", " ").equalsIgnoreCase(tenLoai)) {
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
                        if (madokho == -1 || maloai == -1 || mamonhoc == -1) {
                            System.out.println("Lỗi mapping tại dòng " + row + ":");
                            System.out.println("- Độ khó: " + tenDoKho + " -> " + (madokho != -1 ? "OK" : "FAILED"));
                            System.out.println("- Loại: " + tenLoai + " -> " + (maloai != -1 ? "OK" : "FAILED"));
                            System.out.println("- Môn học: " + tenMonHoc + " -> " + (mamonhoc != -1 ? "OK" : "FAILED"));
                        }
                        if (madokho != -1 && maloai != -1 && mamonhoc != -1) {
                            CauHoiDTO ch = new CauHoiDTO();
                            ch.setNoidung(noidung);
                            ch.setMadokho(madokho);
                            ch.setMaloai(maloai);
                            ch.setMamonhoc(mamonhoc);
                            ch.setNguoitao(mainFrame.getNguoiDung().getManguoidung());
                            ch.setTrangthai(1);

                            // Thêm câu hỏi và lấy ID vừa tạo
                            int generatedId = bus.addReturnId(ch);

                            if (generatedId != -1) {
                                String loaiLower = tenLoai.toLowerCase();

                                // XỬ LÝ ĐÁP ÁN THEO TỪNG LOẠI
                                if (loaiLower.contains("trắc")) {
                                    // 1. Loại Trắc nghiệm: Đọc A, B, C, D (Cột 4-7) và Key (Cột 8)
                                    String correctChar = formatter.formatCellValue(excelRow.getCell(8)).trim().toUpperCase();
                                    for (int i = 0; i < 4; i++) {
                                        String textDA = formatter.formatCellValue(excelRow.getCell(4 + i)).trim();
                                        if (!textDA.isEmpty()) {
                                            char label = (char) ('A' + i);
                                            daBUS.add(new DapAnDTO(0, generatedId, textDA, String.valueOf(label).equals(correctChar)));
                                        }
                                    }

                                } else if (loaiLower.contains("đúng")) {
                                    // 2. Loại Đúng/Sai: Tự tạo 2 record, check Cột 8 xem cái nào đúng
                                    String correctText = formatter.formatCellValue(excelRow.getCell(8)).trim();
                                    daBUS.add(new DapAnDTO(0, generatedId, "Đúng", correctText.equalsIgnoreCase("Đúng")));
                                    daBUS.add(new DapAnDTO(0, generatedId, "Sai", correctText.equalsIgnoreCase("Sai")));

                                } else if (loaiLower.contains("điền")) {
                                    // 3. Loại Điền khuyết: Lấy nội dung ở Cột 4 làm đáp án đúng
                                    String fillText = formatter.formatCellValue(excelRow.getCell(4)).trim();
                                    if (!fillText.isEmpty()) {
                                        daBUS.add(new DapAnDTO(0, generatedId, fillText, true));
                                    }
                                }

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
                JOptionPane.showMessageDialog(this, "Nhập thành công " + countSuccess + " câu hỏi. Lỗi " + countError + " dòng.");
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
