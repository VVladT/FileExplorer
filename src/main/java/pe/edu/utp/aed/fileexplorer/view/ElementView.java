package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.model.observers.DirectoryObserver;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ElementView extends JPanel implements DirectoryObserver {
    protected Directory directory;
    protected final ElementController controller;

    public ElementView(Directory directory, ElementController controller) {
        this.directory = directory;
        this.controller = controller;
        setPreferredSize(new Dimension(600, 400));
    }

    public void updateDirectory(Directory newDirectory) {
        if (this.directory != null) {
            this.directory.removeObserver(this);
        }
        this.directory = newDirectory;
        this.directory.addObserver(this);
        refreshView();
    }

    public abstract void refreshView();
    public abstract void addElement(Element element);
    public abstract void removeElement(Element element);

    public List<ElementCard> getAllElements() {
        List<ElementCard> allElements = new ArrayList<>();

        for (Component component : getContentPanel().getComponents()) {
            if (component instanceof ElementCard ec) {
                allElements.add(ec);
            }
        }

        return allElements;
    }

    public List<ElementCard> getDirectories() {
        List<ElementCard> directories = new ArrayList<>();

        for (Component component : getContentPanel().getComponents()) {
            if (component instanceof ElementCard ec) {
                if (ec.getElement().isDirectory()) {
                    directories.add(ec);
                }
            }
        }

        return directories;
    }

    public JPanel getContentPanel() {
        return this;
    }

    public ElementCard getElementCard(Element element) {
        for (Component component : getContentPanel().getComponents()) {
            if (component instanceof ElementCard ec) {
                if (ec.getElement().equals(element)) {
                    return ec;
                }
            }
        }
        return null;
    }
}
