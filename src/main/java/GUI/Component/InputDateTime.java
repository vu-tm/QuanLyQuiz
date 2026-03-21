package GUI.Component;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class InputDateTime extends JPanel {

    private JLabel lblTitle;
    public DateTimePicker dateTimePicker;

    public InputDateTime(String title) {
        this.setLayout(new GridLayout(2, 1, 0, 4));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(0, 10, 5, 10));
        this.setPreferredSize(new Dimension(100, 90));

        lblTitle = new JLabel(title);

        Locale localeVI = new Locale("vi", "VN");

        DatePickerSettings dateSettings = new DatePickerSettings(localeVI);
        dateSettings.setFormatForDatesCommonEra("dd/MM/yyyy");

        TimePickerSettings timeSettings = new TimePickerSettings(localeVI);
        timeSettings.setFormatForDisplayTime("HH:mm:ss");
        timeSettings.setFormatForMenuTimes("HH:mm:ss");
        timeSettings.use24HourClockFormat();

        dateTimePicker = new DateTimePicker(dateSettings, timeSettings);

        JButton calendarBtn = dateTimePicker.getDatePicker().getComponentToggleCalendarButton();
        calendarBtn.setText("");
        calendarBtn.setIcon(new FlatSVGIcon("icon/calendar.svg", 16, 16));

        JButton clockBtn = dateTimePicker.getTimePicker().getComponentToggleTimeMenuButton();
        clockBtn.setText("");
        clockBtn.setIcon(new FlatSVGIcon("icon/clock.svg", 16, 16));

        this.add(lblTitle);
        this.add(dateTimePicker);
    }

    public Date getDate() {
        LocalDateTime ldt = dateTimePicker.getDateTimePermissive();
        if (ldt == null) return null;
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setDate(Date d) {
        if (d == null) return;
        LocalDateTime ldt = d.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        dateTimePicker.setDateTimePermissive(ldt);
    }

    public void setDisable() {
        dateTimePicker.getDatePicker().setEnabled(false);
        dateTimePicker.getTimePicker().setEnabled(false);
    }
}