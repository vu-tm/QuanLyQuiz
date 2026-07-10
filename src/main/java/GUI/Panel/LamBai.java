package GUI.Panel;

import BUS.BaiThiBUS;
import BUS.DeThiBUS;
import BUS.KyThiBUS;
import BUS.MonHocBUS;
import DTO.DeThiDTO;
import DTO.NguoiDungDTO;
import GUI.Component.MainFunction;
import GUI.Component.PaginatedTable;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.LamBaiDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class LamBai extends JPanel implements ActionListener {

    private GUI.Main mainFrame;
    private PanelBorderRadius pnlMain, functionBar;
    private JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    private JTable table;
    private PaginatedTable paginatedTable;
    private MainFunction mainFunction;
    private JComboBox<String> cbxFilterTrangThai;
    private DefaultTableCellRenderer statusRenderer;

    private DeThiBUS deThiBUS = new DeThiBUS();
    private KyThiBUS kyThiBUS = new KyThiBUS();
    private MonHocBUS monHocBUS = new MonHocBUS();
    private BaiThiBUS baiThiBUS = new BaiThiBUS();

    private ArrayList<DeThiDTO> listDeThi;
    private NguoiDungDTO user;

    Color BackgroundColor = new Color(240, 247, 250);

    public LamBai(GUI.Main mainFrame) {
        this.mainFrame = mainFrame;
        this.user = mainFrame.getNguoiDung();
        initComponent();
        loadData();
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(true);

        String[] header = {"Mã đề", "Tên đề thi", "Kỳ thi", "Môn học", "Thời gian", "Số câu", "Trạng thái"};
        paginatedTable = new PaginatedTable(header);
        table = paginatedTable.getTable();

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setFocusable(false);
        table.setRowHeight(40);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                if (column == 6 && value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "Đang mở":
                            c.setForeground(new Color(0, 153, 51));
                            c.setFont(c.getFont().deriveFont(Font.BOLD));
                            break;
                        case "Hết hạn":
                            c.setForeground(Color.RED);
                            c.setFont(c.getFont().deriveFont(Font.BOLD));
                            break;
                        case "Chưa bắt đầu":
                            c.setForeground(Color.BLUE);
                            c.setFont(c.getFont().deriveFont(Font.BOLD));
                            break;
                        case "Đã hoàn thành":
                            c.setForeground(new Color(128, 128, 128));
                            c.setFont(c.getFont().deriveFont(Font.BOLD));
                            break;
                        default:
                            c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.BLACK);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }

                if (isSelected) {
                    c.setBackground(new Color(184, 207, 229));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);

        table.setAutoCreateRowSorter(false);
        Comparator<Object>[] comps = new Comparator[7];
        comps[0] = TableSorter.INTEGER_COMPARATOR;
        comps[1] = TableSorter.STRING_COMPARATOR;
        comps[2] = TableSorter.STRING_COMPARATOR;
        comps[3] = TableSorter.STRING_COMPARATOR;
        comps[4] = (Object o1, Object o2) -> {
            int t1 = Integer.parseInt(o1.toString().replace(" phút", ""));
            int t2 = Integer.parseInt(o2.toString().replace(" phút", ""));
            return Integer.compare(t1, t2);
        };
        comps[5] = TableSorter.INTEGER_COMPARATOR;
        comps[6] = TableSorter.STRING_COMPARATOR;
        paginatedTable.enableFullDataSorting(comps);

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

        contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BackgroundColor);
        this.add(contentCenter, BorderLayout.CENTER);

        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new BorderLayout());
        functionBar.setBorder(new EmptyBorder(10, 20, 10, 20));
        functionBar.setBackground(Color.WHITE);

        String[] action = {"create"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "0", action);
        mainFunction.btn.get("create").setText("VÀO THI");
        mainFunction.btn.get("create").addActionListener(this);
        functionBar.add(mainFunction, BorderLayout.WEST);

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        pnlFilter.setOpaque(false);
        pnlFilter.add(new JLabel("Lọc trạng thái:"));
        cbxFilterTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đang mở", "Chưa bắt đầu", "Hết hạn", "Đã hoàn thành"});
        cbxFilterTrangThai.setPreferredSize(new Dimension(150, 35));
        cbxFilterTrangThai.addActionListener(e -> thucHienLoc());
        pnlFilter.add(cbxFilterTrangThai);

        functionBar.add(pnlFilter, BorderLayout.EAST);

        contentCenter.add(functionBar, BorderLayout.NORTH);

        pnlMain = new PanelBorderRadius();
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.add(paginatedTable, BorderLayout.CENTER);
        contentCenter.add(pnlMain, BorderLayout.CENTER);
    }

    public void loadData() {
        listDeThi = deThiBUS.getDeThiChoSinhVien(user.getManguoidung());
        loadDataTable(listDeThi);
    }

    public void loadDataTable(ArrayList<DeThiDTO> danhSach) {
        List<Object[]> data = new ArrayList<>();
        for (DeThiDTO dt : danhSach) {
            data.add(new Object[]{
                dt.getMade(),
                dt.getTende(),
                kyThiBUS.getTenById(dt.getMakythi()),
                monHocBUS.getTenById(dt.getMonthi()),
                dt.getThoigianthi() + " phút",
                dt.getTongsocau(),
                calculateTrangThai(dt.getMakythi(), dt.getMade())
            });
        }
        paginatedTable.setData(data);
    }

    public void thucHienLoc() {
        String selected = (String) cbxFilterTrangThai.getSelectedItem();
        if (selected.equals("Tất cả")) {
            loadDataTable(listDeThi);
            return;
        }

        ArrayList<DeThiDTO> result = new ArrayList<>();
        for (DeThiDTO dt : listDeThi) {
            String currentStatus = calculateTrangThai(dt.getMakythi(), dt.getMade());
            if (currentStatus.equals(selected)) {
                result.add(dt);
            }
        }
        loadDataTable(result);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainFunction.btn.get("create")) {
            int index = table.getSelectedRow();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đề thi muốn làm!");
                return;
            }

            int modelRow = table.convertRowIndexToModel(index);
            int made = (int) table.getValueAt(modelRow, 0);
            String trangThai = table.getValueAt(modelRow, 6).toString();

            if (trangThai.equals("Hết hạn")) {
                JOptionPane.showMessageDialog(this, "Kỳ thi này đã kết thúc, bạn không thể vào thi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (trangThai.equals("Chưa bắt đầu")) {
                JOptionPane.showMessageDialog(this, "Kỳ thi này chưa tới giờ bắt đầu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DeThiDTO selectedDeThi = deThiBUS.getById(made);
            int userId = user.getManguoidung();

            if (baiThiBUS.checkDaLam(userId, made)) {
                int confirmLai = JOptionPane.showConfirmDialog(this,
                        "Bạn đã làm bài thi này rồi!\n"
                        + "Điểm cũ sẽ bị xóa và thay thế bằng kết quả mới.\n\n"
                        + "Bạn có muốn làm lại?",
                        "Xác nhận làm lại",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirmLai != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn bắt đầu làm bài: " + selectedDeThi.getTende() + "?\n"
                    + "Thời gian làm bài: " + selectedDeThi.getThoigianthi() + " phút.",
                    "Xác nhận vào thi", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                LamBaiDialog dialog = new LamBaiDialog(mainFrame, selectedDeThi, user);
                dialog.setVisible(true);
                loadData();
            }
        }
    }

    private String calculateTrangThai(int maKyThi, int made) {
        DTO.KyThiDTO kt = kyThiBUS.getById(maKyThi);
        if (kt == null || kt.getTrangthai() == 0) {
            return "Hết hạn";
        }

        long now = System.currentTimeMillis();
        long start = kt.getThoigianbatdau().getTime();
        long end = kt.getThoigianketthuc().getTime();

        if (now < start) {
            return "Chưa bắt đầu";
        } else if (now > end) {
            return "Hết hạn";
        } else {
            return "Đang mở";
        }
    }
}