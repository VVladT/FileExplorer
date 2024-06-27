package pe.edu.utp.aed.fileexplorer.controller;

import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.model.Element;

public class ElementBuffer {
    private Directory parent;
    private Element element;
    private Command command;

    public void copyElement(Element element) {
        this.element = element;
        command = Command.COPY;
    }

    public void cutElement(Directory parent, Element element) {
        this.element = element;
        this.parent = parent;
        command = Command.CUT;
    }

    public Element recoveryElement() {
        if (command == Command.COPY) return recoveryFromCopy();
        if (command == Command.CUT) return recoveryFromCut();

        return null;
    }

    private Element recoveryFromCopy() {
        return element.clone();
    }

    private Element recoveryFromCut() {
        try {
            parent.removeChild(element);
            Element e = element;
            element = null;
            return e;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isEmpty() {
        return element == null;
    }
}
