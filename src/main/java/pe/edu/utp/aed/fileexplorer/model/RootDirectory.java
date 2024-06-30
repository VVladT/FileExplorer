package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.time.LocalDateTime;

public class RootDirectory extends Directory {
    private static RootDirectory instance;

    private RootDirectory() {
        super("root", ElementType.Root, LocalDateTime.now());
    }

    public static RootDirectory getInstance() {
        if (instance == null) {
            instance = new RootDirectory();
        }

        return instance;
    }

    public static void clear() {
        instance.removeChildren();
    }

    @Override
    public void addChild(Element child) {
        if (child.getType() != ElementType.Drive) {
            throw new IllegalArgumentException("Root directory only supports drives");
        }

        super.addChild(child);
    }

    @Override
    public ImageIcon getIcon() {
        return IconAdapter.ROOT_ICON;
    }

    @Override
    public Element clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
