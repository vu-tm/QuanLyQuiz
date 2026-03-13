package GUI.Dialog;

import BUS.KyThiBUS;
import DTO.KyThiDTO;
import GUI.Component.InputDate;
import GUI.Component.InputForm;
import GUI.Component.ButtonCustom;
import GUI.Panel.KyThi;
import helper.Validation;
import java.awt.*;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class KyThiDialog extends JDialog {
    private InputForm tenKyThi;
    private InputDate thoiGianBD, thoiGianKT;
    private ButtonCustom btnLuu, btnHuy;
    private KyThiBUS bus = new KyThiBUS();
    private KyThi parent;
    private KyThiDTO currentDTO;
    private JPanel pnlMain, pnlButtons;

    public KyThiDialog(KyThi parent, JFrame owner, String title, boolean modal, String type, KyThiDTO kt) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = kt;
        this.setTitle(title);
        init(type);
    }

    private void init(String type) {
        this.setSize(500, 550);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlMain.setBackground(Color.WHITE);

        tenKyThi = new InputForm("Tên kỳ thi");
        thoiGianBD = new InputDate("Thời gian bắt đầu");
        thoiGianKT = new InputDate("Thời gian kết thúc");

        if (currentDTO != null) {
            tenKyThi.setText(currentDTO.getTenkythi());
            thoiGianBD.setDate(currentDTO.getThoigianbatdau());
            thoiGianKT.setDate(currentDTO.getThoigianketthuc());
        }

        pnlMain.add(tenKyThi);
        pnlMain.add(Box.createVerticalStrut(15));
        pnlMain.add(thoiGianBD);
        pnlMain.add(Box.createVerticalStrut(15));
        pnlMain.add(thoiGianKT);

        JScrollPane scrollPane = new JScrollPane(pnlMain);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);

        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnLuu = new ButtonCustom(type.equals("create") ? "Thêm mới" : "Lưu thay đổi", "success", 14);
        btnHuy = new ButtonCustom("Huỷ bỏ", "danger", 14);

        if (!type.equals("view")) {
            pnlButtons.add(btnLuu);
        }
        pnlButtons.add(btnHuy);

        this.add(pnlButtons, BorderLayout.SOUTH);

        if(type.equals("view")) {
            tenKyThi.setDisable();
            thoiGianBD.setDisable();
            thoiGianKT.setDisable();
        }

        btnLuu.addActionListener(e -> {
            if (validateInput()) {
                try {
                    KyThiDTO kt = new KyThiDTO();
                    kt.setTenkythi(tenKyThi.getText());
                    kt.setThoigianbatdau(new Timestamp(thoiGianBD.getDate().getTime()));
                    kt.setThoigianketthuc(new Timestamp(thoiGianKT.getDate().getTime()));
                    
                    if (type.equals("create")) {
                        if (bus.add(kt)) {
                            JOptionPane.showMessageDialog(this, "Thêm thành công!");
                            parent.loadDataTable(bus.getAll());
                            dispose();
                        }
                    } else {
                        kt.setMakythi(currentDTO.getMakythi());
                        if (bus.update(kt)) {
                            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                            parent.loadDataTable(bus.getAll());
                            dispose();
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi định dạng dữ liệu!");
                }
            }
        });

        btnHuy.addActionListener(e -> dispose());
        
        this.setVisible(true);
    }

    private boolean validateInput() {
        if (Validation.isEmpty(tenKyThi.getText())) {
            JOptionPane.showMessageDialog(this, "Tên kỳ thi không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            Date start = thoiGianBD.getDate();
            Date end = thoiGianKT.getDate();

            if (start == null || end == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ thời gian bắt đầu và kết thúc!");
                return false;
            }

            if (end.before(start)) {
                JOptionPane.showMessageDialog(this, "Thời gian kết thúc phải lớn hơn thời gian bắt đầu!", "Lỗi !", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu ngày tháng không hợp lệ!");
            return false;
        }
        
        return true;
    }
}