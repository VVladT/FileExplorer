package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.model.RootDirectory;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;
import pe.edu.utp.aed.fileexplorer.view.events.ToolBar;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainView extends JFrame {
    private ElementController controller;
    private Directory currentDirectory;
    private JPanel mainPanel;
    private QuickAccessPanel lateralPanel;
    private ToolBar toolBar;
    private ElementView elementView;

    public MainView(ElementController controller) {
        super("Explorador de archivos");
        this.controller = controller;
        this.currentDirectory = RootDirectory.getInstance();
        initialize();
    }

    private void initialize() {
        setDefaultSettings();
        initializeController();
        initializeComponents();
        arrangeComponents();
    }

    private void setDefaultSettings() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(calculateSize());
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initializeController() {
        controller.setCurrentDirectory(currentDirectory);
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        toolBar = new ToolBar(controller);
        elementView = new DetailsView(currentDirectory, controller, getLayeredPane());
        lateralPanel = new QuickAccessPanel(controller);
    }

    private void arrangeComponents() {
        add(new JScrollPane(lateralPanel), BorderLayout.WEST);
        add(mainPanel);
        setJMenuBar(toolBar);
        add(createUpperPanel(), BorderLayout.NORTH);
        refreshView();
    }

    private JPanel createUpperPanel() {
        JPanel upperPanel = new JPanel(new BorderLayout());
        upperPanel.add(toolBar.getToolbarPanel(), BorderLayout.NORTH);
        upperPanel.add(toolBar.getLowerPanel(), BorderLayout.SOUTH);
        return upperPanel;
    }

    public void setCurrentDirectory(Directory newDirectory) {
        setTitle("Explorador de archivos - " + newDirectory.getName());
        currentDirectory = newDirectory;
        toolBar.getAddressBar().setText(currentDirectory.getPath());
        updateDirectoryView();
    }

    private void updateDirectoryView() {
        elementView.updateDirectory(currentDirectory);
        controller.setCurrentDirectory(currentDirectory);
    }

    public void setElementView(ElementView elementView) {
        this.elementView = elementView;
        refreshView();
    }

    private void refreshView() {
        clearObservers();
        mainPanel.removeAll();
        mainPanel.add(new JScrollPane(elementView), BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private Dimension calculateSize() {
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenDimension.width * 0.9);
        int height = (int) (screenDimension.height * 0.9);
        return new Dimension(width, height);
    }

    public List<ElementCard> getDirectories() {
        return elementView.getDirectories();
    }

    public List<ElementCard> getElements() {
        return elementView.getAllElements();
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    public JPanel getContentPanel() {
        return elementView.getContentPanel();
    }

    public void refreshToolbar() {
        toolBar.enableButtons();
    }

    public void setIconView() {
        setElementView(new IconView(currentDirectory, controller, getLayeredPane()));
    }

    public void setDetailsView() {
        setElementView(new DetailsView(currentDirectory, controller, getLayeredPane()));
    }

    public void clearObservers() {
        for (ElementCard element : getElements()) {
            element.clear();
        }
    }

    public ElementCard getElementCard(Element element) {
        return elementView.getElementCard(element);
    }

    public void start() {
        setVisible(true);
    }
}
