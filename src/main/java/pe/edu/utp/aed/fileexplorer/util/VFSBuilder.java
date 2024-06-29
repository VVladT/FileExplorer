package pe.edu.utp.aed.fileexplorer.util;

import pe.edu.utp.aed.fileexplorer.model.FileFolder;
import pe.edu.utp.aed.fileexplorer.model.TextFile;
import pe.edu.utp.aed.fileexplorer.model.VirtualDrive;
import pe.edu.utp.aed.fileexplorer.model.VirtualFileSystem;

import java.time.LocalDateTime;

public class VFSBuilder {
    private static final VirtualDrive defaultDrive =
            new VirtualDrive("Disco Principal", LocalDateTime.now(), 256 * FileSize.GB);

    private static final FileFolder[] defaultFolders = {
            new FileFolder("Usuario", LocalDateTime.now()),
            new FileFolder("Archivos", LocalDateTime.now()),
            new FileFolder("Carpetas", LocalDateTime.now()),
            new FileFolder("Descargas", LocalDateTime.now()),
    };

    private static final TextFile passwords = new TextFile("contraseñas", LocalDateTime.now());

    public static VirtualFileSystem buildDefaultVFS() {
        VirtualFileSystem vfs = new VirtualFileSystem();
        vfs.addDrive(defaultDrive);

        for (FileFolder defaultFolder : defaultFolders) {
            vfs.addElement(defaultDrive, defaultFolder);
            vfs.addElementToQuickAccess(defaultFolder);
        }

        vfs.addElement(defaultFolders[0], passwords);

        passwords.getContent().insert(0, "defaultPassword");
        passwords.updateSize();

        return vfs;
    }
}
