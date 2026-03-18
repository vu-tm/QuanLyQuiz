package GUI.Dialog;

import BUS.DoKhoBUS;
import DTO.DoKhoDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Panel.DoKho;
import helper.Validation;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DoKhoDialog extends JDialog {

    private InputForm tenDoKho;
    private ButtonCustom btnLuu, btnHuy;
    private DoKhoBUS bus = new DoKhoBUS();
    private DoKho parent;
    private DoKhoDTO currentDTO;
    private JPanel pnlMain, pnlButtons;

    public DoKhoDialog(DoKho parent, JFrame owner, String title, boolean modal, String type, DoKhoDTO dk) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = dk;
        this.setTitle(title);
        init(type);
    }

    private void init(String type) {
        this.setSize(450, 300);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        initPnlMain(type);
        initPnlButtons(type);

        this.add(pnlMain, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);

        if (type.equals("view")) {
            tenDoKho.setDisable();
        }

        this.setVisible(true);
    }

    private void initPnlMain(String type) {
        pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlMain.setBackground(Color.WHITE);

        tenDoKho = new InputForm("Tên độ khó");

        if (currentDTO != null) {
            tenDoKho.setText(currentDTO.getTendokho());
        }

        pnlMain.add(tenDoKho);
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
                    luuDoKho(type);
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

    private void luuDoKho(String type) {
        DoKhoDTO dk = new DoKhoDTO();
        dk.setTendokho(tenDoKho.getText());

        if (type.equals("create")) {
            if (bus.add(dk)) {
                JOptionPane.showMessageDialog(null, "Thêm thành công!");
                parent.loadDataTable(bus.getAll());
                dispose();
            }
        } else {
            dk.setMadokho(currentDTO.getMadokho());
            if (bus.update(dk)) {
                JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
                parent.loadDataTable(bus.getAll());
                dispose();
            }
        }
    }

    private boolean validateInput() {
        if (Validation.isEmpty(tenDoKho.getText())) {
            JOptionPane.showMessageDialog(null, "Tên độ khó không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}