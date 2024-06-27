package pe.edu.utp.aed.fileexplorer.view.events;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.exceptions.PathNotFoundException;
import pe.edu.utp.aed.fileexplorer.model.RootDirectory;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ToolBar extends JMenuBar {
    private final ElementController controller;
    private JPanel toolbarPanel;
    private JPanel lowerPanel;
    private CardLayout cardLayout;

    private JButton saveButton;
    private JButton loadButton;

    private JButton pinToQuickAccessButton;
    private JButton renameButton;
    private JButton copyButton;
    private JButton cutButton;
    private JButton pasteButton;
    private JButton deleteButton;
    private JButton newDriveButton;
    private JButton newFolderButton;
    private JButton newFileButton;

    private JButton iconsButton;
    private JButton detailsButton;

    private JButton backButton;
    private JButton nextButton;

    private JTextField addressBar;
    private JTextField searchBar;

    public ToolBar(ElementController controller) {
        this.controller = controller;
        cardLayout = new CardLayout();
        toolbarPanel = new JPanel(cardLayout);

        initButtons();
        initToolbars();
        initMenuButtons();
        initSearchBars();
    }

    private void initMenuButtons() {
        JButton fileButton = new JButton("Archivo");
        JButton homeButton = new JButton("Inicio");
        JButton viewButton = new JButton("Vista");

        fileButton.addActionListener(e -> switchToolbar("Archivo"));
        homeButton.addActionListener(e -> switchToolbar("Inicio"));
        viewButton.addActionListener(e -> switchToolbar("Vista"));

        add(fileButton);
        add(homeButton);
        add(viewButton);
    }

    private void switchToolbar(String toolbarName) {
        cardLayout.show(toolbarPanel, toolbarName);
    }

    private void initButtons() {
        saveButton = new JButton("Guardar");
        loadButton = new JButton("Cargar");

        pinToQuickAccessButton = new JButton("Anclar a acceso rápido");
        renameButton = new JButton("Renombrar");
        copyButton = new JButton("Copiar");
        cutButton = new JButton("Cortar");
        pasteButton = new JButton("Pegar");
        deleteButton = new JButton("Eliminar");
        newDriveButton = new JButton("Nuevo disco");
        newFolderButton = new JButton("Nueva carpeta");
        newFileButton = new JButton("Nuevo archivo");

        iconsButton = new JButton("Iconos");
        detailsButton = new JButton("Detalles");

        backButton = new JButton(IconAdapter.getScaledIcon(24, 24, IconAdapter.BACK_ICON));
        nextButton = new JButton(IconAdapter.getScaledIcon(24, 24, IconAdapter.NEXT_ICON));
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        nextButton.setOpaque(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setBorderPainted(false);

        addButtonActions();
    }

    private void addButtonActions() {
        saveButton.addActionListener(e -> controller.saveFile());
        loadButton.addActionListener(e -> controller.loadFile());

        pinToQuickAccessButton.addActionListener(e -> controller.pinElementToQuickAccess());
        renameButton.addActionListener(e -> controller.renameElement());
        copyButton.addActionListener(e -> controller.copyElement());
        cutButton.addActionListener(e -> controller.cutElement());
        pasteButton.addActionListener(e -> controller.pasteElement());
        deleteButton.addActionListener(e -> controller.deleteElement());
        newDriveButton.addActionListener(e -> controller.createNewDrive());
        newFolderButton.addActionListener(e -> controller.createNewFolder());
        newFileButton.addActionListener(e -> controller.createNewFile());

        iconsButton.addActionListener(e -> controller.switchIconView());
        detailsButton.addActionListener(e -> controller.switchDetailsView());

        backButton.addActionListener(e -> controller.back());
        nextButton.addActionListener(e -> controller.next());

        enableButtons();
    }

    public void enableButtons() {
        if (controller.getSelectedElement() != null) {
            renameButton.setEnabled(true);
            copyButton.setEnabled(true);
            cutButton.setEnabled(true);
            deleteButton.setEnabled(true);
        } else {
            renameButton.setEnabled(false);
            copyButton.setEnabled(false);
            cutButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        if (controller.getCurrentDirectory() != RootDirectory.getInstance()
                && controller.getCurrentDirectory() != null) {
            pasteButton.setEnabled(!controller.bufferIsEmpty());
            newFolderButton.setEnabled(true);
            newFileButton.setEnabled(true);
            newDriveButton.setEnabled(false);
        } else {
            pasteButton.setEnabled(!controller.bufferIsEmpty());
            newFolderButton.setEnabled(false);
            newFileButton.setEnabled(false);
            newDriveButton.setEnabled(true);
        }

        backButton.setEnabled(controller.navHasBack());
        nextButton.setEnabled(controller.navHasNext());
    }

    private void initToolbars() {
        JToolBar fileToolbar = new JToolBar();
        fileToolbar.add(saveButton);
        fileToolbar.add(loadButton);
        fileToolbar.setFloatable(false);

        JToolBar homeToolbar = new JToolBar();
        homeToolbar.add(pinToQuickAccessButton);
        homeToolbar.add(renameButton);
        homeToolbar.add(copyButton);
        homeToolbar.add(cutButton);
        homeToolbar.add(pasteButton);
        homeToolbar.add(deleteButton);
        homeToolbar.add(newDriveButton);
        homeToolbar.add(newFolderButton);
        homeToolbar.add(newFileButton);
        homeToolbar.setFloatable(false);

        JToolBar viewToolbar = new JToolBar();
        viewToolbar.add(iconsButton);
        viewToolbar.add(detailsButton);
        viewToolbar.setFloatable(false);

        toolbarPanel.add(fileToolbar, "Archivo");
        toolbarPanel.add(homeToolbar, "Inicio");
        toolbarPanel.add(viewToolbar, "Vista");
    }

    private void initSearchBars() {
        lowerPanel = new JPanel(new GridBagLayout());

        addressBar = new JTextField(30);
        addressBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        controller.searchByPath(addressBar.getText());
                    } catch (PathNotFoundException ex) {
                        addressBar.setText(controller.getCurrentDirectory().getPath());
                    }
                }
            }
        });

        searchBar = new JTextField(20);
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    controller.searchElements(searchBar.getText());
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.weightx = 0;
        lowerPanel.add(backButton, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        lowerPanel.add(nextButton, gbc);

        gbc.gridx = 2;
        gbc.weightx = 4;
        lowerPanel.add(addressBar, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0;
        lowerPanel.add(new JLabel("Buscar: "), gbc);

        gbc.gridx = 4;
        gbc.weightx = 2;
        lowerPanel.add(searchBar, gbc);
    }

    public JPanel getToolbarPanel() {
        return toolbarPanel;
    }

    public JPanel getLowerPanel() {
        return lowerPanel;
    }
}
