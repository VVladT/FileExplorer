package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.model.RootDirectory;
import pe.edu.utp.aed.fileexplorer.model.VirtualDrive;
import pe.edu.utp.aed.fileexplorer.util.ElementSorter;
import pe.edu.utp.aed.fileexplorer.view.components.DetailsElementCard;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;
import pe.edu.utp.aed.fileexplorer.view.events.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DetailsView extends ElementView {
    private final MouseHandler mouseHandler;
    private boolean dirIsRoot;
    private final JPanel headerPanel;
    private final JPanel contentPanel;

    private boolean sortedNameAsc = false;
    private boolean sortedCreationAsc = false;
    private boolean sortedModificationAsc = false;
    private boolean sortedTypeAsc = false;
    private boolean sortedSizeAsc = false;
    private boolean sortedAvailableSpaceAsc = false;

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

        if (!dirIsRoot) {
            JButton nameHeader = createHeaderButton("Nombre", new Dimension(200, 30));
            JButton creationDateHeader = createHeaderButton("Fecha de creación", new Dimension(120, 30));
            JButton modificationDateHeader = createHeaderButton("Fecha de modificación", new Dimension(120, 30));
            JButton typeHeader = createHeaderButton("Tipo", new Dimension(80, 30));
            JButton sizeHeader = createHeaderButton("Tamaño", new Dimension(80, 30));

            nameHeader.addActionListener(e -> sortByName());
            creationDateHeader.addActionListener(e -> sortByCreation());
            modificationDateHeader.addActionListener(e -> sortByModify());
            typeHeader.addActionListener(e -> sortByType());
            sizeHeader.addActionListener(e -> sortBySize());

            addButtonToHeaderPanel(nameHeader, gbc, 0, 10);
            addButtonToHeaderPanel(creationDateHeader, gbc, 1, 5);
            addButtonToHeaderPanel(modificationDateHeader, gbc, 2, 5);
            addButtonToHeaderPanel(typeHeader, gbc, 3, 2);
            addButtonToHeaderPanel(sizeHeader, gbc, 4, 1);
        } else {
            JButton nameHeader = createHeaderButton("Nombre", new Dimension(200, 30));
            JButton typeHeader = createHeaderButton("Tipo", new Dimension(200, 30));
            JButton totalSpaceHeader = createHeaderButton("Utilizado", new Dimension(100, 30));
            JButton availableSpaceHeader = createHeaderButton("Disponible", new Dimension(100, 30));

            nameHeader.addActionListener(e -> sortByName());
            typeHeader.addActionListener(e -> sortByType());
            totalSpaceHeader.addActionListener(e -> sortBySize());
            availableSpaceHeader.addActionListener(e -> sortByAvailableSpace());

            addButtonToHeaderPanel(nameHeader, gbc, 0, 10);
            addButtonToHeaderPanel(typeHeader, gbc, 1, 5);
            addButtonToHeaderPanel(totalSpaceHeader, gbc, 2, 1);
            addButtonToHeaderPanel(availableSpaceHeader, gbc, 3, 1);
        }

        headerPanel.revalidate();
        headerPanel.repaint();
    }

    private void sortByName() {
        List<Element> elements = directory.getChildren();
        boolean sortedAsc = sortedNameAsc;
        resetSortStates();
        ElementSorter.sortByName(elements, sortedAsc);
        sortedNameAsc = !sortedAsc;
        refreshView(elements);
    }

    private void sortByCreation() {
        List<Element> elements = directory.getChildren();
        boolean sortedAsc = sortedCreationAsc;
        resetSortStates();
        ElementSorter.sortByCreationDate(elements, sortedAsc);
        sortedCreationAsc = !sortedAsc;
        refreshView(elements);
    }

    private void sortByModify() {
        List<Element> elements = directory.getChildren();
        boolean sortedAsc = sortedModificationAsc;
        resetSortStates();
        ElementSorter.sortByModificationDate(elements, sortedAsc);
        sortedModificationAsc = !sortedAsc;
        refreshView(elements);
    }

    private void sortByType() {
        List<Element> elements = directory.getChildren();
        boolean sortedAsc = sortedTypeAsc;
        resetSortStates();
        ElementSorter.sortByType(elements, sortedAsc);
        sortedTypeAsc = !sortedAsc;
        refreshView(elements);
    }

    private void sortBySize() {
        List<Element> elements = directory.getChildren();
        boolean sortedAsc = sortedSizeAsc;
        resetSortStates();
        ElementSorter.sortBySize(elements, sortedAsc);
        sortedSizeAsc = !sortedAsc;
        refreshView(elements);
    }

    private void sortByAvailableSpace() {
        List<Element> elements = directory.getChildren();
        List<VirtualDrive> virtualDrives = elements.stream()
                .filter(element -> element instanceof VirtualDrive)
                .map(element -> (VirtualDrive) element)
                .collect(Collectors.toList());

        boolean sortedAsc = sortedAvailableSpaceAsc;
        resetSortStates();
        ElementSorter.sortByAvailableSpace(virtualDrives, sortedAsc);
        sortedAvailableSpaceAsc = !sortedAsc;
        refreshView(elements);
    }

    private JButton createHeaderButton(String text, Dimension dimension) {
        JButton button = new JButton(text);
        button.setPreferredSize(dimension);

        return button;
    }

    private void addButtonToHeaderPanel(JButton button, GridBagConstraints gbc, int gridx, int weightx) {
        gbc.gridx = gridx;
        gbc.weightx = weightx;
        headerPanel.add(button, gbc);
    }

    @Override
    public void refreshView() {
        removeAllElements();

        dirIsRoot = directory == RootDirectory.getInstance();

        addHeader();

        for (Element element : directory.getChildren()) {
            addElement(element);
        }

        resetSortStates();

        revalidate();
        repaint();
    }

    private void resetSortStates() {
        sortedNameAsc = false;
        sortedCreationAsc = false;
        sortedModificationAsc = false;
        sortedTypeAsc = false;
        sortedSizeAsc = false;
        sortedAvailableSpaceAsc = false;
    }

    private void refreshView(List<Element> elements) {
        removeAllElements();

        for (Element element : elements) {
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
