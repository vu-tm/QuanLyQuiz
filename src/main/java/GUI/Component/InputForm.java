package GUI.Component;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public final class InputForm extends JPanel {
    private JLabel lblTitle;
    private JTextField txtForm;

    public InputForm(String title) {
        this.setLayout(new GridLayout(2, 1));
        this.setBackground(Color.white);
        this.setBorder(new EmptyBorder(0, 10, 5, 10));
        lblTitle = new JLabel(title);
        txtForm = new JTextField();
        this.add(lblTitle);
        this.add(txtForm);
    }

    public String getText() { return txtForm.getText(); }
    public void setText(String content) { txtForm.setText(content); }
    public void setEditable(boolean value) { txtForm.setEditable(value); }
    public JTextField getTxtForm() { return txtForm; }
}