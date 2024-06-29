package pe.edu.utp.aed.fileexplorer.util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class IconAdapter {
    public static final ImageIcon DRIVE_ICON =
            new ImageIcon(IconAdapter.class.getResource("/icons/drive_icon.png"));
    public static final ImageIcon EMPTY_FOLDER_ICON =
            new ImageIcon(IconAdapter.class.getResource("/icons/empty_folder_icon.png"));
    public static final ImageIcon FULLY_FOLDER_ICON =
            new ImageIcon(IconAdapter.class.getResource("/icons/fully_folder_icon.png"));
    public static final ImageIcon FILE_ICON =
            new ImageIcon(IconAdapter.class.getResource("/icons/file_icon.png"));
    public static final ImageIcon QUICK_ACCESS_ICON =
            new ImageIcon(IconAdapter.class.getResource("/icons/quick_access_icon.png"));
    public static final ImageIcon ROOT_ICON =
            new ImageIcon(IconAdapter.class.getResource("/icons/root_icon.png"));
    public static final ImageIcon BACK_ICON =
            new ImageIcon(IconAdapter.class.getResource("/icons/back_icon.png"));
    public static final ImageIcon NEXT_ICON =
            new ImageIcon(IconAdapter.class.getResource("/icons/next_icon.png"));

    public static ImageIcon getScaledIcon(int width, int height, ImageIcon icon) {
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }

    public static Icon getTranslucentIcon(int width, int height, Icon icon, float alpha) {
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        Image scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
