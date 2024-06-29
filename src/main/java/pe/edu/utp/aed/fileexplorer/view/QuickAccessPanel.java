package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.*;
import pe.edu.utp.aed.fileexplorer.model.observers.DirectoryObserver;
import pe.edu.utp.aed.fileexplorer.model.observers.QuickAccessObserver;
import pe.edu.utp.aed.fileexplorer.util.FileSize;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;

public class QuickAccessPanel extends JPanel {
    private JTree quickAccessTree;
    private JTree directoryTree;
    private final ElementController controller;

    private QuickAccess quickAccess;
    private DefaultTreeModel directoryTreeModel;

    public QuickAccessPanel(ElementController controller) {
        this.quickAccess = controller.getVirtualFileSystem().getQuickAccess();
        this.controller = controller;
        setPreferredSize(new Dimension(200, 400));
        this.quickAccess.addObserver(new QuickAccessObserver() {
            @Override
            public void elementAdded(Element element) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(element);
                DefaultTreeModel model = (DefaultTreeModel) quickAccessTree.getModel();
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                model.insertNodeInto(node, root, root.getChildCount());

                if (root.getChildCount() == 1) {
                    quickAccessTree.expandRow(0);
                }
            }

            @Override
            public void elementRemoved(Element element) {
                DefaultTreeModel model = (DefaultTreeModel) quickAccessTree.getModel();
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) quickAccessTree.getModel().getRoot();
                DefaultMutableTreeNode nodeToRemove = findNodeByElement(root, element);
                if (nodeToRemove != null) {
                    model.removeNodeFromParent(nodeToRemove);
                }
            }
        });

        setLayout(new BorderLayout());

        // Inicializar el árbol de acceso rápido
        DefaultMutableTreeNode quickAccessRootNode = new DefaultMutableTreeNode("Acceso Rápido");
        initializeQuickAccessTree(quickAccessRootNode);
        JScrollPane quickAccessScrollPane = new JScrollPane(quickAccessTree);
        quickAccessScrollPane.setPreferredSize(new Dimension(200, 150));
        add(quickAccessScrollPane, BorderLayout.NORTH);

        // Inicializar el árbol de directorios y archivos
        Directory rootDirectory = RootDirectory.getInstance();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootDirectory);
        initializeDirectoryTree(rootNode, rootDirectory);
        JScrollPane treeScrollPane = new JScrollPane(directoryTree);
        add(treeScrollPane, BorderLayout.CENTER);
    }

    private void initializeQuickAccessTree(DefaultMutableTreeNode rootNode) {
        quickAccessTree = new JTree(rootNode);
        quickAccessTree.setCellRenderer(new QuickAccessTreeCellRenderer());
        quickAccessTree.expandRow(0);

        populateQuickAccessTree();

        quickAccessTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath path = quickAccessTree.getSelectionPath();
                if (path != null) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    Element selectedElement = (Element) selectedNode.getUserObject();
                    if (selectedElement != null) {
                        handleSingleClick(selectedElement);
                    }
                }
            }
        });
    }

    private void populateQuickAccessTree() {
        for (Element element : quickAccess.getElements()) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(element);
            DefaultMutableTreeNode quickAccessRootNode = (DefaultMutableTreeNode) quickAccessTree.getModel().getRoot();
            quickAccessRootNode.add(childNode);
        }

        quickAccessTree.expandRow(0);
    }

    private void initializeDirectoryTree(DefaultMutableTreeNode rootNode, Directory rootDirectory) {
        directoryTreeModel = new DefaultTreeModel(rootNode);
        directoryTree = new JTree(directoryTreeModel);
        directoryTree.setCellRenderer(new DirectoryTreeCellRenderer());
        populateDirectoryTree(rootNode, rootDirectory);
        directoryTree.expandRow(0);

        directoryTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath path = directoryTree.getSelectionPath();
                if (path != null) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    Element selectedElement = (Element) selectedNode.getUserObject();
                    if (selectedElement != null) {
                        handleSingleClick(selectedElement);
                    }
                }
            }
        });
    }

    private void populateDirectoryTree(DefaultMutableTreeNode treeNode, Directory directory) {
        addDirectoryObserver(directory);
        for (Element child : directory.getChildren()) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
            treeNode.add(childNode);
            if (child instanceof Directory) {
                populateDirectoryTree(childNode, (Directory) child);
            }
        }
    }

    private void addDirectoryObserver(Directory directory) {
        directory.addObserver(new DirectoryObserver() {
            @Override
            public void elementAdded(Element element) {
                DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) directoryTreeModel.getRoot();
                DefaultMutableTreeNode parentNode = findNodeByElement(rootNode, directory);
                if (parentNode == null) {
                    parentNode = rootNode; // Si no se encuentra el nodo padre, usa la raíz
                }
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(element);
                directoryTreeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
                if (element instanceof Directory) {
                    addDirectoryObserver((Directory) element);
                }
                directoryTree.expandPath(new TreePath(parentNode.getPath()));
            }

            @Override
            public void elementRemoved(Element element) {
                DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) directoryTreeModel.getRoot();
                DefaultMutableTreeNode parentNode = findNodeByElement(rootNode, directory);
                DefaultMutableTreeNode nodeToRemove = findNodeByElement(parentNode, element);
                if (nodeToRemove != null) {
                    directoryTreeModel.removeNodeFromParent(nodeToRemove);
                }
            }
        });
    }

    private DefaultMutableTreeNode findNodeByElement(DefaultMutableTreeNode rootNode, Element element) {
        if (rootNode.getUserObject().equals(element)) {
            return rootNode;
        }

        for (int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootNode.getChildAt(i);
            DefaultMutableTreeNode foundNode = findNodeByElement(node, element);
            if (foundNode != null) {
                return foundNode;
            }
        }
        return null;
    }



    private void handleSingleClick(Element selectedElement) {
        controller.openElement(selectedElement);
    }

}

class QuickAccessTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();
            if (userObject instanceof Element) {
                Element element = (Element) userObject;
                setText(element.getName());
                setIcon(IconAdapter.getScaledIcon(24, 24, ((Element) userObject).getIcon())); // Reemplaza con tu lógica para obtener el ícono del elemento
            } else {
                setText("Acceso Rápido");
                setIcon(IconAdapter.getScaledIcon(24, 24, IconAdapter.QUICK_ACCESS_ICON));
            }
        }
        return this;
    }
}

class DirectoryTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();
            if (userObject instanceof Element) {
                Element element = (Element) userObject;
                setText(element.getName());
                setIcon(IconAdapter.getScaledIcon(24, 24, ((Element) userObject).getIcon())); // Reemplaza con tu lógica para obtener el ícono del directorio
            }
        }
        return this;
    }
}
