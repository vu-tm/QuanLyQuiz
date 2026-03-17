package GUI.Panel;

import BUS.BaiThiBUS;
import BUS.DeThiBUS;
import BUS.NguoiDungBUS;
import DTO.BaiThiDTO;
import DTO.DeThiDTO;
import DTO.NguoiDungDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietBaiThiDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class BaiThi extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable table;
    JScrollPane scrollTable;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    BaiThiBUS bus = new BaiThiBUS();
    DeThiBUS deThiBUS = new DeThiBUS();
    NguoiDungBUS nguoiDungBUS = new NguoiDungBUS();
    ArrayList<BaiThiDTO> listHienTai = bus.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public BaiThi() {
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

        String[] header = {"Mã bài thi", "Mã đề", "Tên đề thi", "Mã SV", "Tên sinh viên", "Điểm", "Thời gian vào", "Thời gian làm"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setFocusable(false);
        table.setRowHeight(30);
        scrollTable.setViewportView(table);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(table, 0, TableSorter.INTEGER_COMPARATOR);

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
        mainFunction = new MainFunction(action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }

        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã bài thi", "Mã đề", "Mã SV"});
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

        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTable, BorderLayout.CENTER);
        contentCenter.add(main, BorderLayout.CENTER);
    }

    public void loadDataTable(ArrayList<BaiThiDTO> result) {
        tblModel.setRowCount(0);
        for (BaiThiDTO bt : result) {
            DeThiDTO deThi = deThiBUS.getById(bt.getMade());
            NguoiDungDTO nguoiDung = nguoiDungBUS.getById(bt.getManguoidung());
            
            tblModel.addRow(new Object[]{
                bt.getMabaithi(),
                bt.getMade(),
                deThi != null ? deThi.getTende() : "N/A",
                bt.getManguoidung(),
                nguoiDung != null ? nguoiDung.getHoten() : "N/A",
                bt.getDiemthi(),
                bt.getThoigianvaothi(),
                bt.getThoigianlambai() + " p"
            });
        }
    }

    public void thucHienTimKiem() {
        String kieu = (String) search.cbxChoose.getSelectedItem();
        String text = search.txtSearchForm.getText().toLowerCase();
        
        listHienTai = bus.getAll();
        ArrayList<BaiThiDTO> result = new ArrayList<>();
        
        for (BaiThiDTO bt : listHienTai) {
            boolean match = false;
            switch (kieu) {
                case "Tất cả":
                    match = String.valueOf(bt.getMabaithi()).contains(text) || 
                            String.valueOf(bt.getMade()).contains(text) || 
                            bt.getManguoidung().toLowerCase().contains(text);
                    break;
                case "Mã bài thi":
                    match = String.valueOf(bt.getMabaithi()).contains(text);
                    break;
                case "Mã đề":
                    match = String.valueOf(bt.getMade()).contains(text);
                    break;
                case "Mã SV":
                    match = bt.getManguoidung().toLowerCase().contains(text);
                    break;
            }
            if (match) {
                result.add(bt);
            }
        }
        loadDataTable(result);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        int index = table.getSelectedRow();
        if (index == -1 && (source == mainFunction.btn.get("detail") || source == mainFunction.btn.get("delete"))) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng!");
            return;
        }

        if (source == mainFunction.btn.get("detail")) {
            int maBaiThi = (int) table.getValueAt(index, 0);
            BaiThiDTO selected = bus.getById(maBaiThi);
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            new ChiTietBaiThiDialog(owner, "Chi tiết bài thi", true, selected);
        } else if (source == mainFunction.btn.get("delete")) {
            int maBaiThi = (int) table.getValueAt(index, 0);
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bài thi này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (bus.delete(maBaiThi) > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    listHienTai = bus.getAll();
                    loadDataTable(listHienTai);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }
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