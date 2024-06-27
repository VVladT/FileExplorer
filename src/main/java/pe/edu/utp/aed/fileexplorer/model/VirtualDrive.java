package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.util.FileSize;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.time.LocalDateTime;

public class VirtualDrive extends Directory {
    public static final long DEFAULT_SIZE = 512 * FileSize.GB;
    long totalSpace;

    public VirtualDrive(String name, LocalDateTime creationDate, long totalSpace) {
        super(name, ElementType.Drive, creationDate);
        this.totalSpace = totalSpace;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public long getUsedSpace() {
        return getSize();
    }

    public long getAvailableSpace() {
        return totalSpace - getSize();
    }

    public void addUsedSpace(long space) {
        if (getSize() + space > totalSpace) {
            throw new IllegalArgumentException("Not enough space in the disk.");
        }
        setSize(getSize() + space);
    }

    public void removeUsedSpace(long space) {
        if (getSize() - space < 0) {
            throw new IllegalArgumentException("Used space cannot be negative.");
        }
        setSize(getSize() - space);
    }

    @Override
    public ImageIcon getIcon() {
        return IconAdapter.DRIVE_ICON;
    }

    @Override
    public Element clone() {
        throw new UnsupportedOperationException("Virtual Disk can't be cloned");
    }
}
