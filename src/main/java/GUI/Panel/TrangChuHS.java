package GUI.Panel;

import GUI.Main;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TrangChuHS extends JPanel {

    public TrangChuHS() {
        initComponent();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(250, 250, 250));

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBackground(Color.WHITE);
        pnlCenter.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel lblWelcome = new JLabel("CHÀO MỪNG BẠN ĐẾN VỚI HỆ THỐNG QUẢN LÝ QUIZ");
        lblWelcome.setFont(new Font("Roboto", Font.BOLD, 28));
        lblWelcome.setForeground(new Color(1, 87, 155));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblName = new JLabel("Người dùng: " + Main.user.getHoten() + " (" + Main.user.getUsername() + ")");
        lblName.setFont(new Font("Roboto", Font.PLAIN, 18));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Chúc bạn có một kỳ thi đạt kết quả tốt nhất!");
        lblSub.setFont(new Font("Roboto", Font.ITALIC, 16));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlCenter.add(lblWelcome);
        pnlCenter.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlCenter.add(lblName);
        pnlCenter.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlCenter.add(lblSub);

        this.add(pnlCenter, BorderLayout.CENTER);
    }
}
