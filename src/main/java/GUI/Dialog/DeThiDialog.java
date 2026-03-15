package GUI.Dialog;

import BUS.CauHoiBUS;
import BUS.DeThiBUS;
import BUS.DoKhoBUS;
import BUS.KyThiBUS;
import BUS.MonHocBUS;
import DTO.CauHoiDTO;
import DTO.DeThiDTO;
import DTO.DoKhoDTO;
import DTO.KyThiDTO;
import DTO.MonHocDTO;
import GUI.Component.ButtonCustom;
import GUI.Component.InputForm;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.SelectForm;
import GUI.Panel.DeThi;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.PlainDocument;

public class DeThiDialog extends JDialog {

    private InputForm tenDe, soCau;
    private SelectForm cbxKyThi, cbxMonHoc;
    private JTextField txtThoiGian, txtSearch;
    private JComboBox<String> cbxFilterDoKho;
    private ButtonCustom btnLuu, btnHuy;
    private JPanel pnlInfo, pnlSelect, pnlQuestionList, pnlButtons;
    private JScrollPane scrollQuestion;

    private KyThiBUS kyThiBUS = new KyThiBUS();
    private DoKhoBUS doKhoBUS = new DoKhoBUS();
    private CauHoiBUS cauHoiBUS = new CauHoiBUS();
    private MonHocBUS monHocBUS = new MonHocBUS();
    private DeThiBUS deThiBUS = new DeThiBUS();

    // Lưu trạng thái checkbox theo mã câu hỏi để không bị reset khi lọc
    private HashSet<Integer> selectedCauHoi = new HashSet<>();

    private DeThi parent;
    private DeThiDTO currentDTO;
    private String currentType;

    // Danh sách toàn bộ câu hỏi đang hiển thị trong panel (để lọc lại)
    private ArrayList<CauHoiDTO> allCauHoi;

    public DeThiDialog(DeThi parent, JFrame owner, String title, boolean modal, String type, DeThiDTO dt) {
        super(owner, title, modal);
        this.parent = parent;
        this.currentDTO = dt;
        this.currentType = type;

        // Nếu là update/view, load sẵn câu hỏi đã chọn
        if (currentDTO != null) {
            ArrayList<Integer> daCho = deThiBUS.getMaCauHoiByMade(currentDTO.getMade());
            for (int ma : daCho) {
                selectedCauHoi.add(ma);
            }
        }

        allCauHoi = cauHoiBUS.getAll();
        init(type);
    }

    private void init(String type) {
        this.setSize(new Dimension(1100, 650));
        this.setLayout(new BorderLayout(0, 0));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.WHITE);

        initPnlInfo();
        initPnlSelect();
        initPnlButtons(type);

        this.add(pnlInfo, BorderLayout.WEST);
        this.add(pnlSelect, BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);

        if (type.equals("view")) {
            tenDe.setDisable();
            txtThoiGian.setEnabled(false);
            cbxKyThi.getCbb().setEnabled(false);
            cbxMonHoc.getCbb().setEnabled(false);
        }

