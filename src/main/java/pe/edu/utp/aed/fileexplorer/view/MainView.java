package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;
import pe.edu.utp.aed.fileexplorer.view.events.ToolBar;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainView extends JFrame {
    private ElementController controller;
    private Directory currentDirectory;
    private JPanel panel;
    private ToolBar toolBar;
    private ElementView elementView;

    public MainView(ElementController controller, Directory currentDirectory) {
        super("Explorador de archivos");
        this.currentDirectory = currentDirectory;
        controller.setCurrentDirectory(currentDirectory);
        this.controller = controller;
        controller.setMainView(this);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        elementView = new DetailsView(currentDirectory, null, null);
        panel.add(new JScrollPane(elementView), BorderLayout.CENTER);
        add(panel);
        toolBar = new ToolBar(controller);
        setJMenuBar(toolBar);
        JPanel upperPanel = new JPanel(new BorderLayout());
        upperPanel.add(toolBar.getToolbarPanel(), BorderLayout.NORTH);
        upperPanel.add(toolBar.getLowerPanel(), BorderLayout.SOUTH);
        add(upperPanel, BorderLayout.NORTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(calculateSize());
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void setCurrentDirectory(Directory newDirectory) {
        currentDirectory = newDirectory;
        elementView.updateDirectory(currentDirectory);
        controller.setCurrentDirectory(currentDirectory);
    }

    public void setElementView(ElementView elementView) {
        this.elementView = elementView;
        panel.removeAll();
        panel.add(new JScrollPane(elementView), BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
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
        elementView = new IconView(currentDirectory, controller, getLayeredPane());
        panel.removeAll();
        panel.add(new JScrollPane(elementView), BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    public void setDetailsView() {
        elementView = new DetailsView(currentDirectory, controller, getLayeredPane());
        panel.removeAll();
        panel.add(new JScrollPane(elementView), BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }
}
