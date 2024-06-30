package pe.edu.utp.aed.fileexplorer.view.components;

import pe.edu.utp.aed.fileexplorer.model.Element;
import pe.edu.utp.aed.fileexplorer.model.observers.ElementObserver;

import javax.swing.*;
import java.awt.*;

public abstract class ElementCard extends JPanel implements ElementObserver {
    protected Element element;
    private boolean selected = false;
    protected boolean isCut = false;

    public ElementCard(Element element) {
        this.element = element;
        setLayout(new BorderLayout());
        init();
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    protected abstract void init();

    public abstract void refresh();

    @Override
    public void sizeChanged(long increase) {
        refresh();
    }

    @Override
    public void elementUpdated() {
        refresh();
    }

    public void hover() {
        setBackground(new Color(0xC7F9FF));
    }

    public void resetHover() {
        setBackground(null);
    }

    public void click() {
        selected = true;
        setBackground(new Color(0x9EF5FF));
        setBorder(BorderFactory.createLineBorder(new Color(0x00BACE)));
    }

    public void resetClick() {
        selected = false;
        setBackground(null);
        setBorder(null);
    }

    public void cut() {
        isCut = true;
        refresh();
    }

    public boolean isSelected() {
        return selected;
    }

    public void clear() {
        element.removeObserver(this);
    }
}
