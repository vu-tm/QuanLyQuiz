package GUI.Panel;

import BUS.BaiThiBUS;
import BUS.DeThiBUS;
import DTO.BaiThiDTO;
import DTO.DeThiDTO;
import GUI.Main;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Dialog.ChiTietBaiThiDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class KetQuaHS extends JPanel implements ActionListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable table;
    JScrollPane scrollTable;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    BaiThiBUS bus = new BaiThiBUS();
    DeThiBUS deThiBUS = new DeThiBUS();
    ArrayList<BaiThiDTO> listHienTai;

    Color BackgroundColor = new Color(240, 247, 250);

    public KetQuaHS() {
        initComponent();
        loadDataTable();
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

        String[] header = { "Mã bài thi", "Mã đề", "Tên đề thi", "Điểm", "Thời gian làm bài", "Ngày thi" };
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setFocusable(false);
        table.setRowHeight(35);
        scrollTable.setViewportView(table);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

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

        String[] action = { "detail" };
        mainFunction = new MainFunction(action);
        mainFunction.btn.get("detail").addActionListener(this);
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[] { "Tất cả", "Mã bài thi", "Mã đề" });
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Implement search if needed
            }
        });
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTable, BorderLayout.CENTER);
        contentCenter.add(main, BorderLayout.CENTER);
    }

    private void loadDataTable() {
        listHienTai = bus.getByUser(Main.user.getId());
        tblModel.setRowCount(0);
        for (BaiThiDTO bt : listHienTai) {
            DeThiDTO deThi = deThiBUS.getById(bt.getMade());
            tblModel.addRow(new Object[] {
                    bt.getMabaithi(),
                    bt.getMade(),
                    deThi != null ? deThi.getTende() : "N/A",
                    bt.getDiemthi(),
                    bt.getThoigianlambai() + " giây",
                    bt.getThoigianvaothi()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("detail")) {
            int index = table.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bài thi!");
                return;
            }
            int maBaiThi = (int) table.getValueAt(index, 0);
            BaiThiDTO selected = bus.getById(maBaiThi);
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            new ChiTietBaiThiDialog(owner, "Chi tiết bài thi", true, selected);
        }
    }
}
