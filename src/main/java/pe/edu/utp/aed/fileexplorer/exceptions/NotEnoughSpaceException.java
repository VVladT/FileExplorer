package pe.edu.utp.aed.fileexplorer.exceptions;

import pe.edu.utp.aed.fileexplorer.model.VirtualDrive;

public class NotEnoughSpaceException extends RuntimeException {
    public NotEnoughSpaceException(VirtualDrive drive) {
        super("Not enough space in disk " + drive.getName());
    }
}
