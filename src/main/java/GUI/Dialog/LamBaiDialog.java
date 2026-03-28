package GUI.Dialog;

import BUS.DeThiBUS;
import BUS.DapAnBUS;
import DAO.BaiThiDAO;
import DAO.ChiTietBaiThiDAO;
import DTO.BaiThiDTO;
import DTO.CauHoiDTO;
import DTO.ChiTietBaiThiDTO;
import DTO.DapAnDTO;
import DTO.DeThiDTO;
import DTO.NguoiDungDTO;
import GUI.Component.ButtonCustom;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LamBaiDialog extends JDialog {

    private static final Color C_BLUE = new Color(25, 118, 210);
    private static final Color C_BLUE_LIGHT = new Color(227, 242, 253);
    private static final Color C_ORANGE = new Color(230, 81, 0);
    private static final Color C_BG = new Color(240, 244, 248);
    private static final Color C_WHITE = Color.WHITE;
    private static final Color C_BORDER = new Color(207, 216, 220);
    private static final Color C_TEXT = new Color(26, 35, 50);
    private static final Color C_TEXT2 = new Color(84, 110, 122);

    private final DeThiDTO deThi;
    private final NguoiDungDTO user;
    private final ArrayList<CauHoiDTO> dsCauHoi;
    private final HashMap<Integer, String> userAnswers = new HashMap<>();
    private int currentIdx = 0;
    private int timeLeft;
    private Timer timer;
    private Timestamp startTime;

    private JLabel lblTimer, lblHoTen, lblQLabel;
    private JTextArea txtQuestion;
    private JPanel pnlAnswers, pnlAnswerBar;
    private JButton[] btnNavs;
    private JLabel lblStatAnswered, lblStatLeft;
    private JButton btnPrev, btnNext;
    private JLabel lblProgress;
    private JTextField txtFillInput;

    public LamBaiDialog(JFrame parent, DeThiDTO deThi, NguoiDungDTO user) {
        super(parent, "Phần mềm thi trắc nghiệm", true);
        this.deThi = deThi;
        this.user = user;
        this.timeLeft = deThi.getThoigianthi() * 60;
        this.startTime = new Timestamp(System.currentTimeMillis());

        DeThiBUS dtBus = new DeThiBUS();
        this.dsCauHoi = dtBus.getDanhSachCauHoiByMade(deThi.getMade());

        initComponents();
        loadQuestion(0);
        startTimer();

        setSize(1200, 750);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(C_BG);
        add(buildHeader(), BorderLayout.NORTH);
        add(buildMain(), BorderLayout.CENTER);
        setupKeyBindings();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitQuiz();
            }
        });
    }

    private JPanel buildHeader() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(C_WHITE);
        pnl.setPreferredSize(new Dimension(0, 64));
        pnl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDER));

        JPanel leftPnl = new JPanel(new GridBagLayout());
        leftPnl.setOpaque(false);
        leftPnl.setBorder(new EmptyBorder(0, 20, 0, 0));

        ButtonCustom btnThoat = new ButtonCustom("THOÁT", "danger", 13, 110, 40);
        btnThoat.addActionListener(e -> exitQuiz());
        btnThoat.setFocusable(false);
        leftPnl.add(btnThoat);

        JPanel centerPnl = new JPanel(new GridBagLayout());
        centerPnl.setOpaque(false);
        lblHoTen = new JLabel("Thí sinh: " + user.getHoten());
        lblHoTen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHoTen.setForeground(C_TEXT);
        centerPnl.add(lblHoTen);

        JPanel rightPnl = new JPanel(new GridBagLayout());
        rightPnl.setOpaque(false);
        rightPnl.setBorder(new EmptyBorder(0, 0, 0, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 0);

        JPanel timerBox = new JPanel(new GridBagLayout());
        timerBox.setBackground(new Color(245, 247, 249));
        timerBox.setPreferredSize(new Dimension(150, 40));
        timerBox.setBorder(BorderFactory.createLineBorder(C_BORDER));

        JLabel clockIcon = new JLabel(new FlatSVGIcon("icon/clock.svg", 18, 18));
        lblTimer = new JLabel("00:00:00");
        lblTimer.setFont(new Font("Consolas", Font.BOLD, 18));
        lblTimer.setForeground(C_TEXT);

        GridBagConstraints gbcTimer = new GridBagConstraints();
        gbcTimer.gridx = 0;
        gbcTimer.gridy = 0;
        gbcTimer.anchor = GridBagConstraints.CENTER;
        timerBox.add(clockIcon, gbcTimer);

        gbcTimer.gridx = 1;
        gbcTimer.insets = new Insets(3, 8, 0, 0);
        timerBox.add(lblTimer, gbcTimer);

        ButtonCustom btnNop = new ButtonCustom("NỘP BÀI", "success", 13, 110, 40);
        btnNop.addActionListener(e -> confirmSubmit());
        btnNop.setFocusable(false);

        rightPnl.add(timerBox, gbc);
        rightPnl.add(btnNop, gbc);

        pnl.add(leftPnl, BorderLayout.WEST);
        pnl.add(centerPnl, BorderLayout.CENTER);
        pnl.add(rightPnl, BorderLayout.EAST);
        return pnl;
    }

    private JPanel buildMain() {
        JPanel pnl = new JPanel(new BorderLayout(20, 0));
        pnl.setOpaque(false);
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.add(buildLeft(), BorderLayout.CENTER);
        pnl.add(buildRight(), BorderLayout.EAST);
        return pnl;
    }

    private JPanel buildLeft() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setOpaque(false);

        JPanel qCard = makeCard();
        qCard.setLayout(new BorderLayout(0, 15));
        qCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        lblQLabel = new JLabel("Câu 1 / " + dsCauHoi.size());
        lblQLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblQLabel.setForeground(C_BLUE);

        txtQuestion = new JTextArea();
        txtQuestion.setEditable(false);
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        txtQuestion.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtQuestion.setForeground(C_TEXT);
        txtQuestion.setBackground(C_WHITE);

        qCard.add(lblQLabel, BorderLayout.NORTH);
        qCard.add(txtQuestion, BorderLayout.CENTER);

        pnlAnswers = new JPanel(new GridLayout(0, 2, 20, 20));
        pnlAnswers.setOpaque(false);

        pnlAnswerBar = buildAnswerBar();
        JPanel navRow = buildNavRow();

        pnl.add(qCard);
        pnl.add(Box.createVerticalStrut(20));
        pnl.add(pnlAnswers);
        pnl.add(Box.createVerticalGlue());
        pnl.add(pnlAnswerBar);
        pnl.add(Box.createVerticalStrut(15));
        pnl.add(navRow);

        return pnl;
    }

    private JPanel buildAnswerBar() {
        JPanel bar = new JPanel(new GridBagLayout());
        bar.setBackground(C_BLUE);
        bar.setPreferredSize(new Dimension(0, 55));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        return bar;
    }

    private void rebuildAnswerBar(String[] keys, ArrayList<DapAnDTO> dsDapAn, int index) {
        pnlAnswerBar.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        JLabel lbl = new JLabel("Đáp án của bạn:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(255, 255, 255, 200));
        pnlAnswerBar.add(lbl, gbc);

        for (int i = 0; i < dsDapAn.size() && i < keys.length; i++) {
            final String key = keys[i];
            final String ansId = String.valueOf(dsDapAn.get(i).getMadapan());
            boolean chosen = ansId.equals(userAnswers.get(index));

            JButton chip = new JButton(key);
            chip.setFont(new Font("Segoe UI", Font.BOLD, 14));
            chip.setPreferredSize(new Dimension(38, 38));
            chip.setFocusPainted(false);
            chip.setBackground(chosen ? C_WHITE : C_BLUE);
            chip.setForeground(chosen ? C_BLUE : new Color(255, 255, 255, 150));
            chip.setBorder(BorderFactory.createLineBorder(chosen ? C_WHITE : new Color(255, 255, 255, 100)));
            pnlAnswerBar.add(chip, gbc);
        }
        pnlAnswerBar.revalidate();
        pnlAnswerBar.repaint();
    }

    private JPanel buildNavRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        btnPrev = makeNavArrow("Câu trước");
        btnNext = makeNavArrow("Câu tiếp");
        btnPrev.setFocusable(false);
        btnNext.setFocusable(false);

        lblProgress = new JLabel("1 / " + dsCauHoi.size(), JLabel.CENTER);
        lblProgress.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblProgress.setForeground(C_TEXT2);

        btnPrev.addActionListener(e -> goTo(currentIdx - 1));
        btnNext.addActionListener(e -> goTo(currentIdx + 1));

        row.add(btnPrev, BorderLayout.WEST);
        row.add(lblProgress, BorderLayout.CENTER);
        row.add(btnNext, BorderLayout.EAST);
        return row;
    }

    private JButton makeNavArrow(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(C_BLUE);
        btn.setBackground(C_WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setBorder(BorderFactory.createLineBorder(new Color(176, 208, 245)));
        return btn;
    }

    private void loadQuestion(int index) {
        currentIdx = index;
        CauHoiDTO q = dsCauHoi.get(index);

        lblQLabel.setText("Câu " + (index + 1) + " / " + dsCauHoi.size());
        txtQuestion.setText(q.getNoidung());
        lblProgress.setText((index + 1) + " / " + dsCauHoi.size());

        btnPrev.setEnabled(index > 0);
        btnNext.setEnabled(index < dsCauHoi.size() - 1);

        pnlAnswers.removeAll();
        DapAnBUS daBus = new DapAnBUS();

        if (q.getMaloai() == 3) { // Điền khuyết
            pnlAnswers.setLayout(new BorderLayout());
            JPanel pnlFill = new JPanel(new BorderLayout(10, 10));
            pnlFill.setOpaque(false);
            pnlFill.setBorder(new EmptyBorder(20, 0, 20, 0));

            JLabel lblHint = new JLabel("Nhập đáp án của bạn vào đây:");
            lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblHint.setForeground(C_TEXT2);

            txtFillInput = new JTextField();
            txtFillInput.setFont(new Font("Segoe UI", Font.BOLD, 18));
            txtFillInput.setPreferredSize(new Dimension(0, 50));
            SwingUtilities.invokeLater(() -> txtFillInput.requestFocusInWindow());

            String oldAns = userAnswers.get(index);
            txtFillInput.setText(oldAns != null ? oldAns : "");

            txtFillInput.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    save();
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    save();
                }

                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    save();
                }

                public void save() {
                    userAnswers.put(index, txtFillInput.getText().trim());
                    updateNavStatus(index);
                    if (btnNavs != null) {
                        styleNavNum(btnNavs[index], index, index);
                    }
                    rebuildAnswerBarForFill(index);
                }
            });

            pnlFill.add(lblHint, BorderLayout.NORTH);
            pnlFill.add(txtFillInput, BorderLayout.CENTER);
            pnlAnswers.add(pnlFill, BorderLayout.NORTH);
            rebuildAnswerBarForFill(index);
        } else { // Trắc nghiệm / Đúng sai
            pnlAnswers.setLayout(new GridLayout(0, 2, 20, 20));
            ArrayList<DapAnDTO> dsDapAn = daBus.getDapAnDeHienThi(q.getMacauhoi());
            String[] keys = {"A", "B", "C", "D"};

            for (int i = 0; i < dsDapAn.size(); i++) {
                DapAnDTO da = dsDapAn.get(i);
                String key = (i < keys.length) ? keys[i] : String.valueOf(i + 1);
                boolean selected = String.valueOf(da.getMadapan()).equals(userAnswers.get(index));

                JButton btn = makeAnswerButton(key, da.getNoidungtl(), selected);
                final String ansId = String.valueOf(da.getMadapan());
                btn.addActionListener(e -> {
                    userAnswers.put(index, ansId);
                    updateNavStatus(index);
                    loadQuestion(index);
                });
                pnlAnswers.add(btn);
            }
            rebuildAnswerBar(keys, dsDapAn, index);
        }

        if (btnNavs != null) {
            for (int i = 0; i < btnNavs.length; i++) {
                styleNavNum(btnNavs[i], i, index);
            }
        }
        pnlAnswers.revalidate();
        pnlAnswers.repaint();
    }

    private JButton makeAnswerButton(String key, String text, boolean selected) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(15, 0));
        btn.setBackground(selected ? C_BLUE_LIGHT : C_WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 65));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(selected ? C_BLUE : C_BORDER, 1),
                new EmptyBorder(12, 18, 12, 18)
        ));

        JLabel keyLbl = new JLabel(key, JLabel.CENTER);
        keyLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        keyLbl.setOpaque(true);
        keyLbl.setBackground(selected ? C_BLUE : new Color(245, 247, 249));
        keyLbl.setForeground(selected ? C_WHITE : C_TEXT2);
        keyLbl.setPreferredSize(new Dimension(34, 34));

        JLabel textLbl = new JLabel("<html>" + escapeHtml(text) + "</html>");
        textLbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textLbl.setForeground(C_TEXT);

        btn.add(keyLbl, BorderLayout.WEST);
        btn.add(textLbl, BorderLayout.CENTER);
        return btn;
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            if (timeLeft <= 0) {
                timer.stop();
                submitQuiz();
                return;
            }
            int h = timeLeft / 3600, m = (timeLeft % 3600) / 60, s = timeLeft % 60;
            lblTimer.setText(String.format("%02d:%02d:%02d", h, m, s));
        });
        timer.start();
    }

    private void updateNavStatus(int index) {
        int answered = userAnswers.size();
        lblStatAnswered.setText(String.valueOf(answered));
        lblStatLeft.setText(String.valueOf(dsCauHoi.size() - answered));
    }

    private void goTo(int idx) {
        if (idx < 0 || idx >= dsCauHoi.size()) {
            return;
        }
        loadQuestion(idx);
    }

    private void confirmSubmit() {
        if (JOptionPane.showConfirmDialog(this, "Xác nhận nộp bài?", "Thông báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            submitQuiz();
        }
    }

    private void rebuildAnswerBarForFill(int index) {
        pnlAnswerBar.removeAll();
        String ans = userAnswers.get(index);
        JLabel lbl = new JLabel("Đã nhập: " + (ans != null && !ans.isEmpty() ? ans : "..."));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        pnlAnswerBar.add(lbl);
        pnlAnswerBar.revalidate();
        pnlAnswerBar.repaint();
    }

    private void submitQuiz() {
        if (dsCauHoi.get(currentIdx).getMaloai() == 3 && txtFillInput != null) {
            userAnswers.put(currentIdx, txtFillInput.getText().trim());
        }

        timer.stop();
        int socaudung = 0;
        DapAnBUS daBus = new DapAnBUS();

        for (int i = 0; i < dsCauHoi.size(); i++) {
            CauHoiDTO q = dsCauHoi.get(i);
            String userAns = userAnswers.get(i);
            if (userAns == null || userAns.isEmpty()) {
                continue;
            }

            ArrayList<DapAnDTO> dsDung = daBus.getDapAnDungByCauHoi(q.getMacauhoi());

            if (q.getMaloai() == 3) { // Điền khuyết
                if (!dsDung.isEmpty()) {
                    String correctText = dsDung.get(0).getNoidungtl().trim();
                    if (userAns.equalsIgnoreCase(correctText)) {
                        socaudung++;
                    }
                }
            } else { // Trắc nghiệm / Đúng sai
                try {
                    int chosenId = Integer.parseInt(userAns);
                    for (DapAnDTO da : dsDung) {
                        if (da.getMadapan() == chosenId) {
                            socaudung++;
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                }
            }
        }

        double diem = (double) socaudung * 10.0 / dsCauHoi.size();
        diem = Math.round(diem * 100.0) / 100.0;

        BaiThiDTO bt = new BaiThiDTO();
        bt.setMade(deThi.getMade());
        bt.setManguoidung(user.getManguoidung());
        bt.setDiemthi(diem);
        bt.setThoigianvaothi(this.startTime);
        bt.setThoigianlambai((deThi.getThoigianthi() * 60) - timeLeft);
        bt.setSocaudung(socaudung);
        bt.setSocausai(dsCauHoi.size() - socaudung);

        int mabaithiGenerated = BaiThiDAO.getInstance().insert(bt);
        if (mabaithiGenerated > 0) {
            ChiTietBaiThiDAO ctDAO = ChiTietBaiThiDAO.getInstance();
            for (int i = 0; i < dsCauHoi.size(); i++) {
                ChiTietBaiThiDTO ct = new ChiTietBaiThiDTO();
                ct.setMabaithi(mabaithiGenerated);
                ct.setMacauhoi(dsCauHoi.get(i).getMacauhoi());

                String ans = userAnswers.get(i);
                if (dsCauHoi.get(i).getMaloai() == 3) {
                    ct.setDapanchon(0);
                    ct.setNoidungdienkhuyet(ans != null ? ans : "");
                } else {
                    ct.setDapanchon(ans != null ? Integer.parseInt(ans) : 0);
                    ct.setNoidungdienkhuyet("");
                }
                ctDAO.insert(ct);
            }
            JOptionPane.showMessageDialog(this, "Nộp bài thành công! Điểm: " + diem);
        }
        this.dispose();
    }

    private void exitQuiz() {
        if (JOptionPane.showConfirmDialog(this, "Thoát và không lưu kết quả?", "Cảnh báo", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            timer.stop();
            this.dispose();
        }
    }

    private JPanel makeCard() {
        JPanel p = new JPanel();
        p.setBackground(C_WHITE);
        p.setBorder(BorderFactory.createLineBorder(C_BORDER));
        return p;
    }

    private JPanel buildRight() {
        JPanel card = makeCard();
        card.setPreferredSize(new Dimension(250, 0));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 14, 16, 14));

        JPanel topPart = new JPanel();
        topPart.setLayout(new BoxLayout(topPart, BoxLayout.Y_AXIS));
        topPart.setOpaque(false);

        JLabel title = new JLabel("BẢNG CÂU HỎI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(C_TEXT2);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel stats = new JPanel(new GridLayout(1, 3, 8, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        lblStatAnswered = new JLabel("0", JLabel.CENTER);
        lblStatLeft = new JLabel(String.valueOf(dsCauHoi.size()), JLabel.CENTER);
        JLabel lblTotal = new JLabel(String.valueOf(dsCauHoi.size()), JLabel.CENTER);
        stats.add(makeStatCard(lblStatAnswered, "Đã làm"));
        stats.add(makeStatCard(lblStatLeft, "Còn lại"));
        stats.add(makeStatCard(lblTotal, "Tổng"));

        JPanel grid = new JPanel(new GridLayout(0, 4, 8, 8));
        grid.setOpaque(false);
        btnNavs = new JButton[dsCauHoi.size()];
        for (int i = 0; i < dsCauHoi.size(); i++) {
            btnNavs[i] = makeNavNum(i + 1);
            final int idx = i;
            btnNavs[i].addActionListener(e -> loadQuestion(idx));
            grid.add(btnNavs[i]);
        }

        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setOpaque(false);
        gridWrapper.add(grid, BorderLayout.NORTH);

        topPart.add(title);
        topPart.add(Box.createVerticalStrut(12));
        topPart.add(stats);
        topPart.add(Box.createVerticalStrut(15));
        topPart.add(new JSeparator());
        topPart.add(Box.createVerticalStrut(15));
        topPart.add(gridWrapper);

        JPanel botPart = new JPanel();
        botPart.setLayout(new BoxLayout(botPart, BoxLayout.Y_AXIS));
        botPart.setOpaque(false);
        botPart.add(new JSeparator());
        botPart.add(Box.createVerticalStrut(12));
        botPart.add(makeLegendItem(C_BLUE, C_BLUE_LIGHT, "Đã trả lời"));
        botPart.add(Box.createVerticalStrut(6));
        botPart.add(makeLegendItem(C_BLUE, C_BLUE, "Câu hiện tại"));
        botPart.add(Box.createVerticalStrut(6));
        botPart.add(makeLegendItem(C_BORDER, C_WHITE, "Chưa làm"));

        card.add(topPart, BorderLayout.CENTER);
        card.add(botPart, BorderLayout.SOUTH);

        return card;
    }

    private JButton makeNavNum(int num) {
        JButton btn = new JButton(String.valueOf(num));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(42, 42));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleNavNum(JButton btn, int i, int currentIndex) {
        boolean isCurrent = (i == currentIndex);
        boolean isAnswered = userAnswers.containsKey(i) && !userAnswers.get(i).isEmpty();
        if (isCurrent) {
            btn.setBackground(C_BLUE);
            btn.setForeground(C_WHITE);
        } else if (isAnswered) {
            btn.setBackground(C_BLUE_LIGHT);
            btn.setForeground(C_BLUE);
        } else {
            btn.setBackground(C_WHITE);
            btn.setForeground(C_TEXT2);
        }
    }

    private JPanel makeStatCard(JLabel numLabel, String caption) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(236, 239, 241));
        numLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        numLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel capLbl = new JLabel(caption, JLabel.CENTER);
        capLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        capLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(Box.createVerticalStrut(6));
        p.add(numLabel);
        p.add(capLbl);
        p.add(Box.createVerticalStrut(6));
        return p;
    }

    private JPanel makeLegendItem(Color borderColor, Color bgColor, String label) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        JPanel dot = new JPanel();
        dot.setPreferredSize(new Dimension(14, 14));
        dot.setBackground(bgColor);
        dot.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        row.add(dot);
        row.add(lbl);
        return row;
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private void setupKeyBindings() {
        getRootPane().setDefaultButton(null);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        // Chỉ xử lý khi dialog này đang active và là KEY_PRESSED
                        if (!isActive() || e.getID() != KeyEvent.KEY_PRESSED) {
                            return false;
                        }

                        int code = e.getKeyCode();

                        // Câu điền khuyết: chỉ cho Enter chuyển câu, bỏ qua 1-4
                        boolean fillFocused = (txtFillInput != null && txtFillInput.isFocusOwner());

                        if (code == KeyEvent.VK_ENTER) {
                            if (fillFocused) {
                                // Enter trong ô điền khuyết -> chuyển câu hoặc nộp bài
                                if (currentIdx == dsCauHoi.size() - 1) {
                                    confirmSubmit();
                                } else {
                                    goTo(currentIdx + 1);
                                }
                                return true;
                            } else {
                                if (currentIdx == dsCauHoi.size() - 1) {
                                    confirmSubmit();
                                } else {
                                    goTo(currentIdx + 1);
                                }
                                return true;
                            }
                        }

                        // Phím 1-4 chỉ hoạt động khi không focus vào ô điền khuyết
                        if (!fillFocused) {
                            CauHoiDTO q = dsCauHoi.get(currentIdx);
                            if (q.getMaloai() != 3) {
                                int answerIdx = -1;
                                if (code == KeyEvent.VK_1) {
                                    answerIdx = 0;
                                } else if (code == KeyEvent.VK_2) {
                                    answerIdx = 1;
                                } else if (code == KeyEvent.VK_3) {
                                    answerIdx = 2;
                                } else if (code == KeyEvent.VK_4) {
                                    answerIdx = 3;
                                }

                                if (answerIdx >= 0) {
                                    DapAnBUS daBus = new DapAnBUS();
                                    ArrayList<DapAnDTO> dsDapAn = daBus.getDapAnDeHienThi(q.getMacauhoi());
                                    if (answerIdx < dsDapAn.size()) {
                                        String ansId = String.valueOf(dsDapAn.get(answerIdx).getMadapan());
                                        userAnswers.put(currentIdx, ansId);
                                        updateNavStatus(currentIdx);
                                        loadQuestion(currentIdx);
                                    }
                                    return true;
                                }
                            }
                        }

                        return false;
                    }
                });
    }
}
