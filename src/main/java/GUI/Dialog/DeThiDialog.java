package GUI.Dialog;

import BUS.KyThiBUS;
import GUI.Component.InputForm;
import GUI.Component.SelectForm;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.ButtonCustom;
import java.awt.*;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.PlainDocument;

public class DeThiDialog extends JDialog {

    private InputForm tenDe, soCau;
    private SelectForm cbxKyThi, cbxLop;
    private JTextField txtThoiGian;
    private ButtonCustom btnTao, btnHuy;
    private JPanel pnlMain, pnlButtons;

    private KyThiBUS kyThiBUS = new KyThiBUS();

    public DeThiDialog(JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        this.setTitle(title);
        init(type);
    }

    private void init(String type) {
        this.setSize(new Dimension(900, 450));
        this.setLayout(new BorderLayout(0, 0));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        pnlMain = new JPanel(new GridLayout(3, 2, 20, 10));
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlMain.setBackground(Color.WHITE);

        tenDe = new InputForm("Tên đề thi");

        String[] dsKyThi = Stream.concat(Stream.of("Chọn kỳ thi"),
                kyThiBUS.getAll().stream().map(kt -> kt.getTenkythi())).toArray(String[]::new);
        cbxKyThi = new SelectForm("Kỳ thi", dsKyThi);

        JPanel pnlThoiGian = new JPanel(new BorderLayout());
        pnlThoiGian.setBorder(new LineBorder(new Color(204, 214, 219)));
        pnlThoiGian.setBackground(Color.WHITE);

        txtThoiGian = new JTextField("");
        txtThoiGian.setHorizontalAlignment(JTextField.CENTER);
        txtThoiGian.setBorder(new EmptyBorder(0, 10, 0, 10));
        ((PlainDocument) txtThoiGian.getDocument()).setDocumentFilter(new NumericDocumentFilter());

        JLabel lblPhut = new JLabel("phút  ", JLabel.RIGHT);
        lblPhut.setPreferredSize(new Dimension(60, 0));
        lblPhut.setOpaque(true);
        lblPhut.setBackground(new Color(240, 247, 250));

        pnlThoiGian.add(txtThoiGian, BorderLayout.CENTER);
        pnlThoiGian.add(lblPhut, BorderLayout.EAST);

        JPanel pnlTimeGroup = new JPanel(new GridLayout(2, 1));
        pnlTimeGroup.setBackground(Color.WHITE);
        pnlTimeGroup.setBorder(new EmptyBorder(0, 10, 5, 10));
        pnlTimeGroup.add(new JLabel("Thời gian làm bài"));
        pnlTimeGroup.add(pnlThoiGian);

        cbxLop = new SelectForm("Giao cho lớp", new String[]{"Chọn lớp", "Lớp 10A1", "Lớp 11B2", "Lớp 12C3"});
        soCau = new InputForm("Số câu hỏi");

        pnlMain.add(tenDe);
        pnlMain.add(cbxKyThi);
        pnlMain.add(pnlTimeGroup);
        pnlMain.add(cbxLop);
        pnlMain.add(soCau);

        this.add(pnlMain, BorderLayout.CENTER);

        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(10, 0, 10, 0));

        btnTao = new ButtonCustom(type.equals("create") ? "Tạo đề thi" : "Lưu thông tin", "success", 14);
        btnHuy = new ButtonCustom("Huỷ bỏ", "danger", 14);

        if (!type.equals("view")) {
            pnlButtons.add(btnTao);
        }
        pnlButtons.add(btnHuy);

        this.add(pnlButtons, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dispose());

        if (type.equals("view")) {
            tenDe.setDisable();
            soCau.setDisable();
            txtThoiGian.setEnabled(false);
            cbxKyThi.getCbb().setEnabled(false);
            cbxLop.getCbb().setEnabled(false);
        }

        this.setVisible(true);
    }
}
