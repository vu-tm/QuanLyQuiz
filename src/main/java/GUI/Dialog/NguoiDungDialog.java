package GUI.Dialog;

import BUS.NguoiDungBUS;
import BUS.NhomQuyenBUS;
import DTO.NguoiDungDTO;
import DTO.NhomQuyenDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Panel.NguoiDung;
import helper.Validation;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NguoiDungDialog extends JDialog {

    private InputForm txtUsername, txtHoten, txtNgaySinh, txtMatKhau;
    private JComboBox<String> cbxGioiTinh, cbxNhomQuyen, cbxTrangThai;
    private ButtonCustom btnLuu, btnHuy;
    private NguoiDungBUS bus = new NguoiDungBUS();
    private NhomQuyenBUS nqBus = new NhomQuyenBUS();
    private NguoiDung parent;
    private NguoiDungDTO currentDTO;
    private JPanel pnlMain, pnlButtons;
    private List<NhomQuyenDTO> listNhomQuyen = nqBus.getAll();

    public NguoiDungDialog(NguoiDung parent, JFrame owner, String title, boolean modal, String type, NguoiDungDTO user) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = user;
        init(type);
    }

    private void init(String type) {
        this.setSize(500, 600);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        initPnlMain(type);
        initPnlButtons(type);

        JScrollPane scrollPane = new JScrollPane(pnlMain);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);

        if (type.equals("view")) {
            disableAll();
        }

        this.setVisible(true);
    }

    private void initPnlMain(String type) {
        pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlMain.setBackground(Color.WHITE);

        txtUsername = new InputForm("Username");
        txtHoten = new InputForm("Họ và tên");
        txtNgaySinh = new InputForm("Ngày sinh (yyyy-MM-dd)");
        txtMatKhau = new InputForm("Mật khẩu");

        // Giới tính
        JPanel pnlGioiTinh = new JPanel(new BorderLayout());
        pnlGioiTinh.setBackground(Color.WHITE);
        pnlGioiTinh.add(new JLabel("Giới tính"), BorderLayout.NORTH);
        cbxGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbxGioiTinh.setPreferredSize(new Dimension(0, 40));
        pnlGioiTinh.add(cbxGioiTinh, BorderLayout.CENTER);

        // Nhóm quyền
        JPanel pnlNhomQuyen = new JPanel(new BorderLayout());
        pnlNhomQuyen.setBackground(Color.WHITE);
        pnlNhomQuyen.add(new JLabel("Nhóm quyền"), BorderLayout.NORTH);
        cbxNhomQuyen = new JComboBox<>();
        for (NhomQuyenDTO nq : listNhomQuyen) {
            cbxNhomQuyen.addItem(nq.getTennhomquyen());
        }
        cbxNhomQuyen.setPreferredSize(new Dimension(0, 40));
        pnlNhomQuyen.add(cbxNhomQuyen, BorderLayout.CENTER);

        // Trạng thái
        JPanel pnlTrangThai = new JPanel(new BorderLayout());
        pnlTrangThai.setBackground(Color.WHITE);
        pnlTrangThai.add(new JLabel("Trạng thái"), BorderLayout.NORTH);
        cbxTrangThai = new JComboBox<>(new String[]{"Đã khóa", "Hoạt động"});
        cbxTrangThai.setSelectedIndex(1);
        cbxTrangThai.setPreferredSize(new Dimension(0, 40));
        pnlTrangThai.add(cbxTrangThai, BorderLayout.CENTER);

        if (currentDTO != null) {
            txtUsername.setText(currentDTO.getUsername());
            txtUsername.setEditable(false);
            txtHoten.setText(currentDTO.getHoten());
            txtNgaySinh.setText(currentDTO.getNgaysinh().toString());
            txtMatKhau.setText(currentDTO.getMatkhau());
            cbxGioiTinh.setSelectedIndex(currentDTO.isGioitinh() ? 0 : 1);
            cbxTrangThai.setSelectedIndex(currentDTO.getTrangthai());
            // Set nhóm quyền
            for (int i = 0; i < listNhomQuyen.size(); i++) {
                if (listNhomQuyen.get(i).getManhomquyen() == currentDTO.getManhomquyen()) {
                    cbxNhomQuyen.setSelectedIndex(i);
                    break;
                }
            }
        }

        pnlMain.add(txtUsername);
        pnlMain.add(Box.createVerticalStrut(10));
        pnlMain.add(txtHoten);
        pnlMain.add(Box.createVerticalStrut(10));
        pnlMain.add(txtNgaySinh);
        pnlMain.add(Box.createVerticalStrut(10));
        pnlMain.add(txtMatKhau);
        pnlMain.add(Box.createVerticalStrut(10));
        pnlMain.add(pnlGioiTinh);
        pnlMain.add(Box.createVerticalStrut(10));
        pnlMain.add(pnlNhomQuyen);
        pnlMain.add(Box.createVerticalStrut(10));
        pnlMain.add(pnlTrangThai);
    }

    private void initPnlButtons(String type) {
        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnLuu = new ButtonCustom(type.equals("create") ? "Thêm mới" : "Lưu thay đổi", "success", 14);
        btnHuy = new ButtonCustom("Huỷ bỏ", "danger", 14);

        btnLuu.addActionListener(e -> {
            if (validateInput()) {
                luuDuLieu(type);
            }
        });

        btnHuy.addActionListener(e -> dispose());

        if (!type.equals("view")) {
            pnlButtons.add(btnLuu);
        }
        pnlButtons.add(btnHuy);
    }

    private void luuDuLieu(String type) {
        NguoiDungDTO user = new NguoiDungDTO();
        user.setUsername(txtUsername.getText().trim());
        user.setHoten(txtHoten.getText().trim());
        user.setNgaysinh(Date.valueOf(txtNgaySinh.getText().trim()));
        user.setMatkhau(txtMatKhau.getText());
        user.setGioitinh(cbxGioiTinh.getSelectedIndex() == 0);
        user.setTrangthai(cbxTrangThai.getSelectedIndex());
        user.setManhomquyen(listNhomQuyen.get(cbxNhomQuyen.getSelectedIndex()).getManhomquyen());

        if (type.equals("create")) {
            user.setId(bus.getNextId());
            if (bus.insert(user)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                parent.loadDataTable((java.util.ArrayList<NguoiDungDTO>) bus.getAll());
                dispose();
            }
        } else {
            user.setId(currentDTO.getId());
            if (bus.update(user)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parent.loadDataTable((java.util.ArrayList<NguoiDungDTO>) bus.getAll());
                dispose();
            }
        }
    }

    private boolean validateInput() {
        if (Validation.isEmpty(txtUsername.getText()) || Validation.isEmpty(txtHoten.getText())) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Date.valueOf(txtNgaySinh.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ (yyyy-MM-dd)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void disableAll() {
        txtUsername.setEditable(false);
        txtHoten.setEditable(false);
        txtNgaySinh.setEditable(false);
        txtMatKhau.setEditable(false);
        cbxGioiTinh.setEnabled(false);
        cbxNhomQuyen.setEnabled(false);
        cbxTrangThai.setEnabled(false);
    }
}
