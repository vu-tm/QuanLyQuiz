package GUI.Panel;
import java.awt.*;
import javax.swing.*;

public class DeThi extends JPanel {
    public DeThi() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("QUẢN LÝ ĐỀ THI", SwingConstants.CENTER);
        lbl.setFont(new Font("Roboto", Font.BOLD, 30));
        this.add(lbl, BorderLayout.CENTER);
    }
}