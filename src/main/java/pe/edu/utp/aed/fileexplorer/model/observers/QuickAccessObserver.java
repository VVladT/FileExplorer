package pe.edu.utp.aed.fileexplorer.model.observers;

import pe.edu.utp.aed.fileexplorer.model.Element;

public interface QuickAccessObserver {
    void elementAdded(Element element);
    void elementRemoved(Element element);
}
