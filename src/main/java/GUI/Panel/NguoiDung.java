package GUI.Panel;
import java.awt.*;
import javax.swing.*;

public class NguoiDung extends JPanel {
    public NguoiDung() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("QUẢN LÝ NGƯỜI DÙNG", SwingConstants.CENTER);
        lbl.setFont(new Font("Roboto", Font.BOLD, 30));
        this.add(lbl, BorderLayout.CENTER);
    }
}