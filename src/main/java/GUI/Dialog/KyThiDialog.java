package GUI.Dialog;

import BUS.KyThiBUS;
import DTO.KyThiDTO;
import GUI.Component.InputDate;
import GUI.Component.InputForm;
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
    private JButton btnLuu, btnHuy;
    private KyThiBUS bus = new KyThiBUS();
    private KyThi parent;
    private KyThiDTO currentDTO;

    public KyThiDialog(KyThi parent, JFrame owner, String title, boolean modal, String type, KyThiDTO kt) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = kt;
        init(type);
    }

    private void init(String type) {
        this.setSize(450, 500);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        JPanel pnlMain = new JPanel(new GridLayout(3, 1, 10, 10));
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
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
        pnlMain.add(thoiGianBD);
        pnlMain.add(thoiGianKT);

        JPanel pnlButtons = new JPanel();
        pnlButtons.setBackground(Color.WHITE);
        btnLuu = new JButton(type.equals("create") ? "Thêm mới" : "Lưu thay đổi");
        btnHuy = new JButton("Hủy bỏ");

        if (!type.equals("view")) pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);

        this.add(pnlMain, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);

        if(type.equals("view")) {
            tenKyThi.setEditable(false);
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
            JOptionPane.showMessageDialog(this, "Tên kỳ thi không được để trống!");
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