package helper;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnterKeyTraversal {

    /**
     * Gắn Enter key traversal cho danh sách component theo thứ tự.
     * @param components Danh sách JTextField / JComboBox / JButton theo thứ tự tab
     */
    public static void setup(JComponent... components) {
        List<JComponent> list = new ArrayList<>(Arrays.asList(components));

        for (int i = 0; i < list.size(); i++) {
            final int current = i;
            JComponent comp = list.get(i);

            if (comp instanceof JTextField tf) {
                tf.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            focusNext(list, current);
                        }
                    }
                });

            } else if (comp instanceof JComboBox<?> cb) {
                // ComboBox: chỉ chuyển khi không đang mở dropdown
                cb.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER && !cb.isPopupVisible()) {
                            focusNext(list, current);
                        }
                    }
                });

            } else if (comp instanceof JButton btn) {
                // Button cuối: Enter sẽ click button
                btn.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            btn.doClick();
                        }
                    }
                });
            }
        }
    }

    /**
     * Focus vào component tiếp theo trong danh sách.
     * Nếu là component cuối → quay lại đầu.
     */
    private static void focusNext(List<JComponent> list, int current) {
        int next = (current + 1) % list.size();
        JComponent nextComp = list.get(next);

        // Bỏ qua các component bị disabled hoặc không editable
        int attempts = 0;
        while (attempts < list.size()) {
            if (nextComp.isEnabled() && nextComp.isVisible()) {
                if (nextComp instanceof JTextField tf && !tf.isEditable()) {
                    // Bỏ qua ô read-only (như Mã SV)
                    next = (next + 1) % list.size();
                    nextComp = list.get(next);
                    attempts++;
                    continue;
                }
                break;
            }
            next = (next + 1) % list.size();
            nextComp = list.get(next);
            attempts++;
        }

        nextComp.requestFocusInWindow();

        // Nếu là TextField → chọn hết text để tiện gõ đè
        if (nextComp instanceof JTextField tf) {
            tf.selectAll();
        }
        // Nếu là ComboBox → mở dropdown luôn
        // (bỏ comment nếu muốn)
        // if (nextComp instanceof JComboBox<?> cb) cb.showPopup();
    }
}