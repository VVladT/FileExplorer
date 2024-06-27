package pe.edu.utp.aed.fileexplorer.exceptions;

import pe.edu.utp.aed.fileexplorer.model.Directory;
import pe.edu.utp.aed.fileexplorer.model.Element;

public class SameNameException extends RuntimeException {
    public SameNameException(Element child, Directory directory) {
        super(child.getType() + " with name " + child.getName() + " already exists in " + directory.getName());
    }
}
