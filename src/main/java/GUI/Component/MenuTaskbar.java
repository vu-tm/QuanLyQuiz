package GUI.Component;

import BUS.NguoiDungBUS;
import DAO.ChiTietQuyenDAO;
import DTO.ChiTietQuyenDTO;
import DTO.NguoiDungDTO;
import GUI.Login;
import GUI.Main;
import GUI.Panel.*;
import GUI.ThongKe.ThongKe;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuTaskbar extends JPanel {

    String[][] getSt = {
        {"Trang chủ", "home.svg", "trangchu"},
        {"Môn học", "subject.svg", "4"},
        {"Câu hỏi", "question.svg", "1"},
        {"Độ khó", "dokho.svg", "10"},
        {"Đề thi", "dethi.svg", "2"},
        {"Kỳ thi", "kythi.svg", "3"},
        {"Lớp học", "class.svg", "5"},
        {"Người dùng", "nguoidung.svg", "6"},
        {"Nhóm quyền", "nhomquyen.svg", "7"},
        {"Bài thi", "baithi.svg", "11"},
        {"Phân công", "phancong.svg", "8"},
        {"Thống kê", "thongke.svg", "9"},
        {"Làm bài", "lambai.svg", "0"},
        {"Đăng xuất", "logout.svg", "dangxuat"}
    };

    Main main;
    NguoiDungDTO nguoiDung;

    ArrayList<ChiTietQuyenDTO> listQuyen;
    public itemTaskbar[] listitem;
    JScrollPane scrollPane;
    JPanel pnlCenter, pnlTop, pnlBottom, bar1, bar2, bar3, bar4;
    Color DefaultColor = new Color(255, 255, 255);
    Color LineColor = new Color(204, 214, 219);

    public MenuTaskbar(Main main, NguoiDungDTO nguoiDung) {
        this.main = main;
        this.nguoiDung = nguoiDung;
        this.listQuyen = ChiTietQuyenDAO.getInstance()
                .selectAll(nguoiDung.getManhomquyen());
        initComponent();
    }

    private void initComponent() {
        listitem = new itemTaskbar[getSt.length];
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(DefaultColor);

        // TOP AREA
        pnlTop = new JPanel(new BorderLayout());
        pnlTop.setPreferredSize(new Dimension(250, 80));
        pnlTop.setBackground(new Color(48, 103, 204));
        this.add(pnlTop, BorderLayout.NORTH);
        NguoiDungBUS ndBUS = new NguoiDungBUS();
        String tenNhomQuyen = ndBUS.getTenNhomQuyen(nguoiDung.getManhomquyen());
        JLabel lblLogo = new JLabel(tenNhomQuyen.toUpperCase(), SwingConstants.CENTER);
        lblLogo.setFont(new Font("Roboto", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);
        pnlTop.add(lblLogo, BorderLayout.CENTER);
        bar1 = new JPanel();
        bar1.setBackground(LineColor);
        bar1.setPreferredSize(new Dimension(1, 0));
        pnlTop.add(bar1, BorderLayout.EAST);

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
                if (i != 0 && !checkRole(getSt[i][2])) {
                    listitem[i].setVisible(false);
                }
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
                            main.setPanel(new MonHoc(main));
                            break;
                        case 2:
                            main.setPanel(new CauHoi());
                            break;
                        case 3:
                            main.setPanel(new DoKho(main));
                            break;
                        case 4:
                            main.setPanel(new DeThi(main));
                            break;
                        case 5:
                            main.setPanel(new KyThi(main));
                            break;
                        case 6:
                            main.setPanel(new LopHoc(main));
                            break;
                        case 7:
                            main.setPanel(new NguoiDung(main));
                            break;
                        case 8:
                            main.setPanel(new NhomQuyen(main));
                            break;
                        case 9:
                            main.setPanel(new BaiThi());
                            break;
                        case 10:
                            main.setPanel(new PhanCong(main));
                            break;
                        case 11:
                            main.setPanel(new ThongKe());
                            break;
                        case 12:
                            main.setPanel(new LamBai());
                            break;
                        case 13:
                            if (JOptionPane.showConfirmDialog(null, "Đăng xuất?", "Xác nhận", 0) == 0) {
                                main.dispose();
                                new Login().setVisible(true);
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
        this.revalidate();
        this.repaint();
    }

    public boolean checkRole(String maCN) {
        for (ChiTietQuyenDTO q : listQuyen) {
            if (q.getHanhdong().equalsIgnoreCase("view")
                    && maCN.equals(q.getMachucnang())) {
                return true;
            }
        }
        return false;
    }
}
