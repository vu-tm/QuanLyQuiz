package GUI.Component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class itemTaskbar extends JPanel implements MouseListener {

    private Color FontColor = new Color(96, 125, 139);
    private Color ColorBlack = new Color(26, 26, 26);
    private Color DefaultColor = new Color(255, 255, 255);
    
    private JLabel lblIcon, pnlContent;
    public boolean isSelected = false;

    public itemTaskbar(String linkIcon, String content) {
        this.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 7));
        this.setPreferredSize(new Dimension(225, 45));
        this.setBackground(DefaultColor);
        // Bo góc
        this.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        this.addMouseListener(this);

        // Icon SVG
        lblIcon = new JLabel();
        lblIcon.setBorder(new EmptyBorder(0, 10, 0, 0));
        lblIcon.setPreferredSize(new Dimension(45, 30));
        try {
            lblIcon.setIcon(new FlatSVGIcon("icon/" + linkIcon, 24, 24));
        } catch (Exception e) {
            System.err.println("Lỗi load icon: " + linkIcon);
        }
        this.add(lblIcon);

        // Văn bản
        pnlContent = new JLabel(content);
        pnlContent.setPreferredSize(new Dimension(155, 30));
        pnlContent.putClientProperty("FlatLaf.style", "font: 145% $medium.font");
        pnlContent.setForeground(ColorBlack);
        this.add(pnlContent);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!isSelected) {
            setBackground(new Color(235, 237, 240));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!isSelected) {
            setBackground(DefaultColor);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    // Khi active
    public void setForeground(Color c) {
        if (pnlContent != null) pnlContent.setForeground(c);
    }
}