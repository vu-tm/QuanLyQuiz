package GUI;

import BUS.NguoiDungBUS;
import DTO.NguoiDungDTO;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DangNhap extends JFrame {

    private JPanel pnlMain;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblTitle, lblUser, lblPass, lblMsg;
    private NguoiDungBUS userBUS = new NguoiDungBUS();

    public DangNhap() {
        setupLaf();
        initComponent();
        this.setTitle("Đăng nhập - Hệ thống QuanLyQuiz");
        this.setSize(new Dimension(400, 500));
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
        pnlMain = new JPanel();
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(30, 40, 30, 40));
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));

        lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(new Color(1, 87, 155));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblUser = new JLabel("Tên đăng nhập");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập username");
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        lblPass = new JLabel("Mật khẩu");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mật khẩu");
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        lblMsg = new JLabel(" ");
        lblMsg.setForeground(Color.RED);
        lblMsg.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(new Color(1, 87, 155));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Roboto", Font.BOLD, 16));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlMain.add(lblTitle);
        pnlMain.add(Box.createRigidArea(new Dimension(0, 30)));
        pnlMain.add(lblUser);
        pnlMain.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlMain.add(txtUsername);
        pnlMain.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlMain.add(lblPass);
        pnlMain.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlMain.add(txtPassword);
        pnlMain.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlMain.add(lblMsg);
        pnlMain.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlMain.add(btnLogin);

        this.add(pnlMain);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        txtPassword.addKeyListener(new KeyAdapter() {
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
        String pass = new String(txtPassword.getPassword());

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
