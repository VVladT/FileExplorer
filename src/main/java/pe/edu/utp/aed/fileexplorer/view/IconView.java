package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.*;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;
import pe.edu.utp.aed.fileexplorer.view.components.IconElementCard;
import pe.edu.utp.aed.fileexplorer.view.events.KeyboardHandler;
import pe.edu.utp.aed.fileexplorer.view.events.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IconView extends ElementView {
    private MouseHandler mouseHandler;
    private final GridLayout grid;
    private final List<JPanel> emptyPanels = new ArrayList<>();
    private final int DEFAULT_ROWS = 6;
    private final int DEFAULT_COLUMNS = 7;

    public IconView(Directory directory, ElementController controller, JLayeredPane layeredPane) {
        super(directory, controller, layeredPane);
        grid = new GridLayout(DEFAULT_ROWS, DEFAULT_COLUMNS, 10, 10);
        setLayout(grid);
        mouseHandler = new MouseHandler(layeredPane, controller);
        addMouseListener(mouseHandler);
        directory.addObserver(this);
        updateDirectory(directory);
    }

    @Override
    public void refreshView() {
        removeAllElements();
        removeEmptyPanels();
        for (Element element : directory.getChildren()) {
            addElement(element);
        }
        addEmptyPanels();
        revalidate();
        repaint();
    }

    private void removeAllElements() {
        for (Component component : getContentPanel().getComponents()) {
            if (component instanceof ElementCard ec) {
                remove(ec);
            }
        }
    }

    @Override
    public void addElement(Element element) {
        IconElementCard iconElementCard = new IconElementCard(element);
        adjustGridLayout();
        addListeners(iconElementCard);
        add(iconElementCard);
        revalidateAndRepaint();
    }

    private void adjustGridLayout() {
        if (grid.getRows() * grid.getColumns() < directory.getChildren().size()) {
            grid.setRows(grid.getRows() + 1);
        }
    }

    private void revalidateAndRepaint() {
        revalidate();
        repaint();
    }

    @Override
    public void removeElement(Element element) {
        for (Component component : getComponents()) {
            if (component instanceof IconElementCard iec) {
                if (iec.getElement().equals(element)) {
                    element.removeObserver(iec);
                    remove(component);
                    adjustGridLayoutOnRemove();
                    revalidateAndRepaint();
                    return;
                }
            }
        }
    }

    private void adjustGridLayoutOnRemove() {
        if (directory.getChildren().size() < DEFAULT_ROWS * DEFAULT_COLUMNS) {
            removeEmptyPanels();
            addEmptyPanels();
        } else if (grid.getRows() * grid.getColumns() - DEFAULT_COLUMNS >= directory.getChildren().size()) {
            grid.setRows(grid.getRows() - 1);
        }
    }


    public void addListeners(ElementCard elementCard) {
        elementCard.addMouseListener(mouseHandler);
        elementCard.addMouseMotionListener(mouseHandler);
    }

    private void removeEmptyPanels() {
        for (JPanel panel : emptyPanels) {
            remove(panel);
        }
        emptyPanels.clear();
    }

    private void addEmptyPanels() {
        int emptyCells = DEFAULT_ROWS * DEFAULT_COLUMNS - directory.getChildren().size();
        if (emptyCells > 0) {
            for (int i = 0; i < emptyCells; i++) {
                JPanel panel = new JPanel();
                emptyPanels.add(panel);
                add(panel);
            }
        }
    }

    @Override
    public void elementAdded(Element element) {
        removeEmptyPanels();
        addElement(element);
        addEmptyPanels();
    }

    @Override
    public void elementRemoved(Element element) {
        removeElement(element);
    }

    public static void main(String[] args) {
        //RootDirectory directory = RootDirectory.getInstance();
        Directory directory = new Folder("oal", LocalDateTime.now());

        Folder folder1 = new Folder("folder1", LocalDateTime.now());
        Folder folder2 = new Folder("folder2", LocalDateTime.now());
        Folder folder3 = new Folder("folder3", LocalDateTime.now());
        Folder folder4 = new Folder("folder4", LocalDateTime.now());
        Folder folder5 = new Folder("folder5", LocalDateTime.now());
        Folder folder6 = new Folder("folder6", LocalDateTime.now());
        Folder folder7 = new Folder("folder7", LocalDateTime.now());
        Folder folder8 = new Folder("folder8", LocalDateTime.now());
        Folder folder9 = new Folder("folder9", LocalDateTime.now());
        Folder folder10 = new Folder("folder10", LocalDateTime.now());

        directory.addChild(folder1);
        directory.addChild(folder2);
        directory.addChild(folder3);
        directory.addChild(folder4);
        directory.addChild(folder5);
        directory.addChild(folder6);
        directory.addChild(folder7);
        directory.addChild(folder8);
        directory.addChild(folder9);
        directory.addChild(folder10);
        directory.removeChild(folder1);
        directory.addChild(new TextFile("oal", LocalDateTime.now()));

        ElementController elementController = new ElementController();
        MainView mv = new MainView(elementController, directory);
        mv.setFocusable(true);
        mv.addKeyListener(new KeyboardHandler(elementController));
        IconView iconView = new IconView(directory, elementController, mv.getLayeredPane());
        mv.setElementView(iconView);
    }
}
