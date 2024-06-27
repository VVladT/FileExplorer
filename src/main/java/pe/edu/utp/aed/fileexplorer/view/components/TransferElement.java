package pe.edu.utp.aed.fileexplorer.view.components;

import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.util.IconAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TransferElement extends JPanel {
    private static final int PREF_W = 80;
    private static final int PREF_H = 80;
    private BufferedImage iconImage;
    private float alpha;
    private Element element;

    public TransferElement(Element element) {
        this.alpha = 0.6f;
        this.element = element;
        this.iconImage = iconToBufferedImage(IconAdapter.getScaledIcon(60, 60, element.getIcon()));
        setPreferredSize(new Dimension(PREF_W, PREF_H));
        setMaximumSize(getPreferredSize());
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private BufferedImage iconToBufferedImage(Icon icon) {
        BufferedImage bufferedImage = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = bufferedImage.createGraphics();
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();
        return bufferedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        if (iconImage != null) {
            int x = (getWidth() - iconImage.getWidth()) / 2;
            int y = (getHeight() - iconImage.getHeight()) / 2;
            g2d.drawImage(iconImage, x, y, this);
        }
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    public Element getElement() {
        return element;
    }
}
