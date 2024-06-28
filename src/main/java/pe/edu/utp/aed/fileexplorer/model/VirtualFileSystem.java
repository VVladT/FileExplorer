package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.exceptions.PathNotFoundException;
import pe.edu.utp.aed.fileexplorer.exceptions.SameNameException;

import java.util.List;

public class VirtualFileSystem {
    private final RootDirectory root;
    private final QuickAccess quickAccess;

    public VirtualFileSystem() {
        this.quickAccess = new QuickAccess();
        root = RootDirectory.getInstance();
    }

    public VirtualFileSystem(List<VirtualDrive> drives, QuickAccess quickAccess) {
        root = RootDirectory.getInstance();
        for (VirtualDrive drive : drives) {
            root.addChild(drive);
        }
        this.quickAccess = quickAccess;
    }

    public void addDrive(VirtualDrive drive) {
        if (checkParentAndChild(root, drive)) {
            root.addChild(drive);
        }
    }

    public void deleteDrive(VirtualDrive drive) {
        if (checkParentAndChild(root, drive)) {
            root.removeChild(drive);
        }
    }

    public void addElement(Directory parent, Element element) {
        if (checkParentAndChild(parent, element)) {
            addFailedOperation(parent, element);
        }
    }

    public void renameElement(Directory parent, Element element, String newName) {
        if (checkParentAndChild(parent, element)) {
            parent.renameChild(element, newName);
        }
    }

    public void deleteElement(Directory parent, Element element) {
        if (checkParentAndChild(parent, element)) {
            parent.removeChild(element);
        }
    }

    public void pasteElement(Directory parent, Element element) {
        if (checkParentAndChild(parent, element)) {
            addFailedOperation(parent, element);
        }
    }

    private boolean checkParentAndChild(Directory parent, Element element) {
        return parent != null && element != null;
    }

    private void addFailedOperation(Directory parent, Element element) {
        int count = 0;
        boolean procesed = false;
        String initialName = element.getName();

        while (!procesed) {
            try {
                parent.addChild(element);
                procesed = true;
            } catch (SameNameException e) {
                count++;
                element.setName(String.format("%s (%d)", initialName, count));
            }
        }
    }

    public List<Element> getDrives() {
        return root.getChildren();
    }

    public VirtualDrive getDrive(String name) {
        return (VirtualDrive) root.getChild(name);
    }

    public List<Element> search(String name) {
        return root.search(name);
    }

    public Element getElementByPath(String path) {
        return getElementByPath(root, path);
    }

    public static Element getElementByPath(Directory root, String path) {
        String[] nodes = path.split("/");

        for (int i = 0; i < nodes.length; i++) {
            Element element = root.getChild(nodes[i]);
            if (element == null) throw new PathNotFoundException(path);
            if (element.isDirectory()) {
                root = (Directory) element;
            } else if (i == nodes.length - 1) {
                return element;
            }
        }

        throw new PathNotFoundException(path);
    }

    public RootDirectory getRoot() {
        return root;
    }

    public QuickAccess getQuickAccess() {
        return quickAccess;
    }
}
