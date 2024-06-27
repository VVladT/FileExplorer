package pe.edu.utp.aed.fileexplorer.exceptions;

public class PathNotFoundException extends RuntimeException {
    public PathNotFoundException(String path) {
        super("Path " + path + " not found");
    }
}
