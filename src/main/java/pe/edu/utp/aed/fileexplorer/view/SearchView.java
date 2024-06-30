package pe.edu.utp.aed.fileexplorer.view;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.view.components.SearchElementCard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchView extends ElementView {
    private final JPanel headerPanel;
    private final JPanel contentPanel;
    private final List<Element> elements;

    public SearchView(List<Element> elements, ElementController controller) {
        super(null, controller, null);
        this.elements = elements;

        setLayout(new BorderLayout());
        headerPanel = new JPanel(new GridBagLayout());
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        refreshView();
    }

    protected void init() {
        addMouseListener(mouseHandler);
    }

    @Override
    public void refreshView() {
        addHeader();
        addElements();
        revalidate();
        repaint();
    }

    private void addElements() {
        for (Element element : elements) {
            addElement(element);
        }

        revalidate();
        repaint();
    }

    private void addHeader() {
        headerPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        JLabel nameLabel = createHeaderLabel("Nombre", 150);
        JLabel pathLabel = createHeaderLabel("Ruta", 250);
        JLabel modificationDateLabel = createHeaderLabel("Fecha de modificación", 120);
        JLabel sizeLabel = createHeaderLabel("Tamaño", 80);

        gbc.gridx = 0;
        gbc.weightx = 5;
        headerPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 10;
        headerPanel.add(pathLabel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1;
        headerPanel.add(modificationDateLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        headerPanel.add(sizeLabel, gbc);

        headerPanel.revalidate();
        headerPanel.repaint();
    }

    private JLabel createHeaderLabel(String text, int width) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(12f));
        label.setPreferredSize(new Dimension(width, 30));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return label;
    }

    @Override
    public void addElement(Element element) {
        SearchElementCard searchElementCard = new SearchElementCard(element);
        searchElementCard.addMouseListener(mouseHandler);
        contentPanel.add(searchElementCard);
        revalidate();
        repaint();
    }

    @Override
    public void removeElement(Element element) {

    }

    @Override
    public void elementAdded(Element element) {

    }

    @Override
    public void elementRemoved(Element element) {

    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }
}
