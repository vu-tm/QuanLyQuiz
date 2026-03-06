package GUI.Panel;
import java.awt.*;
import javax.swing.*;

public class LopHoc extends JPanel {
    public LopHoc() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("QUẢN LÝ LỚP HỌC", SwingConstants.CENTER);
        lbl.setFont(new Font("Roboto", Font.BOLD, 30));
        this.add(lbl, BorderLayout.CENTER);
    }
}