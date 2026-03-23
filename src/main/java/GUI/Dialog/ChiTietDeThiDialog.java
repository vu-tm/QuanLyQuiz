package GUI.Dialog;

import BUS.DeThiBUS;
import BUS.KyThiBUS;
import BUS.MonHocBUS;
import BUS.DoKhoBUS;
import BUS.NguoiDungBUS;
import DTO.DeThiDTO;
import DTO.CauHoiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import helper.Formater;
import helper.writePDF;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public final class ChiTietDeThiDialog extends JDialog implements ActionListener {

    private JPanel pnmain, pnmain_top, pnmain_bottom, pnmain_btn;
    private InputForm txtTenDe, txtKyThi, txtMonHoc, txtNguoiTao, txtThoiGianTao, txtThoiGianThi;
    private DefaultTableModel tblModel;
    private JTable table;
    private JScrollPane scrollTable;

    private DeThiDTO dethi;
    private DeThiBUS dethiBus = new DeThiBUS();
    private KyThiBUS kythiBus = new KyThiBUS();
    private MonHocBUS monhocBus = new MonHocBUS();
    private DoKhoBUS dokhoBus = new DoKhoBUS();
    private NguoiDungBUS ndBUS = new NguoiDungBUS();

    private ButtonCustom btnPdf, btnHuyBo;

    private ArrayList<CauHoiDTO> dsCauHoi;

    public ChiTietDeThiDialog(JFrame owner, String title, boolean modal, DeThiDTO dethiDTO) {
        super(owner, title, modal);
        this.dethi = dethiDTO;
        this.dsCauHoi = dethiBus.getDanhSachCauHoiByMade(dethiDTO.getMade());
        initComponent(title);
        fillData();
        loadDataTable();
        this.setVisible(true);
    }

    public void initComponent(String title) {
        this.setSize(new Dimension(1000, 600));
        this.setLayout(new BorderLayout(0, 0));

        pnmain = new JPanel(new BorderLayout());

        pnmain_top = new JPanel(new GridLayout(2, 3, 10, 10));
        pnmain_top.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnmain_top.setBackground(Color.WHITE);

        txtTenDe = new InputForm("Tên đề thi");
        txtKyThi = new InputForm("Kỳ thi");
        txtMonHoc = new InputForm("Môn học");
        txtNguoiTao = new InputForm("Người tạo");
        txtThoiGianTao = new InputForm("Thời gian tạo");
        txtThoiGianThi = new InputForm("Thời gian thi (phút)");

        InputForm[] inputs = {txtTenDe, txtKyThi, txtMonHoc, txtNguoiTao, txtThoiGianTao, txtThoiGianThi};
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
        tblModel.setColumnIdentifiers(new String[]{"STT", "Mã CH", "Nội dung câu hỏi", "Độ khó"});

        table = new JTable(tblModel);
        table.setRowSelectionAllowed(false);
        table.setFocusable(false);
        table.setRowHeight(35);

        scrollTable = new JScrollPane(table);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(600);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        pnmain_bottom.add(scrollTable, BorderLayout.CENTER);
        pnmain.add(pnmain_bottom, BorderLayout.CENTER);

        // ── Bottom: nút bấm ──────────────────────────────────────────────────
        pnmain_btn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnmain_btn.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnmain_btn.setBackground(Color.WHITE);

        btnPdf = new ButtonCustom("Xuất PDF", "success", 14);
        btnHuyBo = new ButtonCustom("Đóng", "danger", 14);

        btnPdf.addActionListener(this);
        btnHuyBo.addActionListener(this);

        pnmain_btn.add(btnPdf);
        pnmain_btn.add(btnHuyBo);

        pnmain.add(pnmain_btn, BorderLayout.SOUTH);

        this.add(pnmain, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
    }

    private void fillData() {
        txtTenDe.setText(dethi.getTende());
        txtKyThi.setText(kythiBus.getTenById(dethi.getMakythi()));
        txtMonHoc.setText(monhocBus.getTenById(dethi.getMonthi()));
        txtNguoiTao.setText(ndBUS.getHotenById(dethi.getNguoitao()));
        txtThoiGianTao.setText(Formater.FormatTime(dethi.getThoigiantao()));
        txtThoiGianThi.setText(String.valueOf(dethi.getThoigianthi()));
    }

    public void loadDataTable() {
        tblModel.setRowCount(0);
        for (int i = 0; i < dsCauHoi.size(); i++) {
            CauHoiDTO ch = dsCauHoi.get(i);
            tblModel.addRow(new Object[]{
                i + 1,
                ch.getMacauhoi(),
                ch.getNoidung(),
                dokhoBus.getTenDoKho(ch.getMadokho())
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHuyBo) {
            dispose();
        }
        if (e.getSource() == btnPdf) {
            writePDF w = new writePDF();
            w.writeDeThi(dethi.getMade());
        }
    }
}
