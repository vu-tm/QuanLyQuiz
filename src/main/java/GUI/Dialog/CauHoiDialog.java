package GUI.Dialog;

import BUS.CauHoiBUS;
import BUS.DapAnBUS;
import BUS.DoKhoBUS;
import BUS.LoaiCauHoiBUS;
import BUS.MonHocBUS;
import DTO.CauHoiDTO;
import DTO.DapAnDTO;
import DTO.DoKhoDTO;
import DTO.LoaiCauHoiDTO;
import DTO.MonHocDTO;
import GUI.Component.ButtonCustom;
import GUI.Panel.CauHoi;
import helper.Validation;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class CauHoiDialog extends JDialog {

    private CauHoiBUS busCauHoi = new CauHoiBUS();
    private LoaiCauHoiBUS busLoaiCauHoi = new LoaiCauHoiBUS();
    private DoKhoBUS busDoKho = new DoKhoBUS();
    private MonHocBUS busMonHoc = new MonHocBUS();
    private DapAnBUS busDapAn = new DapAnBUS();

    private GUI.Main mainFrame;
    private CauHoi parent;
    private CauHoiDTO currentDTO;

    private JComboBox<LoaiCauHoiDTO> cmbLoaiCauHoi;
    private JComboBox<DoKhoDTO> cmbDoKho;
    private JComboBox<MonHocDTO> cmbMonHoc;
    private JTextArea txtaCauHoi;
    private JPanel pnlAnswers;

    private List<JRadioButton> rdAnswers = new ArrayList<>();
    private ButtonGroup btnGroup;
    private List<JTextField> txtAnswers = new ArrayList<>();
    private ButtonCustom btnLuu, btnHuy;

    private static final String TYPE_TRAC_NGHIEM = "Trắc nghiệm";
    private static final String TYPE_DUNG_SAI = "Đúng/Sai";
    private static final String TYPE_DIEN_KHUYET = "Điền khuyết";

    public CauHoiDialog(CauHoi parent, JFrame owner, String title, CauHoiDTO dto) {
        super(owner, title, true);
        this.mainFrame = (GUI.Main) owner;
        this.parent = parent;
        this.currentDTO = (dto != null && dto.getMacauhoi() != 0) ? dto : null;
        init();
    }

    private void init() {
        this.setSize(750, 750);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(new Color(248, 249, 250));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(25, 25, 20, 25));
        contentPanel.setBackground(new Color(248, 249, 250));

        contentPanel.add(createAttributesPanel());
        contentPanel.add(Box.createVerticalStrut(20));

        contentPanel.add(createContentPanel());
        contentPanel.add(Box.createVerticalStrut(20));

        pnlAnswers = new JPanel();
        pnlAnswers.setLayout(new BoxLayout(pnlAnswers, BoxLayout.Y_AXIS));
        pnlAnswers.setBackground(Color.WHITE);
        pnlAnswers.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(200, 200, 200), 1), " THIẾT LẬP ĐÁP ÁN ĐÚNG ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), new Color(13, 110, 253)));
        contentPanel.add(pnlAnswers);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        btnHuy = new ButtonCustom("Đóng", "danger", 14);
        btnLuu = new ButtonCustom(currentDTO == null ? "Thêm câu hỏi" : "Lưu thay đổi", "success", 14);

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> luuCauHoi());

        pnlFooter.add(btnLuu);
        pnlFooter.add(btnHuy);
        this.add(pnlFooter, BorderLayout.SOUTH);

        loadComboBoxData();
        if (currentDTO != null) {
            loadDataForEdit();
            if (this.getTitle().contains("độ khó")) {
                disableAllExceptDifficulty();
            }
        } else {
            if (cmbLoaiCauHoi.getItemCount() > 0) {
                updateAnswerUIByTen(cmbLoaiCauHoi.getItemAt(0).getTenloai());
            }
        }

        this.setVisible(true);
    }

    private JPanel createAttributesPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setOpaque(false);

        cmbLoaiCauHoi = new JComboBox<>();
        cmbDoKho = new JComboBox<>();
        cmbMonHoc = new JComboBox<>();

        cmbLoaiCauHoi.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LoaiCauHoiDTO) {
                    setText(((LoaiCauHoiDTO) value).getTenloai());
                }
                return this;
            }
        });

        cmbLoaiCauHoi.addActionListener(e -> {
            LoaiCauHoiDTO selected = (LoaiCauHoiDTO) cmbLoaiCauHoi.getSelectedItem();
            if (selected != null) {
                updateAnswerUIByTen(selected.getTenloai());
            }
        });

        cmbDoKho.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DoKhoDTO) {
                    setText(((DoKhoDTO) value).getTendokho());
                }
                return this;
            }
        });

        cmbMonHoc.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MonHocDTO) {
                    setText(((MonHocDTO) value).getTenmonhoc());
                }
                return this;
            }
        });

        panel.add(createComboWrapper("Loại câu hỏi", cmbLoaiCauHoi));
        panel.add(createComboWrapper("Độ khó", cmbDoKho));
        panel.add(createComboWrapper("Môn học", cmbMonHoc));

        return panel;
    }

    private void updateAnswerUIByTen(String tenLoai) {
        if (tenLoai.contains("Trắc")) {
            updateAnswerUI(TYPE_TRAC_NGHIEM);
        } else if (tenLoai.contains("Đúng")) {
            updateAnswerUI(TYPE_DUNG_SAI);
        } else {
            updateAnswerUI(TYPE_DIEN_KHUYET);
        }
    }

    private JPanel createComboWrapper(String label, JComboBox<?> combo) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p.add(lbl, BorderLayout.NORTH);
        combo.setPreferredSize(new Dimension(0, 35));
        p.add(combo, BorderLayout.CENTER);
        return p;
    }

    private JPanel createContentPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel("Nội dung câu hỏi:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtaCauHoi = new JTextArea(5, 0);
        txtaCauHoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtaCauHoi.setLineWrap(true);
        txtaCauHoi.setWrapStyleWord(true);
        txtaCauHoi.setBorder(new EmptyBorder(8, 8, 8, 8));
        JScrollPane sp = new JScrollPane(txtaCauHoi);
        sp.setBorder(new LineBorder(new Color(200, 200, 200)));
        p.add(lbl, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private void updateAnswerUI(String type) {
        pnlAnswers.removeAll();
        rdAnswers.clear();
        txtAnswers.clear();
        btnGroup = new ButtonGroup();
        pnlAnswers.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        if (type.equals(TYPE_TRAC_NGHIEM)) {
            renderMultipleChoice(gbc);
        } else if (type.equals(TYPE_DUNG_SAI)) {
            renderTrueFalse(gbc);
        } else if (type.equals(TYPE_DIEN_KHUYET)) {
            renderFillInBlank(gbc);
        }
        pnlAnswers.revalidate();
        pnlAnswers.repaint();
    }

    private void renderMultipleChoice(GridBagConstraints gbc) {
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            gbc.gridx = 0;
            gbc.weightx = 0;
            JRadioButton rd = new JRadioButton("Đúng");
            rd.setOpaque(false);
            btnGroup.add(rd);
            rdAnswers.add(rd);
            pnlAnswers.add(rd, gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            JTextField txt = new JTextField();
            txt.setPreferredSize(new Dimension(0, 35));
            txt.setBorder(BorderFactory.createTitledBorder("Nội dung đáp án " + labels[i]));
            txtAnswers.add(txt);
            pnlAnswers.add(txt, gbc);
            gbc.gridy++;
        }
        if (!rdAnswers.isEmpty()) {
            rdAnswers.get(0).setSelected(true);
        }
    }

    private void renderTrueFalse(GridBagConstraints gbc) {
        String[] options = {"Đúng", "Sai"};
        for (int i = 0; i < 2; i++) {
            gbc.gridx = 0;
            gbc.weightx = 0;
            JRadioButton rd = new JRadioButton("Đáp án đúng");
            rd.setOpaque(false);
            btnGroup.add(rd);
            rdAnswers.add(rd);
            pnlAnswers.add(rd, gbc);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            JTextField txt = new JTextField(options[i]);
            txt.setEditable(false);
            txt.setPreferredSize(new Dimension(0, 35));
            txt.setBackground(new Color(245, 245, 245));
            txtAnswers.add(txt);
            pnlAnswers.add(txt, gbc);
            gbc.gridy++;
        }
        rdAnswers.get(0).setSelected(true);
    }

    private void renderFillInBlank(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        JLabel lbl = new JLabel("Nhập câu trả lời chính xác cần điền:");
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        pnlAnswers.add(lbl, gbc);
        gbc.gridy++;
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(0, 40));
        txt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtAnswers.add(txt);
        pnlAnswers.add(txt, gbc);
        JRadioButton rdHidden = new JRadioButton();
        rdHidden.setSelected(true);
        rdAnswers.add(rdHidden);
    }

    private void loadComboBoxData() {
        busLoaiCauHoi.getAll().forEach(cmbLoaiCauHoi::addItem);
        busDoKho.getAll().forEach(cmbDoKho::addItem);
        busMonHoc.getAll().forEach(cmbMonHoc::addItem);
    }

    private void loadDataForEdit() {
        txtaCauHoi.setText(currentDTO.getNoidung());
        for (int i = 0; i < cmbDoKho.getItemCount(); i++) {
            if (cmbDoKho.getItemAt(i).getMadokho() == currentDTO.getMadokho()) {
                cmbDoKho.setSelectedIndex(i);
            }
        }
        for (int i = 0; i < cmbMonHoc.getItemCount(); i++) {
            if (cmbMonHoc.getItemAt(i).getMamonhoc() == currentDTO.getMamonhoc()) {
                cmbMonHoc.setSelectedIndex(i);
            }
        }
        for (int i = 0; i < cmbLoaiCauHoi.getItemCount(); i++) {
            if (cmbLoaiCauHoi.getItemAt(i).getMaloai() == currentDTO.getMaloai()) {
                cmbLoaiCauHoi.setSelectedIndex(i);
            }
        }

        updateAnswerUIByTen(((LoaiCauHoiDTO) cmbLoaiCauHoi.getSelectedItem()).getTenloai());
        ArrayList<DapAnDTO> listDA = busDapAn.getDapAnDeHienThi(currentDTO.getMacauhoi());
        for (int i = 0; i < listDA.size() && i < txtAnswers.size(); i++) {
            txtAnswers.get(i).setText(listDA.get(i).getNoidungtl());
            if (listDA.get(i).isLadapan()) {
                rdAnswers.get(i).setSelected(true);
            }
        }
    }

    private void luuCauHoi() {
        if (Validation.isEmpty(txtaCauHoi.getText())) {
            JOptionPane.showMessageDialog(this, "Nội dung câu hỏi không được để trống!");
            return;
        }
        try {
            CauHoiDTO ch = (currentDTO != null) ? currentDTO : new CauHoiDTO();
            ch.setNoidung(txtaCauHoi.getText().trim());
            ch.setMadokho(((DoKhoDTO) cmbDoKho.getSelectedItem()).getMadokho());
            ch.setMamonhoc(((MonHocDTO) cmbMonHoc.getSelectedItem()).getMamonhoc());
            ch.setMaloai(((LoaiCauHoiDTO) cmbLoaiCauHoi.getSelectedItem()).getMaloai());
            ch.setTrangthai(1);
            if (currentDTO == null) {
                ch.setNguoitao(mainFrame.getNguoiDung().getId());
            }
            boolean success = (currentDTO == null) ? busCauHoi.add(ch) : busCauHoi.update(ch);
            if (success) {
                int maCH = (currentDTO == null) ? findMaxId() : ch.getMacauhoi();
                if (!this.getTitle().contains("độ khó")) {
                    saveAnswers(maCH);
                }
                JOptionPane.showMessageDialog(this, "Đã lưu thông tin!");
                if (parent != null) {
                    parent.loadDataTable(busCauHoi.getAll());
                }
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu!");
        }
    }

    private void saveAnswers(int maCH) {
        busDapAn.deleteByMaCauHoi(maCH);
        for (int i = 0; i < txtAnswers.size(); i++) {
            String content = txtAnswers.get(i).getText().trim();
            if (!content.isEmpty()) {
                busDapAn.add(new DapAnDTO(0, maCH, content, rdAnswers.get(i).isSelected()));
            }
        }
    }

    private int findMaxId() {
        return busCauHoi.getAll().stream().mapToInt(CauHoiDTO::getMacauhoi).max().orElse(0);
    }

    private void disableAllExceptDifficulty() {
        cmbLoaiCauHoi.setEnabled(false);
        cmbMonHoc.setEnabled(false);
        txtaCauHoi.setEditable(false);
        txtaCauHoi.setBackground(new Color(240, 240, 240));
        for (JTextField txt : txtAnswers) {
            txt.setEditable(false);
            txt.setBackground(new Color(240, 240, 240));
        }
        for (JRadioButton rd : rdAnswers) {
            rd.setEnabled(false);
        }
        btnLuu.setText("Cập nhật độ khó");
    }
}
