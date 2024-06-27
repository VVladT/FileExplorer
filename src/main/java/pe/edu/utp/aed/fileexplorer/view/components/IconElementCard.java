package pe.edu.utp.aed.fileexplorer.view.components;

import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.awt.*;

public class IconElementCard extends ElementCard {
    private JLabel iconLabel, nameLabel;

    public IconElementCard(Element element) {
        super(element);
    }

    @Override
    protected void init() {
        iconLabel = new JLabel(IconAdapter.getScaledIcon(60, 60, element.getIcon()));
        nameLabel = new JLabel(element.getName());

        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        add(iconLabel, BorderLayout.CENTER);
        add(nameLabel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(100, 100));
        setMaximumSize(getPreferredSize());
    }

    @Override
    public void refresh() {
        iconLabel.setIcon(IconAdapter.getScaledIcon(60, 60, element.getIcon()));
        nameLabel.setText(element.getName());
    }
}
