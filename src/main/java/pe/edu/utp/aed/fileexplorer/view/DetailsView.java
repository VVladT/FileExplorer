package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.model.TextFile;
import pe.edu.utp.aed.fileexplorer.model.Folder;
import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.view.components.DetailsElementCard;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;
import pe.edu.utp.aed.fileexplorer.view.events.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DetailsView extends ElementView {
    private MouseHandler mouseHandler;
    private JPanel headerPanel;
    private JPanel contentPanel;
    private GridBagLayout headerLayout;
    private GridBagConstraints gbc;

    public DetailsView(Directory directory, ElementController controller, JLayeredPane layeredPane) {
        super(directory, controller, layeredPane);
        setLayout(new BorderLayout());

        mouseHandler = new MouseHandler(layeredPane, controller);
        addMouseListener(mouseHandler);

        headerPanel = new JPanel();
        headerLayout = new GridBagLayout();
        headerPanel.setLayout(headerLayout);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        directory.addObserver(this);
        updateDirectory(directory);
    }

    private void addHeader() {
        headerPanel.removeAll();
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        JButton nameHeader = new JButton("Name");
        nameHeader.setPreferredSize(new Dimension(200, 30));
        JButton creationDateHeader = new JButton("Creation Date");
        creationDateHeader.setPreferredSize(new Dimension(120, 30));
        JButton modificationDateHeader = new JButton("Modification Date");
        modificationDateHeader.setPreferredSize(new Dimension(120, 30));
        JButton typeHeader = new JButton("Type");
        typeHeader.setPreferredSize(new Dimension(80, 30));
        JButton sizeHeader = new JButton("Size");
        sizeHeader.setPreferredSize(new Dimension(80, 30));

        nameHeader.addActionListener(createSortActionListener(Comparator.comparing(Element::getName)));
        creationDateHeader.addActionListener(createSortActionListener(Comparator.comparing(Element::getCreationDate)));
        modificationDateHeader.addActionListener(createSortActionListener(Comparator.comparing(Element::getModificationDate)));
        sizeHeader.addActionListener(createSortActionListener(Comparator.comparing(Element::getSize)));

        gbc.gridx = 0;
        gbc.weightx = 10;
        headerPanel.add(nameHeader, gbc);

        gbc.gridx = 1;
        gbc.weightx = 5;
        headerPanel.add(creationDateHeader, gbc);

        gbc.gridx = 2;
        gbc.weightx = 5;
        headerPanel.add(modificationDateHeader, gbc);

        gbc.gridx = 3;
        gbc.weightx = 2;
        headerPanel.add(typeHeader, gbc);

        gbc.gridx = 4;
        gbc.weightx = 1;
        headerPanel.add(sizeHeader, gbc);

        headerPanel.revalidate();
        headerPanel.repaint();
    }

    private ActionListener createSortActionListener(Comparator<Element> comparator) {
        return e -> sortElements(comparator);
    }

    @Override
    public void refreshView() {
        removeAllElements();
        addHeader();

        List<Element> sortedElements = directory.getChildren(); // Ensure the list is sorted
        for (Element element : sortedElements) {
            addElement(element);
        }

        revalidate();
        repaint();
    }

    private void removeAllElements() {
        for (Element child : directory.getChildren()) {
            removeElement(child);
        }
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
            if (component instanceof DetailsElementCard dec) {
                if (dec.getElement().equals(element)) {
                    element.removeObserver(dec);
                    contentPanel.remove(dec);
                    revalidate();
                    repaint();
                    return;
                }
            }
        }
    }

    private void sortElements(Comparator<Element> comparator) {
        List<Element> sortedElements = directory.getChildren().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        directory.getChildren().clear();
        directory.getChildren().addAll(sortedElements);
        refreshView();
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
            if (component instanceof ElementCard ec) {
                allElements.add(ec);
            }
        }

        return allElements;
    }

    @Override
    public List<ElementCard> getDirectories() {
        List<ElementCard> directories = new ArrayList<>();

        for (Component component : contentPanel.getComponents()) {
            if (component instanceof ElementCard ec) {
                if (ec.getElement().isDirectory()) {
                    directories.add(ec);
                }
            }
        }

        return directories;
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        DetailsView detailsView = new DetailsView(directory, null, null);
        directory.removeChild(folder3);
        directory.addChild(new TextFile("oal", LocalDateTime.now()));
        frame.add(new JScrollPane(detailsView));
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
