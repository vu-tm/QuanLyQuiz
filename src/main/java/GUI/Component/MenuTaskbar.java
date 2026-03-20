package GUI.Component;

import GUI.Main;
import GUI.Panel.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuTaskbar extends JPanel {

    String[][] getSt = {
        {"Trang chủ", "home.svg"},
        {"Môn học", "subject.svg"},
        {"Câu hỏi", "question.svg"},
        {"Độ khó", "dokho.svg"},
        {"Đề thi", "dethi.svg"},
        {"Kỳ thi", "kythi.svg"},
        {"Lớp học", "class.svg"},
        {"Người dùng", "nguoidung.svg"},
        {"Nhóm quyền", "nhomquyen.svg"},
        {"Bài thi", "baithi.svg"},
        {"Phân công", "phancong.svg"},
        {"Đăng xuất", "logout.svg"}
    };

    Main main;
    public itemTaskbar[] listitem;
    JScrollPane scrollPane;
    JPanel pnlCenter, pnlTop, pnlBottom, bar1, bar2, bar3, bar4;
    Color DefaultColor = new Color(255, 255, 255);
    Color LineColor = new Color(204, 214, 219);

    public MenuTaskbar(Main main) {
        this.main = main;
        initComponent();
    }

    private void initComponent() {
        listitem = new itemTaskbar[getSt.length];
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(DefaultColor);

        // TOP AREA
        pnlTop = new JPanel(new BorderLayout());
        pnlTop.setPreferredSize(new Dimension(250, 80));
        pnlTop.setBackground(DefaultColor);
        this.add(pnlTop, BorderLayout.NORTH);

        JLabel lblLogo = new JLabel("QUẢN LÝ QUIZ", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Roboto", Font.BOLD, 18));
        lblLogo.setForeground(new Color(1, 87, 155));
        pnlTop.add(lblLogo, BorderLayout.CENTER);

        bar1 = new JPanel();
        bar1.setBackground(LineColor);
        bar1.setPreferredSize(new Dimension(1, 0));
        pnlTop.add(bar1, BorderLayout.EAST);
        bar2 = new JPanel();
        bar2.setBackground(LineColor);
        bar2.setPreferredSize(new Dimension(0, 1));
        pnlTop.add(bar2, BorderLayout.SOUTH);

        // CENTER AREA
        pnlCenter = new JPanel();
        pnlCenter.setPreferredSize(new Dimension(230, 600));
        pnlCenter.setBackground(DefaultColor);
        pnlCenter.setLayout(new FlowLayout(0, 0, 5));

        bar3 = new JPanel();
        bar3.setBackground(LineColor);
        bar3.setPreferredSize(new Dimension(1, 0));
        this.add(bar3, BorderLayout.EAST);

        scrollPane = new JScrollPane(pnlCenter, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(new EmptyBorder(5, 10, 0, 10));
        this.add(scrollPane, BorderLayout.CENTER);

        // BOTTOM AREA
        pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setPreferredSize(new Dimension(250, 55));
        pnlBottom.setBackground(DefaultColor);
        this.add(pnlBottom, BorderLayout.SOUTH);

        bar4 = new JPanel();
        bar4.setBackground(LineColor);
        bar4.setPreferredSize(new Dimension(1, 0));
        pnlBottom.add(bar4, BorderLayout.EAST);

        for (int i = 0; i < getSt.length; i++) {
            listitem[i] = new itemTaskbar(getSt[i][1], getSt[i][0]);
            if (i == getSt.length - 1) {
                pnlBottom.add(listitem[i]);
            } else {
                pnlCenter.add(listitem[i]);
            }

            final int index = i;
            listitem[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    pnlMenuTaskbarMousePress(evt);

                    switch (index) {
                        case 0:
                            main.setPanel(new TrangChu());
                            break;
                        case 1:
                            main.setPanel(new MonHoc());
                            break;
                        case 2:
                            main.setPanel(new CauHoi());
                            break;
                        case 3:
                            main.setPanel(new DoKho());
                            break;
                        case 4:
                            main.setPanel(new DeThi());
                            break;
                        case 5:
                            main.setPanel(new KyThi());
                            break;
                        case 6:
                            main.setPanel(new LopHoc());
                            break;
                        case 7:
                            main.setPanel(new NguoiDung());
                            break;
                        case 8:
                            main.setPanel(new NhomQuyen());
                            break;
                        case 9:
                            main.setPanel(new BaiThi());
                            break;
                        case 10:
                            main.setPanel(new PhanCong());
                            break;
                        case 11:
                            if (JOptionPane.showConfirmDialog(null, "Đăng xuất?", "Xác nhận", 0) == 0) {
                                System.exit(0);
                            }
                            break;
                    }
                }
            });
        }
        listitem[0].isSelected = true;
        listitem[0].setBackground(new Color(187, 222, 251));
    }

    public void pnlMenuTaskbarMousePress(MouseEvent evt) {
        for (int i = 0; i < getSt.length; i++) {
            if (evt.getSource() == listitem[i]) {
                listitem[i].isSelected = true;
                listitem[i].setBackground(new Color(187, 222, 251));
                listitem[i].setForeground(new Color(1, 87, 155));
            } else {
                listitem[i].isSelected = false;
                listitem[i].setBackground(DefaultColor);
                listitem[i].setForeground(new Color(96, 125, 139));
            }
        }
    }
}
