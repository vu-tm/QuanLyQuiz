package GUI.Panel;

import BUS.LoaiCauHoiBUS;
import DTO.LoaiCauHoiDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.LoaiCauHoiDialog;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class LoaiCauHoi extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius pnlMain, functionBar;
    private GUI.Main mainFrame;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableLoai;
    JScrollPane scrollTable;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    LoaiCauHoiBUS loaiBUS = new LoaiCauHoiBUS();
    ArrayList<LoaiCauHoiDTO> listHienTai = loaiBUS.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public LoaiCauHoi(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        tableLoai = new JTable();
        scrollTable = new JScrollPane();

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] header = new String[]{"Mã loại", "Tên loại câu hỏi"};
        tblModel.setColumnIdentifiers(header);
        tableLoai.setModel(tblModel);
        tableLoai.setFocusable(false);
        tableLoai.setRowHeight(30);
        tableLoai.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableLoai.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableLoai.getColumnCount(); i++) {
            tableLoai.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableLoai.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableLoai, 0, TableSorter.INTEGER_COMPARATOR);
        scrollTable.setViewportView(tableLoai);

        pnlBorder1 = new JPanel(); pnlBorder1.setPreferredSize(new Dimension(0, 10)); pnlBorder1.setBackground(BackgroundColor);
        pnlBorder2 = new JPanel(); pnlBorder2.setPreferredSize(new Dimension(0, 10)); pnlBorder2.setBackground(BackgroundColor);
        pnlBorder3 = new JPanel(); pnlBorder3.setPreferredSize(new Dimension(10, 0)); pnlBorder3.setBackground(BackgroundColor);
        pnlBorder4 = new JPanel(); pnlBorder4.setPreferredSize(new Dimension(10, 0)); pnlBorder4.setBackground(BackgroundColor);

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

        String[] action = {"detail", "delete", "export"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "12", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã loại", "Tên loại"});
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
            listHienTai = loaiBUS.getAll();
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

    public void loadDataTable(ArrayList<LoaiCauHoiDTO> danhSach) {
        tblModel.setRowCount(0);
        for (LoaiCauHoiDTO lch : danhSach) {
            tblModel.addRow(new Object[]{
                lch.getMaloai(),
                lch.getTenloai()
            });
        }
    }

    public void thucHienTimKiem() {
        String kieu = (String) search.cbxChoose.getSelectedItem();
        String text = search.txtSearchForm.getText();
        listHienTai = loaiBUS.search(text, kieu);
        loadDataTable(listHienTai);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object source = e.getSource();

        if (source == mainFunction.btn.get("detail") || source == mainFunction.btn.get("delete")) {
            int index = tableLoai.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại câu hỏi!");
                return;
            }
            int modelRow = tableLoai.convertRowIndexToModel(index);
            int maloai = (int) tblModel.getValueAt(modelRow, 0);
            LoaiCauHoiDTO selected = loaiBUS.getById(maloai);

            if (source == mainFunction.btn.get("detail")) {
                new LoaiCauHoiDialog(this, owner, "Thông tin chi tiết", true, "view", selected);
            } else if (source == mainFunction.btn.get("delete")) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa loại này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (loaiBUS.delete(maloai)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        listHienTai = loaiBUS.getAll();
                        loadDataTable(listHienTai);
                    }
                }
            }
        } else if (source == mainFunction.btn.get("export")) {
            try {
                helper.JTableExporter.exportJTableToExcel(tableLoai);
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