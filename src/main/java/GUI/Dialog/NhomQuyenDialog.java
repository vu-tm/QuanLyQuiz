package GUI.Dialog;

import BUS.NhomQuyenBUS;
import DAO.DanhMucChucNangDAO;
import DAO.NhomQuyenDAO;
import DTO.ChiTietQuyenDTO;
import DTO.DanhMucChucNangDTO;
import DTO.NhomQuyenDTO;
import GUI.Component.ButtonCustom;
import GUI.Panel.NhomQuyen;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NhomQuyenDialog extends JDialog implements ActionListener {

    private JLabel lblTennhomquyen;
    private JTextField txtTennhomquyen;
    private JPanel jpTop, jpLeft, jpCen, jpBottom;
    private JCheckBox[][] listCheckBox;
    private ButtonCustom btnAdd, btnUpdate, btnClose;
    private NhomQuyen jp;
    private int sizeDmCn, sizeHanhdong;
    private ArrayList<DanhMucChucNangDTO> dmcn;
    private String[] mahanhdong = {"view", "create", "update", "delete"};
    private ArrayList<ChiTietQuyenDTO> ctQuyen;
    private NhomQuyenDTO nqDTO;
    private NhomQuyenBUS nqBUS;
    private String type;

    public NhomQuyenDialog(NhomQuyen jp, JFrame owner, String title, boolean modal, String type, NhomQuyenDTO nqDTO) {
        super(owner, title, modal);
        this.jp = jp;
        this.type = type;
        this.nqDTO = nqDTO;
        this.nqBUS = new NhomQuyenBUS();

        if (nqDTO != null) {
            this.ctQuyen = nqBUS.getChiTietQuyen(nqDTO.getManhomquyen());
        }

        initComponents(type);
    }

    private void initComponents(String type) {
        dmcn = DanhMucChucNangDAO.getInstance().selectAll();
        String[] hanhdong = {"Xem", "Tạo mới", "Cập nhật", "Xoá"};

        this.setSize(new Dimension(1000, 580));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));

        // --- TOP: Tên nhóm quyền ---
        jpTop = new JPanel(new BorderLayout(20, 10));
        jpTop.setBorder(new EmptyBorder(20, 20, 20, 20));
        jpTop.setBackground(Color.WHITE);
        lblTennhomquyen = new JLabel("Tên nhóm quyền");
        txtTennhomquyen = new JTextField();
        txtTennhomquyen.setPreferredSize(new Dimension(150, 35));
        jpTop.add(lblTennhomquyen, BorderLayout.WEST);
        jpTop.add(txtTennhomquyen, BorderLayout.CENTER);

        // --- LEFT: Danh mục chức năng ---
        jpLeft = new JPanel(new GridLayout(dmcn.size() + 1, 1));
        jpLeft.setBackground(Color.WHITE);
        jpLeft.setBorder(new EmptyBorder(0, 20, 0, 14));
        JLabel dmcnLabel = new JLabel("Danh mục chức năng ");
        dmcnLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        jpLeft.add(dmcnLabel);
        for (DanhMucChucNangDTO i : dmcn) {
            jpLeft.add(new JLabel(i.getTenchucnang()));
        }

        // --- CENTER: Checkbox CRUD ---
        sizeDmCn = dmcn.size();
        sizeHanhdong = hanhdong.length;
        jpCen = new JPanel(new GridLayout(sizeDmCn + 1, sizeHanhdong));
        jpCen.setBackground(Color.WHITE);
        listCheckBox = new JCheckBox[sizeDmCn][sizeHanhdong];

        // Header hành động
        for (int i = 0; i < sizeHanhdong; i++) {
            JLabel lblhanhdong = new JLabel(hanhdong[i]);
            lblhanhdong.setHorizontalAlignment(SwingConstants.CENTER);
            jpCen.add(lblhanhdong);
        }
        // Checkbox matrix
        for (int i = 0; i < sizeDmCn; i++) {
            for (int j = 0; j < sizeHanhdong; j++) {
                listCheckBox[i][j] = new JCheckBox();
                listCheckBox[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                listCheckBox[i][j].setBackground(Color.WHITE);
                jpCen.add(listCheckBox[i][j]);
            }
        }

        // --- BOTTOM: Nút bấm ---
        jpBottom = new JPanel(new FlowLayout());
        jpBottom.setBackground(Color.WHITE);
        jpBottom.setBorder(new EmptyBorder(20, 0, 20, 0));

        switch (type) {
            case "create" -> {
                btnAdd = new ButtonCustom("Thêm nhóm quyền", "success", 14);
                btnAdd.addActionListener(this);
                jpBottom.add(btnAdd);
            }
            case "update" -> {
                btnUpdate = new ButtonCustom("Cập nhật nhóm quyền", "success", 14);
                btnUpdate.addActionListener(this);
                jpBottom.add(btnUpdate);
                initUpdate();
            }
            case "view" -> {
                initUpdate();
                setReadOnly();
            }
        }

        btnClose = new ButtonCustom("Huỷ bỏ", "danger", 14);
        btnClose.addActionListener(this);
        jpBottom.add(btnClose);

        this.add(jpTop, BorderLayout.NORTH);
        this.add(jpLeft, BorderLayout.WEST);
        this.add(jpCen, BorderLayout.CENTER);
        this.add(jpBottom, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void initUpdate() {
        txtTennhomquyen.setText(nqDTO.getTennhomquyen());
        if (ctQuyen == null) return;
        for (ChiTietQuyenDTO k : ctQuyen) {
            for (int i = 0; i < sizeDmCn; i++) {
                for (int j = 0; j < sizeHanhdong; j++) {
                    if (k.getHanhdong().equals(mahanhdong[j])
                            && k.getMachucnang().equals(dmcn.get(i).getMachucnang())) {
                        listCheckBox[i][j].setSelected(true);
                    }
                }
            }
        }
    }

    private void setReadOnly() {
        txtTennhomquyen.setEditable(false);
        for (int i = 0; i < sizeDmCn; i++) {
            for (int j = 0; j < sizeHanhdong; j++) {
                listCheckBox[i][j].setEnabled(false);
            }
        }
    }

    private ArrayList<ChiTietQuyenDTO> getListChiTietSelected(int manhomquyen) {
        ArrayList<ChiTietQuyenDTO> result = new ArrayList<>();
        for (int i = 0; i < sizeDmCn; i++) {
            for (int j = 0; j < sizeHanhdong; j++) {
                if (listCheckBox[i][j].isSelected()) {
                    result.add(new ChiTietQuyenDTO(manhomquyen, dmcn.get(i).getMachucnang(), mahanhdong[j]));
                }
            }
        }
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            String ten = txtTennhomquyen.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên nhóm quyền không được trống!");
                return;
            }
            int nextId = NhomQuyenDAO.getInstance().getAutoIncrement();
            ArrayList<ChiTietQuyenDTO> ctList = getListChiTietSelected(nextId);
            if (nqBUS.add(new NhomQuyenDTO(nextId, ten), ctList)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                jp.loadDataTable(nqBUS.getAll());
                dispose();
            }
        } else if (e.getSource() == btnUpdate) {
            String ten = txtTennhomquyen.getText().trim();
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên nhóm quyền không được trống!");
                return;
            }
            nqDTO.setTennhomquyen(ten);
            ArrayList<ChiTietQuyenDTO> ctList = getListChiTietSelected(nqDTO.getManhomquyen());
            if (nqBUS.update(nqDTO, ctList)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                jp.loadDataTable(nqBUS.getAll());
                dispose();
            }
        } else if (e.getSource() == btnClose) {
            dispose();
        }
    }
}