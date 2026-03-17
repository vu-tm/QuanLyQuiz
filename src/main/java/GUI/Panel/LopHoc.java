package GUI.Panel;

import BUS.LopBUS;
import DTO.LopDTO;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class LopHoc extends JPanel {
    private LopBUS lopBUS = new LopBUS();
    private ArrayList<LopDTO> listLop;
    
    private JTable tblLop;
    private DefaultTableModel modelLop;
    private JTextField txtTimKiem;
    private JComboBox<String> cbTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnChiTiet, btnLamMoi;
    
    Color MainColor = new Color(250, 250, 250);
    Color FontColorHeader = new Color(96, 125, 139);
    
    public LopHoc() {
        initComponent();
        loadDataTable();
    }
    
    private void initComponent() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(Color.WHITE);
        
        // HEADER
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setPreferredSize(new Dimension(0, 70));
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.add(pnlHeader, BorderLayout.NORTH);
        
        JLabel lblTitle = new JLabel("QUẢN LÝ LỚP HỌC");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(new Color(26, 26, 26));
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        
        // TOOLBAR
        JPanel pnlToolbar = new JPanel(new BorderLayout());
        pnlToolbar.setPreferredSize(new Dimension(0, 80));
        pnlToolbar.setBackground(Color.WHITE);
        pnlToolbar.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.add(pnlToolbar, BorderLayout.CENTER);
        
        // Top toolbar - Tìm kiếm
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.setBackground(Color.WHITE);
        pnlToolbar.add(pnlTop, BorderLayout.NORTH);
        
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(new Font("Roboto", Font.PLAIN, 14));
        pnlTop.add(lblTimKiem);
        
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(250, 35));
        txtTimKiem.setFont(new Font("Roboto", Font.PLAIN, 14));
        pnlTop.add(txtTimKiem);
        
        cbTimKiem = new JComboBox<>(new String[]{"Tất cả", "Mã lớp", "Tên lớp", "Năm học", "Học kỳ"});
        cbTimKiem.setPreferredSize(new Dimension(120, 35));
        cbTimKiem.setFont(new Font("Roboto", Font.PLAIN, 14));
        pnlTop.add(cbTimKiem);
        
        JButton btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setPreferredSize(new Dimension(100, 35));
        btnTimKiem.setFont(new Font("Roboto", Font.PLAIN, 14));
        btnTimKiem.setBackground(new Color(30, 136, 229));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.addActionListener(e -> timKiem());
        pnlTop.add(btnTimKiem);
        
        // Bottom toolbar - Các nút chức năng
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlBottom.setBackground(Color.WHITE);
        pnlToolbar.add(pnlBottom, BorderLayout.CENTER);
        
        btnThem = createButton("Thêm", new Color(76, 175, 80));
        btnThem.addActionListener(e -> themLop());
        pnlBottom.add(btnThem);
        
        btnSua = createButton("Sửa", new Color(255, 152, 0));
        btnSua.addActionListener(e -> suaLop());
        pnlBottom.add(btnSua);
        
        btnXoa = createButton("Xóa", new Color(244, 67, 54));
        btnXoa.addActionListener(e -> xoaLop());
        pnlBottom.add(btnXoa);
        
        btnChiTiet = createButton("Chi tiết", new Color(33, 150, 243));
        btnChiTiet.addActionListener(e -> xemChiTiet());
        pnlBottom.add(btnChiTiet);
        
        btnLamMoi = createButton("Làm mới", new Color(96, 125, 139));
        btnLamMoi.addActionListener(e -> lamMoi());
        pnlBottom.add(btnLamMoi);
        
        // TABLE
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        String[] header = {"Mã lớp", "Tên lớp", "Sĩ số", "Năm học", "Học kỳ", "Giảng viên", "Môn học", "Trạng thái"};
        modelLop = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblLop = new JTable(modelLop);
        tblLop.setFont(new Font("Roboto", Font.PLAIN, 13));
        tblLop.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblLop.setAutoCreateRowSorter(true);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblLop.getColumnCount(); i++) {
            tblLop.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(tblLop);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)));
        pnlTable.add(scrollPane, BorderLayout.CENTER);
        
        pnlToolbar.add(pnlTable, BorderLayout.SOUTH);
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setFont(new Font("Roboto", Font.PLAIN, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void loadDataTable() {
        modelLop.setRowCount(0);
        listLop = lopBUS.getAll();
        for (LopDTO lop : listLop) {
            modelLop.addRow(new Object[]{
                lop.getMalop(),
                lop.getTenlop(),
                lop.getSiso(),
                lop.getNamhoc(),
                lop.getHocky(),
                lop.getGiangvien(), // Tạm thời hiển thị mã, sau sẽ lấy tên
                lop.getMamonhoc(), // Tạm thời hiển thị mã, sau sẽ lấy tên môn
                lop.getTrangthai() == 1 ? "Hoạt động" : "Ngừng"
            });
        }
    }
    
    private void timKiem() {
        String text = txtTimKiem.getText().trim();
        String type = cbTimKiem.getSelectedItem().toString();
        
        if (text.isEmpty()) {
            loadDataTable();
            return;
        }
        
        ArrayList<LopDTO> result = lopBUS.search(text, type);
        modelLop.setRowCount(0);
        for (LopDTO lop : result) {
            modelLop.addRow(new Object[]{
                lop.getMalop(),
                lop.getTenlop(),
                lop.getSiso(),
                lop.getNamhoc(),
                lop.getHocky(),
                lop.getGiangvien(),
                lop.getMamonhoc(),
                lop.getTrangthai() == 1 ? "Hoạt động" : "Ngừng"
            });
        }
    }
    
    private void themLop() {
        LopDialog dialog = new LopDialog(null, "Thêm lớp học", true);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            LopDTO lop = dialog.getLop();
            if (lopBUS.add(lop)) {
                JOptionPane.showMessageDialog(this, "Thêm lớp thành công!");
                loadDataTable();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm lớp thất bại!");
            }
        }
    }
    
    private void suaLop() {
        int row = tblLop.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp cần sửa!");
            return;
        }
        
        int malop = (int) tblLop.getValueAt(row, 0);
        LopDTO lop = lopBUS.getById(malop);
        
        LopDialog dialog = new LopDialog(null, "Sửa lớp học", true);
        dialog.setLop(lop);
        dialog.setVisible(true);
        
        if (dialog.isSuccess()) {
            LopDTO lopMoi = dialog.getLop();
            if (lopBUS.update(lopMoi)) {
                JOptionPane.showMessageDialog(this, "Sửa lớp thành công!");
                loadDataTable();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa lớp thất bại!");
            }
        }
    }
    
    private void xoaLop() {
        int row = tblLop.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa lớp này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int malop = (int) tblLop.getValueAt(row, 0);
            if (lopBUS.delete(malop)) {
                JOptionPane.showMessageDialog(this, "Xóa lớp thành công!");
                loadDataTable();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa lớp thất bại!");
            }
        }
    }
    
    private void xemChiTiet() {
        int row = tblLop.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp cần xem!");
            return;
        }
        
        int malop = (int) tblLop.getValueAt(row, 0);
        JOptionPane.showMessageDialog(this, "Chức năng xem chi tiết sinh viên sẽ được hoàn thiện sau!\nMã lớp: " + malop);
    }
    
    private void lamMoi() {
        txtTimKiem.setText("");
        cbTimKiem.setSelectedIndex(0);
        loadDataTable();
    }
}

