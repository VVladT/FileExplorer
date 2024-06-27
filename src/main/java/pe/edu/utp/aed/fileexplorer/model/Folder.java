package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.time.LocalDateTime;

public class Folder extends Directory {
    public Folder(String name, LocalDateTime creationDate) {
        super(name, ElementType.Folder, creationDate);
    }

    @Override
    public ImageIcon getIcon() {
        if (getChildren().isEmpty()) {
            return IconAdapter.EMPTY_FOLDER_ICON;
        } else {
            return IconAdapter.FULLY_FOLDER_ICON;
        }
    }

    @Override
    public Element clone() {
        Folder clone = new Folder(getName(), getCreationDate());
        clone.setModificationDate(getModificationDate());
        clone.setSize(getSize());
        for (Element child : getChildren()) {
            clone.addChild(child.clone());
        }

        return clone;
    }
}
