package GUI.Panel;

import java.awt.*;
import javax.swing.*;

public class TrangChu extends JPanel {
    public TrangChu() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("TRANG CHỦ", SwingConstants.CENTER);
        lbl.setFont(new Font("Roboto", Font.BOLD, 30));
        this.add(lbl, BorderLayout.CENTER);
    }
}