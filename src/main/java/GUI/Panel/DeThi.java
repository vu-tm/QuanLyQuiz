package GUI.Panel;

import BUS.DeThiBUS;
import DTO.DeThiDTO;
import GUI.Component.IntegratedSearch;
import GUI.Component.MainFunction;
import GUI.Component.PanelBorderRadius;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class DeThi extends JPanel {

    private JTable tableDeThi;
    private DefaultTableModel tblModel;
    private DeThiBUS dtBUS = new DeThiBUS();
    private Color BackgroundColor = new Color(240, 247, 250);

    public DeThi() {
        initComponent();
        loadDataTable();
    }

    private void initComponent() {
        this.setBackground(BackgroundColor);
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        PanelBorderRadius functionBar = new PanelBorderRadius();
        functionBar.setPreferredSize(new Dimension(0, 100));
        functionBar.setLayout(new GridLayout(1, 2, 50, 0));
        functionBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] action = {"create", "update", "delete", "detail", "import", "export"};
        MainFunction mainFunction = new MainFunction(action);
        functionBar.add(mainFunction);

        IntegratedSearch search = new IntegratedSearch(new String[]{"Tất cả", "Mã đề", "Tên đề"});
        functionBar.add(search);

        // Table
        tableDeThi = new JTable();
        tblModel = new DefaultTableModel();
        String[] header = {"Mã đề", "Tên đề thi", "Mã môn học", "Thời gian", "Người tạo"};
        tblModel.setColumnIdentifiers(header);
        tableDeThi.setModel(tblModel);
        tableDeThi.setFocusable(false);
        tableDeThi.setRowHeight(40); 

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableDeThi.getColumnCount(); i++) {
            tableDeThi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollTable = new JScrollPane(tableDeThi);
        scrollTable.setBorder(new EmptyBorder(0, 0, 0, 0));

        // LAYOUT CHÍNH
        JPanel contentCenter = new JPanel(new BorderLayout(10, 10));
        contentCenter.setBackground(BackgroundColor);
        contentCenter.add(functionBar, BorderLayout.NORTH);

        PanelBorderRadius mainPanel = new PanelBorderRadius();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollTable, BorderLayout.CENTER);
        contentCenter.add(mainPanel, BorderLayout.CENTER);

        this.add(contentCenter, BorderLayout.CENTER);
    }

    /**
     * Hàm lấy dữ liệu từ Database thông qua BUS và hiển thị lên JTable
     */
    public void loadDataTable() {
        ArrayList<DeThiDTO> list = dtBUS.getAll();
        
        // Xóa dữ liệu cũ trên bảng
        tblModel.setRowCount(0); 
        
        // Đổ dữ liệu mới vào
        for (DeThiDTO dt : list) {
            tblModel.addRow(new Object[]{
                dt.getMade(), 
                dt.getTende(), 
                dt.getMonthi(), 
                dt.getThoigianthi() + " phút", 
                dt.getNguoitao()
            });
        }
    }
}