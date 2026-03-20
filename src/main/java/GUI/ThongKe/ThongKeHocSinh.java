package GUI.ThongKe;

import BUS.ThongKeBUS;
import DTO.ThongKe.ThongKeHocSinhDTO;
import GUI.Component.InputForm;
import GUI.Component.InputDate;
import GUI.Component.PanelBorderRadius;
import GUI.Component.TableSorter;
import helper.JTableExporter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeHocSinh extends JPanel implements ActionListener, KeyListener {
    PanelBorderRadius pnlLeft, pnlCenter;
    JTable tblHocSinh;
    DefaultTableModel tblModel;
    InputForm inputSearch;
    InputDate dateStart, dateEnd; // Sử dụng class InputDate của bạn
    JButton btnExport, btnReset;
    ArrayList<ThongKeHocSinhDTO> list;
    Color primaryColor = new Color(6, 101, 208); // Màu #0665d0 bạn yêu cầu

    public ThongKeHocSinh() {
        initComponent();
        refreshTable();
    }

    private void initComponent() {
        this.setLayout(new BorderLayout(10, 10));
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // PANEL TRÁI - Chỉnh Layout để không bị "lòi"
        pnlLeft = new PanelBorderRadius();
        pnlLeft.setPreferredSize(new Dimension(300, 100));
        pnlLeft.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        pnlLeft.setBackground(Color.WHITE);

        inputSearch = new InputForm("Tìm kiếm học sinh");
        inputSearch.setPreferredSize(new Dimension(280, 80)); // Fix kích thước để nằm gọn trong 300px
        inputSearch.getTxtForm().addKeyListener(this);

        dateStart = new InputDate("Từ ngày");
        dateStart.setPreferredSize(new Dimension(280, 80));
        
        dateEnd = new InputDate("Đến ngày");
        dateEnd.setPreferredSize(new Dimension(280, 80));

        // Thiết lập ngày mặc định dùng hàm setDate(Date d) có sẵn trong component của bạn
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1);
        
        // Chuyển LocalDate sang Date để dùng hàm setDate của bạn
        Date dStart = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dEnd = new Date();
        
        dateStart.setDate(dStart);
        dateEnd.setDate(dEnd);

        // Lắng nghe thay đổi ngày (Dùng dateTimePicker trực tiếp từ component của bạn)
        dateStart.dateTimePicker.addDateTimeChangeListener(e -> refreshTable());
        dateEnd.dateTimePicker.addDateTimeChangeListener(e -> refreshTable());

        // Group nút bấm
        JPanel btnGroup = new JPanel(new GridLayout(1, 2, 10, 0));
        btnGroup.setPreferredSize(new Dimension(280, 45));
        btnGroup.setOpaque(false);
        btnExport = createButton("Xuất Excel", new Color(76, 175, 80));
        btnReset = createButton("Làm mới", primaryColor);
        btnExport.addActionListener(this);
        btnReset.addActionListener(this);
        btnGroup.add(btnExport);
        btnGroup.add(btnReset);

        pnlLeft.add(inputSearch);
        pnlLeft.add(dateStart);
        pnlLeft.add(dateEnd);
        pnlLeft.add(btnGroup);

        // PANEL GIỮA (Bảng dữ liệu)
        pnlCenter = new PanelBorderRadius();
        pnlCenter.setLayout(new BorderLayout());
        tblModel = new DefaultTableModel(new String[]{"STT", "Mã học sinh", "Tên học sinh", "Số đề đã làm"}, 0);
        tblHocSinh = new JTable(tblModel);
        tblHocSinh.setRowHeight(35);
        tblHocSinh.setSelectionBackground(new Color(187, 222, 251));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblHocSinh.setDefaultRenderer(Object.class, centerRenderer);
        
        pnlCenter.add(new JScrollPane(tblHocSinh), BorderLayout.CENTER);

        this.add(pnlLeft, BorderLayout.WEST);
        this.add(pnlCenter, BorderLayout.CENTER);
    }

    public void refreshTable() {
        // Sử dụng hàm getDate() có sẵn trong InputDate của bạn
        Date start = dateStart.getDate();
        Date end = dateEnd.getDate();
        
        if (start != null && end != null) {
            LocalDate lStart = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate lEnd = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            list = ThongKeBUS.getThongKeHocSinh(lStart, lEnd);
            loadDataTable(list);
        }
    }

    public void loadDataTable(ArrayList<ThongKeHocSinhDTO> data) {
        tblModel.setRowCount(0);
        for (ThongKeHocSinhDTO i : data) {
            tblModel.addRow(new Object[]{i.getStt(), i.getManguoidung(), i.getHoten(), i.getSoDedalLam()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnExport) {
            try { JTableExporter.exportJTableToExcel(tblHocSinh); } catch (Exception ex) {}
        } else if (e.getSource() == btnReset) {
            inputSearch.setText("");
            LocalDate now = LocalDate.now();
            dateStart.setDate(Date.from(now.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            dateEnd.setDate(new Date());
            refreshTable();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String txt = inputSearch.getText().toLowerCase();
        ArrayList<ThongKeHocSinhDTO> temp = new ArrayList<>();
        if (list != null) {
            for (ThongKeHocSinhDTO i : list) {
                if (i.getHoten().toLowerCase().contains(txt) || i.getManguoidung().toLowerCase().contains(txt)) {
                    temp.add(i);
                }
            }
        }
        loadDataTable(temp);
    }
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}