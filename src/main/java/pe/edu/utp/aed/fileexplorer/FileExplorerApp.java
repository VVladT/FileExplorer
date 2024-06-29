package pe.edu.utp.aed.fileexplorer;

import pe.edu.utp.aed.fileexplorer.controller.ElementController;
import pe.edu.utp.aed.fileexplorer.model.VirtualFileSystem;
import pe.edu.utp.aed.fileexplorer.util.VFSBuilder;

public class FileExplorerApp {
    public static void main(String[] args) {
        VirtualFileSystem vfs = VFSBuilder.buildDefaultVFS();
        ElementController controller = new ElementController(vfs);
        controller.startApplication();
    }

}