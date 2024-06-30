package pe.edu.utp.aed.fileexplorer.controller;

import pe.edu.utp.aed.fileexplorer.exceptions.NotEnoughSpaceException;
import pe.edu.utp.aed.fileexplorer.exceptions.SameNameException;
import pe.edu.utp.aed.fileexplorer.model.*;
import pe.edu.utp.aed.fileexplorer.util.*;
import pe.edu.utp.aed.fileexplorer.view.MainView;
import pe.edu.utp.aed.fileexplorer.view.SearchView;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;
import xyz.cupscoffee.files.api.exception.InvalidFormatFileException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ElementController {
    private final DirectoryNavigator nav;
    private final ElementBuffer buffer;
    private final VirtualFileSystem vfs;
    private Directory currentDirectory;
    private Element selectedElement;
    private final MainView mainView;

    public ElementController(VirtualFileSystem vfs) {
        buffer = new ElementBuffer();
        nav = new DirectoryNavigator();
        this.vfs = vfs;
        mainView = new MainView(this);
    }

    public void startApplication() {
        mainView.setVisible(true);
    }

    // Accessor Methods
    public VirtualFileSystem getVirtualFileSystem() {
        return vfs;
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(Directory currentDirectory) {
        this.currentDirectory = currentDirectory;
        refreshToolbar();
    }

    public Element getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(Element selectedElement) {
        this.selectedElement = selectedElement;
        refreshToolbar();
    }

    // File System Operations
    public void createNewDrive() {
        VirtualDrive newDrive = FileSystemEditor.createNewDrive();
        vfs.addDrive(newDrive);
    }

    public void createNewFolder() {
        FileFolder newFolder = FileSystemEditor.createNewFolder();
        vfs.addElement(currentDirectory, newFolder);
    }

    public void createNewFile() {
        TextFile newFile = FileSystemEditor.createNewFile();
        vfs.addElement(currentDirectory, newFile);
    }

    public void deleteElement() {
        if (selectedElement != null) {
            handleDirectoryNavigation(selectedElement);
            currentDirectory.removeChild(selectedElement);
        }
        resetSelectedElement();
    }

    public void renameElement() {
        String oldName = selectedElement.getName();
        String newName = FileSystemEditor.renameElement(selectedElement);
        try {
            if (!oldName.equals(newName)) {
                vfs.renameElement(currentDirectory, selectedElement, newName);
            }
        } catch (SameNameException e) {
            showMessage("Ya existe un archivo con ese nombre: " + newName);
        }
    }

    public void pinElementToQuickAccess() {
        if (selectedElement != null) {
            vfs.addElementToQuickAccess(selectedElement);
        }
    }

    public void unPinElementFromQuickAccess() {
        if (selectedElement != null) {
            vfs.removeElementFromQuickAccess(selectedElement);
        }
    }

    // Buffer Operations

    public void copyElement() {
        buffer.copyElement(selectedElement);
        refreshToolbar();
    }

    public void cutElement() {
        if (selectedElement != null && !(selectedElement instanceof VirtualDrive)
                && selectedElement != RootDirectory.getInstance()) {
            handleDirectoryNavigation(selectedElement);
            ElementCard elementCard = mainView.getElementCard(selectedElement);
            elementCard.cut();
            buffer.cutElement(currentDirectory, selectedElement);
            refreshToolbar();
        }
    }

    public void pasteElement() {
        if (!buffer.isEmpty()) {
            Element element = buffer.peekElement();
            if (element.isDirectory() && buffer.getCommand() == ElementBuffer.Command.CUT) {
                Directory dir = (Directory) element;
                if (dir.containsElement(currentDirectory)) {
                    showMessage("The cut directory contains current directory");
                    return;
                }
            }

            try {
                element = buffer.recoveryElement();
                vfs.pasteElement(currentDirectory, element);
            } catch (NotEnoughSpaceException e) {
                showMessage(e.getMessage());
            }
        }
        resetSelectedElement();
        refreshLayeredPane();
    }

    public boolean bufferIsEmpty() {
        return buffer.isEmpty();
    }

    // Navigation Operations
    public boolean navHasBack() {
        return nav.hasBack();
    }

    public boolean navHasNext() {
        return nav.hasNext();
    }

    public void back() {
        if (navHasBack()) {
            Directory pastDirectory = nav.back(currentDirectory);
            mainView.setCurrentDirectory(pastDirectory);
        }
    }

    public void next() {
        if (navHasNext()) {
            Directory nextDirectory = nav.next(currentDirectory);
            mainView.setCurrentDirectory(nextDirectory);
        }
    }

    // Export Operations
    public void exportFile() {
        exportFile(selectedElement);
    }

    private void exportFile(Element element) {
        if (element != null) {
            if (element.isDirectory()) {
                exportDirectory((Directory) element);
            } else if (element instanceof TextFile) {
                exportTextFile((TextFile) element);
            }
        }
    }

    private void exportDirectory(Directory directory) {
        if (directory instanceof FileFolder) {
            FileExporter fileExporter = new FileExporter();
            fileExporter.exportFolder((FileFolder) directory);
        }
    }

    private void exportTextFile(TextFile textFile) {
        TextExporter exporterText = new TextExporter();
        exporterText.exportTextFile(textFile);
    }

    // Element Operations
    public void moveElement(Directory target) {
        handleDirectoryNavigation(selectedElement);
        vfs.deleteElement(currentDirectory, selectedElement);
        vfs.addElement(target, selectedElement);
        resetSelectedElement();
    }

    public void openElement() {
        openElement(selectedElement);
    }

    public void openElement(Element element) {
        if (element != null) {
            if (element.isDirectory()) {
                if (currentDirectory == element) return;
                nav.addDirectoryToBack(currentDirectory);
                mainView.setCurrentDirectory((Directory) element);
                resetSelectedElement();
            } else if (element instanceof TextFile) {
                openTextFileForEditing((TextFile) element);
            }
        }
    }

    private void openTextFileForEditing(TextFile textFile) {
        TextEditor editor = new TextEditor();
        editor.openFile(textFile);
    }

    // Search Operations
    public void searchElements(String nameFile) {
        List<Element> foundElements = vfs.search(nameFile);
        mainView.setSearchView(foundElements);
    }

    public void searchByPath(String path) {
        Element element = vfs.getElementByPath(path);
        openElement(element);
    }

    // Clipboard Operations
    public void copyPathToClipboard() {
        if (selectedElement != null) {
            String path = selectedElement.getPath();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(path), null);
        }
    }

    // View Operations
    public List<ElementCard> getDirectoriesCard() {
        return mainView.getDirectories();
    }

    public void switchIconView() {
        mainView.setIconView();
    }

    public void switchDetailsView() {
        mainView.setDetailsView();
    }

    public void refreshFocus() {
        mainView.refreshFocus();
    }

    public JPanel getContentPanel() {
        return mainView.getContentPanel();
    }

    public void refreshLayeredPane() {
        mainView.getLayeredPane().revalidate();
        mainView.getLayeredPane().repaint();
    }

    private void refreshToolbar() {
        if (mainView != null) {
            mainView.refreshToolbar();
        }
    }

    private void resetSelectedElement() {
        selectedElement = null;
        refreshToolbar();
    }

    private void handleDirectoryNavigation(Element element) {
        if (element.isDirectory() && nav.contains((Directory) element)) {
            nav.clear();
        }
    }

    public void saveFile() {
        try {
            SaveAndLoadVFS.saveVFS(vfs);
            showMessage("Archivo guardado correctamente.");
        } catch (IOException e) {
            showMessage("Error al guardar el archivo");
        }
    }

    public void loadFile() {
        try {
            SaveAndLoadVFS.loadVFS();
            mainView.reload();
        } catch (FileNotFoundException e) {
            showMessage("No se encontró el archivo");
        } catch (InvalidFormatFileException e) {
            showMessage("Formato de archivo inválido");
        }
    }

    public boolean isSearchMode() {
        return mainView.getElementView() instanceof SearchView;
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(mainView, message);
    }
}
