package GUI.Panel;

import BUS.BaiThiBUS;
import BUS.DeThiBUS;
import BUS.NguoiDungBUS;
import DTO.BaiThiDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietBaiThiDialog;
import helper.Formater;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaiThi extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius pnlMain, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable table;
    JScrollPane scrollTable;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;
    private GUI.Main mainFrame;

    private BaiThiBUS btBUS = new BaiThiBUS();
    private DeThiBUS dtBUS = new DeThiBUS();
    private NguoiDungBUS ndBUS = new NguoiDungBUS();
    ArrayList<BaiThiDTO> listHienTai = btBUS.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public BaiThi(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        int maQuyen = mainFrame.getNguoiDung().getManhomquyen();
        int userId = mainFrame.getNguoiDung().getId();
        if (maQuyen == 1) {
            this.listHienTai = btBUS.getAll();
        } else {
            this.listHienTai = btBUS.getByUser(userId);
        }
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

        String[] header = {"Mã bài thi", "Tên đề thi", "Người làm", "Điểm", "Thời gian vào", "Thời gian làm"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
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

        String[] action = {"detail", "export"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "11", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã bài thi", "Mã đề", "Mã người dùng"});
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
            int maQuyen = mainFrame.getNguoiDung().getManhomquyen();
            if (maQuyen == 1) {
                listHienTai = btBUS.getAll();
            } else {
                listHienTai = btBUS.getByUser(mainFrame.getNguoiDung().getId());
            }
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

    public void loadDataTable(ArrayList<BaiThiDTO> result) {
        tblModel.setRowCount(0);
        for (BaiThiDTO bt : result) {
            String tenDe = dtBUS.getById(bt.getMade()) != null ? dtBUS.getById(bt.getMade()).getTende() : "N/A";
            String tenNguoiDung = ndBUS.getHotenById(bt.getManguoidung());
            tblModel.addRow(new Object[]{
                bt.getMabaithi(),
                tenDe,
                tenNguoiDung,
                bt.getDiemthi(),
                Formater.FormatTime(bt.getThoigianvaothi()),
                bt.getThoigianlambai() + " giây"
            });
        }
    }

    public void thucHienTimKiem() {
        String kieu = (String) search.cbxChoose.getSelectedItem();
        String text = search.txtSearchForm.getText();
        ArrayList<BaiThiDTO> danhSachGoc;
        if (mainFrame.getNguoiDung().getManhomquyen() == 1) {
            danhSachGoc = btBUS.getAll();
        } else {
            danhSachGoc = btBUS.getByUser(mainFrame.getNguoiDung().getId());
        }
        listHienTai = btBUS.search(text, kieu, danhSachGoc);
        loadDataTable(listHienTai);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        int index = table.getSelectedRow();

        if (source == mainFunction.btn.get("detail")) {
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kết quả bài thi!");
                return;
            }
            int maBT = (int) table.getValueAt(index, 0);
            BaiThiDTO selected = btBUS.getById(maBT);
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            new ChiTietBaiThiDialog(owner, "Chi tiết kết quả bài thi", true, selected);
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
