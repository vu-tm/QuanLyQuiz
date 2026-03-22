package GUI.Dialog;

import BUS.LopBUS;
import BUS.MonHocBUS;
import BUS.NguoiDungBUS;
import BUS.PhanCongBUS;
import DTO.ChiTietLopDTO;
import DTO.LopDTO;
import DTO.MonHocDTO;
import DTO.NguoiDungDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Component.SelectForm;
import GUI.Panel.LopHoc;
import helper.Validation;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class LopDialog extends JDialog implements ActionListener {

    private static final int MA_QUYEN_SINH_VIEN = 3;

    private InputForm txtTenlop, txtNamhoc, txtHocky;
    private InputForm txtGiangvien;
    private SelectForm cbMonhoc;
    private InputForm txtSoSVDaChon;

    private JTextField txtSearch;
    private JCheckBox chkTatCa;
    private JPanel pnlSinhVienList;
    private ArrayList<JCheckBox> listCheckBox = new ArrayList<>();
    private ArrayList<NguoiDungDTO> listSV = new ArrayList<>();

    private ButtonCustom btnConfirm, btnHuyBo;

    private LopHoc lopHocPanel;
    private LopBUS lopBUS = new LopBUS();
    private NguoiDungBUS nguoidungBUS = new NguoiDungBUS();
    private MonHocBUS monhocBUS = new MonHocBUS();
    private LopDTO lop;
    private String mode;
    private NguoiDungDTO nguoiDungDangNhap;

    // Map tên môn học -> mã môn học để tra ngược
    private HashMap<String, Integer> tenToMaMon = new HashMap<>();

    public LopDialog(LopHoc lopHocPanel, JFrame owner, String title, boolean modal,
            String mode, LopDTO lop, NguoiDungDTO nguoiDungDangNhap) {
        super(owner, title, modal);
        this.lopHocPanel = lopHocPanel;
        this.mode = mode;
        this.lop = lop;
        this.nguoiDungDangNhap = nguoiDungDangNhap;
        initComponent(title);
        if (mode.equals("update")) {
            fillData();
        }
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponent(String title) {
        this.setSize(new Dimension(1100, 700));
        this.setLayout(new BorderLayout(0, 0));
        this.getContentPane().setBackground(Color.WHITE);

        JPanel pnlContent = new JPanel(new GridLayout(1, 2, 15, 0));
        pnlContent.setBackground(Color.WHITE);
        pnlContent.setBorder(new EmptyBorder(15, 15, 5, 15));

        // LEFT
        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        pnlLeft.setBackground(Color.WHITE);

        txtTenlop = new InputForm("Tên lớp");
        txtNamhoc = new InputForm("Năm học");
        txtHocky = new InputForm("Học kỳ");

        txtGiangvien = new InputForm("Giảng viên");
        txtGiangvien.setText(nguoiDungDangNhap.getHoten());
        txtGiangvien.setEditable(false);
        txtGiangvien.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        cbMonhoc = new SelectForm("Môn học", new String[]{});
        cbMonhoc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        loadMonHoc();

        txtSoSVDaChon = new InputForm("Số sinh viên đã chọn");
        txtSoSVDaChon.setText("0");
        txtSoSVDaChon.setEditable(false);

        pnlLeft.add(txtTenlop);
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(txtNamhoc);
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(txtHocky);
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(txtGiangvien);
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(cbMonhoc);
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(txtSoSVDaChon);
        pnlLeft.add(Box.createVerticalGlue());

        // RIGHT
        JPanel pnlRight = new JPanel(new BorderLayout(0, 8));
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Chọn sinh viên",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                new Color(33, 150, 243)
        ));

        JPanel pnlSearchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        pnlSearchBar.setBackground(Color.WHITE);
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(220, 30));
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterSinhVien(txtSearch.getText().trim());
            }
        });
        pnlSearchBar.add(lblSearch);
        pnlSearchBar.add(txtSearch);
        pnlRight.add(pnlSearchBar, BorderLayout.NORTH);

        pnlSinhVienList = new JPanel();
        pnlSinhVienList.setLayout(new BoxLayout(pnlSinhVienList, BoxLayout.Y_AXIS));
        pnlSinhVienList.setBackground(Color.WHITE);

        JScrollPane scrollSV = new JScrollPane(pnlSinhVienList);
        scrollSV.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollSV.getVerticalScrollBar().setUnitIncrement(12);
        pnlRight.add(scrollSV, BorderLayout.CENTER);

        JPanel pnlChonTatCa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlChonTatCa.setBackground(Color.WHITE);
        chkTatCa = new JCheckBox("Chọn tất cả");
        chkTatCa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkTatCa.setBackground(Color.WHITE);
        chkTatCa.addActionListener(e -> handleChonTatCa());
        pnlChonTatCa.add(chkTatCa);
        pnlRight.add(pnlChonTatCa, BorderLayout.SOUTH);

        pnlContent.add(pnlLeft);
        pnlContent.add(pnlRight);
        this.add(pnlContent, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlBtn.setBackground(Color.WHITE);
        pnlBtn.setBorder(new EmptyBorder(0, 15, 10, 15));

        String labelConfirm = mode.equals("create") ? "Tạo lớp học" : "Lưu thông tin";
        btnConfirm = new ButtonCustom(labelConfirm, "success", 14);
        btnHuyBo = new ButtonCustom("Hủy bỏ", "danger", 14);

        btnConfirm.addActionListener(this);
        btnHuyBo.addActionListener(this);

        pnlBtn.add(btnConfirm);
        pnlBtn.add(btnHuyBo);
        this.add(pnlBtn, BorderLayout.SOUTH);

        loadSinhVien();
    }

    private void loadMonHoc() {
        ArrayList<String> items = new ArrayList<>();
        tenToMaMon.clear();

        if (nguoiDungDangNhap.getManhomquyen() == 1) {
            for (MonHocDTO mh : monhocBUS.getAll()) {
                String ten = mh.getTenmonhoc();
                items.add(ten);
                tenToMaMon.put(ten, mh.getMamonhoc());
            }
        } else {
            PhanCongBUS phanCongBUS = new PhanCongBUS();
            ArrayList<Integer> danhSachMaMon = phanCongBUS.getMonHocByGiangVien(nguoiDungDangNhap.getId());
            for (int mamonhoc : danhSachMaMon) {
                MonHocDTO mh = monhocBUS.getById(mamonhoc);
                if (mh != null) {
                    String ten = mh.getTenmonhoc();
                    items.add(ten);
                    tenToMaMon.put(ten, mh.getMamonhoc());
                }
            }
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Giảng viên này chưa được phân công môn học nào! Không thể tạo lớp.",
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                btnConfirm.setEnabled(false);
            }
        }

        cbMonhoc.setArr(items.toArray(new String[0]));
    }

    private void loadSinhVien() {
        listSV = nguoidungBUS.getByNhomQuyen(MA_QUYEN_SINH_VIEN);
        listCheckBox.clear();
        pnlSinhVienList.removeAll();
        for (NguoiDungDTO sv : listSV) {
            JCheckBox chk = new JCheckBox(sv.getHoten());
            chk.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            chk.setBackground(Color.WHITE);
            chk.setBorder(new EmptyBorder(6, 10, 6, 10));
            chk.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            chk.addActionListener(e -> capNhatSoSVDaChon());
            listCheckBox.add(chk);
            pnlSinhVienList.add(chk);
        }
        pnlSinhVienList.revalidate();
        pnlSinhVienList.repaint();
    }

    private void filterSinhVien(String keyword) {
        pnlSinhVienList.removeAll();
        String kw = keyword.toLowerCase();
        for (JCheckBox chk : listCheckBox) {
            if (kw.isEmpty() || chk.getText().toLowerCase().contains(kw)) {
                pnlSinhVienList.add(chk);
            }
        }
        pnlSinhVienList.revalidate();
        pnlSinhVienList.repaint();
    }

    private void handleChonTatCa() {
        boolean selected = chkTatCa.isSelected();
        for (JCheckBox chk : listCheckBox) {
            if (chk.isVisible()) {
                chk.setSelected(selected);
            }
        }
        capNhatSoSVDaChon();
    }

    private void capNhatSoSVDaChon() {
        int count = 0;
        for (JCheckBox chk : listCheckBox) {
            if (chk.isSelected()) {
                count++;
            }
        }
        txtSoSVDaChon.setText(String.valueOf(count));
    }

    private void fillData() {
        if (lop == null) {
            return;
        }
        txtTenlop.setText(lop.getTenlop());
        txtNamhoc.setText(String.valueOf(lop.getNamhoc()));
        txtHocky.setText(String.valueOf(lop.getHocky()));

        // Chọn môn học theo tên
        MonHocDTO mh = monhocBUS.getById(lop.getMamonhoc());
        if (mh != null) {
            for (int i = 0; i < cbMonhoc.cbb.getItemCount(); i++) {
                if (cbMonhoc.cbb.getItemAt(i).toString().equals(mh.getTenmonhoc())) {
                    cbMonhoc.setSelectedIndex(i);
                    break;
                }
            }
        }

        ArrayList<ChiTietLopDTO> listTrongLop = lopBUS.getChiTietByMaLop(lop.getMalop());
        for (ChiTietLopDTO ct : listTrongLop) {
            for (int i = 0; i < listSV.size(); i++) {
                if (listSV.get(i).getId() == ct.getManguoidung()) {
                    listCheckBox.get(i).setSelected(true);
                    break;
                }
            }
        }
        capNhatSoSVDaChon();
    }

    private boolean validateInput() {
        if (Validation.isEmpty(txtTenlop.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Tên lớp không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (Validation.isEmpty(txtNamhoc.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Năm học không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtNamhoc.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Năm học phải là số nguyên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (Validation.isEmpty(txtHocky.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Học kỳ không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            int hocky = Integer.parseInt(txtHocky.getText().trim());
            if (hocky < 1 || hocky > 3) {
                JOptionPane.showMessageDialog(this, "Học kỳ phải từ 1 đến 3!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Học kỳ phải là số nguyên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (cbMonhoc.getValue() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn môn học!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private int getMaMonHocSelected() {
        String tenMonHoc = cbMonhoc.getValue();
        if (tenMonHoc != null && tenToMaMon.containsKey(tenMonHoc)) {
            return tenToMaMon.get(tenMonHoc);
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHuyBo) {
            dispose();
        } else if (e.getSource() == btnConfirm) {
            if (mode.equals("create")) {
                handleAdd();
            } else {
                handleEdit();
            }
        }
    }

    private void handleAdd() {
        if (!validateInput()) {
            return;
        }

        int maMonHoc = getMaMonHocSelected();
        if (maMonHoc == -1) {
            JOptionPane.showMessageDialog(this, "Môn học không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LopDTO newLop = new LopDTO();
        newLop.setTenlop(txtTenlop.getText().trim());
        newLop.setSiso(0);
        newLop.setNamhoc(Integer.parseInt(txtNamhoc.getText().trim()));
        newLop.setHocky(Integer.parseInt(txtHocky.getText().trim()));
        newLop.setGiangvien(nguoiDungDangNhap.getId());
        newLop.setMamonhoc(maMonHoc);
        newLop.setTrangthai(1);

        if (lopBUS.add(newLop)) {
            ArrayList<LopDTO> all = lopBUS.getAll();
            LopDTO created = all.get(all.size() - 1);
            luuChiTietLop(created.getMalop());
            lopHocPanel.refreshData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm lớp học thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEdit() {
        if (!validateInput()) {
            return;
        }

        int maMonHoc = getMaMonHocSelected();
        if (maMonHoc == -1) {
            JOptionPane.showMessageDialog(this, "Môn học không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        lop.setTenlop(txtTenlop.getText().trim());
        lop.setSiso(0);
        lop.setNamhoc(Integer.parseInt(txtNamhoc.getText().trim()));
        lop.setHocky(Integer.parseInt(txtHocky.getText().trim()));
        lop.setGiangvien(nguoiDungDangNhap.getId());
        lop.setMamonhoc(maMonHoc);

        if (lopBUS.update(lop)) {
            luuChiTietLop(lop.getMalop());
            lopHocPanel.loadDataTable(lopBUS.getAll());
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật lớp học thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void luuChiTietLop(int malop) {
        for (int i = 0; i < listCheckBox.size(); i++) {
            int maSV = listSV.get(i).getId();
            boolean isChecked = listCheckBox.get(i).isSelected();

            if (isChecked) {
                if (lopBUS.checkExistsChiTiet(malop, maSV)) {
                    lopBUS.restoreChiTiet(malop, maSV);
                } else {
                    ChiTietLopDTO ct = new ChiTietLopDTO(malop, maSV, 1);
                    lopBUS.addChiTiet(ct);
                }
            } else {
                if (lopBUS.checkExistsChiTiet(malop, maSV)) {
                    lopBUS.deleteChiTiet(malop, maSV);
                }
            }
        }
    }
}