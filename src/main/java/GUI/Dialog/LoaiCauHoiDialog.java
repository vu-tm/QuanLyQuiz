package GUI.Dialog;

import BUS.LoaiCauHoiBUS;
import DTO.LoaiCauHoiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.SelectForm;
import GUI.Panel.LoaiCauHoi;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoaiCauHoiDialog extends JDialog {

    private SelectForm tenLoai;
    private ButtonCustom btnLuu, btnHuy;
    private LoaiCauHoiBUS bus = new LoaiCauHoiBUS();
    private LoaiCauHoi parent;
    private LoaiCauHoiDTO currentDTO;
    private JPanel pnlMain, pnlButtons;

    public LoaiCauHoiDialog(LoaiCauHoi parent, JFrame owner, String title, boolean modal, String type, LoaiCauHoiDTO dto) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = dto;
        this.setTitle(title);
        init(type);
    }

    private void init(String type) {
        this.setSize(450, 300);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        initPnlMain(type);
        initPnlButtons(type);

        this.add(pnlMain, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);

        if (type.equals("view")) {
            tenLoai.setDisable();
        }

        this.setVisible(true);
    }

    private void initPnlMain(String type) {
        pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlMain.setBackground(Color.WHITE);

        tenLoai = new SelectForm("Loại câu hỏi", LoaiCauHoiBUS.DS_LOAI_HO_TRO);

        if (currentDTO != null) {
            tenLoai.setSelectedItem(bus.normalizeTenLoai(currentDTO.getTenloai()));
        }

        pnlMain.add(tenLoai);
    }

    private void initPnlButtons(String type) {
        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnLuu = new ButtonCustom(type.equals("create") ? "Thêm mới" : "Lưu thay đổi", "success", 14);
        btnHuy = new ButtonCustom("Huỷ bỏ", "danger", 14);

        btnLuu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                luuLoaiCauHoi(type);
            }
        });

        btnHuy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        if (!type.equals("view")) {
            pnlButtons.add(btnLuu);
        }
        pnlButtons.add(btnHuy);
    }

    private void luuLoaiCauHoi(String type) {
        String ten = (String) tenLoai.getSelectedItem();
        LoaiCauHoiDTO dto = new LoaiCauHoiDTO();
        dto.setTenloai(ten);

        boolean result;
        if (type.equals("create")) {
            result = bus.add(dto);
            if (result) {
                JOptionPane.showMessageDialog(null, "Thêm thành công!");
            }
        } else {
            dto.setMaloai(currentDTO.getMaloai());
            result = bus.update(dto);
            if (result) {
                JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
            }
        }

        if (!result) {
            JOptionPane.showMessageDialog(null,
                    "Lưu thất bại. Chỉ hỗ trợ 3 loại: Trắc nghiệm, Điền khuyết, Đúng sai và không cho phép trùng lặp.",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        parent.loadDataTable(bus.getAll());
        dispose();
    }
}