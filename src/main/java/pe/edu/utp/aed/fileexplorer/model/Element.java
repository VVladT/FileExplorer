package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.model.observers.ElementObserver;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class Element {
    private String name;
    private ElementType type;
    private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private long size;
    private final List<ElementObserver> observers;

    public Element(String name, ElementType type,
                   LocalDateTime creationDate) {
        this.name = name;
        this.type = type;
        this.creationDate = creationDate;
        this.modificationDate = creationDate;
        this.size = 0;
        this.observers = new ArrayList<>();
    }

    public String getPath() {
        StringBuilder path = new StringBuilder();

        if (findPath(RootDirectory.getInstance(), path)) {
            return path.toString();
        }

        return null;
    }

    private boolean findPath(Directory currentDir, StringBuilder path) {
        if (currentDir == null) {
            return false;
        }

        if (currentDir.getChildren().contains(this)) {
            path.insert(0, "/" + currentDir.getName() + "/" + this.getName());
            return true;
        }

        for (Element child : currentDir.getChildren()) {
            if (child instanceof Directory) {
                Directory childDir = (Directory) child;
                if (findPath(childDir, path)) {
                    if (currentDir != RootDirectory.getInstance()) {
                        path.insert(0, "/" + currentDir.getName());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public abstract ImageIcon getIcon();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyElementUpdated();
    }

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getCreationDateString() {
        return dateFormat.format(creationDate);
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public String getModificationDateString() {
        return dateFormat.format(modificationDate);
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long newSize) {
        notifySizeChange(newSize - size);
        size = newSize;
    }

    public void increaseSize(long increase) {
        notifySizeChange(increase);
        size += increase;
    }

    public void decreaseSize(long decrease) {
        notifySizeChange(-decrease);
        size -= decrease;
    }

    public boolean isDirectory() {
        return this instanceof Directory;
    }

    protected void updateModificationDate() {
        setModificationDate(LocalDateTime.now());
    }

    public abstract Element clone();

    @Override
    public String toString() {
        return "name: " + name + " : " + getPath();
    }

    public void addObserver(ElementObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ElementObserver observer) {
        observers.remove(observer);
    }

    public void notifySizeChange(long increase) {
        for (ElementObserver observer : observers) {
            observer.sizeChanged(increase);
        }
    }

    public void notifyElementUpdated() {
        for (ElementObserver observer : observers) {
            observer.elementUpdated();
        }
    }
}
