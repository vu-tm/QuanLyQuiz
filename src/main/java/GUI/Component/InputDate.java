package GUI.Component;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.PlainDocument;

public class InputDate extends JPanel {

    JLabel lbltitle;
    public JDateChooser date;
    private JTextField txtHour, txtMinute, txtSecond;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public InputDate(String title) {
        this.setLayout(new GridLayout(3, 1, 0, 4));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(0, 10, 5, 10));
        this.setPreferredSize(new Dimension(100, 130));

        lbltitle = new JLabel(title);

        date = new JDateChooser();
        date.setDateFormatString("dd/MM/yyyy");

        // Nhập giờ phút giây
        JPanel pnlTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlTime.setBackground(Color.WHITE);

        txtHour = new JTextField("00");
        txtMinute = new JTextField("00");
        txtSecond = new JTextField("00");

        txtHour.setPreferredSize(new Dimension(40, 28));
        txtMinute.setPreferredSize(new Dimension(40, 28));
        txtSecond.setPreferredSize(new Dimension(40, 28));

        ((PlainDocument) txtHour.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        ((PlainDocument) txtMinute.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        ((PlainDocument) txtSecond.getDocument()).setDocumentFilter(new NumericDocumentFilter());

        pnlTime.add(new JLabel("Giờ:"));
        pnlTime.add(txtHour);
        pnlTime.add(new JLabel("Phút:"));
        pnlTime.add(txtMinute);
        pnlTime.add(new JLabel("Giây:"));
        pnlTime.add(txtSecond);

        this.add(lbltitle);
        this.add(date);
        this.add(pnlTime);
    }

    // Lấy ngày giờ đã chọn
    public Date getDate() {
        Date selectedDate = date.getDate();
        if (selectedDate == null) {
            return null;
        }

        int hour = parseTime(txtHour.getText(), 23);
        int minute = parseTime(txtMinute.getText(), 59);
        int second = parseTime(txtSecond.getText(), 59);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        return calendar.getTime();
    }

    // Giới hạn giá trị nhập vào không vượt quá max
    private int parseTime(String text, int max) {
        try {
            int value = Integer.parseInt(text);
            if (value < 0) {
                return 0;
            }
            if (value > max) {
                return max;
            }
            return value;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Gán ngày giờ vào các ô
    public void setDate(Date d) {
        if (d == null) {
            return;
        }
        date.setDate(d);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        txtHour.setText(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
        txtMinute.setText(String.format("%02d", calendar.get(Calendar.MINUTE)));
        txtSecond.setText(String.format("%02d", calendar.get(Calendar.SECOND)));
    }

    public JDateChooser getDateChooser() {
        return this.date;
    }

    public void setDisable() {
        JTextFieldDateEditor editor = (JTextFieldDateEditor) date.getDateEditor();
        editor.setEditable(false);
        txtHour.setEnabled(false);
        txtMinute.setEnabled(false);
        txtSecond.setEnabled(false);
    }
}