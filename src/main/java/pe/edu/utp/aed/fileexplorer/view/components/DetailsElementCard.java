package pe.edu.utp.aed.fileexplorer.view.components;

import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.awt.*;

public class DetailsElementCard extends ElementCard {
    private JLabel nameLabel, creationDateLabel, modificationDateLabel, typeLabel, sizeLabel;

    public DetailsElementCard(Element element) {
        super(element);
    }

    @Override
    protected void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.gridx = 0;
        gbc.weightx = 10;
        nameLabel = new JLabel(IconAdapter.getScaledIcon(24, 24, element.getIcon()));
        nameLabel.setText(element.getName());
        nameLabel.setHorizontalAlignment(JLabel.LEFT);
        nameLabel.setHorizontalTextPosition(JLabel.RIGHT);
        nameLabel.setPreferredSize(new Dimension(200, 24));
        add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 5;
        creationDateLabel = new JLabel(element.getCreationDateString());
        creationDateLabel.setHorizontalAlignment(JLabel.LEFT);
        creationDateLabel.setPreferredSize(new Dimension(120, 24));
        add(creationDateLabel, gbc);

        gbc.gridx = 2;
        gbc.weightx = 5;
        modificationDateLabel = new JLabel(element.getModificationDateString());
        modificationDateLabel.setHorizontalAlignment(JLabel.LEFT);
        modificationDateLabel.setPreferredSize(new Dimension(120, 24));
        add(modificationDateLabel, gbc);

        gbc.gridx = 3;
        gbc.weightx = 2;
        typeLabel = new JLabel(element.getType().toString());
        typeLabel.setHorizontalAlignment(JLabel.LEFT);
        typeLabel.setPreferredSize(new Dimension(80, 24));
        add(typeLabel, gbc);

        gbc.gridx = 4;
        gbc.weightx = 1;
        sizeLabel = new JLabel(String.valueOf(element.getSize()));
        sizeLabel.setHorizontalAlignment(JLabel.LEFT);
        sizeLabel.setPreferredSize(new Dimension(80, 24));
        add(sizeLabel, gbc);
        revalidate();
        repaint();
    }

    @Override
    public void refresh() {
        nameLabel.setText(element.getName());
        nameLabel.setIcon(IconAdapter.getScaledIcon(24, 24, element.getIcon()));
        creationDateLabel.setText(element.getCreationDateString());
        modificationDateLabel.setText(element.getModificationDateString());
        typeLabel.setText(element.getType().toString());
        sizeLabel.setText(String.valueOf(element.getSize()));
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
    }
}
