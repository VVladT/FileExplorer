package pe.edu.utp.aed.fileexplorer.controller;

import pe.edu.utp.aed.fileexplorer.model.*;
import pe.edu.utp.aed.fileexplorer.util.FileSystemEditor;
import pe.edu.utp.aed.fileexplorer.view.MainView;
import pe.edu.utp.aed.fileexplorer.view.components.ElementCard;

import javax.swing.*;
import java.util.List;

public class ElementController {
    private final DirectoryNavigator nav;
    private final ElementBuffer buffer;
    private final VirtualFileSystem vfs;
    private Directory currentDirectory;
    private Element selectedElement;
    private MainView mainView;

    public ElementController() {
        buffer = new ElementBuffer();
        nav = new DirectoryNavigator();
        vfs = new VirtualFileSystem();
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(Directory currentDirectory) {
        this.currentDirectory = currentDirectory;
        if (mainView != null) {
            mainView.refreshToolbar();
        }
    }

    public Element getSelectedElement() {
        return selectedElement;
    }

    public void setSelectedElement(Element selectedElement) {
        this.selectedElement = selectedElement;
        if (mainView != null) {
            mainView.refreshToolbar();
        }
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void createNewDrive() {
        VirtualDrive newDrive = FileSystemEditor.createNewDrive();
        vfs.addDrive(newDrive);
    }

    public void deleteElement() {
        try {
            if (selectedElement.isDirectory()) {
                if (nav.contains((Directory) selectedElement)) nav.clear();
            }
            currentDirectory.removeChild(selectedElement);
        } catch (IllegalArgumentException e) {

        }
        resetSelectedElement();
        mainView.refreshToolbar();
    }

    public void copyElement() {
        buffer.copyElement(selectedElement);
        mainView.refreshToolbar();
    }

    public void cutElement() {
        if (selectedElement.isDirectory()) {
            if (nav.contains((Directory) selectedElement)) nav.clear();
        }
        buffer.cutElement(currentDirectory, selectedElement);

        mainView.refreshToolbar();
    }

    public void moveElement(Directory target) {
        if (selectedElement.isDirectory()) {
            if (nav.contains((Directory) selectedElement)) nav.clear();
        }
        vfs.deleteElement(currentDirectory, selectedElement);
        vfs.addElement(target, selectedElement);
        resetSelectedElement();
        mainView.refreshToolbar();
    }

    public void pasteElement() {
        if (!buffer.isEmpty()) {
            Element element = null;
            element = buffer.recoveryElement();
            vfs.pasteElement(currentDirectory, element);
        }
        resetSelectedElement();
        mainView.refreshToolbar();
        refreshLayeredPane();
    }

    public List<ElementCard> getDirectoriesCard() {
        return mainView.getDirectories();
    }

    public void refreshLayeredPane() {
        mainView.getLayeredPane().revalidate();
        mainView.getLayeredPane().repaint();
    }

    public boolean bufferIsEmpty() {
        return buffer.isEmpty();
    }

    public void saveFile() {
    }

    public void loadFile() {
    }

    public void pinElementToQuickAccess() {
    }

    public void renameElement() {
        String newName = FileSystemEditor.renameElement(selectedElement);
        vfs.renameElement(currentDirectory, selectedElement, newName);
    }

    public void createNewFolder() {
        Folder newFolder = FileSystemEditor.createNewFolder();
        vfs.addElement(currentDirectory, newFolder);
    }

    public void createNewFile() {
        TextFile newFile = FileSystemEditor.createNewFile();
        vfs.addElement(currentDirectory, newFile);
    }

    public void switchIconView() {
        mainView.setIconView();
    }

    public void switchDetailsView() {
        mainView.setDetailsView();
    }

    public void refreshFocus() {
        mainView.requestFocus();
    }

    public JPanel getContentPanel() {
        return mainView.getContentPanel();
    }

    public void openElement() {
        openElement(selectedElement);
    }

    public void openElement(Element element) {
        if (element != null) {
            if (element.isDirectory()) {
                nav.addDirectoryToBack(currentDirectory);
                mainView.setCurrentDirectory((Directory) element);
                resetSelectedElement();
            } else {

            }
        }
    }

    public void searchElements(String nameFile) {
        List<Element> foundElements = vfs.search(nameFile);

    }

    public void searchByPath(String path) {
        Element element = vfs.getElementByPath(path);
        openElement(element);
    }

    public boolean navHasBack() {
        return nav.hasBack();
    }

    public boolean navHasNext() {
        return nav.hasNext();
    }

    public void back() {
        if (navHasBack()) {
            Directory nextDirectory = nav.back(currentDirectory);
            mainView.setCurrentDirectory(nextDirectory);
        }
    }

    public void next() {
        if (navHasNext()) {
            Directory nextDirectory = nav.next(currentDirectory);
            mainView.setCurrentDirectory(nextDirectory);
        }
    }

    private void resetSelectedElement() {
        selectedElement = null;
    }
}
