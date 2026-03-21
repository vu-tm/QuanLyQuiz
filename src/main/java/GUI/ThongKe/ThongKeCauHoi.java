package GUI.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeCauHoiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
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
    ButtonCustom btnExport, btnReset;
    ArrayList<ThongKeCauHoiDTO> list;

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
        JPanel btn_layout = new JPanel(new BorderLayout());
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
        btn_layout.add(btnGroup, BorderLayout.NORTH);

        left_content.add(inputSearch);
        left_content.add(btn_layout);

        // PANEL GIỮA
        pnlCenter = new PanelBorderRadius();
        pnlCenter.setLayout(new BorderLayout());

        tblCauHoi = new JTable();
        scrollTbl = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = {"STT", "Mã câu hỏi", "Nội dung", "Tổng lần thi", "Tỷ lệ đúng (%)", "Tỷ lệ sai (%)"};
        tblModel.setColumnIdentifiers(header);
        tblCauHoi.setModel(tblModel);

        // table
        tblCauHoi.setAutoCreateRowSorter(true);
        tblCauHoi.setDefaultEditor(Object.class, null);
        tblCauHoi.setRowHeight(35);
        tblCauHoi.setFocusable(false);
        scrollTbl.setViewportView(tblCauHoi);

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
            tblModel.addRow(new Object[]{
                i.getStt(),
                i.getMacauhoi(),
                i.getNoidung(),
                i.getTonglan(),
                String.format("%.1f%%", i.getTyleDung()),
                String.format("%.1f%%", i.getTyleSai())
            });
        }
    }

    public void refreshTable() {
        list = ThongKeBUS.getThongKeCauHoi();
        loadDataTable(list);
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
