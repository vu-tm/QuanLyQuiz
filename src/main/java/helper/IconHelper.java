package helper;

import javax.swing.*;
import java.awt.*;

public class IconHelper {

    public static ImageIcon loadIcon(String fileName) {
        try {
            // Đường dẫn đến thư mục icon (Other Sources/src/main/resources/icon)
            java.net.URL imgURL = IconHelper.class.getResource("/icon/" + fileName);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Không tìm thấy icon: " + fileName);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ImageIcon loadIcon(String fileName, int width, int height) {
        ImageIcon icon = loadIcon(fileName);
        if (icon != null) {
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        }
        return null;
    }
}