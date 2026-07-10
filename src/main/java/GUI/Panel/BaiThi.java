package GUI.Panel;

import BUS.BaiThiBUS;
import BUS.DeThiBUS;
import BUS.NguoiDungBUS;
import DTO.BaiThiDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PaginatedTable;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.ChiTietBaiThiDialog;
import helper.Formater;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class BaiThi extends JPanel implements ActionListener, ItemListener {

    PanelBorderRadius pnlMain, functionBar;
    JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    JTable table;
    PaginatedTable paginatedTable;
    MainFunction mainFunction;
    IntegratedSearch search;
    private GUI.Main mainFrame;

    private final BaiThiBUS btBUS = new BaiThiBUS();
    private final DeThiBUS dtBUS = new DeThiBUS();
    private final NguoiDungBUS ndBUS = new NguoiDungBUS();
    ArrayList<BaiThiDTO> listHienTai;

    private final Color backgroundColor = new Color(240, 247, 250);

    public BaiThi(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        int maQuyen = mainFrame.getNguoiDung().getManhomquyen();
        int userId = mainFrame.getNguoiDung().getManguoidung();
        if (maQuyen == 1) {
            this.listHienTai = btBUS.getAll();
        } else {
            this.listHienTai = btBUS.getByUser(userId);
        }
        initComponent();
        loadDataTable(listHienTai);
    }

    private void initComponent() {
        this.setBackground(backgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        // Khởi tạo PaginatedTable với các tiêu đề cột
        String[] header = {"Mã bài thi", "Tên đề thi", "Người làm", "Điểm", "Thời gian vào", "Thời gian làm"};
        paginatedTable = new PaginatedTable(header);
        table = paginatedTable.getTable();

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setFocusable(false);
        table.setRowHeight(30);

        // Căn giữa tất cả các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Cấu hình sắp xếp toàn bộ dữ liệu
        @SuppressWarnings("unchecked")
        Comparator<Object>[] comps = new Comparator[6];
        comps[0] = TableSorter.INTEGER_COMPARATOR;          // Mã bài thi
        comps[1] = TableSorter.STRING_COMPARATOR;           // Tên đề thi
        comps[2] = TableSorter.STRING_COMPARATOR;           // Người làm
        comps[3] = (o1, o2) -> {
            // Điểm có thể là null hoặc số thực
            double d1 = o1 == null ? -1 : Double.parseDouble(o1.toString());
            double d2 = o2 == null ? -1 : Double.parseDouble(o2.toString());
            return Double.compare(d1, d2);
        };
        comps[4] = TableSorter.STRING_COMPARATOR;           // Thời gian vào (đã format)
        comps[5] = (o1, o2) -> {
            // Thời gian làm dạng "xxx giây" -> trích xuất số giây
            String s1 = o1.toString().replace(" giây", "");
            String s2 = o2.toString().replace(" giây", "");
            int sec1 = Integer.parseInt(s1);
            int sec2 = Integer.parseInt(s2);
            return Integer.compare(sec1, sec2);
        };
        paginatedTable.enableFullDataSorting(comps);

        // Các panel viền
        pnlBorder1 = new JPanel();
        pnlBorder1.setPreferredSize(new Dimension(0, 10));
        pnlBorder1.setBackground(backgroundColor);
        pnlBorder2 = new JPanel();
        pnlBorder2.setPreferredSize(new Dimension(0, 10));
        pnlBorder2.setBackground(backgroundColor);
        pnlBorder3 = new JPanel();
        pnlBorder3.setPreferredSize(new Dimension(10, 0));
        pnlBorder3.setBackground(backgroundColor);
        pnlBorder4 = new JPanel();
        pnlBorder4.setPreferredSize(new Dimension(10, 0));
        pnlBorder4.setBackground(backgroundColor);

        this.add(pnlBorder1, BorderLayout.NORTH);
        this.add(pnlBorder2, BorderLayout.SOUTH);
        this.add(pnlBorder3, BorderLayout.EAST);
        this.add(pnlBorder4, BorderLayout.WEST);

        contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(backgroundColor);
        this.add(contentCenter, BorderLayout.CENTER);

        // Function Bar
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));
        functionBar.setBackground(Color.WHITE);

        String[] action = {"detail", "delete", "export"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "11", action);
        for (String ac : action) {
            mainFunction.btn.get(ac).addActionListener(this);
        }
        functionBar.add(mainFunction);

        // Search
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
                listHienTai = btBUS.getByUser(mainFrame.getNguoiDung().getManguoidung());
            }
            loadDataTable(listHienTai);
        });
        functionBar.add(search);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        pnlMain = new PanelBorderRadius();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.add(paginatedTable, BorderLayout.CENTER);
        contentCenter.add(pnlMain, BorderLayout.CENTER);
    }

    public void loadDataTable(ArrayList<BaiThiDTO> danhSach) {
        this.listHienTai = danhSach;
        List<Object[]> data = new ArrayList<>();
        for (BaiThiDTO bt : danhSach) {
            String tenDe = dtBUS.getById(bt.getMade()) != null ? dtBUS.getById(bt.getMade()).getTende() : "N/A";
            String tenNguoiDung = ndBUS.getHotenById(bt.getManguoidung());
            data.add(new Object[]{
                bt.getMabaithi(),
                tenDe,
                tenNguoiDung,
                bt.getDiemthi(),
                Formater.FormatTime(bt.getThoigianvaothi()),
                bt.getThoigianlambai() + " giây"
            });
        }
        paginatedTable.setData(data);
    }

    public void thucHienTimKiem() {
        String kieu = (String) search.cbxChoose.getSelectedItem();
        String text = search.txtSearchForm.getText();
        ArrayList<BaiThiDTO> danhSachGoc;
        if (mainFrame.getNguoiDung().getManhomquyen() == 1) {
            danhSachGoc = btBUS.getAll();
        } else {
            danhSachGoc = btBUS.getByUser(mainFrame.getNguoiDung().getManguoidung());
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
            int modelIndex = table.convertRowIndexToModel(index);
            int maBT = (int) table.getModel().getValueAt(modelIndex, 0);
            BaiThiDTO selected = btBUS.getById(maBT);
            boolean isAdmin = (mainFrame.getNguoiDung().getManhomquyen() == 1);
            JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
            new ChiTietBaiThiDialog(owner, "Chi tiết kết quả bài thi", true, selected, isAdmin);
        } else if (source == mainFunction.btn.get("delete")) {
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kết quả bài thi cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa kết quả bài thi này? Dữ liệu chi tiết bài thi cũng sẽ bị mất!",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int modelIndex = table.convertRowIndexToModel(index);
                int maBT = (int) table.getModel().getValueAt(modelIndex, 0);

                if (btBUS.delete(maBT) > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");

                    int maQuyen = mainFrame.getNguoiDung().getManhomquyen();
                    int userId = mainFrame.getNguoiDung().getManguoidung();
                    if (maQuyen == 1) {
                        listHienTai = btBUS.getAll();
                    } else {
                        listHienTai = btBUS.getByUser(userId);
                    }
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
                JOptionPane.showMessageDialog(this, "Xuất file Excel thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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