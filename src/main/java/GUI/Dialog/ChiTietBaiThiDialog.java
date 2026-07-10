package GUI.Dialog;

import BUS.BaiThiBUS;
import BUS.CauHoiBUS;
import BUS.DeThiBUS;
import BUS.NguoiDungBUS;
import DTO.BaiThiDTO;
import DTO.ChiTietBaiThiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import helper.Formater;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public final class ChiTietBaiThiDialog extends JDialog implements ActionListener {

    private JPanel pnmain, pnmain_top, pnmain_bottom, pnmain_btn;
    private InputForm txtTenDe, txtNguoiLam, txtDiem, txtThoiGianVao, txtThoiGianLam, txtSoCauDung;
    private DefaultTableModel tblModel;
    private JTable table;
    private JScrollPane scrollTable;

    private BaiThiDTO baithi;
    private BaiThiBUS baithiBUS = new BaiThiBUS();
    private DeThiBUS dethiBUS = new DeThiBUS();
    private NguoiDungBUS ndBUS = new NguoiDungBUS();
    private CauHoiBUS cauHoiBUS = new CauHoiBUS();

    private ButtonCustom btnDong;
    private boolean isAdmin;

    // --- Phóng to / thu nhỏ cửa sổ ---
    private JButton btnMaximize;
    private Rectangle savedBounds;
    private boolean isMaximized = false;

    // --- Wrap text + tự tính chiều cao dòng ---
    private int[] wrapCols;
    private static final int MIN_ROW_HEIGHT = 35;

    public ChiTietBaiThiDialog(JFrame owner, String title, boolean modal, BaiThiDTO baithiDTO, boolean isAdmin) {
        super(owner, title, modal);
        this.baithi = baithiDTO;
        this.isAdmin = isAdmin;
        initComponent();
        fillData();
        loadDataTable();
        this.setVisible(true);
    }

    public void initComponent() {
        this.setSize(new Dimension(1100, 650));
        this.setLayout(new BorderLayout(0, 0));
        this.setResizable(true);

        pnmain = new JPanel(new BorderLayout());

        // ----- Khối trên cùng: thanh công cụ (nút phóng to) + thông tin bài thi -----
        JPanel pnmain_topWrapper = new JPanel(new BorderLayout());
        pnmain_topWrapper.setBackground(Color.WHITE);

        JPanel pnHeaderTool = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        pnHeaderTool.setBackground(Color.WHITE);

        btnMaximize = makeMaximizeButton();
        btnMaximize.addActionListener(e -> toggleMaximize());
        pnHeaderTool.add(btnMaximize);

        pnmain_topWrapper.add(pnHeaderTool, BorderLayout.NORTH);

        pnmain_top = new JPanel(new GridLayout(2, 3, 10, 10));
        pnmain_top.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnmain_top.setBackground(Color.WHITE);

        txtTenDe = new InputForm("Đề thi");
        txtNguoiLam = new InputForm("Sinh viên");
        txtDiem = new InputForm("Điểm số");
        txtThoiGianVao = new InputForm("Thời gian vào thi");
        txtThoiGianLam = new InputForm("Thời gian làm bài (giây)");
        txtSoCauDung = new InputForm("Số câu đúng/sai");

        InputForm[] inputs = {txtTenDe, txtNguoiLam, txtDiem, txtThoiGianVao, txtThoiGianLam, txtSoCauDung};
        for (InputForm inp : inputs) {
            inp.setEditable(false);
            pnmain_top.add(inp);
        }

        pnmain_topWrapper.add(pnmain_top, BorderLayout.CENTER);
        pnmain.add(pnmain_topWrapper, BorderLayout.NORTH);

        pnmain_bottom = new JPanel(new BorderLayout());
        pnmain_bottom.setBorder(new EmptyBorder(5, 10, 5, 10));
        pnmain_bottom.setBackground(Color.WHITE);

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        if (isAdmin) {
            tblModel.setColumnIdentifiers(new String[]{"STT", "Nội dung câu hỏi", "Đáp án đã chọn", "Đáp án đúng", "Kết quả"});
        } else {
            tblModel.setColumnIdentifiers(new String[]{"STT", "Nội dung câu hỏi", "Đáp án đã chọn", "Kết quả"});
        }

        table = new JTable(tblModel);
        table.setFocusable(false);
        table.setRowHeight(MIN_ROW_HEIGHT);

        scrollTable = new JScrollPane(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(500);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        if (isAdmin) {
            table.getColumnModel().getColumn(3).setPreferredWidth(200); // Cột Đáp án đúng
            table.getColumnModel().getColumn(4).setPreferredWidth(100); // Cột Kết quả
        } else {
            table.getColumnModel().getColumn(3).setPreferredWidth(100); // Cột Kết quả
        }

        int indexKetQua = isAdmin ? 4 : 3;

        // ----- Renderer xuống dòng đầy đủ cho các cột nội dung dài -----
        WrapCellRenderer wrapRenderer = new WrapCellRenderer();
        table.getColumnModel().getColumn(1).setCellRenderer(wrapRenderer); // Nội dung câu hỏi
        table.getColumnModel().getColumn(2).setCellRenderer(wrapRenderer); // Đáp án đã chọn
        if (isAdmin) {
            table.getColumnModel().getColumn(3).setCellRenderer(wrapRenderer); // Đáp án đúng
        }
        wrapCols = isAdmin ? new int[]{1, 2, 3} : new int[]{1, 2};

        table.getColumnModel().getColumn(indexKetQua).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                if (value != null && "Sai".equalsIgnoreCase(value.toString())) {
                    c.setForeground(Color.RED);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                    if (isSelected) {
                        c.setForeground(table.getSelectionForeground());
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });

        // ----- Cho phép sắp xếp theo cột Kết quả: Sai -> Chưa làm -> Đúng -----
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tblModel);
        sorter.setComparator(indexKetQua, (Object o1, Object o2) ->
                Integer.compare(ketQuaRank(o1 == null ? "" : o1.toString()), ketQuaRank(o2 == null ? "" : o2.toString())));
        for (int col = 0; col < tblModel.getColumnCount(); col++) {
            // Chỉ cho sắp xếp theo STT và theo Kết quả, các cột nội dung dài không cần sort
            sorter.setSortable(col, col == 0 || col == indexKetQua);
        }
        sorter.addRowSorterListener(e -> adjustRowHeights());
        table.setRowSorter(sorter);

        // ----- Tự tính lại chiều cao dòng khi bảng đổi kích thước (VD khi phóng to dialog) -----
        table.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(ChiTietBaiThiDialog.this::adjustRowHeights);
            }
        });

        pnmain_bottom.add(scrollTable, BorderLayout.CENTER);
        pnmain.add(pnmain_bottom, BorderLayout.CENTER);

        pnmain_btn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnmain_btn.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnmain_btn.setBackground(Color.WHITE);

        btnDong = new ButtonCustom("Đóng", "danger", 14);
        btnDong.addActionListener(this);
        pnmain_btn.add(btnDong);

        pnmain.add(pnmain_btn, BorderLayout.SOUTH);

        this.add(pnmain, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    private void fillData() {
        txtTenDe.setText(dethiBUS.getById(baithi.getMade()).getTende());
        txtNguoiLam.setText(ndBUS.getHotenById(baithi.getManguoidung()));
        txtDiem.setText(String.valueOf(baithi.getDiemthi()));
        txtThoiGianVao.setText(Formater.FormatTime(baithi.getThoigianvaothi()));
        txtThoiGianLam.setText(String.valueOf(baithi.getThoigianlambai()));
        txtSoCauDung.setText(baithi.getSocaudung() + " đúng - " + baithi.getSocausai() + " sai");
    }

    public void loadDataTable() {
        tblModel.setRowCount(0);
        ArrayList<ChiTietBaiThiDTO> listCT = baithiBUS.getChiTietByMaBaiThi(baithi.getMabaithi());

        for (int i = 0; i < listCT.size(); i++) {
            ChiTietBaiThiDTO ct = listCT.get(i);
            String noiDungCH = cauHoiBUS.getById(ct.getMacauhoi()).getNoidung();
            String dapAnText = baithiBUS.getAnswerText(ct);
            String ketQua = baithiBUS.evaluateAnswer(ct);

            if (isAdmin) {
                String hienThiDapAnDung = "";
                if ("Sai".equalsIgnoreCase(ketQua) || "Chưa làm".equalsIgnoreCase(ketQua)) {
                    hienThiDapAnDung = baithiBUS.getCorrectAnswerText(ct.getMacauhoi());
                } else {
                    hienThiDapAnDung = "-";
                }

                tblModel.addRow(new Object[]{
                    i + 1, noiDungCH, dapAnText, hienThiDapAnDung, ketQua
                });
            } else {
                // Đối với sinh viên
                tblModel.addRow(new Object[]{
                    i + 1, noiDungCH, dapAnText, ketQua
                });
            }
        }

        SwingUtilities.invokeLater(this::adjustRowHeights);
    }

    /**
     * Thứ tự sắp xếp theo kết quả: Sai (0) -> Chưa làm (1) -> Đúng (2).
     */
    private int ketQuaRank(String kq) {
        if ("Sai".equalsIgnoreCase(kq)) {
            return 0;
        }
        if ("Chưa làm".equalsIgnoreCase(kq)) {
            return 1;
        }
        if ("Đúng".equalsIgnoreCase(kq)) {
            return 2;
        }
        return 3;
    }

    /**
     * Tự động tính lại chiều cao từng dòng dựa trên nội dung wrap của các cột dài,
     * để không bị ẩn dữ liệu khi nội dung dài hơn 2 dòng.
     */
    private void adjustRowHeights() {
        if (table == null || wrapCols == null || table.getRowCount() == 0) {
            return;
        }
        for (int row = 0; row < table.getRowCount(); row++) {
            int maxHeight = MIN_ROW_HEIGHT;
            for (int col : wrapCols) {
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Component comp = table.prepareRenderer(renderer, row, col);
                int prefHeight = comp.getPreferredSize().height;
                maxHeight = Math.max(maxHeight, prefHeight + 6);
            }
            if (table.getRowHeight(row) != maxHeight) {
                table.setRowHeight(row, maxHeight);
            }
        }
    }

    /**
     * Phóng to dialog ra toàn màn hình (theo vùng làm việc, trừ taskbar) hoặc
     * thu về kích thước/ vị trí ban đầu.
     */
    private void toggleMaximize() {
        if (!isMaximized) {
            savedBounds = getBounds();
            GraphicsConfiguration gc = getGraphicsConfiguration();
            Rectangle screenBounds = (gc != null)
                    ? gc.getBounds()
                    : new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            Insets screenInsets = (gc != null)
                    ? Toolkit.getDefaultToolkit().getScreenInsets(gc)
                    : new Insets(0, 0, 0, 0);
            Rectangle maxBounds = new Rectangle(
                    screenBounds.x + screenInsets.left,
                    screenBounds.y + screenInsets.top,
                    screenBounds.width - screenInsets.left - screenInsets.right,
                    screenBounds.height - screenInsets.top - screenInsets.bottom
            );
            setBounds(maxBounds);
            isMaximized = true;
            btnMaximize.setToolTipText("Thu nhỏ về kích thước ban đầu");
        } else {
            if (savedBounds != null) {
                setBounds(savedBounds);
            }
            isMaximized = false;
            btnMaximize.setToolTipText("Phóng to cửa sổ");
        }
        btnMaximize.repaint();
        SwingUtilities.invokeLater(this::adjustRowHeights);
    }

    /**
     * Tạo nút icon hình chữ nhật kiểu nút Maximize mặc định trên thanh tiêu đề Windows.
     * Khi đang phóng to sẽ đổi thành icon "khôi phục" (2 hình chữ nhật chồng nhau).
     */
    private JButton makeMaximizeButton() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(1.3f));
                g2.setColor(new Color(70, 70, 70));

                int w = getWidth(), h = getHeight();
                int size = 10;
                int x = (w - size) / 2;
                int y = (h - size) / 2;

                if (isMaximized) {
                    // Icon khôi phục: 2 hình chữ nhật chồng lên nhau
                    int off = 3;
                    g2.drawRect(x, y + off, size - off, size - off);
                    g2.setColor(isOpaque() ? getBackground() : Color.WHITE);
                    g2.fillRect(x + off, y, size - off + 1, size - off + 1);
                    g2.setColor(new Color(70, 70, 70));
                    g2.drawRect(x + off, y, size - off, size - off);
                } else {
                    // Icon phóng to: 1 hình chữ nhật
                    g2.drawRect(x, y, size, size);
                }
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(32, 28));
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setToolTipText("Phóng to cửa sổ");
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setContentAreaFilled(true);
                btn.setOpaque(true);
                btn.setBackground(new Color(225, 225, 225));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setContentAreaFilled(false);
                btn.setOpaque(false);
                btn.repaint();
            }
        });
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDong) {
            dispose();
        }
    }

    /**
     * Renderer hiển thị nội dung dạng JTextArea có xuống dòng (word-wrap),
     * giúp hiện đầy đủ nội dung dài thay vì bị cắt/ẩn.
     */
    private static class WrapCellRenderer extends JTextArea implements TableCellRenderer {

        WrapCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setEditable(false);
            setFont(new Font("Segoe UI", Font.PLAIN, 13));
            setBorder(new EmptyBorder(6, 8, 6, 8));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());

            int colWidth = table.getColumnModel().getColumn(column).getWidth();
            setSize(colWidth, Short.MAX_VALUE);

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }
}