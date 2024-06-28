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
                ec.clear();
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
                    iec.clear();
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
        Directory directory = new FileFolder("oal", LocalDateTime.now());

        FileFolder folder1 = new FileFolder("folder1", LocalDateTime.now());
        FileFolder folder2 = new FileFolder("folder2", LocalDateTime.now());
        FileFolder folder3 = new FileFolder("folder3", LocalDateTime.now());
        FileFolder folder4 = new FileFolder("folder4", LocalDateTime.now());
        FileFolder folder5 = new FileFolder("folder5", LocalDateTime.now());
        FileFolder folder6 = new FileFolder("folder6", LocalDateTime.now());
        FileFolder folder7 = new FileFolder("folder7", LocalDateTime.now());
        FileFolder folder8 = new FileFolder("folder8", LocalDateTime.now());
        FileFolder folder9 = new FileFolder("folder9", LocalDateTime.now());
        FileFolder folder10 = new FileFolder("folder10", LocalDateTime.now());

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
