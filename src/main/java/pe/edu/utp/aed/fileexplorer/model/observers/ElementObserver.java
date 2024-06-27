package pe.edu.utp.aed.fileexplorer.model.observers;

public interface ElementObserver {
    void sizeChanged(long increase);
    void elementUpdated();
}
