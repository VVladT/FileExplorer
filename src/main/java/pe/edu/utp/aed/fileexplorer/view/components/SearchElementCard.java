package pe.edu.utp.aed.fileexplorer.view.components;

import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.util.FileSize;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.awt.*;

public class SearchElementCard extends ElementCard {
    private JLabel nameLabel, pathLabel, modificationDateLabel,
            sizeLabel;

    public SearchElementCard(Element element) {
        super(element);
    }

    @Override
    protected void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        nameLabel = createLabel(150);
        pathLabel = createLabel(250);
        modificationDateLabel = createLabel(120);
        sizeLabel = createLabel(80);

        gbc.gridx = 0;
        gbc.weightx = 5;
        add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 10;
        add(pathLabel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1;
        add(modificationDateLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        add(sizeLabel, gbc);

        refresh();
    }

    private JLabel createLabel(int width) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setHorizontalTextPosition(JLabel.RIGHT);
        label.setPreferredSize(new Dimension(width, 24));
        return label;
    }

    @Override
    public void refresh() {
        nameLabel.setText(element.getName());
        nameLabel.setIcon(IconAdapter.getScaledIcon
                (24, 24, element.getIcon()));
        pathLabel.setText(element.getPath());
        modificationDateLabel.setText(element.getModificationDateString());
        sizeLabel.setText(FileSize.formatSize(element.getSize()));

        revalidate();
        repaint();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
    }
}
