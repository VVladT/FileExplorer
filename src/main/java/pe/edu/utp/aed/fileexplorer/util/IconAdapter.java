package pe.edu.utp.aed.fileexplorer.util;

import javax.swing.*;
import java.awt.*;

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
}
