package GUI.Panel;

import BUS.NhomQuyenBUS;
import DTO.NhomQuyenDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.NhomQuyenDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class NhomQuyen extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius main, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable tableNhomQuyen;
    JScrollPane scrollTableNhomQuyen;
    MainFunction mainFunction;
    IntegratedSearch search;
    DefaultTableModel tblModel;

    NhomQuyenBUS nhomquyenBUS = new NhomQuyenBUS();
    ArrayList<NhomQuyenDTO> listHienTai = nhomquyenBUS.getAll();

    Color BackgroundColor = new Color(240, 247, 250);

    public NhomQuyen() {
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        tableNhomQuyen = new JTable();
        scrollTableNhomQuyen = new JScrollPane();

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] header = new String[]{"Mã nhóm quyền", "Tên nhóm quyền"};
        tblModel.setColumnIdentifiers(header);
        tableNhomQuyen.setModel(tblModel);
        tableNhomQuyen.setFocusable(false);
        tableNhomQuyen.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableNhomQuyen.getTableHeader().setPreferredSize(new Dimension(0, 40));
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableNhomQuyen.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scrollTableNhomQuyen.setViewportView(tableNhomQuyen);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableNhomQuyen.getColumnCount(); i++) {
            tableNhomQuyen.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableNhomQuyen.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(tableNhomQuyen, 0, TableSorter.INTEGER_COMPARATOR);

        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
        pnlBorder1.setBackground(BackgroundColor);
        this.add(pnlBorder1, BorderLayout.NORTH);
        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(BackgroundColor);
        this.add(pnlBorder2, BorderLayout.SOUTH);
        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(BackgroundColor);
        this.add(pnlBorder3, BorderLayout.EAST);
        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(BackgroundColor);
        this.add(pnlBorder4, BorderLayout.WEST);

        contentCenter = new JPanel();
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        this.add(contentCenter, BorderLayout.CENTER);

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        functionBar.setBackground(Color.WHITE);

        String[] action = {"create", "update", "delete", "detail", "export"};
        mainFunction = new MainFunction(action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã nhóm", "Tên nhóm"});
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
            listHienTai = nhomquyenBUS.getAll();
            loadDataTable(listHienTai);
        });

        functionBar.add(search);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTableNhomQuyen, BorderLayout.CENTER);
        contentCenter.add(main, BorderLayout.CENTER);
    }

    public void thucHienTimKiem() {
        String kieuTimKiem = (String) search.cbxChoose.getSelectedItem();
        String noiDungTim = search.txtSearchForm.getText();
        listHienTai = nhomquyenBUS.search(noiDungTim, kieuTimKiem);
        loadDataTable(listHienTai);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }

    public void loadDataTable(ArrayList<NhomQuyenDTO> danhSach) {
        tblModel.setRowCount(0);
        for (NhomQuyenDTO nq : danhSach) {
            tblModel.addRow(new Object[]{
                nq.getManhomquyen(),
                nq.getTennhomquyen()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object source = e.getSource();

        if (source == mainFunction.btn.get("create")) {
            new NhomQuyenDialog(this, owner, "Thêm nhóm quyền mới", true, "create", null);
        } else if (source == mainFunction.btn.get("update")) {
            int index = tableNhomQuyen.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm quyền cần sửa!");
            } else {
                int modelRow = tableNhomQuyen.convertRowIndexToModel(index);
                int ma = (int) tblModel.getValueAt(modelRow, 0);
                NhomQuyenDTO selected = nhomquyenBUS.getById(ma);
                new NhomQuyenDialog(this, owner, "Chỉnh sửa nhóm quyền", true, "update", selected);
            }
        } else if (source == mainFunction.btn.get("detail")) {
            int index = tableNhomQuyen.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm quyền cần xem!");
            } else {
                int modelRow = tableNhomQuyen.convertRowIndexToModel(index);
                int ma = (int) tblModel.getValueAt(modelRow, 0);
                NhomQuyenDTO selected = nhomquyenBUS.getById(ma);
                new NhomQuyenDialog(this, owner, "Thông tin chi tiết nhóm quyền", true, "view", selected);
            }
        } else if (source == mainFunction.btn.get("delete")) {
            int index = tableNhomQuyen.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm quyền cần xóa!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhóm quyền này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int modelRow = tableNhomQuyen.convertRowIndexToModel(index);
                    int ma = (int) tblModel.getValueAt(modelRow, 0);
                    if (nhomquyenBUS.delete(ma)) {
                        JOptionPane.showMessageDialog(this, "Xóa thành công!");
                        listHienTai = nhomquyenBUS.getAll();
                        loadDataTable(listHienTai);
                    }
                }
            }
        } else if (source == mainFunction.btn.get("export")) {
            try {
                helper.JTableExporter.exportJTableToExcel(tableNhomQuyen);
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}