// Dialog thêm/sửa lớp
class LopDialog extends JDialog {
    private LopDTO lop;
    private boolean success = false;
    
    private JTextField txtTenLop, txtSiSo, txtNamHoc, txtHocKy;
    private JComboBox<String> cbGiangVien, cbMonHoc;
    private JButton btnLuu, btnHuy;
    
    public LopDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initComponent();
    }
    
    private void initComponent() {
        this.setSize(450, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new GridBagLayout());
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlMain.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Tên lớp
        gbc.gridx = 0; gbc.gridy = 0;
        pnlMain.add(new JLabel("Tên lớp:"), gbc);
        gbc.gridx = 1;
        txtTenLop = new JTextField(20);
        pnlMain.add(txtTenLop, gbc);
        
        // Sĩ số
        gbc.gridx = 0; gbc.gridy = 1;
        pnlMain.add(new JLabel("Sĩ số:"), gbc);
        gbc.gridx = 1;
        txtSiSo = new JTextField(20);
        pnlMain.add(txtSiSo, gbc);
        
        // Năm học
        gbc.gridx = 0; gbc.gridy = 2;
        pnlMain.add(new JLabel("Năm học:"), gbc);
        gbc.gridx = 1;
        txtNamHoc = new JTextField(20);
        pnlMain.add(txtNamHoc, gbc);
        
        // Học kỳ
        gbc.gridx = 0; gbc.gridy = 3;
        pnlMain.add(new JLabel("Học kỳ:"), gbc);
        gbc.gridx = 1;
        txtHocKy = new JTextField(20);
        pnlMain.add(txtHocKy, gbc);
        
        // Giảng viên
        gbc.gridx = 0; gbc.gridy = 4;
        pnlMain.add(new JLabel("Giảng viên:"), gbc);
        gbc.gridx = 1;
        cbGiangVien = new JComboBox<>(new String[]{"GV001 - Nguyễn Văn A", "GV002 - Trần Thị B"});
        pnlMain.add(cbGiangVien, gbc);
        
        // Môn học
        gbc.gridx = 0; gbc.gridy = 5;
        pnlMain.add(new JLabel("Môn học:"), gbc);
        gbc.gridx = 1;
        cbMonHoc = new JComboBox<>(new String[]{"1 - Lập trình Java", "2 - Cơ sở dữ liệu"});
        pnlMain.add(cbMonHoc, gbc);
        
        this.add(pnlMain, BorderLayout.CENTER);
        
        // Button panel
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButton.setBackground(Color.WHITE);
        
        btnLuu = new JButton("Lưu");
        btnLuu.setPreferredSize(new Dimension(80, 35));
        btnLuu.setBackground(new Color(76, 175, 80));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.addActionListener(e -> luuLop());
        pnlButton.add(btnLuu);
        
        btnHuy = new JButton("Hủy");
        btnHuy.setPreferredSize(new Dimension(80, 35));
        btnHuy.setBackground(new Color(244, 67, 54));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.addActionListener(e -> dispose());
        pnlButton.add(btnHuy);
        
        this.add(pnlButton, BorderLayout.SOUTH);
    }
    
    private void luuLop() {
        String tenlop = txtTenLop.getText().trim();
        String sisoStr = txtSiSo.getText().trim();
        String namhocStr = txtNamHoc.getText().trim();
        String hockyStr = txtHocKy.getText().trim();
        
        if (tenlop.isEmpty() || sisoStr.isEmpty() || namhocStr.isEmpty() || hockyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        try {
            int siso = Integer.parseInt(sisoStr);
            int namhoc = Integer.parseInt(namhocStr);
            int hocky = Integer.parseInt(hockyStr);
            
            String giangvien = cbGiangVien.getSelectedItem().toString().split(" - ")[0];
            int mamonhoc = Integer.parseInt(cbMonHoc.getSelectedItem().toString().split(" - ")[0]);
            
            if (lop == null) {
                lop = new LopDTO();
            }
            
            lop.setTenlop(tenlop);
            lop.setSiso(siso);
            lop.setNamhoc(namhoc);
            lop.setHocky(hocky);
            lop.setGiangvien(giangvien);
            lop.setMamonhoc(mamonhoc);
            lop.setTrangthai(1);
            
            success = true;
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Sĩ số, năm học, học kỳ phải là số!");
        }
    }
    
    public void setLop(LopDTO lop) {
        this.lop = lop;
        txtTenLop.setText(lop.getTenlop());
        txtSiSo.setText(String.valueOf(lop.getSiso()));
        txtNamHoc.setText(String.valueOf(lop.getNamhoc()));
        txtHocKy.setText(String.valueOf(lop.getHocky()));
    }
    
    public LopDTO getLop() {
        return lop;
    }
    
    public boolean isSuccess() {
        return success;
    }
}