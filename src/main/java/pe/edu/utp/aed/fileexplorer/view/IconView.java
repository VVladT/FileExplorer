package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.*;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;
import pe.edu.utp.aed.fileexplorer.view.components.IconElementCard;
import pe.edu.utp.aed.fileexplorer.view.events.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IconView extends ElementView {
    private final MouseHandler mouseHandler;
    private final GridLayout grid;
    private final List<JPanel> emptyPanels = new ArrayList<>();
    private final int DEFAULT_ROWS = 6;
    private final int DEFAULT_COLUMNS = 7;

    public IconView(Directory directory, ElementController controller, JLayeredPane layeredPane) {
        super(directory, controller);
        grid = new GridLayout(DEFAULT_ROWS, DEFAULT_COLUMNS, 10, 10);
        mouseHandler = new MouseHandler(layeredPane, controller);
        setLayout(grid);
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
}
