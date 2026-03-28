package GUI.Dialog;

import BUS.MonHocBUS;
import DTO.MonHocDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Component.NumericDocumentFilter;
import GUI.Panel.MonHoc;
import helper.Validation;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

public class MonHocDialog extends JDialog {

    private InputForm tenMonHoc, soTinChi;
    private ButtonCustom btnLuu, btnHuy;
    private MonHocBUS bus = new MonHocBUS();
    private MonHoc parent;
    private MonHocDTO currentDTO;
    private JPanel pnlMain, pnlButtons;

    public MonHocDialog(MonHoc parent, JFrame owner, String title, boolean modal, String type, MonHocDTO mh) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = mh;
        this.setTitle(title);
        init(type);
    }

    private void init(String type) {
        this.setSize(450, 350);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        initPnlMain();
        initPnlButtons(type);

        this.add(pnlMain, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);

        if (type.equals("view")) {
            tenMonHoc.setDisable();
            soTinChi.setDisable();
        }

        this.setVisible(true);
    }

    private void initPnlMain() {
        pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlMain.setBackground(Color.WHITE);

        tenMonHoc = new InputForm("Tên môn học");
        soTinChi = new InputForm("Số tín chỉ");

        PlainDocument doc = (PlainDocument) soTinChi.getTxtForm().getDocument();
        doc.setDocumentFilter(new NumericDocumentFilter());

        if (currentDTO != null) {
            tenMonHoc.setText(currentDTO.getTenmonhoc());
            soTinChi.setText(String.valueOf(currentDTO.getSotinchi()));
        }

        pnlMain.add(tenMonHoc);
        pnlMain.add(Box.createVerticalStrut(15));
        pnlMain.add(soTinChi);
    }

    private void initPnlButtons(String type) {
        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnLuu = new ButtonCustom(type.equals("create") ? "Thêm mới" : "Lưu thay đổi", "success", 14);
        btnHuy = new ButtonCustom("Huỷ bỏ", "danger", 14);

        btnLuu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    luuMonHoc(type);
                }
            }
        });

        btnHuy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        if (!type.equals("view")) {
            pnlButtons.add(btnLuu);
        }
        pnlButtons.add(btnHuy);
    }

    private void luuMonHoc(String type) {
        String ten = tenMonHoc.getText().trim();
        int soTin = Integer.parseInt(soTinChi.getText().trim());

        MonHocDTO mh = new MonHocDTO();
        mh.setTenmonhoc(ten);
        mh.setSotinchi(soTin);

        if (type.equals("create")) {
            if (bus.add(mh)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                parent.loadDataTable(bus.getAll());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tên môn học đã tồn tại hoặc có lỗi xảy ra!", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            mh.setMamonhoc(currentDTO.getMamonhoc());
            if (bus.update(mh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parent.loadDataTable(bus.getAll());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tên môn học đã tồn tại hoặc có lỗi xảy ra!", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput() {
        if (Validation.isEmpty(tenMonHoc.getText())) {
            JOptionPane.showMessageDialog(null, "Tên môn học không được để trống!");
            return false;
        }
        if (Validation.isEmpty(soTinChi.getText())) {
            JOptionPane.showMessageDialog(null, "Số tín chỉ không được để trống!");
            return false;
        }
        return true;
    }
}
