package GUI.Panel;

import BUS.BaiThiBUS;
import BUS.DeThiBUS;
import BUS.MonHocBUS;
import DTO.DeThiDTO;
import GUI.Main;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class VaoThi extends JPanel implements ActionListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable table;
    JScrollPane scrollTable;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    DeThiBUS deThiBUS;
    MonHocBUS monHocBUS;
    BaiThiBUS baiThiBUS;
    ArrayList<DeThiDTO> listDeThi;

    Color BackgroundColor = new Color(240, 247, 250);

    public VaoThi() {
        deThiBUS = new DeThiBUS();
        monHocBUS = new MonHocBUS();
        baiThiBUS = new BaiThiBUS();
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

        String[] header = { "Mã đề", "Tên đề thi", "Môn học", "Số câu hỏi", "Thời gian làm bài", "Trạng thái" };
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setFocusable(false);
        table.setRowHeight(35);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        scrollTable.setViewportView(table);

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

        String[] action = { "create" };
        mainFunction = new MainFunction(action);
        mainFunction.btn.get("create").setText("Bắt đầu thi");
        // Update icon to something else if needed, but 'add' is fine for now
        mainFunction.btn.get("create").addActionListener(this);
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[] { "Tất cả", "Mã đề", "Tên đề" });
        search.txtSearchForm.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                search();
            }
        });
        
        search.btnReset.addActionListener(e -> {
            search.txtSearchForm.setText("");
            search.cbxChoose.setSelectedIndex(0);
            loadDataTable();
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
        if (Main.user == null) {
            JOptionPane.showMessageDialog(this, "Phiên đăng nhập hết hạn!");
            return;
        }
        listDeThi = deThiBUS.getDanhSachDeThiByNguoiDung(Main.user.getId());
        if (listDeThi == null)
            listDeThi = new ArrayList<>();
        tblModel.setRowCount(0);
        for (DeThiDTO dt : listDeThi) {
            String tenMon = monHocBUS.getTenById(dt.getMonthi());
            boolean daThi = baiThiBUS.hasTakenExam(Main.user.getId(), dt.getMade());
            tblModel.addRow(new Object[] {
                    dt.getMade(),
                    dt.getTende(),
                    tenMon,
                    dt.getTongsocau(),
                    dt.getThoigianthi() + " phút",
                    daThi ? "Đã thi" : "Chưa thi"
            });
        }
    }

    private void search() {
        String text = search.txtSearchForm.getText().toLowerCase();
        String type = search.cbxChoose.getSelectedItem().toString();
        
        ArrayList<DeThiDTO> result = deThiBUS.search(text, type, listDeThi);
        
        tblModel.setRowCount(0);
        for (DeThiDTO dt : result) {
            String tenMon = monHocBUS.getTenById(dt.getMonthi());
            boolean daThi = baiThiBUS.hasTakenExam(Main.user.getId(), dt.getMade());
            
            tblModel.addRow(new Object[] {
                    dt.getMade(),
                    dt.getTende(),
                    tenMon,
                    dt.getTongsocau(),
                    dt.getThoigianthi() + " phút",
                    daThi ? "Đã thi" : "Chưa thi"
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("create")) {
            int index = table.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi!");
                return;
            }
            int made = (int) table.getValueAt(index, 0);
            String trangThai = (String) table.getValueAt(index, 5);
            
            if (trangThai.equals("Đã thi")) {
                JOptionPane.showMessageDialog(this, "Bạn đã hoàn thành bài thi này và không thể thi lại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DeThiDTO selectedDe = deThiBUS.getById(made);
            
            if (JOptionPane.showConfirmDialog(this, "Bắt đầu làm bài thi: " + selectedDe.getTende() + "?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                ((Main) mainFrame).setPanel(new LamBai(selectedDe));
            }
        }
    }
}
