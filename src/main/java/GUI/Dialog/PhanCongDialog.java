package GUI.Dialog;

import BUS.MonHocBUS;
import BUS.NguoiDungBUS;
import BUS.PhanCongBUS;
import DTO.MonHocDTO;
import DTO.NguoiDungDTO;
import DTO.PhanCongDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.SelectForm;
import GUI.Panel.PhanCong;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PhanCongDialog extends JDialog {

    private SelectForm cbxNguoiDung, cbxMonHoc;
    private ButtonCustom btnLuu, btnHuy;
    private JPanel pnlContent, pnlButtons;
    
    private MonHocBUS mhBUS = new MonHocBUS();
    private NguoiDungBUS ndBUS = new NguoiDungBUS();
    private PhanCongBUS pcBUS = new PhanCongBUS();
    
    private PhanCong parent;
    private PhanCongDTO currentDTO;
    private String currentType;

    public PhanCongDialog(PhanCong parent, JFrame owner, String title, boolean modal, String type, PhanCongDTO pc) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = pc;
        this.currentType = type;
        init();
    }

    private void init() {
        this.setSize(new Dimension(450, 350));
        this.setLayout(new BorderLayout(0, 0));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        pnlContent = new JPanel(new GridLayout(2, 1, 0, 20));
        pnlContent.setBorder(new EmptyBorder(30, 30, 30, 30));
        pnlContent.setBackground(Color.WHITE);

        List<NguoiDungDTO> listGV = ndBUS.getAll().stream()
                .filter(u -> u.getManhomquyen() == 2)
                .collect(Collectors.toList());
        String[] dsGV = new String[listGV.size() + 1];
        dsGV[0] = "Chọn giảng viên";
        for (int i = 0; i < listGV.size(); i++) {
            dsGV[i + 1] = listGV.get(i).getId() + " - " + listGV.get(i).getHoten();
        }
        cbxNguoiDung = new SelectForm("Giảng viên", dsGV);

        ArrayList<MonHocDTO> listMH = mhBUS.getAll();
        String[] dsMH = new String[listMH.size() + 1];
        dsMH[0] = "Chọn môn học";
        for (int i = 0; i < listMH.size(); i++) {
            dsMH[i + 1] = listMH.get(i).getMamonhoc() + " - " + listMH.get(i).getTenmonhoc();
        }
        cbxMonHoc = new SelectForm("Môn học", dsMH);

        if (currentDTO != null) {
            cbxNguoiDung.setSelectedItem(currentDTO.getManguoidung() + " - " + ndBUS.getById(currentDTO.getManguoidung()).getHoten());
            cbxMonHoc.setSelectedItem(currentDTO.getMamonhoc() + " - " + mhBUS.getTenById(currentDTO.getMamonhoc()));
        }

        pnlContent.add(cbxNguoiDung);
        pnlContent.add(cbxMonHoc);

        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        btnLuu = new ButtonCustom(currentType.equals("create") ? "Thêm mới" : "Lưu thay đổi", "success", 14);
        btnHuy = new ButtonCustom("Hủy bỏ", "danger", 14);

        if (currentType.equals("view")) {
            cbxNguoiDung.setDisable();
            cbxMonHoc.setDisable();
            btnHuy.setText("Đóng");
        } else {
            pnlButtons.add(btnLuu);
        }
        pnlButtons.add(btnHuy);

        btnLuu.addActionListener(e -> actionSave());
        btnHuy.addActionListener(e -> dispose());

        this.add(pnlContent, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void actionSave() {
        if (cbxNguoiDung.getSelectedIndex() == 0 || cbxMonHoc.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ thông tin!");
            return;
        }

        String mand = ((String) cbxNguoiDung.getValue()).split(" - ")[0];
        int mamh = Integer.parseInt(((String) cbxMonHoc.getValue()).split(" - ")[0]);
        PhanCongDTO newDTO = new PhanCongDTO(mamh, mand);

        if (currentType.equals("create")) {
            if (pcBUS.add(newDTO)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                parent.loadDataTable(pcBUS.getAll());
                dispose();
            } else JOptionPane.showMessageDialog(this, "Đã tồn tại phân công này!");
        } else {
            if (pcBUS.update(currentDTO, newDTO)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parent.loadDataTable(pcBUS.getAll());
                dispose();
            } else JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
}