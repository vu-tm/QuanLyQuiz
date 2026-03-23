package GUI.Dialog;

import BUS.LoaiCauHoiBUS;
import DTO.LoaiCauHoiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Panel.LoaiCauHoi;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoaiCauHoiDialog extends JDialog {

    private InputForm tenLoai, maLoai;
    private ButtonCustom btnHuy;
    private LoaiCauHoiDTO currentDTO;
    private JPanel pnlMain, pnlButtons;

    public LoaiCauHoiDialog(LoaiCauHoi parent, JFrame owner, String title, boolean modal, String type, LoaiCauHoiDTO dto) {
        super(owner, title, modal);
        this.currentDTO = dto;
        init(type);
    }

    private void init(String type) {
        this.setSize(400, 300);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        initPnlMain();
        initPnlButtons();

        this.add(pnlMain, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);

        // Vì chỉ dùng để xem nên luôn disable input
        maLoai.setDisable();
        tenLoai.setDisable();

        this.setVisible(true);
    }

    private void initPnlMain() {
        pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlMain.setBackground(Color.WHITE);

        maLoai = new InputForm("Mã loại");
        tenLoai = new InputForm("Tên loại câu hỏi");

        if (currentDTO != null) {
            maLoai.setText(String.valueOf(currentDTO.getMaloai()));
            tenLoai.setText(currentDTO.getTenloai());
        }

        pnlMain.add(maLoai);
        pnlMain.add(Box.createVerticalStrut(15));
        pnlMain.add(tenLoai);
    }

    private void initPnlButtons() {
        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnHuy = new ButtonCustom("Đóng", "danger", 14);
        btnHuy.addActionListener(e -> dispose());
        pnlButtons.add(btnHuy);
    }
}