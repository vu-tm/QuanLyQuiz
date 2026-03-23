package GUI.Dialog;

import BUS.CauHoiBUS;
import BUS.DoKhoBUS;
import BUS.LoaiCauHoiBUS;
import BUS.MonHocBUS;
import DAO.DapAnDAO;
import DTO.CauHoiDTO;
import DTO.DapAnDTO;
import DTO.DoKhoDTO;
import DTO.MonHocDTO;
import GUI.Component.ButtonCustom;
import GUI.Panel.CauHoi;
import helper.Validation;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class AddCauHoiDialog extends JDialog {

    private CauHoiBUS busCauHoi = new CauHoiBUS();
    private LoaiCauHoiBUS busLoaiCauHoi = new LoaiCauHoiBUS();
    private DoKhoBUS busDoKho = new DoKhoBUS();
    private MonHocBUS busMonHoc = new MonHocBUS();
    private DapAnDAO dapAnDAO = DapAnDAO.getInstance();
    
    private CauHoi parent;
    private CauHoiDTO currentDTO;
    
    // UI Components
    private JComboBox<String> cmbLoaiCauHoi;
    private JComboBox<DoKhoDTO> cmbDoKho;
    private JComboBox<MonHocDTO> cmbMonHoc;
    private JCheckBox chkTrangThai;
    private JTextArea txtaCauHoi;
    private JPanel pnlAnswers;
    private List<JCheckBox> chkAnswers = new ArrayList<>();
    private List<JTextField> txtAnswers = new ArrayList<>();
    private ButtonCustom btnLuu, btnHuy;
    
    // Constants
    private static final int TRAC_NGHIEM = 1;
    private static final int DUNG_SAI = 2;
    private int currentQuestionType = TRAC_NGHIEM;
    
    public AddCauHoiDialog(CauHoi parent, JFrame owner, String title, CauHoiDTO dto) {
        super(owner, title, true);
        this.parent = parent;
        this.currentDTO = dto;
        init();
    }
    
    private void init() {
        this.setSize(700, 750);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));
        this.getContentPane().setBackground(Color.WHITE);
        this.setResizable(true);
        
        // Main panel with scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // 1. Top panel: attributes (loại, độ khó, môn học)
        JPanel pnlAttributes = createAttributesPanel();
        mainPanel.add(pnlAttributes);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // 2. Question content area
        JPanel pnlContent = createContentPanel();
        mainPanel.add(pnlContent);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // 3. Answers area (dynamic)
        pnlAnswers = new JPanel();
        pnlAnswers.setLayout(new BoxLayout(pnlAnswers, BoxLayout.Y_AXIS));
        pnlAnswers.setBackground(Color.WHITE);
        pnlAnswers.setBorder(new TitledBorder("Đáp án"));
        mainPanel.add(pnlAnswers);
        
        // Scroll pane for main content
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        btnLuu = new ButtonCustom(currentDTO == null ? "Thêm mới" : "Cập nhật", "success", 14);
        btnHuy = new ButtonCustom("Huỷ bỏ", "danger", 14);
        
        btnLuu.addActionListener(e -> luuCauHoi());
        btnHuy.addActionListener(e -> dispose());
        
        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);
        
        this.add(pnlButtons, BorderLayout.SOUTH);
        
        // Load dữ liệu nếu là edit
        if (currentDTO != null) {
            loadData();
        } else {
            updateAnswerPanel(TRAC_NGHIEM);
        }
        
        this.setVisible(true);
    }
    
    private JPanel createAttributesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new TitledBorder("Thuộc tính"));
        
        // Loại câu hỏi
        JPanel pnlLoai = new JPanel(new BorderLayout(5, 5));
        pnlLoai.setBackground(Color.WHITE);
        JLabel lblLoai = new JLabel("Loại câu hỏi:");
        lblLoai.setPreferredSize(new Dimension(100, 24));
        cmbLoaiCauHoi = new JComboBox<>(new String[]{"Trắc nghiệm", "Đúng/Sai"});
        cmbLoaiCauHoi.addActionListener(e -> onLoaiCauHoiChanged());
        pnlLoai.add(lblLoai, BorderLayout.WEST);
        pnlLoai.add(cmbLoaiCauHoi, BorderLayout.CENTER);
        
        // Độ khó
        JPanel pnlDoKho = new JPanel(new BorderLayout(5, 5));
        pnlDoKho.setBackground(Color.WHITE);
        JLabel lblDoKho = new JLabel("Độ khó:");
        lblDoKho.setPreferredSize(new Dimension(100, 24));
        cmbDoKho = new JComboBox<>();
        pnlDoKho.add(lblDoKho, BorderLayout.WEST);
        pnlDoKho.add(cmbDoKho, BorderLayout.CENTER);

                // Custom renderer for DoKhoDTO
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
        
        // Môn học
        JPanel pnlMon = new JPanel(new BorderLayout(5, 5));
        pnlMon.setBackground(Color.WHITE);
        JLabel lblMon = new JLabel("Môn học:");
        lblMon.setPreferredSize(new Dimension(100, 24));
        cmbMonHoc = new JComboBox<>();
        pnlMon.add(lblMon, BorderLayout.WEST);
        pnlMon.add(cmbMonHoc, BorderLayout.CENTER);
        
                // Custom renderer for MonHocDTO
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
                chkTrangThai.setSelected(true);
                chkTrangThai.setBackground(Color.WHITE);
                pnlTrangThai.add(chkTrangThai);
        
        panel.add(pnlLoai);
        panel.add(pnlDoKho);
        panel.add(pnlMon);
        panel.add(pnlTrangThai);
        
        // Load dữ liệu combobox
        loadComboBoxData();
        
        return panel;
    }
    
    private void loadComboBoxData() {
        // Load độ khó
        List<DoKhoDTO> doKhoList = busDoKho.getAll();
        for (DoKhoDTO dk : doKhoList) {
            cmbDoKho.addItem(dk);
        }
        
        // Load môn học
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
        
        JScrollPane scrollPane = new JScrollPane(txtaCauHoi);
        
        JPanel pnlLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlLabel.setBackground(Color.WHITE);
        pnlLabel.add(lblCauHoi);
        
        panel.add(pnlLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void onLoaiCauHoiChanged() {
        int selected = cmbLoaiCauHoi.getSelectedIndex();
        currentQuestionType = (selected == 0) ? TRAC_NGHIEM : DUNG_SAI;
        updateAnswerPanel(currentQuestionType);
    }
    
    private void updateAnswerPanel(int type) {
        pnlAnswers.removeAll();
        chkAnswers.clear();
        txtAnswers.clear();
        
        int numAnswers = (type == TRAC_NGHIEM) ? 4 : 2;
        String[] answerLabels = (type == TRAC_NGHIEM) 
            ? new String[]{"Đáp án A", "Đáp án B", "Đáp án C", "Đáp án D"}
            : new String[]{"Đúng", "Sai"};
        
        // Header row (cột Trả lời rộng hơn cột Đ/án đúng)
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
        
        // Answer rows
        for (int i = 0; i < numAnswers; i++) {
            JPanel pnlRow = new JPanel(new GridBagLayout());
            pnlRow.setBackground(Color.WHITE);
            pnlRow.setBorder(new EmptyBorder(0, 10, 5, 10));
            
            JCheckBox chk = new JCheckBox();
            chk.setBackground(Color.WHITE);
            JTextField txt = new JTextField(answerLabels[i]);
            txt.setFont(new Font("Arial", Font.PLAIN, 12));

            if (type == DUNG_SAI) {
                txt.setEditable(false);
            }
            
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
        if (currentDTO == null) return;
        
        // Load text area
        txtaCauHoi.setText(currentDTO.getNoidung());
        chkTrangThai.setSelected(currentDTO.getTrangthai() == 1);
        
        // Load combobox selections
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
        
        // Determine question type từ loại câu hỏi
        currentQuestionType = isDungSaiLoai(currentDTO.getMaloai()) ? DUNG_SAI : TRAC_NGHIEM;
        cmbLoaiCauHoi.setSelectedIndex(currentQuestionType - 1);
        updateAnswerPanel(currentQuestionType);
        
        // Load answers
        List<DapAnDTO> answers = getAnswersForEdit(currentDTO.getMacauhoi());
        for (int i = 0; i < answers.size() && i < txtAnswers.size(); i++) {
            DapAnDTO ans = answers.get(i);
            txtAnswers.get(i).setText(ans.getNoidungtl());
            chkAnswers.get(i).setSelected(ans.isLadapan());
        }
    }

    private List<DapAnDTO> getAnswersForEdit(int macauhoi) {
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
    
    private void luuCauHoi() {
        if (!validateInput()) {
            return;
        }
        
        try {
            String noidung = txtaCauHoi.getText().trim();
            DoKhoDTO selectedDoKho = (DoKhoDTO) cmbDoKho.getSelectedItem();
            MonHocDTO selectedMon = (MonHocDTO) cmbMonHoc.getSelectedItem();
            int maloai = resolveMaloaiBySelection();
            
            CauHoiDTO ch = new CauHoiDTO();
            ch.setNoidung(noidung);
            ch.setMadokho(selectedDoKho.getMadokho());
            ch.setMaloai(maloai);
            ch.setMamonhoc(selectedMon.getMamonhoc());
            ch.setNguoitao(currentDTO != null ? currentDTO.getNguoitao() : "admin");
            ch.setTrangthai(chkTrangThai.isSelected() ? 1 : 0);
            
            if (currentDTO != null) {
                ch.setMacauhoi(currentDTO.getMacauhoi());
                if (busCauHoi.edit(ch)) {
                    // Update answers
                    updateAnswersInDB(ch.getMacauhoi());
                    JOptionPane.showMessageDialog(this, "Cập nhật câu hỏi thành công!");
                    parent.loadData();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (busCauHoi.add(ch)) {
                    int newQuestionId = findLatestQuestionId();
                    if (newQuestionId > 0) {
                        updateAnswersInDB(newQuestionId);
                    }
                    JOptionPane.showMessageDialog(this, "Thêm câu hỏi thành công!");
                    parent.loadData();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void updateAnswersInDB(int macauhoi) {
        // Xóa đáp án cũ
        List<DapAnDTO> oldAnswers = dapAnDAO.selectByCauHoi(macauhoi);
        for (DapAnDTO ans : oldAnswers) {
            dapAnDAO.delete(ans.getMadapan());
        }
        
        // Thêm đáp án mới
        for (int i = 0; i < txtAnswers.size(); i++) {
            String content = txtAnswers.get(i).getText().trim();
            if (!content.isEmpty()) {
                boolean isCorrect = chkAnswers.get(i).isSelected();
                DapAnDTO ans = new DapAnDTO(0, macauhoi, content, isCorrect);
                dapAnDAO.insert(ans);
            }
        }
    }

    private int findLatestQuestionId() {
        List<CauHoiDTO> list = busCauHoi.load();
        int maxId = -1;
        for (CauHoiDTO item : list) {
            if (item.getMacauhoi() > maxId) {
                maxId = item.getMacauhoi();
            }
        }
        return maxId;
    }

    private int resolveMaloaiBySelection() {
        boolean isTracNghiem = currentQuestionType == TRAC_NGHIEM;
        String keyword = isTracNghiem ? "trắc" : "đúng";
        try {
            for (var loai : busLoaiCauHoi.getAll()) {
                if (loai.getTenloai() != null && loai.getTenloai().toLowerCase().contains(keyword)) {
                    return loai.getMaloai();
                }
            }
        } catch (Exception ignored) {
        }
        return isTracNghiem ? 1 : 2;
    }
    
    private boolean validateInput() {
        if (Validation.isEmpty(txtaCauHoi.getText())) {
            JOptionPane.showMessageDialog(this, "Nội dung câu hỏi không được để trống!");
            return false;
        }
        
        boolean hasAnswer = false;
        for (JTextField txt : txtAnswers) {
            if (!Validation.isEmpty(txt.getText())) {
                hasAnswer = true;
                break;
            }
        }
        
        if (!hasAnswer) {
            JOptionPane.showMessageDialog(this, "Phải nhập ít nhất một đáp án!");
            return false;
        }
        
        boolean hasCorrectAnswer = false;
        for (JCheckBox chk : chkAnswers) {
            if (chk.isSelected()) {
                hasCorrectAnswer = true;
                break;
            }
        }
        
        if (!hasCorrectAnswer) {
            JOptionPane.showMessageDialog(this, "Phải chọn ít nhất một đáp án đúng!");
            return false;
        }
        
        return true;
    }
}