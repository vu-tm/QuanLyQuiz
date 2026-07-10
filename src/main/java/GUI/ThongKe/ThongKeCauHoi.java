package GUI.ThongKe;

import BUS.ThongKeBUS;
import DTO.CauHoiDTO;
import DTO.DoKhoDTO;
import DTO.ThongKe.ThongKeCauHoiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.CauHoiDialog;
import helper.JTableExporter;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeCauHoi extends JPanel implements ActionListener, KeyListener {

    PanelBorderRadius pnlLeft, pnlCenter;
    JTable tblCauHoi;
    JScrollPane scrollTbl;
    DefaultTableModel tblModel;
    InputForm inputSearch;
    ButtonCustom btnExport, btnReset, btnCapNhatDoKho;
    ArrayList<ThongKeCauHoiDTO> list;
    private BUS.CauHoiBUS cauHoiBUS = new BUS.CauHoiBUS();
    private BUS.DoKhoBUS doKhoBUS = new BUS.DoKhoBUS();

    public ThongKeCauHoi() {
        list = ThongKeBUS.getThongKeCauHoi();
        initComponent();
        loadDataTable(list);
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // PANEL TRÁI
        pnlLeft = new PanelBorderRadius();
        pnlLeft.setPreferredSize(new Dimension(300, 100));
        pnlLeft.setLayout(new BorderLayout());
        pnlLeft.setBackground(Color.WHITE);
        pnlLeft.setBorder(new EmptyBorder(0, 0, 0, 5));

        // Panel con
        JPanel left_content = new JPanel(new GridLayout(2, 1));
        left_content.setOpaque(false);
        pnlLeft.add(left_content, BorderLayout.NORTH);

        // Input Tìm kiếm
        inputSearch = new InputForm("Tìm kiếm câu hỏi");
        inputSearch.getTxtForm().putClientProperty("JTextField.showClearButton", true);
        inputSearch.getTxtForm().addKeyListener(this);

        // Group nút bấm
        JPanel btn_layout = new JPanel(new GridLayout(2, 1, 0, 10));
        btn_layout.setBackground(Color.WHITE);
        btn_layout.setBorder(new EmptyBorder(20, 10, 0, 10));

        JPanel btnGroup = new JPanel(new GridLayout(1, 2, 10, 0));
        btnGroup.setOpaque(false);

        btnExport = new ButtonCustom("Xuất Excel", "excel", 14);
        btnReset = new ButtonCustom("Làm mới", "success", 14);
        btnExport.addActionListener(this);
        btnReset.addActionListener(this);

        btnGroup.add(btnExport);
        btnGroup.add(btnReset);

        btnCapNhatDoKho = new ButtonCustom("Cập nhật độ khó", "danger", 14);
        btnCapNhatDoKho.addActionListener(this);

        btn_layout.add(btnGroup);
        btn_layout.add(btnCapNhatDoKho);

        left_content.add(inputSearch);
        left_content.add(btn_layout);

        // PANEL GIỮA
        pnlCenter = new PanelBorderRadius();
        pnlCenter.setLayout(new BorderLayout());

        tblCauHoi = new JTable();
        scrollTbl = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = {"STT", "Mã câu hỏi", "Nội dung", "Độ khó", "Tổng lần thi", "Tỷ lệ đúng (%)", "Tỷ lệ sai (%)"};
        tblModel.setColumnIdentifiers(header);
        tblCauHoi.setModel(tblModel);

        DefaultTableCellRenderer percentRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Double) {
                    value = String.format("%.1f%%", (Double) value);
                }
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        };

        // table
        tblCauHoi.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblCauHoi.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblCauHoi.getColumnModel().getColumn(2).setPreferredWidth(350);
        tblCauHoi.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblCauHoi.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblCauHoi.getColumnModel().getColumn(5).setPreferredWidth(110);
        tblCauHoi.getColumnModel().getColumn(6).setPreferredWidth(110);
        tblCauHoi.setDefaultEditor(Object.class, null);
        tblCauHoi.setRowHeight(35);
        tblCauHoi.setFocusable(false);
        tblCauHoi.getColumnModel().getColumn(5).setCellRenderer(percentRenderer);
        tblCauHoi.getColumnModel().getColumn(6).setCellRenderer(percentRenderer);
        scrollTbl.setViewportView(tblCauHoi);
        tblCauHoi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblCauHoi.getSelectedRow();
                    if (row != -1) {
                        String maStr = tblCauHoi.getValueAt(row, 1).toString();
                        int macauhoi = Integer.parseInt(maStr.replace("CH-", ""));
                        CauHoiDTO dto = cauHoiBUS.getById(macauhoi);

                        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(ThongKeCauHoi.this);
                        CauHoiDialog diag = new CauHoiDialog(null, owner, "Chỉnh sửa độ khó câu hỏi", dto);
                        refreshTable();
                    }
                }
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblCauHoi.setDefaultRenderer(Object.class, centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        tblCauHoi.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        // Chỉnh độ rộng cột
        tblCauHoi.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblCauHoi.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblCauHoi.getColumnModel().getColumn(2).setPreferredWidth(400);
        tblCauHoi.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblCauHoi.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblCauHoi.getColumnModel().getColumn(5).setPreferredWidth(120);

        // Sắp xếp
        TableSorter.configureTableColumnSorter(tblCauHoi, 0, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblCauHoi, 1, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblCauHoi, 3, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblCauHoi, 4, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblCauHoi, 5, TableSorter.DOUBLE_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblCauHoi, 6, TableSorter.DOUBLE_COMPARATOR);

        pnlCenter.add(scrollTbl, BorderLayout.CENTER);

        this.add(pnlLeft, BorderLayout.WEST);
        this.add(pnlCenter, BorderLayout.CENTER);
    }

    public void loadDataTable(ArrayList<ThongKeCauHoiDTO> data) {
        tblModel.setRowCount(0);
        if (data == null) {
            return;
        }
        for (ThongKeCauHoiDTO i : data) {
            CauHoiDTO ch = cauHoiBUS.getById(i.getMacauhoi());
            String tenDoKho = (ch != null) ? doKhoBUS.getTenDoKho(ch.getMadokho()) : "N/A";
            tblModel.addRow(new Object[]{
                i.getStt(),
                "CH-" + i.getMacauhoi(),
                i.getNoidung(),
                tenDoKho,
                i.getTonglan(),
                i.getTyleDung(),
                i.getTyleSai()
            });
        }
    }

    public void refreshTable() {
        list = ThongKeBUS.getThongKeCauHoi();
        loadDataTable(list);
    }

    private void capNhatDoKhoTuDong() {
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để cập nhật!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Hệ thống sẽ tự động cập nhật độ khó cho tất cả câu hỏi dựa trên tỷ lệ trả lời đúng.\n"
                + "Chỉ áp dụng cho câu hỏi đã có người làm bài (Tổng lần thi > 0).\n"
                + "Bạn có muốn tiếp tục?",
                "Xác nhận cập nhật độ khó",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        java.util.List<DoKhoDTO> listDoKho = doKhoBUS.getAll();
        int maDoKhoDe = -1, maDoKhoTB = -1, maDoKhoKho = -1;
        for (DoKhoDTO dk : listDoKho) {
            String ten = dk.getTendokho().trim().toLowerCase();
            if (ten.contains("dễ")) {
                maDoKhoDe = dk.getMadokho();
            } else if (ten.contains("trung")) {
                maDoKhoTB = dk.getMadokho();
            } else if (ten.contains("khó")) {
                maDoKhoKho = dk.getMadokho();
            }
        }

        if (maDoKhoDe == -1 || maDoKhoTB == -1 || maDoKhoKho == -1) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy đủ 3 mức độ khó (Dễ / Trung bình / Khó) trong hệ thống!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int countUpdated = 0;
        for (ThongKeCauHoiDTO tk : list) {
            if (tk.getTonglan() <= 0) {
                continue;
            }

            double tyLeDung = tk.getTyleDung();
            int maDoKhoMoi;
            if (tyLeDung >= 70) {
                maDoKhoMoi = maDoKhoDe;
            } else if (tyLeDung >= 40) {
                maDoKhoMoi = maDoKhoTB;
            } else {
                maDoKhoMoi = maDoKhoKho;
            }

            CauHoiDTO ch = cauHoiBUS.getById(tk.getMacauhoi());
            if (ch == null) {
                continue;
            }

            if (ch.getMadokho() != maDoKhoMoi) {
                ch.setMadokho(maDoKhoMoi);
                if (cauHoiBUS.update(ch)) {
                    countUpdated++;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Đã tự động cập nhật độ khó cho " + countUpdated + " câu hỏi!");
        refreshTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExport) {
            try {
                JTableExporter.exportJTableToExcel(tblCauHoi);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xuất file Excel!");
            }
        } else if (e.getSource() == btnReset) {
            inputSearch.getTxtForm().setText("");
            refreshTable();
        } else if (e.getSource() == btnCapNhatDoKho) {
            capNhatDoKhoTuDong();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String searchText = inputSearch.getTxtForm().getText().toLowerCase();
        if (searchText.isEmpty()) {
            loadDataTable(list);
        } else {
            ArrayList<ThongKeCauHoiDTO> temp = new ArrayList<>();
            if (list != null) {
                for (ThongKeCauHoiDTO i : list) {
                    if (String.valueOf(i.getMacauhoi()).contains(searchText)
                            || i.getNoidung().toLowerCase().contains(searchText)) {
                        temp.add(i);
                    }
                }
            }
            loadDataTable(temp);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }
}