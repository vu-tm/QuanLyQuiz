package GUI.Dialog;

import GUI.Component.InputForm;
import GUI.Panel.DeThi;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DeThiDialog extends JDialog {
    private InputForm tenDe, thoiGian, monHoc;
    private JButton btnLuu, btnHuy;

    public DeThiDialog(JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        init(type);
    }

    private void init(String type) {
        this.setSize(400, 450);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        JPanel pnlMain = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlMain.setBackground(Color.WHITE);

        tenDe = new InputForm("Tên đề thi");
        thoiGian = new InputForm("Thời gian làm bài (phút)");
        monHoc = new InputForm("Môn học");

        pnlMain.add(tenDe);
        pnlMain.add(thoiGian);
        pnlMain.add(monHoc);

        JPanel pnlButtons = new JPanel();
        pnlButtons.setBackground(Color.WHITE);
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        
        if (!type.equals("view")) pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);

        this.add(pnlMain, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);
        
        btnHuy.addActionListener(e -> dispose());
        this.setVisible(true);
    }
}