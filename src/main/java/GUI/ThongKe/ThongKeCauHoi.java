package GUI.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeCauHoiDTO;
import GUI.Component.InputForm;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import helper.JTableExporter;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    JButton btnExport, btnReset;
    ArrayList<ThongKeCauHoiDTO> list;
    private final Font font = new Font("Arial", Font.BOLD, 14);

    public ThongKeCauHoi() {
        list = ThongKeBUS.getThongKeCauHoi();
        initComponent();
        loadDataTable(list);
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // LEFT panel
        pnlLeft = new PanelBorderRadius();
        pnlLeft.setPreferredSize(new Dimension(300, 100));
        pnlLeft.setLayout(new BorderLayout());
        pnlLeft.setBorder(new EmptyBorder(0, 0, 0, 5));

        JPanel leftContent = new JPanel(new GridLayout(3, 1));
        leftContent.setPreferredSize(new Dimension(300, 200));
        pnlLeft.add(leftContent, BorderLayout.NORTH);

        inputSearch = new InputForm("Tìm kiếm câu hỏi");
        inputSearch.getTxtForm().putClientProperty("JTextField.showClearButton", true);
        inputSearch.getTxtForm().addKeyListener(this);
        inputSearch.getLblTitle().setFont(font);

        JPanel btnLayout = new JPanel(new BorderLayout());
        btnLayout.setPreferredSize(new Dimension(30, 36));
        btnLayout.setBorder(new EmptyBorder(20, 10, 0, 10));
        btnLayout.setBackground(Color.WHITE);

        JPanel btnInner = new JPanel(new GridLayout(1, 2, 10, 10));
        btnInner.setOpaque(false);

        btnExport = createButton("Xuất Excel", new Color(76, 175, 80));
        btnReset = createButton("Làm mới", new Color(72, 118, 255));
        btnExport.addActionListener(this);
        btnReset.addActionListener(this);

        btnInner.add(btnExport);
        btnInner.add(btnReset);
        btnLayout.add(btnInner, BorderLayout.NORTH);

        leftContent.add(inputSearch);
        leftContent.add(new JPanel()); // spacing
        leftContent.add(btnLayout);

        // CENTER panel (table)
        pnlCenter = new PanelBorderRadius();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));

        tblCauHoi = new JTable();
        scrollTbl = new JScrollPane();
        tblModel = new DefaultTableModel();
        String[] header = {"STT", "Mã câu hỏi", "Nội dung", "Tổng lần thi", "Tỷ lệ đúng (%)", "Tỷ lệ sai (%)"};
        tblModel.setColumnIdentifiers(header);
        tblCauHoi.setModel(tblModel);
        tblCauHoi.setAutoCreateRowSorter(true);
        tblCauHoi.setDefaultEditor(Object.class, null);
        scrollTbl.setViewportView(tblCauHoi);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblCauHoi.setDefaultRenderer(Object.class, centerRenderer);
        tblCauHoi.setFocusable(false);
        tblCauHoi.setRowHeight(30);

        // Nội dung câu hỏi căn trái
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        tblCauHoi.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        tblCauHoi.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblCauHoi.getColumnModel().getColumn(1).setPreferredWidth(60);
        tblCauHoi.getColumnModel().getColumn(2).setPreferredWidth(400);
        tblCauHoi.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblCauHoi.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblCauHoi.getColumnModel().getColumn(5).setPreferredWidth(100);

        TableSorter.configureTableColumnSorter(tblCauHoi, 0, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblCauHoi, 1, TableSorter.INTEGER_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblCauHoi, 2, TableSorter.STRING_COMPARATOR);
        TableSorter.configureTableColumnSorter(tblCauHoi, 3, TableSorter.INTEGER_COMPARATOR);

        pnlCenter.add(scrollTbl);

        this.add(pnlLeft, BorderLayout.WEST);
        this.add(pnlCenter, BorderLayout.CENTER);
    }

    public void loadDataTable(ArrayList<ThongKeCauHoiDTO> data) {
        tblModel.setRowCount(0);
        for (ThongKeCauHoiDTO i : data) {
            tblModel.addRow(new Object[]{
                i.getStt(), i.getMacauhoi(), i.getNoidung(),
                i.getTonglan(),
                i.getTonglan() > 0 ? String.format("%.1f%%", i.getTyleDung()) : "N/A",
                i.getTonglan() > 0 ? String.format("%.1f%%", i.getTyleSai()) : "N/A"
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
            try { JTableExporter.exportJTableToExcel(tblCauHoi); }
            catch (IOException ex) { Logger.getLogger(ThongKeCauHoi.class.getName()).log(Level.SEVERE, null, ex); }
        } else if (e.getSource() == btnReset) {
            inputSearch.setText("");
            list = ThongKeBUS.getThongKeCauHoi();
            loadDataTable(list);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (inputSearch.getDocument().isEmpty()) {
            loadDataTable(list);
        } else {
            ArrayList<ThongKeCauHoiDTO> temp = new ArrayList<>();
            for (ThongKeCauHoiDTO i : list) {
                if (String.valueOf(i.getMacauhoi()).contains(inputSearch.getDocument()) ||
                    i.getNoidung().toUpperCase().contains(inputSearch.getDocument().toUpperCase())) {
                    temp.add(i);
                }
            }
            loadDataTable(temp);
        }
    }

    public JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color actualBgColor = bgColor;
                if (getModel().isPressed()) actualBgColor = bgColor.darker();
                else if (getModel().isRollover()) actualBgColor = bgColor.brighter();
                g2.setColor(actualBgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(140, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
}