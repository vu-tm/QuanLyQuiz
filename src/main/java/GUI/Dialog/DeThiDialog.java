package GUI.Dialog;

import BUS.CauHoiBUS;
import BUS.DeThiBUS;
import BUS.DoKhoBUS;
import BUS.KyThiBUS;
import BUS.LopBUS;
import BUS.MonHocBUS;
import DAO.GiaoDeThiDAO;
import DTO.CauHoiDTO;
import DTO.DeThiDTO;
import DTO.DoKhoDTO;
import DTO.KyThiDTO;
import DTO.LopDTO;
import DTO.MonHocDTO;
import DTO.GiaoDeThiDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.SelectForm;
import GUI.Panel.DeThi;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.PlainDocument;

public class DeThiDialog extends JDialog {

    private InputForm tenDe, soCau;
    private SelectForm cbxKyThi, cbxMonHoc;
    private JTextField txtThoiGian, txtSearch;
    private JComboBox<String> cbxFilterDoKho;
    private ButtonCustom btnLuu, btnHuy;
    private JPanel pnlInfo, pnlRight, pnlSelect, pnlQuestionList, pnlButtons;
    private JPanel pnlLopHoc, pnlLopHeader, pnlLopGrid;
    private JScrollPane scrollQuestion, scrollLop;
    private GUI.Main mainFrame;

    private KyThiBUS kyThiBUS = new KyThiBUS();
    private DoKhoBUS doKhoBUS = new DoKhoBUS();
    private CauHoiBUS cauHoiBUS = new CauHoiBUS();
    private MonHocBUS monHocBUS = new MonHocBUS();
    private DeThiBUS deThiBUS = new DeThiBUS();
    private LopBUS lopBUS = new LopBUS();
    private GiaoDeThiDAO giaoDeThiDAO = GiaoDeThiDAO.getInstance();

    private HashSet<Integer> selectedCauHoi = new HashSet<>();
    private HashSet<Integer> selectedLop = new HashSet<>();
    private ArrayList<LopDTO> listLopHienThi = new ArrayList<>();
    private JCheckBox chkChonTatCa, chkAllQuestions;

    private DeThi parent;
    private DeThiDTO currentDTO;
    private String currentType;

    private List<CauHoiDTO> currentFilteredQuestions = new ArrayList<>();
    private List<CauHoiDTO> allCauHoi;
    private ArrayList<MonHocDTO> listMH;

    public DeThiDialog(DeThi parent, JFrame owner, String title, boolean modal, String type, DeThiDTO dt) {
        super(owner, title, modal);
        this.mainFrame = (GUI.Main) owner;
        this.parent = parent;
        this.currentDTO = dt;
        this.currentType = type;

        if (currentDTO != null) {
            for (int ma : deThiBUS.getMaCauHoiByMade(currentDTO.getMade())) {
                selectedCauHoi.add(ma);
            }
            for (int malop : giaoDeThiDAO.getMaLopByMaDe(currentDTO.getMade())) {
                selectedLop.add(malop);
            }
        }

        allCauHoi = cauHoiBUS.getAll();
        listMH = monHocBUS.getAll();
        init(type);
    }

    private void init(String type) {
        this.setSize(new Dimension(1150, 700));
        this.setLayout(new BorderLayout(0, 0));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        initPnlLopHoc();
        initPnlSelect();
        initPnlRight();
        initPnlInfo();
        initPnlButtons(type);

        this.add(pnlInfo, BorderLayout.WEST);
        this.add(pnlRight, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);

        if (type.equals("view")) {
            tenDe.setDisable();
            txtThoiGian.setEnabled(false);
            cbxKyThi.getCbb().setEnabled(false);
            cbxMonHoc.getCbb().setEnabled(false);
        }

        if (currentDTO != null) {
            capNhatDanhSachLop();
        }

        this.setVisible(true);
    }

