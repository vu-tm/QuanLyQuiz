package GUI.Dialog;

import BUS.DoKhoBUS;
import BUS.KyThiBUS;
import DTO.DoKhoDTO;
import GUI.Component.InputForm;
import GUI.Component.SelectForm;
import GUI.Component.NumericDocumentFilter;
import GUI.Component.ButtonCustom;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.PlainDocument;

public class DeThiDialog extends JDialog {

    private InputForm tenDe, soCau;
    private SelectForm cbxKyThi, cbxLop;
    private JTextField txtThoiGian, txtSearch;
    private JComboBox<String> cbxFilterDoKho;
    private ButtonCustom btnTao, btnHuy;
    private JPanel pnlInfo, pnlSelect, pnlQuestionList, pnlButtons;
    private JScrollPane scrollQuestion;

    private KyThiBUS kyThiBUS = new KyThiBUS();
    private DoKhoBUS doKhoBUS = new DoKhoBUS();
    private int countSelected = 0;

    public DeThiDialog(JFrame owner, String title, boolean modal, String type) {
        super(owner, title, modal);
        this.setTitle(title);
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

        // Bên trái
        this.add(pnlInfo, BorderLayout.WEST);

        // Bên phải
        this.add(pnlSelect, BorderLayout.CENTER);

        // Bên dưới
        this.add(pnlButtons, BorderLayout.SOUTH);

        if (type.equals("view")) {
            tenDe.setDisable();
            soCau.setDisable();
            txtThoiGian.setEnabled(false);
            cbxKyThi.getCbb().setEnabled(false);
            cbxLop.getCbb().setEnabled(false);
        }

        this.setVisible(true);
    }

    private void initPnlInfo() {
        pnlInfo = new JPanel(new GridLayout(5, 1, 0, 10));
        pnlInfo.setPreferredSize(new Dimension(320, 0));
        pnlInfo.setBorder(new EmptyBorder(20, 20, 20, 10));
        pnlInfo.setBackground(Color.WHITE);

        tenDe = new InputForm("Tên đề thi");

        ArrayList<DTO.KyThiDTO> listKT = kyThiBUS.getAll();
        String[] dsKyThi = new String[listKT.size() + 1];
        dsKyThi[0] = "Chọn kỳ thi";
        for (int i = 0; i < listKT.size(); i++) {
            dsKyThi[i + 1] = listKT.get(i).getTenkythi();
        }
        cbxKyThi = new SelectForm("Kỳ thi", dsKyThi);

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

        cbxLop = new SelectForm("Giao cho lớp", new String[]{"Chọn lớp", "Lớp 10A1", "Lớp 11B2", "Lớp 12C3"});

        soCau = new InputForm("Số câu hỏi đã chọn");
        soCau.getTxtForm().setEditable(false);
        soCau.getTxtForm().setText("0");

        pnlInfo.add(tenDe);
        pnlInfo.add(cbxKyThi);
        pnlInfo.add(pnlTimeGroup);
        pnlInfo.add(cbxLop);
        pnlInfo.add(soCau);
    }

