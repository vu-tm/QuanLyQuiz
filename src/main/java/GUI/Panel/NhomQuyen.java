package GUI.Panel;
import java.awt.*;
import javax.swing.*;

public class NhomQuyen extends JPanel {
    public NhomQuyen() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("QUẢN LÝ NHÓM QUYỀN", SwingConstants.CENTER);
        lbl.setFont(new Font("Roboto", Font.BOLD, 30));
        this.add(lbl, BorderLayout.CENTER);
    }
}