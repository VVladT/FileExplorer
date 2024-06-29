package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.view.components.DetailsElementCard;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;
import pe.edu.utp.aed.fileexplorer.view.events.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DetailsView extends ElementView {
    private final MouseHandler mouseHandler;
    private final JPanel headerPanel;
    private final JPanel contentPanel;

    public DetailsView(Directory directory, ElementController controller, JLayeredPane layeredPane) {
        super(directory, controller);
        setLayout(new BorderLayout());

        mouseHandler = new MouseHandler(layeredPane, controller);
        addMouseListener(mouseHandler);

        headerPanel = new JPanel(new GridBagLayout());
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        directory.addObserver(this);
        updateDirectory(directory);
    }

    private void addHeader() {
        headerPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        JButton nameHeader = createHeaderButton("Name", new Dimension(200, 30), Comparator.comparing(Element::getName));
        JButton creationDateHeader = createHeaderButton("Creation Date", new Dimension(120, 30), Comparator.comparing(Element::getCreationDate));
        JButton modificationDateHeader = createHeaderButton("Modification Date", new Dimension(120, 30), Comparator.comparing(Element::getModificationDate));
        JButton typeHeader = createHeaderButton("Type", new Dimension(80, 30), null);
        JButton sizeHeader = createHeaderButton("Size", new Dimension(80, 30), Comparator.comparing(Element::getSize));

        addButtonToHeaderPanel(nameHeader, gbc, 0, 10);
        addButtonToHeaderPanel(creationDateHeader, gbc, 1, 5);
        addButtonToHeaderPanel(modificationDateHeader, gbc, 2, 5);
        addButtonToHeaderPanel(typeHeader, gbc, 3, 2);
        addButtonToHeaderPanel(sizeHeader, gbc, 4, 1);

        headerPanel.revalidate();
        headerPanel.repaint();
    }

    private JButton createHeaderButton(String text, Dimension dimension, Comparator<Element> comparator) {
        JButton button = new JButton(text);
        button.setPreferredSize(dimension);
        if (comparator != null) {
            button.addActionListener(createSortActionListener(comparator));
        }
        return button;
    }

    private void addButtonToHeaderPanel(JButton button, GridBagConstraints gbc, int gridx, int weightx) {
        gbc.gridx = gridx;
        gbc.weightx = weightx;
        headerPanel.add(button, gbc);
    }

    private ActionListener createSortActionListener(Comparator<Element> comparator) {
        return null;
    }

    @Override
    public void refreshView() {
        removeAllElements();
        addHeader();

        for (Element element : directory.getChildren()) {
            addElement(element);
        }

        revalidate();
        repaint();
    }

    private void removeAllElements() {
        for (Component component : contentPanel.getComponents()) {
            if (component instanceof ElementCard) {
                ((ElementCard) component).clear();
            }
        }
        contentPanel.removeAll();
    }

    @Override
    public void addElement(Element element) {
        DetailsElementCard detailsElementCard = new DetailsElementCard(element);
        addListeners(detailsElementCard);
        contentPanel.add(detailsElementCard);
        revalidate();
        repaint();
    }

    private void addListeners(ElementCard elementCard) {
        elementCard.addMouseListener(mouseHandler);
        elementCard.addMouseMotionListener(mouseHandler);
    }

    @Override
    public void removeElement(Element element) {
        for (Component component : contentPanel.getComponents()) {
            if (component instanceof DetailsElementCard) {
                DetailsElementCard detailsElementCard = (DetailsElementCard) component;
                if (detailsElementCard.getElement().equals(element)) {
                    detailsElementCard.clear();
                    contentPanel.remove(detailsElementCard);
                    revalidate();
                    repaint();
                    return;
                }
            }
        }
    }

    @Override
    public void elementAdded(Element element) {
        addElement(element);
    }

    @Override
    public void elementRemoved(Element element) {
        removeElement(element);
    }

    @Override
    public List<ElementCard> getAllElements() {
        List<ElementCard> allElements = new ArrayList<>();
        for (Component component : contentPanel.getComponents()) {
            if (component instanceof ElementCard) {
                allElements.add((ElementCard) component);
            }
        }
        return allElements;
    }

    @Override
    public List<ElementCard> getDirectories() {
        List<ElementCard> directories = new ArrayList<>();
        for (Component component : contentPanel.getComponents()) {
            if (component instanceof ElementCard) {
                ElementCard elementCard = (ElementCard) component;
                if (elementCard.getElement().isDirectory()) {
                    directories.add(elementCard);
                }
            }
        }
        return directories;
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }
}
