package pe.edu.utp.aed.fileexplorer.view.events;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.model.RootDirectory;
import pe.edu.utp.aed.fileexplorer.model.VirtualDrive;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;
import pe.edu.utp.aed.fileexplorer.view.components.TransferElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    private final JLayeredPane layeredPane;
    private final ElementController elementController;
    private ElementCard selectedElement;
    private TransferElement transferElement;
    private JPopupMenu popupMenu;
    private boolean dragging = false;

    public MouseHandler(JLayeredPane layeredPane, ElementController elementController) {
        this.layeredPane = layeredPane;
        this.elementController = elementController;
        popupMenu = new JPopupMenu();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)
                && !elementController.isSearchMode()) {
            Component component = e.getComponent();
            if (selectedElement != null) {
                selectedElement.resetClick();
            }
            if (component instanceof ElementCard ec) {
                selectedElement = ec;
                selectedElement.click();
                elementController.setSelectedElement(selectedElement.getElement());
            }
            updatePopupMenu(component);
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }

        elementController.refreshFocus();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            handleLeftPressed(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            handleLeftDragged(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            handleLeftReleased(e);
        }
        elementController.refreshFocus();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getComponent() instanceof ElementCard ec && !ec.isSelected()) {
            if (!dragging) {
                ec.hover();
            } else if (ec.getElement().isDirectory()) {
                ec.hover();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getComponent() instanceof ElementCard ec && !ec.isSelected()) {
            ec.resetHover();
        }
    }

    private void handleLeftPressed(MouseEvent e) {
        handleLeftClick(e);
    }

    private void handleLeftClick(MouseEvent e) {
        Component component = e.getComponent();
        if (e.getClickCount() == 2) {
            selectedElement = null;
            elementController.openElement();
        }
        if (component instanceof ElementCard ec) {
            if (selectedElement != null) {
                selectedElement.resetClick();
            }
            selectedElement = ec;
            selectedElement.click();
            elementController.setSelectedElement(selectedElement.getElement());
        } else {
            resetSelection();
        }
    }

    private void handleLeftDragged(MouseEvent e) {
        if (selectedElement != null) {
            if (!(selectedElement.getElement() instanceof VirtualDrive)) {
                selectedElement.resetClick();
                selectedElement.resetHover();
                Point location = getRelativeLocation(e);
                if (transferElement == null) {
                    addTransferElementToLayeredPane(location);
                }
                updateTransferElementLocation(location);
                hoverDirectories(location);
                dragging = true;
            }
        }
    }

    private void handleLeftReleased(MouseEvent e) {
        if (selectedElement != null && dragging) {
            Point location = getRelativeLocation(e);
            updateElementCard(location);
            resetLayeredPane();
            resetHandlerState();
        }
    }

    private void hoverDirectories(Point location) {
        Point convertedPoint = SwingUtilities.convertPoint(layeredPane, location, elementController.getContentPanel());

        for (ElementCard elementCard : elementController.getDirectoriesCard()) {
            if (elementCard.getBounds().contains(convertedPoint)
                    && elementCard.getElement() != transferElement.getElement()) {
                elementCard.hover();
            } else {
                elementCard.resetHover();
            }
        }
    }

    private Point getRelativeLocation(MouseEvent e) {
        return SwingUtilities.convertPoint(selectedElement, e.getPoint(), layeredPane);
    }

    private void addTransferElementToLayeredPane(Point location) {
        transferElement = new TransferElement(selectedElement.getElement());
        transferElement.setSize(transferElement.getPreferredSize());
        transferElement.setLocation(location);
        layeredPane.add(transferElement, JLayeredPane.DRAG_LAYER);
        layeredPane.repaint();
    }

    private void updateElementCard(Point location) {
        Point convertedPoint = SwingUtilities.convertPoint(layeredPane, location, elementController.getContentPanel());

        for (ElementCard elementCard : elementController.getDirectoriesCard()) {
            if (elementCard.getBounds().contains(convertedPoint)) {
                if (elementCard.getElement() != transferElement.getElement()) {
                    Directory target = (Directory) elementCard.getElement();
                    elementController.moveElement(target);
                    elementCard.resetHover();
                    break;
                }
            }
        }
    }

    private void updateTransferElementLocation(Point location) {
        Point newLocation = new Point(location.x - transferElement.getWidth() / 2,
                location.y - transferElement.getHeight() / 2);
        transferElement.setLocation(newLocation);
        layeredPane.repaint();
    }

    private void resetLayeredPane() {
        if (transferElement != null) {
            layeredPane.remove(transferElement);
            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

    private void resetHandlerState() {
        selectedElement = null;
        dragging = false;
        transferElement = null;
    }

    public void resetSelection() {
        if (selectedElement != null) {
            selectedElement.resetClick();
        }
        elementController.setSelectedElement(null);
    }

    private void updatePopupMenu(Component component) {
        popupMenu.removeAll();
        if (component instanceof ElementCard ec) {
            addElementMenuItems(ec.getElement());
        } else if (elementController.getCurrentDirectory() == RootDirectory.getInstance()) {
            addRootMenuItems();
        } else {
            addDirectoryMenuItems();
        }
    }

    private void addElementMenuItems(Element element) {
        JMenuItem openItem = new JMenuItem("Abrir");
        JMenuItem renameItem = new JMenuItem("Renombrar");
        JMenuItem pinItem = new JMenuItem("Anclar a acceso rápido");
        JMenuItem unpinItem = new JMenuItem("Desanclar de acceso rápido");
        JMenuItem copyPathItem = new JMenuItem("Copiar ruta a portapapeles");
        JMenuItem copyItem = new JMenuItem("Copiar");
        JMenuItem cutItem = new JMenuItem("Cortar");
        JMenuItem exportItem = new JMenuItem("Exportar");
        JMenuItem deleteItem = new JMenuItem("Eliminar");

        openItem.addActionListener(e -> elementController.openElement());
        renameItem.addActionListener(e -> elementController.renameElement());
        pinItem.addActionListener(e -> elementController.pinElementToQuickAccess());
        unpinItem.addActionListener(e -> elementController.unPinElementFromQuickAccess());
        copyPathItem.addActionListener(e -> elementController.copyPathToClipboard());
        copyItem.addActionListener(e -> elementController.copyElement());
        cutItem.addActionListener(e -> elementController.cutElement());
        exportItem.addActionListener(e -> elementController.exportFile());
        deleteItem.addActionListener(e -> elementController.deleteElement());

        popupMenu.add(openItem);
        popupMenu.add(renameItem);
        if (!elementController.getVirtualFileSystem().getQuickAccess().contains(element)) {
            popupMenu.add(pinItem);
        } else {
            popupMenu.add(unpinItem);
        }
        popupMenu.add(copyPathItem);
        if (element != RootDirectory.getInstance() &&
                !(element instanceof VirtualDrive)) {
            popupMenu.add(copyItem);
            popupMenu.add(cutItem);
            popupMenu.add(deleteItem);
            popupMenu.add(exportItem);
        } else {
            popupMenu.add(deleteItem);
        }
    }

    private void addRootMenuItems() {
        JMenuItem newDriveItem = new JMenuItem("Nuevo disco");
        newDriveItem.addActionListener(e -> elementController.createNewDrive());
        popupMenu.add(newDriveItem);
    }

    private void addDirectoryMenuItems() {
        JMenuItem newFolderItem = new JMenuItem("Nueva carpeta");
        JMenuItem newFileItem = new JMenuItem("Nuevo archivo");
        JMenuItem pasteItem = new JMenuItem("Pegar");

        newFolderItem.addActionListener(e -> elementController.createNewFolder());
        newFileItem.addActionListener(e -> elementController.createNewFile());
        pasteItem.addActionListener(e -> elementController.pasteElement());

        popupMenu.add(newFolderItem);
        popupMenu.add(newFileItem);
        popupMenu.add(pasteItem);
    }
}
