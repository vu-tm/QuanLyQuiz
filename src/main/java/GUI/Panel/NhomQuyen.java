package GUI.Panel;

import BUS.NhomQuyenBUS;
import DTO.NhomQuyenDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.AddNhomQuyenDialog;
import GUI.Dialog.ChiTietNhomQuyenDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class NhomQuyen extends JPanel implements ActionListener, ItemListener {

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private NhomQuyenBUS bus = new NhomQuyenBUS();

    private PanelBorderRadius main, functionBar;
    private JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    private JScrollPane scrollTable;
    private MainFunction mainFunction;
    private IntegratedSearch search;

    private List<NhomQuyenDTO> listHienTai = bus.getAll();
    private Color BackgroundColor = new Color(240, 247, 250);

    public NhomQuyen() {
        initComponent();
        loadDataTable();
    }

    private void initComponent() {
        setBackground(BackgroundColor);
        setLayout(new BorderLayout(0, 0));

        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
        pnlBorder1.setBackground(BackgroundColor);
        add(pnlBorder1, BorderLayout.NORTH);

        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(BackgroundColor);
        add(pnlBorder2, BorderLayout.SOUTH);

        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(BackgroundColor);
        add(pnlBorder3, BorderLayout.EAST);

        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(BackgroundColor);
        add(pnlBorder4, BorderLayout.WEST);

        contentCenter = new JPanel();
        contentCenter.setBackground(BackgroundColor);
        contentCenter.setLayout(new BorderLayout(10, 10));
        add(contentCenter, BorderLayout.CENTER);

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        functionBar.setBackground(Color.WHITE);

        // các chức năng của chương trình
        String[] actions = {"create", "update", "delete", "detail"};
        mainFunction = new MainFunction(actions);
        for (String ac : actions) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        search = new IntegratedSearch(new String[]{"Tất cả", "Mã nhóm", "Tên nhóm"});
        searchField = search.txtSearchForm;
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                thucHienTimKiem();
            }
        });
        search.cbxChoose.addItemListener(this);
        search.btnReset.addActionListener(e -> {
            searchField.setText("");
            search.cbxChoose.setSelectedIndex(0);
            listHienTai = bus.getAll();
            loadDataTable();
        });
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        String[] columns = {"Mã nhóm quyền", "Tên nhóm quyền", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(240, 240, 240));
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(245, 245, 245));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        table.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(table, 0, TableSorter.INTEGER_COMPARATOR);

        scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        main = new PanelBorderRadius();
        main.setLayout(new BorderLayout());
        main.setBackground(Color.WHITE);
        main.add(scrollTable, BorderLayout.CENTER);

        contentCenter.add(main, BorderLayout.CENTER);
    }

    private void thucHienTimKiem() {
        String kieuTim = (String) search.cbxChoose.getSelectedItem();
        String tuKhoa = searchField.getText().trim().toLowerCase();
        List<NhomQuyenDTO> all = bus.getAll();
        List<NhomQuyenDTO> result = new java.util.ArrayList<>();

        if (tuKhoa.isEmpty()) {
            result = all;
        } else {
            for (NhomQuyenDTO item : all) {
                boolean match = false;
                switch (kieuTim) {
                    case "Mã nhóm":
                        match = String.valueOf(item.getManhomquyen()).contains(tuKhoa);
                        break;
                    case "Tên nhóm":
                        match = item.getTennhomquyen().toLowerCase().contains(tuKhoa);
                        break;
                    default: // Tất cả
                        match = String.valueOf(item.getManhomquyen()).contains(tuKhoa)
                                || item.getTennhomquyen().toLowerCase().contains(tuKhoa);
                        break;
                }
                if (match) {
                    result.add(item);
                }
            }
        }
        listHienTai = result;
        loadDataTable();
    }

    private void loadDataTable() {
        tableModel.setRowCount(0);
        for (NhomQuyenDTO item : listHienTai) {
            String trangThai = item.getTrangthai() == 1 ? "Hoạt động" : "Đã khóa";
            tableModel.addRow(new Object[]{
                item.getManhomquyen(),
                item.getTennhomquyen(),
                trangThai
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        Object src = e.getSource();

        if (src == mainFunction.btn.get("create")) {
            new AddNhomQuyenDialog(owner, "Thêm nhóm quyền", null, bus, () -> {
                listHienTai = bus.getAll();
                loadDataTable();
            });
        } else if (src == mainFunction.btn.get("update")) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm quyền cần sửa!");
                return;
            }
            int modelRow = table.convertRowIndexToModel(row);
            int maNhomQuyen = (int) tableModel.getValueAt(modelRow, 0);
            NhomQuyenDTO dto = bus.getById(maNhomQuyen);
            if (dto != null) {
                new AddNhomQuyenDialog(owner, "Sửa nhóm quyền", dto, bus, () -> {
                    listHienTai = bus.getAll();
                    loadDataTable();
                });
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhóm quyền!");
            }
        } else if (src == mainFunction.btn.get("delete")) {
            xoaNhomQuyen();
        } else if (src == mainFunction.btn.get("detail")) {
            xemChiTiet(owner);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            thucHienTimKiem();
        }
    }

    private void xoaNhomQuyen() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm quyền cần xóa!");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maNhomQuyen = (int) tableModel.getValueAt(modelRow, 0);
        String tenNhomQuyen = (String) tableModel.getValueAt(modelRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa nhóm quyền \"" + tenNhomQuyen + "\"?\n(Xóa nhóm quyền sẽ xóa tất cả phân quyền liên quan)",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bus.delete(maNhomQuyen)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                listHienTai = bus.getAll();
                loadDataTable();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void xemChiTiet(JFrame owner) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhóm quyền cần xem!");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int maNhomQuyen = (int) tableModel.getValueAt(modelRow, 0);
        NhomQuyenDTO dto = bus.getById(maNhomQuyen);
        if (dto != null) {
            new ChiTietNhomQuyenDialog(owner, "Thông tin nhóm quyền", dto, bus);
        }
    }
}