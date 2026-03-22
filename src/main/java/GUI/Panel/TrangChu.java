package GUI.Panel;

import java.awt.*;
import javax.swing.*;

public class TrangChu extends JPanel {

    public TrangChu() {
        setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                java.net.URL imgURL = TrangChu.class.getResource("/img/dashboard.jpg");

                if (imgURL != null) {
                    ImageIcon imageIcon = new ImageIcon(imgURL);
                    Image image = imageIcon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(245, 245, 245));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        centerPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG", SwingConstants.CENTER);

        welcomeLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBackground(new Color(60, 63, 65, 200));
        welcomeLabel.setOpaque(true);
        welcomeLabel.setPreferredSize(new Dimension(0, 80));
        centerPanel.add(welcomeLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}
