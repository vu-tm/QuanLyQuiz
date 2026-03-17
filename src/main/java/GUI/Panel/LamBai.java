package GUI.Panel;

import BUS.BaiThiBUS;
import BUS.DeThiBUS;
import BUS.DapAnBUS;
import DTO.CauHoiDTO;
import DTO.DapAnDTO;
import DTO.DeThiDTO;
import GUI.Main;
import GUI.Component.PanelBorderRadius;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LamBai extends JPanel {

    private DeThiDTO deThi;
    private ArrayList<CauHoiDTO> dsCauHoi;
    private ArrayList<ArrayList<DapAnDTO>> dsDapAnList = new ArrayList<>();
    private ArrayList<String> answersList = new ArrayList<>(); // index matches dsCauHoi, contains madapan or fill-in text
    private int currentQuestionIndex = 0;
    private int secondsRemaining;
    private Timer timer;

    private JLabel lblTime, lblQuestionNum, lblQuestionContent;
    private JPanel pnlAnswers, pnlQuestionList;
    private JButton btnPrev, btnNext, btnFinish;
    private JButton[] btnQuestionNav;

    private DeThiBUS deThiBUS = new DeThiBUS();
    private DapAnBUS dapAnBUS = new DapAnBUS();
    private BaiThiBUS baiThiBUS = new BaiThiBUS();

    public LamBai(DeThiDTO deThi) {
        this.deThi = deThi;
        this.secondsRemaining = deThi.getThoigianthi() * 60;
        this.dsCauHoi = deThiBUS.getDanhSachCauHoiByMade(deThi.getMade());
        for (CauHoiDTO ch : dsCauHoi) {
            dsDapAnList.add(dapAnBUS.getByMaCauHoi(ch.getMacauhoi()));
            answersList.add(""); // Initialize with empty strings
        }
        initComponent();
        startTimer();
        displayQuestion(0);
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBackground(new Color(248, 250, 252));

        // Header: Navy Blue Premium
        JPanel pnlHeader = new JPanel(new BorderLayout(20, 0));
        pnlHeader.setBackground(new Color(15, 23, 42)); // Slate 900
        pnlHeader.setPreferredSize(new Dimension(0, 80));
        pnlHeader.setBorder(new EmptyBorder(0, 40, 0, 40));

        JLabel lblTitle = new JLabel(deThi.getTende().toUpperCase());
        lblTitle.setFont(new Font("Inter", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        // Timer Card
        PanelBorderRadius pnlTimerCard = new PanelBorderRadius();
        pnlTimerCard.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 8));
        pnlTimerCard.setBackground(new Color(30, 41, 59)); // Slate 800
        pnlTimerCard.putClientProperty("FlatLaf.style", "arc: 12");
        
        lblTime = new JLabel(formatTime(secondsRemaining));
        lblTime.setFont(new Font("Monospaced", Font.BOLD, 28));
        lblTime.setForeground(new Color(251, 191, 36)); // Amber 400
        lblTime.setIcon(new com.formdev.flatlaf.extras.FlatSVGIcon("icon/clock.svg", 24, 24));
        pnlTimerCard.add(lblTime);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlTimerCard, BorderLayout.EAST);
        this.add(pnlHeader, BorderLayout.NORTH);

        // Center Content with Card
        JPanel pnlCenterContainer = new JPanel(new GridBagLayout());
        pnlCenterContainer.setOpaque(false);
        pnlCenterContainer.setBorder(new EmptyBorder(30, 30, 30, 30));

        PanelBorderRadius pnlContentCard = new PanelBorderRadius();
        pnlContentCard.setLayout(new BorderLayout(0, 30));
        pnlContentCard.setBackground(Color.WHITE);
        pnlContentCard.setBorder(new EmptyBorder(40, 50, 40, 50));
        pnlContentCard.putClientProperty("FlatLaf.style", "arc: 24; [light]background: #ffffff; [dark]background: #1e293b;");
        
        // Question Header
        lblQuestionNum = new JLabel("Câu hỏi 1");
        lblQuestionNum.setFont(new Font("Inter", Font.BOLD, 28));
        lblQuestionNum.setForeground(new Color(51, 65, 85));
        
        lblQuestionContent = new JLabel("Nội dung câu hỏi...");
        lblQuestionContent.setFont(new Font("Inter", Font.PLAIN, 22));
        lblQuestionContent.setVerticalAlignment(JLabel.TOP);
        
        pnlAnswers = new JPanel();
        pnlAnswers.setLayout(new BoxLayout(pnlAnswers, BoxLayout.Y_AXIS));
        pnlAnswers.setOpaque(false);
        
        JPanel pnlQuestionContent = new JPanel(new BorderLayout(0, 20));
        pnlQuestionContent.setOpaque(false);
        pnlQuestionContent.add(lblQuestionContent, BorderLayout.NORTH);
        pnlQuestionContent.add(pnlAnswers, BorderLayout.CENTER);

        JScrollPane scrollQuestion = new JScrollPane(pnlQuestionContent);
        scrollQuestion.setBorder(null);
        scrollQuestion.setOpaque(false);
        scrollQuestion.getViewport().setOpaque(false);
        scrollQuestion.getVerticalScrollBar().setUnitIncrement(16);
        
        pnlContentCard.add(lblQuestionNum, BorderLayout.NORTH);
        pnlContentCard.add(scrollQuestion, BorderLayout.CENTER);
        
        pnlCenterContainer.add(pnlContentCard);
        this.add(pnlCenterContainer, BorderLayout.CENTER);

        // Bottom: Navigation Dock
        PanelBorderRadius pnlDock = new PanelBorderRadius();
        pnlDock.setLayout(new BorderLayout(20, 0));
        pnlDock.setBackground(Color.WHITE);
        pnlDock.setPreferredSize(new Dimension(0, 160));
        pnlDock.setBorder(new EmptyBorder(15, 30, 15, 30));
        pnlDock.putClientProperty("FlatLaf.style", "arc: 0; border: 1,0,0,0,#e2e8f0");

        // Question Buttons
        pnlQuestionList = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        pnlQuestionList.setOpaque(false);
        
        btnQuestionNav = new JButton[dsCauHoi.size()];
        for (int i = 0; i < dsCauHoi.size(); i++) {
            final int index = i;
            btnQuestionNav[i] = new JButton(String.valueOf(i + 1));
            btnQuestionNav[i].setPreferredSize(new Dimension(50, 50));
            btnQuestionNav[i].setFocusable(false);
            btnQuestionNav[i].setFont(new Font("Inter", Font.BOLD, 15));
            btnQuestionNav[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnQuestionNav[i].putClientProperty("JButton.buttonType", "roundRect");
            btnQuestionNav[i].putClientProperty("FlatLaf.style", "arc: 999");
            btnQuestionNav[i].addActionListener(e -> displayQuestion(index));
            pnlQuestionList.add(btnQuestionNav[i]);
        }
        
        JScrollPane scrollNav = new JScrollPane(pnlQuestionList);
        scrollNav.setBorder(null);
        scrollNav.setOpaque(false);
        scrollNav.getViewport().setOpaque(false);
        scrollNav.getHorizontalScrollBar().setUnitIncrement(16);
        pnlDock.add(scrollNav, BorderLayout.CENTER);

        // Main Controls
        JPanel pnlMainActions = new JPanel(new GridLayout(1, 3, 12, 0));
        pnlMainActions.setOpaque(false);
        pnlMainActions.setPreferredSize(new Dimension(450, 55));

        btnPrev = new JButton("Quay lại");
        btnNext = new JButton("Tiếp theo");
        btnFinish = new JButton("Kết thúc bài thi");
        
        styleActionButton(btnPrev, new Color(226, 232, 240), Color.BLACK);
        styleActionButton(btnNext, new Color(59, 130, 246), Color.WHITE);
        styleActionButton(btnFinish, new Color(239, 68, 68), Color.WHITE);
        
        btnPrev.addActionListener(e -> displayQuestion(currentQuestionIndex - 1));
        btnNext.addActionListener(e -> displayQuestion(currentQuestionIndex + 1));
        btnFinish.addActionListener(e -> confirmSubmit());
        
        pnlMainActions.add(btnPrev);
        pnlMainActions.add(btnNext);
        pnlMainActions.add(btnFinish);
        
        JPanel pnlRightDock = new JPanel(new GridBagLayout());
        pnlRightDock.setOpaque(false);
        pnlRightDock.add(pnlMainActions);
        pnlDock.add(pnlRightDock, BorderLayout.EAST);

        this.add(pnlDock, BorderLayout.SOUTH);
    }

    private void styleActionButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Inter", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.putClientProperty("FlatLaf.style", "arc: 12");
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void displayQuestion(int index) {
        if (index < 0 || index >= dsCauHoi.size()) return;
        
        currentQuestionIndex = index;
        CauHoiDTO ch = dsCauHoi.get(index);
        lblQuestionNum.setText("Câu hỏi " + (index + 1));
        lblQuestionContent.setText("<html><body style='width: 100%; line-height: 1.4;'>" + ch.getNoidung() + "</body></html>");
        
        pnlAnswers.removeAll();
        ArrayList<DapAnDTO> listDA = dsDapAnList.get(index);
        
        if (ch.getMaloai() == 1) { // Trắc nghiệm
            ButtonGroup group = new ButtonGroup();
            for (DapAnDTO da : listDA) {
                JRadioButton rb = new JRadioButton(da.getNoidungtl());
                rb.setFont(new Font("Inter", Font.PLAIN, 18));
                rb.setOpaque(false);
                rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
                rb.setFocusable(false);
                rb.setBorder(new EmptyBorder(12, 15, 12, 15));
                rb.putClientProperty("FlatLaf.style", "arc: 8");
                
                group.add(rb);
                pnlAnswers.add(rb);
                pnlAnswers.add(Box.createRigidArea(new Dimension(0, 10)));
                
                String savedAns = answersList.get(index);
                if (!savedAns.isEmpty() && savedAns.equals(String.valueOf(da.getMadapan()))) {
                    rb.setSelected(true);
                }
                
                rb.addActionListener(e -> {
                    answersList.set(index, String.valueOf(da.getMadapan()));
                    updateNavButtons();
                });
            }
        } else if (ch.getMaloai() == 2) { // Điền khuyết
            JTextField txtInput = new JTextField();
            txtInput.setFont(new Font("Inter", Font.PLAIN, 20));
            txtInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            txtInput.putClientProperty("JTextField.placeholderText", "Nhập đáp án của bạn tại đây...");
            
            String savedAns = answersList.get(index);
            txtInput.setText(savedAns);
            
            txtInput.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    answersList.set(index, txtInput.getText().trim());
                    updateNavButtons();
                }
            });
            
            pnlAnswers.add(txtInput);
        }
        
        btnPrev.setEnabled(index > 0);
        btnNext.setEnabled(index < dsCauHoi.size() - 1);
        
        pnlAnswers.revalidate();
        pnlAnswers.repaint();
        updateNavButtons();
    }

    private void updateNavButtons() {
        for (int i = 0; i < btnQuestionNav.length; i++) {
            boolean isCurrent = (i == currentQuestionIndex);
            boolean isAnswered = !answersList.get(i).isEmpty();
            
            if (isCurrent) {
                btnQuestionNav[i].setBackground(new Color(59, 130, 246));
                btnQuestionNav[i].setForeground(Color.WHITE);
                btnQuestionNav[i].setBorder(BorderFactory.createLineBorder(new Color(30, 64, 175), 2));
            } else if (isAnswered) {
                btnQuestionNav[i].setBackground(new Color(34, 197, 94));
                btnQuestionNav[i].setForeground(Color.WHITE);
                btnQuestionNav[i].setBorder(null);
            } else {
                btnQuestionNav[i].setBackground(new Color(241, 245, 249));
                btnQuestionNav[i].setForeground(new Color(71, 85, 105));
                btnQuestionNav[i].setBorder(null);
            }
        }
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsRemaining--;
                lblTime.setText(formatTime(secondsRemaining));
                if (secondsRemaining <= 10) {
                    lblTime.setForeground(Color.RED);
                }
                if (secondsRemaining <= 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(LamBai.this, "Hết thời gian làm bài! Hệ thống sẽ tự động nộp bài.");
                    submit();
                }
            }
        });
        timer.start();
    }

    private String formatTime(int totalSeconds) {
        int mins = totalSeconds / 60;
        int secs = totalSeconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    private void confirmSubmit() {
        int answeredCount = 0;
        for (String ans : answersList) {
            if (!ans.isEmpty()) answeredCount++;
        }
        
        if (answeredCount < dsCauHoi.size()) {
            if (JOptionPane.showConfirmDialog(this, "Bạn chưa hoàn thành tất cả câu hỏi (" + answeredCount + "/" + dsCauHoi.size() + ").\nVẫn muốn nộp bài?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                return;
            }
        } else {
            if (JOptionPane.showConfirmDialog(this, "Bạn đã hoàn thành bài thi.\nXác nhận nộp bài?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                return;
            }
        }
        submit();
    }

    private void submit() {
        if (timer != null) timer.stop();
        
        int timeSpent = deThi.getThoigianthi() * 60 - secondsRemaining;
        double diem = baiThiBUS.gradeAndSave(deThi, Main.user.getId(), answersList, timeSpent);
        
        if (diem >= 0) {
            JOptionPane.showMessageDialog(this, "Nộp bài thành công! Điểm của bạn: " + diem);
            ((Main) SwingUtilities.getWindowAncestor(this)).setPanel(new KetQuaHS());
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu kết quả bài thi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
