package GUI.Dialog;

import BUS.KyThiBUS;
import DTO.KyThiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputDate;
import GUI.Component.InputForm;
import GUI.Panel.KyThi;
import helper.Validation;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class KyThiDialog extends JDialog {

    private InputForm tenKyThi;
    private InputDate thoiGianBatDau, thoiGianKetThuc;
    private ButtonCustom btnLuu, btnHuy;
    private KyThiBUS bus = new KyThiBUS();
    private KyThi parent;
    private KyThiDTO currentDTO;
    private JPanel pnlMain, pnlButtons;

    public KyThiDialog(KyThi parent, JFrame owner, String title, boolean modal, String type, KyThiDTO kyThi) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = kyThi;
        this.setTitle(title);
        init(type);
    }

    private void init(String type) {
        this.setSize(500, 550);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        initPnlMain(type);
        initPnlButtons(type);

        JScrollPane scrollPane = new JScrollPane(pnlMain);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);

        if (type.equals("view")) {
            tenKyThi.setDisable();
            thoiGianBatDau.setDisable();
            thoiGianKetThuc.setDisable();
        }

        this.setVisible(true);
    }

    private void initPnlMain(String type) {
        pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlMain.setBackground(Color.WHITE);

        tenKyThi = new InputForm("Tên kỳ thi");
        thoiGianBatDau = new InputDate("Thời gian bắt đầu");
        thoiGianKetThuc = new InputDate("Thời gian kết thúc");

        if (currentDTO != null) {
            tenKyThi.setText(currentDTO.getTenkythi());
            thoiGianBatDau.setDate(currentDTO.getThoigianbatdau());
            thoiGianKetThuc.setDate(currentDTO.getThoigianketthuc());
        }

        pnlMain.add(tenKyThi);
        pnlMain.add(Box.createVerticalStrut(15));
        pnlMain.add(thoiGianBatDau);
        pnlMain.add(Box.createVerticalStrut(15));
        pnlMain.add(thoiGianKetThuc);
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
                    luuKyThi(type);
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

    // Lưu hoặc cập nhật kỳ thi
    private void luuKyThi(String type) {
        KyThiDTO kyThi = new KyThiDTO();
        kyThi.setTenkythi(tenKyThi.getText());
        kyThi.setThoigianbatdau(new Timestamp(thoiGianBatDau.getDate().getTime()));
        kyThi.setThoigianketthuc(new Timestamp(thoiGianKetThuc.getDate().getTime()));

        if (type.equals("create")) {
            if (bus.add(kyThi)) {
                JOptionPane.showMessageDialog(null, "Thêm thành công!");
                parent.loadDataTable(bus.getAll());
                dispose();
            }
        } else {
            kyThi.setMakythi(currentDTO.getMakythi());
            if (bus.update(kyThi)) {
                JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
                parent.loadDataTable(bus.getAll());
                dispose();
            }
        }
    }

    // Kiểm tra dữ liệu trước khi lưu
    private boolean validateInput() {
        if (Validation.isEmpty(tenKyThi.getText())) {
            JOptionPane.showMessageDialog(null, "Tên kỳ thi không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Date batDau = thoiGianBatDau.getDate();
        Date ketThuc = thoiGianKetThuc.getDate();

        if (batDau == null || ketThuc == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ thời gian bắt đầu và kết thúc!");
            return false;
        }

        if (ketThuc.before(batDau)) {
            JOptionPane.showMessageDialog(null, "Thời gian kết thúc phải lớn hơn thời gian bắt đầu!", "Lỗi!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}