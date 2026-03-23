package GUI.Dialog;

import BUS.LopBUS;
import BUS.MonHocBUS;
import BUS.NguoiDungBUS;
import DTO.ChiTietLopDTO;
import DTO.LopDTO;
import DTO.MonHocDTO;
import DTO.NguoiDungDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ChiTietLopDialog extends JDialog implements ActionListener {
    private JPanel pnmain, pnmain_top, pnmain_bottom, pnmain_btn;
    private InputForm txtTenlop, txtNamhoc, txtHocky, txtGiangvien, txtMonhoc, txtSiso;
    private DefaultTableModel tblModel;
    private JTable table;
    private JScrollPane scrollTable;

    private LopDTO lop;
    private LopBUS lopBUS = new LopBUS();
    private NguoiDungBUS nguoidungBUS = new NguoiDungBUS();
    private MonHocBUS monhocBUS = new MonHocBUS();

    private ButtonCustom btnDong;

    public ChiTietLopDialog(JFrame owner, String title, boolean modal, LopDTO lop) {
        super(owner, title, modal);
        this.lop = lop;
        initComponent(title);
        fillData();
        loadDataTable();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponent(String title) {
        this.setSize(new Dimension(1000, 600));
        this.setLayout(new BorderLayout(0, 0));
        this.getContentPane().setBackground(Color.WHITE);

        pnmain = new JPanel(new BorderLayout());
        pnmain.setBackground(Color.WHITE);

        pnmain_top = new JPanel(new GridLayout(2, 2, 10, 10));
        pnmain_top.setBorder(new EmptyBorder(15, 15, 10, 15));
        pnmain_top.setBackground(Color.WHITE);

        txtTenlop = new InputForm("Tên lớp");
        txtNamhoc = new InputForm("Năm học");
        txtHocky = new InputForm("Học kỳ");
        txtGiangvien = new InputForm("Giảng viên");
        txtMonhoc = new InputForm("Môn học");

        InputForm[] inputs = {txtTenlop, txtNamhoc, txtHocky, txtGiangvien, txtMonhoc};
        for (InputForm inp : inputs) {
            inp.setEditable(false);
            pnmain_top.add(inp);
        }
        pnmain.add(pnmain_top, BorderLayout.NORTH);

        pnmain_bottom = new JPanel(new BorderLayout());
        pnmain_bottom.setBorder(new EmptyBorder(5, 15, 5, 15));
        pnmain_bottom.setBackground(Color.WHITE);

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblModel.setColumnIdentifiers(new String[]{"STT", "Mã sinh viên", "Họ tên", "Giới tính"});

        table = new JTable(tblModel);
        table.setRowSelectionAllowed(false);
        table.setFocusable(false);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(350);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        pnmain_bottom.add(scrollTable, BorderLayout.CENTER);
        pnmain.add(pnmain_bottom, BorderLayout.CENTER);

        pnmain_btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnmain_btn.setBorder(new EmptyBorder(5, 15, 15, 15));
        pnmain_btn.setBackground(Color.WHITE);

        btnDong = new ButtonCustom("Đóng", "danger", 14);
        btnDong.addActionListener(this);
        pnmain_btn.add(btnDong);

        pnmain.add(pnmain_btn, BorderLayout.SOUTH);
        this.add(pnmain, BorderLayout.CENTER);
    }

    private void fillData() {
        txtTenlop.setText(lop.getTenlop());
        txtNamhoc.setText(String.valueOf(lop.getNamhoc()));
        txtHocky.setText("Học kỳ " + lop.getHocky());

        String tenGV = nguoidungBUS.getHotenById(lop.getGiangvien());
        txtGiangvien.setText(tenGV != null ? tenGV : String.valueOf(lop.getGiangvien()));

        MonHocDTO mh = monhocBUS.getById(lop.getMamonhoc());
        txtMonhoc.setText(mh != null ? mh.getTenmonhoc() : String.valueOf(lop.getMamonhoc()));
    }

    private void loadDataTable() {
        tblModel.setRowCount(0);
        ArrayList<ChiTietLopDTO> list = lopBUS.getChiTietByMaLop(lop.getMalop());
        int stt = 1;
        for (ChiTietLopDTO ct : list) {
            NguoiDungDTO sv = nguoidungBUS.getById(ct.getManguoidung());
            if (sv != null) {
                tblModel.addRow(new Object[]{
                    stt++,
                    ct.getManguoidung(),
                    sv.getHoten(),
                    nguoidungBUS.getGioiTinhText(sv.isGioitinh())
                });
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDong) {
            dispose();
        }
    }
}
