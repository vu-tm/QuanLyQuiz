package GUI.Dialog;

import BUS.NhomQuyenBUS;
import BUS.QuyenBUS;
import DTO.NhomQuyenDTO;
import DTO.QuyenDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class AddNhomQuyenDialog extends JDialog {
    private InputForm txtMaNhom;
    private InputForm txtTenNhom;
    private JCheckBox[][] quyenCheckBoxes;
    private JCheckBox chkThamGiaThi;
    private JCheckBox chkThamGiaHocPhan;
    private ButtonCustom btnLuu, btnHuy;

    private NhomQuyenBUS nhomQuyenBUS;
    private QuyenBUS quyenBUS = new QuyenBUS();
    private NhomQuyenDTO currentDTO;
    private Runnable onSaveCallback;

    private final String[] doiTuong = {
        "Người dùng", "Môn học", "Câu hỏi", "Đề thi",
        "Kì thi", "Lớp học", "Bài thi", "Nhóm quyền"
    };
    private final String[] hanhDong = {"Xem", "Thêm mới", "Cập nhật", "Xoá"};

    private int[][] maQuyenMapping;
    private int maQuyenThamGiaThi = 0;
    private int maQuyenThamGiaHocPhan = 0;

    private static final int FIELD_HEIGHT = 58;   
    private static final int LABEL_WIDTH  = 90;  
    private static final int FIELD_WIDTH  = 240; 

    public AddNhomQuyenDialog(JFrame parent, String title, NhomQuyenDTO dto,
                               NhomQuyenBUS bus, Runnable callback) {
        super(parent, title, true);
        this.currentDTO   = dto;
        this.nhomQuyenBUS = bus;
        this.onSaveCallback = callback;
        try {
            initComponent();
            loadQuyenMapping();
            setData();
            pack();
            setMinimumSize(getSize());
            setLocationRelativeTo(parent);
            setResizable(false);
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Lỗi khởi tạo dialog: " + e.getMessage());
        }
    }

    private void initComponent() {
        JPanel root = (JPanel) getContentPane();
        root.setLayout(new BorderLayout(0, 8));
        root.setBorder(new EmptyBorder(12, 14, 10, 14));
        root.setBackground(Color.WHITE);

        root.add(buildInfoPanel(),   BorderLayout.NORTH);
        root.add(buildCenterPanel(), BorderLayout.CENTER);
        root.add(buildButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(titledBorder("Thông tin nhóm quyền"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.insets    = new Insets(8, 10, 8, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lblMa = new JLabel("Mã nhóm:");
        lblMa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMa.setPreferredSize(new Dimension(LABEL_WIDTH, FIELD_HEIGHT));
        panel.add(lblMa, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMaNhom = new InputForm("");
        txtMaNhom.setEditable(false);
        txtMaNhom.setFocusable(false);
        txtMaNhom.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        txtMaNhom.setMinimumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        panel.add(txtMaNhom, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lblTen = new JLabel("Tên nhóm:");
        lblTen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTen.setPreferredSize(new Dimension(LABEL_WIDTH, FIELD_HEIGHT));
        panel.add(lblTen, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTenNhom = new InputForm("");
        txtTenNhom.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        txtTenNhom.setMinimumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        panel.add(txtTenNhom, gbc);

        return panel;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(0, 6));
        center.setBackground(Color.WHITE);
        center.add(buildQuyenTable(),   BorderLayout.CENTER);
        center.add(buildSpecialQuyen(), BorderLayout.SOUTH);
        return center;
    }

    private JPanel buildQuyenTable() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(titledBorder("Chi tiết quyền"));

        int cols = hanhDong.length + 1;
        int rows = doiTuong.length + 1;

        JPanel grid = new JPanel(new GridLayout(rows, cols, 0, 0));
        grid.setBackground(Color.WHITE);

        Color headerBg = new Color(245, 247, 250);
        Color rowEven  = Color.WHITE;
        Color rowOdd   = new Color(250, 251, 253);
        Color border   = new Color(220, 222, 226);

        JLabel corner = styledCell("", true, headerBg, border);
        corner.setPreferredSize(new Dimension(110, 28));
        grid.add(corner);

        for (String hd : hanhDong) {
            grid.add(styledCell(hd, true, headerBg, border));
        }

        quyenCheckBoxes = new JCheckBox[doiTuong.length][hanhDong.length];

        for (int i = 0; i < doiTuong.length; i++) {
            Color rowBg = (i % 2 == 0) ? rowEven : rowOdd;

            JLabel lblDT = styledCell(doiTuong[i], false, rowBg, border);
            lblDT.setPreferredSize(new Dimension(110, 26));
            grid.add(lblDT);

            for (int j = 0; j < hanhDong.length; j++) {
                JCheckBox chk = new JCheckBox();
                chk.setHorizontalAlignment(SwingConstants.CENTER);
                chk.setBackground(rowBg);
                chk.setOpaque(true);

                // Viền ô
                chk.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, border));
                quyenCheckBoxes[i][j] = chk;
                grid.add(chk);
            }
        }

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    /** Cell dùng chung cho header và label hàng */
    private JLabel styledCell(String text, boolean bold, Color bg, Color borderColor) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 12));
        lbl.setBackground(bg);
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, borderColor));
        lbl.setPreferredSize(new Dimension(72, bold ? 28 : 26));
        return lbl;
    }

    /** Quyền đặc biệt */
    private JPanel buildSpecialQuyen() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        panel.setBackground(Color.WHITE);
        panel.setBorder(titledBorder("Quyền đặc biệt"));

        chkThamGiaThi     = new JCheckBox("Tham gia thi");
        chkThamGiaHocPhan = new JCheckBox("Tham gia học phần");

        styleCheckBox(chkThamGiaThi);
        styleCheckBox(chkThamGiaHocPhan);

        panel.add(chkThamGiaThi);
        panel.add(chkThamGiaHocPhan);
        return panel;
    }

    private void styleCheckBox(JCheckBox chk) {
        chk.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chk.setBackground(Color.WHITE);
    }

    // ── Panel nút bấm ────────────────────────────────────────────────────────
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 6));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 222, 226)));

        btnLuu = new ButtonCustom("Lưu",  "success", 14);
        btnHuy = new ButtonCustom("Hủy",  "danger",  14);

        btnLuu.setPreferredSize(new Dimension(110, 34));
        btnHuy.setPreferredSize(new Dimension(110, 34));

        btnLuu.addActionListener(e -> luu());
        btnHuy.addActionListener(e -> dispose());

        panel.add(btnLuu);
        panel.add(btnHuy);
        return panel;
    }

    // ── Helper: TitledBorder ─────────────────────────────────────────────────
    private TitledBorder titledBorder(String title) {
        return BorderFactory.createTitledBorder(
                new LineBorder(new Color(210, 213, 218), 1, true),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(80, 90, 110));
    }

    // ── Logic dữ liệu (giữ nguyên) ───────────────────────────────────────────
    private void loadQuyenMapping() {
        List<QuyenDTO> dsQuyen = quyenBUS.getAll();
        maQuyenMapping = new int[doiTuong.length][hanhDong.length];

        for (QuyenDTO q : dsQuyen) {
            int i = timIndexDoiTuong(q.getTendoituong());
            int j = timIndexHanhDong(q.getHanhdong());
            if (i != -1 && j != -1) {
                maQuyenMapping[i][j] = q.getMaquyen();
            } else {
                if ("Tham gia thi".equalsIgnoreCase(q.getChucnang())) {
                    maQuyenThamGiaThi = q.getMaquyen();
                } else if ("Tham gia học phần".equalsIgnoreCase(q.getChucnang())) {
                    maQuyenThamGiaHocPhan = q.getMaquyen();
                }
            }
        }
    }

    private int timIndexDoiTuong(String ten) {
        if (ten == null) return -1;
        for (int i = 0; i < doiTuong.length; i++) {
            if (doiTuong[i].equalsIgnoreCase(ten.trim())) return i;
        }
        return -1;
    }

    private int timIndexHanhDong(String hd) {
        if (hd == null) return -1;
        for (int j = 0; j < hanhDong.length; j++) {
            if (hanhDong[j].equalsIgnoreCase(hd.trim())) return j;
        }
        return -1;
    }

    private void setData() {
        if (currentDTO != null) {
            txtMaNhom.setText(String.valueOf(currentDTO.getManhomquyen()));
            txtTenNhom.setText(currentDTO.getTennhomquyen());
            txtTenNhom.setEditable(true);

            List<Integer> dsQuyen = nhomQuyenBUS.getQuyenByNhom(currentDTO.getManhomquyen());
            resetCheckBoxes();
            for (int ma : dsQuyen) {
                boolean found = false;
                for (int i = 0; i < doiTuong.length && !found; i++) {
                    for (int j = 0; j < hanhDong.length; j++) {
                        if (maQuyenMapping[i][j] == ma) {
                            quyenCheckBoxes[i][j].setSelected(true);
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    if (ma == maQuyenThamGiaThi)     chkThamGiaThi.setSelected(true);
                    else if (ma == maQuyenThamGiaHocPhan) chkThamGiaHocPhan.setSelected(true);
                }
            }
        } else {
            txtMaNhom.setText(String.valueOf(nhomQuyenBUS.getNextId()));
            txtTenNhom.setText("");
            resetCheckBoxes();
            txtTenNhom.setEditable(true);
        }
        SwingUtilities.invokeLater(() -> txtTenNhom.requestFocus());
    }

    private void resetCheckBoxes() {
        for (int i = 0; i < doiTuong.length; i++)
            for (int j = 0; j < hanhDong.length; j++)
                quyenCheckBoxes[i][j].setSelected(false);
        chkThamGiaThi.setSelected(false);
        chkThamGiaHocPhan.setSelected(false);
    }

    private void luu() {
        String ten = txtTenNhom.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhóm quyền!");
            txtTenNhom.requestFocus();
            return;
        }

        List<Integer> dsQuyenChon = layDanhSachQuyenChon();

        if (currentDTO == null) {
            NhomQuyenDTO nq = new NhomQuyenDTO();
            nq.setTennhomquyen(ten);
            nq.setTrangthai(1);
            int maMoi = nhomQuyenBUS.insert(nq);
            if (maMoi > 0) {
                if (nhomQuyenBUS.insertChiTietQuyen(maMoi, dsQuyenChon)) {
                    JOptionPane.showMessageDialog(this, "Thêm nhóm quyền thành công!");
                    if (onSaveCallback != null) onSaveCallback.run();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm quyền thất bại! Vui lòng thử lại.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhóm quyền thất bại!");
            }
        } else {
            currentDTO.setTennhomquyen(ten);
            if (nhomQuyenBUS.update(currentDTO)) {
                nhomQuyenBUS.deleteChiTietQuyen(currentDTO.getManhomquyen());
                if (nhomQuyenBUS.insertChiTietQuyen(currentDTO.getManhomquyen(), dsQuyenChon)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật nhóm quyền thành công!");
                    if (onSaveCallback != null) onSaveCallback.run();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật quyền thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật nhóm quyền thất bại!");
            }
        }
    }

    private List<Integer> layDanhSachQuyenChon() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < doiTuong.length; i++)
            for (int j = 0; j < hanhDong.length; j++)
                if (quyenCheckBoxes[i][j].isSelected() && maQuyenMapping[i][j] != 0)
                    list.add(maQuyenMapping[i][j]);
        if (chkThamGiaThi.isSelected()     && maQuyenThamGiaThi     != 0) list.add(maQuyenThamGiaThi);
        if (chkThamGiaHocPhan.isSelected() && maQuyenThamGiaHocPhan != 0) list.add(maQuyenThamGiaHocPhan);
        return list;
    }
}