package pe.edu.utp.aed.fileexplorer.model;

import pe.edu.utp.aed.fileexplorer.exceptions.SameNameException;
import pe.edu.utp.aed.fileexplorer.model.observers.DirectoryObserver;
import pe.edu.utp.aed.fileexplorer.model.observers.ElementObserver;
import pe.edu.utp.aed.fileexplorer.util.datastructures.HashTable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Directory extends Element implements ElementObserver {
    private final HashTable<String, Element> children;
    private final List<DirectoryObserver> observers;

    public Directory(String name, ElementType type, LocalDateTime creationDate) {
        super(name, type, creationDate);
        this.children = new HashTable<>();
        this.observers = new ArrayList<>();
    }

    public void addChild(Element child) {
        if (children.containsKey(child.getName())) throw new SameNameException(child, this);
        increaseSize(child.getSize());
        child.addObserver(this);
        children.put(child.getName(), child);
        updateModificationDate();
        notifyElementAdded(child);
        notifyElementUpdated();
    }

    public Element getChild(String childName) {
        return children.get(childName);
    }

    public void removeChild(Element child) {
        if (child == null) return;
        if (!children.containsKey(child.getName())) throw new IllegalArgumentException("No such child: " + child.getName());
        decreaseSize(child.getSize());
        child.removeObserver(this);
        children.remove(child.getName());
        updateModificationDate();
        notifyElementRemoved(child);
    }

    public void renameChild(Element child, String newName) {
        if (child == null) return;
        if (children.containsKey(newName)) throw new SameNameException(child, this);
        if (!children.containsKey(child.getName())) throw new IllegalArgumentException("There is no such child");
        children.remove(child.getName());
        child.setName(newName);
        children.put(newName, child);
        updateModificationDate();
    }

    public List<Element> getChildren() {
        return children.getValues();
    }

    public boolean containsChild(String childName) {
        return children.containsKey(childName);
    }

    public List<Element> search(String name) {
        List<Element> result = new ArrayList<>();
        search(this, name, result);
        return result;
    }

    private void search(Directory dir, String name, List<Element> list) {
        for (Element child : dir.getChildren()) {
            if (child.getName().equals(name)) list.add(child);
            if (child.isDirectory()) {
                search((Directory) child, name, list);
            }
        }
    }

    public void addObserver(DirectoryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DirectoryObserver observer) {
        observers.remove(observer);
    }

    public void notifyElementAdded(Element element) {
        for (DirectoryObserver observer : observers) {
            observer.elementAdded(element);
        }
    }

    public void notifyElementRemoved(Element element) {
        for (DirectoryObserver observer : observers) {
            observer.elementRemoved(element);
        }
    }

    @Override
    public void sizeChanged(long increase) {
        increaseSize(increase);
    }

    @Override
    public void elementUpdated() {

    }
}
