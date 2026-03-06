package GUI.Panel;
import java.awt.*;
import javax.swing.*;

public class CauHoi extends JPanel {
    public CauHoi() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("QUẢN LÝ CÂU HỎI", SwingConstants.CENTER);
        lbl.setFont(new Font("Roboto", Font.BOLD, 30));
        this.add(lbl, BorderLayout.CENTER);
    }
}