    private void initPnlSelect() {
        pnlSelect = new JPanel(new BorderLayout(10, 10));
        pnlSelect.setBorder(new EmptyBorder(20, 10, 20, 20));
        pnlSelect.setBackground(Color.WHITE);

        // Thanh tìm kiếm
        JPanel pnlSearchTool = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearchTool.setBackground(Color.WHITE);
        txtSearch = new JTextField(15);
        txtSearch.setPreferredSize(new Dimension(150, 35));
        ArrayList<DoKhoDTO> listDK = doKhoBUS.getAll();
        String[] dsDoKho = new String[listDK.size() + 1];
        dsDoKho[0] = "Tất cả độ khó";
        for (int i = 0; i < listDK.size(); i++) {
            dsDoKho[i + 1] = listDK.get(i).getTendokho();
        }
        cbxFilterDoKho = new JComboBox<>(dsDoKho);
        cbxFilterDoKho.setPreferredSize(new Dimension(120, 35));

        pnlSearchTool.add(new JLabel("Tìm kiếm:"));
        pnlSearchTool.add(txtSearch);
        pnlSearchTool.add(cbxFilterDoKho);

        // Danh sách câu hỏi
        pnlQuestionList = new JPanel();
        pnlQuestionList.setLayout(new BoxLayout(pnlQuestionList, BoxLayout.Y_AXIS));
        pnlQuestionList.setBackground(Color.WHITE);
        addQuestionToPanel("OOP là viết tắt của từ nào?", "Dễ");
        addQuestionToPanel("Đặc điểm cơ bản của lập trình hướng đối tượng là gì?", "TB");
        addQuestionToPanel("Thế nào là tính đa hình?", "Khó");
        addQuestionToPanel("Java có bao nhiêu kiểu dữ liệu nguyên thủy?", "Dễ");
        addQuestionToPanel("OOP là viết tắt của từ nào?", "Dễ");
        addQuestionToPanel("Đặc điểm cơ bản của lập trình hướng đối tượng là gì?", "TB");
        addQuestionToPanel("Thế nào là tính đa hình?", "Khó");
        addQuestionToPanel("Java có bao nhiêu kiểu dữ liệu nguyên thủy?", "Dễ");
        addQuestionToPanel("OOP là viết tắt của từ nào?", "Dễ");
        addQuestionToPanel("Đặc điểm cơ bản của lập trình hướng đối tượng là gì?", "TB");
        addQuestionToPanel("Thế nào là tính đa hình?", "Khó");
        addQuestionToPanel("Java có bao nhiêu kiểu dữ liệu nguyên thủy?", "Dễ");
        addQuestionToPanel("OOP là viết tắt của từ nào?", "Dễ");
        addQuestionToPanel("Đặc điểm cơ bản của lập trình hướng đối tượng là gì?", "TB");
        addQuestionToPanel("Thế nào là tính đa hình?", "Khó");
        addQuestionToPanel("Java có bao nhiêu kiểu dữ liệu nguyên thủy?", "Dễ");

        scrollQuestion = new JScrollPane(pnlQuestionList);
        scrollQuestion.setBorder(new LineBorder(new Color(230, 230, 230)));
        scrollQuestion.getVerticalScrollBar().setUnitIncrement(16);

        pnlSelect.add(pnlSearchTool, BorderLayout.NORTH);
        pnlSelect.add(scrollQuestion, BorderLayout.CENTER);
    }

    private void initPnlButtons(String type) {
        pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlButtons.setBackground(Color.WHITE);
        pnlButtons.setBorder(new EmptyBorder(10, 0, 10, 0));

        btnTao = new ButtonCustom(type.equals("create") ? "Tạo đề thi" : "Lưu thông tin", "success", 14);
        btnHuy = new ButtonCustom("Huỷ bỏ", "danger", 14);

        btnHuy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        if (!type.equals("view")) {
            pnlButtons.add(btnTao);
        }
        pnlButtons.add(btnHuy);
    }

    private void addQuestionToPanel(String content, String level) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 235, 235)),
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));

        JCheckBox chk = new JCheckBox();
        chk.setBackground(Color.WHITE);
        chk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (chk.isSelected()) {
                    countSelected++;
                } else {
                    countSelected--;
                }
                soCau.getTxtForm().setText(String.valueOf(countSelected));
            }
        });

        JLabel lblContent = new JLabel("<html>" + content + "</html>");

        JLabel lblLevel = new JLabel(level);
        lblLevel.setOpaque(true);
        lblLevel.setHorizontalAlignment(SwingConstants.CENTER);
        lblLevel.setPreferredSize(new Dimension(60, 25));
        if (level.equals("Dễ")) {
            lblLevel.setBackground(Color.GREEN);
        } else if (level.equals("TB")) {
            lblLevel.setBackground(Color.YELLOW);
        } else {
            lblLevel.setBackground(Color.ORANGE);
        }

        item.add(chk, BorderLayout.WEST);
        item.add(lblContent, BorderLayout.CENTER);
        item.add(lblLevel, BorderLayout.EAST);

        pnlQuestionList.add(item);
    }
}
