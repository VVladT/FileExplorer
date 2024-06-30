package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.model.observers.DirectoryObserver;
import pe.edu.utp.aed.fileexplorer.model.observers.QuickAccessObserver;

import java.util.ArrayList;
import java.util.List;

public class QuickAccess implements DirectoryObserver {
    List<Element> elements;
    List<QuickAccessObserver> observers;

    public QuickAccess() {
        elements = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public QuickAccess(List<Element> elements) {
        this.elements = elements;
    }
    public void addElement(Element element) {
        elements.add(element);
        notifyElementAdded(element);
    }
    public void removeElement(Element element) {
        elements.remove(element);
        notifyElementRemoved(element);
    }

    public List<Element> getElements() {
        return elements;
    }

    public void addObserver(QuickAccessObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(QuickAccessObserver observer) {
        observers.remove(observer);
    }

    public boolean contains(Element element) {
        return elements.contains(element);
    }

    public void notifyElementAdded(Element element) {
        for (QuickAccessObserver observer : observers) {
            observer.elementAdded(element);
        }
    }

    public void notifyElementRemoved(Element element) {
        for (QuickAccessObserver observer : observers) {
            observer.elementRemoved(element);
        }
    }

    @Override
    public void elementAdded(Element element) {

    }

    @Override
    public void elementRemoved(Element element) {
        if (contains(element)) {
            removeElement(element);
            return;
        }

        if (element instanceof Directory dir) {
            List<Element> removedElements = new ArrayList<>();
            for (Element e : elements) {
                if (dir.containsElement(e)) {
                    removedElements.add(e);
                }
            }

            for (Element removedElement : removedElements) {
                removeElement(removedElement);
            }
        }
    }
}
