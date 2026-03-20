package GUI.Dialog;

import BUS.DoKhoBUS;
import BUS.LoaiCauHoiBUS;
import BUS.MonHocBUS;
import DAO.DapAnDAO;
import DTO.CauHoiDTO;
import DTO.DapAnDTO;
import DTO.DoKhoDTO;
import DTO.MonHocDTO;
import GUI.Component.ButtonCustom;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ViewCauHoiDialog extends JDialog {

    private final LoaiCauHoiBUS busLoaiCauHoi = new LoaiCauHoiBUS();
    private final DoKhoBUS busDoKho = new DoKhoBUS();
    private final MonHocBUS busMonHoc = new MonHocBUS();
    private final DapAnDAO dapAnDAO = DapAnDAO.getInstance();

    private final CauHoiDTO currentDTO;

    private JComboBox<String> cmbLoaiCauHoi;
    private JComboBox<DoKhoDTO> cmbDoKho;
    private JComboBox<MonHocDTO> cmbMonHoc;
    private JCheckBox chkTrangThai;
    private JTextArea txtaCauHoi;
    private JPanel pnlAnswers;
    private final List<JCheckBox> chkAnswers = new ArrayList<>();
    private final List<JTextField> txtAnswers = new ArrayList<>();
    private ButtonCustom btnDong;

    private static final int TRAC_NGHIEM = 1;
    private static final int DUNG_SAI = 2;
    private int currentQuestionType = TRAC_NGHIEM;

    public ViewCauHoiDialog(JFrame owner, CauHoiDTO dto) {
        super(owner, "Chi tiết câu hỏi", true);
        this.currentDTO = dto;
        init();
    }

    private void init() {
        this.setSize(700, 750);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
        this.getContentPane().setBackground(Color.WHITE);
        this.setResizable(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pnlAttributes = createAttributesPanel();
        mainPanel.add(pnlAttributes);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel pnlContent = createContentPanel();
        mainPanel.add(pnlContent);
        mainPanel.add(Box.createVerticalStrut(15));

        pnlAnswers = new JPanel();
        pnlAnswers.setLayout(new BoxLayout(pnlAnswers, BoxLayout.Y_AXIS));
        pnlAnswers.setBackground(Color.WHITE);
        pnlAnswers.setBorder(new TitledBorder("Đáp án"));
        mainPanel.add(pnlAnswers);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnDong = new ButtonCustom("Đóng", "danger", 14);
        btnDong.addActionListener(e -> dispose());
        pnlButtons.add(btnDong);

        this.add(pnlButtons, BorderLayout.SOUTH);

        loadData();

        this.setVisible(true);
    }

    private JPanel createAttributesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder("Thuộc tính"));

        JPanel pnlLoai = new JPanel(new BorderLayout(5, 5));
        pnlLoai.setBackground(Color.WHITE);
        JLabel lblLoai = new JLabel("Loại câu hỏi:");
        lblLoai.setPreferredSize(new Dimension(100, 24));
        cmbLoaiCauHoi = new JComboBox<>(new String[]{"Trắc nghiệm", "Đúng/Sai"});
        cmbLoaiCauHoi.setEnabled(false);
        pnlLoai.add(lblLoai, BorderLayout.WEST);
        pnlLoai.add(cmbLoaiCauHoi, BorderLayout.CENTER);

        JPanel pnlDoKho = new JPanel(new BorderLayout(5, 5));
        pnlDoKho.setBackground(Color.WHITE);
        JLabel lblDoKho = new JLabel("Độ khó:");
        lblDoKho.setPreferredSize(new Dimension(100, 24));
        cmbDoKho = new JComboBox<>();
        cmbDoKho.setEnabled(false);
        pnlDoKho.add(lblDoKho, BorderLayout.WEST);
        pnlDoKho.add(cmbDoKho, BorderLayout.CENTER);

        cmbDoKho.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            if (value != null) {
                label.setText(value.getTendokho());
            }
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        });

        JPanel pnlMon = new JPanel(new BorderLayout(5, 5));
        pnlMon.setBackground(Color.WHITE);
        JLabel lblMon = new JLabel("Môn học:");
        lblMon.setPreferredSize(new Dimension(100, 24));
        cmbMonHoc = new JComboBox<>();
        cmbMonHoc.setEnabled(false);
        pnlMon.add(lblMon, BorderLayout.WEST);
        pnlMon.add(cmbMonHoc, BorderLayout.CENTER);

        cmbMonHoc.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            if (value != null) {
                label.setText(value.getTenmonhoc());
            }
            label.setOpaque(true);
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        });

        JPanel pnlTrangThai = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTrangThai.setBackground(Color.WHITE);
        chkTrangThai = new JCheckBox("Hoạt động");
        chkTrangThai.setBackground(Color.WHITE);
        chkTrangThai.setEnabled(false);
        pnlTrangThai.add(chkTrangThai);

        panel.add(pnlLoai);
        panel.add(pnlDoKho);
        panel.add(pnlMon);
        panel.add(pnlTrangThai);

        loadComboBoxData();

        return panel;
    }

    private void loadComboBoxData() {
        List<DoKhoDTO> doKhoList = busDoKho.getAll();
        for (DoKhoDTO dk : doKhoList) {
            cmbDoKho.addItem(dk);
        }

        List<MonHocDTO> monHocList = busMonHoc.getAll();
        for (MonHocDTO mh : monHocList) {
            cmbMonHoc.addItem(mh);
        }
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder("Câu hỏi"));

        JLabel lblCauHoi = new JLabel("Nội dung:");
        txtaCauHoi = new JTextArea(6, 40);
        txtaCauHoi.setLineWrap(true);
        txtaCauHoi.setWrapStyleWord(true);
        txtaCauHoi.setFont(new Font("Arial", Font.PLAIN, 12));
        txtaCauHoi.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(txtaCauHoi);

        JPanel pnlLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlLabel.setBackground(Color.WHITE);
        pnlLabel.add(lblCauHoi);

        panel.add(pnlLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void updateAnswerPanel(int type) {
        pnlAnswers.removeAll();
        chkAnswers.clear();
        txtAnswers.clear();

        int numAnswers = (type == TRAC_NGHIEM) ? 4 : 2;
        String[] answerLabels = (type == TRAC_NGHIEM)
            ? new String[]{"Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D"}
            : new String[]{"Đúng", "Sai"};

        JPanel pnlHeader = new JPanel(new GridBagLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(0, 10, 10, 10));

        JLabel lblCorrect = new JLabel("✓ Đ/án đúng");
        lblCorrect.setFont(new Font("Arial", Font.BOLD, 11));
        JLabel lblContent = new JLabel("Trả lời");
        lblContent.setFont(new Font("Arial", Font.BOLD, 11));

        GridBagConstraints gbcHeader = new GridBagConstraints();
        gbcHeader.gridy = 0;
        gbcHeader.insets = new Insets(0, 0, 0, 10);
        gbcHeader.anchor = GridBagConstraints.WEST;

        gbcHeader.gridx = 0;
        gbcHeader.weightx = 0.22;
        gbcHeader.fill = GridBagConstraints.HORIZONTAL;
        pnlHeader.add(lblCorrect, gbcHeader);

        gbcHeader.gridx = 1;
        gbcHeader.weightx = 0.78;
        gbcHeader.insets = new Insets(0, 0, 0, 0);
        pnlHeader.add(lblContent, gbcHeader);

        pnlAnswers.add(pnlHeader);
        pnlAnswers.add(Box.createVerticalStrut(5));

        for (int i = 0; i < numAnswers; i++) {
            JPanel pnlRow = new JPanel(new GridBagLayout());
            pnlRow.setBackground(Color.WHITE);
            pnlRow.setBorder(new EmptyBorder(0, 10, 5, 10));

            JCheckBox chk = new JCheckBox();
            chk.setBackground(Color.WHITE);
            chk.setEnabled(false);
            JTextField txt = new JTextField(answerLabels[i]);
            txt.setFont(new Font("Arial", Font.PLAIN, 12));
            txt.setEditable(false);

            chkAnswers.add(chk);
            txtAnswers.add(txt);

            GridBagConstraints gbcRow = new GridBagConstraints();
            gbcRow.gridy = 0;
            gbcRow.anchor = GridBagConstraints.WEST;

            gbcRow.gridx = 0;
            gbcRow.weightx = 0.22;
            gbcRow.insets = new Insets(0, 0, 0, 10);
            gbcRow.fill = GridBagConstraints.HORIZONTAL;
            pnlRow.add(chk, gbcRow);

            gbcRow.gridx = 1;
            gbcRow.weightx = 0.78;
            gbcRow.insets = new Insets(0, 0, 0, 0);
            pnlRow.add(txt, gbcRow);

            pnlAnswers.add(pnlRow);
        }

        pnlAnswers.revalidate();
        pnlAnswers.repaint();
    }

    private void loadData() {
        if (currentDTO == null) {
            return;
        }

        txtaCauHoi.setText(currentDTO.getNoidung());
        chkTrangThai.setSelected(currentDTO.getTrangthai() == 1);

        for (int i = 0; i < cmbDoKho.getItemCount(); i++) {
            if (cmbDoKho.getItemAt(i).getMadokho() == currentDTO.getMadokho()) {
                cmbDoKho.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cmbMonHoc.getItemCount(); i++) {
            if (cmbMonHoc.getItemAt(i).getMamonhoc() == currentDTO.getMamonhoc()) {
                cmbMonHoc.setSelectedIndex(i);
                break;
            }
        }

        currentQuestionType = isDungSaiLoai(currentDTO.getMaloai()) ? DUNG_SAI : TRAC_NGHIEM;
        cmbLoaiCauHoi.setSelectedIndex(currentQuestionType - 1);
        updateAnswerPanel(currentQuestionType);

        List<DapAnDTO> answers = getAnswersForView(currentDTO.getMacauhoi());
        for (int i = 0; i < answers.size() && i < txtAnswers.size(); i++) {
            DapAnDTO ans = answers.get(i);
            txtAnswers.get(i).setText(ans.getNoidungtl());
            chkAnswers.get(i).setSelected(ans.isLadapan());
        }
    }

    private List<DapAnDTO> getAnswersForView(int macauhoi) {
        List<DapAnDTO> result = new ArrayList<>();
        for (DapAnDTO item : dapAnDAO.selectAll()) {
            if (item.getMacauhoi() == macauhoi) {
                result.add(item);
            }
        }
        return result;
    }

    private boolean isDungSaiLoai(int maLoai) {
        try {
            for (var loai : busLoaiCauHoi.getAll()) {
                if (loai.getMaloai() == maLoai && loai.getTenloai() != null
                        && loai.getTenloai().toLowerCase().contains("đúng")) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return maLoai == 2;
    }
}
