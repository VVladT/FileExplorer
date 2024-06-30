package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.*;
import pe.edu.utp.aed.fileexplorer.model.observers.DirectoryObserver;
import pe.edu.utp.aed.fileexplorer.model.observers.QuickAccessObserver;
import pe.edu.utp.aed.fileexplorer.view.renderers.TreeCellRenderers;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class QuickAccessPanel extends JPanel {
    private JTree quickAccessTree;
    private JTree directoryTree;
    private final ElementController controller;

    private QuickAccess quickAccess;
    private DefaultTreeModel directoryTreeModel;

    public QuickAccessPanel(ElementController controller) {
        this.quickAccess = controller.getVirtualFileSystem().getQuickAccess();
        this.controller = controller;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 300));

        initializeQuickAccessObserver();
        initializeQuickAccessPanel();
        initializeDirectoryPanel();
    }

    private void initializeQuickAccessObserver() {
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
    }

    private void initializeQuickAccessPanel() {
        DefaultMutableTreeNode quickAccessRootNode = new DefaultMutableTreeNode("Acceso Rápido");
        quickAccessTree = new JTree(quickAccessRootNode);
        quickAccessTree.setCellRenderer(new TreeCellRenderers.QuickAccessTreeCellRenderer());
        populateQuickAccessTree();
        add(quickAccessTree, BorderLayout.NORTH);

        quickAccessTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTreeClick(e, quickAccessTree);
            }
        });
    }

    private void initializeDirectoryPanel() {
        Directory rootDirectory = RootDirectory.getInstance();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootDirectory);
        directoryTreeModel = new DefaultTreeModel(rootNode);
        directoryTree = new JTree(directoryTreeModel);
        directoryTree.setCellRenderer(new TreeCellRenderers.DirectoryTreeCellRenderer());
        populateDirectoryTree(rootNode, rootDirectory);
        add(directoryTree, BorderLayout.CENTER);

        directoryTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTreeClick(e, directoryTree);
            }
        });
    }

    private void populateQuickAccessTree() {
        DefaultMutableTreeNode quickAccessRootNode = (DefaultMutableTreeNode) quickAccessTree.getModel().getRoot();
        for (Element element : quickAccess.getElements()) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(element);
            quickAccessRootNode.add(childNode);
        }
        quickAccessTree.expandRow(0);
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
        directoryTree.expandRow(0);
    }

    private void addDirectoryObserver(Directory directory) {
        directory.addObserver(new DirectoryObserver() {
            @Override
            public void elementAdded(Element element) {
                DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) directoryTreeModel.getRoot();
                DefaultMutableTreeNode parentNode = findNodeByElement(rootNode, directory);
                if (parentNode == null) {
                    parentNode = rootNode;
                }
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(element);
                directoryTreeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
                if (element instanceof Directory) {
                    addDirectoryObserver((Directory) element);
                }
                //expandTreeNode(directoryTree, parentNode);
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

    private void handleTreeClick(MouseEvent e, JTree tree) {
        TreePath path = tree.getSelectionPath();
        if (path != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            Element selectedElement = null;
            if (selectedNode.getUserObject() instanceof Element) {
                selectedElement = (Element) selectedNode.getUserObject();
            }
            if (selectedElement != null) {
                controller.openElement(selectedElement);
            }
        }
    }

    private static DefaultMutableTreeNode findNodeByElement(DefaultMutableTreeNode rootNode, Element element) {
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
}
