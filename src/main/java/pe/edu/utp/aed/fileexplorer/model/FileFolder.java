package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

public class FileFolder extends Directory {
    public FileFolder(String name, LocalDateTime creationDate) {
        super(name, ElementType.Folder, creationDate);
    }

    public FileFolder(String name, LocalDateTime createdDateTime, LocalDateTime lastModifiedDateTime, long size, List<Element> children) {
        super(name, ElementType.Folder, createdDateTime, lastModifiedDateTime, size, children);
    }

    @Override
    public ImageIcon getIcon() {
        if (isEmpty()) {
            return IconAdapter.EMPTY_FOLDER_ICON;
        } else {
            return IconAdapter.FULLY_FOLDER_ICON;
        }
    }

    @Override
    public Element clone() {
        FileFolder clone = new FileFolder(getName(), getCreationDate());
        clone.setModificationDate(getModificationDate());
        clone.setSize(getSize());
        for (Element child : getChildren()) {
            clone.addChild(child.clone());
        }

        return clone;
    }
}