    private void initPnlInfo() {
        pnlInfo = new JPanel(new GridLayout(5, 1, 0, 10));
        pnlInfo.setPreferredSize(new Dimension(320, 0));
        pnlInfo.setBorder(new EmptyBorder(20, 20, 20, 10));
        pnlInfo.setBackground(Color.WHITE);

        tenDe = new InputForm("Tên đề thi");

        ArrayList<KyThiDTO> listKT = kyThiBUS.getAll();
        String[] dsKyThi = new String[listKT.size() + 1];
        dsKyThi[0] = "Chọn kỳ thi";
        for (int i = 0; i < listKT.size(); i++) {
            dsKyThi[i + 1] = listKT.get(i).getTenkythi();
        }
        cbxKyThi = new SelectForm("Kỳ thi", dsKyThi);

        String[] dsMonHoc = new String[listMH.size() + 1];
        dsMonHoc[0] = "Chọn môn học";
        for (int i = 0; i < listMH.size(); i++) {
            dsMonHoc[i + 1] = listMH.get(i).getTenmonhoc();
        }
        cbxMonHoc = new SelectForm("Môn học", dsMonHoc);

        cbxMonHoc.getCbb().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int indexMH = cbxMonHoc.getCbb().getSelectedIndex() - 1;

                if (indexMH >= 0) {
                    int selectedMaMH = listMH.get(indexMH).getMamonhoc();
                    selectedCauHoi.removeIf(id -> {
                        DTO.CauHoiDTO q = cauHoiBUS.getById(id);
                        return q != null && q.getMamonhoc() != selectedMaMH;
                    });
                } else {
                    selectedCauHoi.clear();
                }

                soCau.setText(String.valueOf(selectedCauHoi.size()));

                capNhatDanhSachLop();
                applyFilter();
            }
        });

        JPanel pnlThoiGian = new JPanel(new BorderLayout());
        pnlThoiGian.setBorder(new LineBorder(new Color(204, 214, 219)));
        pnlThoiGian.setBackground(Color.WHITE);
        txtThoiGian = new JTextField("");
        txtThoiGian.setBorder(new EmptyBorder(0, 10, 0, 10));
        ((PlainDocument) txtThoiGian.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        JLabel lblPhut = new JLabel("phút  ", JLabel.RIGHT);
        lblPhut.setPreferredSize(new Dimension(60, 0));
        lblPhut.setOpaque(true);
        lblPhut.setBackground(new Color(240, 247, 250));
        pnlThoiGian.add(txtThoiGian, BorderLayout.CENTER);
        pnlThoiGian.add(lblPhut, BorderLayout.EAST);

        JPanel pnlTimeGroup = new JPanel(new GridLayout(2, 1));
        pnlTimeGroup.setBackground(Color.WHITE);
        pnlTimeGroup.setBorder(new EmptyBorder(0, 10, 5, 10));
        pnlTimeGroup.setPreferredSize(new Dimension(100, 100));
        pnlTimeGroup.add(new JLabel("Thời gian làm bài"));
        pnlTimeGroup.add(pnlThoiGian);

        soCau = new InputForm("Số câu hỏi đã chọn");
        soCau.setEditable(false);
        soCau.getTxtForm().setFocusable(false);
        soCau.setText(String.valueOf(selectedCauHoi.size()));

        if (currentDTO != null) {
            tenDe.setText(currentDTO.getTende());
            txtThoiGian.setText(String.valueOf(currentDTO.getThoigianthi()));
            for (int i = 0; i < listKT.size(); i++) {
                if (listKT.get(i).getMakythi() == currentDTO.getMakythi()) {
                    cbxKyThi.getCbb().setSelectedIndex(i + 1);
                    break;
                }
            }
            for (int i = 0; i < listMH.size(); i++) {
                if (listMH.get(i).getMamonhoc() == currentDTO.getMonthi()) {
                    cbxMonHoc.getCbb().setSelectedIndex(i + 1);
                    break;
                }
            }
        }

        pnlInfo.add(tenDe);
        pnlInfo.add(cbxKyThi);
        pnlInfo.add(pnlTimeGroup);
        pnlInfo.add(cbxMonHoc);
        pnlInfo.add(soCau);
    }

    private void initPnlRight() {
        pnlRight = new JPanel(new BorderLayout(0, 5));
        pnlRight.setBorder(new EmptyBorder(20, 10, 10, 20));
        pnlRight.setBackground(Color.WHITE);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlSelect, pnlLopHoc);
        split.setDividerSize(4);
        split.setBorder(null);
        split.setEnabled(false);

        SwingUtilities.invokeLater(() -> {
            int totalHeight = split.getHeight();
            split.setDividerLocation((int) (totalHeight * 0.70));
        });

        pnlRight.add(split, BorderLayout.CENTER);
    }

    private void initPnlSelect() {
        pnlSelect = new JPanel(new BorderLayout(5, 5));
        pnlSelect.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(200, 200, 200)),
                "Chọn câu hỏi",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));
        pnlSelect.setBackground(Color.WHITE);

        JPanel pnlSearchTool = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlSearchTool.setBackground(Color.WHITE);

        chkAllQuestions = new JCheckBox("Chọn tất cả");
        chkAllQuestions.setBackground(Color.WHITE);
        chkAllQuestions.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        chkAllQuestions.addActionListener(e -> {
            boolean isSelected = chkAllQuestions.isSelected();
            for (CauHoiDTO ch : currentFilteredQuestions) {
                if (isSelected) {
                    selectedCauHoi.add(ch.getMacauhoi());
                } else {
                    selectedCauHoi.remove(ch.getMacauhoi());
                }
            }
            soCau.setText(String.valueOf(selectedCauHoi.size()));
            renderCauHoi(currentFilteredQuestions);
        });

        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(180, 32));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm câu hỏi...");

        ArrayList<DoKhoDTO> listDK = doKhoBUS.getAll();
        String[] dsDoKho = new String[listDK.size() + 1];
        dsDoKho[0] = "Tất cả độ khó";
        for (int i = 0; i < listDK.size(); i++) {
            dsDoKho[i + 1] = listDK.get(i).getTendokho();
        }
        cbxFilterDoKho = new JComboBox<>(dsDoKho);
        cbxFilterDoKho.setPreferredSize(new Dimension(140, 32));

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilter();
            }
        });
        cbxFilterDoKho.addActionListener(e -> applyFilter());

        pnlSearchTool.add(chkAllQuestions);
        pnlSearchTool.add(new JSeparator(JSeparator.VERTICAL));
        pnlSearchTool.add(lblTimKiem);
        pnlSearchTool.add(txtSearch);
        pnlSearchTool.add(cbxFilterDoKho);

        pnlQuestionList = new JPanel();
        pnlQuestionList.setLayout(new BoxLayout(pnlQuestionList, BoxLayout.Y_AXIS));
        pnlQuestionList.setBackground(Color.WHITE);
        renderCauHoi(allCauHoi);

        scrollQuestion = new JScrollPane(pnlQuestionList);
        scrollQuestion.setBorder(new LineBorder(new Color(230, 230, 230)));
        scrollQuestion.getVerticalScrollBar().setUnitIncrement(16);

        pnlSelect.add(pnlSearchTool, BorderLayout.NORTH);
        pnlSelect.add(scrollQuestion, BorderLayout.CENTER);
    }

    private void initPnlLopHoc() {
        pnlLopHoc = new JPanel(new BorderLayout(0, 0));
        pnlLopHoc.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(200, 200, 200)),
                "Giao đề thi cho lớp",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13)));
        pnlLopHoc.setBackground(Color.WHITE);

        pnlLopHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlLopHeader.setBackground(Color.WHITE);
        pnlLopHeader.setVisible(false);

        chkChonTatCa = new JCheckBox("Chọn tất cả");
        chkChonTatCa.setBackground(Color.WHITE);
        chkChonTatCa.setFont(new Font("Roboto", Font.PLAIN, 13));
        chkChonTatCa.addActionListener(e -> {
            boolean checked = chkChonTatCa.isSelected();
            for (LopDTO lop : listLopHienThi) {
                if (checked) {
                    selectedLop.add(lop.getMalop());
                } else {
                    selectedLop.remove(lop.getMalop());
                }
            }
            renderLopGrid();
        });
        if ("view".equals(currentType)) {
            chkChonTatCa.setEnabled(false);
        }
        pnlLopHeader.add(chkChonTatCa);

        pnlLopGrid = new JPanel(new GridLayout(0, 4, 2, 2));  // 4 cột, gap nhỏ
        pnlLopGrid.setBorder(new EmptyBorder(3, 8, 3, 8));
        pnlLopGrid.setBackground(Color.WHITE);

        scrollLop = new JScrollPane(pnlLopGrid);
        scrollLop.setBorder(null);
        scrollLop.getVerticalScrollBar().setUnitIncrement(10);

        pnlLopHoc.add(pnlLopHeader, BorderLayout.NORTH);
        pnlLopHoc.add(scrollLop, BorderLayout.CENTER);
    }

    private void capNhatDanhSachLop() {
        int indexMH = cbxMonHoc.getCbb().getSelectedIndex() - 1;

        if (indexMH < 0) {
            listLopHienThi = new ArrayList<>();
            pnlLopHeader.setVisible(false);
            pnlLopGrid.removeAll();
            pnlLopGrid.revalidate();
            pnlLopGrid.repaint();
            return;
        }

        int maMonHoc = listMH.get(indexMH).getMamonhoc();
        listLopHienThi = lopBUS.getByMonHoc(maMonHoc);
        pnlLopHeader.setVisible(true);
        renderLopGrid();
    }

    private void renderLopGrid() {
        pnlLopGrid.removeAll();

        boolean tatCa = !listLopHienThi.isEmpty();

        for (LopDTO lop : listLopHienThi) {
            boolean isChecked = selectedLop.contains(lop.getMalop());
            if (!isChecked) {
                tatCa = false;
            }

            JCheckBox chk = new JCheckBox(lop.getTenlop());
            chk.setBackground(Color.WHITE);
            chk.setFont(new Font("Roboto", Font.PLAIN, 13));
            chk.setSelected(isChecked);

            if (!"view".equals(currentType)) {
                int malop = lop.getMalop();
                chk.addActionListener(e -> {
                    if (chk.isSelected()) {
                        selectedLop.add(malop);
                    } else {
                        selectedLop.remove(malop);
                    }
                    syncChonTatCa();
                });
            } else {
                chk.setEnabled(false);
            }

            pnlLopGrid.add(chk);
        }

        chkChonTatCa.setSelected(tatCa && !listLopHienThi.isEmpty());
        pnlLopGrid.revalidate();
        pnlLopGrid.repaint();
    }

    private void syncChonTatCa() {
        boolean tatCa = !listLopHienThi.isEmpty();
        for (LopDTO lop : listLopHienThi) {
            if (!selectedLop.contains(lop.getMalop())) {
                tatCa = false;
                break;
            }
        }
        chkChonTatCa.setSelected(tatCa);
    }

    private void applyFilter() {
        String keyword = txtSearch.getText().toLowerCase().trim();
        String tenDoKho = (String) cbxFilterDoKho.getSelectedItem();

        int indexMH = cbxMonHoc.getCbb().getSelectedIndex() - 1;
        int selectedMaMH = (indexMH >= 0) ? listMH.get(indexMH).getMamonhoc() : -1;

        ArrayList<CauHoiDTO> filtered = new ArrayList<>();
        for (CauHoiDTO ch : allCauHoi) {
            boolean matchMonHoc = (selectedMaMH == -1) || (ch.getMamonhoc() == selectedMaMH);

            boolean matchKeyword = keyword.isEmpty() || ch.getNoidung().toLowerCase().contains(keyword);

            boolean matchDoKho = "Tất cả độ khó".equals(tenDoKho)
                    || doKhoBUS.getTenDoKho(ch.getMadokho()).equals(tenDoKho);

            if (matchMonHoc && matchKeyword && matchDoKho) {
                filtered.add(ch);
            }
        }
        this.currentFilteredQuestions = filtered;
        syncChonTatCaCauHoi();
        renderCauHoi(filtered);
    }

    private void renderCauHoi(List<CauHoiDTO> danhSach) {
        this.currentFilteredQuestions = danhSach;
        pnlQuestionList.removeAll();
        for (CauHoiDTO ch : danhSach) {
            String tenDoKho = doKhoBUS.getTenDoKho(ch.getMadokho());
            addQuestionToPanel(ch.getMacauhoi(), ch.getNoidung(), tenDoKho, selectedCauHoi.contains(ch.getMacauhoi()));
        }
        pnlQuestionList.revalidate();
        pnlQuestionList.repaint();
    }

    private void addQuestionToPanel(int maCauHoi, String content, String level, boolean isChecked) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 235, 235)),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));

        JCheckBox chk = new JCheckBox();
        chk.setBackground(Color.WHITE);
        chk.setSelected(isChecked);

        if (!"view".equals(currentType)) {
            chk.addActionListener((ActionEvent e) -> {
                if (chk.isSelected()) {
                    selectedCauHoi.add(maCauHoi);
                } else {
                    selectedCauHoi.remove(maCauHoi);
                }
                soCau.setText(String.valueOf(selectedCauHoi.size()));
                syncChonTatCaCauHoi();
            });
        } else {
            chk.setEnabled(false);
        }

        JLabel lblContent = new JLabel("<html>" + content + "</html>");

        JLabel lblLevel = new JLabel(level);
        lblLevel.setOpaque(true);
        lblLevel.setHorizontalAlignment(SwingConstants.CENTER);
        lblLevel.setPreferredSize(new Dimension(90, 25));
        if (level.equalsIgnoreCase("Dễ")) {
            lblLevel.setBackground(new Color(144, 238, 144));
        } else if (level.equalsIgnoreCase("Trung bình")) {
            lblLevel.setBackground(new Color(255, 255, 153));
        } else {
            lblLevel.setBackground(new Color(255, 165, 0));
        }

        item.add(chk, BorderLayout.WEST);
        item.add(lblContent, BorderLayout.CENTER);
        item.add(lblLevel, BorderLayout.EAST);
        pnlQuestionList.add(item);
    }

    private void initPnlButtons(String type) {
        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(5, 0, 5, 0));

        btnLuu = new ButtonCustom(type.equals("create") ? "Tạo đề thi" : "Lưu thông tin", "success", 14);
        btnHuy = new ButtonCustom("Huỷ bỏ", "danger", 14);

        btnLuu.addActionListener((ActionEvent e) -> {
            if (validateInput()) {
                luuDeThi(type);
            }
        });
        btnHuy.addActionListener((ActionEvent e) -> dispose());

        if (!type.equals("view")) {
            pnlButtons.add(btnLuu);
        }
        pnlButtons.add(btnHuy);
    }

    private boolean validateInput() {
        if (tenDe.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên đề thi không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtThoiGian.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thời gian làm bài!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (cbxKyThi.getCbb().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn kỳ thi!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void luuDeThi(String type) {
        if (cbxKyThi.getCbb().getSelectedIndex() == 0 || cbxMonHoc.getCbb().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ Kỳ thi và Môn học!");
            return;
        }

        int indexKT = cbxKyThi.getCbb().getSelectedIndex() - 1;
        int indexMH = cbxMonHoc.getCbb().getSelectedIndex() - 1;

        ArrayList<KyThiDTO> listKT = kyThiBUS.getAll();
        int maKyThi = indexKT >= 0 ? listKT.get(indexKT).getMakythi() : 0;
        int maMonHoc = indexMH >= 0 ? listMH.get(indexMH).getMamonhoc() : 0;
        int thoiGian = Integer.parseInt(txtThoiGian.getText().trim());

        ArrayList<Integer> listMaCauHoi = new ArrayList<>(selectedCauHoi);
        ArrayList<Integer> listMaLop = new ArrayList<>(selectedLop);

        if (type.equals("create")) {
            DeThiDTO dt = new DeThiDTO();
            dt.setTende(tenDe.getText().trim());
            dt.setMakythi(maKyThi);
            dt.setMonthi(maMonHoc);
            dt.setThoigianthi(thoiGian);
            dt.setTongsocau(listMaCauHoi.size());
            dt.setThoigiantao(new Timestamp(System.currentTimeMillis()));
            dt.setNguoitao(mainFrame.getNguoiDung().getId());
            dt.setTrangthai(true);
            int madeVuaTao = deThiBUS.add(dt);

            if (madeVuaTao > 0) {
                deThiBUS.saveChiTiet(madeVuaTao, listMaCauHoi);
                giaoDeThiDAO.deleteByMaDe(madeVuaTao);
                for (int malop : listMaLop) {
                    giaoDeThiDAO.insert(new GiaoDeThiDTO(madeVuaTao, malop));
                }

                JOptionPane.showMessageDialog(this, "Thêm đề thi thành công!");
                parent.loadDataTable(deThiBUS.getAll());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            currentDTO.setTende(tenDe.getText().trim());
            currentDTO.setMakythi(maKyThi);
            currentDTO.setMonthi(maMonHoc);
            currentDTO.setThoigianthi(thoiGian);
            currentDTO.setTongsocau(listMaCauHoi.size());

            if (deThiBUS.update(currentDTO)) {
                deThiBUS.saveChiTiet(currentDTO.getMade(), listMaCauHoi);
                giaoDeThiDAO.deleteByMaDe(currentDTO.getMade());
                for (int malop : listMaLop) {
                    giaoDeThiDAO.insert(new GiaoDeThiDTO(currentDTO.getMade(), malop));
                }
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parent.loadDataTable(deThiBUS.getAll());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void syncChonTatCaCauHoi() {
        if (currentFilteredQuestions.isEmpty()) {
            chkAllQuestions.setSelected(false);
            return;
        }

        boolean allSelected = true;
        for (CauHoiDTO ch : currentFilteredQuestions) {
            if (!selectedCauHoi.contains(ch.getMacauhoi())) {
                allSelected = false;
                break;
            }
        }
        chkAllQuestions.setSelected(allSelected);
    }
}
