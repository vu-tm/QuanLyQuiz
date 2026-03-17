package GUI.Dialog;

import BUS.BaiThiBUS;
import BUS.CauHoiBUS;
import DTO.BaiThiDTO;
import DTO.ChiTietBaiThiDTO;
import DTO.CauHoiDTO;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ChiTietBaiThiDialog extends JDialog {

    private BaiThiDTO baiThiDTO;
    private BaiThiBUS baiThiBUS = new BaiThiBUS();
    private CauHoiBUS cauHoiBUS = new CauHoiBUS();
    
    private JTable table;
    private DefaultTableModel tblModel;
    private JLabel lblMaBaiThi, lblMaDe, lblMaSV, lblDiem, lblSoCauDung;

    public ChiTietBaiThiDialog(JFrame owner, String title, boolean modal, BaiThiDTO baiThiDTO) {
        super(owner, title, modal);
        this.baiThiDTO = baiThiDTO;
        initComponent();
        loadData();
        this.setSize(800, 600);
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
    }

    private void initComponent() {
        this.setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel(new GridLayout(2, 3, 10, 10));
        pnlHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
        pnlHeader.setBackground(Color.WHITE);

        lblMaBaiThi = new JLabel("Mã bài thi: " + baiThiDTO.getMabaithi());
        lblMaDe = new JLabel("Mã đề: " + baiThiDTO.getMade());
        lblMaSV = new JLabel("Mã SV: " + baiThiDTO.getManguoidung());
        lblDiem = new JLabel("Điểm: " + baiThiDTO.getDiemthi());
        lblSoCauDung = new JLabel("Số câu đúng: " + baiThiDTO.getSocaudung() + "/" + (baiThiDTO.getSocaudung() + baiThiDTO.getSocausai()));

        pnlHeader.add(lblMaBaiThi);
        pnlHeader.add(lblMaDe);
        pnlHeader.add(lblMaSV);
        pnlHeader.add(lblDiem);
        pnlHeader.add(lblSoCauDung);

        this.add(pnlHeader, BorderLayout.NORTH);

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] header = {"STT", "Mã câu hỏi", "Nội dung câu hỏi", "Đáp án đã chọn", "Nội dung điền khuyết"};
        tblModel.setColumnIdentifiers(header);
        table = new JTable(tblModel);
        table.setRowHeight(30);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        pnlFooter.add(btnClose);
        this.add(pnlFooter, BorderLayout.SOUTH);
    }

    private void loadData() {
        ArrayList<ChiTietBaiThiDTO> listCT = baiThiBUS.getChiTietByMaBaiThi(baiThiDTO.getMabaithi());
        tblModel.setRowCount(0);
        int stt = 1;
        for (ChiTietBaiThiDTO ct : listCT) {
            CauHoiDTO ch = cauHoiBUS.getById(ct.getMacauhoi());
            tblModel.addRow(new Object[]{
                stt++,
                ct.getMacauhoi(),
                ch != null ? ch.getNoidung() : "N/A",
                ct.getDapanchon() == 0 ? "Chưa chọn" : ct.getDapanchon(),
                ct.getNoidungdienkhuyet() != null ? ct.getNoidungdienkhuyet() : ""
            });
        }
    }
}
