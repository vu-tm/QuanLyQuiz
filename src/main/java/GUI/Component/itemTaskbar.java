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

    private JLabel lblIcon, pnlContent, pnlContent1;
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

    public itemTaskbar(String linkIcon, String content, String content2, int n) {
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(new Color(207, 159, 255));
        this.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
        this.addMouseListener(this);

        lblIcon = new JLabel();
        lblIcon.setPreferredSize(new Dimension(80, 80));
        lblIcon.setBorder(new EmptyBorder(0, 15, 0, 0));
        try {
            lblIcon.setIcon(new FlatSVGIcon("icon/" + linkIcon, 40, 40));
        } catch (Exception e) {
            System.err.println("Loi load icon: " + linkIcon);
        }
        this.add(lblIcon, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        center.setBorder(new EmptyBorder(15, 0, 0, 0));
        center.setOpaque(false);
        this.add(center, BorderLayout.CENTER);

        // Số lớn (ví dụ: "42")
        pnlContent = new JLabel(content);
        pnlContent.setPreferredSize(new Dimension(200, 30));
        pnlContent.putClientProperty("FlatLaf.style", "font: 250% $semibold.font");
        pnlContent.setForeground(new Color(26, 26, 26));
        center.add(pnlContent);

        // Tiêu đề nhỏ (ví dụ: "Tổng số đề thi")
        pnlContent1 = new JLabel(content2);
        pnlContent1.setPreferredSize(new Dimension(200, 25));
        pnlContent1.putClientProperty("FlatLaf.style", "font: 120% $medium.font");
        pnlContent1.setForeground(new Color(80, 80, 80));
        center.add(pnlContent1);
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
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // Khi active
    public void setForeground(Color c) {
        if (pnlContent != null) {
            pnlContent.setForeground(c);
        }
    }
}