        this.setVisible(true);
    }

    private void initPnlInfo() {
        pnlInfo = new JPanel(new GridLayout(5, 1, 0, 10));
        pnlInfo.setPreferredSize(new Dimension(320, 0));
        pnlInfo.setBorder(new EmptyBorder(20, 20, 20, 10));
        pnlInfo.setBackground(Color.WHITE);

        tenDe = new InputForm("Tên đề thi");

        // Combobox kỳ thi
        ArrayList<KyThiDTO> listKT = kyThiBUS.getAll();
        String[] dsKyThi = new String[listKT.size() + 1];
        dsKyThi[0] = "Chọn kỳ thi";
        for (int i = 0; i < listKT.size(); i++) {
            dsKyThi[i + 1] = listKT.get(i).getTenkythi();
        }
        cbxKyThi = new SelectForm("Kỳ thi", dsKyThi);

        // Combobox môn học
        ArrayList<MonHocDTO> listMH = monHocBUS.getAll();
        String[] dsMonHoc = new String[listMH.size() + 1];
        dsMonHoc[0] = "Chọn môn học";
        for (int i = 0; i < listMH.size(); i++) {
            dsMonHoc[i + 1] = listMH.get(i).getTenmonhoc();
        }
        cbxMonHoc = new SelectForm("Môn học", dsMonHoc);

        // Thời gian làm bài
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

        // Số câu đã chọn (readonly)
        soCau = new InputForm("Số câu hỏi đã chọn");
        soCau.setEditable(false);
        soCau.setText("0");

        // Nếu đang edit/view, điền sẵn thông tin
        if (currentDTO != null) {
            tenDe.setText(currentDTO.getTende());
            txtThoiGian.setText(String.valueOf(currentDTO.getThoigianthi()));
            soCau.setText(String.valueOf(selectedCauHoi.size()));

            // Tìm index kỳ thi
            for (int i = 0; i < listKT.size(); i++) {
                if (listKT.get(i).getMakythi() == currentDTO.getMakythi()) {
                    cbxKyThi.getCbb().setSelectedIndex(i + 1);
                    break;
                }
            }
            // Tìm index môn học
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

    private void initPnlSelect() {
        pnlSelect = new JPanel(new BorderLayout(10, 10));
        pnlSelect.setBorder(new EmptyBorder(20, 10, 20, 20));
        pnlSelect.setBackground(Color.WHITE);

        // Thanh tìm kiếm + lọc độ khó
        JPanel pnlSearchTool = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearchTool.setBackground(Color.WHITE);

        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(180, 35));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm câu hỏi...");

        ArrayList<DoKhoDTO> listDK = doKhoBUS.getAll();
        String[] dsDoKho = new String[listDK.size() + 1];
        dsDoKho[0] = "Tất cả độ khó";
        for (int i = 0; i < listDK.size(); i++) {
            dsDoKho[i + 1] = listDK.get(i).getTendokho();
        }
        cbxFilterDoKho = new JComboBox<>(dsDoKho);
        cbxFilterDoKho.setPreferredSize(new Dimension(140, 35));

        // Listener tìm kiếm - không reset selectedCauHoi
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFilter();
            }
        });
        cbxFilterDoKho.addActionListener(e -> applyFilter());

        pnlSearchTool.add(new JLabel("Tìm kiếm:"));
        pnlSearchTool.add(txtSearch);
        pnlSearchTool.add(cbxFilterDoKho);

        // Danh sách câu hỏi
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

    // Lọc câu hỏi theo từ khóa và độ khó, giữ nguyên trạng thái đã chọn
    private void applyFilter() {
        String keyword = txtSearch.getText().toLowerCase().trim();
        String tenDoKho = (String) cbxFilterDoKho.getSelectedItem();

        ArrayList<CauHoiDTO> filtered = new ArrayList<>();
        for (CauHoiDTO ch : allCauHoi) {
            boolean matchKeyword = keyword.isEmpty() || ch.getNoidung().toLowerCase().contains(keyword);
            boolean matchDoKho = tenDoKho.equals("Tất cả độ khó")
                    || doKhoBUS.getTenDoKho(ch.getMadokho()).equals(tenDoKho);
            if (matchKeyword && matchDoKho) {
                filtered.add(ch);
            }
        }
        renderCauHoi(filtered);
    }

    // Render lại danh sách câu hỏi, tích lại các checkbox đã chọn trước đó
    private void renderCauHoi(ArrayList<CauHoiDTO> danhSach) {
        pnlQuestionList.removeAll();
        for (CauHoiDTO ch : danhSach) {
            String tenDoKho = doKhoBUS.getTenDoKho(ch.getMadokho());
            boolean daDuocChon = selectedCauHoi.contains(ch.getMacauhoi());
            addQuestionToPanel(ch.getMacauhoi(), ch.getNoidung(), tenDoKho, daDuocChon);
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

        // View mode: không cho chọn/bỏ chọn
        if (currentType.equals("view")) {
            chk.setEnabled(false);
        } else {
            chk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chk.isSelected()) {
                        selectedCauHoi.add(maCauHoi);
                    } else {
                        selectedCauHoi.remove(maCauHoi);
                    }
                    soCau.setText(String.valueOf(selectedCauHoi.size()));
                }
            });
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
        pnlButtons.setBorder(new EmptyBorder(10, 0, 10, 0));

        btnLuu = new ButtonCustom(type.equals("create") ? "Tạo đề thi" : "Lưu thông tin", "success", 14);
        btnHuy = new ButtonCustom("Huỷ bỏ", "danger", 14);

        btnLuu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    luuDeThi(type);
                }
            }
        });

        btnHuy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

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
        ArrayList<KyThiDTO> listKT = kyThiBUS.getAll();
        ArrayList<MonHocDTO> listMH = monHocBUS.getAll();

        int indexKT = cbxKyThi.getCbb().getSelectedIndex() - 1;
        int indexMH = cbxMonHoc.getCbb().getSelectedIndex() - 1;

        int maKyThi = indexKT >= 0 ? listKT.get(indexKT).getMakythi() : 0;
        int maMonHoc = indexMH >= 0 ? listMH.get(indexMH).getMamonhoc() : 0;
        int thoiGian = Integer.parseInt(txtThoiGian.getText().trim());

        ArrayList<Integer> listMaCauHoi = new ArrayList<>(selectedCauHoi);

        if (type.equals("create")) {
            DeThiDTO dt = new DeThiDTO();
            dt.setTende(tenDe.getText().trim());
            dt.setMakythi(maKyThi);
            dt.setMonthi(maMonHoc);
            dt.setThoigianthi(thoiGian);
            dt.setTongsocau(listMaCauHoi.size());
            dt.setThoigiantao(new Timestamp(System.currentTimeMillis()));
            dt.setNguoitao("admin");
            dt.setTrangthai(true);

            if (deThiBUS.add(dt)) {
                // Lấy made vừa tạo bằng cách reload và lấy phần tử đầu tiên (ORDER BY made DESC)
                ArrayList<DeThiDTO> all = deThiBUS.getAll();
                if (!all.isEmpty()) {
                    int madeVuaTao = all.get(0).getMade();
                    deThiBUS.saveChiTiet(madeVuaTao, listMaCauHoi);
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
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                parent.loadDataTable(deThiBUS.getAll());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}