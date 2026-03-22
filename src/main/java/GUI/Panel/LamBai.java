package GUI.Panel;
import java.awt.*;
import javax.swing.*;

public class LamBai extends JPanel {
    public LamBai() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("QUẢN LÝ BÀI THI", SwingConstants.CENTER);
        lbl.setFont(new Font("Roboto", Font.BOLD, 30));
        this.add(lbl, BorderLayout.CENTER);
    }
}