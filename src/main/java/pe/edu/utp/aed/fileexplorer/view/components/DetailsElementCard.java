package pe.edu.utp.aed.fileexplorer.view.components;

import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.model.VirtualDrive;
import pe.edu.utp.aed.fileexplorer.util.FileSize;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.awt.*;

public class DetailsElementCard extends ElementCard {
    private JLabel nameLabel, creationDateLabel, modificationDateLabel, typeLabel,
            sizeLabel, availableSpaceLabel;

    public DetailsElementCard(Element element) {
        super(element);
    }

    @Override
    protected void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        if (element instanceof VirtualDrive) {
            nameLabel = createLabel(200);
            typeLabel = createLabel(200);
            sizeLabel = createLabel(100);
            availableSpaceLabel = createLabel(100);

            gbc.gridx = 0;
            gbc.weightx = 10;
            add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 5;
            add(typeLabel, gbc);

            gbc.gridx = 2;
            gbc.weightx = 1;
            add(sizeLabel, gbc);

            gbc.gridx = 3;
            gbc.weightx = 1;
            add(availableSpaceLabel, gbc);
        } else {
            nameLabel = createLabel(200);
            creationDateLabel = createLabel(120);
            modificationDateLabel = createLabel(120);
            typeLabel = createLabel(80);
            sizeLabel = createLabel(80);

            gbc.gridx = 0;
            gbc.weightx = 10;
            add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 5;
            add(creationDateLabel, gbc);

            gbc.gridx = 2;
            gbc.weightx = 5;
            add(modificationDateLabel, gbc);

            gbc.gridx = 3;
            gbc.weightx = 2;
            add(typeLabel, gbc);

            gbc.gridx = 4;
            gbc.weightx = 1;
            add(sizeLabel, gbc);
        }

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
        if (element instanceof VirtualDrive drive) {
            nameLabel.setText(drive.getName());
            nameLabel.setIcon(IconAdapter.getTranslucentIcon
                    (24, 24, element.getIcon(), isCut? 0.5f : 1.0f));
            typeLabel.setText(drive.getType().toString());
            sizeLabel.setText(FileSize.formatSize(drive.getSize()));
            availableSpaceLabel.setText(FileSize.formatSize(drive.getAvailableSpace()));
        } else {
            nameLabel.setText(element.getName());
            nameLabel.setIcon(IconAdapter.getTranslucentIcon
                    (24, 24, element.getIcon(), isCut? 0.5f : 1.0f));
            creationDateLabel.setText(element.getCreationDateString());
            modificationDateLabel.setText(element.getModificationDateString());
            typeLabel.setText(element.getType().toString());
            sizeLabel.setText(FileSize.formatSize(element.getSize()));
        }
        revalidate();
        repaint();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
    }
}
