package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.time.LocalDateTime;

public class TextFile extends Element {
    private StringBuilder content; //size = content.toString().length

    public TextFile(String name, LocalDateTime creationDate) {
        super(name, ElementType.File, creationDate);
        this.content = new StringBuilder();
    }

    public TextFile(String name, LocalDateTime createdDateTime, LocalDateTime lastModifiedDateTime, long size, String content) {
        super(name, ElementType.File, createdDateTime, lastModifiedDateTime, size);
        this.content = new StringBuilder(content != null ? content : "");
    }

    public StringBuilder getContent() {
        return content;
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
        clone.content = new StringBuilder(this.content);
        return clone;
    }
}
