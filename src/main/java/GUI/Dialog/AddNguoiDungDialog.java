package GUI.Dialog;

import BUS.NguoiDungBUS;
import BUS.NhomQuyenBUS;
import DTO.NguoiDungDTO;
import DTO.NhomQuyenDTO;
import helper.IconHelper;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class AddNguoiDungDialog extends JDialog {
    private JTextField txtId, txtUsername, txtHoten, txtNgaySinh;
    private JRadioButton rdNam, rdNu;
    private ButtonGroup groupGioiTinh;
    private JComboBox<NhomQuyenDTO> cboNhomQuyen;
    private JRadioButton rdHoatDong, rdKhoa;
    private ButtonGroup groupTrangThai;
    private JButton btnSave, btnCancel;

    private NguoiDungBUS nguoiDungBUS = new NguoiDungBUS();
    private NhomQuyenBUS nhomQuyenBUS = new NhomQuyenBUS();
    private List<NhomQuyenDTO> listNhomQuyen;

    private NguoiDungDTO user; // null nếu thêm mới, khác null nếu sửa
    private boolean isEditMode = false;
    private Runnable onSaveCallback;

    public AddNguoiDungDialog(JFrame parent, String title, NguoiDungDTO user, Runnable onSaveCallback) {
        super(parent, title, true);
        this.user = user;
        this.isEditMode = (user != null);
        this.onSaveCallback = onSaveCallback;
        initComponent();
        loadNhomQuyen();
        if (isEditMode) {
            loadDataToForm();
        }
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponent() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel thông tin
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(new TitledBorder("Thông tin người dùng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã số
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Mã số:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(15);
        if (isEditMode) txtId.setEditable(false);
        infoPanel.add(txtId, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        infoPanel.add(txtUsername, gbc);

        // Họ tên
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1;
        txtHoten = new JTextField(15);
        infoPanel.add(txtHoten, gbc);

        // Giới tính
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        JPanel pnGioiTinh = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnGioiTinh.setBackground(Color.WHITE);
        rdNam = new JRadioButton("Nam", true);
        rdNu = new JRadioButton("Nữ");
        groupGioiTinh = new ButtonGroup();
        groupGioiTinh.add(rdNam);
        groupGioiTinh.add(rdNu);
        pnGioiTinh.add(rdNam);
        pnGioiTinh.add(rdNu);
        infoPanel.add(pnGioiTinh, gbc);

        // Ngày sinh
        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Ngày sinh (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        txtNgaySinh = new JTextField(15);
        infoPanel.add(txtNgaySinh, gbc);

        // Nhóm quyền
        gbc.gridx = 0; gbc.gridy = 5;
        infoPanel.add(new JLabel("Nhóm quyền:"), gbc);
        gbc.gridx = 1;
        cboNhomQuyen = new JComboBox<>();
        infoPanel.add(cboNhomQuyen, gbc);

        // Trạng thái
        gbc.gridx = 0; gbc.gridy = 6;
        infoPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1;
        JPanel pnTrangThai = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnTrangThai.setBackground(Color.WHITE);
        rdHoatDong = new JRadioButton("Hoạt động", true);
        rdKhoa = new JRadioButton("Đã khóa");
        groupTrangThai = new ButtonGroup();
        groupTrangThai.add(rdHoatDong);
        groupTrangThai.add(rdKhoa);
        pnTrangThai.add(rdHoatDong);
        pnTrangThai.add(rdKhoa);
        infoPanel.add(pnTrangThai, gbc);

        add(infoPanel, BorderLayout.NORTH);

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");
        btnSave.setBackground(new Color(0, 120, 215));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);


        btnSave.addActionListener(e -> saveUser());
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadNhomQuyen() {
        listNhomQuyen = nhomQuyenBUS.getAll();
        DefaultComboBoxModel<NhomQuyenDTO> model = new DefaultComboBoxModel<>();
        for (NhomQuyenDTO nq : listNhomQuyen) {
            model.addElement(nq);
        }
        cboNhomQuyen.setModel(model);
        cboNhomQuyen.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof NhomQuyenDTO) {
                    setText(((NhomQuyenDTO) value).getTennhomquyen());
                }
                return this;
            }
        });
    }

    private void loadDataToForm() {
        txtId.setText(user.getId());
        txtUsername.setText(user.getUsername());
        txtHoten.setText(user.getHoten());
        rdNam.setSelected(user.isGioitinh());
        rdNu.setSelected(!user.isGioitinh());
        if (user.getNgaysinh() != null) {
            txtNgaySinh.setText(user.getNgaysinh().toString());
        }
        for (int i = 0; i < listNhomQuyen.size(); i++) {
            if (listNhomQuyen.get(i).getManhomquyen() == user.getManhomquyen()) {
                cboNhomQuyen.setSelectedIndex(i);
                break;
            }
        }
        rdHoatDong.setSelected(user.getTrangthai() == 1);
        rdKhoa.setSelected(user.getTrangthai() == 0);
    }

    private void saveUser() {
        String id = txtId.getText().trim();
        String username = txtUsername.getText().trim();
        String hoten = txtHoten.getText().trim();
        String ngaySinhStr = txtNgaySinh.getText().trim();

        if (id.isEmpty() || username.isEmpty() || hoten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean gioitinh = rdNam.isSelected();
        Date ngaySinh = null;
        if (!ngaySinhStr.isEmpty()) {
            try {
                ngaySinh = Date.valueOf(ngaySinhStr);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng (yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        NhomQuyenDTO selectedNhom = (NhomQuyenDTO) cboNhomQuyen.getSelectedItem();
        if (selectedNhom == null) {
            JOptionPane.showMessageDialog(this, "Chưa chọn nhóm quyền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int trangthai = rdHoatDong.isSelected() ? 1 : 0;

        NguoiDungDTO dto = new NguoiDungDTO(id, username, hoten, gioitinh, ngaySinh, null, trangthai, selectedNhom.getManhomquyen());

        boolean success;
        if (isEditMode) {
            success = nguoiDungBUS.update(dto);
        } else {
            // Kiểm tra tồn tại khi thêm mới
            if (nguoiDungBUS.checkExistId(id)) {
                JOptionPane.showMessageDialog(this, "Mã số đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nguoiDungBUS.checkExistUsername(username)) {
                JOptionPane.showMessageDialog(this, "Username đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            success = nguoiDungBUS.insert(dto);
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Lưu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            if (onSaveCallback != null) onSaveCallback.run();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lưu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}