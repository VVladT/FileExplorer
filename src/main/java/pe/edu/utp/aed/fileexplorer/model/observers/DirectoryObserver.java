package pe.edu.utp.aed.fileexplorer.model.observers;

import pe.edu.utp.aed.fileexplorer.model.Element;

public interface DirectoryObserver {
    void elementAdded(Element element);
    void elementRemoved(Element element);
}
