package GUI;

import BUS.NguoiDungBUS;
import DTO.NguoiDungDTO;
import GUI.Component.InputForm;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame implements KeyListener {

    JPanel pnlMain, pnlLogIn;
    JLabel lblImage, lbl3, lbl6;
    InputForm txtUsername, txtPassword;

    Color FontColor = new Color(96, 125, 139);

    public Login() {
        initComponent();
        txtUsername.setText("admin");
        txtPassword.setPass("123");
    }

    private void initComponent() {
        this.setVisible(false);
        this.setSize(new Dimension(1000, 420));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(0, 0));
        this.setTitle("Đăng nhập");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        imgIntro();

        pnlMain = new JPanel();
        pnlMain.setBackground(Color.white);
        pnlMain.setBorder(new EmptyBorder(40, 0, 0, 0));
        pnlMain.setPreferredSize(new Dimension(500, 420));
        pnlMain.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 8));

        lbl3 = new JLabel("ĐĂNG NHẬP VÀO HỆ THỐNG");
        lbl3.setFont(new Font(FlatRobotoFont.FAMILY_SEMIBOLD, Font.BOLD, 20));
        pnlMain.add(lbl3);

        JPanel paneldn = new JPanel();
        paneldn.setBackground(Color.WHITE);
        paneldn.setPreferredSize(new Dimension(400, 160));
        paneldn.setLayout(new GridLayout(2, 1));

        txtUsername = new InputForm("Tên đăng nhập");
        paneldn.add(txtUsername);
        txtPassword = new InputForm("Mật khẩu", "password");
        paneldn.add(txtPassword);

        txtUsername.getTxtForm().addKeyListener(this);
        txtPassword.getTxtPass().addKeyListener(this);

        pnlMain.add(paneldn);

        lbl6 = new JLabel("ĐĂNG NHẬP");
        lbl6.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 16));
        lbl6.setForeground(Color.white);

        pnlLogIn = new JPanel();
        pnlLogIn.putClientProperty(FlatClientProperties.STYLE, "arc: 99");
        pnlLogIn.setBackground(Color.BLACK);
        pnlLogIn.setPreferredSize(new Dimension(380, 45));
        pnlLogIn.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 11));

        pnlLogIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                pnlLogIn.setBackground(FontColor);
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                checkLogin();
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                pnlLogIn.setBackground(Color.BLACK);
            }
        });
        pnlLogIn.add(lbl6);

        pnlMain.add(pnlLogIn);

        this.add(pnlMain, BorderLayout.EAST);
    }

    public void checkLogin() {
        String usernameCheck = txtUsername.getText().trim();
        String passwordCheck = txtPassword.getPass().trim();

        if (usernameCheck.isEmpty() || passwordCheck.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập thông tin đầy đủ", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tìm user theo username
        NguoiDungBUS bus = new NguoiDungBUS();
        ArrayList<NguoiDungDTO> list = bus.getAll();
        NguoiDungDTO nd = null;
        for (NguoiDungDTO u : list) {
            if (u.getUsername().equals(usernameCheck)) {
                nd = u;
                break;
            }
        }

        if (nd == null) {
            JOptionPane.showMessageDialog(this,
                    "Tài khoản của bạn không tồn tại trên hệ thống", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (nd.getTrangthai() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Tài khoản của bạn đang bị khóa", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!nd.getMatkhau().equals(passwordCheck)) {
            JOptionPane.showMessageDialog(this,
                    "Mật khẩu không khớp", "Cảnh báo!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Đăng nhập thành công
        this.dispose();
        try {
            Main main = new Main(nd);
            main.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void imgIntro() {
        JPanel bo = new JPanel();
        bo.setBorder(new EmptyBorder(3, 10, 5, 5));
        bo.setPreferredSize(new Dimension(500, 740));
        bo.setBackground(Color.white);
        bo.setLayout(new BorderLayout());
        this.add(bo, BorderLayout.WEST);

        lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);

        java.net.URL imgURL = Login.class.getResource("/img/login.png");
        if (imgURL != null) {
            Image scaled = new ImageIcon(imgURL).getImage()
                    .getScaledInstance(420, 380, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaled));
        }

        bo.add(lblImage, BorderLayout.CENTER);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            checkLogin();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);
        FlatIntelliJLaf.registerCustomDefaultsSource("style");
        FlatIntelliJLaf.setup();
        UIManager.put("PasswordField.showRevealButton", true);
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
