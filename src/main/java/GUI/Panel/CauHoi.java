package GUI.Panel;

import BUS.CauHoiBUS;
import DTO.CauHoiDTO;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CauHoi extends JPanel {
    private CauHoiBUS bus;
    private DefaultTableModel model;
    private JTable table;

    private JTextField txtId, txtNoiDung, txtMaDoKho, txtMaLoai, txtMaMon, txtNguoiTao, txtTrangThai, txtSearch;

    public CauHoi() {
        bus = new CauHoiBUS();
        initUI();
        loadData();
    }

    private void initUI() {
        this.setLayout(new BorderLayout(8,8));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(12,12,12,12));

        JLabel title = new JLabel("QUẢN LÝ CÂU HỎI", SwingConstants.CENTER);
        title.setFont(new Font("Roboto", Font.BOLD, 24));
        this.add(title, BorderLayout.NORTH);

        // Split pane: top = form+buttons, bottom = search+table
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.4); // top 40%, bottom 60%
        split.setOneTouchExpandable(true);

        // Top: form and buttons
        JPanel topPanel = new JPanel(new BorderLayout(8,8));
        JPanel form = new JPanel(new GridLayout(0,2,10,10));
        txtId = new JTextField(); txtId.setEditable(false);
        txtNoiDung = new JTextField();
        txtMaDoKho = new JTextField();
        txtMaLoai = new JTextField();
        txtMaMon = new JTextField();
        txtNguoiTao = new JTextField();
        txtTrangThai = new JTextField();

        // helper to create a label+field pair where label is narrow
        java.util.function.BiFunction<String,JComponent,JPanel> pair = (labelText, field) -> {
            JPanel p = new JPanel(new BorderLayout(6,6));
            JLabel lbl = new JLabel(labelText);
            lbl.setPreferredSize(new Dimension(120,24));
            p.add(lbl, BorderLayout.WEST);
            p.add(field, BorderLayout.CENTER);
            return p;
        };

        form.add(pair.apply("Mã câu hỏi", txtId));
        form.add(pair.apply("Nội dung", txtNoiDung));
        form.add(pair.apply("Mã độ khó", txtMaDoKho));
        form.add(pair.apply("Mã loại", txtMaLoai));
        form.add(pair.apply("Mã môn", txtMaMon));
        form.add(pair.apply("Người tạo", txtNguoiTao));
        form.add(pair.apply("Trạng thái", txtTrangThai));

        topPanel.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1,4,8,8));
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        buttons.add(btnAdd); buttons.add(btnUpdate); buttons.add(btnDelete); buttons.add(btnRefresh);
        topPanel.add(buttons, BorderLayout.SOUTH);

        // Bottom: search + table
        JPanel bottomPanel = new JPanel(new BorderLayout(6,6));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,6,6));
        txtSearch = new JTextField(30);
        JButton btnSearch = new JButton("Tìm");
        searchPanel.add(new JLabel("Tìm kiếm:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        bottomPanel.add(searchPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Mã","Nội dung","Mã độ khó","Mã loại","Mã môn","Người tạo","Trạng thái"},0){
            public boolean isCellEditable(int row,int col){return false;}
        };
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        bottomPanel.add(sp, BorderLayout.CENTER);

        split.setTopComponent(topPanel);
        split.setBottomComponent(bottomPanel);

        this.add(split, BorderLayout.CENTER);

        // Actions
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnRefresh.addActionListener(e -> loadData());
        btnSearch.addActionListener(e -> onSearch());

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int r = table.getSelectedRow();
                if(r>=0) fillFormFromSelectedRow(r);
            }
        });
    }

    private void fillFormFromSelectedRow(int r){
        txtId.setText(String.valueOf(model.getValueAt(r,0)));
        txtNoiDung.setText(String.valueOf(model.getValueAt(r,1)));
        txtMaDoKho.setText(String.valueOf(model.getValueAt(r,2)));
        txtMaLoai.setText(String.valueOf(model.getValueAt(r,3)));
        txtMaMon.setText(String.valueOf(model.getValueAt(r,4)));
        txtNguoiTao.setText(String.valueOf(model.getValueAt(r,5)));
        txtTrangThai.setText(String.valueOf(model.getValueAt(r,6)));
    }

    private void loadData(){
        model.setRowCount(0);
        List<CauHoiDTO> list = bus.load();
        for(CauHoiDTO ch: list){
            model.addRow(new Object[]{ch.getMacauhoi(), ch.getNoidung(), ch.getMadokho(), ch.getMaloai(), ch.getMamonhoc(), ch.getNguoitao(), ch.getTrangthai()});
        }
    }

    private void onAdd(){
        try{
            String noidung = txtNoiDung.getText().trim();
            int madokho = Integer.parseInt(txtMaDoKho.getText().trim());
            int maloai = Integer.parseInt(txtMaLoai.getText().trim());
            int mamon = Integer.parseInt(txtMaMon.getText().trim());
            String nguoitao = txtNguoiTao.getText().trim();
            int trangthai = Integer.parseInt(txtTrangThai.getText().trim());
            CauHoiDTO ch = new CauHoiDTO(0,noidung,madokho,maloai,mamon,nguoitao,trangthai);
            if(bus.add(ch)){
                JOptionPane.showMessageDialog(this, "Thêm thành công");
                loadData();
            } else JOptionPane.showMessageDialog(this, "Thêm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onUpdate(){
        try{
            int id = Integer.parseInt(txtId.getText().trim());
            String noidung = txtNoiDung.getText().trim();
            int madokho = Integer.parseInt(txtMaDoKho.getText().trim());
            int maloai = Integer.parseInt(txtMaLoai.getText().trim());
            int mamon = Integer.parseInt(txtMaMon.getText().trim());
            String nguoitao = txtNguoiTao.getText().trim();
            int trangthai = Integer.parseInt(txtTrangThai.getText().trim());
            CauHoiDTO ch = new CauHoiDTO(id,noidung,madokho,maloai,mamon,nguoitao,trangthai);
            if(bus.edit(ch)){
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                loadData();
            } else JOptionPane.showMessageDialog(this, "Cập nhật thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Chưa chọn mục để sửa hoặc dữ liệu không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete(){
        try{
            int id = Integer.parseInt(txtId.getText().trim());
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa mục này?", "Xóa", JOptionPane.YES_NO_OPTION);
            if(confirm==JOptionPane.YES_OPTION){
                if(bus.remove(id)){
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    loadData();
                } else JOptionPane.showMessageDialog(this, "Xóa thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Chưa chọn mục để xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSearch(){
        String k = txtSearch.getText().trim();
        model.setRowCount(0);
        if(k.isEmpty()){
            loadData();
            return;
        }
        List<CauHoiDTO> list = bus.search(k);
        for(CauHoiDTO ch: list){
            model.addRow(new Object[]{ch.getMacauhoi(), ch.getNoidung(), ch.getMadokho(), ch.getMaloai(), ch.getMamonhoc(), ch.getNguoitao(), ch.getTrangthai()});
        }
    }
}
