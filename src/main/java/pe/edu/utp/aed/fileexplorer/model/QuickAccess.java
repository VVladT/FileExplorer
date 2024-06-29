package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.model.observers.QuickAccessObserver;

import java.util.ArrayList;
import java.util.List;

public class QuickAccess {
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
}
