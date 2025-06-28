package util;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;


// utility class for reusable functionalities
public final class SwingUtils {
    private SwingUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static void applyFontToLabels(Component component, Font font) {
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            if (label.getFont() == null || label.getFont().equals(UIManager.getFont("Label.font")))
                label.setFont(font);
        } else if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents())
                applyFontToLabels(child, font);
        }
    }

    public static ImageIcon loadImage(String path, int width, int height) {
        Image image = new ImageIcon(path).getImage();
        Image scaledImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public static void writeToCsv(String filePath, List<String> fields) throws IOException {
        FileWriter writer = new FileWriter(filePath, true);
        for (int i = 0; i < fields.size(); i++) {
            writer.append(fields.get(i));
            if (i < fields.size() - 1)
                writer.append(",");
        }
        writer.append("\n");
        writer.close();
    }

    public static List<List<String>> readFromCsv(String filePath) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1);
                List<String> row = new ArrayList<>();
                for (String field : fields)
                    row.add(field);
                rows.add(row);
            }
        }
        return rows;
    }
}
