package GUI.Dialog;

import BUS.NhomQuyenBUS;
import BUS.QuyenBUS;
import DTO.NhomQuyenDTO;
import DTO.QuyenDTO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ChiTietNhomQuyenDialog extends JDialog {
    private JTextField txtMaNhom;
    private JTextField txtTenNhom;
    private JCheckBox[][] quyenCheckBoxes;
    private JCheckBox chkThamGiaThi;
    private JCheckBox chkThamGiaHocPhan;

    private NhomQuyenBUS nhomQuyenBUS;
    private QuyenBUS quyenBUS = new QuyenBUS();
    private NhomQuyenDTO currentDTO;

    private final String[] doiTuong = {
        "người dùng", "môn học", "câu hỏi", "đề thi",
        "kì thi", "lớp học", "bài thi", "nhóm quyền"
    };
    private final String[] hanhDong = {"Xem", "Thêm mới", "Cập nhật", "Xoá"};

    private int[][] maQuyenMapping;
    private int maQuyenThamGiaThi = 0;
    private int maQuyenThamGiaHocPhan = 0;

    public ChiTietNhomQuyenDialog(JFrame parent, String title, NhomQuyenDTO dto, NhomQuyenBUS bus) {
        super(parent, title, true);
        this.currentDTO = dto;
        this.nhomQuyenBUS = bus;
        try {
            initComponent();
            loadQuyenMapping();
            hienThiDuLieu();
            pack();
            setLocationRelativeTo(parent);
            setResizable(false);
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Lỗi khởi tạo dialog: " + e.getMessage());
        }
    }

    private void initComponent() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel thông tin
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(new TitledBorder("Thông tin nhóm quyền"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Mã nhóm quyền:"), gbc);
        gbc.gridx = 1;
        txtMaNhom = new JTextField(15);
        txtMaNhom.setEditable(false);
        txtMaNhom.setBackground(new Color(240, 240, 240));
        txtMaNhom.setFocusable(false); 
        infoPanel.add(txtMaNhom, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Tên nhóm quyền:"), gbc);
        gbc.gridx = 1;
        txtTenNhom = new JTextField(15);
        txtTenNhom.setEditable(false);
        txtTenNhom.setBackground(new Color(240, 240, 240));
        txtTenNhom.setFocusable(false); 
        infoPanel.add(txtTenNhom, gbc);

        add(infoPanel, BorderLayout.NORTH);

        // Panel bảng quyền
        JPanel quyenPanel = new JPanel(new BorderLayout());
        quyenPanel.setBorder(new TitledBorder("Chi tiết quyền"));

        JPanel bangQuyen = new JPanel(new GridLayout(doiTuong.length + 1, hanhDong.length + 1, 2, 2));
        bangQuyen.setBackground(Color.WHITE);

        // Header
        bangQuyen.add(new JLabel(""));
        for (String hd : hanhDong) {
            JLabel lbl = new JLabel(hd, SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            bangQuyen.add(lbl);
        }

        quyenCheckBoxes = new JCheckBox[doiTuong.length][hanhDong.length];
        for (int i = 0; i < doiTuong.length; i++) {
            JLabel lblDoiTuong = new JLabel(doiTuong[i]);
            lblDoiTuong.setFont(new Font("Arial", Font.PLAIN, 12));
            bangQuyen.add(lblDoiTuong);
            for (int j = 0; j < hanhDong.length; j++) {
                JCheckBox chk = new JCheckBox();
                chk.setHorizontalAlignment(SwingConstants.CENTER);
                chk.setEnabled(false); // Chỉ xem, không tương tác
                chk.setFocusable(false); // Không nhận focus
                quyenCheckBoxes[i][j] = chk;
                bangQuyen.add(chk);
            }
        }
        quyenPanel.add(bangQuyen, BorderLayout.CENTER);

        // Checkbox đặc biệt
        JPanel panelRieng = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panelRieng.setBorder(new TitledBorder("Quyền đặc biệt"));
        chkThamGiaThi = new JCheckBox("Tham gia thi");
        chkThamGiaThi.setEnabled(false);
        chkThamGiaThi.setFocusable(false);
        chkThamGiaHocPhan = new JCheckBox("Tham gia học phần");
        chkThamGiaHocPhan.setEnabled(false);
        chkThamGiaHocPhan.setFocusable(false);
        panelRieng.add(chkThamGiaThi);
        panelRieng.add(chkThamGiaHocPhan);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(quyenPanel, BorderLayout.CENTER);
        centerPanel.add(panelRieng, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Panel nút - chỉ có nút Đóng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDong = new JButton("Đóng");
        btnDong.setBackground(new Color(0, 120, 215));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.addActionListener(e -> dispose());
        buttonPanel.add(btnDong);
        add(buttonPanel, BorderLayout.SOUTH);

        // Focus vào nút Đóng khi dialog hiện ra
        SwingUtilities.invokeLater(() -> btnDong.requestFocus());
    }

    private void loadQuyenMapping() {
        List<QuyenDTO> dsQuyen = quyenBUS.getAll();
        maQuyenMapping = new int[doiTuong.length][hanhDong.length];

        for (int i = 0; i < doiTuong.length; i++) {
            for (int j = 0; j < hanhDong.length; j++) {
                maQuyenMapping[i][j] = 0;
            }
        }

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
            if (doiTuong[i].equalsIgnoreCase(ten.trim())) {
                return i;
            }
        }
        return -1;
    }

    private int timIndexHanhDong(String hanhDong) {
        if (hanhDong == null) return -1;
        for (int j = 0; j < this.hanhDong.length; j++) {
            if (this.hanhDong[j].equalsIgnoreCase(hanhDong.trim())) {
                return j;
            }
        }
        return -1;
    }

    private void hienThiDuLieu() {
        txtMaNhom.setText(String.valueOf(currentDTO.getManhomquyen()));
        txtTenNhom.setText(currentDTO.getTennhomquyen());

        List<Integer> dsQuyen = nhomQuyenBUS.getQuyenByNhom(currentDTO.getManhomquyen());

        // Reset tất cả checkbox
        for (int i = 0; i < doiTuong.length; i++) {
            for (int j = 0; j < hanhDong.length; j++) {
                quyenCheckBoxes[i][j].setSelected(false);
            }
        }
        chkThamGiaThi.setSelected(false);
        chkThamGiaHocPhan.setSelected(false);

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
                if (ma == maQuyenThamGiaThi) {
                    chkThamGiaThi.setSelected(true);
                } else if (ma == maQuyenThamGiaHocPhan) {
                    chkThamGiaHocPhan.setSelected(true);
                }
            }
        }
    }
}