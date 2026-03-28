package GUI.Panel;

import BUS.BaiThiBUS;
import BUS.DeThiBUS;
import BUS.KyThiBUS;
import BUS.MonHocBUS;
import DTO.DeThiDTO;
import DTO.NguoiDungDTO;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import GUI.Dialog.LamBaiDialog;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class LamBai extends JPanel implements ActionListener {

    private GUI.Main mainFrame;
    private PanelBorderRadius pnlMain, functionBar;
    private JPanel pnlBorder1, pnlBorder2, pnlBorder3, pnlBorder4, contentCenter;
    private JTable table;
    private JScrollPane scrollTable;
    private MainFunction mainFunction;
    private JComboBox<String> cbxFilterTrangThai;
    private DefaultTableModel tblModel;

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

        // 1. Khởi tạo Table
        table = new JTable();
        scrollTable = new JScrollPane();
        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] header = {"Mã đề", "Tên đề thi", "Kỳ thi", "Môn học", "Thời gian", "Số câu", "Trạng thái"};
        tblModel.setColumnIdentifiers(header);
        table.setModel(tblModel);
        table.setFocusable(false);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        // Renderer căn giữa và TÔ MÀU TRẠNG THÁI
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                // Cột trạng thái là cột số 6
                if (column == 6 && value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "Đang mở":
                            c.setForeground(new Color(0, 153, 51)); // Xanh lá
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

        scrollTable.setViewportView(table);
        table.setAutoCreateRowSorter(true);
        TableSorter.configureTableColumnSorter(table, 0, TableSorter.INTEGER_COMPARATOR);

        // 2. Các Panel đệm (Giữ nguyên)
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

        // 4. Thanh chức năng (Sửa ô tìm kiếm thành ComboBox)
        functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new BorderLayout());
        functionBar.setBorder(new EmptyBorder(10, 20, 10, 20));
        functionBar.setBackground(Color.WHITE);

        // Bên trái: Nút vào thi
        String[] action = {"create"};
        mainFunction = new MainFunction(mainFrame.getNguoiDung().getManhomquyen(), "0", action);
        mainFunction.btn.get("create").setText("VÀO THI");
        mainFunction.btn.get("create").addActionListener(this);
        functionBar.add(mainFunction, BorderLayout.WEST);

        // Bên phải: Bộ lọc trạng thái
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
        pnlMain.add(scrollTable, BorderLayout.CENTER);
        contentCenter.add(pnlMain, BorderLayout.CENTER);
    }

    public void loadData() {
        listDeThi = deThiBUS.getDeThiChoSinhVien(user.getManguoidung());
        loadDataTable(listDeThi);
    }

    public void loadDataTable(ArrayList<DeThiDTO> result) {
        tblModel.setRowCount(0);
        for (DeThiDTO dt : result) {
            tblModel.addRow(new Object[]{
                dt.getMade(),
                dt.getTende(),
                kyThiBUS.getTenById(dt.getMakythi()),
                monHocBUS.getTenById(dt.getMonthi()),
                dt.getThoigianthi() + " phút",
                dt.getTongsocau(),
                calculateTrangThai(dt.getMakythi(), dt.getMade())
            });
        }
    }

    // Logic lọc mới theo ComboBox
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
            String trangThai = tblModel.getValueAt(modelRow, 6).toString();

            if (trangThai.equals("Đã hoàn thành")) {
                JOptionPane.showMessageDialog(this, "Bạn đã hoàn thành bài thi này rồi, không thể thi lại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (trangThai.equals("Hết hạn")) {
                JOptionPane.showMessageDialog(this, "Kỳ thi này đã kết thúc, bạn không thể vào thi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (trangThai.equals("Chưa bắt đầu")) {
                JOptionPane.showMessageDialog(this, "Kỳ thi này chưa tới giờ bắt đầu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int made = (int) tblModel.getValueAt(modelRow, 0);
            DeThiDTO selectedDeThi = deThiBUS.getById(made);

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
        if (baiThiBUS.checkDaLam(user.getManguoidung(), made)) {
            return "Đã hoàn thành";
        }

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
