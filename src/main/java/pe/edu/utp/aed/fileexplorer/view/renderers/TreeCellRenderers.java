package pe.edu.utp.aed.fileexplorer.view.renderers;

import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class TreeCellRenderers {
    public static class QuickAccessTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObject = node.getUserObject();
                if (userObject instanceof Element) {
                    Element element = (Element) userObject;
                    setText(element.getName());
                    setIcon(IconAdapter.getScaledIcon(24, 24, ((Element) userObject).getIcon()));
                } else {
                    setText("Acceso Rápido");
                    setIcon(IconAdapter.getScaledIcon(24, 24, IconAdapter.QUICK_ACCESS_ICON));
                }
            }
            return this;
        }
    }

    public static class DirectoryTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObject = node.getUserObject();
                if (userObject instanceof Element) {
                    Element element = (Element) userObject;
                    setText(element.getName());
                    setIcon(IconAdapter.getScaledIcon(24, 24, ((Element) userObject).getIcon()));
                }
            }
            return this;
        }
    }
}
