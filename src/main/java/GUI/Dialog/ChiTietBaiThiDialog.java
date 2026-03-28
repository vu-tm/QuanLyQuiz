package GUI.Dialog;

import BUS.BaiThiBUS;
import BUS.CauHoiBUS;
import BUS.DeThiBUS;
import BUS.NguoiDungBUS;
import DTO.BaiThiDTO;
import DTO.ChiTietBaiThiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import helper.Formater;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class ChiTietBaiThiDialog extends JDialog implements ActionListener {

    private JPanel pnmain, pnmain_top, pnmain_bottom, pnmain_btn;
    private InputForm txtTenDe, txtNguoiLam, txtDiem, txtThoiGianVao, txtThoiGianLam, txtSoCauDung;
    private DefaultTableModel tblModel;
    private JTable table;
    private JScrollPane scrollTable;

    private BaiThiDTO baithi;
    private BaiThiBUS baithiBUS = new BaiThiBUS();
    private DeThiBUS dethiBUS = new DeThiBUS();
    private NguoiDungBUS ndBUS = new NguoiDungBUS();
    private CauHoiBUS cauHoiBUS = new CauHoiBUS();

    private ButtonCustom btnDong;

    public ChiTietBaiThiDialog(JFrame owner, String title, boolean modal, BaiThiDTO baithiDTO) {
        super(owner, title, modal);
        this.baithi = baithiDTO;
        initComponent();
        fillData();
        loadDataTable();
        this.setVisible(true);
    }

    public void initComponent() {
        this.setSize(new Dimension(1000, 650));
        this.setLayout(new BorderLayout(0, 0));

        pnmain = new JPanel(new BorderLayout());

        pnmain_top = new JPanel(new GridLayout(2, 3, 10, 10));
        pnmain_top.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnmain_top.setBackground(Color.WHITE);

        txtTenDe = new InputForm("Đề thi");
        txtNguoiLam = new InputForm("Sinh viên");
        txtDiem = new InputForm("Điểm số");
        txtThoiGianVao = new InputForm("Thời gian vào thi");
        txtThoiGianLam = new InputForm("Thời gian làm bài (giây)");
        txtSoCauDung = new InputForm("Số câu đúng/sai");

        InputForm[] inputs = {txtTenDe, txtNguoiLam, txtDiem, txtThoiGianVao, txtThoiGianLam, txtSoCauDung};
        for (InputForm inp : inputs) {
            inp.setEditable(false);
            pnmain_top.add(inp);
        }
        pnmain.add(pnmain_top, BorderLayout.NORTH);

        pnmain_bottom = new JPanel(new BorderLayout());
        pnmain_bottom.setBorder(new EmptyBorder(5, 10, 5, 10));
        pnmain_bottom.setBackground(Color.WHITE);

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblModel.setColumnIdentifiers(new String[]{"STT", "Nội dung câu hỏi", "Đáp án đã chọn", "Kết quả"});

        table = new JTable(tblModel);
        table.setFocusable(false);
        table.setRowHeight(35);

        scrollTable = new JScrollPane(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(500);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                if (value != null && "Sai".equalsIgnoreCase(value.toString())) {
                    c.setForeground(Color.RED);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));

                    if (isSelected) {
                        c.setForeground(table.getSelectionForeground());
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });

        pnmain_bottom.add(scrollTable, BorderLayout.CENTER);
        pnmain.add(pnmain_bottom, BorderLayout.CENTER);

        pnmain_btn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnmain_btn.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnmain_btn.setBackground(Color.WHITE);

        btnDong = new ButtonCustom("Đóng", "danger", 14);
        btnDong.addActionListener(this);
        pnmain_btn.add(btnDong);

        pnmain.add(pnmain_btn, BorderLayout.SOUTH);

        this.add(pnmain, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    private void fillData() {
        txtTenDe.setText(dethiBUS.getById(baithi.getMade()).getTende());
        txtNguoiLam.setText(ndBUS.getHotenById(baithi.getManguoidung()));
        txtDiem.setText(String.valueOf(baithi.getDiemthi()));
        txtThoiGianVao.setText(Formater.FormatTime(baithi.getThoigianvaothi()));
        txtThoiGianLam.setText(String.valueOf(baithi.getThoigianlambai()));
        txtSoCauDung.setText(baithi.getSocaudung() + " đúng - " + baithi.getSocausai() + " sai");
    }

    public void loadDataTable() {
        tblModel.setRowCount(0);
        ArrayList<ChiTietBaiThiDTO> listCT = baithiBUS.getChiTietByMaBaiThi(baithi.getMabaithi());
        for (int i = 0; i < listCT.size(); i++) {
            ChiTietBaiThiDTO ct = listCT.get(i);
            String noiDungCH = cauHoiBUS.getById(ct.getMacauhoi()).getNoidung();
            String dapAnText = baithiBUS.getAnswerText(ct);
            String ketQua = baithiBUS.evaluateAnswer(ct);

            tblModel.addRow(new Object[]{
                i + 1, noiDungCH, dapAnText, ketQua
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDong) {
            dispose();
        }
    }
}
