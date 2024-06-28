package pe.edu.utp.aed.fileexplorer.debug;

import pe.edu.utp.aed.fileexplorer.model.*;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;
import pe.edu.utp.aed.fileexplorer.view.components.DetailsElementCard;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProbandoWeas extends JFrame {
    private JList<Element> fileListView;
    private JToggleButton toggleViewButton;
    private boolean isIconView = true;

    public ProbandoWeas(List<Element> elements) {
        setTitle("File Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        DefaultListModel<Element> listModel = new DefaultListModel<>();
        for (Element element : elements) {
            listModel.addElement(element);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new DetailsElementCard(new FileFolder("Prueba", LocalDateTime.now())));
        // View for icons and details using JList
        fileListView = new JList<>(listModel);
        fileListView.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        fileListView.setCellRenderer(new IconViewRenderer());
        add(new JScrollPane(panel), BorderLayout.CENTER);

        // Toggle button
        toggleViewButton = new JToggleButton("Cambiar Vista");
        toggleViewButton.addActionListener(e -> toggleView());
        add(toggleViewButton, BorderLayout.SOUTH);
    }

    private void toggleView() {
        if (isIconView) {
            fileListView.setCellRenderer(new DetailViewRenderer());
        } else {
            fileListView.setCellRenderer(new IconViewRenderer());
        }
        isIconView = !isIconView;
        fileListView.repaint();
    }

    // Renderer for icon view with grid-like format
    public class IconViewRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JPanel panel = new JPanel(new BorderLayout());
            if (value instanceof Element) {
                Element element = (Element) value;
                JLabel label = new JLabel(IconAdapter.getScaledIcon(60, 60, element.getIcon()));
                label.setText(element.getName());
                label.setHorizontalTextPosition(JLabel.CENTER);
                label.setVerticalTextPosition(JLabel.BOTTOM);
                label.setPreferredSize(new Dimension(100, 100));
                panel.add(label, BorderLayout.CENTER);
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                if (isSelected) {
                    panel.setBackground(list.getSelectionBackground());
                } else {
                    panel.setBackground(list.getBackground());
                }
            }
            return panel;
        }
    }

    // Renderer for detail view in table-like format
    public class DetailViewRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            JPanel panel = new JPanel(new GridLayout(1, 4));
            if (value instanceof Element) {
                Element element = (Element) value;
                JLabel iconLabel = new JLabel(IconAdapter.getScaledIcon(30, 30, element.getIcon()));
                JLabel nameLabel = new JLabel(element.getName());
                JLabel dateLabel = new JLabel(element.getCreationDate().toString());
                JLabel sizeLabel = new JLabel(String.valueOf(element.getSize()));
                panel.add(iconLabel);
                panel.add(nameLabel);
                panel.add(dateLabel);
                panel.add(sizeLabel);
            }
            if (isSelected) {
                panel.setBackground(list.getSelectionBackground());
                panel.setForeground(list.getSelectionForeground());
            } else {
                panel.setBackground(list.getBackground());
                panel.setForeground(list.getForeground());
            }
            return panel;
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<Element> elements = new ArrayList<>();
            elements.add(new TextFile("Ola", LocalDateTime.now()));
            elements.add(new FileFolder("Folder", LocalDateTime.now()));
            FileFolder folder = new FileFolder("Folder2", LocalDateTime.now());
            folder.addChild(new TextFile("File", LocalDateTime.now()));
            elements.add(folder);
            elements.add(new VirtualDrive("Drive", LocalDateTime.now(), 20));

            ProbandoWeas explorer = new ProbandoWeas(elements);
            explorer.setVisible(true);
        });
    }
}
