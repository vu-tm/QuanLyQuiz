package GUI.Dialog;

import BUS.NguoiDungBUS;
import DTO.NguoiDungDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import helper.Formater;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ChiTietNguoiDungDialog extends JDialog implements ActionListener {

    private JPanel pnmain, pnmain_top, pnmain_btn;
    private InputForm txtID, txtUsername, txtHoTen, txtGioiTinh, txtNgaySinh, txtNhomQuyen, txtTrangThai;
    private ButtonCustom btnDong;

    private NguoiDungDTO nguoiDung;
    private NguoiDungBUS bus = new NguoiDungBUS();

    public ChiTietNguoiDungDialog(JFrame owner, String title, boolean modal, NguoiDungDTO nguoiDung) {
        super(owner, title, modal);
        this.nguoiDung = nguoiDung;
        initComponent();
        fillData();
        setVisible(true);
    }

    private void initComponent() {
        setSize(new Dimension(600, 400));
        setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);

        pnmain = new JPanel(new BorderLayout());
        pnmain.setBackground(Color.WHITE);

        // Top panel chứa các trường thông tin
        pnmain_top = new JPanel(new GridBagLayout());
        pnmain_top.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnmain_top.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Tạo các InputForm (chế độ chỉ đọc)
        txtID = new InputForm("ID");
        txtUsername = new InputForm("Username");
        txtHoTen = new InputForm("Họ tên");
        txtGioiTinh = new InputForm("Giới tính");
        txtNgaySinh = new InputForm("Ngày sinh");
        txtNhomQuyen = new InputForm("Nhóm quyền");
        txtTrangThai = new InputForm("Trạng thái");

        InputForm[] inputs = {txtID, txtUsername, txtHoTen, txtGioiTinh, txtNgaySinh, txtNhomQuyen, txtTrangThai};
        for (InputForm input : inputs) {
            input.setEditable(false);
        }

        // Sắp xếp theo dạng 2 cột
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3; pnmain_top.add(txtID, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7; pnmain_top.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnmain_top.add(txtHoTen, gbc);
        gbc.gridx = 1; pnmain_top.add(txtGioiTinh, gbc);

        gbc.gridx = 0; gbc.gridy = 2; pnmain_top.add(txtNgaySinh, gbc);
        gbc.gridx = 1; pnmain_top.add(txtNhomQuyen, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; pnmain_top.add(txtTrangThai, gbc);

        pnmain.add(pnmain_top, BorderLayout.CENTER);

        // Bottom panel chứa nút đóng
        pnmain_btn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnmain_btn.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnmain_btn.setBackground(Color.WHITE);

        btnDong = new ButtonCustom("Đóng", "danger", 14);
        btnDong.addActionListener(this);
        pnmain_btn.add(btnDong);

        pnmain.add(pnmain_btn, BorderLayout.SOUTH);

        add(pnmain, BorderLayout.CENTER);
    }

    private void fillData() {
        txtID.setText(String.valueOf(nguoiDung.getId()));
        txtUsername.setText(nguoiDung.getUsername());
        txtHoTen.setText(nguoiDung.getHoten());
        txtGioiTinh.setText(nguoiDung.isGioitinh() ? "Nam" : "Nữ");
        txtNgaySinh.setText(nguoiDung.getNgaysinh() != null ? Formater.FormatDate(nguoiDung.getNgaysinh()) : "N/A");
        txtNhomQuyen.setText(bus.getTenNhomQuyen(nguoiDung.getManhomquyen()));
        txtTrangThai.setText(nguoiDung.getTrangthai() == 1 ? "Hoạt động" : "Đã khóa");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDong) {
            dispose();
        }
    }
}