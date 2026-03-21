package GUI;

import BUS.NguoiDungBUS;
import DTO.NguoiDungDTO;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DangNhap extends JFrame {

    private JPanel pnlMain;
    private InputForm txtUsername;
    private InputForm txtPassword;
    private ButtonCustom btnLogin;
    private JLabel lblTitle, lblMsg;
    private NguoiDungBUS userBUS = new NguoiDungBUS();

    public DangNhap() {
        setupLaf();
        initComponent();
        this.setTitle("Đăng nhập - Hệ thống QuanLyQuiz");
        this.setSize(new Dimension(850, 500));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    private void setupLaf() {
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatIntelliJLaf.setup();
    }

    private void initComponent() {
        this.setLayout(new GridLayout(1, 2));

        // LEFT PANEL - Gradient with Logo & Text
        JPanel pnlLeft = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(1, 87, 155);
                Color color2 = new Color(0, 153, 204);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        JPanel pnlLeftContent = new JPanel();
        pnlLeftContent.setLayout(new BoxLayout(pnlLeftContent, BoxLayout.Y_AXIS));
        pnlLeftContent.setOpaque(false);

        JLabel lblIcon = new JLabel();
        lblIcon.setIcon(new FlatSVGIcon("icon/baithi.svg", 150, 150));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrand = new JLabel("QUẢN LÝ QUIZ");
        lblBrand.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 32));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubBrand = new JLabel("Hệ thống thi trắc nghiệm");
        lblSubBrand.setFont(new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 16));
        lblSubBrand.setForeground(new Color(224, 247, 250));
        lblSubBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlLeftContent.add(lblIcon);
        pnlLeftContent.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlLeftContent.add(lblBrand);
        pnlLeftContent.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlLeftContent.add(lblSubBrand);

        pnlLeft.add(pnlLeftContent);

        // RIGHT PANEL - Login Form
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBackground(Color.WHITE);

        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setPreferredSize(new Dimension(300, 350));

        lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 28));
        lblTitle.setForeground(new Color(1, 87, 155));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername = new InputForm("Tên đăng nhập");
        txtUsername.getTxtForm().putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập username");
        txtUsername.setBackground(Color.WHITE);

        txtPassword = new InputForm("Mật khẩu", "password");
        txtPassword.getTxtPass().putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");
        txtPassword.setBackground(Color.WHITE);

        lblMsg = new JLabel(" ");
        lblMsg.setForeground(Color.RED);
        lblMsg.setFont(new Font(FlatRobotoFont.FAMILY, Font.ITALIC, 12));
        lblMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin = new ButtonCustom("Đăng nhập", "success", 16, 300, 45);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.putClientProperty("JButton.buttonType", "roundRect");

        pnlForm.add(lblTitle);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 40)));
        pnlForm.add(txtUsername);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlForm.add(txtPassword);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlForm.add(lblMsg);
        pnlForm.add(Box.createRigidArea(new Dimension(0, 30)));
        pnlForm.add(btnLogin);

        pnlRight.add(pnlForm);

        this.add(pnlLeft);
        this.add(pnlRight);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        txtPassword.getTxtPass().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
    }

    private void handleLogin() {
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getPass();

        if (user.isEmpty() || pass.isEmpty()) {
            lblMsg.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        NguoiDungDTO result = userBUS.login(user, pass);
        if (result != null) {
            Main.user = result;
            new Main().setVisible(true);
            this.dispose();
        } else {
            lblMsg.setText("Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DangNhap().setVisible(true);
        });
    }
}
