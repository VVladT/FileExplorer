package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.time.LocalDateTime;

public class TextFile extends Element {
    public TextFile(String name, LocalDateTime creationDate) {
        super(name, ElementType.File, creationDate);
    }

    @Override
    public ImageIcon getIcon() {
        return IconAdapter.FILE_ICON;
    }

    @Override
    public Element clone() {
        TextFile clone = new TextFile(getName(), getCreationDate());
        clone.setModificationDate(getModificationDate());
        clone.setSize(getSize());

        return clone;
    }
}
