package GUI.Dialog;

import BUS.NguoiDungBUS;
import BUS.NhomQuyenBUS;
import DTO.NguoiDungDTO;
import DTO.NhomQuyenDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputDate;
import GUI.Component.InputForm;
import GUI.Panel.NguoiDung;
import helper.Validation;
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NguoiDungDialog extends JDialog {

    private NguoiDungBUS bus = new NguoiDungBUS();
    private NhomQuyenBUS nqBus = new NhomQuyenBUS();
    private NguoiDung parent;
    private NguoiDungDTO currentDTO;
    
    private JPanel main, bottom;
    private ButtonCustom btnAdd, btnEdit, btnExit;
    private InputForm txtUsername, txtHoten, txtMatKhau;
    private InputDate jcNgaySinh;
    private JRadioButton male, female;
    private ButtonGroup genderGroup;
    private JComboBox<String> cbxNhomQuyen, cbxTrangThai;
    private List<NhomQuyenDTO> listNhomQuyen = nqBus.getAll();

    public NguoiDungDialog(NguoiDung parent, JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        this.parent = parent;
        init(title, type);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public NguoiDungDialog(NguoiDung parent, JFrame owner, String title, boolean modal, String type, NguoiDungDTO user) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = user;
        init(title, type);
        
        if (user != null) {
            txtUsername.setText(user.getUsername());
            txtHoten.setText(user.getHoten());
            txtMatKhau.setText(user.getMatkhau());
            jcNgaySinh.setDate(user.getNgaysinh());
            
            if (user.isGioitinh()) {
                male.setSelected(true);
            } else {
                female.setSelected(true);
            }
            
            for (int i = 0; i < listNhomQuyen.size(); i++) {
                if (listNhomQuyen.get(i).getManhomquyen() == user.getManhomquyen()) {
                    cbxNhomQuyen.setSelectedIndex(i);
                    break;
                }
            }
            cbxTrangThai.setSelectedIndex(user.getTrangthai());
        }
        
        if (type.equals("view")) {
            setDisableAll();
        }

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void init(String title, String type) {
        // Giảm kích thước Dialog xuống vì các thành phần đã gọn hơn
        this.setSize(new Dimension(450, 600));
        this.setLayout(new BorderLayout(0, 0));

        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.WHITE);
        // Padding ngoài cùng nhỏ lại
        main.setBorder(new EmptyBorder(5, 15, 5, 15));

        txtUsername = new InputForm("Username");
        txtHoten = new InputForm("Họ và tên");
        txtMatKhau = new InputForm("Mật khẩu");

        male = new JRadioButton("Nam");
        female = new JRadioButton("Nữ");
        male.setBackground(Color.WHITE);
        female.setBackground(Color.WHITE);
        genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);

        JPanel pnlGender = new JPanel(new GridLayout(2, 1, 0, 0));
        pnlGender.setBackground(Color.WHITE);
        pnlGender.setBorder(new EmptyBorder(0, 10, 5, 10)); // Giảm padding
        JLabel lbGender = new JLabel("Giới tính");
        lbGender.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JPanel pnlRadio = new JPanel(new GridLayout(1, 2));
        pnlRadio.setBackground(Color.WHITE);
        pnlRadio.add(male);
        pnlRadio.add(female);
        pnlGender.add(lbGender);
        pnlGender.add(pnlRadio);

        jcNgaySinh = new InputDate("Ngày sinh");
        // Thu nhỏ padding bên trong InputDate nếu có thể thông qua component

        // Nhóm quyền
        JPanel pnlNQ = new JPanel(new GridLayout(2, 1, 0, 5));
        pnlNQ.setBackground(Color.WHITE);
        pnlNQ.setBorder(new EmptyBorder(5, 10, 5, 10));
        JLabel lbNQ = new JLabel("Nhóm quyền");
        lbNQ.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlNQ.add(lbNQ);
        cbxNhomQuyen = new JComboBox<>();
        for (NhomQuyenDTO nq : listNhomQuyen) {
            cbxNhomQuyen.addItem(nq.getTennhomquyen());
        }
        cbxNhomQuyen.setPreferredSize(new Dimension(0, 35)); // Tăng chiều cao Dropdown
        pnlNQ.add(cbxNhomQuyen);

        // Trạng thái
        JPanel pnlTT = new JPanel(new GridLayout(2, 1, 0, 5));
        pnlTT.setBackground(Color.WHITE);
        pnlTT.setBorder(new EmptyBorder(5, 10, 5, 10));
        JLabel lbTT = new JLabel("Trạng thái");
        lbTT.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlTT.add(lbTT);
        cbxTrangThai = new JComboBox<>(new String[]{"Đã khóa", "Hoạt động"});
        cbxTrangThai.setSelectedIndex(1);
        cbxTrangThai.setPreferredSize(new Dimension(0, 35)); // Tăng chiều cao Dropdown
        pnlTT.add(cbxTrangThai);

        main.add(txtUsername);
        main.add(txtHoten);
        main.add(txtMatKhau);
        main.add(pnlGender);
        main.add(jcNgaySinh);
        main.add(pnlNQ);
        main.add(pnlTT);

        bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setBackground(Color.WHITE);

        btnAdd = new ButtonCustom("Thêm người dùng", "success", 14);
        btnEdit = new ButtonCustom("Lưu thông tin", "success", 14);
        btnExit = new ButtonCustom("Hủy bỏ", "danger", 14);

        btnExit.addActionListener(e -> dispose());

        btnAdd.addActionListener(e -> {
            try {
                if (validateInput()) {
                    if (bus.checkExistUsername(txtUsername.getText().trim())) {
                        JOptionPane.showMessageDialog(this, "Username đã tồn tại!");
                    } else {
                        actionSave("create");
                    }
                }
            } catch (ParseException ex) {
                Logger.getLogger(NguoiDungDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnEdit.addActionListener(e -> {
            try {
                if (validateInput()) {
                    actionSave("update");
                }
            } catch (ParseException ex) {
                Logger.getLogger(NguoiDungDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        switch (type) {
            case "create" -> bottom.add(btnAdd);
            case "update" -> {
                txtUsername.setDisable();
                bottom.add(btnEdit);
            }
            case "view" -> { }
        }
        bottom.add(btnExit);

        this.add(main, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);
    }

    private void actionSave(String type) throws ParseException {
        String username = txtUsername.getText().trim();
        String hoten = txtHoten.getText().trim();
        String matkhau = txtMatKhau.getText();
        boolean gioiTinh = male.isSelected();
        
        java.util.Date date = jcNgaySinh.getDate();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        
        int manhomquyen = listNhomQuyen.get(cbxNhomQuyen.getSelectedIndex()).getManhomquyen();
        int trangthai = cbxTrangThai.getSelectedIndex();

        if (type.equals("create")) {
            int id = bus.getNextId();
            NguoiDungDTO newUser = new NguoiDungDTO(id, username, hoten, gioiTinh, sqlDate, matkhau, trangthai, manhomquyen);
            if (bus.insert(newUser)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                parent.loadDataTable((ArrayList<NguoiDungDTO>) bus.getAll());
                dispose();
            }
        } else {
            NguoiDungDTO updatedUser = new NguoiDungDTO(currentDTO.getId(), username, hoten, gioiTinh, sqlDate, matkhau, trangthai, manhomquyen);
            if (bus.update(updatedUser)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parent.loadDataTable((ArrayList<NguoiDungDTO>) bus.getAll());
                dispose();
            }
        }
    }

    private boolean validateInput() throws ParseException {
        if (Validation.isEmpty(txtUsername.getText())) {
            JOptionPane.showMessageDialog(this, "Username không được để trống!");
            return false;
        }
        if (Validation.isEmpty(txtHoten.getText())) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!");
            return false;
        }
        if (txtHoten.getText().length() < 6) {
            JOptionPane.showMessageDialog(this, "Họ tên phải ít nhất 6 ký tự!");
            return false;
        }
        if (Validation.isEmpty(txtMatKhau.getText())) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống!");
            return false;
        }
        if (jcNgaySinh.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sinh!");
            return false;
        }
        if (!male.isSelected() && !female.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính!");
            return false;
        }
        return true;
    }

    private void setDisableAll() {
        txtUsername.setDisable();
        txtHoten.setDisable();
        txtMatKhau.setDisable();
        jcNgaySinh.setDisable();
        cbxNhomQuyen.setEnabled(false);
        cbxTrangThai.setEnabled(false);
        male.setEnabled(false);
        female.setEnabled(false);
    }
}